import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;


public class csvReader {

    /*
    *   return
    *   @ ArrayList<String> Title 为 CSV 的 title
    *   @ ArrayList<String[]> Content 为 CSV 的 内容
    */
    public static void readCSV(String url,  ArrayList<String> Title, ArrayList<String[]> Content){


        FileReader fr = null;

        // read file
        try {
             fr = new FileReader(url);
            // 读取成功
            BufferedReader buffr = new BufferedReader(fr);

            //Title
            String titleStr = buffr.readLine();
            String[] titleArr = titleStr.split(",");

            // add
            for(int i=0; i<titleArr.length ;i++){Title.add(titleArr[i]);}

            //Content
            int len = titleArr.length;
            String tmpContentStr;

            // add content
            while((tmpContentStr = buffr.readLine()) != null){
                String[] tmpContentArr = tmpContentStr.split(",");
                Content.add(tmpContentArr);
            }


        }catch (FileNotFoundException e){
            System.out.println("file not found");
            return;
        }catch (IOException e){
            System.out.println("Read Fail");
        }

        finally {
            if(fr != null){
            try {
                fr.close();
            }catch (IOException e){
                System.out.println("Close Fail");
                }
            }
        }

    }


}
