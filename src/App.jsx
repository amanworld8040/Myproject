import React, { useState, useEffect } from "react";
import { BrowserRouter as Router, Routes, Route, Link, Navigate } from "react-router-dom";
import { Users, BookOpen, UserCheck } from "lucide-react";

import UserManagement from "./components/UserManagement";
import TrainingManagement from "./components/TrainingManagement";
import AllocationManagement from "./components/AllocationManagement";
import Home from "./components/Home";
import Login from "./components/Login";
import UserHome from "./components/UserHome";
import UserEnrollments from "./components/UserEnrollments";
import UserPrograms from "./components/UserPrograms";

import "./App.css";

function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [role, setRole] = useState(null); // "admin" or "user"

  useEffect(() => {
    console.log("Auth state changed:", { isAuthenticated, role });
  }, [isAuthenticated, role]);

  const handleLogout = () => {
    setIsAuthenticated(false);
    setRole(null);
  };

  return (
    <Router>
      <div className="app">
        {!isAuthenticated ? (
          // Not logged in → show login
          <Routes>
            <Route
              path="/*"
              element={<Login setIsAuthenticated={setIsAuthenticated} setRole={setRole} />}
            />
          </Routes>
        ) : role === "admin" ? (
          // Admin side
          <>
            <nav className="navbar">
              <div className="nav-container">
                <Link to="/" className="nav-logo">Online Training System</Link>
                <div className="nav-menu">
                  <Link to="/" className="nav-item">Home</Link>
                  <Link to="/users" className="nav-item"><Users size={16} /> Users</Link>
                  <Link to="/trainings" className="nav-item"><BookOpen size={16} /> Training Programs</Link>
                  <Link to="/allocations" className="nav-item"><UserCheck size={16} /> Allocations</Link>
                </div>
              </div>
            </nav>
            <main className="main-content">
              <Routes>
                <Route path="/" element={<Home onLogout={handleLogout} />} />
                <Route path="/users" element={<UserManagement />} />
                <Route path="/trainings" element={<TrainingManagement />} />
                <Route path="/allocations" element={<AllocationManagement />} />
                <Route path="/login" element={<Navigate to="/" />} />
              </Routes>
            </main>
          </>
        ) : role === "user" ? (
          // User side
          <>
            <nav className="navbar">
              <div className="nav-container">
                <Link to="/" className="nav-logo">Online Training System</Link>
                <div className="nav-menu">
                  <Link to="/" className="nav-item">Home</Link>
                  <Link to="/enrollments" className="nav-item">My Enrollments</Link>
                  <Link to="/programs" className="nav-item">Available Programs</Link>
                </div>
              </div>
            </nav>
            <main className="main-content">
              <Routes>
                <Route path="/" element={<UserHome onLogout={handleLogout} />} />
                <Route path="/enrollments" element={<UserEnrollments />} />
                <Route path="/programs" element={<UserPrograms />} />
                <Route path="/login" element={<Navigate to="/" />} />
              </Routes>
            </main>
          </>
        ) : null}
      </div>
    </Router>
  );
}

export default App;
