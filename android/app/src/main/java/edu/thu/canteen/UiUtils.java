package edu.thu.canteen;

import android.content.Context;
import android.widget.Toast;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import java.util.List;

public class UiUtils {
    public static void bindTags(ChipGroup group, List<String> tags) {
        group.removeAllViews();
        for (String tag : tags) {
            Chip chip = new Chip(group.getContext());
            chip.setText(tag);
            chip.setChipBackgroundColorResource(R.color.purple_100);
            chip.setTextColor(group.getContext().getColor(R.color.purple_700));
            chip.setChipStrokeColorResource(R.color.purple_300);
            chip.setChipStrokeWidth(1f);
            chip.setTextSize(12f);
            group.addView(chip);
        }
    }

    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}

