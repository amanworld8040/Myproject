import React from 'react';
import { LogOut } from 'lucide-react';

function UserInterface({ onLogout }) {
  return (
    <div className="user-interface">
      <div className="flex justify-between items-center mb-8">
        <h1 className="page-title">User Dashboard</h1>
        <button onClick={onLogout} className="btn btn-danger flex items-center gap-2">
          <LogOut size={18} /> Logout
        </button>
      </div>
      <p className="text-lg text-gray-600 mb-6">
        Welcome! Here you can view your enrolled trainings, progress, and more.
      </p>
      {/* Add more user-specific features here */}
    </div>
  );
}

export default UserInterface;
