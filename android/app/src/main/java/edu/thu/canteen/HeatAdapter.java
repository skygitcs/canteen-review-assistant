package edu.thu.canteen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.thu.canteen.data.network.CanteenDtos;
import java.util.List;

public class HeatAdapter extends RecyclerView.Adapter<HeatAdapter.ViewHolder> {
    private final List<CanteenDtos.HeatPoint> items;

    public HeatAdapter(List<CanteenDtos.HeatPoint> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_heat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CanteenDtos.HeatPoint item = items.get(position);
        holder.name.setText(item.canteenName);
        holder.value.setText(String.format("Heat %d", item.visits));
        // Scale height based on visits (max out at some reasonable value)
        int height = (int) (16 + Math.min(item.visits, 10) * 18);
        ViewGroup.LayoutParams params = holder.heatBar.getLayoutParams();
        params.height = height;
        holder.heatBar.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView value;
        final View heatBar;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.heat_name);
            value = itemView.findViewById(R.id.heat_value);
            heatBar = itemView.findViewById(R.id.heat_bar);
        }
    }
}

