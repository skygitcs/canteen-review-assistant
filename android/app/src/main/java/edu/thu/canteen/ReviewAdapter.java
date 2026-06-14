package edu.thu.canteen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import edu.thu.canteen.data.model.Review;
import edu.thu.canteen.data.network.ApiResponse;
import edu.thu.canteen.data.network.DishDtos;
import edu.thu.canteen.data.network.NetworkClient;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private final List<Review> items;
    private Runnable refreshTrigger;

    public ReviewAdapter(List<Review> items) {
        this.items = items;
    }

    public void setOnVoteListener(Runnable refreshTrigger) {
        this.refreshTrigger = refreshTrigger;
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
            holder.image.setVisibility(View.GONE);
        } else {
            holder.image.setVisibility(View.VISIBLE);
            String resolvedImageUrl = UiUtils.resolveMediaUrl(item.imageUrl);
            Glide.with(holder.image.getContext()).load(resolvedImageUrl).into(holder.image);
            holder.image.setOnClickListener(v -> {
                android.content.Context context = holder.itemView.getContext();
                if (context instanceof androidx.fragment.app.FragmentActivity) {
                    ImageViewerDialog.show((androidx.fragment.app.FragmentActivity) context, resolvedImageUrl);
                }
            });
        }
        
        holder.upvote.setText("\uD83D\uDC4D " + item.upVotes);
        holder.downvote.setText("\uD83D\uDC4E " + item.downVotes);

        // Highlight based on userVote
        holder.upvote.setAlpha(item.userVote == 1 ? 1.0f : 0.5f);
        holder.downvote.setAlpha(item.userVote == -1 ? 1.0f : 0.5f);

        holder.upvote.setOnClickListener(v -> {
            int targetVote = item.userVote == 1 ? 0 : 1;
            handleVote(item.id, targetVote);
        });
        holder.downvote.setOnClickListener(v -> {
            int targetVote = item.userVote == -1 ? 0 : -1;
            handleVote(item.id, targetVote);
        });
    }

    private void handleVote(long reviewId, int vote) {
        Review item = null;
        for (Review r : items) if (r.id == reviewId) { item = r; break; }
        final Review finalItem = item;

        NetworkClient.getService().voteReview(reviewId, new DishDtos.VoteRequest(vote))
                .enqueue(new Callback<ApiResponse<Object>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<Object>> call, Response<ApiResponse<Object>> response) {
                        if (response.isSuccessful()) {
                            if (vote != 0 && finalItem != null) {
                                String action = vote == 1 ? "\u70b9\u8d5e\u4e86\u8bc4\u4ef7\uff1a" : "\u70b9\u8e29\u4e86\u8bc4\u4ef7\uff1a";
                                String desc = finalItem.content.length() > 10 ? finalItem.content.substring(0, 10) + "..." : finalItem.content;
                                NetworkClient.addLocalActivity(action + desc);
                            }
                            if (refreshTrigger != null) {
                                refreshTrigger.run(); // Reload from server to get accurate counts
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {}
                });
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
        final TextView upvote;
        final TextView downvote;

        ViewHolder(View itemView) {
            super(itemView);
            user = itemView.findViewById(R.id.item_review_user);
            rating = itemView.findViewById(R.id.item_review_rating);
            content = itemView.findViewById(R.id.item_review_content);
            image = itemView.findViewById(R.id.item_review_image);
            upvote = itemView.findViewById(R.id.item_review_upvote);
            downvote = itemView.findViewById(R.id.item_review_downvote);
        }
    }
}
