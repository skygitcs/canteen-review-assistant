const colorMap = {
  // 辣味系 - 红色
  "辣味": "border-red-300 bg-red-50 text-red-600",
  "麻辣": "border-red-400 bg-red-100 text-red-700",
  "川菜": "border-orange-300 bg-orange-50 text-orange-700",

  // 清淡/健康系 - 绿色
  "清淡": "border-emerald-300 bg-emerald-50 text-emerald-600",
  "健康": "border-green-300 bg-green-50 text-green-600",
  "轻食": "border-teal-300 bg-teal-50 text-teal-600",

  // 甜品类 - 粉色
  "甜点": "border-pink-300 bg-pink-50 text-pink-600",
  "甜味": "border-rose-300 bg-rose-50 text-rose-600",
  "烘焙": "border-amber-300 bg-amber-50 text-amber-700",

  // 面食系 - 橙色
  "面食": "border-orange-300 bg-orange-50 text-orange-600",
  "饺子": "border-orange-300 bg-orange-50 text-orange-600",

  // 米饭系 - 黄色/琥珀
  "米饭": "border-amber-300 bg-amber-50 text-amber-700",
  "套餐": "border-yellow-300 bg-yellow-50 text-yellow-700",

  // 咖啡/饮品 - 棕色
  "咖啡": "border-stone-400 bg-stone-100 text-stone-700",
  "饮品": "border-cyan-300 bg-cyan-50 text-cyan-600",
  "冰饮": "border-sky-300 bg-sky-50 text-sky-600",

  // 经典/特色
  "经典": "border-blue-300 bg-blue-50 text-blue-600",
  "特色": "border-violet-300 bg-violet-50 text-violet-600",

  // 汤品类
  "汤品": "border-indigo-300 bg-indigo-50 text-indigo-600",
  "粥品": "border-teal-300 bg-teal-50 text-teal-600",

  // 肉类/蛋白质 - 玫红
  "肉类": "border-rose-300 bg-rose-50 text-rose-600",

  // 北方菜系
  "北方菜": "border-cyan-300 bg-cyan-50 text-cyan-600",

  // 小吃/夜宵
  "小吃": "border-purple-300 bg-purple-50 text-purple-600",
  "夜宵": "border-indigo-300 bg-indigo-50 text-indigo-600",
  "油炸": "border-orange-300 bg-orange-50 text-orange-600",

  // 经济实惠
  "经济": "border-emerald-300 bg-emerald-50 text-emerald-600",
  "实惠": "border-lime-300 bg-lime-50 text-lime-600",
  "性价比高": "border-lime-300 bg-lime-50 text-lime-600",

  // 家常/快餐
  "家常": "border-slate-300 bg-slate-50 text-slate-600",
  "快餐": "border-zinc-300 bg-zinc-50 text-zinc-600",

  // 自选
  "自选": "border-violet-300 bg-violet-50 text-violet-600",
};

export default function Tag({ label }) {
  const styles = colorMap[label] || "border-gray-300 bg-gray-50 text-gray-600";
  return (
    <span className={`inline-flex items-center rounded-full border px-2 py-0.5 text-xs font-medium ${styles}`}>
      {label}
    </span>
  );
}

