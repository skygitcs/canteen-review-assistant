package edu.thu.canteen.data.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import edu.thu.canteen.data.model.Dish;
import edu.thu.canteen.data.model.Review;
import java.util.Arrays;
import org.junit.Test;

public class DishDtosTest {

    @Test
    public void requestConstructorsKeepSubmittedValues() {
        DishDtos.ReviewRequest review = new DishDtos.ReviewRequest(5, "Great taste", "review.jpg");
        DishDtos.VoteRequest vote = new DishDtos.VoteRequest(1);
        DishDtos.FavoriteRequest favorite = new DishDtos.FavoriteRequest(101L);
        DishDtos.DishSubmissionRequest submission = new DishDtos.DishSubmissionRequest(
                1L,
                2L,
                "New Dish",
                "dish.jpg",
                12.5,
                "Dish submitted by a student",
                2,
                Arrays.asList("light", "new")
        );

        assertEquals(5, review.rating);
        assertEquals("Great taste", review.content);
        assertEquals("review.jpg", review.imageUrl);
        assertEquals(1, vote.vote);
        assertEquals(101L, favorite.dishId);
        assertEquals(1L, submission.canteenId);
        assertEquals(2L, submission.windowId);
        assertEquals("New Dish", submission.name);
        assertEquals("dish.jpg", submission.imageUrl);
        assertEquals(12.5, submission.price, 0.001);
        assertEquals("Dish submitted by a student", submission.description);
        assertEquals(2, submission.spiceLevel);
        assertEquals(Arrays.asList("light", "new"), submission.tags);
    }

    @Test
    public void responseDtosKeepAssignedValues() {
        Dish dish = new Dish(101L, 1L, 1L, "Main", "Hot Dishes", 1, "Tofu",
                "", 12.0, "Spicy", 3, Arrays.asList("spicy"), "hot");
        Review review = new Review(201L, 101L, "Lin", 5, "Great", "", 3, 0, 1);

        DishDtos.DishDetailResponse detail = new DishDtos.DishDetailResponse();
        detail.base = dish;
        detail.reviews = Arrays.asList(review);

        DishDtos.UploadResponse upload = new DishDtos.UploadResponse();
        upload.url = "https://example.com/upload.jpg";

        assertSame(dish, detail.base);
        assertEquals(1, detail.reviews.size());
        assertSame(review, detail.reviews.get(0));
        assertEquals("https://example.com/upload.jpg", upload.url);
    }
}
