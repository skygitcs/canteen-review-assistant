package edu.thu.canteen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.chip.ChipGroup;
import edu.thu.canteen.data.model.Canteen;
import java.util.List;

public class CanteenAdapter extends RecyclerView.Adapter<CanteenAdapter.ViewHolder> {
    public interface OnItemClick {
        void onClick(Canteen canteen);
    }

    private final List<Canteen> items;
    private final OnItemClick listener;

    public CanteenAdapter(List<Canteen> items, OnItemClick listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_canteen, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Canteen item = items.get(position);
        holder.name.setText(item.name);
        holder.address.setText(item.address);
        holder.rating.setText(String.format("%.1f", item.avgRating));
        holder.crowd.setText(String.format("%.1f", item.crowdLevel));
        UiUtils.bindTags(holder.tags, item.tags);
        if (item.coverUrl == null || item.coverUrl.isEmpty()) {
            holder.cover.setImageDrawable(null);
            holder.cover.setBackgroundResource(R.drawable.bg_image_placeholder);
        } else {
            Glide.with(holder.cover.getContext()).load(item.coverUrl).into(holder.cover);
        }
        holder.itemView.setOnClickListener(v -> listener.onClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView cover;
        final TextView name;
        final TextView address;
        final TextView rating;
        final TextView crowd;
        final ChipGroup tags;

        ViewHolder(View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.item_canteen_cover);
            name = itemView.findViewById(R.id.item_canteen_name);
            address = itemView.findViewById(R.id.item_canteen_address);
            rating = itemView.findViewById(R.id.item_canteen_rating);
            crowd = itemView.findViewById(R.id.item_canteen_crowd);
            tags = itemView.findViewById(R.id.item_canteen_tags);
        }
    }
}
