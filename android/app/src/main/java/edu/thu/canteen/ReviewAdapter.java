package edu.thu.canteen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import edu.thu.canteen.data.model.Review;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private final List<Review> items;

    public ReviewAdapter(List<Review> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review item = items.get(position);
        holder.user.setText(item.userName);
        holder.rating.setText(String.format("\u8bc4\u5206 %d", item.rating));
        holder.content.setText(item.content);
        if (item.imageUrl == null || item.imageUrl.isEmpty()) {
            holder.image.setImageDrawable(null);
            holder.image.setBackgroundResource(R.drawable.bg_image_placeholder);
        } else {
            Glide.with(holder.image.getContext()).load(item.imageUrl).into(holder.image);
        }
        holder.like.setOnClickListener(v ->
                UiUtils.toast(holder.itemView.getContext(), "\u5df2\u70b9\u8d5e"));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addReview(Review review) {
        items.add(0, review);
        notifyItemInserted(0);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView user;
        final TextView rating;
        final TextView content;
        final ImageView image;
        final Button like;

        ViewHolder(View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.item_review_user);
            rating = itemView.findViewById(R.id.item_review_rating);
            content = itemView.findViewById(R.id.item_review_content);
            image = itemView.findViewById(R.id.item_review_image);
            like = itemView.findViewById(R.id.item_review_like);
        }
    }
}
