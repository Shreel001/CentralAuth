import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import UserAuthForm from "./static/UserAuthForm";
import StaffAuthForm from "./static/StaffAuthForm";
import Dashboard from "./static/Dashboard";

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/user/auth" element={<UserAuthForm />} />
        <Route path="/staff/auth" element={<StaffAuthForm />} />
        <Route path="/dashboard" element={<Dashboard />} />
      </Routes>
    </Router>
  );
}

export default App;
