package ark.domain;

/*
 *
 *   Just one link
 * */
public class SingleLink{

    //movieId,imdbId,tmdbId
    private String movieId;


    private String imdbId;


    private String tmdbId;


    // construst
    public SingleLink(String movieId, String imdbId, String tmdbId){
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