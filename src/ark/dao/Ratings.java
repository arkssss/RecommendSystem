package ark.dao;

import ark.domain.RowInCSV;
import ark.domain.SingleRatings;
import ark.util.Printable;

import java.util.ArrayList;
import java.util.Iterator;

public class Ratings extends RowInCSV
        implements Printable
{

    // read url
    private static final String read_url = System.getProperty("evan.webapp")+ "Static/MovieLens/ratings.csv";

    // My title name
    private static final String[] myTitle = {"userId","movieId","rating", "timestamp"};

    // SingleRatings Collection
    public ArrayList<SingleRatings> RatingsCollection = new ArrayList<>();

    Ratings(){

        super(myTitle, read_url);

        for(Iterator<String[]> it = super.tmp_content.iterator(); it.hasNext();){
            String[] tmpArr = it.next();
            String[] inputArr = new String[myTitle.length];

            for (int i=0;i<myTitle.length; i++) {
                if(i >= tmpArr.length) inputArr[i] = "";
                else inputArr[i] = tmpArr[i];
            }

            RatingsCollection.add(new SingleRatings(inputArr[0], inputArr[1], inputArr[2], inputArr[3]));
        }

    }

    @Override
    public void printAll() {
        int len = RatingsCollection.size();
        if(len == 0) {
            System.out.println();
            return;
        }

        for(Iterator<SingleRatings> it = RatingsCollection.iterator(); it.hasNext();){
            System.out.println(it.next());
        }


    }


}



