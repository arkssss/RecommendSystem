package ark.domain;

import java.util.ArrayList;
import ark.util.csvReader;
/*
 * 一个 CSV ROW 的共同 父类
 * */

public class RowInCSV {

    private ArrayList<String> Title = new ArrayList<>();

    public  ArrayList<String> tmp_title = new ArrayList<>();

    public  ArrayList<String[]> tmp_content = new ArrayList<>();

    public RowInCSV(String[] title, String read_url){
        System.out.println(read_url);
        int len = title.length;
        for(int i=0; i<len ;i++){ Title.add(title[i]); }

        // read my CSV
        csvReader.readCSV(read_url, tmp_title, tmp_content);

        // CSV 不匹配
        if(!checkIsMyTitle(tmp_title)){throw new RuntimeException("CSV 不匹配");}


    }


    // check title if in the rule
    boolean checkIsMyTitle(ArrayList<String> check_title){
        check_title.removeAll(Title);

        return check_title.size() == 0;
    }

}
