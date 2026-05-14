package edu.thu.canteen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.thu.canteen.data.model.ActivityItem;
import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {
    private final List<ActivityItem> items;

    public ActivityAdapter(List<ActivityItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ActivityItem item = items.get(position);
        holder.title.setText(item.title);
        holder.time.setText(item.time);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView time;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_activity_title);
            time = itemView.findViewById(R.id.item_activity_time);
        }
    }
}

