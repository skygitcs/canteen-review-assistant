package edu.thu.canteen.data.model;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import org.junit.Test;

public class ModelConstructorsTest {

    @Test
    public void simpleProfileAndSupportModelsKeepFields() {
        ActivityItem activity = new ActivityItem("Submitted a dish", "09:30");
        AdminSubmission adminSubmission = new AdminSubmission("Test Noodles", "North Canteen");
        SupportMessage supportMessage = new SupportMessage("student01", "Need help with favorites");
        UserProfile userProfile = new UserProfile(
                "student01",
                "Tester",
                "Light",
                "https://example.com/avatar.jpg",
                "USER"
        );

        assertEquals("Submitted a dish", activity.title);
        assertEquals("09:30", activity.time);
        assertEquals("Test Noodles", adminSubmission.name);
        assertEquals("North Canteen", adminSubmission.canteenName);
        assertEquals("student01", supportMessage.user);
        assertEquals("Need help with favorites", supportMessage.message);
        assertEquals("student01", userProfile.username);
        assertEquals("Tester", userProfile.nickname);
        assertEquals("Light", userProfile.tastePreference);
        assertEquals("https://example.com/avatar.jpg", userProfile.avatarUrl);
        assertEquals("USER", userProfile.role);
    }

    @Test
    public void canteenConstructorKeepsDisplayAndFilterFields() {
        Canteen canteen = new Canteen(
                3L,
                "Central Canteen",
                "https://example.com/canteen.jpg",
                "Campus center",
                4.6,
                2.0,
                Arrays.asList("popular", "noodles")
        );

        assertEquals(3L, canteen.id);
        assertEquals("Central Canteen", canteen.name);
        assertEquals("https://example.com/canteen.jpg", canteen.coverUrl);
        assertEquals("Campus center", canteen.address);
        assertEquals(4.6, canteen.avgRating, 0.001);
        assertEquals(2.0, canteen.crowdLevel, 0.001);
        assertEquals(Arrays.asList("popular", "noodles"), canteen.tags);
    }

    @Test
    public void reviewConstructorKeepsVotingFields() {
        Review review = new Review(
                88L,
                12L,
                "Reviewer",
                4,
                "Good value",
                "https://example.com/review.jpg",
                9,
                2,
                -1
        );

        assertEquals(88L, review.id);
        assertEquals(12L, review.dishId);
        assertEquals("Reviewer", review.userName);
        assertEquals(4, review.rating);
        assertEquals("Good value", review.content);
        assertEquals("https://example.com/review.jpg", review.imageUrl);
        assertEquals(9, review.upVotes);
        assertEquals(2, review.downVotes);
        assertEquals(-1, review.userVote);
    }

    @Test
    public void dishSubmissionConstructorKeepsAuditFields() {
        DishSubmission submission = new DishSubmission(
                501L,
                7L,
                1L,
                2L,
                "Rice Bowl",
                "Main Canteen",
                "Rice Window",
                2,
                "PENDING",
                "https://example.com/rice.jpg",
                1,
                13.5,
                "Large portion",
                "Submitter",
                Arrays.asList("rice", "value"),
                "Needs clearer image"
        );

        assertEquals(501L, submission.id);
        assertEquals(7L, submission.submitterId);
        assertEquals("Submitter", submission.submitterName);
        assertEquals(1L, submission.canteenId);
        assertEquals(2L, submission.windowId);
        assertEquals("Rice Bowl", submission.name);
        assertEquals("Main Canteen", submission.canteenName);
        assertEquals("Rice Window", submission.windowName);
        assertEquals(2, submission.floorNo);
        assertEquals("PENDING", submission.auditStatus);
        assertEquals("https://example.com/rice.jpg", submission.imageUrl);
        assertEquals(1, submission.spiceLevel);
        assertEquals(13.5, submission.price, 0.001);
        assertEquals("Large portion", submission.description);
        assertEquals(Arrays.asList("rice", "value"), submission.tags);
        assertEquals("Needs clearer image", submission.auditReason);
    }
}
