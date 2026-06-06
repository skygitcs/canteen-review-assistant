package edu.thu.canteen.data.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DishSubmission {
    @SerializedName("id")
    public final long id;
    
    @SerializedName("submitterId")
    public final long submitterId;

    @SerializedName("submitterName")
    public final String submitterName;
    
    @SerializedName("canteenId")
    public final long canteenId;
    
    @SerializedName("windowId")
    public final long windowId;
    
    @SerializedName("name")
    public final String name;
    
    @SerializedName("canteenName")
    public final String canteenName;
    
    @SerializedName("windowName")
    public final String windowName;
    
    @SerializedName("floorNo")
    public final int floorNo;
    
    @SerializedName("auditStatus")
    public final String auditStatus;
    
    @SerializedName("imageUrl")
    public final String imageUrl;
    
    @SerializedName("spiceLevel")
    public final int spiceLevel;
    
    @SerializedName("price")
    public final double price;
    
    @SerializedName("description")
    public final String description;

    @SerializedName("tags")
    public final List<String> tags;

    @SerializedName("auditReason")
    public final String auditReason;

    public DishSubmission(long id, long submitterId, long canteenId, long windowId, 
                          String name, String canteenName, String windowName, int floorNo,
                          String auditStatus, String imageUrl, int spiceLevel, double price, String description,
                          String submitterName, List<String> tags, String auditReason) {
        this.id = id;
        this.submitterId = submitterId;
        this.submitterName = submitterName;
        this.canteenId = canteenId;
        this.windowId = windowId;
        this.name = name;
        this.canteenName = canteenName;
        this.windowName = windowName;
        this.floorNo = floorNo;
        this.auditStatus = auditStatus;
        this.imageUrl = imageUrl;
        this.spiceLevel = spiceLevel;
        this.price = price;
        this.description = description;
        this.tags = tags;
        this.auditReason = auditReason;
    }
}
