package edu.thu.canteen;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import edu.thu.canteen.data.MockRepository;
import edu.thu.canteen.data.model.Canteen;
import edu.thu.canteen.data.model.Dish;
import java.util.ArrayList;
import java.util.List;

public class CanteenDetailActivity extends AppCompatActivity {
    public static final String EXTRA_CANTEEN_ID = "extra_canteen_id";

    private String activeFloor = "\u5168\u90e8";
    private String activeWindow = "\u5168\u90e8\u7a97\u53e3";
    private String query = "";
    private List<Dish> allDishes;
    private DishAdapter dishAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_detail);

        long canteenId = getIntent().getLongExtra(EXTRA_CANTEEN_ID, 1L);
        Canteen canteen = MockRepository.getCanteenById(canteenId);
        allDishes = MockRepository.getDishesByCanteen(canteen);

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
        findViewById(R.id.nav_back).setOnClickListener(v -> finish());
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        NavigationHelper.bind(this, bottomNav, NavigationHelper.TAB_FOOD_MAP);

        EditText searchInput = findViewById(R.id.dish_search_input);
        Spinner windowSpinner = findViewById(R.id.window_spinner);
        ArrayAdapter<String> windowAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, MockRepository.getWindowFilters(canteen));
        windowAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        windowSpinner.setAdapter(windowAdapter);
        windowSpinner.setOnItemSelectedListener(new SimpleItemSelectedListener(value -> {
            activeWindow = value;
            applyFilters();
        }));
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                query = s.toString().trim();
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        RecyclerView floorList = findViewById(R.id.floor_list);
        floorList.setLayoutManager(new LinearLayoutManager(this));
        CategoryAdapter floorAdapter = new CategoryAdapter(MockRepository.getFloorFilters(canteen), floor -> {
            activeFloor = floor;
            applyFilters();
        });
        floorList.setAdapter(floorAdapter);

        RecyclerView dishList = findViewById(R.id.canteen_dish_list);
        dishList.setLayoutManager(new LinearLayoutManager(this));
        dishAdapter = new DishAdapter(new ArrayList<>(allDishes), dish -> {
            Intent intent = new Intent(this, DishDetailActivity.class);
            intent.putExtra(DishDetailActivity.EXTRA_DISH_ID, dish.id);
            startActivity(intent);
        });
        dishList.setAdapter(dishAdapter);

        Button addDish = findViewById(R.id.add_dish_button);
        addDish.setOnClickListener(v -> FormDialogs.showSupplementDialog(this));
    }

    private void applyFilters() {
        if (dishAdapter == null || allDishes == null) {
            return;
        }
        List<Dish> filtered = new ArrayList<>();
        for (Dish dish : allDishes) {
            boolean matchesFloor = "\u5168\u90e8".equals(activeFloor) || activeFloor.equals(dish.floorNo + "\u697c");
            boolean matchesWindow = "\u5168\u90e8\u7a97\u53e3".equals(activeWindow) || activeWindow.equals(dish.windowName);
            boolean matchesQuery = query.isEmpty()
                    || dish.name.contains(query)
                    || dish.windowName.contains(query)
                    || dish.tags.toString().contains(query);
            if (matchesFloor && matchesWindow && matchesQuery) {
                filtered.add(dish);
            }
        }
        dishAdapter.replaceItems(filtered);
    }
}
