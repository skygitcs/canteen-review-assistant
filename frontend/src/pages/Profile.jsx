import Tag from "../components/Tag.jsx";
import { dishes, favoriteDishIds, recentActivities, userProfile } from "../data/mockData.js";

export default function Profile() {
  const favorites = dishes.filter((dish) => favoriteDishIds.includes(dish.id));

  return (
    <div className="space-y-6">
      <section className="flex items-center gap-4 rounded-2xl bg-white p-4 shadow-sm">
        <img
          src={userProfile.avatarUrl}
          alt={userProfile.nickname}
          className="h-16 w-16 rounded-full object-cover"
        />
        <div>
          <div className="text-lg font-semibold text-gray-900">{userProfile.nickname}</div>
          <div className="text-xs text-gray-500">@{userProfile.username}</div>
          <div className="mt-2 flex flex-wrap gap-2">
            <Tag label={userProfile.tastePreference} />
            {userProfile.campusCardAuthorized ? <Tag label="Card Linked" /> : null}
          </div>
        </div>
      </section>

      <section className="rounded-2xl bg-white p-4 shadow-sm">
        <div className="text-sm font-semibold text-gray-700">Recent Activities</div>
        <div className="mt-3 space-y-2">
          {recentActivities.map((item) => (
            <div key={item.id} className="flex items-center justify-between text-sm">
              <div className="text-gray-800">{item.title}</div>
              <div className="text-xs text-gray-400">{item.time}</div>
            </div>
          ))}
        </div>
      </section>

      <section className="rounded-2xl bg-white p-4 shadow-sm">
        <div className="text-sm font-semibold text-gray-700">My Favorites</div>
        <div className="mt-3 grid gap-3">
          {favorites.map((dish) => (
            <div key={dish.id} className="flex items-center gap-3 rounded-xl bg-gray-50 p-3">
              <img src={dish.imageUrl} alt={dish.name} className="h-14 w-14 rounded-lg" />
              <div className="flex-1">
                <div className="text-sm font-semibold text-gray-900">{dish.name}</div>
                <div className="text-xs text-gray-500">{dish.canteenName}</div>
              </div>
              <div className="text-sm font-semibold text-gray-900">${dish.price}</div>
            </div>
          ))}
        </div>
      </section>

      <section className="rounded-2xl bg-white p-4 shadow-sm">
        <div className="text-sm font-semibold text-gray-700">Support</div>
        <button className="mt-3 w-full rounded-xl bg-gray-900 py-2 text-sm font-semibold text-white">
          Leave a message
        </button>
      </section>
    </div>
  );
}

