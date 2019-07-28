import java.util.ArrayList;
import java.util.Iterator;

public class Links extends RowInCSV
                    implements Printable
{


    // read url
    private static final String read_url = "movie_data/links.csv";

    // My title name
    private static final String[] myTitle = {"movieId", "imdbId", "tmdbId"};

    // SingleLink Collection
    public ArrayList<SingleLink> LinksCollection = new ArrayList<>();


    /*
     *  通过 myUrl 构造 Links对象
     *
    */
    Links(){
        super(myTitle, read_url);

        for(Iterator<String[]> it = super.tmp_content.iterator(); it.hasNext();){
            String[] tmpArr = it.next();

            String movieId = "", imdbId = "", tmdbId = "";
            movieId = tmpArr[0];
            if(tmpArr.length == 2) {imdbId = tmpArr[1];  }
            if(tmpArr.length == 3) {imdbId = tmpArr[1]; tmdbId = tmpArr[2];  }

            LinksCollection.add(new SingleLink(movieId, imdbId, tmdbId));
        }

    }

    @Override
    public void printAll() {
        int len = LinksCollection.size();
        if(len == 0) {
            System.out.println();
            return;
        }

        for(Iterator<SingleLink> it = LinksCollection.iterator(); it.hasNext();){
            System.out.println(it.next());
        }
    }
}


/*
*
*   Just one link
* */
class SingleLink{

    //movieId,imdbId,tmdbId
    private String movieId;


    private String imdbId;


    private String tmdbId;


    // construst
    SingleLink(String movieId, String imdbId, String tmdbId){
        // upper 调用
        this.imdbId = imdbId;
        this.movieId = movieId;
        this.tmdbId = tmdbId;

    }

    @Override
    public String toString() {
        return "movieId : " + this.movieId + " imdbId : " + this.imdbId + " tmdbId : " + this.tmdbId;
    }
}

















