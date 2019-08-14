package ark.domain;

public class SingleMovie{

    public String movieId;

    public String title;

    public String genres;


    public SingleMovie(String movieId, String title, String genres){


        this.movieId = movieId;

        this.title = title;

        this.genres = genres;

    }

    @Override
    public String toString() {
        return "movieId : " + this.movieId + " title : " + this.title + " genres : " + this.genres;
    }
}
