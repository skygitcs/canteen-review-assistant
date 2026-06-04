package edu.thu.canteen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    public interface OnItemClick {
        void onClick(String item);
    }

    private final List<String> items;
    private final OnItemClick listener;
    private int selectedIndex = 0;

    public CategoryAdapter(List<String> items) {
        this(items, null);
    }

    public CategoryAdapter(List<String> items, OnItemClick listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String item = items.get(position);
        holder.text.setText(item);
        holder.text.setSelected(position == selectedIndex);
        holder.itemView.setOnClickListener(v -> {
            selectedIndex = holder.getAdapterPosition();
            notifyDataSetChanged();
            if (listener != null) {
                listener.onClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView text;

        ViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.item_category_text);
        }
    }
}
