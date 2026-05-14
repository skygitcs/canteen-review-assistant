import { useMemo, useState } from "react";
import { Link, useParams } from "react-router-dom";
import SearchBar from "../components/SearchBar.jsx";
import Tag from "../components/Tag.jsx";
import Rating from "../components/Rating.jsx";
import { canteens, dishes } from "../data/mockData.js";

export default function CanteenDetail() {
  const { id } = useParams();
  const canteen = canteens.find((item) => String(item.id) === id) || canteens[0];
  const canteenDishes = dishes.filter((dish) => dish.canteenId === canteen.id);

  const [query, setQuery] = useState("");
  const [tagFilter, setTagFilter] = useState("all");
  const [floorFilter, setFloorFilter] = useState("all");
  const [windowFilter, setWindowFilter] = useState("all");
  const [activeCategory, setActiveCategory] = useState("all");

  const tags = useMemo(() => {
    const allTags = canteenDishes.flatMap((dish) => dish.tags);
    return ["all", ...new Set(allTags)];
  }, [canteenDishes]);

  const categories = useMemo(() => {
    const all = canteenDishes.map((dish) => dish.category);
    return ["all", ...new Set(all)];
  }, [canteenDishes]);

  const floors = useMemo(() => {
    const all = canteen.windows.map((window) => String(window.floorNo));
    return ["all", ...new Set(all)];
  }, [canteen.windows]);

  const windows = useMemo(() => {
    return [
      { id: "all", name: "All" },
      ...canteen.windows.map((window) => ({ id: String(window.id), name: window.name }))
    ];
  }, [canteen.windows]);

  const filtered = canteenDishes.filter((dish) => {
    const matchesQuery = dish.name.toLowerCase().includes(query.trim().toLowerCase());
    const matchesTag = tagFilter === "all" || dish.tags.includes(tagFilter);
    const matchesFloor = floorFilter === "all" || String(dish.floorNo) === floorFilter;
    const matchesWindow = windowFilter === "all" || String(dish.windowId) === windowFilter;
    const matchesCategory = activeCategory === "all" || dish.category === activeCategory;
    return matchesQuery && matchesTag && matchesFloor && matchesWindow && matchesCategory;
  });

  return (
    <div className="space-y-5">
      <section className="overflow-hidden rounded-2xl bg-white shadow-sm">
        <img src={canteen.coverUrl} alt={canteen.name} className="h-40 w-full object-cover" />
        <div className="space-y-2 p-4">
          <div className="text-lg font-semibold text-gray-900">{canteen.name}</div>
          <div className="text-xs text-gray-500">{canteen.address}</div>
          <div className="flex flex-wrap gap-2">
            {canteen.tags.map((tag) => (
              <Tag key={tag} label={tag} />
            ))}
          </div>
          <Rating value={canteen.avgRating} count={canteen.dishCount} />
        </div>
      </section>

      <section className="space-y-3">
        <SearchBar value={query} onChange={setQuery} placeholder="Search dishes" />
        <div className="grid gap-2 text-xs">
          <div className="flex flex-wrap gap-2">
            {tags.map((tag) => (
              <button
                key={tag}
                onClick={() => setTagFilter(tag)}
                className={`rounded-full border px-3 py-1 ${
                  tagFilter === tag
                    ? "border-purple-400 bg-purple-100 text-purple-700"
                    : "border-gray-200 bg-white text-gray-600"
                }`}
              >
                {tag}
              </button>
            ))}
          </div>
          <div className="flex gap-2">
            <select
              value={floorFilter}
              onChange={(event) => setFloorFilter(event.target.value)}
              className="flex-1 rounded-xl border border-gray-200 bg-white px-2 py-2"
            >
              {floors.map((floor) => (
                <option key={floor} value={floor}>
                  Floor {floor}
                </option>
              ))}
            </select>
            <select
              value={windowFilter}
              onChange={(event) => setWindowFilter(event.target.value)}
              className="flex-1 rounded-xl border border-gray-200 bg-white px-2 py-2"
            >
              {windows.map((window) => (
                <option key={window.id} value={window.id}>
                  {window.name}
                </option>
              ))}
            </select>
          </div>
        </div>
      </section>

      <section className="rounded-2xl bg-white p-4 shadow-sm">
        <div className="flex items-center justify-between">
          <div className="text-sm font-semibold text-gray-700">Menu</div>
          <button className="rounded-full border border-gray-200 px-3 py-1 text-xs text-gray-600">
            Add dish info
          </button>
        </div>
        <div className="mt-4 flex gap-3">
          <div className="w-24 space-y-2">
            {categories.map((category) => (
              <button
                key={category}
                onClick={() => setActiveCategory(category)}
                className={`w-full rounded-xl px-2 py-2 text-xs font-medium ${
                  activeCategory === category
                    ? "bg-gray-900 text-white"
                    : "bg-gray-100 text-gray-600"
                }`}
              >
                {category}
              </button>
            ))}
          </div>
          <div className="flex-1 space-y-3">
            {filtered.map((dish) => (
              <Link
                key={dish.id}
                to={`/dishes/${dish.id}`}
                className="flex gap-3 rounded-2xl bg-gray-50 p-3"
              >
                <img
                  src={dish.imageUrl}
                  alt={dish.name}
                  className="h-16 w-16 rounded-xl object-cover"
                />
                <div className="flex-1">
                  <div className="text-sm font-semibold text-gray-900">{dish.name}</div>
                  <div className="text-xs text-gray-500">{dish.windowName}</div>
                  <div className="mt-1 flex flex-wrap gap-1">
                    {dish.tags.map((tag) => (
                      <Tag key={tag} label={tag} />
                    ))}
                  </div>
                </div>
                <div className="text-sm font-semibold text-gray-900">${dish.price}</div>
              </Link>
            ))}
            {filtered.length === 0 ? (
              <div className="rounded-xl border border-dashed border-gray-200 p-4 text-center text-xs text-gray-500">
                No dishes match the current filters.
              </div>
            ) : null}
          </div>
        </div>
      </section>
    </div>
  );
}

