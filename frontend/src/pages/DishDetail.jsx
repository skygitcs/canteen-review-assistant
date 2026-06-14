import { useMemo, useState } from "react";
import { useParams } from "react-router-dom";
import Tag from "../components/Tag.jsx";
import { dishes, reviews } from "../data/mockData.js";

export default function DishDetail() {
  const { id } = useParams();
  const dish = dishes.find((item) => String(item.id) === id) || dishes[0];
  const dishReviews = useMemo(
    () => reviews.filter((review) => review.dishId === dish.id),
    [dish.id]
  );
  const [likes, setLikes] = useState(() =>
    Object.fromEntries(dishReviews.map((review) => [review.id, review.up]))
  );
  const [showForm, setShowForm] = useState(false);

  const handleLike = (reviewId) => {
    setLikes((prev) => ({
      ...prev,
      [reviewId]: (prev[reviewId] || 0) + 1
    }));
  };

  return (
    <div className="space-y-5">
      <section className="overflow-hidden rounded-2xl bg-white shadow-sm">
        <img src={dish.imageUrl} alt={dish.name} className="h-44 w-full object-cover" />
        <div className="space-y-2 p-4">
          <div className="text-lg font-semibold text-gray-900">{dish.name}</div>
          <div className="text-xs text-gray-500">{dish.canteenName}</div>
          <div className="flex flex-wrap gap-2">
            {dish.tags.map((tag) => (
              <Tag key={tag} label={tag} />
            ))}
          </div>
          <div className="text-sm font-semibold text-gray-900">${dish.price}</div>
          <div className="text-xs text-gray-500">{dish.description}</div>
        </div>
      </section>

      <section className="rounded-2xl bg-white p-4 shadow-sm">
        <div className="flex items-center justify-between">
          <div className="text-sm font-semibold text-gray-700">Reviews</div>
          <button
            onClick={() => setShowForm((prev) => !prev)}
            className="rounded-full border border-gray-200 px-3 py-1 text-xs text-gray-600"
          >
            Write review
          </button>
        </div>
        {showForm ? (
          <div className="mt-3 rounded-xl border border-gray-200 p-3 text-sm">
            <div className="text-xs text-gray-500">New review</div>
            <textarea
              rows={3}
              className="mt-2 w-full rounded-lg border border-gray-200 p-2 text-sm"
              placeholder="Share your experience"
            />
            <button className="mt-2 rounded-lg bg-gray-900 px-3 py-1 text-xs font-semibold text-white">
              Submit
            </button>
          </div>
        ) : null}
        <div className="mt-4 space-y-3">
          {dishReviews.map((review) => (
            <div key={review.id} className="rounded-xl bg-gray-50 p-3">
              <div className="flex items-center justify-between">
                <div className="text-sm font-semibold text-gray-800">{review.userName}</div>
                <div className="text-xs text-gray-400">{review.createdAt}</div>
              </div>
              <div className="mt-2 text-sm text-gray-700">{review.content}</div>
              <img
                src={review.imageUrl}
                alt={review.content}
                className="mt-2 h-28 w-full rounded-lg object-cover"
              />
              <div className="mt-2 flex items-center justify-between text-xs">
                <div className="text-gray-500">Rating {review.rating}</div>
                <button
                  onClick={() => handleLike(review.id)}
                  className="rounded-full border border-gray-200 px-3 py-1 text-gray-600"
                >
                  Like {likes[review.id] || 0}
                </button>
              </div>
            </div>
          ))}
          {dishReviews.length === 0 ? (
            <div className="rounded-xl border border-dashed border-gray-200 p-4 text-center text-xs text-gray-500">
              No reviews yet. Be the first to comment.
            </div>
          ) : null}
        </div>
      </section>
    </div>
  );
}

