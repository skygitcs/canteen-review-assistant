package edu.thu.canteen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import edu.thu.canteen.data.model.Dish;
import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {
    public interface OnItemClick {
        void onClick(Dish dish);
    }

    private final List<Dish> items;
    private final OnItemClick listener;

    public FavoriteAdapter(List<Dish> items, OnItemClick listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Dish item = items.get(position);
        holder.name.setText(item.name);
        holder.canteen.setText(item.canteenName);
        holder.price.setText(String.format("\u00a5%.2f", item.price));
        if (item.imageUrl == null || item.imageUrl.isEmpty()) {
            holder.cover.setImageDrawable(null);
            holder.cover.setBackgroundResource(R.drawable.bg_image_placeholder);
        } else {
            Glide.with(holder.cover.getContext()).load(item.imageUrl).into(holder.cover);
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
        final TextView canteen;
        final TextView price;

        ViewHolder(View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.item_favorite_cover);
            name = itemView.findViewById(R.id.item_favorite_name);
            canteen = itemView.findViewById(R.id.item_favorite_canteen);
            price = itemView.findViewById(R.id.item_favorite_price);
        }
    }
}
