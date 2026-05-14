package edu.thu.canteen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import edu.thu.canteen.data.MockRepository;
import edu.thu.canteen.data.model.ActivityItem;
import edu.thu.canteen.data.model.Dish;
import edu.thu.canteen.data.model.UserProfile;
import java.util.List;

public class ProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        UserProfile profile = MockRepository.getUserProfile();
        ImageView avatar = view.findViewById(R.id.profile_avatar);
        TextView name = view.findViewById(R.id.profile_name);
        TextView username = view.findViewById(R.id.profile_username);
        TextView preference = view.findViewById(R.id.profile_preference);

        if (profile.avatarUrl == null || profile.avatarUrl.isEmpty()) {
            avatar.setImageDrawable(null);
            avatar.setBackgroundResource(R.drawable.bg_image_placeholder);
        } else {
            Glide.with(requireContext()).load(profile.avatarUrl).into(avatar);
        }
        name.setText(profile.nickname);
        username.setText("@" + profile.username);
        preference.setText(profile.tastePreference);

        RecyclerView activityList = view.findViewById(R.id.activity_list);
        activityList.setLayoutManager(new LinearLayoutManager(requireContext()));
        List<ActivityItem> activities = MockRepository.getRecentActivities();
        activityList.setAdapter(new ActivityAdapter(activities));

        RecyclerView favoriteList = view.findViewById(R.id.favorite_list);
        favoriteList.setLayoutManager(new LinearLayoutManager(requireContext()));
        List<Dish> favorites = MockRepository.getFavoriteDishes();
        favoriteList.setAdapter(new FavoriteAdapter(favorites, dish -> {
            Intent intent = new Intent(requireContext(), DishDetailActivity.class);
            intent.putExtra(DishDetailActivity.EXTRA_DISH_ID, dish.id);
            startActivity(intent);
        }));

        view.findViewById(R.id.support_button).setOnClickListener(v ->
                UiUtils.toast(requireContext(), "\u7559\u8a00\u5165\u53e3")
        );
        view.findViewById(R.id.profile_header).setOnClickListener(v -> showAccountDialog());

        return view;
    }

    private void showAccountDialog() {
        String[] actions = {
                "\u5207\u6362\u8d26\u53f7",
                "\u9000\u51fa\u767b\u5f55"
        };
        new AlertDialog.Builder(requireContext())
                .setTitle("\u8d26\u53f7\u64cd\u4f5c")
                .setItems(actions, (dialog, which) -> {
                    Intent intent = new Intent(requireContext(), AuthActivity.class);
                    startActivity(intent);
                })
                .show();
    }
}
