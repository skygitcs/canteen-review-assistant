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
    private static final String ALL = "\u5168\u90e8";
    private static final String ALL_WINDOWS = "\u5168\u90e8\u7a97\u53e3";

    // Standard mock image URLs to ensure clicking/zooming works
    private static final String IMG_CANTEEN = "https://picsum.photos/seed/canteen1/800/600";
    private static final String IMG_DISH = "https://picsum.photos/seed/dish1/800/600";

    private static final List<Canteen> CANTEENS = Arrays.asList(
            new Canteen(1, "\u7d2b\u8346\u98df\u5802", "https://picsum.photos/seed/thuc1/800/600", "\u7d2b\u8346\u5b66\u751f\u516c\u5bd3\u533a", 4.4, 2.6,
                    Arrays.asList("\u8fa3\u5473", "\u9762\u98df", "\u6027\u4ef7\u6bd4")),
            new Canteen(2, "\u6843\u674e\u98df\u5802", "https://picsum.photos/seed/thuc2/800/600", "\u6559\u5b66\u533a\u4e3b\u5e72\u9053", 4.1, 3.4,
                    Arrays.asList("\u6e05\u6de1", "\u7c73\u996d", "\u5065\u5eb7")),
            new Canteen(3, "\u6e05\u82ac\u98df\u5802", "https://picsum.photos/seed/thuc3/800/600", "\u56fe\u4e66\u9986\u5357\u4fa7", 4.6, 1.8,
                    Arrays.asList("\u751c\u54c1", "\u5496\u5561", "\u8f7b\u98df")),
            new Canteen(4, "\u7389\u6811\u98df\u5802", "https://picsum.photos/seed/thuc4/800/600", "\u4e1c\u533a\u751f\u6d3b\u533a", 4.3, 2.0,
                    Arrays.asList("\u5feb\u9910", "\u5b9e\u60e0", "\u65e9\u9910")),
            new Canteen(5, "\u542c\u6d9b\u98df\u5802", "https://picsum.photos/seed/thuc5/800/600", "\u6e56\u7554\u5bbf\u820d\u533a", 4.2, 2.8,
                    Arrays.asList("\u8fa3\u5473", "\u5730\u65b9\u83dc", "\u591c\u5bb5")),
            new Canteen(6, "\u89c2\u7574\u98df\u5802", "https://picsum.photos/seed/thuc6/800/600", "\u897f\u533a\u8fd0\u52a8\u573a\u65c1", 4.5, 3.0,
                    Arrays.asList("\u9762\u98df", "\u86cb\u767d\u8d28", "\u70ed\u83dc")),
            new Canteen(7, "\u4e01\u9999\u98df\u5802", "https://picsum.photos/seed/thuc7/800/600", "\u533b\u5b66\u697c\u9644\u8fd1", 4.0, 1.5,
                    Arrays.asList("\u6e05\u6de1", "\u5065\u5eb7", "\u6c64\u54c1"))
    );

    private static final List<Dish> DISHES = new ArrayList<>(Arrays.asList(
            new Dish(101, 1, 7, "\u7d2b\u8346\u98df\u5802", "\u5ddd\u6e58\u70ed\u83dc", 1,
                    "\u9ebb\u5a46\u8c46\u8150", "https://picsum.photos/seed/dish101/800/600", 12.0, "\u8fa3\u5ea6\u8f83\u9ad8\uff0c\u4e0b\u996d\u7a33\u5b9a\u3002",
                    Arrays.asList("\u8fa3\u5473", "\u7ecf\u5178", "\u4e0b\u996d"), "\u70ed\u83dc"),
            new Dish(102, 1, 7, "\u7d2b\u8346\u98df\u5802", "\u9762\u98df\u7a97\u53e3", 1,
                    "\u7ea2\u70e7\u725b\u8089\u9762", "https://picsum.photos/seed/dish102/800/600", 15.0, "\u6c64\u5e95\u504f\u6d53\uff0c\u725b\u8089\u5206\u91cf\u7a33\u5b9a\u3002",
                    Arrays.asList("\u9762\u98df", "\u54b8\u9c9c", "\u70ed\u6c64"), "\u9762\u98df"),
            new Dish(103, 1, 8, "\u7d2b\u8346\u98df\u5802", "\u70e4\u76d8\u7a97\u53e3", 2,
                    "\u7167\u70e7\u9e21\u817f\u996d", "https://picsum.photos/seed/dish103/800/600", 18.0, "\u9e21\u817f\u7126\u9999\uff0c\u914d\u83dc\u6e05\u723d\u3002",
                    Arrays.asList("\u7c73\u996d", "\u86cb\u767d\u8d28", "\u5fae\u751c"), "\u76d6\u996d"),
            new Dish(104, 2, 7, "\u6843\u674e\u98df\u5802", "\u76d6\u996d\u7a97\u53e3", 1,
                    "\u756a\u8304\u6ed1\u86cb\u996d", "https://picsum.photos/seed/dish104/800/600", 13.0, "\u9178\u751c\u53e3\uff0c\u6cb9\u76d0\u8f83\u8f7b\u3002",
                    Arrays.asList("\u6e05\u6de1", "\u7c73\u996d", "\u5bb6\u5e38"), "\u76d6\u996d"),
            new Dish(105, 2, 8, "\u6843\u674e\u98df\u5802", "\u8f7b\u98df\u7a97\u53e3", 1,
                    "\u9e21\u80f8\u6c99\u62c9", "https://picsum.photos/seed/dish105/800/600", 14.0, "\u852c\u83dc\u6bd4\u4f8b\u9ad8\uff0c\u9002\u5408\u60f3\u5403\u8f7b\u4e00\u70b9\u7684\u65f6\u5019\u3002",
                    Arrays.asList("\u5065\u5eb7", "\u6e05\u6de1", "\u86cb\u767d\u8d28"), "\u8f7b\u98df"),
            new Dish(106, 3, 7, "\u6e05\u82ac\u98df\u5802", "\u5496\u5561\u7a97\u53e3", 1,
                    "\u51b7\u8403\u5496\u5561", "https://picsum.photos/seed/dish106/800/600", 10.0, "\u53e3\u611f\u987a\u6ed1\uff0c\u9002\u5408\u4e0b\u5348\u8bfe\u524d\u63d0\u795e\u3002",
                    Arrays.asList("\u5496\u5561", "\u51b0\u996e", "\u5fae\u82e6"), "\u996e\u54c1"),
            new Dish(107, 3, 8, "\u6e05\u82ac\u98df\u5802", "\u70d8\u7119\u7a97\u53e3", 1,
                    "\u5de7\u514b\u529b\u53ef\u9882", "https://picsum.photos/seed/dish107/800/600", 9.0, "\u9ec4\u6cb9\u9999\u660e\u663e\uff0c\u751c\u5ea6\u4e2d\u7b49\u3002",
                    Arrays.asList("\u751c\u54c1", "\u70d8\u7119", "\u5fae\u751c"), "\u70d8\u7119"),
            new Dish(108, 4, 7, "\u7389\u6811\u98df\u5802", "\u65e9\u9910\u7a97\u53e3", 1,
                    "\u9c9c\u8089\u5305\u5957\u9910", "https://picsum.photos/seed/dish108/800/600", 8.0, "\u51fa\u9910\u5feb\uff0c\u65e9\u516b\u524d\u6bd4\u8f83\u7a33\u3002",
                    Arrays.asList("\u65e9\u9910", "\u5b9e\u60e0", "\u70ed\u4e4e"), "\u65e9\u9910"),
            new Dish(109, 5, 7, "\u542c\u6d9b\u98df\u5802", "\u5730\u65b9\u83dc\u7a97\u53e3", 1,
                    "\u9999\u8fa3\u9c7c\u7247", "https://picsum.photos/seed/dish109/800/600", 20.0, "\u9c7c\u7247\u5ae9\uff0c\u8fa3\u5ea6\u504f\u9ad8\u3002",
                    Arrays.asList("\u8fa3\u5473", "\u5730\u65b9\u83dc", "\u86cb\u767d\u8d28"), "\u70ed\u83dc"),
            new Dish(110, 6, 7, "\u89c2\u7574\u98df\u5802", "\u94c1\u677f\u7a97\u53e3", 2,
                    "\u9ed1\u6912\u725b\u67f3\u996d", "https://picsum.photos/seed/dish110/800/600", 18.0, "\u9ed1\u6912\u5473\u91cd\uff0c\u9002\u5408\u8fd0\u52a8\u540e\u8865\u80fd\u91cf\u3002",
                    Arrays.asList("\u86cb\u767d\u8d28", "\u7c73\u996d", "\u54b8\u9c9c"), "\u76d6\u996d"),
            new Dish(111, 7, 7, "\u4e01\u9999\u98df\u5802", "\u6c64\u54c1\u7a97\u53e3", 1,
                    "\u83cc\u83c7\u9e21\u6c64\u9762", "https://picsum.photos/seed/dish111/800/600", 12.0, "\u6c64\u5473\u6e05\uff0c\u9002\u5408\u80c3\u53e3\u4e00\u822c\u7684\u65f6\u5019\u3002",
                    Arrays.asList("\u6e05\u6de1", "\u6c64\u54c1", "\u9762\u98df"), "\u6c64\u9762"),
            new Dish(112, 2, 7, "\u6843\u674e\u98df\u5802", "\u5c0f\u7092\u7a97\u53e3", 2,
                    "\u897f\u84dd\u82b1\u725b\u8089", "https://picsum.photos/seed/dish112/800/600", 17.0, "\u852c\u83dc\u548c\u725b\u8089\u6bd4\u4f8b\u5747\u8861\uff0c\u53e3\u5473\u4e0d\u91cd\u3002",
                    Arrays.asList("\u6e05\u6de1", "\u86cb\u767d\u8d28", "\u70ed\u83dc"), "\u70ed\u83dc"),
            new Dish(113, 3, 8, "\u6e05\u82ac\u98df\u5802", "\u8f7b\u98df\u7a97\u53e3", 2,
                    "\u9c9c\u867e\u725b\u6cb9\u679c\u5377", "https://picsum.photos/seed/dish113/800/600", 16.0, "\u53e3\u611f\u6e05\u723d\uff0c\u9002\u5408\u4e0d\u60f3\u5403\u6cb9\u7684\u65f6\u5019\u3002",
                    Arrays.asList("\u8f7b\u98df", "\u5065\u5eb7", "\u86cb\u767d\u8d28"), "\u8f7b\u98df"),
            new Dish(114, 4, 8, "\u7389\u6811\u98df\u5802", "\u7c89\u9762\u7a97\u53e3", 1,
                    "\u9178\u8fa3\u7c89", "https://picsum.photos/seed/dish114/800/600", 11.0, "\u9178\u5473\u660e\u663e\uff0c\u8fa3\u5ea6\u4e2d\u7b49\u3002",
                    Arrays.asList("\u8fa3\u5473", "\u9762\u98df", "\u5feb\u9910"), "\u9762\u98df"),
            new Dish(115, 4, 7, "\u7389\u6811\u98df\u5802", "\u65e9\u9910\u7a97\u53e3", 1,
                    "\u9e21\u86cb\u704c\u997c", "https://picsum.photos/seed/dish115/800/600", 7.0, "\u4ef7\u683c\u4fbf\u5b9c\uff0c\u51fa\u9910\u5feb\u3002",
                    Arrays.asList("\u65e9\u9910", "\u5b9e\u60e0", "\u5feb\u9910"), "\u65e9\u9910"),
            new Dish(116, 5, 8, "\u542c\u6d9b\u98df\u5802", "\u591c\u5bb5\u7a97\u53e3", 2,
                    "\u9999\u8fa3\u62cc\u9762", "https://picsum.photos/seed/dish116/800/600", 13.0, "\u591c\u5bb5\u65f6\u6bb5\u6bd4\u8f83\u53d7\u6b22\u8fce\uff0c\u8fa3\u5473\u660e\u663e\u3002",
                    Arrays.asList("\u8fa3\u5473", "\u591c\u5bb5", "\u9762\u98df"), "\u9762\u98df"),
            new Dish(117, 5, 8, "\u542c\u6d9b\u98df\u5802", "\u6c64\u7c89\u7a97\u53e3", 2,
                    "\u756a\u8304\u9c7c\u4e38\u7c89", "https://picsum.photos/seed/dish117/800/600", 14.0, "\u756a\u8304\u6c64\u5e95\u504f\u6e05\u723d\uff0c\u4e0d\u592a\u8fa3\u3002",
                    Arrays.asList("\u6c64\u54c1", "\u6e05\u6de1", "\u9762\u98df"), "\u6c64\u9762"),
            new Dish(118, 6, 8, "\u89c2\u7574\u98df\u5802", "\u5065\u8eab\u7a97\u53e3", 1,
                    "\u9e21\u80f8\u7389\u7c73\u996d", "https://picsum.photos/seed/dish118/800/600", 16.0, "\u86cb\u767d\u8d28\u8f83\u9ad8\uff0c\u8c03\u5473\u6bd4\u8f83\u7b80\u5355\u3002",
                    Arrays.asList("\u5065\u5eb7", "\u86cb\u767d\u8d28", "\u7c73\u996d"), "\u76d6\u996d"),
            new Dish(119, 6, 7, "\u89c2\u7574\u98df\u5802", "\u70ed\u83dc\u7a97\u53e3", 1,
                    "\u571f\u8c46\u70e7\u725b\u8089", "https://picsum.photos/seed/dish119/800/600", 18.0, "\u725b\u8089\u548c\u571f\u8c46\u90fd\u6bd4\u8f83\u5165\u5473\u3002",
                    Arrays.asList("\u70ed\u83dc", "\u86cb\u767d\u8d28", "\u4e0b\u996d"), "\u70ed\u83dc"),
            new Dish(120, 7, 8, "\u4e01\u9999\u98df\u5802", "\u7ca5\u70b9\u7a97\u53e3", 1,
                    "\u76ae\u86cb\u7626\u8089\u7ca5", "https://picsum.photos/seed/dish120/800/600", 8.0, "\u6e29\u70ed\u987a\u53e3\uff0c\u9002\u5408\u65e9\u4e0a\u6216\u665a\u4e0a\u3002",
                    Arrays.asList("\u6e05\u6de1", "\u65e9\u9910", "\u5b9e\u60e0"), "\u65e9\u9910"),
            new Dish(121, 7, 8, "\u4e01\u9999\u98df\u5802", "\u7d20\u98df\u7a97\u53e3", 2,
                    "\u9999\u83c7\u9752\u83dc\u996d", "https://picsum.photos/seed/dish121/800/600", 10.0, "\u7d20\u83dc\u6bd4\u8f83\u591a\uff0c\u53e3\u5473\u8f7b\u3002",
                    Arrays.asList("\u6e05\u6de1", "\u5065\u5eb7", "\u7c73\u996d"), "\u76d6\u996d")
    ));

    private static final List<Review> REVIEWS = Arrays.asList(
            new Review(201, 101, "Lin", 5, "\u8fa3\u5ea6\u591f\uff0c\u8c46\u8150\u53e3\u611f\u4e5f\u4e0d\u9519\u3002", "", 0, 0, 0),
            new Review(202, 101, "Mei", 4, "\u6709\u70b9\u54b8\uff0c\u4f46\u6574\u4f53\u4e0b\u996d\u3002", "", 0, 0, 0),
            new Review(203, 104, "Jun", 4, "\u756a\u8304\u5473\u5f88\u8db3\uff0c\u9002\u5408\u4e0d\u60f3\u5403\u91cd\u53e3\u7684\u65f6\u5019\u3002", "", 0, 0, 0)
    );

    private static final UserProfile USER_PROFILE = new UserProfile("student07", "\u5c0f\u5f00", "\u6e05\u6de1", "");

    private static final List<ActivityItem> ACTIVITIES = Arrays.asList(
            new ActivityItem("\u8bc4\u4ef7\u4e86\u9ebb\u5a46\u8c46\u8150", "2026-05-03"),
            new ActivityItem("\u53bb\u8fc7\u7d2b\u8346\u98df\u5802", "2026-05-02")
    );

    private static final List<AdminSubmission> SUBMISSIONS = new ArrayList<>(Arrays.asList(
            new AdminSubmission("\u9999\u8fa3\u9c7c\u7247", "\u542c\u6d9b\u98df\u5802"),
            new AdminSubmission("\u83cc\u83c7\u9e21\u6c64\u9762", "\u4e01\u9999\u98df\u5802")
    ));

    private static final List<SupportMessage> SUPPORT = Collections.singletonList(
            new SupportMessage("student07", "\u600e\u4e48\u4fee\u6539\u5934\u50cf\uff1f")
    );

    public static List<String> getAnnouncements() {
        return Arrays.asList(
                "\u7d2b\u8346\u98df\u5802\u591c\u5bb5\u7a97\u53e3\u5ef6\u957f\u81f3 23:00",
                "\u6e05\u82ac\u98df\u5802\u672c\u5468\u4e0a\u65b0\u70d8\u7119\u548c\u51b7\u8403"
        );
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
        tags.add(ALL);
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
        categories.add(ALL);
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

    public static void addDish(Dish dish) {
        DISHES.add(dish);
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
        result.add(DISHES.get(10));
        result.add(DISHES.get(17));
        return result;
    }

    public static List<AdminSubmission> getAdminSubmissions() {
        return SUBMISSIONS;
    }

    public static List<SupportMessage> getSupportMessages() {
        return SUPPORT;
    }

    public static List<String> getFloorFilters(Canteen canteen) {
        List<String> floors = new ArrayList<>();
        floors.add(ALL);
        for (Dish dish : DISHES) {
            String floor = dish.floorNo + "\u697c";
            if (dish.canteenId == canteen.id && !floors.contains(floor)) {
                floors.add(floor);
            }
        }
        return floors;
    }

    public static List<String> getWindowFilters(Canteen canteen) {
        List<String> windows = new ArrayList<>();
        windows.add(ALL_WINDOWS);
        for (Dish dish : DISHES) {
            if (dish.canteenId == canteen.id && !windows.contains(dish.windowName)) {
                windows.add(dish.windowName);
            }
        }
        return windows;
    }
}
