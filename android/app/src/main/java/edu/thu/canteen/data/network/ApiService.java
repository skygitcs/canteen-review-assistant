package edu.thu.canteen.data.network;

import edu.thu.canteen.data.model.Canteen;
import edu.thu.canteen.data.model.Dish;
import edu.thu.canteen.data.model.UserProfile;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.*;

public interface ApiService {
    // Auth
    @POST("api/auth/register")
    Call<ApiResponse<AuthDtos.AuthResponse>> register(@Body AuthDtos.RegisterRequest request);

    @POST("api/auth/login")
    Call<ApiResponse<AuthDtos.AuthResponse>> login(@Body AuthDtos.LoginRequest request);

    // Announcements
    @GET("api/announcements")
    Call<ApiResponse<List<CanteenDtos.AnnouncementDto>>> getAnnouncements();

    // Canteens
    @GET("api/canteens")
    Call<ApiResponse<List<Canteen>>> getCanteens(
            @Query("keyword") String keyword,
            @Query("onCampus") Boolean onCampus,
            @Query("sort") String sort
    );

    @GET("api/canteens/{id}")
    Call<ApiResponse<CanteenDtos.CanteenDetailResponse>> getCanteenDetail(
            @Path("id") long id,
            @Query("floorNo") Integer floorNo,
            @Query("tag") String tag,
            @Query("sort") String sort
    );

    @POST("api/canteens/{id}/crowd")
    Call<ApiResponse<Void>> reportCrowd(@Path("id") long id, @Body CanteenDtos.CrowdRequest request);

    @GET("api/canteens/heatmap")
    Call<ApiResponse<List<CanteenDtos.HeatPoint>>> getHeatmap(@Query("scope") String scope);

    // Dishes
    @GET("api/dishes")
    Call<ApiResponse<List<Dish>>> searchDishes(
            @Query("keyword") String keyword,
            @Query("canteenId") Long canteenId,
            @Query("floorNo") Integer floorNo,
            @Query("tag") String tag,
            @Query("sort") String sort
    );

    @GET("api/dishes/{id}")
    Call<ApiResponse<DishDtos.DishDetailResponse>> getDishDetail(@Path("id") long id);

    @GET("api/dishes/recommendations")
    Call<ApiResponse<List<Dish>>> getRecommendations(@Query("limit") int limit);

    @POST("api/dishes/submissions")
    Call<ApiResponse<Void>> submitDishSubmission(@Body Dish dish);

    // Reviews
    @POST("api/dishes/{dishId}/reviews")
    Call<ApiResponse<Object>> submitReview(@Path("dishId") long dishId, @Body DishDtos.ReviewRequest request);

    @POST("api/reviews/{reviewId}/vote")
    Call<ApiResponse<Object>> voteReview(@Path("reviewId") long reviewId, @Body DishDtos.VoteRequest request);

    // User & Favorites
    @GET("api/users/me")
    Call<ApiResponse<UserProfile>> getMyProfile();

    @PUT("api/users/me")
    Call<ApiResponse<UserProfile>> updateProfile(@Body UserProfile profile);

    @GET("api/users/me/favorites")
    Call<ApiResponse<List<Dish>>> getFavorites();

    @POST("api/users/me/favorites")
    Call<ApiResponse<Void>> addFavorite(@Body DishDtos.FavoriteRequest request);

    @DELETE("api/users/me/favorites/{dishId}")
    Call<ApiResponse<Void>> removeFavorite(@Path("dishId") long dishId);

    // Admin
    @GET("api/admin/submissions")
    Call<ApiResponse<List<edu.thu.canteen.data.model.DishSubmission>>> getPendingSubmissions();

    @POST("api/admin/submissions/{id}/approve")
    Call<ApiResponse<Object>> approveSubmission(@Path("id") long id, @Body AdminDtos.AuditRequest request);

    @POST("api/admin/submissions/{id}/reject")
    Call<ApiResponse<Object>> rejectSubmission(@Path("id") long id, @Body AdminDtos.AuditRequest request);

    @POST("api/admin/announcements")
    Call<ApiResponse<Object>> createAnnouncement(@Body AdminDtos.AnnouncementRequest request);
}
