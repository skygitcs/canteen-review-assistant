package edu.thu.canteen.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Dish {
    @SerializedName("id")
    public final long id;
    
    @SerializedName("canteenId")
    public final long canteenId;
    
    @SerializedName("windowId")
    public final long windowId;
    
    @SerializedName("canteenName")
    public final String canteenName;
    
    @SerializedName("windowName")
    public final String windowName;
    
    @SerializedName("floorNo")
    public final int floorNo;
    
    @SerializedName("name")
    public final String name;
    
    @SerializedName("imageUrl")
    public final String imageUrl;
    
    @SerializedName("price")
    public final double price;
    
    @SerializedName("description")
    public final String description;
    
    @SerializedName("spiceLevel")
    public final int spiceLevel;
    
    @SerializedName("tags")
    public final List<String> tags;

    @SerializedName("avgRating")
    public final double avgRating;

    @SerializedName("reviewCount")
    public final long reviewCount;

    @SerializedName("favoriteCount")
    public final long favoriteCount;
    
    public final String category; // Client-side only for now

    public Dish(long id, long canteenId, long windowId, String canteenName, String windowName, int floorNo,
                String name, String imageUrl, double price, String description, int spiceLevel,
                List<String> tags, String category) {
        this(id, canteenId, windowId, canteenName, windowName, floorNo, name, imageUrl, price,
                description, spiceLevel, tags, category, 0.0, 0, 0);
    }

    public Dish(long id, long canteenId, long windowId, String canteenName, String windowName, int floorNo,
                String name, String imageUrl, double price, String description, int spiceLevel,
                List<String> tags, String category, double avgRating, long reviewCount, long favoriteCount) {
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
        this.spiceLevel = spiceLevel;
        this.tags = tags;
        this.category = category;
        this.avgRating = avgRating;
        this.reviewCount = reviewCount;
        this.favoriteCount = favoriteCount;
    }
}
