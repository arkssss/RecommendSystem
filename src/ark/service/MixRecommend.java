package ark.service;

import java.util.Iterator;
import java.util.TreeSet;

/*
 * 基于混合式的推荐系统
 * 基本策略是
 * 规定一个  K-领域的 半径。
 * 如果 ： 半径里面的用户过小， 则用 基于内容推荐
 * 如果 ： 半径里面用户多， 则用 基于用户
 * */
public class MixRecommend extends RecommedBase{

    /* 如果邻居小于5 则使用基于内容 */
    private final int K_NEIB = 5;

    /* SIM 小于10的不做 UserCF 推荐 */
    private final int SIM_MIN = 10;


    MixRecommend(){

        System.out.println("Start MixRecommend ...... ");

    }

    /* 获得推荐 */
    public TreeSet<RecommendItem> getMixRecommemd(String userId){


        /* 获得 UserCF 的结果*/
        TreeSet<RecommendItem> UserCFRes = new UserCF().getUserRecommed(userId);

        Iterator<RecommendItem> it = UserCFRes.iterator();

        int counter = 0;
        boolean useUserCF = true;
        while (it.hasNext()){
            if(counter >= K_NEIB) {
                break;
            }
            RecommendItem ri = it.next();

            /* 如果没有达到 前 K 个的标准， 则跳出*/
            if(ri.recommendMark < SIM_MIN) {
                useUserCF = false;
                break;
            }
            counter ++;
        }

        /* 判断是UserCF 还是 Content-based */
        return useUserCF ? UserCFRes : new ContentBase().getContentRecommend(userId);
    }








}
