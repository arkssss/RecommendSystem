package ark.dao;

/*
    存放所有的 读取数据
    懒汉单例模式 用单例是因为整个数据集只需要一份即可，节省空间
* */
public class allData {

    public Links links;
    public Movies movie;
    public Ratings ratings;

    private static allData datas = null;


    private allData() {
        System.out.println("Loading Data .......");
        links = new Links();
        movie = new Movies();
        ratings = new Ratings();
        System.out.println("Data Have Been Loaded .....");
    }

    // 获得实例
    public static allData getInstance(){

        if(datas == null){
            datas = new allData();
        }
        return datas;
    }

}

