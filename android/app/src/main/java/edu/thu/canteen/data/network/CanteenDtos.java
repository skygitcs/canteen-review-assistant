package edu.thu.canteen.data.network;

import edu.thu.canteen.data.model.Canteen;
import edu.thu.canteen.data.model.Dish;
import java.util.List;

public class CanteenDtos {
    public static class CanteenDetailResponse {
        public Canteen base;
        public List<WindowDto> windows;
        public List<Dish> dishes;
    }

    public static class WindowDto {
        public long id;
        public int floorNo;
        public String name;
        public String openHours;
    }

    public static class CrowdRequest {
        public int level;

        public CrowdRequest(int level) {
            this.level = level;
        }
    }

    public static class HeatPoint {
        public Long canteenId;
        public String canteenName;
        public double latitude;
        public double longitude;
        public long visits;
        public double amount;
    }

    public static class AnnouncementDto {
        public long id;
        public String title;
        public String content;
        public String createdAt;
    }
}
