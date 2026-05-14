package edu.thu.canteen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.chip.ChipGroup;
import edu.thu.canteen.data.MockRepository;
import edu.thu.canteen.data.model.Canteen;
import edu.thu.canteen.data.model.Dish;
import java.util.List;

public class HomeFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        LinearLayout announcementList = view.findViewById(R.id.announcement_list);
        TextView announcement = (TextView) inflater.inflate(R.layout.item_announcement, announcementList, false);
        announcement.setText(String.join("    \u00b7    ", MockRepository.getAnnouncements()));
        announcement.setSelected(true);
        announcementList.addView(announcement);

        Canteen recommended = MockRepository.getRecommendedCanteen();
        ImageView cover = view.findViewById(R.id.recommend_cover);
        TextView name = view.findViewById(R.id.recommend_name);
        TextView address = view.findViewById(R.id.recommend_address);
        TextView rating = view.findViewById(R.id.recommend_rating);
        ChipGroup tags = view.findViewById(R.id.recommend_tags);

        if (recommended.coverUrl == null || recommended.coverUrl.isEmpty()) {
            cover.setImageDrawable(null);
            cover.setBackgroundResource(R.drawable.bg_image_placeholder);
        } else {
            Glide.with(this).load(recommended.coverUrl).into(cover);
        }
        name.setText(recommended.name);
        address.setText(recommended.address);
        rating.setText(String.format("\u8bc4\u5206 %.1f", recommended.avgRating));
        UiUtils.bindTags(tags, recommended.tags);

        View recommendCard = view.findViewById(R.id.recommend_card);
        recommendCard.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), CanteenDetailActivity.class);
            intent.putExtra(CanteenDetailActivity.EXTRA_CANTEEN_ID, recommended.id);
            startActivity(intent);
        });

        RecyclerView featuredList = view.findViewById(R.id.featured_list);
        featuredList.setLayoutManager(new LinearLayoutManager(requireContext()));
        List<Dish> featured = MockRepository.getFeaturedDishes(recommended);
        DishAdapter adapter = new DishAdapter(featured, dish -> {
            Intent intent = new Intent(requireContext(), DishDetailActivity.class);
            intent.putExtra(DishDetailActivity.EXTRA_DISH_ID, dish.id);
            startActivity(intent);
        });
        featuredList.setAdapter(adapter);

        return view;
    }
}
