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

  const tags = useMemo(() => {
    const allTags = canteenDishes.flatMap((dish) => dish.tags);
    return ["全部", ...new Set(allTags)];
  }, [canteenDishes]);

  const floors = useMemo(() => {
    const all = canteen.windows.map((window) => String(window.floorNo));
    return [...new Set(all)].sort();
  }, [canteen.windows]);

  const windows = useMemo(() => {
    const filtered = floorFilter === "all"
      ? canteen.windows
      : canteen.windows.filter((w) => String(w.floorNo) === floorFilter);
    return [
      { id: "all", name: "全部窗口" },
      ...filtered.map((window) => ({ id: String(window.id), name: window.name }))
    ];
  }, [canteen.windows, floorFilter]);

  const filtered = canteenDishes.filter((dish) => {
    const matchesQuery = dish.name.includes(query.trim());
    const matchesTag = tagFilter === "全部" || dish.tags.includes(tagFilter);
    const matchesFloor = floorFilter === "all" || String(dish.floorNo) === floorFilter;
    const matchesWindow = windowFilter === "all" || String(dish.windowId) === windowFilter;
    return matchesQuery && matchesTag && matchesFloor && matchesWindow;
  });

  return (
    <div className="space-y-5">
      {/* 食堂信息 */}
      <section className="overflow-hidden rounded-2xl bg-white shadow-sm">
        <div className="h-40 w-full bg-gradient-to-br from-indigo-100 to-purple-100 flex items-center justify-center">
          <span className="text-4xl">🏛️</span>
        </div>
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

      {/* 上方搜索+筛选合并区 */}
      <section className="space-y-3">
        <SearchBar value={query} onChange={setQuery} placeholder="搜索菜品名称..." />
        <div className="flex flex-wrap gap-2">
          {tags.map((tag) => (
            <button
              key={tag}
              onClick={() => setTagFilter(tag)}
              className={`rounded-full border px-3 py-1 text-xs ${
                tagFilter === tag
                  ? "border-purple-400 bg-purple-100 text-purple-700"
                  : "border-gray-200 bg-white text-gray-600"
              }`}
            >
              {tag}
            </button>
          ))}
        </div>
      </section>

      {/* 左侧楼层 + 右侧菜品 */}
      <section className="rounded-2xl bg-white p-4 shadow-sm">
        <div className="flex items-center justify-between mb-4">
          <div className="text-sm font-semibold text-gray-700">🍜 菜单</div>
          <button className="rounded-full border border-gray-200 px-3 py-1 text-xs text-gray-600">
            添加菜品
          </button>
        </div>
        <div className="flex gap-3">
          {/* 左侧楼层选择 */}
          <div className="w-24 shrink-0 space-y-2">
            <div className="text-xs text-gray-400 mb-1">楼层</div>
            <button
              onClick={() => { setFloorFilter("all"); setWindowFilter("all"); }}
              className={`w-full rounded-xl px-2 py-2 text-xs font-medium ${
                floorFilter === "all"
                  ? "bg-gray-900 text-white"
                  : "bg-gray-100 text-gray-600"
              }`}
            >
              全部
            </button>
            {floors.map((floor) => (
              <button
                key={floor}
                onClick={() => {
                  setFloorFilter(floor);
                  setWindowFilter("all");
                }}
                className={`w-full rounded-xl px-2 py-2 text-xs font-medium ${
                  floorFilter === floor
                    ? "bg-gray-900 text-white"
                    : "bg-gray-100 text-gray-600"
                }`}
              >
                {floor} 楼
              </button>
            ))}
          </div>

          {/* 右侧菜品列表 */}
          <div className="flex-1 min-w-0 space-y-3">
            {/* 窗口筛选 dropdown */}
            <select
              value={windowFilter}
              onChange={(event) => setWindowFilter(event.target.value)}
              className="w-full rounded-xl border border-gray-200 bg-white px-3 py-2 text-xs"
            >
              {windows.map((window) => (
                <option key={window.id} value={window.id}>
                  {window.name}
                </option>
              ))}
            </select>

            {filtered.map((dish) => (
              <Link
                key={dish.id}
                to={`/dishes/${dish.id}`}
                className="flex gap-3 rounded-2xl bg-gray-50 p-3"
              >
                <div className="h-16 w-16 rounded-xl bg-gradient-to-br from-orange-100 to-yellow-100 flex items-center justify-center shrink-0">
                  <span className="text-xl">🍽️</span>
                </div>
                <div className="flex-1 min-w-0">
                  <div className="text-sm font-semibold text-gray-900">{dish.name}</div>
                  <div className="text-xs text-gray-500">{dish.windowName}</div>
                  <div className="mt-1 flex flex-wrap gap-1">
                    {dish.tags.map((tag) => (
                      <Tag key={tag} label={tag} />
                    ))}
                  </div>
                </div>
                <div className="text-sm font-semibold text-gray-900 whitespace-nowrap">¥{dish.price}</div>
              </Link>
            ))}
            {filtered.length === 0 ? (
              <div className="rounded-xl border border-dashed border-gray-200 p-4 text-center text-xs text-gray-500">
                没有符合条件的菜品。
              </div>
            ) : null}
          </div>
        </div>
      </section>
    </div>
  );
}

