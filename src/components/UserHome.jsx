import React, { useEffect, useState } from "react";
import { BookOpen, CheckCircle, Home as HomeIcon } from "lucide-react";
import { Link, useNavigate } from "react-router-dom";
import { trainingApi } from "../services/api";

function UserHome({ onLogout }) {
  const navigate = useNavigate();
  const [trainings, setTrainings] = useState([]);
  const [enrolled, setEnrolled] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchPrograms();
  }, []);

  const fetchPrograms = async () => {
    try {
      setLoading(true);
      const res = await trainingApi.getAllTrainings();
      const active = res.data.filter((t) => t.active === true);
      setTrainings(active);
    } catch (err) {
      setError("Failed to load programs");
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleEnroll = (program) => {
    if (!enrolled.find((e) => e.id === program.id)) {
      setEnrolled([...enrolled, program]);
    }
  };

  return (
    <>
  
      <div className="main-content">
        {/* Header */}
        <div className="flex justify-between items-center mb-8">
          <h1 className="page-title">Welcome to Online Training System</h1>
          <button
            onClick={onLogout}
            className="btn btn-danger flex items-center gap-2"
          >
            Logout
          </button>
        </div>

        {/* Subtitle */}
        <p className="text-gray-600 mb-6">
          Explore available programs and track your enrollments.
        </p>

        <div className="space-y-6">
          {/* Available Programs Card (whole card clickable) */}
          <div
            className="card focus:outline-none cursor-pointer"
            id="programs"
            role="button"
            tabIndex={0}
            onClick={() => navigate('/programs')}
            onKeyDown={(e) => {
              if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                navigate('/programs');
              }
            }}
            style={{ cursor: 'pointer' }}
            aria-label="Go to Available Programs"
          >
            <div className="flex justify-between items-center">
              <div className="flex items-center gap-3">
                <BookOpen size={28} className="text-purple-600" />
                <div>
                  <h2 className="card-title">Available Programs</h2>
                  <p className="text-sm text-gray-600">Browse and enroll in training programs</p>
                </div>
              </div>
              <span className="text-purple-600 text-lg select-none" aria-hidden="true">→</span>
            </div>
            <div className="mt-4 space-y-3">
              {loading ? (
                <p>Loading...</p>
              ) : error ? (
                <p className="login-error">{error}</p>
              ) : trainings.length === 0 ? (
                <p>No active programs available</p>
              ) : (
                trainings.map((t) => (
                  <div
                    key={t.id}
                    className="flex justify-between items-center bg-gray-50 p-3 rounded"
                  >
                    <div>
                      <h3 className="font-medium">{t.title}</h3>
                      <p className="text-sm text-gray-500">{t.description}</p>
                    </div>
                    <button
                      className="btn btn-primary"
                      onClick={(e) => { e.stopPropagation(); handleEnroll(t); }}
                      disabled={enrolled.find((e) => e.id === t.id)}
                    >
                      {enrolled.find((e) => e.id === t.id) ? 'Enrolled' : 'Enroll'}
                    </button>
                  </div>
                ))
              )}
            </div>
          </div>

          {/* My Enrollments Card (whole card clickable) */}
          <div
            className="card focus:outline-none cursor-pointer"
            id="enrollments"
            role="button"
            tabIndex={0}
            onClick={() => navigate('/enrollments')}
            onKeyDown={(e) => {
              if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                navigate('/enrollments');
              }
            }}
            style={{ cursor: 'pointer' }}
            aria-label="Go to My Enrollments"
          >
            <div className="flex justify-between items-center">
              <div className="flex items-center gap-3">
                <CheckCircle size={28} className="text-green-600" />
                <div>
                  <h2 className="card-title">My Enrollments</h2>
                  <p className="text-sm text-gray-600">Programs you are currently enrolled in</p>
                </div>
              </div>
              <span className="text-green-600 text-lg select-none" aria-hidden="true">→</span>
            </div>
            <div className="mt-4 space-y-3">
              {enrolled.length === 0 ? (
                <p>You haven’t enrolled in any programs yet</p>
              ) : (
                enrolled.map((t) => (
                  <div
                    key={t.id}
                    className="flex justify-between items-center bg-gray-50 p-3 rounded"
                  >
                    <div>
                      <h3 className="font-medium">{t.title}</h3>
                      <p className="text-sm text-gray-500">{t.description}</p>
                    </div>
                    <span className="text-green-600 font-medium">Enrolled</span>
                  </div>
                ))
              )}
            </div>
          </div>
        </div>
      </div>
    </>
  );
}

export default UserHome;
