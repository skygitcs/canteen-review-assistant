package edu.thu.canteen;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import edu.thu.canteen.data.network.NetworkClient;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class UiUtils {
    public static void bindTags(ChipGroup group, List<String> tags) {
        group.removeAllViews();
        for (String tag : normalizeTags(tags)) {
            Chip chip = new Chip(group.getContext());
            chip.setText(tag);
            styleTagChip(chip, tag);
            group.addView(chip);
        }
    }

    public static List<String> normalizeTags(List<String> tags) {
        Set<String> result = new LinkedHashSet<>();
        if (tags == null) return new ArrayList<>();
        for (String raw : tags) {
            if (raw == null) continue;
            for (String tag : raw.split("[,，、\\s]+")) {
                String trimmed = tag.trim();
                if (!trimmed.isEmpty()) result.add(trimmed);
            }
        }
        return new ArrayList<>(result);
    }

    public static void styleTagChip(Chip chip, String tag) {
        int[] colors = getTagColors(tag == null ? "" : tag);
        chip.setChipBackgroundColor(ColorStateList.valueOf(colors[0]));
        chip.setTextColor(colors[1]);
        chip.setChipStrokeColor(ColorStateList.valueOf(colors[2]));
        chip.setChipStrokeWidth(1f);
        chip.setTextSize(12f);
    }

    public static void styleSelectableTagChip(Chip chip, String tag, boolean checked) {
        if (!checked) {
            styleTagChip(chip, tag);
            chip.setAlpha(0.75f);
            return;
        }
        int[] colors = getTagColors(tag == null ? "" : tag);
        chip.setChipBackgroundColor(ColorStateList.valueOf(colors[1]));
        chip.setTextColor(Color.WHITE);
        chip.setChipStrokeColor(ColorStateList.valueOf(colors[1]));
        chip.setChipStrokeWidth(1f);
        chip.setTextSize(12f);
        chip.setAlpha(1f);
    }

    public static int[] getTagColors(String tag) {
        if (tag.contains("\u8fa3") || tag.contains("\u5ddd\u6e58")) {
            return colors("#FEE2E2", "#B91C1C", "#FCA5A5");
        }
        if (tag.contains("\u6e05\u6de1") || tag.contains("\u5065\u5eb7")
                || tag.contains("\u8f7b\u98df") || tag.contains("\u6c64")) {
            return colors("#DCFCE7", "#15803D", "#86EFAC");
        }
        if (tag.contains("\u751c") || tag.contains("\u70d8\u7119")
                || tag.contains("\u5496\u5561") || tag.contains("\u51b0\u996e")) {
            return colors("#FEF3C7", "#B45309", "#FCD34D");
        }
        if (tag.contains("\u9762") || tag.contains("\u7c89") || tag.contains("\u4e3b\u98df")) {
            return colors("#E0F2FE", "#0369A1", "#7DD3FC");
        }
        if (tag.contains("\u7c73\u996d") || tag.contains("\u76d6\u996d") || tag.contains("\u4e0b\u996d")) {
            return colors("#F5F5F4", "#57534E", "#D6D3D1");
        }
        if (tag.contains("\u86cb\u767d") || tag.contains("\u8089") || tag.contains("\u9c7c") || tag.contains("\u8364\u83dc")) {
            return colors("#FFE4E6", "#BE123C", "#FDA4AF");
        }
        if (tag.contains("\u5b9e\u60e0") || tag.contains("\u6027\u4ef7\u6bd4")) {
            return colors("#ECFCCB", "#4D7C0F", "#BEF264");
        }
        return colors("#EEF2FF", "#4338CA", "#C7D2FE");
    }

    private static int[] colors(String background, String text, String stroke) {
        return new int[] {
                Color.parseColor(background),
                Color.parseColor(text),
                Color.parseColor(stroke)
        };
    }

    public static void toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void loadImage(ImageView imageView, String url, long seed, String type) {
        if (url != null && !url.isEmpty()) {
            Glide.with(imageView.getContext()).load(resolveMediaUrl(url)).into(imageView);
        } else {
            String fallbackUrl = String.format("https://picsum.photos/seed/%s%d/800/600", type, seed);
            Glide.with(imageView.getContext()).load(fallbackUrl).into(imageView);
        }
    }

    public static String resolveMediaUrl(String url) {
        if (url == null || url.isEmpty()) return url;
        if (url.startsWith("http://") || url.startsWith("https://")) return url;
        String base = NetworkClient.BASE_URL.endsWith("/")
                ? NetworkClient.BASE_URL.substring(0, NetworkClient.BASE_URL.length() - 1)
                : NetworkClient.BASE_URL;
        return url.startsWith("/") ? base + url : base + "/" + url;
    }

    public static String formatPrice(double price) {
        return price <= 0 ? "\u79f0\u91cd\u8ba1\u4ef7" : String.format("\u00a5%.2f", price);
    }
}
