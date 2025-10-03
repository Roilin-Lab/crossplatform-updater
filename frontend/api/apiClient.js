// src/api/apiClient.js
const API_BASE_URL = 'http://localhost:8181';

// Helper function to handle API responses
const handleResponse = async (response) => {
  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(`HTTP error! status: ${response.status}, message: ${errorText}`);
  }
  return response.json();
};

// App Versions API
export const appVersionsApi = {
  getAllVersions: async (platform) => {
    const url = platform 
      ? `${API_BASE_URL}/api/versions?platform=${platform}` 
      : `${API_BASE_URL}/api/versions`;
    const response = await fetch(url);
    return handleResponse(response);
  },

  getVersionById: async (id) => {
    const response = await fetch(`${API_BASE_URL}/api/versions/${id}`);
    return handleResponse(response);
  },

  createVersion: async (versionData) => {
    const response = await fetch(`${API_BASE_URL}/api/versions`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(versionData),
    });
    return handleResponse(response);
  },

  updateVersion: async (id, versionData) => {
    const response = await fetch(`${API_BASE_URL}/api/versions/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(versionData),
    });
    return handleResponse(response);
  },

  deleteVersion: async (id) => {
    const response = await fetch(`${API_BASE_URL}/api/versions/${id}`, {
      method: 'DELETE',
    });
    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`HTTP error! status: ${response.status}, message: ${errorText}`);
    }
    return response.text();
  },

  getLatestVersion: async (platform) => {
    const response = await fetch(`${API_BASE_URL}/api/versions/latest?platform=${platform}`);
    return handleResponse(response);
  }
};

// Devices API
export const devicesApi = {
  getAllDevices: async (username) => {
    const response = await fetch(`${API_BASE_URL}/api/devices?username=${username}`);
    return handleResponse(response);
  },

  getDeviceById: async (id) => {
    const response = await fetch(`${API_BASE_URL}/api/devices/${id}`);
    return handleResponse(response);
  },

  createDevice: async (deviceData) => {
    const response = await fetch(`${API_BASE_URL}/api/devices`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(deviceData),
    });
    return handleResponse(response);
  },

  updateDevice: async (id, deviceData) => {
    const response = await fetch(`${API_BASE_URL}/api/devices/${id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(deviceData),
    });
    return handleResponse(response);
  },

  deleteDevice: async (id) => {
    const response = await fetch(`${API_BASE_URL}/api/devices/${id}`, {
      method: 'DELETE',
    });
    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`HTTP error! status: ${response.status}, message: ${errorText}`);
    }
    return response.text();
  }
};