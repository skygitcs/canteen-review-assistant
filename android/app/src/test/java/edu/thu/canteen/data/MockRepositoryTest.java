package edu.thu.canteen.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import edu.thu.canteen.data.model.Canteen;
import edu.thu.canteen.data.model.Dish;
import edu.thu.canteen.data.model.Review;
import edu.thu.canteen.data.model.UserProfile;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

public class MockRepositoryTest {

    @Test
    public void recommendedCanteenIsFirstCanteen() {
        List<Canteen> canteens = MockRepository.getCanteens();

        assertFalse(canteens.isEmpty());
        assertSame(canteens.get(0), MockRepository.getRecommendedCanteen());
    }

    @Test
    public void reviewsAreFilteredByDishId() {
        List<Review> reviews = MockRepository.getReviewsByDish(101L);

        assertFalse(reviews.isEmpty());
        for (Review review : reviews) {
            assertEquals(101L, review.dishId);
        }
        assertTrue(MockRepository.getReviewsByDish(102L).isEmpty());
    }

    @Test
    public void addedDishIsVisibleInFeaturedAndFavorites() {
        int featuredBefore = MockRepository.getFeaturedDishes(MockRepository.getRecommendedCanteen()).size();
        int favoritesBefore = MockRepository.getFavoriteDishes().size();
        Dish dish = new Dish(
                9901L,
                1L,
                1L,
                "Test Canteen",
                "Test Window",
                1,
                "Test Dish",
                "",
                9.9,
                "Dish used by local unit tests",
                1,
                Arrays.asList("test", "light"),
                "test"
        );

        MockRepository.addDish(dish);

        assertEquals(featuredBefore + 1, MockRepository.getFeaturedDishes(MockRepository.getRecommendedCanteen()).size());
        assertEquals(favoritesBefore + 1, MockRepository.getFavoriteDishes().size());
        assertSame(dish, MockRepository.getFeaturedDishes(MockRepository.getRecommendedCanteen()).get(featuredBefore));
    }

    @Test
    public void profileAndFilterOptionsExposePrototypeDefaults() {
        UserProfile profile = MockRepository.getUserProfile();
        Canteen recommended = MockRepository.getRecommendedCanteen();

        assertEquals("student07", profile.username);
        assertEquals("USER", profile.role);
        assertTrue(MockRepository.getCanteenTags().contains("全部"));
        assertTrue(MockRepository.getFloorFilters(recommended).contains("1楼"));
        assertTrue(MockRepository.getWindowFilters(recommended).contains("全部窗口"));
    }

    @Test
    public void activityAdminAndSupportListsAreSafeEmptyLists() {
        assertTrue(MockRepository.getRecentActivities().isEmpty());
        assertTrue(MockRepository.getAdminSubmissions().isEmpty());
        assertTrue(MockRepository.getSupportMessages().isEmpty());
    }
}
