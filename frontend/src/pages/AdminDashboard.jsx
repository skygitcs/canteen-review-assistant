import { adminSubmissions, supportMessages } from "../data/mockData.js";

export default function AdminDashboard() {
  return (
    <div className="min-h-screen bg-gray-50 px-4 pb-10 pt-4">
      <div className="mx-auto max-w-md space-y-6">
        <section className="rounded-2xl bg-white p-4 shadow-sm">
          <div className="text-sm font-semibold text-gray-700">Batch Upload</div>
          <button className="mt-3 w-full rounded-xl border border-dashed border-gray-300 py-3 text-sm text-gray-500">
            Upload dish data
          </button>
        </section>

        <section className="rounded-2xl bg-white p-4 shadow-sm">
          <div className="text-sm font-semibold text-gray-700">Pending Submissions</div>
          <div className="mt-3 space-y-3">
            {adminSubmissions.map((item) => (
              <div key={item.id} className="rounded-xl bg-gray-50 p-3">
                <div className="text-sm font-semibold text-gray-900">{item.name}</div>
                <div className="text-xs text-gray-500">{item.canteenName}</div>
                <div className="mt-2 flex items-center gap-2">
                  <button className="flex-1 rounded-lg bg-gray-900 py-1 text-xs font-semibold text-white">
                    Approve
                  </button>
                  <button className="flex-1 rounded-lg border border-gray-200 py-1 text-xs font-semibold text-gray-600">
                    Reject
                  </button>
                </div>
              </div>
            ))}
          </div>
        </section>

        <section className="rounded-2xl bg-white p-4 shadow-sm">
          <div className="text-sm font-semibold text-gray-700">Support Tickets</div>
          <div className="mt-3 space-y-3">
            {supportMessages.map((message) => (
              <div key={message.id} className="rounded-xl bg-gray-50 p-3">
                <div className="text-xs text-gray-500">From {message.user}</div>
                <div className="text-sm text-gray-800">{message.message}</div>
                <button className="mt-2 rounded-lg border border-gray-200 px-3 py-1 text-xs text-gray-600">
                  Reply
                </button>
              </div>
            ))}
          </div>
        </section>
      </div>
    </div>
  );
}

