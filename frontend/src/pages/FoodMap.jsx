import { useMemo, useState } from "react";
import { Link } from "react-router-dom";
import SearchBar from "../components/SearchBar.jsx";
import Tag from "../components/Tag.jsx";
import Rating from "../components/Rating.jsx";
import { canteens } from "../data/mockData.js";

export default function FoodMap() {
  const [query, setQuery] = useState("");
  const [tagFilter, setTagFilter] = useState("all");

  const tags = useMemo(() => {
    const allTags = canteens.flatMap((canteen) => canteen.tags);
    return ["all", ...new Set(allTags)];
  }, []);

  const filtered = useMemo(() => {
    return canteens.filter((canteen) => {
      const matchesQuery = canteen.name
        .toLowerCase()
        .includes(query.trim().toLowerCase());
      const matchesTag =
        tagFilter === "all" || canteen.tags.includes(tagFilter);
      return matchesQuery && matchesTag;
    });
  }, [query, tagFilter]);

  return (
    <div className="space-y-6">
      <section className="space-y-3">
        <div className="text-sm font-semibold text-gray-700">Canteen List</div>
        <SearchBar
          value={query}
          onChange={setQuery}
          placeholder="Search canteens or tags"
        />
        <div className="flex flex-wrap gap-2">
          {tags.map((tag) => (
            <button
              key={tag}
              onClick={() => setTagFilter(tag)}
              className={`rounded-full border px-3 py-1 text-xs font-medium transition ${
                tagFilter === tag
                  ? "border-purple-400 bg-purple-100 text-purple-700"
                  : "border-gray-200 bg-white text-gray-600"
              }`}
            >
              {tag}
            </button>
          ))}
        </div>
        <div className="grid gap-3">
          {filtered.map((canteen) => (
            <Link
              key={canteen.id}
              to={`/canteens/${canteen.id}`}
              className="rounded-2xl bg-white p-3 shadow-sm"
            >
              <div className="flex items-center gap-3">
                <img
                  src={canteen.coverUrl}
                  alt={canteen.name}
                  className="h-16 w-16 rounded-xl object-cover"
                />
                <div className="flex-1">
                  <div className="text-sm font-semibold text-gray-900">{canteen.name}</div>
                  <div className="text-xs text-gray-500">{canteen.address}</div>
                  <div className="mt-1 flex flex-wrap gap-1">
                    {canteen.tags.map((tag) => (
                      <Tag key={tag} label={tag} />
                    ))}
                  </div>
                </div>
                <div className="text-right">
                  <div className="text-xs text-gray-500">Crowd</div>
                  <div className="text-sm font-semibold text-gray-900">
                    {canteen.crowdLevel.toFixed(1)}
                  </div>
                </div>
              </div>
              <div className="mt-2">
                <Rating value={canteen.avgRating} count={canteen.dishCount} />
              </div>
            </Link>
          ))}
        </div>
      </section>

      <section className="rounded-2xl bg-white p-4 shadow-sm">
        <div className="text-sm font-semibold text-gray-700">Campus Heat Map</div>
        <div className="mt-3 grid gap-3">
          {filtered.map((canteen) => (
            <div key={canteen.id} className="flex items-end justify-between">
              <div>
                <div className="text-xs font-medium text-gray-700">{canteen.name}</div>
                <div className="text-xs text-gray-400">
                  ({canteen.latitude.toFixed(3)}, {canteen.longitude.toFixed(3)})
                </div>
              </div>
              <div className="flex items-end gap-2">
                <div className="h-16 w-20 rounded-lg bg-gray-100 px-2 pb-2">
                  <div
                    className="w-full rounded-md bg-purple-500"
                    style={{ height: `${12 + canteen.crowdLevel * 12}px` }}
                  />
                </div>
                <div className="text-xs text-gray-500">Heat {canteen.crowdLevel}</div>
              </div>
            </div>
          ))}
        </div>
      </section>
    </div>
  );
}

