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
      {/* 公告栏 - 滚动 */}
      <section className="overflow-hidden rounded-2xl bg-white/70 backdrop-blur-sm shadow-sm">
        <div className="flex items-center gap-2 px-4 py-2">
          <span className="text-xs font-semibold text-blue-500 shrink-0">📢 公告</span>
          <div className="overflow-hidden flex-1">
            <div className="animate-marquee whitespace-nowrap text-xs text-gray-500">
              <span className="mr-10">{announcements[0].title} — {announcements[0].content}</span>
              <span className="mr-10">{announcements[1].title} — {announcements[1].content}</span>
              <span className="mr-10">{announcements[0].title} — {announcements[0].content}</span>
              <span className="mr-10">{announcements[1].title} — {announcements[1].content}</span>
            </div>
          </div>
        </div>
      </section>

      {/* 今日推荐 */}
      <section>
        <div className="mb-3 text-sm font-semibold text-gray-700">🍚 今日推荐</div>
        <Link
          to={`/canteens/${recommended.id}`}
          className="block overflow-hidden rounded-2xl bg-white shadow-sm transition hover:-translate-y-0.5"
        >
          <div className="h-40 w-full bg-gradient-to-br from-blue-100 to-purple-100 flex items-center justify-center">
            <span className="text-3xl">🏫</span>
          </div>
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

      {/* 特色菜品 */}
      <section className="space-y-3">
        <div className="text-sm font-semibold text-gray-700">🔥 特色菜品推荐</div>
        <div className="grid gap-3">
          {featured.map((dish) => (
            <Link
              key={dish.id}
              to={`/dishes/${dish.id}`}
              className="flex items-center gap-3 rounded-2xl bg-white p-3 shadow-sm"
            >
              <div className="h-16 w-16 rounded-xl bg-gradient-to-br from-orange-100 to-yellow-100 flex items-center justify-center shrink-0">
                <span className="text-xl">🍽️</span>
              </div>
              <div className="flex-1 min-w-0">
                <div className="text-sm font-semibold text-gray-900">{dish.name}</div>
                <div className="text-xs text-gray-500 truncate">{dish.description}</div>
                <div className="mt-1 flex flex-wrap gap-1">
                  {dish.tags.map((tag) => (
                    <Tag key={tag} label={tag} />
                  ))}
                </div>
              </div>
              <div className="text-sm font-semibold text-gray-900 whitespace-nowrap">¥{dish.price}</div>
            </Link>
          ))}
        </div>
      </section>
    </div>
  );
}

