import java.util.ArrayList;
import java.util.Iterator;

public class Ratings extends RowInCSV
                        implements Printable
{

    // read url
    private static final String read_url = "movie_data/ratings.csv";

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



class SingleRatings{
//    userId,movieId,rating,timestamp
    public String userId;

    public String movieId;

    public String rating;

    public String timestamp;


    SingleRatings(String userId, String movieId, String rating, String timestamp){

        this.userId = userId;
        this.movieId = movieId;
        this.rating = rating;
        this.timestamp = timestamp;

    }

    @Override
    public String toString() {
        return "userId :" + userId + " movieid : " + movieId + " rating : " + rating + " timestamp: " + timestamp;
    }
}