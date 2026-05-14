export default function Tag({ label }) {
  return (
    <span className="inline-flex items-center rounded-full border border-purple-300 bg-purple-100 px-2 py-0.5 text-xs font-medium text-purple-700">
      {label}
    </span>
  );
}

