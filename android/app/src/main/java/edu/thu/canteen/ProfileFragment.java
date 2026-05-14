package edu.thu.canteen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
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
        favoriteList.setAdapter(new FavoriteAdapter(favorites));

        view.findViewById(R.id.support_button).setOnClickListener(v ->
                UiUtils.toast(requireContext(), "\u7559\u8a00\u5165\u53e3")
        );

        return view;
    }
}
