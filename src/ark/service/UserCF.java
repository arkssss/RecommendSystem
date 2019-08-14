package ark.service;

/*
 * 基于用户的协同过滤
 * */

import ark.util.MyUnti;

import java.util.*;


public class UserCF extends RecommedBase{


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


    /* ------------------------------------------For Ranking Data */

    private ArrayList<String> allUser = getAllUser();

    private HashMap<String, ArrayList<String> > userRankingTable = getUserRankingTable() ;

    private TreeMap<UserMovie, Double> userMovieRanking = getUserMovieRanking();

    private HashMap<String, ArrayList<String> > userIntersection = getUserIntersection();

    /* ------------------------------------------For Ranking Data */








    /* 构造函数 */
    public UserCF(){
        System.out.println("Start User-based");
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
