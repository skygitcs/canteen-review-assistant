package edu.thu.canteen.data.model;

import com.google.gson.annotations.SerializedName;

public class DishSubmission {
    @SerializedName("id")
    public final long id;
    
    @SerializedName("submitterId")
    public final long submitterId;
    
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
    
    @SerializedName("price")
    public final double price;
    
    @SerializedName("description")
    public final String description;

    public DishSubmission(long id, long submitterId, long canteenId, long windowId, 
                          String name, String canteenName, String windowName, int floorNo,
                          String auditStatus, double price, String description) {
        this.id = id;
        this.submitterId = submitterId;
        this.canteenId = canteenId;
        this.windowId = windowId;
        this.name = name;
        this.canteenName = canteenName;
        this.windowName = windowName;
        this.floorNo = floorNo;
        this.auditStatus = auditStatus;
        this.price = price;
        this.description = description;
    }
}
