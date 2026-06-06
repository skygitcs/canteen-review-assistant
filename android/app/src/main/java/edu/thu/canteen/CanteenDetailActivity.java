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
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
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

    // Defaults to first available floor and "All Windows" of that floor
    private String activeFloorLabel = ""; 
    private String activeWindowName = "\u5168\u90e8\u7a97\u53e3";
    private String activeTag = "\u5168\u90e8";
    private String query = "";
    
    private final List<Dish> allDishes = new ArrayList<>();
    private final List<CanteenDtos.WindowDto> allWindowsData = new ArrayList<>();
    private DishAdapter dishAdapter;
    private CategoryAdapter floorAdapter;
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
    }

    @Override
    protected void onStart() {
        super.onStart();
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
                            
                            setupTagFilters();
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
        Set<Integer> floorSet = new HashSet<>();
        for (CanteenDtos.WindowDto w : allWindowsData) floorSet.add(w.floorNo);
        List<Integer> sortedFloors = new ArrayList<>(floorSet);
        Collections.sort(sortedFloors);
        
        List<String> floorLabels = new ArrayList<>();
        for (Integer f : sortedFloors) floorLabels.add(f + "\u697c");
        
        if (floorLabels.isEmpty()) {
            activeFloorLabel = "";
            dishAdapter.replaceItems(new ArrayList<>());
            updateEmptyState(true);
            return;
        }

        // Preserve current selection if possible, otherwise default to first floor
        int selectedIdx = floorLabels.indexOf(activeFloorLabel);
        if (selectedIdx < 0) {
            selectedIdx = 0;
            activeFloorLabel = floorLabels.get(0);
        }

        RecyclerView floorList = findViewById(R.id.floor_list);
        floorList.setLayoutManager(new LinearLayoutManager(this));
        floorAdapter = new CategoryAdapter(floorLabels, label -> {
            activeFloorLabel = label;
            activeWindowName = "\u5168\u90e8\u7a97\u53e3"; // Reset window when floor changes
            updateWindowSpinner();
        });
        floorAdapter.setSelectedIndex(selectedIdx);
        floorList.setAdapter(floorAdapter);
        
        updateWindowSpinner();
    }

    private void updateWindowSpinner() {
        Spinner windowSpinner = findViewById(R.id.window_spinner);
        int currentFloor = 0;
        try { currentFloor = Integer.parseInt(activeFloorLabel.replace("\u697c", "")); } catch (Exception e) {}
        
        List<String> windowsOnFloor = new ArrayList<>();
        windowsOnFloor.add("\u5168\u90e8\u7a97\u53e3");
        for (CanteenDtos.WindowDto w : allWindowsData) {
            if (w.floorNo == currentFloor) {
                windowsOnFloor.add(w.name);
            }
        }
        
        // Preserve current selection if possible, otherwise default to "All Windows"
        int selectedIdx = windowsOnFloor.indexOf(activeWindowName);
        if (selectedIdx < 0) {
            selectedIdx = 0;
            activeWindowName = windowsOnFloor.get(0);
        }

        ArrayAdapter<String> windowAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, windowsOnFloor);
        windowAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        windowSpinner.setAdapter(windowAdapter);
        windowSpinner.setSelection(selectedIdx);
        windowSpinner.setOnItemSelectedListener(new SimpleItemSelectedListener(value -> {
            activeWindowName = value;
            applyFilters();
        }));
        
        applyFilters();
    }

    private void setupTagFilters() {
        ChipGroup group = findViewById(R.id.dish_tag_filter_group);
        group.removeAllViews();
        List<String> tags = new ArrayList<>();
        tags.add("\u5168\u90e8");
        for (Dish dish : allDishes) {
            if (dish.tags == null) continue;
            for (String tag : dish.tags) {
                if (tag != null && !tag.isEmpty() && !tags.contains(tag)) tags.add(tag);
            }
        }
        if (!tags.contains(activeTag)) activeTag = "\u5168\u90e8";
        for (String tag : tags) {
            Chip chip = new Chip(this);
            chip.setText(tag);
            chip.setCheckable(true);
            chip.setChecked(tag.equals(activeTag));
            UiUtils.styleTagChip(chip, tag);
            chip.setOnClickListener(v -> {
                activeTag = tag;
                for (int i = 0; i < group.getChildCount(); i++) {
                    View child = group.getChildAt(i);
                    if (child instanceof Chip) {
                        ((Chip) child).setChecked(((Chip) child).getText().toString().equals(activeTag));
                    }
                }
                applyFilters();
            });
            group.addView(chip);
        }
    }

    private void applyFilters() {
        if (dishAdapter == null) return;
        List<Dish> filtered = new ArrayList<>();
        
        int currentFloor = 0;
        try { currentFloor = Integer.parseInt(activeFloorLabel.replace("\u697c", "")); } catch (Exception e) {}

        for (Dish dish : allDishes) {
            boolean matchesFloor = (dish.floorNo == currentFloor);
            boolean matchesWindow = activeWindowName.equals("\u5168\u90e8\u7a97\u53e3") 
                    || (dish.windowName != null && dish.windowName.equals(activeWindowName));
            boolean matchesTag = activeTag.equals("\u5168\u90e8") || (dish.tags != null && dish.tags.contains(activeTag));
            boolean matchesQuery = matchesQuery(dish);
            
            if (matchesFloor && matchesWindow && matchesTag && matchesQuery) {
                filtered.add(dish);
            }
        }
        
        dishAdapter.replaceItems(filtered);
        updateEmptyState(filtered.isEmpty());
        Log.d(TAG, "Filtered dishes: " + filtered.size() + " out of " + allDishes.size());
    }

    private boolean matchesQuery(Dish dish) {
        if (query.isEmpty()) return true;
        String lowerQuery = query.toLowerCase();
        if (dish.name != null && dish.name.toLowerCase().contains(lowerQuery)) return true;
        if (dish.windowName != null && dish.windowName.toLowerCase().contains(lowerQuery)) return true;
        if (dish.description != null && dish.description.toLowerCase().contains(lowerQuery)) return true;
        if (dish.tags != null) {
            for (String tag : dish.tags) {
                if (tag != null && tag.toLowerCase().contains(lowerQuery)) return true;
            }
        }
        return false;
    }

    private void updateEmptyState(boolean empty) {
        View emptyText = findViewById(R.id.canteen_empty_text);
        View dishList = findViewById(R.id.canteen_dish_list);
        emptyText.setVisibility(empty ? View.VISIBLE : View.GONE);
        dishList.setVisibility(empty ? View.GONE : View.VISIBLE);
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
