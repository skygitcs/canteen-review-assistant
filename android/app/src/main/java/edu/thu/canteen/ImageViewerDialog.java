package edu.thu.canteen;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

public class ImageViewerDialog extends DialogFragment {
    private static final String ARG_URL = "arg_url";

    public static void show(FragmentActivity activity, String url) {
        ImageViewerDialog dialog = new ImageViewerDialog();
        Bundle args = new Bundle();
        args.putString(ARG_URL, url);
        dialog.setArguments(args);
        dialog.show(activity.getSupportFragmentManager(), "image_viewer");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_image_viewer, container, false);
        PhotoView photoView = view.findViewById(R.id.photo_view);
        ImageButton closeButton = view.findViewById(R.id.btn_close);

        String url = getArguments() != null ? getArguments().getString(ARG_URL) : "";
        if (url != null && !url.isEmpty()) {
            Glide.with(this).load(url).into(photoView);
        }

        closeButton.setOnClickListener(v -> dismiss());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog() != null ? getDialog().getWindow() : null;
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }
}
