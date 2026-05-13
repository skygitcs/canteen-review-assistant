package edu.thu.canteen.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import edu.thu.canteen.domain.entity.Announcement;
import edu.thu.canteen.domain.mapper.AnnouncementMapper;
import edu.thu.canteen.dto.AdminDtos;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnnouncementService {
    private final AnnouncementMapper announcementMapper;

    public AnnouncementService(AnnouncementMapper announcementMapper) {
        this.announcementMapper = announcementMapper;
    }

    public List<Announcement> active() {
        LocalDateTime now = LocalDateTime.now();
        return announcementMapper.selectList(Wrappers.<Announcement>lambdaQuery()
                .eq(Announcement::getStatus, 1)
                .le(Announcement::getStartsAt, now)
                .ge(Announcement::getEndsAt, now)
                .orderByDesc(Announcement::getCreatedAt));
    }

    public Announcement create(AdminDtos.AnnouncementRequest request) {
        Announcement announcement = new Announcement();
        announcement.setTitle(request.title());
        announcement.setContent(request.content());
        announcement.setStartsAt(LocalDateTime.now());
        announcement.setEndsAt(LocalDateTime.now().plusDays(30));
        announcement.setStatus(1);
        announcementMapper.insert(announcement);
        return announcement;
    }
}
