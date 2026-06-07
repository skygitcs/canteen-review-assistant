package edu.thu.canteen.data.model;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import org.junit.Test;

public class DishTest {

    @Test
    public void constructorKeepsServerAndDisplayFields() {
        Dish dish = new Dish(
                12L,
                3L,
                7L,
                "Qingfen",
                "Spicy Window",
                2,
                "Spicy Pot",
                "https://example.com/dish.jpg",
                18.5,
                "Configurable spice level",
                3,
                Arrays.asList("spicy", "popular"),
                "hot",
                4.7,
                21,
                8
        );

        assertEquals(12L, dish.id);
        assertEquals(3L, dish.canteenId);
        assertEquals(7L, dish.windowId);
        assertEquals("Qingfen", dish.canteenName);
        assertEquals("Spicy Window", dish.windowName);
        assertEquals(2, dish.floorNo);
        assertEquals("Spicy Pot", dish.name);
        assertEquals("https://example.com/dish.jpg", dish.imageUrl);
        assertEquals(18.5, dish.price, 0.001);
        assertEquals("Configurable spice level", dish.description);
        assertEquals(3, dish.spiceLevel);
        assertEquals(Arrays.asList("spicy", "popular"), dish.tags);
        assertEquals("hot", dish.category);
        assertEquals(4.7, dish.avgRating, 0.001);
        assertEquals(21L, dish.reviewCount);
        assertEquals(8L, dish.favoriteCount);
    }
}
