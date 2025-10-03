// src/App.jsx
import React from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router';
import { VersionsPage } from './pages/VersionsPage';
import { DevicesPage } from './pages/DevicesPage';
// import './App.css';

function App() {
  return (
    <Router>
      <div className="min-h-screen bg-gray-50">
        <nav className="bg-blue-600 text-white p-4 shadow-md">
          <div className="container mx-auto flex space-x-4">
            <Link to="/versions" className="hover:underline font-medium">App Versions</Link>
            <Link to="/devices" className="hover:underline font-medium">Devices</Link>
          </div>
        </nav>
        
        <main className="container mx-auto p-4">
          <Routes>
            <Route path="/versions" element={<VersionsPage />} />
            <Route path="/devices" element={<DevicesPage />} />
            <Route path="/" element={<VersionsPage />} />
          </Routes>
        </main>
      </div>
    </Router>
  );
}

export default App;