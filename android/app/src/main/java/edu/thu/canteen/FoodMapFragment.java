package edu.thu.canteen;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import edu.thu.canteen.data.MockRepository;
import edu.thu.canteen.data.model.Canteen;
import java.util.ArrayList;
import java.util.List;

public class FoodMapFragment extends Fragment {
    private final List<Canteen> filtered = new ArrayList<>();
    private CanteenAdapter canteenAdapter;
    private HeatAdapter heatAdapter;
    private String activeTag = "\u5168\u90e8";
    private String query = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_food_map, container, false);

        EditText searchInput = view.findViewById(R.id.map_search_input);
        ChipGroup tagGroup = view.findViewById(R.id.map_tag_group);
        RecyclerView canteenList = view.findViewById(R.id.canteen_list);
        RecyclerView heatList = view.findViewById(R.id.heat_list);

        for (String tag : MockRepository.getCanteenTags()) {
            Chip chip = (Chip) inflater.inflate(R.layout.item_tag_chip, tagGroup, false);
            chip.setText(tag);
            UiUtils.styleTagChip(chip, tag);
            chip.setOnClickListener(v -> {
                activeTag = tag;
                updateFilters();
            });
            tagGroup.addView(chip);
        }

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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                query = s.toString();
                updateFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        updateFilters();
        return view;
    }

    private void updateFilters() {
        filtered.clear();
        String trimmedQuery = query.trim();
        for (Canteen canteen : MockRepository.getCanteens()) {
            boolean matchesTag = "\u5168\u90e8".equals(activeTag) || canteen.tags.contains(activeTag);
            boolean matchesQuery = trimmedQuery.isEmpty()
                    || canteen.name.contains(trimmedQuery)
                    || canteen.address.contains(trimmedQuery)
                    || canteen.tags.toString().contains(trimmedQuery);
            if (matchesTag && matchesQuery) {
                filtered.add(canteen);
            }
        }
        canteenAdapter.notifyDataSetChanged();
        heatAdapter.notifyDataSetChanged();
    }
}
