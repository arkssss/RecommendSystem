public class main {


    public static void main(String[] args){

        String UserID = "2";

        UserCF userCf = new UserCF();
        System.out.println("--------------------------------For UserCF-----------------------");
        System.out.println(userCf.getUserRecommed(UserID));
        System.out.println("--------------------------------For UserCF-----------------------");


        ItemCF itemCF = new ItemCF();
        System.out.println("--------------------------------For ItemCF-----------------------");
        System.out.println(itemCF.getRecommend(UserID));
        System.out.println("--------------------------------For ItemCf-----------------------");


        ContentBase CB = new ContentBase();
        System.out.println("--------------------------------For ContentBase-----------------------");
        System.out.println(CB.getContentRecommend(UserID));
        System.out.println("--------------------------------For ContentBase-----------------------");


        MixRecommend MR = new MixRecommend();
        System.out.println("--------------------------------For MixRecommend-----------------------");
        System.out.println(MR.getMixRecommemd(UserID));
        System.out.println("--------------------------------For MixRecommend-----------------------");
    }





}
