package edu.thu.canteen.data;

import edu.thu.canteen.data.model.ActivityItem;
import edu.thu.canteen.data.model.AdminSubmission;
import edu.thu.canteen.data.model.Canteen;
import edu.thu.canteen.data.model.Dish;
import edu.thu.canteen.data.model.Review;
import edu.thu.canteen.data.model.SupportMessage;
import edu.thu.canteen.data.model.UserProfile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MockRepository {

    private static final List<Canteen> CANTEENS = Arrays.asList(
            new Canteen(1, "\u7d2b\u8346", "https://picsum.photos/seed/canteen1/800/600", "\u7d2b\u8346\u5b66\u751f\u516c\u5bd3\u533a", 4.8, 0, Arrays.asList("\u4e3b\u98df", "\u5b9e\u60e0")),
            new Canteen(2, "\u6843\u674e", "https://picsum.photos/seed/canteen2/800/600", "\u6e05\u534e\u5927\u5b66\u5b66\u751f\u533a", 4.6, 0, Arrays.asList("\u98ce\u5473", "\u591a\u6837")),
            new Canteen(3, "\u6e05\u82ac", "https://picsum.photos/seed/canteen3/800/600", "\u6e05\u534e\u5927\u5b66\u4e2d\u90e8", 4.5, 0, Arrays.asList("\u70ed\u95e8", "\u4e2d\u5fc3")),
            new Canteen(4, "\u7389\u6811", "", "\u897f\u5317\u751f\u6d3b\u533a", 4.2, 0, Arrays.asList("\u65e9\u9910", "\u4fbf\u6377")),
            new Canteen(5, "\u542c\u6d9b", "", "\u4e1c\u5317\u533a", 4.3, 0, Arrays.asList("\u5730\u65b9\u83dc", "\u591c\u5bb5")),
            new Canteen(6, "\u89c2\u7574", "", "\u5357\u90e8\u751f\u6d3b\u533a", 4.4, 0, Arrays.asList("\u70b9\u5fc3", "\u94c1\u677f")),
            new Canteen(7, "\u4e01\u9999", "", "\u4e01\u9999\u56ed", 4.1, 0, Arrays.asList("\u6c64\u54c1", "\u751c\u70b9"))
    );

    private static final List<Dish> DISHES = new ArrayList<>(Arrays.asList(
            new Dish(101, 1, 1, "\u7d2b\u8346\u98df\u5802", "\u5ddd\u6e58\u70ed\u83dc", 1, "\u9ebb\u5a46\u8c46\u8150", "https://picsum.photos/seed/dish101/800/600", 12.0, "\u8fa3\u5ea6\u8f83\u9ad8", 3, Arrays.asList("\u8fa3\u5473", "\u4e0b\u996d"), "\u70ed\u83dc"),
            new Dish(102, 1, 1, "\u7d2b\u8346\u98df\u5802", "\u9762\u98df\u7a97\u53e3", 1, "\u7ea2\u70e7\u725b\u8089\u9762", "https://picsum.photos/seed/dish102/800/600", 15.0, "\u6c64\u5e95\u504f\u6d53", 1, Arrays.asList("\u9762\u98df"), "\u9762\u98df"),
            new Dish(104, 2, 4, "\u6843\u674e\u98df\u5802", "\u76d6\u996d\u7a97\u53e3", 1, "\u756a\u8304\u6ed1\u86cb\u996d", "https://picsum.photos/seed/dish104/800/600", 13.0, "\u9178\u751c\u53e3", 0, Arrays.asList("\u6e05\u6de1"), "\u76d6\u996d")
    ));

    private static final UserProfile USER_PROFILE = new UserProfile("student07", "\u5c0f\u5f00", "\u6e05\u6de1", "", "USER");

    private static final List<Review> REVIEWS = Arrays.asList(
            new Review(201, 101, "Lin", 5, "\u8fa3\u5ea6\u591f", "", 10, 2, 0),
            new Review(202, 101, "Mei", 4, "\u6709\u70b9\u54b8", "", 5, 1, 1)
    );

    public static List<Canteen> getCanteens() { return CANTEENS; }
    public static Canteen getRecommendedCanteen() { return CANTEENS.get(0); }
    public static List<Dish> getFeaturedDishes(Canteen c) { return DISHES; }
    public static UserProfile getUserProfile() { return USER_PROFILE; }
    public static List<Review> getReviewsByDish(long dishId) {
        List<Review> filtered = new ArrayList<>();
        for (Review review : REVIEWS) {
            if (review.dishId == dishId) filtered.add(review);
        }
        return filtered;
    }
    public static List<Dish> getFavoriteDishes() { return DISHES; }
    public static List<ActivityItem> getRecentActivities() { return new ArrayList<>(); }
    public static List<AdminSubmission> getAdminSubmissions() { return new ArrayList<>(); }
    public static List<SupportMessage> getSupportMessages() { return new ArrayList<>(); }
    public static List<String> getCanteenTags() { return Arrays.asList("\u5168\u90e8", "\u4e3b\u98df", "\u98ce\u5473", "\u70ed\u95e8", "\u65e9\u9910", "\u5730\u65b9\u83dc", "\u751c\u70b9"); }
    public static List<String> getFloorFilters(Canteen c) { return Arrays.asList("\u5168\u90e8", "1\u697c", "2\u697c", "3\u697c"); }
    public static List<String> getWindowFilters(Canteen c) { return Arrays.asList("\u5168\u90e8\u7a97\u53e3", "\u5bb6\u5e38\u83dc", "\u9762\u98df", "\u5c0f\u5403"); }
    public static void addDish(Dish d) { DISHES.add(d); }
}
