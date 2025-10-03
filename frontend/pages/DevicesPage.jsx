// src/pages/DevicesPage.jsx
import React, { useState, useEffect } from 'react';
import { devicesApi } from '../api/apiClient';
import { DeviceForm } from '../components/DeviceForm';

export const DevicesPage = () => {
  const [devices, setDevices] = useState([]);
  const [selectedDevice, setSelectedDevice] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [editingDevice, setEditingDevice] = useState(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [usernameFilter, setUsernameFilter] = useState('');

  const fetchDevices = async () => {
    if (!usernameFilter.trim()) {
      setDevices([]);
      return;
    }
    
    setLoading(true);
    setError('');
    try {
      const data = await devicesApi.getAllDevices(usernameFilter);
      setDevices(Array.isArray(data) ? data : []);
    } catch (err) {
      setError(err.message || 'Failed to fetch devices');
      setDevices([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (usernameFilter.trim()) {
      fetchDevices();
    } else {
      setDevices([]);
    }
  }, [usernameFilter]);

  const handleCreate = async (deviceData) => {
    try {
      await devicesApi.createDevice(deviceData);
      setShowForm(false);
      fetchDevices();
    } catch (err) {
      throw err;
    }
  };

  const handleUpdate = async (deviceData) => {
    try {
      await devicesApi.updateDevice(editingDevice.id, deviceData);
      setShowForm(false);
      setEditingDevice(null);
      fetchDevices();
    } catch (err) {
      throw err;
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this device?')) return;
    
    try {
      await devicesApi.deleteDevice(id);
      fetchDevices();
    } catch (err) {
      setError(err.message || 'Failed to delete device');
    }
  };

  const handleEdit = (device) => {
    setEditingDevice(device);
    setShowForm(true);
  };

  const handleView = async (id) => {
    try {
      const device = await devicesApi.getDeviceById(id);
      setSelectedDevice(device);
    } catch (err) {
      setError(err.message || 'Failed to fetch device details');
    }
  };

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold text-gray-800">Devices</h1>
        <button
          onClick={() => {
            setEditingDevice(null);
            setShowForm(true);
          }}
          className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700"
          disabled={!usernameFilter.trim()}
        >
          Create New Device
        </button>
      </div>

      {error && (
        <div className="p-3 bg-red-100 text-red-700 rounded">
          {error}
        </div>
      )}

      {/* Username Filter */}
      <div className="bg-white p-4 rounded-lg shadow">
        <div className="flex items-center space-x-4">
          <label className="font-medium">Filter by Username:</label>
          <input
            type="text"
            value={usernameFilter}
            onChange={(e) => setUsernameFilter(e.target.value)}
            className="p-2 border border-gray-300 rounded flex-1"
            placeholder="Enter username to filter devices..."
          />
        </div>
        {!usernameFilter.trim() && (
          <p className="mt-2 text-gray-500 text-sm">
            Please enter a username to view or manage devices.
          </p>
        )}
      </div>

      {/* Form Modal */}
      {showForm && (
        <div className="fixed inset-0 bg-black/30 backdrop-blur-xs bg-opacity-50 flex items-center justify-center p-4 z-50">
          <div className="w-full max-w-2xl">
            <DeviceForm
              onSubmit={editingDevice ? handleUpdate : handleCreate}
              initialValues={editingDevice}
              onCancel={() => {
                setShowForm(false);
                setEditingDevice(null);
              }}
            />
          </div>
        </div>
      )}

      {/* Selected Device Details */}
      {selectedDevice && (
        <div className="fixed inset-0 bg-black/30 backdrop-blur-xs bg-opacity-50 flex items-center justify-center p-4 z-50">
          <div className="bg-white p-6 rounded-lg shadow-lg max-w-2xl w-full max-h-[80vh] overflow-y-auto">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-bold">Device Details</h2>
              <button
                onClick={() => setSelectedDevice(null)}
                className="text-gray-500 hover:text-gray-700"
              >
                ✕
              </button>
            </div>
            <div className="space-y-3">
              <p><span className="font-medium">ID:</span> {selectedDevice.id}</p>
              <p><span className="font-medium">Name:</span> {selectedDevice.name}</p>
              <p><span className="font-medium">Platform:</span> {selectedDevice.platform}</p>
              <p><span className="font-medium">Owner Username:</span> {selectedDevice.ownerUsername}</p>
              <p><span className="font-medium">Current Version:</span> {selectedDevice.currentVersion}</p>
            </div>
          </div>
        </div>
      )}

      {/* Devices List */}
      <div className="bg-white rounded-lg shadow overflow-hidden">
        {usernameFilter.trim() ? (
          <div className="overflow-x-auto">
            <table className="min-w-full divide-y divide-gray-200">
              <thead className="bg-gray-50">
                <tr>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">ID</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Name</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Platform</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Owner</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Version</th>
                  <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
                </tr>
              </thead>
              <tbody className="bg-white divide-y divide-gray-200">
                {loading ? (
                  <tr>
                    <td colSpan="6" className="px-6 py-4 text-center">
                      Loading...
                    </td>
                  </tr>
                ) : devices.length === 0 ? (
                  <tr>
                    <td colSpan="6" className="px-6 py-4 text-center text-gray-500">
                      No devices found for username "{usernameFilter}"
                    </td>
                  </tr>
                ) : (
                  devices.map((device) => (
                    <tr key={device.id} className="hover:bg-gray-50">
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">{device.id}</td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{device.name}</td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{device.platform}</td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{device.ownerUsername}</td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{device.currentVersion}</td>
                      <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                        <button
                          onClick={() => handleView(device.id)}
                          className="text-blue-600 hover:text-blue-900 mr-3"
                        >
                          View
                        </button>
                        <button
                          onClick={() => handleEdit(device)}
                          className="text-indigo-600 hover:text-indigo-900 mr-3"
                        >
                          Edit
                        </button>
                        <button
                          onClick={() => handleDelete(device.id)}
                          className="text-red-600 hover:text-red-900"
                        >
                          Delete
                        </button>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        ) : (
          <div className="p-8 text-center text-gray-500">
            Enter a username above to view devices
          </div>
        )}
      </div>
    </div>
  );
};