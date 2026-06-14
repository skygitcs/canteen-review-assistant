package edu.thu.canteen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.chip.ChipGroup;
import edu.thu.canteen.data.model.DishSubmission;
import java.util.List;

public class SubmissionAdapter extends RecyclerView.Adapter<SubmissionAdapter.ViewHolder> {
    public interface OnActionClick {
        void onApprove(DishSubmission submission);
        void onReject(DishSubmission submission);
    }

    private final List<DishSubmission> items;
    private final OnActionClick listener;

    public SubmissionAdapter(List<DishSubmission> items, OnActionClick listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_submission, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DishSubmission item = items.get(position);
        holder.name.setText(item.name);
        
        String cName = item.canteenName != null ? item.canteenName : "\u98df\u5802ID:" + item.canteenId;
        String wName = item.windowName != null ? item.windowName : "\u7a97\u53e3ID:" + item.windowId;
        int floor = item.floorNo > 0 ? item.floorNo : 0; // Default to 0 if not returned

        holder.canteen.setText(String.format("%s - %d\u697c %s", cName, floor, wName));
        String submitter = item.submitterName != null && !item.submitterName.isEmpty()
                ? item.submitterName
                : "\u7528\u6237ID:" + item.submitterId;
        holder.meta.setText(String.format("\u4e0a\u4f20\u8005\uff1a%s  \u4ef7\u683c\uff1a%s  \u8fa3\u5ea6\uff1a%d/5",
                submitter, UiUtils.formatPrice(item.price), item.spiceLevel));

        if (item.description != null && !item.description.trim().isEmpty()) {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(item.description);
        } else {
            holder.description.setVisibility(View.GONE);
        }

        UiUtils.bindTags(holder.tags, item.tags);

        if (item.imageUrl != null && !item.imageUrl.trim().isEmpty()) {
            holder.image.setVisibility(View.VISIBLE);
            String resolvedUrl = UiUtils.resolveMediaUrl(item.imageUrl);
            UiUtils.loadImage(holder.image, item.imageUrl, item.id, "submission");
            holder.image.setOnClickListener(v -> {
                if (v.getContext() instanceof FragmentActivity) {
                    ImageViewerDialog.show((FragmentActivity) v.getContext(), resolvedUrl);
                }
            });
        } else {
            holder.image.setVisibility(View.GONE);
            holder.image.setOnClickListener(null);
        }

        holder.approve.setOnClickListener(v -> listener.onApprove(item));
        holder.reject.setOnClickListener(v -> listener.onReject(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView canteen;
        final TextView meta;
        final TextView description;
        final ChipGroup tags;
        final ImageView image;
        final Button approve;
        final Button reject;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_submission_name);
            canteen = itemView.findViewById(R.id.item_submission_canteen);
            meta = itemView.findViewById(R.id.item_submission_meta);
            description = itemView.findViewById(R.id.item_submission_description);
            tags = itemView.findViewById(R.id.item_submission_tags);
            image = itemView.findViewById(R.id.item_submission_image);
            approve = itemView.findViewById(R.id.item_submission_approve);
            reject = itemView.findViewById(R.id.item_submission_reject);
        }
    }
}

