import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { Users, BookOpen, UserCheck, ArrowRight, LogOut } from 'lucide-react';
import { userApi, trainingApi, allocationApi } from '../services/api';

function Home({ onLogout }) {
  const [stats, setStats] = useState({ users: 0, trainings: 0, allocations: 0 });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    try {
      setLoading(true);
      const [usersRes, trainingsRes, allocationsRes] = await Promise.all([
        userApi.getAllUsers(),
        trainingApi.getAllTrainings(),
        allocationApi.getAllAllocations()
      ]);
      setStats({
        users: Array.isArray(usersRes.data) ? usersRes.data.length : 0,
        trainings: Array.isArray(trainingsRes.data) ? trainingsRes.data.length : 0,
        allocations: Array.isArray(allocationsRes.data) ? allocationsRes.data.length : 0
      });
    } catch (err) {
      setError('Failed to fetch stats');
      console.error('Error fetching stats:', err);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <div className="flex justify-between items-center mb-8">
        <div className="text-center">
          <h1 className="page-title">Welcome to Online Training System</h1>
          <p className="text-lg text-gray-600 mb-6">
            Manage users, training programs, and allocations all in one place
          </p>
        </div>
        <button onClick={onLogout} className="btn btn-danger flex items-center gap-2">
          <LogOut size={18} /> Logout
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <Link to="/users" className="card hover:shadow-lg transition-shadow">
          <div className="flex items-center justify-between mb-4">
            <Users size={32} className="text-blue-500" />
            <ArrowRight size={20} className="text-gray-400" />
          </div>
          <h3 className="text-xl font-semibold mb-2">User Management</h3>
          <p className="text-gray-600">
            Create, view, edit, and manage user accounts and profiles
          </p>
        </Link>

        <Link to="/trainings" className="card hover:shadow-lg transition-shadow">
          <div className="flex items-center justify-between mb-4">
            <BookOpen size={32} className="text-green-500" />
            <ArrowRight size={20} className="text-gray-400" />
          </div>
          <h3 className="text-xl font-semibold mb-2">Training Programs</h3>
          <p className="text-gray-600">
            Manage training programs, courses, and educational content
          </p>
        </Link>

        <Link to="/allocations" className="card hover:shadow-lg transition-shadow">
          <div className="flex items-center justify-between mb-4">
            <UserCheck size={32} className="text-purple-500" />
            <ArrowRight size={20} className="text-gray-400" />
          </div>
          <h3 className="text-xl font-semibold mb-2">Allocations</h3>
          <p className="text-gray-600">
            Assign training programs to users and track enrollments
          </p>
        </Link>
      </div>

      <div className="card mt-8">
        <h2 className="text-xl font-semibold mb-4">Quick Stats</h2>
        {loading ? (
          <div className="text-center py-4">Loading stats...</div>
        ) : error ? (
          <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">{error}</div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
            <div className="text-center">
              <div className="text-2xl font-bold text-blue-500">{stats.users}</div>
              <div className="text-gray-600">Total Users</div>
            </div>
            <div className="text-center">
              <div className="text-2xl font-bold text-green-500">{stats.trainings}</div>
              <div className="text-gray-600">Training Programs</div>
            </div>
            <div className="text-center">
              <div className="text-2xl font-bold text-purple-500">{stats.allocations}</div>
              <div className="text-gray-600">Active Allocations</div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

export default Home;