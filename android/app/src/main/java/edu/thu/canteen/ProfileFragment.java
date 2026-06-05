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
import edu.thu.canteen.data.model.ActivityItem;
import edu.thu.canteen.data.model.Dish;
import edu.thu.canteen.data.model.UserProfile;
import edu.thu.canteen.data.network.ApiResponse;
import edu.thu.canteen.data.network.NetworkClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private ImageView avatar;
    private TextView name;
    private TextView username;
    private TextView preference;
    private View adminButton;
    private FavoriteAdapter favoriteAdapter;
    private ActivityAdapter activityAdapter;
    private List<Dish> favoritesList = new ArrayList<>();
    private List<ActivityItem> activityItems = new ArrayList<>();
    private UserProfile currentProfile;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        avatar = view.findViewById(R.id.profile_avatar);
        name = view.findViewById(R.id.profile_name);
        username = view.findViewById(R.id.profile_username);
        preference = view.findViewById(R.id.profile_preference);
        adminButton = view.findViewById(R.id.admin_button);

        RecyclerView activityListView = view.findViewById(R.id.activity_list);
        activityListView.setLayoutManager(new LinearLayoutManager(requireContext()));
        activityAdapter = new ActivityAdapter(activityItems);
        activityListView.setAdapter(activityAdapter);

        RecyclerView favoriteListView = view.findViewById(R.id.favorite_list);
        favoriteListView.setLayoutManager(new LinearLayoutManager(requireContext()));
        favoriteAdapter = new FavoriteAdapter(favoritesList, new FavoriteAdapter.OnItemClick() {
            @Override
            public void onClick(Dish dish) {
                Intent intent = new Intent(requireContext(), DishDetailActivity.class);
                intent.putExtra(DishDetailActivity.EXTRA_DISH_ID, dish.id);
                startActivity(intent);
            }

            @Override
            public void onDelete(Dish dish) {
                removeFavorite(dish);
            }
        });
        favoriteListView.setAdapter(favoriteAdapter);

        // Header and Settings
        view.findViewById(R.id.profile_header).setOnClickListener(v -> showAccountDialog());
        view.findViewById(R.id.account_action_button).setOnClickListener(v -> showAccountDialog());
        view.findViewById(R.id.edit_profile_button).setOnClickListener(v -> editProfile());
        
        adminButton.setOnClickListener(v -> {
            startActivity(new Intent(requireContext(), AdminDashboardActivity.class));
        });

        fetchData();

        return view;
    }

    private void fetchData() {
        NetworkClient.getService().getMyProfile().enqueue(new Callback<ApiResponse<UserProfile>>() {
            @Override
            public void onResponse(Call<ApiResponse<UserProfile>> call, Response<ApiResponse<UserProfile>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null && response.body().data != null) {
                    currentProfile = response.body().data;
                    bindProfile(currentProfile);
                    updateActivities(); // Profile needed for username-based logging
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<UserProfile>> call, Throwable t) {}
        });

        NetworkClient.getService().getFavorites().enqueue(new Callback<ApiResponse<List<Dish>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Dish>>> call, Response<ApiResponse<List<Dish>>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null && response.body().data != null) {
                    favoritesList.clear();
                    favoritesList.addAll(response.body().data);
                    favoriteAdapter.notifyDataSetChanged();
                    updateActivities();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<List<Dish>>> call, Throwable t) {}
        });
    }

    private void updateActivities() {
        if (currentProfile == null) return;
        activityItems.clear();
        
        // 1. Remote Favorites (Always synced with server)
        for (Dish d : favoritesList) {
            activityItems.add(new ActivityItem("\u6536\u85cf\u4e86\u83dc\u54c1\uff1a" + d.name, ""));
        }
        
        // 2. Account-Specific Local Activities (Likes, Reviews, Reports)
        // We use username as key for isolation
        String key = "local_act_" + currentProfile.username;
        String data = requireContext().getSharedPreferences("canteen_prefs", 0).getString(key, "");
        if (!data.isEmpty()) {
            for (String act : data.split(";")) {
                if (!act.isEmpty()) activityItems.add(new ActivityItem(act, ""));
            }
        }
        
        activityAdapter.notifyDataSetChanged();
    }

    private void bindProfile(UserProfile profile) {
        UiUtils.loadImage(avatar, profile.avatarUrl, 0, "user");
        name.setText(profile.nickname);
        username.setText("@" + profile.username);
        preference.setText(profile.tastePreference != null && !profile.tastePreference.isEmpty() 
            ? profile.tastePreference : "\u672a\u8bbe\u7f6e\u53e3\u5473\u504f\u597d");
        
        adminButton.setVisibility("ADMIN".equals(profile.role) ? View.VISIBLE : View.GONE);
    }

    private void editProfile() {
        if (currentProfile == null) return;
        FormDialogs.showEditProfileDialog(requireContext(), currentProfile, (nickname, tastePreference) -> {
            UserProfile updated = new UserProfile(currentProfile.username, nickname, tastePreference, currentProfile.avatarUrl, currentProfile.role);
            NetworkClient.getService().updateProfile(updated).enqueue(new Callback<ApiResponse<UserProfile>>() {
                @Override
                public void onResponse(Call<ApiResponse<UserProfile>> call, Response<ApiResponse<UserProfile>> response) {
                    if (response.isSuccessful()) {
                        UiUtils.toast(requireContext(), "\u4fee\u6531\u6210\u529f");
                        fetchData();
                    }
                }
                @Override
                public void onFailure(Call<ApiResponse<UserProfile>> call, Throwable t) {
                    UiUtils.toast(requireContext(), "\u7f51\u7edc\u9519\u8bef");
                }
            });
        });
    }

    private void removeFavorite(Dish dish) {
        NetworkClient.getService().removeFavorite(dish.id).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (isAdded() && response.isSuccessful()) {
                    favoritesList.remove(dish);
                    favoriteAdapter.notifyDataSetChanged();
                    updateActivities();
                    UiUtils.toast(requireContext(), "\u5df2\u53d6\u6d88\u6536\u85cf");
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {}
        });
    }

    private void showAccountDialog() {
        String[] actions = {"\u5207\u6362\u8d26\u53f7", "\u9000\u51fa\u767b\u5f55"};
        new AlertDialog.Builder(requireContext())
                .setTitle("\u8d26\u53f7\u64cd\u4f5c")
                .setItems(actions, (dialog, which) -> {
                    NetworkClient.clearToken();
                    Intent intent = new Intent(requireContext(), AuthActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                })
                .show();
    }
}
