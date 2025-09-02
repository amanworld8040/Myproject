import React, { useState, useEffect } from 'react';
import api, { allocationApi, userApi, trainingApi } from '../services/api';
import { Plus, Trash2, X, Calendar } from 'lucide-react';

function AllocationManagement() {
  const [allocations, setAllocations] = useState([]);
  const [users, setUsers] = useState([]);
  const [trainings, setTrainings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [formData, setFormData] = useState({
    userId: '',
    programId: '',
    allocatedById: '1', // Default admin ID
    allocationDate: new Date().toISOString().split('T')[0]
  });

  useEffect(() => {
    fetchData();
  }, []);

  const fetchData = async () => { 
    try {
      setLoading(true);
      const [allocationsRes, usersRes, trainingsRes] = await Promise.all([
        allocationApi.getAllAllocations(),
        userApi.getAllUsers(),
        trainingApi.getAllTrainings()
      ]);
      setAllocations(allocationsRes.data);
      setUsers(usersRes.data);
      setTrainings(trainingsRes.data);
    } catch (err) {
      setError('Failed to fetch data');
      console.error('Error fetching data:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const allocationData = {
        ...formData,
        userId: parseInt(formData.userId),
        programId: parseInt(formData.programId),
        allocatedById: parseInt(formData.allocatedById),
        allocationId: 0
      };
      
      await allocationApi.saveAllocation(allocationData);
      await fetchData();
      resetForm();
      setShowModal(false);
    } catch (err) {
      setError('Failed to create allocation');
      console.error('Error saving allocation:', err);
    }
  };

  const handleDelete = async (allocationId) => {
    if (window.confirm('Are you sure you want to delete this allocation?')) {
      try {
        await allocationApi.deleteAllocation(allocationId);
        await fetchData();
      } catch (err) {
        setError('Failed to delete allocation');
        console.error('Error deleting allocation:', err);
      }
    }
  };

  const resetForm = () => {
    setFormData({
      userId: '',
      programId: '',
      allocatedById: '1',
      allocationDate: new Date().toISOString().split('T')[0]
    });
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  const getUserName = (userId) => {
    const user = users.find(u => u.userId === userId);
    return user ? user.name : `User #${userId}`;
  };

  const getProgramName = (programId) => {
    const program = trainings.find(t => t.programId === programId);
    return program ? program.programName : `Program #${programId}`;
  };

  if (loading) {
    return <div className="text-center py-8">Loading allocations...</div>;
  }

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="page-title">Allocation Management</h1>
        <button
          onClick={() => {
            resetForm();
            setShowModal(true);
          }}
          className="btn btn-primary"
        >
          <Plus size={16} />
          Create Allocation
        </button>
      </div>

      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
          {error}
        </div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 mb-6">
        <div className="card">
          <h3 className="text-lg font-semibold mb-2">Total Allocations</h3>
          <div className="text-2xl font-bold text-blue-500">{allocations.length}</div>
        </div>
        <div className="card">
          <h3 className="text-lg font-semibold mb-2">Active Users</h3>
          <div className="text-2xl font-bold text-green-500">{users.length}</div>
        </div>
        <div className="card">
          <h3 className="text-lg font-semibold mb-2">Available Programs</h3>
          <div className="text-2xl font-bold text-purple-500">{trainings.length}</div>
        </div>
      </div>

      <div className="card">
        <div className="overflow-x-auto">
          <table className="table">
            <thead>
              <tr>
                <th>ID</th>
                <th>User</th>
                <th>Training Program</th>
                <th>Allocated By</th>
                <th>Allocation Date</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {allocations.length === 0 ? (
                <tr>
                  <td colSpan="6" className="text-center py-8 text-gray-500">
                    No allocations found
                  </td>
                </tr>
              ) : (
                allocations.map((allocation) => (
                  <tr key={allocation.allocationId}>
                    <td>{allocation.allocationId}</td>
                    <td>{getUserName(allocation.userId)}</td>
                    <td>{getProgramName(allocation.programId)}</td>
                    <td>{getUserName(allocation.allocatedById)}</td>
                    <td>
                      <div className="flex items-center gap-2">
                        <Calendar size={14} className="text-gray-400" />
                        {new Date(allocation.allocationDate).toLocaleDateString()}
                      </div>
                    </td>
                    <td>
                      <button
                        onClick={() => handleDelete(allocation.allocationId)}
                        className="btn btn-danger"
                      >
                        <Trash2 size={14} />
                      </button>
                    </td>
                  </tr>
                ))
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* Modal */}
      {showModal && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
          <div className="bg-white rounded-lg p-6 w-full max-w-md">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-semibold">Create New Allocation</h2>
              <button
                onClick={() => setShowModal(false)}
                className="text-gray-400 hover:text-gray-600"
              >
                <X size={20} />
              </button>
            </div>

            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label className="form-label">Select User</label>
                <select
                  name="userId"
                  value={formData.userId}
                  onChange={handleChange}
                  className="form-select"
                  required
                >
                  <option value="">Choose a user...</option>
                  {users.map((user) => (
                    <option key={user.userId} value={user.userId}>
                      {user.name} ({user.email})
                    </option>
                  ))}
                </select>
              </div>

              <div className="form-group">
                <label className="form-label">Select Training Program</label>
                <select
                  name="programId"
                  value={formData.programId}
                  onChange={handleChange}
                  className="form-select"
                  required
                >
                  <option value="">Choose a program...</option>
                  {trainings.filter(t => t.status === 'ACTIVE').map((training) => (
                    <option key={training.programId} value={training.programId}>
                      {training.programName} (Rs{training.price})
                    </option>
                  ))}
                </select>
              </div>

              <div className="form-group">
                <label className="form-label">Allocated By (Admin)</label>
                <select
                  name="allocatedById"
                  value={formData.allocatedById}
                  onChange={handleChange}
                  className="form-select"
                  required
                >
                  {users.filter(u => u.role === 'ADMIN').map((admin) => (
                    <option key={admin.userId} value={admin.userId}>
                      {admin.name}
                    </option>
                  ))}
                </select>
              </div>

              <div className="form-group">
                <label className="form-label">Allocation Date</label>
                <input
                  type="date"
                  name="allocationDate"
                  value={formData.allocationDate}
                  onChange={handleChange}
                  className="form-input"
                  required
                />
              </div>

              <div className="flex gap-3 justify-end mt-6">
                <button
                  type="button"
                  onClick={() => setShowModal(false)}
                  className="btn btn-secondary"
                >
                  Cancel
                </button>
                <button type="submit" className="btn btn-primary">
                  Create Allocation
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

export default AllocationManagement;