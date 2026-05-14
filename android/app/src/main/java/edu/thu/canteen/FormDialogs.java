package edu.thu.canteen;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;

public class FormDialogs {
    public interface ReviewSubmitListener {
        void onSubmit(int rating, String content);
    }

    public static void showSupplementDialog(Context context) {
        LinearLayout form = createForm(context);
        TextView title = title(context, "\u8865\u5145\u83dc\u54c1\u4fe1\u606f");
        EditText dishName = input(context, "\u83dc\u54c1\u540d\u79f0");
        EditText description = input(context, "\u8865\u5145\u8bf4\u660e");
        EditText tags = input(context, "\u6807\u7b7e\uff0c\u7528\u7a7a\u683c\u6216\u9017\u53f7\u5206\u9694");
        Button upload = softButton(context, "\u4e0a\u4f20\u56fe\u7247");
        upload.setOnClickListener(v -> UiUtils.toast(context, "\u5df2\u9009\u62e9\u56fe\u7247\uff08\u9759\u6001\u6f14\u793a\uff09"));
        form.addView(title);
        form.addView(dishName);
        form.addView(description);
        form.addView(tags);
        form.addView(upload);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(form)
                .setNegativeButton("\u53d6\u6d88", null)
                .setPositiveButton("\u63d0\u4ea4", (d, which) ->
                        UiUtils.toast(context, "\u5df2\u63d0\u4ea4\u8865\u5145\u4fe1\u606f"))
                .show();
        widen(dialog);
    }

    public static void showReviewDialog(Context context, ReviewSubmitListener listener) {
        LinearLayout form = createForm(context);
        int[] rating = {4};
        TextView title = title(context, "\u5199\u8bc4\u4ef7");
        TextView ratingLabel = label(context, "\u6253\u5206");
        LinearLayout stars = new LinearLayout(context);
        stars.setGravity(Gravity.CENTER_VERTICAL);
        stars.setOrientation(LinearLayout.HORIZONTAL);
        TextView[] starViews = new TextView[5];
        for (int i = 0; i < starViews.length; i++) {
            final int value = i + 1;
            TextView star = new TextView(context);
            star.setText("\u2605");
            star.setTextSize(34f);
            star.setGravity(Gravity.CENTER);
            star.setPadding(dp(context, 2), 0, dp(context, 6), 0);
            star.setOnClickListener(v -> {
                rating[0] = value;
                renderStars(starViews, rating[0]);
            });
            starViews[i] = star;
            stars.addView(star);
        }
        renderStars(starViews, rating[0]);

        EditText content = input(context, "\u5199\u4e0b\u4f60\u7684\u8bc4\u4ef7");
        EditText tags = input(context, "\u6807\u7b7e\uff0c\u4f8b\u5982 \u6e05\u6de1 \u4e0b\u996d");
        Button upload = softButton(context, "\u4e0a\u4f20\u56fe\u7247");
        upload.setOnClickListener(v -> UiUtils.toast(context, "\u5df2\u9009\u62e9\u56fe\u7247\uff08\u9759\u6001\u6f14\u793a\uff09"));
        form.addView(title);
        form.addView(ratingLabel);
        form.addView(stars);
        form.addView(content);
        form.addView(tags);
        form.addView(upload);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(form)
                .setNegativeButton("\u53d6\u6d88", null)
                .setPositiveButton("\u53d1\u5e03", (d, which) -> {
                    String text = content.getText().toString().trim();
                    if (text.isEmpty()) {
                        text = "\u8fd9\u9053\u83dc\u8fd8\u4e0d\u9519\u3002";
                    }
                    listener.onSubmit(rating[0], text);
                })
                .show();
        widen(dialog);
    }

    private static LinearLayout createForm(Context context) {
        LinearLayout form = new LinearLayout(context);
        form.setOrientation(LinearLayout.VERTICAL);
        int padding = dp(context, 20);
        form.setPadding(padding, padding, padding, dp(context, 8));
        form.setBackgroundResource(R.drawable.bg_dialog_soft);
        return form;
    }

    private static TextView title(Context context, String text) {
        TextView view = new TextView(context);
        view.setText(text);
        view.setTextColor(Color.parseColor("#7C2D12"));
        view.setTextSize(18f);
        view.setTypeface(null, android.graphics.Typeface.BOLD);
        view.setPadding(0, 0, 0, dp(context, 12));
        return view;
    }

    private static TextView label(Context context, String text) {
        TextView view = new TextView(context);
        view.setText(text);
        view.setTextColor(Color.parseColor("#9A3412"));
        view.setTextSize(13f);
        view.setPadding(0, 0, 0, dp(context, 4));
        return view;
    }

    private static EditText input(Context context, String hint) {
        EditText input = new EditText(context);
        input.setHint(hint);
        input.setSingleLine(false);
        input.setMinLines(1);
        input.setBackgroundResource(R.drawable.bg_input);
        input.setPadding(dp(context, 12), 0, dp(context, 12), 0);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(context, 48)
        );
        params.setMargins(0, 0, 0, dp(context, 10));
        input.setLayoutParams(params);
        return input;
    }

    private static Button softButton(Context context, String text) {
        Button button = new Button(context);
        button.setText(text);
        button.setAllCaps(false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                dp(context, 44)
        );
        params.setMargins(0, 0, 0, dp(context, 4));
        button.setLayoutParams(params);
        return button;
    }

    private static void renderStars(TextView[] stars, int rating) {
        for (int i = 0; i < stars.length; i++) {
            stars[i].setTextColor(i < rating ? Color.parseColor("#F59E0B") : Color.parseColor("#CBD5E1"));
        }
    }

    private static void widen(Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    private static int dp(Context context, int value) {
        return Math.round(value * context.getResources().getDisplayMetrics().density);
    }
}
