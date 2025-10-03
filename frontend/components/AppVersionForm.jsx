// src/components/AppVersionForm.jsx
import React, { useState } from 'react';

export const AppVersionForm = ({ onSubmit, initialValues = null, onCancel }) => {
  const [formData, setFormData] = useState({
    version: initialValues?.version || '',
    platform: initialValues?.platform || 'IOS',
    releaseDate: initialValues?.releaseDate ? initialValues.releaseDate: '',
    changeLog: initialValues?.changeLog || '',
    updateType: initialValues?.updateType || 'OPTIONAL',
    active: initialValues?.active ?? true
  });
  
  const [error, setError] = useState('');

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: type === 'checkbox' ? checked : value
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    
    // Validate version format
    const versionRegex = /^\d+\.\d+\.\d+$/;
    if (!versionRegex.test(formData.version)) {
      setError('Version must be in format X.X.X (e.g., 1.2.3)');
      return;
    }
    
    // Validate release date
    if (!formData.releaseDate) {
      setError('Release date is required');
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
        {initialValues ? 'Edit App Version' : 'Create New App Version'}
      </h2>
      
      {error && (
        <div className="mb-4 p-3 bg-red-100 text-red-700 rounded">
          {error}
        </div>
      )}
      
      <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
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
            placeholder="e.g., 1.2.3"
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
            Release Date *
          </label>
          <input
            type="datetime-local"
            name="releaseDate"
            value={formData.releaseDate}
            onChange={handleChange}
            className="w-full p-2 border border-gray-300 rounded"
            required
          />
        </div>
        
        <div>
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Update Type *
          </label>
          <select
            name="updateType"
            value={formData.updateType}
            onChange={handleChange}
            className="w-full p-2 border border-gray-300 rounded"
            required
          >
            <option value="MANDATORY">Mandatory</option>
            <option value="OPTIONAL">Optional</option>
            <option value="DEPRECATED">Deprecated</option>
          </select>
        </div>
        
        <div className="md:col-span-2">
          <label className="block text-sm font-medium text-gray-700 mb-1">
            Change Log *
          </label>
          <textarea
            name="changeLog"
            value={formData.changeLog}
            onChange={handleChange}
            className="w-full p-2 border border-gray-300 rounded h-24"
            required
          />
        </div>
        
        <div className="flex items-center">
          <input
            type="checkbox"
            name="active"
            checked={formData.active}
            onChange={handleChange}
            className="mr-2"
          />
          <label className="text-sm font-medium text-gray-700">
            Active
          </label>
        </div>
      </div>
      
      <div className="mt-6 flex space-x-3">
        <button
          type="submit"
          className="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700"
        >
          {initialValues ? 'Update Version' : 'Create Version'}
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