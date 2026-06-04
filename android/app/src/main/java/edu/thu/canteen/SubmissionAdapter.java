package edu.thu.canteen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.thu.canteen.data.model.AdminSubmission;
import java.util.List;

public class SubmissionAdapter extends RecyclerView.Adapter<SubmissionAdapter.ViewHolder> {
    private final List<AdminSubmission> items;

    public SubmissionAdapter(List<AdminSubmission> items) {
        this.items = items;
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
        AdminSubmission item = items.get(position);
        holder.name.setText(item.name);
        holder.canteen.setText(item.canteenName);
        holder.approve.setOnClickListener(v -> UiUtils.toast(holder.itemView.getContext(), "Approve"));
        holder.reject.setOnClickListener(v -> UiUtils.toast(holder.itemView.getContext(), "Reject"));
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

