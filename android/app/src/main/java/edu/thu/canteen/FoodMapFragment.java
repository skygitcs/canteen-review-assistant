package edu.thu.canteen;

import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import edu.thu.canteen.data.model.Canteen;
import edu.thu.canteen.data.network.ApiResponse;
import edu.thu.canteen.data.network.CanteenDtos;
import edu.thu.canteen.data.network.NetworkClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodMapFragment extends Fragment {
    private static class MarkerData {
        String name;
        float xPercent;
        float yPercent;
        View view;

        MarkerData(String name, float x, float y) {
            this.name = name;
            this.xPercent = x;
            this.yPercent = y;
        }
    }

    private final List<MarkerData> markerDataList = new ArrayList<>();
    private List<Canteen> allCanteens = new ArrayList<>();
    private final List<Canteen> filtered = new ArrayList<>();
    private final List<CanteenDtos.HeatPoint> heatPoints = new ArrayList<>();
    private CanteenAdapter canteenAdapter;
    private HeatAdapter heatAdapter;
    private String activeTag = "\u5168\u90e8";
    private String query = "";

    private View listViewContainer;
    private View mapViewContainer;
    private PhotoView mapPhotoView;
    private FrameLayout markerContainer;
    private ChipGroup tagGroup;
    private View emptyText;
    private TextView titleView;

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
        emptyText = view.findViewById(R.id.food_map_empty_text);
        titleView = view.findViewById(R.id.food_map_title);

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

        heatAdapter = new HeatAdapter(heatPoints);
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
                    activeTag = "\u5168\u90e8";
                    updateFilters();
                    setupTags();
                    setupMap();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<List<Canteen>>> call, Throwable t) {}
        });

        NetworkClient.getService().getHeatmap("global").enqueue(new Callback<ApiResponse<List<CanteenDtos.HeatPoint>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<CanteenDtos.HeatPoint>>> call, Response<ApiResponse<List<CanteenDtos.HeatPoint>>> response) {
                if (isAdded() && response.isSuccessful() && response.body() != null && response.body().data != null) {
                    heatPoints.clear();
                    heatPoints.addAll(response.body().data);
                    if (heatAdapter != null) heatAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<List<CanteenDtos.HeatPoint>>> call, Throwable t) {}
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
        // Re-setup markers when showing map, to ensure container dimensions are available
        setupMap();
    }

    private void showListView() {
        mapViewContainer.setVisibility(View.GONE);
        listViewContainer.setVisibility(View.VISIBLE);
    }

    private void setupMap() {
        // Load the campus map from the backend
        String mapUrl = NetworkClient.BASE_URL + "uploads/dishes/canteen_map.png";
        
        Glide.with(this)
                .load(mapUrl)
                .placeholder(R.drawable.bg_image_placeholder)
                .error(R.drawable.bg_image_placeholder)
                .listener(new com.bumptech.glide.request.RequestListener<android.graphics.drawable.Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable com.bumptech.glide.load.engine.GlideException e, Object model, com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target, boolean isFirstResource) {
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(android.graphics.drawable.Drawable resource, Object model, com.bumptech.glide.request.target.Target<android.graphics.drawable.Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                        mapPhotoView.post(() -> updateMarkerPositions());
                        return false;
                    }
                })
                .into(mapPhotoView);

        // Initialize static data once - matching DB names
        if (markerDataList.isEmpty()) {
            markerDataList.add(new MarkerData("\u7d2b\u8346", 0.63f, 0.12f));
            markerDataList.add(new MarkerData("\u6843\u674e", 0.39f, 0.18f));
            markerDataList.add(new MarkerData("\u7389\u6811", 0.72f, 0.17f));
            markerDataList.add(new MarkerData("\u4e01\u9999", 0.58f, 0.30f));
            markerDataList.add(new MarkerData("\u542c\u6d9b", 0.47f, 0.39f));
            markerDataList.add(new MarkerData("\u6e05\u82ac", 0.62f, 0.43f));
            markerDataList.add(new MarkerData("\u89c2\u7574", 0.23f, 0.34f));
        }

        // Listen for zooms and pans to reposition markers
        mapPhotoView.setOnMatrixChangeListener(rect -> updateMarkerPositions());

        if (mapViewContainer.getVisibility() != View.VISIBLE) return;

        markerContainer.removeAllViews();
        for (MarkerData data : markerDataList) {
            data.view = createMarkerView(data);
            markerContainer.addView(data.view);
        }
        
        // Initial positioning
        markerContainer.post(this::updateMarkerPositions);
    }

    private void updateMarkerPositions() {
        RectF rect = mapPhotoView.getDisplayRect();
        if (rect == null) return;

        float imgWidth = rect.width();
        float imgHeight = rect.height();
        float imgLeft = rect.left;
        float imgTop = rect.top;

        int markerSize = (int) (32 * getResources().getDisplayMetrics().density);

        for (MarkerData data : markerDataList) {
            if (data.view == null) continue;
            
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) data.view.getLayoutParams();
            params.leftMargin = (int) (imgLeft + (imgWidth * data.xPercent)) - (markerSize / 2);
            params.topMargin = (int) (imgTop + (imgHeight * data.yPercent)) - (markerSize / 2);
            data.view.setLayoutParams(params);
        }
    }

    private View createMarkerView(MarkerData data) {
        View marker = new View(requireContext());
        
        // Fully transparent as requested
        marker.setBackgroundColor(android.graphics.Color.TRANSPARENT);

        int markerSize = (int) (48 * getResources().getDisplayMetrics().density);
        marker.setLayoutParams(new FrameLayout.LayoutParams(markerSize, markerSize));

        // Find the canteen object by name for popup details
        Canteen targetCanteen = null;
        for (Canteen c : allCanteens) {
            if (data.name.contains(c.name) || c.name.contains(data.name)) {
                targetCanteen = c;
                break;
            }
        }

        Canteen finalCanteen = targetCanteen;
        marker.setOnClickListener(v -> {
            if (finalCanteen != null) {
                showCanteenPopup(finalCanteen);
            } else {
                UiUtils.toast(requireContext(), "\u70b9\u51fb\u4e86: " + data.name);
            }
        });
        return marker;
    }

    private void showCanteenPopup(Canteen canteen) {
        BottomSheetDialog dialog = new BottomSheetDialog(requireContext());
        View view = getLayoutInflater().inflate(R.layout.dialog_canteen_preview, null);

        ImageView cover = view.findViewById(R.id.dialog_canteen_cover);
        TextView name = view.findViewById(R.id.dialog_canteen_name);
        TextView address = view.findViewById(R.id.dialog_canteen_address);
        TextView rating = view.findViewById(R.id.dialog_canteen_rating);
        TextView crowd = view.findViewById(R.id.dialog_canteen_crowd);
        ChipGroup tags = view.findViewById(R.id.dialog_canteen_tags);
        View btnDetail = view.findViewById(R.id.btn_go_to_detail);

        UiUtils.loadImage(cover, canteen.coverUrl, canteen.id, "canteen");
        name.setText(canteen.name);
        address.setText(canteen.address);
        rating.setText(String.format("\u2b50 %.1f", canteen.avgRating));
        crowd.setText(String.format("\ud83d\udc65 \u62e5\u6324 %.1f", canteen.crowdLevel));
        UiUtils.bindTags(tags, canteen.tags);

        btnDetail.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(requireContext(), CanteenDetailActivity.class);
            intent.putExtra(CanteenDetailActivity.EXTRA_CANTEEN_ID, canteen.id);
            startActivity(intent);
        });

        dialog.setContentView(view);
        dialog.show();
    }

    private void updateFilters() {
        filtered.clear();
        String trimmedQuery = query.trim();
        for (Canteen canteen : allCanteens) {
            boolean matchesTag = "\u5168\u90e8".equals(activeTag) || (canteen.tags != null && canteen.tags.contains(activeTag));
            boolean matchesQuery = trimmedQuery.isEmpty()
                    || canteen.name.contains(trimmedQuery)
                    || canteen.address.contains(trimmedQuery)
                    || matchesCanteenTag(canteen, trimmedQuery);
            if (matchesTag && matchesQuery) {
                filtered.add(canteen);
            }
        }
        if (canteenAdapter != null) canteenAdapter.notifyDataSetChanged();
        if (heatAdapter != null) heatAdapter.notifyDataSetChanged();
        if (emptyText != null) emptyText.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
        if (titleView != null) titleView.setText("\u5168\u90e8\u98df\u5802\uff08" + filtered.size() + "\uff09");
    }

    private boolean matchesCanteenTag(Canteen canteen, String trimmedQuery) {
        if (trimmedQuery.isEmpty() || canteen.tags == null) return false;
        for (String tag : canteen.tags) {
            if (tag != null && tag.contains(trimmedQuery)) return true;
        }
        return false;
    }
}
