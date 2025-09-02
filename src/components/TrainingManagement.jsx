import React, { useState, useEffect } from 'react';
import { trainingApi } from '../services/api';
import { Plus, Edit, Trash2, X } from 'lucide-react';

function TrainingManagement() {
  const [trainings, setTrainings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [showModal, setShowModal] = useState(false);
  const [editingTraining, setEditingTraining] = useState(null);
  const [formData, setFormData] = useState({
    programName: '',
    description: '',
    price: '',
    status: 'ACTIVE'
  });

  useEffect(() => {
    fetchTrainings();
  }, []);

  const fetchTrainings = async () => {
    try {
      setLoading(true);
      const response = await trainingApi.getAllTrainings();
      setTrainings(response.data);
    } catch (err) {
      setError('Failed to fetch training programs');
      console.error('Error fetching trainings:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const trainingData = {
        ...formData,
        price: parseFloat(formData.price),
        programId: editingTraining ? editingTraining.programId : 0
      };
      
      await trainingApi.saveTraining(trainingData);
      await fetchTrainings();
      resetForm();
      setShowModal(false);
    } catch (err) {
      setError('Failed to save training program');
      console.error('Error saving training:', err);
    }
  };

  const handleEdit = (training) => {
    setEditingTraining(training);
    setFormData({
      programName: training.programName || '',
      description: training.description || '',
      price: training.price?.toString() || '',
      status: training.status || 'ACTIVE'
    });
    setShowModal(true);
  };

  const handleDelete = async (programId) => {
    if (window.confirm('Are you sure you want to delete this training program?')) {
      try {
        await trainingApi.deleteTraining(programId);
        await fetchTrainings();
      } catch (err) {
        setError('Failed to delete training program');
        console.error('Error deleting training:', err);
      }
    }
  };

  const resetForm = () => {
    setFormData({
      programName: '',
      description: '',
      price: '',
      status: 'ACTIVE'
    });
    setEditingTraining(null);
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value
    });
  };

  if (loading) {
    return <div className="text-center py-8">Loading training programs...</div>;
  }

  return (
    <div>
      <div className="flex justify-between items-center mb-6">
        <h1 className="page-title">Training Program Management</h1>
        <button
          onClick={() => {
            resetForm();
            setShowModal(true);
          }}
          className="btn btn-primary"
        >
          <Plus size={16} />
          Add Program
        </button>
      </div>

      {error && (
        <div className="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded mb-4">
          {error}
        </div>
      )}

      <div className="card">
        <div className="overflow-x-auto">
          <table className="table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Program Name</th>
                <th>Description</th>
                <th>Price</th>
                <th>Status</th>
                <th>Enrollments</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {trainings.length === 0 ? (
                <tr>
                  <td colSpan="7" className="text-center py-8 text-gray-500">
                    No training programs found
                  </td>
                </tr>
              ) : (
                trainings.map((training) => (
                  <tr key={training.programId}>
                    <td>{training.programId}</td>
                    <td className="font-medium">{training.programName}</td>
                    <td>
                      <div className="max-w-xs truncate" title={training.description}>
                        {training.description}
                      </div>
                    </td>
                    <td>₹{training.price?.toFixed(2)}</td>
                    <td>
                      <span className={`px-2 py-1 rounded-full text-xs ${
                        training.status === 'ACTIVE' 
                          ? 'bg-green-100 text-green-800'
                          : training.status === 'COMPLETED'
                          ? 'bg-blue-100 text-blue-800'
                          : 'bg-red-100 text-red-800'
                      }`}>
                        {training.status}
                      </span>
                    </td>
                    <td>{training.purchasedByCount || 0}</td>
                    <td>
                      <div className="flex gap-2">
                        <button
                          onClick={() => handleEdit(training)}
                          className="btn btn-secondary"
                        >
                          <Edit size={14} />
                        </button>
                        <button
                          onClick={() => handleDelete(training.programId)}
                          className="btn btn-danger"
                        >
                          <Trash2 size={14} />
                        </button>
                      </div>
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
          <div className="bg-white rounded-lg p-6 w-full max-w-lg">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-semibold">
                {editingTraining ? 'Edit Training Program' : 'Add New Training Program'}
              </h2>
              <button
                onClick={() => setShowModal(false)}
                className="text-gray-400 hover:text-gray-600"
              >
                <X size={20} />
              </button>
            </div>

            <form onSubmit={handleSubmit}>
              <div className="form-group">
                <label className="form-label">Program Name</label>
                <input
                  type="text"
                  name="programName"
                  value={formData.programName}
                  onChange={handleChange}
                  className="form-input"
                  required
                />
              </div>

              <div className="form-group">
                <label className="form-label">Description</label>
                <textarea
                  name="description"
                  value={formData.description}
                  onChange={handleChange}
                  className="form-input"
                  rows={3}
                  required
                />
              </div>

              <div className="form-group">
                <label className="form-label">Price ($)</label>
                <input
                  type="number"
                  name="price"
                  value={formData.price}
                  onChange={handleChange}
                  className="form-input"
                  step="0.01"
                  min="0"
                  required
                />
              </div>

              <div className="form-group">
                <label className="form-label">Status</label>
                <select
                  name="status"
                  value={formData.status}
                  onChange={handleChange}
                  className="form-select"
                >
                  <option value="ACTIVE">Active</option>
                  <option value="COMPLETED">Completed</option>
                  <option value="CANCELLED">Cancelled</option>
                </select>
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
                  {editingTraining ? 'Update' : 'Create'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

export default TrainingManagement;