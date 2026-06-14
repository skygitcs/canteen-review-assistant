package edu.thu.canteen.data.network;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ApiResponseTest {

    @Test
    public void successOnlyWhenCodeIsZero() {
        ApiResponse<String> success = new ApiResponse<>();
        success.code = 0;
        success.message = "ok";
        success.data = "payload";

        ApiResponse<String> failure = new ApiResponse<>();
        failure.code = 400;

        assertTrue(success.isSuccess());
        assertEquals("ok", success.message);
        assertEquals("payload", success.data);
        assertFalse(failure.isSuccess());
    }
}
