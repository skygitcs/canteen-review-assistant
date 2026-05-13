package edu.thu.canteen.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public final class AdminDtos {
    private AdminDtos() {
    }

    public record AuditRequest(@Size(max = 200) String reason) {
    }

    public record AnnouncementRequest(
            @NotBlank @Size(max = 80) String title,
            @NotBlank @Size(max = 500) String content
    ) {
    }
}
