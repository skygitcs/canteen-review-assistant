package edu.thu.canteen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.thu.canteen.data.model.Canteen;
import java.util.List;

public class HeatAdapter extends RecyclerView.Adapter<HeatAdapter.ViewHolder> {
    private final List<Canteen> items;

    public HeatAdapter(List<Canteen> items) {
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
        Canteen item = items.get(position);
        holder.name.setText(item.name);
        holder.value.setText(String.format("Heat %.1f", item.crowdLevel));
        int height = (int) (16 + item.crowdLevel * 18);
        View bar = holder.itemView.findViewById(R.id.heat_bar);
        ViewGroup.LayoutParams params = bar.getLayoutParams();
        params.height = height;
        bar.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView name;
        final TextView value;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.heat_name);
            value = itemView.findViewById(R.id.heat_value);
        }
    }
}

