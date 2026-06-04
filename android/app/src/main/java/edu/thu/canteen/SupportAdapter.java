package edu.thu.canteen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.thu.canteen.data.model.SupportMessage;
import java.util.List;

public class SupportAdapter extends RecyclerView.Adapter<SupportAdapter.ViewHolder> {
    private final List<SupportMessage> items;

    public SupportAdapter(List<SupportMessage> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_support, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SupportMessage item = items.get(position);
        holder.user.setText(item.user);
        holder.message.setText(item.message);
        holder.reply.setOnClickListener(v -> UiUtils.toast(holder.itemView.getContext(), "Reply"));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView user;
        final TextView message;
        final Button reply;

        ViewHolder(View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.item_support_user);
            message = itemView.findViewById(R.id.item_support_message);
            reply = itemView.findViewById(R.id.item_support_reply);
        }
    }
}

