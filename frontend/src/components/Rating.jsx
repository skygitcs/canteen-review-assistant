export default function Rating({ value, count }) {
  return (
    <div className="text-xs text-gray-600">
      Rating {value.toFixed(1)} ({count} reviews)
    </div>
  );
}

