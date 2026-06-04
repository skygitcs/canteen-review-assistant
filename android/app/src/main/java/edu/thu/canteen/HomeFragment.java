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
import edu.thu.canteen.data.model.Canteen;
import edu.thu.canteen.data.model.Dish;
import edu.thu.canteen.data.network.ApiResponse;
import edu.thu.canteen.data.network.NetworkClient;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private LinearLayout announcementList;
    private ImageView recommendCover;
    private TextView recommendName;
    private TextView recommendAddress;
    private TextView recommendRating;
    private ChipGroup recommendTags;
    private RecyclerView featuredListView;
    private DishAdapter dishAdapter;
    private List<Dish> featuredDishes = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        announcementList = view.findViewById(R.id.announcement_list);
        recommendCover = view.findViewById(R.id.recommend_cover);
        recommendName = view.findViewById(R.id.recommend_name);
        recommendAddress = view.findViewById(R.id.recommend_address);
        recommendRating = view.findViewById(R.id.recommend_rating);
        recommendTags = view.findViewById(R.id.recommend_tags);
        featuredListView = view.findViewById(R.id.featured_list);

        featuredListView.setLayoutManager(new LinearLayoutManager(requireContext()));
        dishAdapter = new DishAdapter(featuredDishes, dish -> {
            Intent intent = new Intent(requireContext(), DishDetailActivity.class);
            intent.putExtra(DishDetailActivity.EXTRA_DISH_ID, dish.id);
            startActivity(intent);
        });
        featuredListView.setAdapter(dishAdapter);

        fetchData();

        return view;
    }

    private void fetchData() {
        // Announcements
        NetworkClient.getService().getAnnouncements().enqueue(new Callback<ApiResponse<List<Object>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Object>>> call, Response<ApiResponse<List<Object>>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null && response.body().data != null) {
                    List<String> contents = new ArrayList<>();
                    for (Object obj : response.body().data) {
                        if (obj instanceof Map) {
                            contents.add((String) ((Map<?, ?>) obj).get("content"));
                        }
                    }
                    if (!contents.isEmpty()) {
                        announcementList.removeAllViews();
                        TextView tv = (TextView) getLayoutInflater().inflate(R.layout.item_announcement, announcementList, false);
                        tv.setText(String.join("    \u00b7    ", contents));
                        tv.setSelected(true);
                        announcementList.addView(tv);
                    }
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<List<Object>>> call, Throwable t) {}
        });

        // Recommended Canteen (Get first from list for now)
        NetworkClient.getService().getCanteens(null, null, "rating").enqueue(new Callback<ApiResponse<List<Canteen>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Canteen>>> call, Response<ApiResponse<List<Canteen>>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null && response.body().data != null && !response.body().data.isEmpty()) {
                    bindCanteen(response.body().data.get(0));
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<List<Canteen>>> call, Throwable t) {}
        });

        // Featured Dishes
        NetworkClient.getService().getRecommendations(6).enqueue(new Callback<ApiResponse<List<Dish>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Dish>>> call, Response<ApiResponse<List<Dish>>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null && response.body().data != null) {
                    featuredDishes.clear();
                    featuredDishes.addAll(response.body().data);
                    dishAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<List<Dish>>> call, Throwable t) {}
        });
    }

    private void bindCanteen(Canteen canteen) {
        if (canteen.coverUrl == null || canteen.coverUrl.isEmpty()) {
            recommendCover.setImageDrawable(null);
            recommendCover.setBackgroundResource(R.drawable.bg_image_placeholder);
        } else {
            Glide.with(this).load(canteen.coverUrl).into(recommendCover);
        }
        recommendName.setText(canteen.name);
        recommendAddress.setText(canteen.address);
        recommendRating.setText(String.format("\u8bc4\u5206 %.1f", canteen.avgRating));
        UiUtils.bindTags(recommendTags, canteen.tags);

        View recommendCard = getView().findViewById(R.id.recommend_card);
        recommendCard.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), CanteenDetailActivity.class);
            intent.putExtra(CanteenDetailActivity.EXTRA_CANTEEN_ID, canteen.id);
            startActivity(intent);
        });
    }
}
