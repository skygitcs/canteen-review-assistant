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
import edu.thu.canteen.data.model.Canteen;
import edu.thu.canteen.data.model.Dish;
import edu.thu.canteen.data.network.ApiResponse;
import edu.thu.canteen.data.network.CanteenDtos;
import edu.thu.canteen.data.network.NetworkClient;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FormDialogs {
    public interface ReviewSubmitListener {
        void onSubmit(int rating, String content, String imageUrl);
    }

    public interface SupplementSubmitListener {
        void onSubmitted();
    }

    public interface CrowdSubmitListener {
        void onSubmit(int level);
    }

    public interface ProfileEditListener {
        void onUpdate(String nickname, String tastePreference);
    }

    public static void showEditProfileDialog(Context context, edu.thu.canteen.data.model.UserProfile current, ProfileEditListener listener) {
        LinearLayout form = createForm(context);
        TextView title = title(context, "\u7f16\u8f91\u4e2a\u4eba\u8d44\u6599");
        EditText nicknameInput = input(context, "\u6635\u79f0");
        nicknameInput.setText(current.nickname);
        EditText preferenceInput = input(context, "\u53e3\u5473\u504f\u597d");
        preferenceInput.setText(current.tastePreference);

        form.addView(title);
        form.addView(label(context, "\u6635\u79f0"));
        form.addView(nicknameInput);
        form.addView(label(context, "\u53e3\u5473\u504f\u597d"));
        form.addView(preferenceInput);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(form)
                .setNegativeButton("\u53d6\u6d88", null)
                .setPositiveButton("\u4fdd\u5b58", (d, which) -> {
                    String nickname = nicknameInput.getText().toString().trim();
                    String preference = preferenceInput.getText().toString().trim();
                    if (nickname.isEmpty()) {
                        UiUtils.toast(context, "\u6635\u79f0\u4e0d\u80fd\u4e3a\u7a7a");
                        return;
                    }
                    listener.onUpdate(nickname, preference);
                })
                .show();
        widen(dialog);
    }

    public static void showSupplementDialog(Context context, Canteen canteen, SupplementSubmitListener listener) {
        NetworkClient.getService().getCanteenDetail(canteen.id, null, null, null).enqueue(new Callback<ApiResponse<CanteenDtos.CanteenDetailResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<CanteenDtos.CanteenDetailResponse>> call, Response<ApiResponse<CanteenDtos.CanteenDetailResponse>> response) {
                if (response.isSuccessful() && response.body() != null && response.body().data != null) {
                    showSupplementForm(context, response.body().data, listener);
                } else {
                    UiUtils.toast(context, "\u65e0\u6cd5\u83b7\u53d6\u7a97\u53e3\u4fe1\u606f");
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<CanteenDtos.CanteenDetailResponse>> call, Throwable t) {
                UiUtils.toast(context, "\u7f51\u7edc\u9519\u8bef");
            }
        });
    }

    private static void showSupplementForm(Context context, CanteenDtos.CanteenDetailResponse data, SupplementSubmitListener listener) {
        LinearLayout form = createForm(context);
        TextView title = title(context, "\u8865\u5145\u83dc\u54c1\u4fe1\u606f");
        
        TextView windowLabel = label(context, "\u9009\u62e9\u7a97\u53e3");
        android.widget.Spinner windowSpinner = new android.widget.Spinner(context);
        List<String> windowNames = new ArrayList<>();
        for (CanteenDtos.WindowDto w : data.windows) windowNames.add(w.name);
        android.widget.ArrayAdapter<String> adapter = new android.widget.ArrayAdapter<>(context, android.R.layout.simple_spinner_item, windowNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        windowSpinner.setAdapter(adapter);

        EditText dishName = input(context, "\u83dc\u54c1\u540d\u79f0");
        EditText priceInput = input(context, "\u4ef7\u683c");
        priceInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER | android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL);
        EditText descriptionInput = input(context, "\u8865\u5145\u8bf4\u660e");
        EditText tagsInput = input(context, "\u6807\u7b7e\uff0c\u7528\u7a7a\u683c\u6216\u9017\u53f7\u5206\u9694");
        
        TextView spiceLabel = label(context, "\u8fa3\u5ea6 (0-5)");
        android.widget.SeekBar spiceSeekBar = new android.widget.SeekBar(context);
        spiceSeekBar.setMax(5);
        spiceSeekBar.setProgress(0);

        form.addView(title);
        form.addView(windowLabel);
        form.addView(windowSpinner);
        form.addView(dishName);
        form.addView(priceInput);
        form.addView(descriptionInput);
        form.addView(tagsInput);
        form.addView(spiceLabel);
        form.addView(spiceSeekBar);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(form)
                .setNegativeButton("\u53d6\u6d88", null)
                .setPositiveButton("\u63d0\u4ea4", (d, which) -> {
                    String name = dishName.getText().toString().trim();
                    if (name.isEmpty()) {
                        UiUtils.toast(context, "\u8bf7\u8f93\u5165\u83dc\u54c1\u540d\u79f0");
                        return;
                    }
                    int windowIdx = windowSpinner.getSelectedItemPosition();
                    CanteenDtos.WindowDto selectedWindow = data.windows.get(windowIdx);
                    double price = 15.0;
                    try { price = Double.parseDouble(priceInput.getText().toString()); } catch (Exception e) {}

                    Dish newDish = new Dish(
                        0, data.base.id, selectedWindow.id, data.base.name, selectedWindow.name, selectedWindow.floorNo,
                        name, "", price, descriptionInput.getText().toString(), spiceSeekBar.getProgress(),
                        Arrays.asList(tagsInput.getText().toString().split("[,\\s]+")), "\u70ed\u83dc"
                    );
                    
                    submitDish(context, newDish, listener);
                })
                .show();
        widen(dialog);
    }

    private static void submitDish(Context context, Dish dish, SupplementSubmitListener listener) {
        NetworkClient.getService().submitDishSubmission(dish).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful()) {
                    UiUtils.toast(context, "\u5df2\u63d0\u4ea4\u8865\u5145\u4fe1\u606f\uff0c\u8bf7\u7b49\u5f85\u5ba1\u6838");
                    if (listener != null) listener.onSubmitted();
                } else {
                    UiUtils.toast(context, "\u63d0\u4ea4\u5931\u8d25");
                }
            }
            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                UiUtils.toast(context, "\u7f51\u7edc\u9519\u8bef");
            }
        });
    }

    public static void showCrowdDialog(Context context, CrowdSubmitListener listener) {
        LinearLayout form = createForm(context);
        int[] level = {3};
        TextView title = title(context, "\u4e0a\u62a5\u62e5\u6324\u5ea6");
        TextView label = label(context, "\u5f53\u524d\u6392\u961f\u60c5\u51b5\uff081-5\uff0c5\u4e3a\u6700\u62e5\u6324\uff09");

        LinearLayout levels = new LinearLayout(context);
        levels.setGravity(Gravity.CENTER_VERTICAL);
        levels.setOrientation(LinearLayout.HORIZONTAL);
        TextView[] levelViews = new TextView[5];
        for (int i = 0; i < levelViews.length; i++) {
            final int value = i + 1;
            TextView lv = new TextView(context);
            lv.setText(String.valueOf(value));
            lv.setTextSize(24f);
            lv.setGravity(Gravity.CENTER);
            lv.setPadding(dp(context, 10), dp(context, 10), dp(context, 10), dp(context, 10));
            lv.setOnClickListener(v -> {
                level[0] = value;
                renderLevels(levelViews, level[0]);
            });
            levelViews[i] = lv;
            levels.addView(lv);
        }
        renderLevels(levelViews, level[0]);

        form.addView(title);
        form.addView(label);
        form.addView(levels);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(form)
                .setNegativeButton("\u53d6\u6d88", null)
                .setPositiveButton("\u63d0\u4ea4", (d, which) -> {
                    listener.onSubmit(level[0]);
                })
                .show();
        widen(dialog);
    }

    private static void renderLevels(TextView[] views, int selected) {
        for (int i = 0; i < views.length; i++) {
            boolean isSelected = (i + 1) == selected;
            views[i].setTextColor(isSelected ? Color.parseColor("#7C2D12") : Color.parseColor("#CBD5E1"));
            views[i].setTypeface(null, isSelected ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);
        }
    }

    public static void showReviewDialog(Context context, ReviewSubmitListener listener) {
        LinearLayout form = createForm(context);
        int[] rating = {4};
        final String[] uploadedUrl = {null};
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
        Button upload = softButton(context, "\u4e0a\u4f20\u56fe\u7247");
        upload.setOnClickListener(v -> {
            uploadedUrl[0] = "https://picsum.photos/seed/upload" + System.currentTimeMillis() + "/800/600";
            UiUtils.toast(context, "\u5df2\u6a21\u62df\u4e0a\u4f20\u56fe\u7247");
            upload.setText("\u2705 \u5df2\u4e0a\u4f20");
        });
        
        form.addView(title);
        form.addView(ratingLabel);
        form.addView(stars);
        form.addView(content);
        form.addView(upload);

        AlertDialog dialog = new AlertDialog.Builder(context)
                .setView(form)
                .setNegativeButton("\u53d6\u6d88", null)
                .setPositiveButton("\u53d1\u5e03", (d, which) -> {
                    String text = content.getText().toString().trim();
                    if (text.isEmpty()) text = "\u8fd9\u9053\u83dc\u8fd8\u4e0d\u9519\u3002";
                    listener.onSubmit(rating[0], text, uploadedUrl[0]);
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
