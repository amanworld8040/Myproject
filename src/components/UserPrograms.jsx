import React, { useEffect, useState, useCallback } from "react";
import { userApi, trainingApi } from "../services/api";

function UserPrograms({ onEnroll }) {
  const userId = localStorage.getItem("userId");

  const [programs, setPrograms] = useState([]);
  const [loadingPrograms, setLoadingPrograms] = useState(true);
  const [error, setError] = useState("");
  const [enrollError, setEnrollError] = useState("");
  const [success, setSuccess] = useState("");
  const [enrollingId, setEnrollingId] = useState(null);

  /** 🔹 Fetch available programs */
  // Normalize raw program objects coming from different endpoints/keys
  const normalizePrograms = (list) => {
    if (!Array.isArray(list)) return [];
    return list.map((p, idx) => ({
      id: p.id ?? p.programId ?? p.trainingId ?? idx,
      name: p.name ?? p.programName ?? p.title ?? "Unnamed Program",
      description: p.description ?? p.details ?? "No description provided",
      price: p.price ?? p.cost ?? p.amount ?? 0,
      status: (p.status || p.state || "ACTIVE").toUpperCase(),
    }));
  };

  const fetchPrograms = useCallback(async () => {
    if (!userId) return;
    try {
      setLoadingPrograms(true);
      setError("");
      let primaryData = [];
      try {
        const res = await userApi.getAvailablePrograms(userId);
        primaryData = res?.data;
      } catch (inner) {
        console.warn("Primary available-programs endpoint failed", inner);
      }

      let normalized = normalizePrograms(primaryData);

      // Fallback: if primary empty, try general trainings list
      if (normalized.length === 0) {
        try {
          const res2 = await trainingApi.getAllTrainings();
          normalized = normalizePrograms(res2?.data);
        } catch (fallbackErr) {
          console.warn("Fallback training list fetch failed", fallbackErr);
        }
      }

      setPrograms(normalized);
      if (normalized.length === 0) {
        setError("No programs available.");
      }
    } catch (err) {
      console.error("Programs fetch error", err);
      setError(err.response?.data?.message || err.message || "Error fetching programs");
      setPrograms([]);
    } finally {
      setLoadingPrograms(false);
    }
  }, [userId]);

  useEffect(() => {
    if (!userId) {
      setError("User not logged in. Please log in to view programs.");
      setLoadingPrograms(false);
      return;
    }
    fetchPrograms();
  }, [userId, fetchPrograms]);

  /** 🔹 Handle enrollment */
  const handleEnroll = async (programId) => {
    if (!userId) return;
    setSuccess("");
    setEnrollError("");
    setEnrollingId(programId);

    try {
      const res = await userApi.enrollInProgram(userId, programId);
      const payload = res?.data || {};
      const successFlag = payload.success !== false; // defaults true if undefined
      const msg = payload.message || (successFlag ? "Enrolled successfully" : "Failed to enroll");
      if (!successFlag) {
        setEnrollError(msg);
      } else {
        setSuccess(msg);
        // Mark program as enrolled locally so it disappears from ACTIVE filter
        setPrograms(prev => prev.map(p => p.id === programId ? { ...p, status: 'ENROLLED' } : p));
        if (onEnroll) onEnroll();
        // Background refresh (no await to keep UI snappy)
        fetchPrograms();
      }
    } catch (err) {
      const already = err?.response?.data?.message === 'Already enrolled';
      console.error("Enroll action error", err);
      if (already) {
        setEnrollError('Already enrolled in this program');
        // Update UI to reflect state
        setPrograms(prev => prev.map(p => p.id === programId ? { ...p, status: 'ENROLLED' } : p));
      } else {
        setEnrollError(err.response?.data?.message || "Failed to enroll");
      }
    } finally {
      setEnrollingId(null);

      // Auto-clear success/error messages after 3s
      setTimeout(() => {
        setSuccess("");
        setEnrollError("");
      }, 3000);
    }
  };

  const fatalProgramsError = error && programs.length === 0;

  return (
    <div className="space-y-8">
      <div className="card">
        <div className="flex justify-between items-center mb-6">
          <h1 className="page-title">Available Training Programs</h1>
        </div>

        {/* Fatal fetch error */}
        {fatalProgramsError && (
          <div className="mb-4 p-3 bg-red-50 border border-red-200 rounded">
            <p className="text-red-600 font-medium mb-2">{error}</p>
            <button onClick={fetchPrograms} className="btn btn-secondary btn-sm">
              Retry
            </button>
          </div>
        )}

        {/* Programs list */}
        {loadingPrograms ? (
          <p className="text-gray-600">Loading programs...</p>
        ) : programs.length === 0 ? (
          <p className="text-gray-500">No programs available.</p>
        ) : (
          <div className="overflow-x-auto">
            <table className="table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Name</th>
                  <th>Description</th>
                  <th>Price</th>
                  <th>Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {programs.filter(p => p.status === 'ACTIVE').length === 0 ? (
                  <tr>
                    <td colSpan="6" className="text-center py-8 text-gray-500">No active programs available</td>
                  </tr>
                ) : (
                  programs.filter(p => p.status === 'ACTIVE').map(program => (
                    <tr key={program.id}>
                      <td>{program.id}</td>
                      <td className="font-semibold">{program.name}</td>
                      <td>{program.description}</td>
                      <td>₹{Number(program.price).toFixed(2)}</td>
                      <td>
                        <span className="px-2 py-1 rounded-full text-xs bg-green-100 text-green-700">ACTIVE</span>
                      </td>
                      <td>
                        <button
                          className="btn btn-primary"
                          onClick={() => handleEnroll(program.id)}
                          disabled={enrollingId === program.id}
                        >
                            {enrollingId === program.id ? 'Enrolling...' : 'Enroll'}
                        </button>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        )}

        {/* Success & Error Messages */}
        {success && <p className="text-green-600 mt-2">{success}</p>}
        {enrollError && <p className="text-red-500 mt-2">{enrollError}</p>}
      </div>
    </div>
  );
}

export default UserPrograms;
