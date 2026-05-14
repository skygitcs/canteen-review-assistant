export const announcements = [
  {
    id: 1,
    title: "Late night service updated",
    content: "Extended hours for exams week."
  },
  {
    id: 2,
    title: "New seasonal menu",
    content: "Try the summer specials this week."
  }
];

export const canteens = [
  {
    id: 1,
    name: "Zijing Canteen",
    coverUrl: "https://picsum.photos/seed/canteen1/900/600",
    address: "East Campus Road",
    openHours: "07:00-22:00",
    payMethods: "Card, Mobile Pay",
    onCampus: true,
    latitude: 39.9923,
    longitude: 116.3057,
    avgRating: 4.4,
    dishCount: 58,
    crowdLevel: 2.6,
    tags: ["Spicy", "Value", "Noodles"],
    featuredDishIds: [101, 102, 103],
    windows: [
      { id: 11, name: "Hot Dishes", floorNo: 1 },
      { id: 12, name: "Noodle Bar", floorNo: 1 },
      { id: 13, name: "Grill", floorNo: 2 }
    ]
  },
  {
    id: 2,
    name: "Lotus Canteen",
    coverUrl: "https://picsum.photos/seed/canteen2/900/600",
    address: "North Campus Plaza",
    openHours: "06:30-21:30",
    payMethods: "Card, Mobile Pay",
    onCampus: true,
    latitude: 39.9951,
    longitude: 116.3104,
    avgRating: 4.1,
    dishCount: 42,
    crowdLevel: 3.4,
    tags: ["Light", "Rice", "Healthy"],
    featuredDishIds: [104, 105],
    windows: [
      { id: 21, name: "Rice Bowls", floorNo: 1 },
      { id: 22, name: "Salad", floorNo: 1 }
    ]
  },
  {
    id: 3,
    name: "Library Food Hall",
    coverUrl: "https://picsum.photos/seed/canteen3/900/600",
    address: "Library South Gate",
    openHours: "10:00-20:30",
    payMethods: "Mobile Pay",
    onCampus: false,
    latitude: 39.9894,
    longitude: 116.2991,
    avgRating: 4.6,
    dishCount: 36,
    crowdLevel: 1.8,
    tags: ["Cafe", "Dessert", "Coffee"],
    featuredDishIds: [106, 107],
    windows: [
      { id: 31, name: "Coffee", floorNo: 1 },
      { id: 32, name: "Bakery", floorNo: 1 }
    ]
  }
];

export const dishes = [
  {
    id: 101,
    canteenId: 1,
    canteenName: "Zijing Canteen",
    windowId: 11,
    windowName: "Hot Dishes",
    floorNo: 1,
    name: "Mapo Tofu",
    imageUrl: "https://picsum.photos/seed/dish101/800/600",
    price: 12.0,
    description: "Soft tofu with chili and peppercorn.",
    spiceLevel: 3,
    tags: ["Spicy", "Classic"],
    avgRating: 4.5,
    reviewCount: 26,
    favoriteCount: 45,
    category: "Hot"
  },
  {
    id: 102,
    canteenId: 1,
    canteenName: "Zijing Canteen",
    windowId: 12,
    windowName: "Noodle Bar",
    floorNo: 1,
    name: "Beef Noodles",
    imageUrl: "https://picsum.photos/seed/dish102/800/600",
    price: 15.0,
    description: "Rich broth with tender beef slices.",
    spiceLevel: 2,
    tags: ["Noodles", "Savory"],
    avgRating: 4.3,
    reviewCount: 18,
    favoriteCount: 31,
    category: "Noodles"
  },
  {
    id: 103,
    canteenId: 1,
    canteenName: "Zijing Canteen",
    windowId: 13,
    windowName: "Grill",
    floorNo: 2,
    name: "Grilled Chicken Bowl",
    imageUrl: "https://picsum.photos/seed/dish103/800/600",
    price: 18.0,
    description: "Charred chicken with garlic rice.",
    spiceLevel: 1,
    tags: ["Grill", "Protein"],
    avgRating: 4.2,
    reviewCount: 12,
    favoriteCount: 27,
    category: "Grill"
  },
  {
    id: 104,
    canteenId: 2,
    canteenName: "Lotus Canteen",
    windowId: 21,
    windowName: "Rice Bowls",
    floorNo: 1,
    name: "Teriyaki Pork Rice",
    imageUrl: "https://picsum.photos/seed/dish104/800/600",
    price: 16.0,
    description: "Sweet glaze with roasted pork slices.",
    spiceLevel: 0,
    tags: ["Rice", "Sweet"],
    avgRating: 4.0,
    reviewCount: 9,
    favoriteCount: 19,
    category: "Rice"
  },
  {
    id: 105,
    canteenId: 2,
    canteenName: "Lotus Canteen",
    windowId: 22,
    windowName: "Salad",
    floorNo: 1,
    name: "Chicken Salad",
    imageUrl: "https://picsum.photos/seed/dish105/800/600",
    price: 14.0,
    description: "Light greens with grilled chicken.",
    spiceLevel: 0,
    tags: ["Healthy", "Light"],
    avgRating: 4.2,
    reviewCount: 8,
    favoriteCount: 23,
    category: "Salad"
  },
  {
    id: 106,
    canteenId: 3,
    canteenName: "Library Food Hall",
    windowId: 31,
    windowName: "Coffee",
    floorNo: 1,
    name: "Cold Brew",
    imageUrl: "https://picsum.photos/seed/dish106/800/600",
    price: 10.0,
    description: "Smooth cold brew with citrus notes.",
    spiceLevel: 0,
    tags: ["Coffee", "Iced"],
    avgRating: 4.7,
    reviewCount: 15,
    favoriteCount: 34,
    category: "Drinks"
  },
  {
    id: 107,
    canteenId: 3,
    canteenName: "Library Food Hall",
    windowId: 32,
    windowName: "Bakery",
    floorNo: 1,
    name: "Chocolate Croissant",
    imageUrl: "https://picsum.photos/seed/dish107/800/600",
    price: 9.0,
    description: "Butter layers with dark chocolate.",
    spiceLevel: 0,
    tags: ["Dessert", "Baked"],
    avgRating: 4.6,
    reviewCount: 11,
    favoriteCount: 29,
    category: "Bakery"
  }
];

export const reviews = [
  {
    id: 201,
    dishId: 101,
    userId: 1,
    userName: "Lin",
    rating: 5,
    content: "Great spice balance and aroma.",
    imageUrl: "https://picsum.photos/seed/review201/600/400",
    up: 6,
    down: 0,
    createdAt: "2025-05-01"
  },
  {
    id: 202,
    dishId: 101,
    userId: 2,
    userName: "Mei",
    rating: 4,
    content: "A bit salty but tasty.",
    imageUrl: "https://picsum.photos/seed/review202/600/400",
    up: 3,
    down: 1,
    createdAt: "2025-05-02"
  },
  {
    id: 203,
    dishId: 104,
    userId: 3,
    userName: "Jun",
    rating: 4,
    content: "Comforting and filling.",
    imageUrl: "https://picsum.photos/seed/review203/600/400",
    up: 2,
    down: 0,
    createdAt: "2025-04-28"
  }
];

export const userProfile = {
  id: 7,
  username: "student07",
  nickname: "Kai",
  avatarUrl: "https://picsum.photos/seed/avatar/200/200",
  role: "USER",
  tastePreference: "Mild",
  campusCardAuthorized: true
};

export const recentActivities = [
  {
    id: 401,
    type: "review",
    title: "Reviewed Mapo Tofu",
    time: "2025-05-03"
  },
  {
    id: 402,
    type: "visit",
    title: "Visited Zijing Canteen",
    time: "2025-05-02"
  }
];

export const favoriteDishIds = [101, 106];

export const adminSubmissions = [
  {
    id: 501,
    name: "Spicy Fish Soup",
    canteenName: "Zijing Canteen",
    submitter: "student11",
    status: "PENDING"
  },
  {
    id: 502,
    name: "Mango Pudding",
    canteenName: "Library Food Hall",
    submitter: "student03",
    status: "PENDING"
  }
];

export const supportMessages = [
  {
    id: 601,
    user: "student07",
    message: "How to update my avatar?"
  }
];

