import { useState, useEffect } from "react";
import "./Dashboard.css";

export default function Dashboard() {
  const [user, setUser] = useState(null);

  useEffect(() => {
    const savedUser = localStorage.getItem("user");
    if (savedUser) {
      try {
        const userData = JSON.parse(savedUser);
        setUser(userData);
      } catch (error) {
        console.error("Failed to parse user data from localStorage:", error);
      }
    }
  }, []);

  if (!user) {
    return (
      <div className="container">
        <h2>No user data available</h2>
      </div>
    );
  }

  // Handle the copy action
  const handleCopy = () => {
    if (user && user.token) {
      navigator.clipboard
        .writeText(user.token)
        .catch((err) => {
          console.error("Failed to copy token:", err);
        });
    }
  };

  // Truncate the token for display
  const truncatedToken =
    user.token && user.token.length > 12
      ? `${user.token.substring(0, 12)}...`
      : user.token;

  return (
    <div className="container">
      <h1 className="greeting">Hello, {user.username}!</h1>
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
          <div className="token">
            <span className="label">Token: </span>
            <span className="value">{truncatedToken}</span>
          </div>
          <button onClick={handleCopy}>Copy</button>
        </div>
      </div>
    </div>
  );
}