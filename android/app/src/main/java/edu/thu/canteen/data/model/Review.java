package edu.thu.canteen.data.model;

public class Review {
    public final long id;
    public final long dishId;
    public final String userName;
    public final int rating;
    public final String content;
    public final String imageUrl;
    public final int upVotes;
    public final int downVotes;
    public final int userVote; // 1 for up, -1 for down, 0 for none

    public Review(long id, long dishId, String userName, int rating, String content, String imageUrl, 
                  int upVotes, int downVotes, int userVote) {
        this.id = id;
        this.dishId = dishId;
        this.userName = userName;
        this.rating = rating;
        this.content = content;
        this.imageUrl = imageUrl;
        this.upVotes = upVotes;
        this.downVotes = downVotes;
        this.userVote = userVote;
    }
}

