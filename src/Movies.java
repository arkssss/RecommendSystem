import java.util.ArrayList;
import java.util.Iterator;


public class Movies extends RowInCSV
                    implements Printable
{

    // read url
    private static final String read_url = "movie_data/movies.csv";

    // My title name
    private static final String[] myTitle = {"movieId","title","genres"};

    // SingleLink Collection
    public ArrayList<SingleMovie> MoviesCollection = new ArrayList<>();

    Movies(){

        super(myTitle, read_url);

        for(Iterator<String[]> it = super.tmp_content.iterator(); it.hasNext();){
            String[] tmpArr = it.next();
            String[] inputArr = new String[myTitle.length];

            //
            int len = tmpArr.length;
            for(int i=0; i<len ; i++){

                if(i == 0) inputArr[i] = tmpArr[i];
                else if(i == len -1) inputArr[2] = tmpArr[i];
                else{
                    inputArr[1] = inputArr[1] == null? tmpArr[i] : inputArr[1] + tmpArr[i];
                }

            }
            MoviesCollection.add(new SingleMovie(inputArr[0], inputArr[1], inputArr[2]));
        }

    }

    @Override
    public void printAll() {
        int len = MoviesCollection.size();
        if(len == 0) {
            System.out.println();
            return;
        }

        for(Iterator<SingleMovie> it = MoviesCollection.iterator(); it.hasNext();){
            System.out.println(it.next());
        }


    }
}


class SingleMovie{

    public String movieId;

    public String title;

    public String genres;


    SingleMovie(String movieId, String title, String genres){


        this.movieId = movieId;

        this.title = title;

        this.genres = genres;

    }

    @Override
    public String toString() {
        return "movieId : " + this.movieId + " title : " + this.title + " genres : " + this.genres;
    }
}