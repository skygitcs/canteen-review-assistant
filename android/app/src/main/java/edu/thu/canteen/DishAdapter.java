package edu.thu.canteen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.android.material.chip.ChipGroup;
import edu.thu.canteen.data.model.Dish;
import java.util.List;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.ViewHolder> {
    public interface OnItemClick {
        void onClick(Dish dish);
    }

    private final List<Dish> items;
    private final OnItemClick listener;

    public DishAdapter(List<Dish> items, OnItemClick listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dish, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Dish item = items.get(position);
        holder.name.setText(item.name);
        holder.window.setText(item.floorNo + "\u697c \u00b7 " + item.windowName);
        holder.price.setText(UiUtils.formatPrice(item.price));
        holder.rating.setText(String.format("\u2605 %.1f\n%d\u6761", item.avgRating, item.reviewCount));
        UiUtils.bindTags(holder.tags, item.tags);
        UiUtils.loadImage(holder.cover, item.imageUrl, item.id, "dish");
        
        // Click cover to zoom
        holder.cover.setOnClickListener(v -> {
            if (v.getContext() instanceof FragmentActivity) {
                String finalUrl = (item.imageUrl != null && !item.imageUrl.isEmpty())
                    ? UiUtils.resolveMediaUrl(item.imageUrl)
                    : String.format("https://picsum.photos/seed/dish%d/800/600", item.id);
                ImageViewerDialog.show((FragmentActivity) v.getContext(), finalUrl);
            }
        });
        holder.itemView.setOnClickListener(v -> listener.onClick(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void replaceItems(List<Dish> nextItems) {
        android.util.Log.d("DishAdapter", "Replacing items, new count: " + nextItems.size());
        items.clear();
        items.addAll(nextItems);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView cover;
        final TextView name;
        final TextView window;
        final TextView price;
        final TextView rating;
        final ChipGroup tags;

        ViewHolder(View itemView) {
            super(itemView);
            cover = itemView.findViewById(R.id.item_dish_cover);
            name = itemView.findViewById(R.id.item_dish_name);
            window = itemView.findViewById(R.id.item_dish_window);
            price = itemView.findViewById(R.id.item_dish_price);
            rating = itemView.findViewById(R.id.item_dish_rating);
            tags = itemView.findViewById(R.id.item_dish_tags);
        }
    }
}
