package edu.thu.canteen;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.chip.ChipGroup;
import edu.thu.canteen.data.MockRepository;
import edu.thu.canteen.data.model.Canteen;
import edu.thu.canteen.data.model.Dish;
import java.util.List;

public class CanteenDetailActivity extends AppCompatActivity {
    public static final String EXTRA_CANTEEN_ID = "extra_canteen_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canteen_detail);

        long canteenId = getIntent().getLongExtra(EXTRA_CANTEEN_ID, 1L);
        Canteen canteen = MockRepository.getCanteenById(canteenId);

        ImageView cover = findViewById(R.id.canteen_cover);
        TextView name = findViewById(R.id.canteen_name);
        TextView address = findViewById(R.id.canteen_address);
        ChipGroup tags = findViewById(R.id.canteen_tags);

        Glide.with(this).load(canteen.coverUrl).into(cover);
        name.setText(canteen.name);
        address.setText(canteen.address);
        UiUtils.bindTags(tags, canteen.tags);

        Spinner floorSpinner = findViewById(R.id.floor_spinner);
        Spinner windowSpinner = findViewById(R.id.window_spinner);
        ArrayAdapter<String> floorAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, MockRepository.getFloorFilters(canteen));
        floorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        floorSpinner.setAdapter(floorAdapter);

        ArrayAdapter<String> windowAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, MockRepository.getWindowFilters(canteen));
        windowAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        windowSpinner.setAdapter(windowAdapter);

        RecyclerView categoryList = findViewById(R.id.category_list);
        categoryList.setLayoutManager(new LinearLayoutManager(this));
        CategoryAdapter categoryAdapter = new CategoryAdapter(MockRepository.getCategories(canteen));
        categoryList.setAdapter(categoryAdapter);

        RecyclerView dishList = findViewById(R.id.canteen_dish_list);
        dishList.setLayoutManager(new LinearLayoutManager(this));
        List<Dish> dishes = MockRepository.getDishesByCanteen(canteen);
        DishAdapter dishAdapter = new DishAdapter(dishes, dish -> {
            Intent intent = new Intent(this, DishDetailActivity.class);
            intent.putExtra(DishDetailActivity.EXTRA_DISH_ID, dish.id);
            startActivity(intent);
        });
        dishList.setAdapter(dishAdapter);

        Button addDish = findViewById(R.id.add_dish_button);
        addDish.setOnClickListener(v -> UiUtils.toast(this, "Add dish info"));
    }
}

