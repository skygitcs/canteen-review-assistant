import { NavLink } from "react-router-dom";

const navItems = [
  { to: "/", label: "Home" },
  { to: "/map", label: "Food Map" },
  { to: "/profile", label: "Profile" }
];

export default function BottomNav() {
  return (
    <nav className="fixed bottom-0 left-0 right-0 z-30 border-t border-gray-200 bg-white">
      <div className="mx-auto flex max-w-md items-center justify-around px-4 py-2 text-sm">
        {navItems.map((item) => (
          <NavLink
            key={item.to}
            to={item.to}
            className={({ isActive }) =>
              `rounded-full px-3 py-1 transition ${
                isActive
                  ? "bg-gray-900 text-white"
                  : "text-gray-600 hover:text-gray-900"
              }`
            }
          >
            {item.label}
          </NavLink>
        ))}
      </div>
    </nav>
  );
}

