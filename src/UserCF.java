/*
* 基于用户的协同过滤
* */
import java.util.*;


public class UserCF extends RecommedBase{

    class RecommendItem implements Comparable{
        public String movieId;
        public Double recommendMark;

        RecommendItem(String movieId, Double recommendMark){
            this.movieId = movieId;
            this.recommendMark = recommendMark;
        }

        @Override
        public String toString() {
            return "Recommend Movie is : " + this.movieId + " , Mark is : "+this.recommendMark;
        }

        @Override
        public int compareTo(Object o) {
            RecommendItem ri = (RecommendItem) o;
            return this.recommendMark > ri.recommendMark ? -1 : 1;
        }
    }



    class UserMovie implements Comparable{
        public String userId;
        public String movieId;

        UserMovie(String userId, String movieId){
            this.userId = userId;
            this.movieId = movieId;
        }

        @Override
        public int compareTo(Object o) {
            UserMovie um = (UserMovie) o;
            if(!um.userId.equals(this.userId)){
                return um.userId.compareTo(this.userId);
            }else {
                return um.movieId.compareTo(this.movieId);
            }
        }
    }



    /* Ranking Sim Structure*/
    /* For TreeSet to Sort */
    class RankSim implements Comparable{

        public String userId;
        public Double Sim;

        RankSim(String userId, Double sim){
            this.userId = userId;
            this.Sim = sim;
        }

        @Override
        public int compareTo(Object o) {
            /* 强制转型 */
            RankSim r = (RankSim) o;
            return this.Sim > r.Sim ? -1 : 1;
        }
    }




    /* 获得用户的临近K个用户 */
    private final int K = 10;


    /* 获得用户的 K 个推荐物品 */
    private final int K_ITEM = 10;



    /* 存放所有的UserId */
    private ArrayList<String> allUser  = new ArrayList<>();



    /*
        保存用户评过分的电影Id
        UserId => [movieId1, movieId2, ...];
    */
    private HashMap<String, ArrayList<String> > userRankingTable = new HashMap<>();



    /*
        用户 Id , 电影 Id , 对应出一个评分
        [UserId, MovieId] => Rank
    * */
    private TreeMap<UserMovie, Double> userMovieRanking = new TreeMap<>();


    /*
        保存和用户 userId1 有相同电影的 用户集合
        可以在计算 SimK 的的时候简化计算
        userId => [userId1, userId2, ...]
    */
    private HashMap<String, ArrayList<String> > userIntersection = new HashMap<>();




    /* Save userRankingTable*/
    private void saveUserRankingTable(){

        Iterator<SingleRatings> it = myData.ratings.RatingsCollection.iterator();
        while(it.hasNext()){
            String tmpUserId = it.next().userId;
            String movieId = it.next().movieId;
            String rank = it.next().rating;

            /* For userRankingTable*/
            if(!userRankingTable.containsKey(tmpUserId)) {
                // 新用户，放入新 ArrayList
                userRankingTable.put(tmpUserId, new ArrayList<String>());
                allUser.add(tmpUserId);
            }
            userRankingTable.get(tmpUserId).add(movieId);
            userMovieRanking.put(new UserMovie(tmpUserId, movieId), new Double(rank));

        }


        /* For userIntersection 实现直接用 ArrayList 求交即可 */
        int len = allUser.size();
        for(int i=0; i< len-1 ;i++){

            String User1 = allUser.get(i);
            userIntersection.put(User1, new ArrayList<String>());

            for(int j=i+1; j<len ;j++){
                /* 获得 User1 & User2 */
                String User2 = allUser.get(j);

                /* 重新开批空间 防止改变userRankingTable */
                ArrayList<String> movies1 = new ArrayList<>(userRankingTable.get(User1));
                ArrayList<String> movies2 = new ArrayList<>(userRankingTable.get(User2));

                movies1.retainAll(movies2);
                if(movies1.size() != 0){
                    // 有交集
                    userIntersection.get(User1).add(User2);
                }


            }
        }

    }




    /* init data */
    private void init(){

        saveUserRankingTable();

    }



    /* 构造函数 */
    UserCF(){
        System.out.println("User-based init....");
        init();
        System.out.println("User-based Start Work ....");
    }

    /*
        传递一个用户 Id
        获得这个用户的推荐物品
    */
    public TreeSet<RecommendItem> getUserRecommed(String userID){

        if(!allUser.contains(userID)) throw new RuntimeException("No Such User");

        ArrayList<RankSim> SimKUser = new ArrayList<>();
        getSimKUser(userID, SimKUser);
        /* SimKUser 为推荐的和userID相似的 K 个用户 */


        /* Start Recommend*/
        TreeSet<RecommendItem> recommendItem = new TreeSet<>();
        getRecommendItem(userID, SimKUser, recommendItem);

        /* All Recommend Items*/
        TreeSet<RecommendItem> finalItem = new TreeSet<>();
        Iterator it = recommendItem.iterator();
        int counter = 0;

        while(it.hasNext()){
            if(counter >= K_ITEM) break;
            Object o =  it.next();
            RecommendItem ri = (RecommendItem) o;
            finalItem.add(ri);
            counter ++;
        }

        return finalItem;
    }




    /*
    * 获得推荐的 K 个 Item
    * */
    private void getRecommendItem(String userId, ArrayList<RankSim> SimKUser, TreeSet<RecommendItem> recommendItem){
        /* SimKUser 为降序*/
        /* 先算出来, 我们需要对多少电影进行计算
        *  所有SimKUser 取并集， 再除去 userId 的 movies
        * */

        Iterator<RankSim> it = SimKUser.iterator();
        ArrayList<String> unionMovies = new ArrayList<>();
        while(it.hasNext()){
            RankSim tmpUser = it.next();
            unionMovies.addAll(userRankingTable.get(tmpUser.userId));
        }

        /* 除去userId 看过的电影*/
        unionMovies.removeAll(userRankingTable.get(userId));


        /* 得到最后需要遍历 unionMovies 求推荐指数 */
        /* Start Recommend*/
        System.out.println("Recommending, Plz Hold a second");


        Iterator<String> iter = unionMovies.iterator();
        int len = SimKUser.size();

        while(iter.hasNext()){
            double recommendMark = 0;
            String curtMovie = iter.next();
            /* For All Sim user*/
            for(int i=0; i<len ;i++){
                /* */
                String curtSimUser = SimKUser.get(i).userId;
                double curtSim = SimKUser.get(i).Sim;
                if(userMovieRanking.containsKey(new UserMovie(curtSimUser, curtMovie))){
                    // 有则 乘
                    recommendMark += (userMovieRanking.get(new UserMovie(curtSimUser, curtMovie)) * curtSim);
                }
            }

            recommendItem.add(new RecommendItem(curtMovie, recommendMark));
        }
    }


    /*
    * 获得和 userId 临近的 K 个用户 Id
    * */
    private void getSimKUser(String userId, ArrayList<RankSim> SimKUser){

        ArrayList<String> relatedUsers = userIntersection.get(userId);
        TreeSet<RankSim> ranking = new TreeSet<>();

         /* 遍历 */
        Iterator<String> it = relatedUsers.iterator();
        while(it.hasNext()){
            String curtUserId = it.next();
            // Compute Sim
            double sim = MyUnti.cosSim(userRankingTable.get(userId), userRankingTable.get(curtUserId));
            ranking.add(new RankSim(curtUserId, sim));
        }


        /* 取ranking 的前 K 个用户*/
        Iterator<RankSim> iter = ranking.iterator();
        int i = 0;
        while(iter.hasNext()){

            if(i >= K) break;
            SimKUser.add(iter.next());

            i++;

        }

    }


}
