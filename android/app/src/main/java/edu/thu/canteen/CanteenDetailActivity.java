package edu.thu.canteen;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.ChipGroup;
import edu.thu.canteen.data.model.Canteen;
import edu.thu.canteen.data.model.Dish;
import edu.thu.canteen.data.network.ApiResponse;
import edu.thu.canteen.data.network.CanteenDtos;
import edu.thu.canteen.data.network.NetworkClient;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CanteenDetailActivity extends AppCompatActivity {
    public static final String EXTRA_CANTEEN_ID = "extra_canteen_id";

    private String activeFloor = "\u5168\u90e8";
    private String activeWindow = "\u5168\u90e8\u7a97\u53e3";
    private String query = "";
    private List<Dish> allDishes = new ArrayList<>();
    private DishAdapter dishAdapter;
    private Canteen canteen;
    private long canteenId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_detail);

        canteenId = getIntent().getLongExtra(EXTRA_CANTEEN_ID, 1L);

        RecyclerView dishList = findViewById(R.id.canteen_dish_list);
        dishList.setLayoutManager(new LinearLayoutManager(this));
        dishAdapter = new DishAdapter(new ArrayList<>(), dish -> {
            Intent intent = new Intent(this, DishDetailActivity.class);
            intent.putExtra(DishDetailActivity.EXTRA_DISH_ID, dish.id);
            startActivity(intent);
        });
        dishList.setAdapter(dishAdapter);

        findViewById(R.id.nav_back).setOnClickListener(v -> finish());
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        NavigationHelper.bind(this, bottomNav, NavigationHelper.TAB_FOOD_MAP);

        EditText searchInput = findViewById(R.id.dish_search_input);
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                query = s.toString().trim();
                applyFilters();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        Button addDish = findViewById(R.id.add_dish_button);
        addDish.setOnClickListener(v -> {
            if (canteen != null) {
                FormDialogs.showSupplementDialog(this, canteen, () -> {
                    fetchData();
                });
            } else {
                UiUtils.toast(this, "\u6b63\u5728\u52a0\u8f7d\u98df\u5802\u4fe1\u606f...");
            }
        });

        Button crowdBtn = findViewById(R.id.crowd_report_button);
        if (crowdBtn != null) {
            crowdBtn.setOnClickListener(v -> reportCrowd());
        }

        fetchData();
    }

    private void fetchData() {
        NetworkClient.getService().getCanteenDetail(canteenId, null, null, null)
                .enqueue(new Callback<ApiResponse<CanteenDtos.CanteenDetailResponse>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<CanteenDtos.CanteenDetailResponse>> call, Response<ApiResponse<CanteenDtos.CanteenDetailResponse>> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                            CanteenDtos.CanteenDetailResponse detail = response.body().data;
                            canteen = detail.base;
                            bindCanteen(canteen);
                            allDishes.clear();
                            allDishes.addAll(detail.dishes);
                            applyFilters();
                            setupFilters(detail);
                        }
                    }
                    @Override
                    public void onFailure(Call<ApiResponse<CanteenDtos.CanteenDetailResponse>> call, Throwable t) {}
                });
    }

    private void bindCanteen(Canteen canteen) {
        ImageView cover = findViewById(R.id.canteen_cover);
        TextView name = findViewById(R.id.canteen_name);
        TextView address = findViewById(R.id.canteen_address);
        TextView navTitle = findViewById(R.id.nav_title);
        ChipGroup tags = findViewById(R.id.canteen_tags);

        if (canteen.coverUrl == null || canteen.coverUrl.isEmpty()) {
            cover.setImageDrawable(null);
            cover.setBackgroundResource(R.drawable.bg_image_placeholder);
        } else {
            Glide.with(this).load(canteen.coverUrl).into(cover);
        }
        name.setText(canteen.name);
        navTitle.setText(canteen.name);
        address.setText(canteen.address);
        UiUtils.bindTags(tags, canteen.tags);
    }

    private void setupFilters(CanteenDtos.CanteenDetailResponse data) {
        Spinner windowSpinner = findViewById(R.id.window_spinner);
        List<String> windows = new ArrayList<>();
        windows.add("\u5168\u90e8\u7a97\u53e3");
        for (CanteenDtos.WindowDto w : data.windows) {
            windows.add(w.name);
        }
        ArrayAdapter<String> windowAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, windows);
        windowAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        windowSpinner.setAdapter(windowAdapter);
        windowSpinner.setOnItemSelectedListener(new SimpleItemSelectedListener(value -> {
            activeWindow = value;
            applyFilters();
        }));

        RecyclerView floorList = findViewById(R.id.floor_list);
        floorList.setLayoutManager(new LinearLayoutManager(this));
        List<String> floors = new ArrayList<>();
        floors.add("\u5168\u90e8");
        Set<Integer> floorSet = new HashSet<>();
        for (CanteenDtos.WindowDto w : data.windows) {
            floorSet.add(w.floorNo);
        }
        for (Integer f : floorSet) floors.add(f + "\u697c");
        
        floorList.setAdapter(new CategoryAdapter(floors, floor -> {
            activeFloor = floor;
            applyFilters();
        }));
    }

    private void applyFilters() {
        if (dishAdapter == null) return;
        List<Dish> filtered = new ArrayList<>();
        for (Dish dish : allDishes) {
            boolean matchesFloor = "\u5168\u90e8".equals(activeFloor) || activeFloor.equals(dish.floorNo + "\u697c");
            boolean matchesWindow = "\u5168\u90e8\u7a97\u53e3".equals(activeWindow) || activeWindow.equals(dish.windowName);
            boolean matchesQuery = query.isEmpty() || dish.name.contains(query);
            if (matchesFloor && matchesWindow && matchesQuery) {
                filtered.add(dish);
            }
        }
        dishAdapter.replaceItems(filtered);
    }

    private void reportCrowd() {
        FormDialogs.showCrowdDialog(this, level -> {
            NetworkClient.getService().reportCrowd(canteenId, new CanteenDtos.CrowdRequest(level))
                    .enqueue(new Callback<ApiResponse<Void>>() {
                        @Override
                        public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                            if (response.isSuccessful()) {
                                UiUtils.toast(CanteenDetailActivity.this, "\u4e0a\u62a5\u6210\u529f");
                            }
                        }
                        @Override
                        public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {}
                    });
        });
    }
}
