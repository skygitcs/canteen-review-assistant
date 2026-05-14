package edu.thu.canteen;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;

public class FormDialogs {
    public interface ReviewSubmitListener {
        void onSubmit(int rating, String content);
    }

    public static void showSupplementDialog(Context context) {
        LinearLayout form = createForm(context);
        EditText dishName = input(context, "\u83dc\u54c1\u540d\u79f0");
        EditText description = input(context, "\u8865\u5145\u8bf4\u660e");
        EditText tags = input(context, "\u6807\u7b7e\uff0c\u7528\u7a7a\u683c\u6216\u9017\u53f7\u5206\u9694");
        Button upload = new Button(context);
        upload.setText("\u4e0a\u4f20\u56fe\u7247");
        upload.setAllCaps(false);
        upload.setOnClickListener(v -> UiUtils.toast(context, "\u5df2\u9009\u62e9\u56fe\u7247\uff08\u9759\u6001\u6f14\u793a\uff09"));
        form.addView(dishName);
        form.addView(description);
        form.addView(tags);
        form.addView(upload);

        new AlertDialog.Builder(context)
                .setTitle("\u8865\u5145\u83dc\u54c1\u4fe1\u606f")
                .setView(form)
                .setNegativeButton("\u53d6\u6d88", null)
                .setPositiveButton("\u63d0\u4ea4", (dialog, which) ->
                        UiUtils.toast(context, "\u5df2\u63d0\u4ea4\u8865\u5145\u4fe1\u606f"))
                .show();
    }

    public static void showReviewDialog(Context context, ReviewSubmitListener listener) {
        LinearLayout form = createForm(context);
        TextView ratingLabel = new TextView(context);
        ratingLabel.setText("\u6253\u5206");
        RatingBar ratingBar = new RatingBar(context);
        ratingBar.setNumStars(5);
        ratingBar.setStepSize(1f);
        ratingBar.setRating(4f);
        ratingBar.setIsIndicator(false);
        EditText content = input(context, "\u5199\u4e0b\u4f60\u7684\u8bc4\u4ef7");
        EditText tags = input(context, "\u6807\u7b7e\uff0c\u4f8b\u5982 \u6e05\u6de1 \u4e0b\u996d");
        Button upload = new Button(context);
        upload.setText("\u4e0a\u4f20\u56fe\u7247");
        upload.setAllCaps(false);
        upload.setOnClickListener(v -> UiUtils.toast(context, "\u5df2\u9009\u62e9\u56fe\u7247\uff08\u9759\u6001\u6f14\u793a\uff09"));
        form.addView(ratingLabel);
        form.addView(ratingBar);
        form.addView(content);
        form.addView(tags);
        form.addView(upload);

        new AlertDialog.Builder(context)
                .setTitle("\u5199\u8bc4\u4ef7")
                .setView(form)
                .setNegativeButton("\u53d6\u6d88", null)
                .setPositiveButton("\u53d1\u5e03", (dialog, which) -> {
                    String text = content.getText().toString().trim();
                    if (text.isEmpty()) {
                        text = "\u8fd9\u9053\u83dc\u8fd8\u4e0d\u9519\u3002";
                    }
                    listener.onSubmit(Math.round(ratingBar.getRating()), text);
                })
                .show();
    }

    private static LinearLayout createForm(Context context) {
        LinearLayout form = new LinearLayout(context);
        form.setOrientation(LinearLayout.VERTICAL);
        int padding = dp(context, 12);
        form.setPadding(padding, padding, padding, 0);
        return form;
    }

    private static EditText input(Context context, String hint) {
        EditText input = new EditText(context);
        input.setHint(hint);
        input.setSingleLine(false);
        input.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        return input;
    }

    private static int dp(Context context, int value) {
        return Math.round(value * context.getResources().getDisplayMetrics().density);
    }
}
