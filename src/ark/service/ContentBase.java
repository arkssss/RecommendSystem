package ark.service;

import ark.service.RecommedBase;
import ark.util.MyUnti;

import java.util.*;

/*
 * 基于内容
 * */
public class ContentBase extends RecommedBase {

    /* 新版本数据初始化方法 */
    /* 利用这种方式获取可以 很好的节约内存 */
    /* 用到什么 加载什么*/
    class CRecommendItem extends RecommendItem{
        public String MoiveName;
        public String CommonTags;

        CRecommendItem(String movieId, Double recommendMark, String moiveName, String CommonTags){
            super(movieId, recommendMark);
            this.MoiveName = moiveName;
            this.CommonTags = CommonTags;
        }

        @Override
        public String toString() {
            return "MoiveName : " + MoiveName + "\n" + "CommonTags : " + CommonTags + "\n";
        }
    }

    /* 1.moiveTags */
    private HashMap<String, ArrayList<String>> myMovieTags = new HashMap<>(getMovieTags());

    /* 2.moiveTitle */
    private HashMap<String, String> myMovieTitle = getMovieTitle();

    /* 3. getUserRankingTable */
    private HashMap<String, ArrayList<String> > myUserRanking = getUserRankingTable();

    /*4 UserMovieRanking*/
    private TreeMap<UserMovie, Double> myUserMovieRanking = getUserMovieRanking();


    ContentBase(){
        System.out.println("Start Content-Base ...");
    }

    /* 策略就是，推荐给 用户评分最好的一部电影的 内容推荐*/
    public TreeSet<RecommendItem> getContentRecommend(String UserId){

        ArrayList<String> myRinkingTable = myUserRanking.get(UserId);

        /* 选择一个最大值 */
        Iterator<String> it = myRinkingTable.iterator();

        double maxRanking = 0;
        String MaxMovie = "";

        while (it.hasNext()){
            String curtMovie = it.next();
            double curtRanking = myUserMovieRanking.get(new UserMovie(UserId, curtMovie));
            if(curtRanking > maxRanking){
                // swap
                maxRanking = curtRanking;
                MaxMovie = curtMovie;
            }
        }

        if(MaxMovie.equals("")) throw new RuntimeException("Movie ID Error");

        return recommend(MaxMovie);
    }

    public TreeSet<RecommendItem> recommend(String movieID){
        if(!myMovieTags.containsKey(movieID)) throw new RuntimeException("No Such Movie : " + movieID);
        ArrayList<String> curtMoiveTags = myMovieTags.get(movieID);

        /* 推荐结果 */
        TreeSet<RecommendItem> res = new TreeSet<>();

        /* 删除自己 */
        myMovieTags.remove(movieID);
        for(String key : myMovieTags.keySet()){

            ArrayList<String> nextMoiveTags = myMovieTags.get(key);
            double sim = MyUnti.cosSim(curtMoiveTags, nextMoiveTags);
            if(sim != 0){
                nextMoiveTags.retainAll(curtMoiveTags);
                res.add(new CRecommendItem(key, sim, myMovieTitle.get(key), nextMoiveTags.toString()));
            }

        }
        return res;

    }





}
