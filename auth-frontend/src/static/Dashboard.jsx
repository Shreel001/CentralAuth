import { useState, useEffect } from "react";
import "./Dashboard.css"

export default function Dashboard() {
  const [user, setUser] = useState(null);

  useEffect(() => {
    const savedUser = localStorage.getItem("user");
    console.log(JSON.parse(savedUser).user)
    if (savedUser) {
      const userData = JSON.parse(savedUser).user;
      setUser(userData);
    }
  }, []);

  if (!user) {
    return (
      <div className="container">
        <h2>No user data available</h2>
      </div>
    );
  }

  return (
    <div className="container">
      <h1 className="greeting">Hello, {user.username} !</h1>
      <div className="user-info">
        <h2>Account Information</h2>
        <div className="info-row">
            <div className="username">
                <span className="label">Username: </span>
                <span className="value">{user.username}</span>
            </div>
            <div className="email">
                <span className="label">Email: </span>
                <span className="value">{user.email}</span>
            </div>
            <div className="role">
                <span className="label">Role: </span>
                <span className="value">{user.role}</span>
            </div>
        </div>
      </div>
    </div>
  );
}