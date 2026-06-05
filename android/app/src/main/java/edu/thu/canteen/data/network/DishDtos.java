package edu.thu.canteen.data.network;

import edu.thu.canteen.data.model.Dish;
import edu.thu.canteen.data.model.Review;
import java.util.List;

public class DishDtos {
    public static class DishDetailResponse {
        public Dish base;
        public List<Review> reviews;
    }

    public static class ReviewRequest {
        public int rating;
        public String content;
        public String imageUrl;

        public ReviewRequest(int rating, String content, String imageUrl) {
            this.rating = rating;
            this.content = content;
            this.imageUrl = imageUrl;
        }
    }

    public static class VoteRequest {
        public int vote; // 1 for up, -1 for down, 0 for cancel

        public VoteRequest(int vote) {
            this.vote = vote;
        }
    }

    public static class FavoriteRequest {
        public long dishId;

        public FavoriteRequest(long dishId) {
            this.dishId = dishId;
        }
    }
}
