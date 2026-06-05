package edu.thu.canteen;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.ChipGroup;
import edu.thu.canteen.data.model.Dish;
import edu.thu.canteen.data.model.Review;
import edu.thu.canteen.data.network.ApiResponse;
import edu.thu.canteen.data.network.DishDtos;
import edu.thu.canteen.data.network.NetworkClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DishDetailActivity extends AppCompatActivity {
    public static final String EXTRA_DISH_ID = "extra_dish_id";
    private long dishId;
    private Dish currentDish;
    private boolean isFavorited = false;
    private TextView favoriteBtn;
    private Button favoriteBtnLarge;
    private ReviewAdapter reviewAdapter;
    private List<Review> reviews = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_detail);

        dishId = getIntent().getLongExtra(EXTRA_DISH_ID, 101L);

        RecyclerView reviewList = findViewById(R.id.review_list);
        reviewList.setLayoutManager(new LinearLayoutManager(this));
        reviewAdapter = new ReviewAdapter(reviews);
        reviewAdapter.setOnVoteListener(this::fetchData);
        reviewList.setAdapter(reviewAdapter);

        favoriteBtn = findViewById(R.id.dish_favorite);
        favoriteBtn.setOnClickListener(v -> toggleFavorite());

        favoriteBtnLarge = findViewById(R.id.dish_favorite_large);
        favoriteBtnLarge.setOnClickListener(v -> toggleFavorite());

        Button writeReview = findViewById(R.id.write_review_button);
        writeReview.setOnClickListener(v -> FormDialogs.showReviewDialog(this, this::submitReview));

        findViewById(R.id.nav_back).setOnClickListener(v -> finish());
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        NavigationHelper.bind(this, bottomNav, NavigationHelper.TAB_FOOD_MAP);

        fetchData();
        checkFavoriteStatus();
    }

    private void fetchData() {
        NetworkClient.getService().getDishDetail(dishId).enqueue(new Callback<ApiResponse<DishDtos.DishDetailResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<DishDtos.DishDetailResponse>> call, Response<ApiResponse<DishDtos.DishDetailResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    currentDish = response.body().data.base;
                    bindDish(currentDish);
                    reviews.clear();
                    reviews.addAll(response.body().data.reviews);
                    reviewAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<DishDtos.DishDetailResponse>> call, Throwable t) {}
        });
    }

    private void bindDish(Dish dish) {
        ImageView cover = findViewById(R.id.dish_cover);
        TextView name = findViewById(R.id.dish_name);
        TextView canteenView = findViewById(R.id.dish_canteen);
        TextView price = findViewById(R.id.dish_price);
        TextView description = findViewById(R.id.dish_description);
        TextView navTitle = findViewById(R.id.nav_title);
        ChipGroup tags = findViewById(R.id.dish_tags);

        UiUtils.loadImage(cover, dish.imageUrl, dish.id, "dish");
        cover.setOnClickListener(v -> {
            String finalUrl = (dish.imageUrl != null && !dish.imageUrl.isEmpty()) 
                ? dish.imageUrl 
                : String.format("https://picsum.photos/seed/dish%d/800/600", dish.id);
            ImageViewerDialog.show(this, finalUrl);
        });

        name.setText(dish.name);
        navTitle.setText(dish.name);
        canteenView.setText(dish.canteenName);
        price.setText(String.format("\u00a5%.2f", dish.price));
        description.setText(dish.description);
        UiUtils.bindTags(tags, dish.tags);
    }

    private void checkFavoriteStatus() {
        NetworkClient.getService().getFavorites().enqueue(new Callback<ApiResponse<List<Dish>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Dish>>> call, Response<ApiResponse<List<Dish>>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    for (Dish d : response.body().data) {
                        if (d.id == dishId) {
                            isFavorited = true;
                            updateFavoriteUI();
                            break;
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<List<Dish>>> call, Throwable t) {}
        });
    }

    private void toggleFavorite() {
        if (isFavorited) {
            NetworkClient.getService().removeFavorite(dishId).enqueue(new Callback<ApiResponse<Void>>() {
                @Override
                public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                    if (response.isSuccessful()) {
                        isFavorited = false;
                        updateFavoriteUI();
                        UiUtils.toast(DishDetailActivity.this, "\u5df2\u53d6\u6d88\u6536\u85cf");
                    }
                }
                @Override
                public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {}
            });
        } else {
            NetworkClient.getService().addFavorite(new DishDtos.FavoriteRequest(dishId)).enqueue(new Callback<ApiResponse<Void>>() {
                @Override
                public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                    if (response.isSuccessful()) {
                        isFavorited = true;
                        updateFavoriteUI();
                        UiUtils.toast(DishDetailActivity.this, "\u5df2\u6536\u85cf");
                    }
                }
                @Override
                public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {}
            });
        }
    }

    private void updateFavoriteUI() {
        favoriteBtn.setText(isFavorited ? "\u2665" : "\u2661");
        favoriteBtn.setTextColor(isFavorited ? 0xFFFF4B4B : 0xFF111827);

        favoriteBtnLarge.setText(isFavorited ? "\u2665 \u5df2\u6536\u85cf" : "\u2661 \u6536\u85cf\u83dc\u54c1");
        favoriteBtnLarge.setTextColor(isFavorited ? 0xFFFF4B4B : 0xFF111827);
    }

    private void submitReview(int rating, String content, String imageUrl) {
        NetworkClient.getService().submitReview(dishId, new DishDtos.ReviewRequest(rating, content, imageUrl))
                .enqueue(new Callback<ApiResponse<Object>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            UiUtils.toast(DishDetailActivity.this, "\u8bc4\u4ef7\u5df2\u53d1\u5e03");
                            if (currentDish != null) {
                                NetworkClient.addLocalActivity("\u53d1\u5e03\u4e86\u8bc4\u4ef7\uff1a" + currentDish.name);
                            }
                            // Add locally for immediate feedback
                            reviewAdapter.addReview(new Review(System.currentTimeMillis(), dishId,
                                    "\u6211", rating, content, imageUrl, 0, 0, 0));
                            fetchData(); // Refresh to get official data
                        } else {
                            UiUtils.toast(DishDetailActivity.this, "\u53d1\u5e03\u5931\u8d25");
                        }
                    }
                    @Override
                    public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                        UiUtils.toast(DishDetailActivity.this, "\u7f51\u7edc\u9519\u8bef");
                    }
                });
    }
}
