import React, { useEffect, useState } from "react";
import axios from "axios";

function UserEnrollments({ refreshKey }) {
  const userId = localStorage.getItem("userId") || "2"; // fallback to 2
  const [rows, setRows] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    if (!userId) {
      setError("User not logged in.");
      setLoading(false);
      return;
    }

    const fetchEnrollments = async () => {
      try {
        setLoading(true);
        setError("");
        const { data } = await axios.get(
          `http://localhost:8090/api/user/my-trainings/${userId}`
        );

        // ✅ Your API returns { success, trainings: [...] }
        const arr = Array.isArray(data?.trainings) ? data.trainings : [];
        setRows(arr);

  // (debug) console.log("Fetched enrollments:", arr);
      } catch (err) {
        console.error("Enrollments fetch error", err);
        setError(
          err.response?.data?.message ||
            err.message ||
            "Error fetching enrollments"
        );
        setRows([]);
      } finally {
        setLoading(false);
      }
    };

    fetchEnrollments();
  }, [userId, refreshKey]);

  return (
    <div className="space-y-8">
      <div className="card">
        <div className="flex justify-between items-center mb-6">
          <h1 className="page-title">My Enrollments</h1>
        </div>

        {loading && (
          <p className="text-gray-600">Loading enrollments...</p>
        )}

        {!loading && error && (
          <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded">
            <p className="text-red-600 font-medium mb-2">{error}</p>
            <button
              onClick={() => setRows([]) || setLoading(true) || setError("") || (async () => {
                try {
                  const { data } = await axios.get(`http://localhost:8090/api/user/my-trainings/${userId}`);
                  const arr = Array.isArray(data?.trainings) ? data.trainings : [];
                  setRows(arr);
                } catch (e) {
                  setError(e.response?.data?.message || e.message || 'Retry failed');
                } finally {
                  setLoading(false);
                }
              })()}
              className="btn btn-secondary btn-sm"
            >
              Retry
            </button>
          </div>
        )}

        {!loading && !error && (
          rows.length === 0 ? (
            <p className="text-gray-500">No enrollments found for user {userId}.</p>
          ) : (
            <div className="overflow-x-auto">
              <table className="table">
                <thead>
                  <tr>
                    <th>Program ID</th>
                    <th>Program Name</th>
                    <th>Allocation Date</th>
                  </tr>
                </thead>
                <tbody>
                  {rows.map((r, i) => (
                    <tr key={i}>
                      <td>{r.programId ?? '-'}</td>
                      <td className="font-semibold">{r.programName ?? 'Unnamed Program'}</td>
                      <td>{r.allocationDate ?? '-'}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )
        )}
      </div>
    </div>
  );
}

export default UserEnrollments;
