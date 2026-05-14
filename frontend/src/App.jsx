import { BrowserRouter, Routes, Route, Outlet, useLocation } from "react-router-dom";
import BottomNav from "./components/BottomNav.jsx";
import Home from "./pages/Home.jsx";
import FoodMap from "./pages/FoodMap.jsx";
import Profile from "./pages/Profile.jsx";
import CanteenDetail from "./pages/CanteenDetail.jsx";
import DishDetail from "./pages/DishDetail.jsx";
import AdminDashboard from "./pages/AdminDashboard.jsx";

function AppShell() {
  const location = useLocation();
  const showNav = !location.pathname.startsWith("/admin");

  return (
    <div className="min-h-screen bg-gray-50 pb-16">
      <div className="mx-auto max-w-md px-4 pb-6 pt-4">
        <Outlet />
      </div>
      {showNav ? <BottomNav /> : null}
    </div>
  );
}

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route element={<AppShell />}>
          <Route path="/" element={<Home />} />
          <Route path="/map" element={<FoodMap />} />
          <Route path="/profile" element={<Profile />} />
          <Route path="/canteens/:id" element={<CanteenDetail />} />
          <Route path="/dishes/:id" element={<DishDetail />} />
        </Route>
        <Route path="/admin" element={<AdminDashboard />} />
        <Route path="*" element={<Home />} />
      </Routes>
    </BrowserRouter>
  );
}

