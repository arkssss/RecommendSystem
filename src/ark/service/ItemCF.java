package ark.service;

import ark.service.RecommedBase;
import ark.util.MyUnti;

import java.util.*;

/*
 * 基于物品的推荐
 * */
public class ItemCF extends RecommedBase {



    /* 获得最相似的K个值 */
    private final int K_ITEM = 10;

    /* --------------------------------- For Ranking Data ---------------------*/
    private ArrayList<String> allUser = getAllUser();

    private HashMap<String, ArrayList<String> > userRankingTable = getUserRankingTable() ;

    private ArrayList<String> allMovie = getAllMovie();

    private TreeMap<UserMovie, Double> userMovieRanking = getUserMovieRanking();

    private HashMap<String, ArrayList<String>> movieIntersection = getMovieIntersection();

    private HashMap<String, ArrayList<String>> movieAudience = getMovieAudience();

    /* --------------------------------- For Ranking Data ---------------------*/






    class movieSim implements Comparable{
        String movieId;
        double sim;

        public movieSim(String movieId, double sim){
            this.movieId = movieId;
            this.sim = sim;
        }

        @Override
        public int compareTo(Object o) {
            movieSim ms = (movieSim) o;

            return sim > ms.sim ? -1 : 1;
        }
    }

    public ItemCF(){
        System.out.println("Start ItemCF .....");
    }

    public TreeSet<RecommendItem> getRecommend(String userID){


        /* 判断有没有 user*/
        if(!allUser.contains(userID)) throw new RuntimeException("No Such User");

        /* 结果数据集 */
        TreeSet<RecommendItem> myRecommend = new TreeSet<>();

        /* 获得这个user 的看过的电影的名单 */
        ArrayList<String> historyMovue = userRankingTable.get(userID);
        System.out.println("Cal Processing ...");
        /* 遍历所有的电影 */
        int movieLen = allMovie.size();

        for(int i=0; i<movieLen ;i++){
            String CurtM = allMovie.get(i);
            if(!historyMovue.contains(CurtM)) {
                // 说明不是用户看过的电影
                /* 和CurtM 相似的 K 部电影 */
                TreeMap<String, Double> TopK = getSimKMovie(CurtM);
                double totMark = 0 ;
                /* 遍历 TopK*/
                for(String key:TopK.keySet()){

                    if(historyMovue.contains(key)){
                        // 包含 key, 计算
                        totMark += (TopK.get(key) * userMovieRanking.get(new UserMovie(userID, key)));

                    }
                }

                if(totMark != 0){
                    // 入推荐名单
                    myRecommend.add(new RecommendItem(CurtM, totMark));
                }
            }


        }

        System.out.println("Cal done ...");

        /* 结果数据集 */
        TreeSet<RecommendItem> finalRecommend = new TreeSet<>();
        Iterator it = myRecommend.iterator();
        int counter = 0;
        while(it.hasNext()){
            Object tmpR = it.next();
            RecommendItem tmpR2 = (RecommendItem) tmpR;
            if(counter >= K_ITEM) break;;
            finalRecommend.add(new RecommendItem(tmpR2.movieId, tmpR2.recommendMark));
            counter++;
        }
        return finalRecommend;
    }


    /* 获得个MovieId 类似的 K 个movie*/
    private TreeMap<String, Double> getSimKMovie(String movieId){
        /* 结果 */
        TreeMap<String, Double> res = new TreeMap<>();

        /* 获得 和 movieId 有用户交集的 电影 */
        ArrayList<String> intractMovie = movieIntersection.get(movieId);

        /* 自动排序 红黑树 */
        TreeSet<movieSim> mySet = new TreeSet<>();

        /* 计算相似相似度，返回前 K 名*/
        int len = intractMovie.size();
        for(int i =0 ;i<len; i++){
            String curtMovie = intractMovie.get(i);
            mySet.add(new movieSim(curtMovie, MyUnti.cosSim(movieAudience.get(movieId), movieIntersection.get(curtMovie))));
        }

        /* 遍历前K个mySet */
        Iterator<movieSim> it = mySet.iterator();
        int counter = 0;
        while(it.hasNext()){
            movieSim tmpms = it.next();
            if(counter >= K_ITEM) break;
            res.put(tmpms.movieId, tmpms.sim);
            counter ++ ;
        }
        return res;
    }





}
