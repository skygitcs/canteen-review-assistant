package edu.thu.canteen;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import edu.thu.canteen.data.model.Canteen;
import edu.thu.canteen.data.model.Dish;
import edu.thu.canteen.data.network.ApiResponse;
import edu.thu.canteen.data.network.CanteenDtos;
import edu.thu.canteen.data.network.NetworkClient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CanteenDetailActivity extends AppCompatActivity {
    public static final String EXTRA_CANTEEN_ID = "extra_canteen_id";
    private static final String TAG = "CanteenDetail";

    private String activeFloorLabel = "";
    private String activeWindowName = "";
    private String query = "";
    
    private final List<Dish> allDishes = new ArrayList<>();
    private final List<CanteenDtos.WindowDto> allWindowsData = new ArrayList<>();
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

        findViewById(R.id.add_dish_button).setOnClickListener(v -> {
            if (canteen != null) FormDialogs.showSupplementDialog(this, canteen, this::fetchData);
        });
        findViewById(R.id.crowd_report_button).setOnClickListener(v -> reportCrowd());

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
                            if (detail.dishes != null) allDishes.addAll(detail.dishes);
                            
                            allWindowsData.clear();
                            if (detail.windows != null) allWindowsData.addAll(detail.windows);
                            
                            setupFilters();
                        }
                    }
                    @Override
                    public void onFailure(Call<ApiResponse<CanteenDtos.CanteenDetailResponse>> call, Throwable t) {
                        UiUtils.toast(CanteenDetailActivity.this, "\u7f51\u7edc\u9519\u8bef");
                    }
                });
    }

    private void bindCanteen(Canteen canteen) {
        UiUtils.loadImage(findViewById(R.id.canteen_cover), canteen.coverUrl, canteen.id, "canteen");
        ((TextView)findViewById(R.id.canteen_name)).setText(canteen.name);
        ((TextView)findViewById(R.id.nav_title)).setText(canteen.name);
        ((TextView)findViewById(R.id.canteen_address)).setText(canteen.address);
        UiUtils.bindTags(findViewById(R.id.canteen_tags), canteen.tags);
    }

    private void setupFilters() {
        // 1. Get unique floors from window data (No "All" option)
        Set<Integer> floorSet = new HashSet<>();
        for (CanteenDtos.WindowDto w : allWindowsData) floorSet.add(w.floorNo);
        List<Integer> sortedFloors = new ArrayList<>(floorSet);
        Collections.sort(sortedFloors);
        
        List<String> floorLabels = new ArrayList<>();
        for (Integer f : sortedFloors) floorLabels.add(f + "\u697c");
        
        if (floorLabels.isEmpty()) return;

        // 2. Setup Floor List
        RecyclerView floorList = findViewById(R.id.floor_list);
        floorList.setLayoutManager(new LinearLayoutManager(this));
        activeFloorLabel = floorLabels.get(0); // Default to first floor
        
        CategoryAdapter floorAdapter = new CategoryAdapter(floorLabels, label -> {
            activeFloorLabel = label;
            updateWindowSpinner();
        });
        floorList.setAdapter(floorAdapter);
        
        // 3. Initial update of window spinner
        updateWindowSpinner();
    }

    private void updateWindowSpinner() {
        Spinner windowSpinner = findViewById(R.id.window_spinner);
        int currentFloor = Integer.parseInt(activeFloorLabel.replace("\u697c", ""));
        
        List<String> windowsOnFloor = new ArrayList<>();
        for (CanteenDtos.WindowDto w : allWindowsData) {
            if (w.floorNo == currentFloor) {
                windowsOnFloor.add(w.name);
            }
        }
        
        if (windowsOnFloor.isEmpty()) {
            activeWindowName = "";
        } else {
            activeWindowName = windowsOnFloor.get(0);
        }

        ArrayAdapter<String> windowAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, windowsOnFloor);
        windowAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        windowSpinner.setAdapter(windowAdapter);
        windowSpinner.setOnItemSelectedListener(new SimpleItemSelectedListener(value -> {
            activeWindowName = value;
            applyFilters();
        }));
        
        // Trigger initial filter for the first window
        applyFilters();
    }

    private void applyFilters() {
        if (dishAdapter == null) return;
        List<Dish> filtered = new ArrayList<>();
        
        int currentFloor = 0;
        try { currentFloor = Integer.parseInt(activeFloorLabel.replace("\u697c", "")); } catch (Exception e) {}

        for (Dish dish : allDishes) {
            // Strict match: Floor must match AND Window must match
            boolean matchesFloor = (dish.floorNo == currentFloor);
            boolean matchesWindow = (dish.windowName != null && dish.windowName.equals(activeWindowName));
            boolean matchesQuery = query.isEmpty() || (dish.name != null && dish.name.toLowerCase().contains(query.toLowerCase()));
            
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
                            if (response.isSuccessful()) UiUtils.toast(CanteenDetailActivity.this, "\u4e0a\u62a5\u6210\u529f");
                        }
                        @Override
                        public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {}
                    });
        });
    }
}
