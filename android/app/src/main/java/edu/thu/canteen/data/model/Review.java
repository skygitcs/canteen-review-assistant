package edu.thu.canteen.data.model;

public class Review {
    public final long id;
    public final long dishId;
    public final String userName;
    public final int rating;
    public final String content;
    public final String imageUrl;

    public Review(long id, long dishId, String userName, int rating, String content, String imageUrl) {
        this.id = id;
        this.dishId = dishId;
        this.userName = userName;
        this.rating = rating;
        this.content = content;
        this.imageUrl = imageUrl;
    }
}

