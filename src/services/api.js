// api.js
import axios from "axios";

const BASE_URL = "http://localhost:8090/api";

const api = axios.create({
  baseURL: BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// User API
export const userApi = {
  getAllUsers: () => api.get("/users"),
  getUserById: (id) => api.get(`/users/${id}`),
  saveUser: (user) => api.post("/users", user),
  deleteUser: (id) => api.delete(`/users/${id}`),
  getAvailablePrograms: (userId) => api.get(`/user/available-programs/${userId}`),
  getEnrollments: (userId) => api.get(`/user/my-trainings/${userId}`), // ✅ added
  getMyTrainings: (userId) => api.get(`/user/my-trainings/${userId}`),
  enrollProgram: (userId, programId) => api.post(`/user/enroll/${userId}/${programId}`),
  // Robust enrollment: backend expects body { userId, trainingId }
  enrollInProgram: async (userId, programId) => {
    // 1. Body style (preferred, matches provided controller)
    try {
      return await api.post(`/user/enroll`, { userId, trainingId: programId });
    } catch (err) {
      const status = err?.response?.status;
      if (status && ![404, 400, 405].includes(status)) throw err;
      console.warn("Body enroll endpoint failed, trying path variant", status, err?.response?.data);
      if (status === 400) throw err; // propagate validation like Already enrolled
    }
    // 2. Path param variant
    try {
      return await api.post(`/user/enroll/${userId}/${programId}`);
    } catch (err2) {
      const status2 = err2?.response?.status;
      if (status2 && ![404, 400, 405].includes(status2)) throw err2;
      console.warn("Path enroll failed, trying allocations fallback", status2, err2?.response?.data);
      if (status2 === 400) throw err2;
    }
    // 3. Allocation style (legacy)
    return api.post(`/allocations`, { userId, trainingId: programId });
  },

};


// Training API
export const trainingApi = {
  getAllTrainings: () => api.get("/training"),
  getTrainingById: (id) => api.get(`/training/${id}`),
  saveTraining: (training) => api.post("/training", training),
  deleteTraining: (id) => api.delete(`/training/${id}`),
};

// Allocation API
export const allocationApi = {
  getAllAllocations: () => api.get("/allocations"),
  getAllocationById: (id) => api.get(`/allocations/${id}`),
  saveAllocation: (allocation) => api.post("/allocations", allocation),
  deleteAllocation: (id) => api.delete(`/allocations/${id}`),
};

export default api;
