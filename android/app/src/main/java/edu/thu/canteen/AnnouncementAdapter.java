package edu.thu.canteen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.thu.canteen.data.network.CanteenDtos;
import java.util.List;

public class AnnouncementAdapter extends RecyclerView.Adapter<AnnouncementAdapter.ViewHolder> {
    private final List<CanteenDtos.AnnouncementDto> items;

    public AnnouncementAdapter(List<CanteenDtos.AnnouncementDto> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_announcement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CanteenDtos.AnnouncementDto item = items.get(position);
        holder.title.setText(item.title);
        holder.content.setText(item.content);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView title;
        final TextView content;

        ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.announce_title);
            content = itemView.findViewById(R.id.announce_content);
        }
    }
}
