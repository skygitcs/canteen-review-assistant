package edu.thu.canteen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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
        
        holder.canteen.setText(String.format("%s - %d\u697c %s (\u00a5%.2f)", 
                cName, floor, wName, item.price));
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
        final Button approve;
        final Button reject;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_submission_name);
            canteen = itemView.findViewById(R.id.item_submission_canteen);
            approve = itemView.findViewById(R.id.item_submission_approve);
            reject = itemView.findViewById(R.id.item_submission_reject);
        }
    }
}

