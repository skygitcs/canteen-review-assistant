package edu.thu.canteen.data.network;

public class AdminDtos {
    public static class AuditRequest {
        public String reason;

        public AuditRequest(String reason) {
            this.reason = reason;
        }
    }

    public static class AnnouncementRequest {
        public String title;
        public String content;

        public AnnouncementRequest(String title, String content) {
            this.title = title;
            this.content = content;
        }
    }
}
