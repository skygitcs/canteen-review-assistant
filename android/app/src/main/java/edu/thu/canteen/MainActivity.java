package edu.thu.canteen;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_nav);
        showTab(resolveTab(getIntent()));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        showTab(resolveTab(intent));
    }

    private String resolveTab(Intent intent) {
        return intent.getStringExtra(NavigationHelper.EXTRA_TAB) == null
                ? NavigationHelper.TAB_HOME
                : intent.getStringExtra(NavigationHelper.EXTRA_TAB);
    }

    private void showTab(String tab) {
        Fragment fragment;
        if (NavigationHelper.TAB_FOOD_MAP.equals(tab)) {
            fragment = new FoodMapFragment();
        } else if (NavigationHelper.TAB_PROFILE.equals(tab)) {
            fragment = new ProfileFragment();
        } else {
            tab = NavigationHelper.TAB_HOME;
            fragment = new HomeFragment();
        }
        NavigationHelper.bind(this, bottomNav, tab);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
