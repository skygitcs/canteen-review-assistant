import { useMemo } from "react";
import { Link } from "react-router-dom";
import Tag from "../components/Tag.jsx";
import Rating from "../components/Rating.jsx";
import { announcements, canteens, dishes } from "../data/mockData.js";

export default function Home() {
  const recommended = useMemo(() => {
    const index = Math.floor(Math.random() * canteens.length);
    return canteens[index];
  }, []);

  const featured = dishes.filter((dish) =>
    recommended.featuredDishIds.includes(dish.id)
  );

  return (
    <div className="space-y-6">
      <section className="rounded-2xl bg-white p-4 shadow-sm">
        <div className="text-xs font-semibold uppercase text-gray-400">Announcements</div>
        <div className="mt-2 space-y-2">
          {announcements.map((item) => (
            <div key={item.id} className="rounded-xl bg-gray-50 px-3 py-2 text-sm">
              <div className="font-semibold text-gray-800">{item.title}</div>
              <div className="text-xs text-gray-500">{item.content}</div>
            </div>
          ))}
        </div>
      </section>

      <section>
        <div className="mb-3 text-sm font-semibold text-gray-700">Today Recommendation</div>
        <Link
          to={`/canteens/${recommended.id}`}
          className="block overflow-hidden rounded-2xl bg-white shadow-sm transition hover:-translate-y-0.5"
        >
          <img
            src={recommended.coverUrl}
            alt={recommended.name}
            className="h-40 w-full object-cover"
          />
          <div className="space-y-3 p-4">
            <div className="space-y-1">
              <div className="text-lg font-semibold text-gray-900">{recommended.name}</div>
              <div className="text-xs text-gray-500">{recommended.address}</div>
            </div>
            <div className="flex flex-wrap gap-2">
              {recommended.tags.map((tag) => (
                <Tag key={tag} label={tag} />
              ))}
            </div>
            <Rating value={recommended.avgRating} count={recommended.dishCount} />
          </div>
        </Link>
      </section>

      <section className="space-y-3">
        <div className="text-sm font-semibold text-gray-700">Featured Dishes</div>
        <div className="grid gap-3">
          {featured.map((dish) => (
            <Link
              key={dish.id}
              to={`/dishes/${dish.id}`}
              className="flex items-center gap-3 rounded-2xl bg-white p-3 shadow-sm"
            >
              <img
                src={dish.imageUrl}
                alt={dish.name}
                className="h-16 w-16 rounded-xl object-cover"
              />
              <div className="flex-1">
                <div className="text-sm font-semibold text-gray-900">{dish.name}</div>
                <div className="text-xs text-gray-500">{dish.description}</div>
                <div className="mt-1 flex flex-wrap gap-1">
                  {dish.tags.map((tag) => (
                    <Tag key={tag} label={tag} />
                  ))}
                </div>
              </div>
              <div className="text-sm font-semibold text-gray-900">${dish.price}</div>
            </Link>
          ))}
        </div>
      </section>
    </div>
  );
}

