package ark.service;
/*
 * 所有推荐算法的夫类
 * */

import ark.dao.allData;
import ark.domain.SingleMovie;
import ark.domain.SingleRatings;

import java.util.*;

public class RecommedBase{

    /*
     * 内部类
     * */
    public class UserMovie implements Comparable{
        public String userId;
        public String movieId;

        UserMovie(String userId, String movieId){
            this.userId = userId;
            this.movieId = movieId;
        }

        @Override
        public int compareTo(Object o) {
            UserCF.UserMovie um = (UserCF.UserMovie) o;
            if(!um.userId.equals(this.userId)){
                return um.userId.compareTo(this.userId);
            }else {
                return um.movieId.compareTo(this.movieId);
            }
        }
    }


    public class RecommendItem implements Comparable{
        public String movieId;
        public Double recommendMark;

        RecommendItem(String movieId, Double recommendMark){
            this.movieId = movieId;
            this.recommendMark = recommendMark;
        }

        @Override
        public String toString() {
            return "Recommend Movie is : " + "\n" + getMovieTitle().get(this.movieId) + "\n" + " Mark is : "+this.recommendMark + "\n";
        }

        @Override
        public int compareTo(Object o) {
            RecommendItem ri = (RecommendItem) o;
            if(this.recommendMark > ri.recommendMark) return -1;
            else if(this.recommendMark < ri.recommendMark) return 1;
            else return 0;
        }
    }




    /* 获得一份数据集 */
    public allData myData;





    /* 存放所有的UserId */
    private static ArrayList<String> allUser  = new ArrayList<>();





    /* 存放所有的movieId*/
    private static ArrayList<String> allMovie = new ArrayList<>();






    /*
        保存用户评过分的电影Id
        UserId => [movieId1, movieId2, ...];
    */
    private static HashMap<String, ArrayList<String> > userRankingTable = new HashMap<>();





    /*
        用户 Id , 电影 Id , 对应出一个评分
        [UserId, MovieId] => Rank
    * */
    private static TreeMap<UserMovie, Double> userMovieRanking = new TreeMap<>();





    /*
        保存和用户 userId1 有相同电影的 用户集合
        可以在计算 SimK 的的时候简化计算
        userId => [userId1, userId2, ...]
    */
    private static HashMap<String, ArrayList<String> > userIntersection = new HashMap<>();





    /*
       保存 同时看过一个电影的用户集合 (ItemCF)
       movieId => [userId1, userId2, ...]
    */
    private static HashMap<String, ArrayList<String>> movieAudience = new HashMap<>();





    /*
        保存 一个和 电影 movieId1 至少有一个 相同用户的 其他movie
        便于 简化计算
        movieId => [movieId1, movieId2, ...]
     */
    private static HashMap<String, ArrayList<String>> movieIntersection = new HashMap<>();


    // ---------------------------------------- For movies 数据集
    /*
     *  保存一个电影对应的 tags
     *  movieId => [tag1, tag2, ...]
     * */
    // 注意初始化的时候 别写为 null
    private static HashMap<String, ArrayList<String>> movieTags = new HashMap<>();

    // 保存一个电影对应的 title
    private static HashMap<String, String > movieTitle = new HashMap<>();



    /* ------------------------------------- For Rankings 数据集 */

    public ArrayList<String> getAllUser(){

        if(allUser.size() == 0){
            analyseData();
        }
        return allUser;

    }

    public ArrayList<String> getAllMovie(){
        if(allMovie.size() == 0){
            analyseData();
        }
        return allMovie;
    }


    public HashMap<String, ArrayList<String> > getUserRankingTable(){
        if(userRankingTable.isEmpty()){
            analyseData();
        }
        return userRankingTable;
    }


    public TreeMap<UserMovie, Double> getUserMovieRanking(){

        if(userMovieRanking.isEmpty()){
            analyseData();
        }
        return userMovieRanking;
    }


    public HashMap<String, ArrayList<String> > getUserIntersection(){
        if(userIntersection.isEmpty()){
            analyseData();
        }
        return userIntersection;
    }



    public HashMap<String, ArrayList<String>> getMovieAudience(){
        if(movieAudience.isEmpty()){
            analyseData();
        }
        return movieAudience;
    }


    public HashMap<String, ArrayList<String>> getMovieIntersection(){
        if(movieIntersection.isEmpty()){
            analyseData();
        }
        return movieIntersection;
    }

    /* 工厂模式旧版本 --------------------------------- */


    RecommedBase(){

        myData = allData.getInstance();

    }

    /* Save movieTags*/
    /* Save MovieTitle*/
    private void initMovieTags(){

        /* get movie data */
        Iterator<SingleMovie> it = myData.movie.MoviesCollection.iterator();
        while(it.hasNext()){

            SingleMovie sm = it.next();
            String movieId = sm.movieId;
            String tmpMovieTitle = sm.title;

            ArrayList<String> myTags = new ArrayList<>();
            int lenS = sm.genres.length();
            String tag = "";
            for(int i=0; i<lenS; i++){
                if(i == (lenS - 1) && !tag.equals("")){
                    myTags.add(tag);
                }
                if(sm.genres.charAt(i) == '|') {
                    myTags.add(tag);
                    tag = "";
                    continue;
                }
                if(sm.genres.charAt(i) != ' ')
                    tag += sm.genres.charAt(i);
            }
            /* Save movieTags */
            movieTags.put(movieId, myTags);
            movieTitle.put(movieId, tmpMovieTitle);
        }


    }


    /* 获得movieTags */
    public HashMap<String, ArrayList<String>> getMovieTags(){

        if(movieTags.size() == 0){
            initMovieTags();
        }

        return movieTags;
    }


    /* 获得movieTitle */
    public HashMap<String, String> getMovieTitle(){

        if(movieTitle.size() == 0){
            initMovieTags();
        }

        return movieTitle;
    }




    /* Save userRankingTable */
    /* Save userMovieRanking */
    /* Save allUser */
    /* Save userIntersection*/
    /* Save movieAudience*/
    public void analyseData(){

        clearRankings();

        Iterator<SingleRatings> it = myData.ratings.RatingsCollection.iterator();
        while(it.hasNext()){
            String tmpUserId = it.next().userId;
            String movieId = it.next().movieId;
            String rank = it.next().rating;

            /* For userRankingTable*/
            if(!userRankingTable.containsKey(tmpUserId)) {
                // 新用户，放入新 ArrayList
                userRankingTable.put(tmpUserId, new ArrayList<>());
                allUser.add(tmpUserId);
            }
            userRankingTable.get(tmpUserId).add(movieId);
            userMovieRanking.put(new UserMovie(tmpUserId, movieId), new Double(rank));


            /* For movieAudience*/
            if(!movieAudience.containsKey(movieId)){
                // 新电影
                movieAudience.put(movieId, new ArrayList<>());
                allMovie.add(movieId);
            }
            /* 增加一个对应关系 */
            movieAudience.get(movieId).add(tmpUserId);
        }


        /* For userIntersection 实现直接用 ArrayList 求交即可 */
        int len = allUser.size();
        for(int i=0; i< len-1 ;i++){

            String User1 = allUser.get(i);
            userIntersection.put(User1, new ArrayList<>());

            for(int j=i+1; j<len ;j++){
                /* 获得 User1 & User2 */
                String User2 = allUser.get(j);

                /* 重新开批空间 防止改变userRankingTable */
                ArrayList<String> movies1 = new ArrayList<>(userRankingTable.get(User1));
                ArrayList<String> movies2 = new ArrayList<>(userRankingTable.get(User2));

                movies1.retainAll(movies2);
                if(movies1.size() != 0){
                    // 有交集
                    if(!userIntersection.containsKey(User1))
                        userIntersection.put(User1, new ArrayList<>());
                    if(!userIntersection.containsKey(User2))
                        userIntersection.put(User2, new ArrayList<>());
                    userIntersection.get(User1).add(User2);
                    userIntersection.get(User2).add(User1);
                }


            }
        }



        /* For movieIntersection */
        /* 遍历 HaspMap */
        int len2 = allMovie.size();
        for(int i=0; i<len2 - 1; i++){
            String curtMovie = allMovie.get(i);

            for(int j=i+1; j<len2; j++){
                String nextMovie = allMovie.get(j);

                ArrayList<String> tmpCurt = new ArrayList<>(movieAudience.get(curtMovie));
                ArrayList<String> tmpNext = new ArrayList<>(movieAudience.get(nextMovie));

                tmpCurt.retainAll(tmpNext);

                // 放入交集名单
                if(tmpCurt.size() != 0){
                    // 有交集
                    if(!movieIntersection.containsKey(curtMovie))
                        movieIntersection.put(curtMovie, new ArrayList<>());
                    if(!movieIntersection.containsKey(nextMovie))
                        movieIntersection.put(nextMovie, new ArrayList<>());
                    movieIntersection.get(curtMovie).add(nextMovie);
                    movieIntersection.get(nextMovie).add(curtMovie);
                }

            }



        }

    }





    /* 清除 和Ranking有关的数据 */
    private void clearRankings(){
        allUser.clear();
        allMovie.clear();
        userRankingTable.clear();
        userMovieRanking.clear();
        userIntersection.clear();
        movieAudience.clear();
        movieIntersection.clear();

    }


}

