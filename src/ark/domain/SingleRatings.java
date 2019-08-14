package ark.domain;

public class SingleRatings{
    //    userId,movieId,rating,timestamp
    public String userId;

    public String movieId;

    public String rating;

    public String timestamp;


    public SingleRatings(String userId, String movieId, String rating, String timestamp){

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