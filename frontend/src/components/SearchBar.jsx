export default function SearchBar({ value, onChange, placeholder }) {
  return (
    <div className="flex items-center gap-2 rounded-xl border border-gray-200 bg-white px-3 py-2">
      <span className="text-xs text-gray-500">Search</span>
      <input
        className="w-full text-sm outline-none"
        placeholder={placeholder}
        value={value}
        onChange={(event) => onChange(event.target.value)}
      />
    </div>
  );
}

