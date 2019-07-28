import java.util.ArrayList;

// 工具类
public class MyUnti {

     /*
        皮尔斯相识度
        返回 皮尔斯 相似度
     */
    static double pearsonSim(ArrayList<Integer> x, ArrayList<Integer> y){

        int lenX = x.size();
        int lenY = y.size();

        if(lenX != lenY) throw new RuntimeException("vector length must be the same");
        if(lenX == 0)    throw new RuntimeException("Empty Vector");

        double varX=0, varY=0, expX=0, expY=0, expXY=0;


        // get variance
        for(int i=0; i<lenX; i++){
            int xValue = x.get(i);
            int yValue = y.get(i);

            // Exp
            expX += xValue;
            expY += yValue;
            expXY+= (xValue * yValue);

            // var
            varX += (xValue * xValue);
            varY += (yValue * yValue);
        }
        varX = Math.sqrt(varX);
        varY = Math.sqrt(varY);

        return (expXY - expX*expY) / (varX * varY);
    }




    /*
        余弦相似度
        余弦形似度不管 Rank, 只涉及用户的观看过的想同的 电影的数目
        计算方法如下 ：
        Nums(xMovies 交 yMovies) / Sqrt(Nums(xMovies) * Nums(yMovies))
    */
    static double cosSim(ArrayList<String> xMovies, ArrayList<String> yMovies){

        int lenx = xMovies.size();
        int leny = yMovies.size();

        if(lenx == 0 || leny == 0){
            return 0;
        }

        ArrayList<String> copyxMovies = new ArrayList<>(xMovies);
        copyxMovies.retainAll(yMovies);
        int sameNumber = yMovies.size();

        return sameNumber / (Math.sqrt(lenx) * Math.sqrt(leny));

    }


}
