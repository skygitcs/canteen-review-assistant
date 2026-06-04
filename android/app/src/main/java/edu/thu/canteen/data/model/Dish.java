package edu.thu.canteen.data.model;

import java.util.List;

public class Dish {
    public final long id;
    public final long canteenId;
    public final long windowId;
    public final String canteenName;
    public final String windowName;
    public final int floorNo;
    public final String name;
    public final String imageUrl;
    public final double price;
    public final String description;
    public final List<String> tags;
    public final String category;

    public Dish(long id, long canteenId, long windowId, String canteenName, String windowName, int floorNo,
                String name, String imageUrl, double price, String description,
                List<String> tags, String category) {
        this.id = id;
        this.canteenId = canteenId;
        this.windowId = windowId;
        this.canteenName = canteenName;
        this.windowName = windowName;
        this.floorNo = floorNo;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
        this.description = description;
        this.tags = tags;
        this.category = category;
    }
}
