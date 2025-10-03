// src/pages/VersionsPage.jsx
import React, { useState, useEffect } from 'react';
import { appVersionsApi } from '../api/apiClient';
import { AppVersionForm } from '../components/AppVersionForm';

export const VersionsPage = () => {
  const [versions, setVersions] = useState([]);
  const [latestVersions, setLatestVersions] = useState({ IOS: null, ANDROID: null });
  const [selectedVersion, setSelectedVersion] = useState(null);
  const [showForm, setShowForm] = useState(false);
  const [editingVersion, setEditingVersion] = useState(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [platformFilter, setPlatformFilter] = useState('');

  const fetchVersions = async () => {
    setLoading(true);
    setError('');
    try {
      const data = await appVersionsApi.getAllVersions(platformFilter);
      setVersions(Array.isArray(data) ? data : []);
    } catch (err) {
      setError(err.message || 'Failed to fetch versions');
      setVersions([]);
    } finally {
      setLoading(false);
    }
  };

  const fetchLatestVersions = async () => {
    try {
      const iosLatest = await appVersionsApi.getLatestVersion('IOS');
      const androidLatest = await appVersionsApi.getLatestVersion('ANDROID');
      setLatestVersions({ IOS: iosLatest, ANDROID: androidLatest });
    } catch (err) {
      // Handle errors silently for latest versions
    }
  };

  useEffect(() => {
    fetchVersions();
    fetchLatestVersions();
  }, [platformFilter]);

  const handleCreate = async (versionData) => {
    try {
      await appVersionsApi.createVersion(versionData);
      setShowForm(false);
      fetchVersions();
      fetchLatestVersions();
    } catch (err) {
      throw err;
    }
  };

  const handleUpdate = async (versionData) => {
    try {
      await appVersionsApi.updateVersion(editingVersion.id, versionData);
      setShowForm(false);
      setEditingVersion(null);
      fetchVersions();
      fetchLatestVersions();
    } catch (err) {
      throw err;
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this version?')) return;
    
    try {
      await appVersionsApi.deleteVersion(id);
      fetchVersions();
      fetchLatestVersions();
    } catch (err) {
      setError(err.message || 'Failed to delete version');
    }
  };

  const handleEdit = (version) => {
    setEditingVersion(version);
    setShowForm(true);
  };

  const handleView = async (id) => {
    try {
      const version = await appVersionsApi.getVersionById(id);
      setSelectedVersion(version);
    } catch (err) {
      setError(err.message || 'Failed to fetch version details');
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return '';
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  };

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold text-gray-800">App Versions</h1>
        <button
          onClick={() => {
            setEditingVersion(null);
            setShowForm(true);
          }}
          className="px-4 py-2 bg-green-600 text-white rounded hover:bg-green-700"
        >
          Create New Version
        </button>
      </div>

      {error && (
        <div className="p-3 bg-red-100 text-red-700 rounded">
          {error}
        </div>
      )}

      {/* Latest Versions */}
      <div className="bg-white p-4 rounded-lg shadow">
        <h2 className="text-lg font-semibold mb-3">Latest Versions</h2>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div className="border border-gray-200 p-3 rounded">
            <h3 className="font-medium text-blue-600">iOS</h3>
            {latestVersions.IOS ? (
              <div>
                <p>Version: {latestVersions.IOS.version}</p>
                <p>Release: {formatDate(latestVersions.IOS.releaseDate)}</p>
                <p>Type: {latestVersions.IOS.updateType}</p>
              </div>
            ) : (
              <p className="text-gray-500">No iOS version found</p>
            )}
          </div>
          <div className="border border-gray-200 p-3 rounded">
            <h3 className="font-medium text-green-600">Android</h3>
            {latestVersions.ANDROID ? (
              <div>
                <p>Version: {latestVersions.ANDROID.version}</p>
                <p>Release: {formatDate(latestVersions.ANDROID.releaseDate)}</p>
                <p>Type: {latestVersions.ANDROID.updateType}</p>
              </div>
            ) : (
              <p className="text-gray-500">No Android version found</p>
            )}
          </div>
        </div>
      </div>

      {/* Filter */}
      <div className="bg-white p-4 rounded-lg shadow">
        <div className="flex items-center space-x-4">
          <label className="font-medium">Filter by Platform:</label>
          <select
            value={platformFilter}
            onChange={(e) => setPlatformFilter(e.target.value)}
            className="p-2 border border-gray-300 rounded"
          >
            <option value="">All Platforms</option>
            <option value="IOS">iOS</option>
            <option value="ANDROID">Android</option>
          </select>
        </div>
      </div>

      {/* Form Modal */}
      {showForm && (
        <div className="fixed inset-0 bg-black/30 backdrop-blur-sm bg-opacity-50 flex items-center justify-center p-4 m-0 z-50">
          <div className="w-full max-w-2xl">
            <AppVersionForm
              onSubmit={editingVersion ? handleUpdate : handleCreate}
              initialValues={editingVersion}
              onCancel={() => {
                setShowForm(false);
                setEditingVersion(null);
              }}
            />
          </div>
        </div>
      )}

      {/* Selected Version Details */}
      {selectedVersion && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
          <div className="bg-white p-6 rounded-lg shadow-lg max-w-2xl w-full max-h-[80vh] overflow-y-auto">
            <div className="flex justify-between items-center mb-4">
              <h2 className="text-xl font-bold">Version Details</h2>
              <button
                onClick={() => setSelectedVersion(null)}
                className="text-gray-500 hover:text-gray-700"
              >
                ✕
              </button>
            </div>
            <div className="space-y-3">
              <p><span className="font-medium">ID:</span> {selectedVersion.id}</p>
              <p><span className="font-medium">Version:</span> {selectedVersion.version}</p>
              <p><span className="font-medium">Platform:</span> {selectedVersion.platform}</p>
              <p><span className="font-medium">Release Date:</span> {formatDate(selectedVersion.releaseDate)}</p>
              <p><span className="font-medium">Change Log:</span> {selectedVersion.changeLog}</p>
              <p><span className="font-medium">Update Type:</span> {selectedVersion.updateType}</p>
              <p><span className="font-medium">Active:</span> {selectedVersion.active ? 'Yes' : 'No'}</p>
            </div>
          </div>
        </div>
      )}

      {/* Versions List */}
      <div className="bg-white rounded-lg shadow overflow-hidden">
        <div className="overflow-x-auto">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Version</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Platform</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Release Date</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Update Type</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Active</th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {loading ? (
                <tr>
                  <td colSpan="7" className="px-6 py-4 text-center">
                    Loading...
                  </td>
                </tr>
              ) : versions.length === 0 ? (
                <tr>
                  <td colSpan="7" className="px-6 py-4 text-center text-gray-500">
                    No versions found
                  </td>
                </tr>
              ) : (
                versions.map((version) => (
                  <tr key={version.id} className="hover:bg-gray-50">
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{version.version}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{version.platform}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{formatDate(version.releaseDate)}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">{version.updateType}</td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                      {version.active ? (
                        <span className="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-green-100 text-green-800">
                          Active
                        </span>
                      ) : (
                        <span className="px-2 inline-flex text-xs leading-5 font-semibold rounded-full bg-red-100 text-red-800">
                          Inactive
                        </span>
                      )}
                    </td>
                    <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                      <button
                        onClick={() => handleView(version.id)}
                        className="text-blue-600 hover:text-blue-900 mr-3"
                      >
                        View
                      </button>
                      <button
                        onClick={() => handleEdit(version)}
                        className="text-indigo-600 hover:text-indigo-900 mr-3"
                      >
                        Edit
                      </button>
                      <button
                        onClick={() => handleDelete(version.id)}
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
      </div>
    </div>
  );
};