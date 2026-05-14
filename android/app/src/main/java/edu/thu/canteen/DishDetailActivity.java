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
import com.google.android.material.chip.ChipGroup;
import edu.thu.canteen.data.MockRepository;
import edu.thu.canteen.data.model.Dish;
import edu.thu.canteen.data.model.Review;
import java.util.List;

public class DishDetailActivity extends AppCompatActivity {
    public static final String EXTRA_DISH_ID = "extra_dish_id";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_detail);

        long dishId = getIntent().getLongExtra(EXTRA_DISH_ID, 101L);
        Dish dish = MockRepository.getDishById(dishId);

        ImageView cover = findViewById(R.id.dish_cover);
        TextView name = findViewById(R.id.dish_name);
        TextView canteen = findViewById(R.id.dish_canteen);
        TextView price = findViewById(R.id.dish_price);
        TextView description = findViewById(R.id.dish_description);
        ChipGroup tags = findViewById(R.id.dish_tags);

        Glide.with(this).load(dish.imageUrl).into(cover);
        name.setText(dish.name);
        canteen.setText(dish.canteenName);
        price.setText(String.format("$%.2f", dish.price));
        description.setText(dish.description);
        UiUtils.bindTags(tags, dish.tags);

        RecyclerView reviewList = findViewById(R.id.review_list);
        reviewList.setLayoutManager(new LinearLayoutManager(this));
        List<Review> reviews = MockRepository.getReviewsByDish(dishId);
        reviewList.setAdapter(new ReviewAdapter(reviews));

        Button writeReview = findViewById(R.id.write_review_button);
        writeReview.setOnClickListener(v -> UiUtils.toast(this, "Write review"));
    }
}

