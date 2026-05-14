package edu.thu.canteen;

import android.app.Activity;
import android.content.Intent;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class NavigationHelper {
    public static final String EXTRA_TAB = "extra_tab";
    public static final String TAB_HOME = "home";
    public static final String TAB_FOOD_MAP = "food_map";
    public static final String TAB_PROFILE = "profile";

    public static void bind(Activity activity, BottomNavigationView bottomNav, String selectedTab) {
        bottomNav.setOnItemSelectedListener(null);
        if (TAB_HOME.equals(selectedTab)) {
            bottomNav.setSelectedItemId(R.id.menu_home);
        } else if (TAB_FOOD_MAP.equals(selectedTab)) {
            bottomNav.setSelectedItemId(R.id.menu_food_map);
        } else if (TAB_PROFILE.equals(selectedTab)) {
            bottomNav.setSelectedItemId(R.id.menu_profile);
        }

        bottomNav.setOnItemSelectedListener(item -> {
            String targetTab;
            if (item.getItemId() == R.id.menu_home) {
                targetTab = TAB_HOME;
            } else if (item.getItemId() == R.id.menu_food_map) {
                targetTab = TAB_FOOD_MAP;
            } else {
                targetTab = TAB_PROFILE;
            }
            if (targetTab.equals(selectedTab) && activity instanceof MainActivity) {
                return true;
            }
            Intent intent = new Intent(activity, MainActivity.class);
            intent.putExtra(EXTRA_TAB, targetTab);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivity(intent);
            return true;
        });
    }
}
