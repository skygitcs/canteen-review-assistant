package edu.thu.canteen.data.network;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import edu.thu.canteen.data.model.Canteen;
import edu.thu.canteen.data.model.Dish;
import edu.thu.canteen.data.model.UserProfile;
import java.util.Arrays;
import org.junit.Test;

public class AdminAuthCanteenDtosTest {

    @Test
    public void adminRequestConstructorsKeepSubmittedValues() {
        AdminDtos.AuditRequest audit = new AdminDtos.AuditRequest("Looks good");
        AdminDtos.AnnouncementRequest announcement =
                new AdminDtos.AnnouncementRequest("Lunch update", "Window 2 opens early");

        assertEquals("Looks good", audit.reason);
        assertEquals("Lunch update", announcement.title);
        assertEquals("Window 2 opens early", announcement.content);
    }

    @Test
    public void authRequestConstructorsAndResponseKeepSubmittedValues() {
        AuthDtos.LoginRequest login = new AuthDtos.LoginRequest("student", "password");
        AuthDtos.RegisterRequest register = new AuthDtos.RegisterRequest("student", "password", "Tester");
        UserProfile profile = new UserProfile("student", "Tester", "Light", "", "USER");
        AuthDtos.AuthResponse response = new AuthDtos.AuthResponse();
        response.token = "jwt-token";
        response.user = profile;

        assertEquals("student", login.username);
        assertEquals("password", login.password);
        assertEquals("student", register.username);
        assertEquals("password", register.password);
        assertEquals("Tester", register.nickname);
        assertEquals("jwt-token", response.token);
        assertSame(profile, response.user);
    }

    @Test
    public void canteenRequestAndResponseDtosKeepFields() {
        Canteen canteen = new Canteen(1L, "Main", "", "North", 4.5, 1.0, Arrays.asList("hot"));
        Dish dish = new Dish(10L, 1L, 2L, "Main", "Noodles", 1, "Beef Noodles",
                "", 15.0, "Rich soup", 1, Arrays.asList("noodles"), "noodles");

        CanteenDtos.CrowdRequest crowdRequest = new CanteenDtos.CrowdRequest(3);
        CanteenDtos.WindowDto window = new CanteenDtos.WindowDto();
        window.id = 2L;
        window.floorNo = 1;
        window.name = "Noodles";
        window.openHours = "10:00-13:00";

        CanteenDtos.CanteenDetailResponse detail = new CanteenDtos.CanteenDetailResponse();
        detail.base = canteen;
        detail.windows = Arrays.asList(window);
        detail.dishes = Arrays.asList(dish);

        CanteenDtos.HeatPoint heatPoint = new CanteenDtos.HeatPoint();
        heatPoint.canteenId = 1L;
        heatPoint.canteenName = "Main";
        heatPoint.latitude = 40.001;
        heatPoint.longitude = 116.321;
        heatPoint.visits = 120L;
        heatPoint.amount = 856.5;

        CanteenDtos.AnnouncementDto announcement = new CanteenDtos.AnnouncementDto();
        announcement.id = 9L;
        announcement.title = "Notice";
        announcement.content = "Open as usual";
        announcement.createdAt = "2026-06-07T10:00:00";

        assertEquals(3, crowdRequest.level);
        assertEquals(2L, window.id);
        assertEquals(1, window.floorNo);
        assertEquals("Noodles", window.name);
        assertEquals("10:00-13:00", window.openHours);
        assertSame(canteen, detail.base);
        assertEquals(1, detail.windows.size());
        assertSame(window, detail.windows.get(0));
        assertEquals(1, detail.dishes.size());
        assertSame(dish, detail.dishes.get(0));
        assertEquals(1L, heatPoint.canteenId.longValue());
        assertEquals("Main", heatPoint.canteenName);
        assertEquals(40.001, heatPoint.latitude, 0.001);
        assertEquals(116.321, heatPoint.longitude, 0.001);
        assertEquals(120L, heatPoint.visits);
        assertEquals(856.5, heatPoint.amount, 0.001);
        assertEquals(9L, announcement.id);
        assertEquals("Notice", announcement.title);
        assertEquals("Open as usual", announcement.content);
        assertEquals("2026-06-07T10:00:00", announcement.createdAt);
    }
}
