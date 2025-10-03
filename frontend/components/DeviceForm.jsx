// src/components/DeviceForm.jsx
import React, { useState } from 'react';

export const DeviceForm = ({ onSubmit, initialValues = null, onCancel }) => {
  const [formData, setFormData] = useState({
    name: initialValues?.name || '',
    platform: initialValues?.platform || 'IOS',
    ownerUsername: initialValues?.ownerUsername || '',
    version: initialValues?.version || ''
  });
  
  const [error, setError] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    
    // Basic validation
    if (!formData.name.trim()) {
      setError('Device name is required');
      return;
    }
    
    if (!formData.ownerUsername.trim()) {
      setError('Owner username is required');
      return;
    }
    
    if (!formData.version.trim()) {
      setError('Version is required');
      return;
    }
    
    try {
      await onSubmit(formData);
    } catch (err) {
      setError(err.message || 'An error occurred');
    }
  };

  return (
    <form onSubmit={handleSubmit} className="bg-white p-6 rounded-lg shadow-md">
      <h2 className="text-xl font-bold mb-4">
        {initialValues ? 'Edit Device' : 'Create New Device'}
      </h2>
      
      {error && (
        <div className="mb-4 p-3 bg-red-100 text-red-700 rounded">
          {error}
        </div>
      )}
      
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Device Name *
          </label>
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
            className="w-full p-2 border border-gray-300 rounded"
            required
          />
        </div>
        
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Platform *
          </label>
          <select
            name="platform"
            value={formData.platform}
            onChange={handleChange}
            className="w-full p-2 border border-gray-300 rounded"
            required
          >
            <option value="IOS">iOS</option>
            <option value="ANDROID">Android</option>
          </select>
        </div>
        
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Owner Username *
          </label>
          <input
            type="text"
            name="ownerUsername"
            value={formData.ownerUsername}
            onChange={handleChange}
            className="w-full p-2 border border-gray-300 rounded"
            required
          />
        </div>
        
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Version *
          </label>
          <input
            type="text"
            name="version"
            value={formData.version}
            onChange={handleChange}
            className="w-full p-2 border border-gray-300 rounded"
            required
          />
        </div>
      </div>
      
      <div className="mt-6 flex space-x-3">
        <button
          type="submit"
          className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
        >
          {initialValues ? 'Update Device' : 'Create Device'}
        </button>
        <button
          type="button"
          onClick={onCancel}
          className="px-4 py-2 bg-gray-300 text-gray-700 rounded hover:bg-gray-400"
        >
          Cancel
        </button>
      </div>
    </form>
  );
};