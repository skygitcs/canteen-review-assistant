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
import java.util.Collections;
import java.util.List;

public class MockRepository {
    private static final List<Canteen> CANTEENS = Arrays.asList(
            new Canteen(1, "Zijing Canteen", "https://picsum.photos/seed/canteen1/900/600",
                    "East Campus Road", 4.4, 2.6,
                    Arrays.asList("Spicy", "Value", "Noodles")),
            new Canteen(2, "Lotus Canteen", "https://picsum.photos/seed/canteen2/900/600",
                    "North Campus Plaza", 4.1, 3.4,
                    Arrays.asList("Light", "Rice", "Healthy")),
            new Canteen(3, "Library Food Hall", "https://picsum.photos/seed/canteen3/900/600",
                    "Library South Gate", 4.6, 1.8,
                    Arrays.asList("Cafe", "Dessert", "Coffee"))
    );

    private static final List<Dish> DISHES = Arrays.asList(
            new Dish(101, 1, "Zijing Canteen", "Hot Dishes", 1,
                    "Mapo Tofu", "https://picsum.photos/seed/dish101/800/600",
                    12.0, "Soft tofu with chili and peppercorn.",
                    Arrays.asList("Spicy", "Classic"), "Hot"),
            new Dish(102, 1, "Zijing Canteen", "Noodle Bar", 1,
                    "Beef Noodles", "https://picsum.photos/seed/dish102/800/600",
                    15.0, "Rich broth with tender beef slices.",
                    Arrays.asList("Noodles", "Savory"), "Noodles"),
            new Dish(103, 1, "Zijing Canteen", "Grill", 2,
                    "Grilled Chicken Bowl", "https://picsum.photos/seed/dish103/800/600",
                    18.0, "Charred chicken with garlic rice.",
                    Arrays.asList("Grill", "Protein"), "Grill"),
            new Dish(104, 2, "Lotus Canteen", "Rice Bowls", 1,
                    "Teriyaki Pork Rice", "https://picsum.photos/seed/dish104/800/600",
                    16.0, "Sweet glaze with roasted pork slices.",
                    Arrays.asList("Rice", "Sweet"), "Rice"),
            new Dish(105, 2, "Lotus Canteen", "Salad", 1,
                    "Chicken Salad", "https://picsum.photos/seed/dish105/800/600",
                    14.0, "Light greens with grilled chicken.",
                    Arrays.asList("Healthy", "Light"), "Salad"),
            new Dish(106, 3, "Library Food Hall", "Coffee", 1,
                    "Cold Brew", "https://picsum.photos/seed/dish106/800/600",
                    10.0, "Smooth cold brew with citrus notes.",
                    Arrays.asList("Coffee", "Iced"), "Drinks"),
            new Dish(107, 3, "Library Food Hall", "Bakery", 1,
                    "Chocolate Croissant", "https://picsum.photos/seed/dish107/800/600",
                    9.0, "Butter layers with dark chocolate.",
                    Arrays.asList("Dessert", "Baked"), "Bakery")
    );

    private static final List<Review> REVIEWS = Arrays.asList(
            new Review(201, 101, "Lin", 5, "Great spice balance and aroma.",
                    "https://picsum.photos/seed/review201/600/400"),
            new Review(202, 101, "Mei", 4, "A bit salty but tasty.",
                    "https://picsum.photos/seed/review202/600/400"),
            new Review(203, 104, "Jun", 4, "Comforting and filling.",
                    "https://picsum.photos/seed/review203/600/400")
    );

    private static final UserProfile USER_PROFILE = new UserProfile(
            "student07", "Kai", "Mild", "https://picsum.photos/seed/avatar/200/200"
    );

    private static final List<ActivityItem> ACTIVITIES = Arrays.asList(
            new ActivityItem("Reviewed Mapo Tofu", "2025-05-03"),
            new ActivityItem("Visited Zijing Canteen", "2025-05-02")
    );

    private static final List<AdminSubmission> SUBMISSIONS = Arrays.asList(
            new AdminSubmission("Spicy Fish Soup", "Zijing Canteen"),
            new AdminSubmission("Mango Pudding", "Library Food Hall")
    );

    private static final List<SupportMessage> SUPPORT = Collections.singletonList(
            new SupportMessage("student07", "How to update my avatar?")
    );

    public static List<String> getAnnouncements() {
        return Arrays.asList("Late night service updated", "New seasonal menu");
    }

    public static Canteen getRecommendedCanteen() {
        return CANTEENS.get(0);
    }

    public static List<Dish> getFeaturedDishes(Canteen canteen) {
        List<Dish> result = new ArrayList<>();
        for (Dish dish : DISHES) {
            if (dish.canteenId == canteen.id && result.size() < 3) {
                result.add(dish);
            }
        }
        return result;
    }

    public static List<Canteen> getCanteens() {
        return CANTEENS;
    }

    public static List<String> getCanteenTags() {
        List<String> tags = new ArrayList<>();
        tags.add("All");
        for (Canteen canteen : CANTEENS) {
            for (String tag : canteen.tags) {
                if (!tags.contains(tag)) {
                    tags.add(tag);
                }
            }
        }
        return tags;
    }

    public static Canteen getCanteenById(long id) {
        for (Canteen canteen : CANTEENS) {
            if (canteen.id == id) {
                return canteen;
            }
        }
        return CANTEENS.get(0);
    }

    public static List<String> getCategories(Canteen canteen) {
        List<String> categories = new ArrayList<>();
        categories.add("All");
        for (Dish dish : DISHES) {
            if (dish.canteenId == canteen.id && !categories.contains(dish.category)) {
                categories.add(dish.category);
            }
        }
        return categories;
    }

    public static List<Dish> getDishesByCanteen(Canteen canteen) {
        List<Dish> result = new ArrayList<>();
        for (Dish dish : DISHES) {
            if (dish.canteenId == canteen.id) {
                result.add(dish);
            }
        }
        return result;
    }

    public static Dish getDishById(long id) {
        for (Dish dish : DISHES) {
            if (dish.id == id) {
                return dish;
            }
        }
        return DISHES.get(0);
    }

    public static List<Review> getReviewsByDish(long dishId) {
        List<Review> result = new ArrayList<>();
        for (Review review : REVIEWS) {
            if (review.dishId == dishId) {
                result.add(review);
            }
        }
        return result;
    }

    public static UserProfile getUserProfile() {
        return USER_PROFILE;
    }

    public static List<ActivityItem> getRecentActivities() {
        return ACTIVITIES;
    }

    public static List<Dish> getFavoriteDishes() {
        List<Dish> result = new ArrayList<>();
        result.add(DISHES.get(0));
        result.add(DISHES.get(5));
        return result;
    }

    public static List<AdminSubmission> getAdminSubmissions() {
        return SUBMISSIONS;
    }

    public static List<SupportMessage> getSupportMessages() {
        return SUPPORT;
    }

    public static List<String> getFloorFilters(Canteen canteen) {
        return Arrays.asList("All", "1F", "2F");
    }

    public static List<String> getWindowFilters(Canteen canteen) {
        return Arrays.asList("All", "Hot Dishes", "Noodle Bar", "Grill");
    }
}

