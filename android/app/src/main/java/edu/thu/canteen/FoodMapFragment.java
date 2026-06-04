package edu.thu.canteen;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import edu.thu.canteen.data.model.Canteen;
import edu.thu.canteen.data.network.ApiResponse;
import edu.thu.canteen.data.network.NetworkClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodMapFragment extends Fragment {
    private List<Canteen> allCanteens = new ArrayList<>();
    private final List<Canteen> filtered = new ArrayList<>();
    private CanteenAdapter canteenAdapter;
    private HeatAdapter heatAdapter;
    private String activeTag = "\u5168\u90e8";
    private String query = "";

    private View listViewContainer;
    private View mapViewContainer;
    private PhotoView mapPhotoView;
    private FrameLayout markerContainer;
    private ChipGroup tagGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_map, container, false);

        listViewContainer = view.findViewById(R.id.list_view_container);
        mapViewContainer = view.findViewById(R.id.map_view_container);
        mapPhotoView = view.findViewById(R.id.map_photo_view);
        markerContainer = view.findViewById(R.id.marker_container);
        tagGroup = view.findViewById(R.id.map_tag_group);

        EditText searchInput = view.findViewById(R.id.map_search_input);
        RecyclerView canteenList = view.findViewById(R.id.canteen_list);
        RecyclerView heatList = view.findViewById(R.id.heat_list);

        MaterialButton btnSwitchMap = view.findViewById(R.id.btn_switch_map);
        MaterialButton btnSwitchList = view.findViewById(R.id.btn_switch_list);

        btnSwitchMap.setOnClickListener(v -> showMapView());
        btnSwitchList.setOnClickListener(v -> showListView());

        canteenAdapter = new CanteenAdapter(filtered, canteen -> {
            Intent intent = new Intent(requireContext(), CanteenDetailActivity.class);
            intent.putExtra(CanteenDetailActivity.EXTRA_CANTEEN_ID, canteen.id);
            startActivity(intent);
        });
        canteenList.setLayoutManager(new LinearLayoutManager(requireContext()));
        canteenList.setAdapter(canteenAdapter);

        heatAdapter = new HeatAdapter(filtered);
        heatList.setLayoutManager(new LinearLayoutManager(requireContext()));
        heatList.setAdapter(heatAdapter);

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                query = s.toString();
                updateFilters();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        fetchData();
        return view;
    }

    private void fetchData() {
        NetworkClient.getService().getCanteens(null, null, "rating").enqueue(new Callback<ApiResponse<List<Canteen>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Canteen>>> call, Response<ApiResponse<List<Canteen>>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null && response.body().data != null) {
                    allCanteens.clear();
                    allCanteens.addAll(response.body().data);
                    updateFilters();
                    setupTags();
                    setupMap();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<List<Canteen>>> call, Throwable t) {}
        });
    }

    private void setupTags() {
        if (!isAdded()) return;
        tagGroup.removeAllViews();
        List<String> tags = new ArrayList<>();
        tags.add("\u5168\u90e8");
        for (Canteen c : allCanteens) {
            if (c.tags != null) {
                for (String t : c.tags) {
                    if (!tags.contains(t)) tags.add(t);
                }
            }
        }

        for (String tag : tags) {
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.item_tag_chip, tagGroup, false);
            chip.setText(tag);
            UiUtils.styleTagChip(chip, tag);
            chip.setOnClickListener(v -> {
                activeTag = tag;
                updateFilters();
            });
            tagGroup.addView(chip);
        }
    }

    private void showMapView() {
        listViewContainer.setVisibility(View.GONE);
        mapViewContainer.setVisibility(View.VISIBLE);
    }

    private void showListView() {
        mapViewContainer.setVisibility(View.GONE);
        listViewContainer.setVisibility(View.VISIBLE);
    }

    private void setupMap() {
        mapPhotoView.setImageResource(R.drawable.bg_image_placeholder);
        markerContainer.removeAllViews();
        // Just add markers for first few canteens with dummy positions
        for (int i = 0; i < Math.min(allCanteens.size(), 3); i++) {
            Canteen c = allCanteens.get(i);
            addMarker(200 + i * 150, 300 + i * 100, c);
        }
    }

    private void addMarker(float x, float y, Canteen canteen) {
        View marker = new View(requireContext());
        marker.setBackgroundResource(android.R.drawable.ic_dialog_map);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(80, 80);
        params.leftMargin = (int) x;
        params.topMargin = (int) y;
        marker.setLayoutParams(params);
        marker.setOnClickListener(v -> showCanteenPopup(canteen));
        markerContainer.addView(marker);
    }

    private void showCanteenPopup(Canteen canteen) {
        String tagList = canteen.tags != null ? String.join(", ", canteen.tags) : "\u65e0";
        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle(canteen.name)
                .setMessage("\u4f4d\u7f6e: " + canteen.address + "\n\u6807\u7b7e: " + tagList)
                .setPositiveButton("\u67e5\u770b\u8be6\u60c5", (d, w) -> {
                    Intent intent = new Intent(requireContext(), CanteenDetailActivity.class);
                    intent.putExtra(CanteenDetailActivity.EXTRA_CANTEEN_ID, canteen.id);
                    startActivity(intent);
                })
                .setNegativeButton("\u5173\u95ed", null)
                .show();
    }

    private void updateFilters() {
        filtered.clear();
        String trimmedQuery = query.trim();
        for (Canteen canteen : allCanteens) {
            boolean matchesTag = "\u5168\u90e8".equals(activeTag) || (canteen.tags != null && canteen.tags.contains(activeTag));
            boolean matchesQuery = trimmedQuery.isEmpty()
                    || canteen.name.contains(trimmedQuery)
                    || canteen.address.contains(trimmedQuery);
            if (matchesTag && matchesQuery) {
                filtered.add(canteen);
            }
        }
        if (canteenAdapter != null) canteenAdapter.notifyDataSetChanged();
        if (heatAdapter != null) heatAdapter.notifyDataSetChanged();
    }
}
