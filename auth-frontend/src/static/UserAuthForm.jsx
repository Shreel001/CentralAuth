import { useState } from "react";
import "./AuthForm.css";
import { useNavigate } from "react-router-dom";

export default function UserAuthForm() {
  const [isLogin, setIsLogin] = useState(true);
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [role, setRole] = useState("");
  const [message, setMessage] = useState("");
  
  const navigate = useNavigate();

  const handleSubmit = async (e) => {
    e.preventDefault();
    const url = isLogin
      ? "http://localhost:8080/users/login"
      : "http://localhost:8080/users/signup";

    const body = isLogin
      ? { username, password }
      : { username, email, password, role };

    try {
      const response = await fetch(url, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body),
      });

      if (!response.ok) {
        const errData = await response.json();
        setMessage(errData.message || "Operation failed");
        return;
      }

      const data = await response.json();
      if (isLogin) {
        localStorage.setItem("token", data.token);
        localStorage.setItem("user", JSON.stringify(data));
        navigate("/dashboard");
      }
      setMessage(isLogin ? "Login successful!" : "Signup successful!");
    } catch (err) {
      setMessage("Error: " + err.message);
    }
  };

  return (
    <div className="auth-container">
      {/* Left Side - Image */}
      <div className="auth-image" id="left-container">
        <img
          src="https://img.freepik.com/premium-vector/bank-transfer-isolated-cartoon-vector-illustrations_107173-21789.jpg"
          alt="Library"
        />
      </div>

      {/* Right Side - Auth container */}
      <div className="auth-form-container">
        <div className="auth-card">
          <div className="toggle-btns">
            <button
              className={isLogin ? "active" : ""}
              onClick={() => setIsLogin(true)}
            >
              Login
            </button>
            <button
              className={!isLogin ? "active" : ""}
              onClick={() => setIsLogin(false)}
            >
              Sign Up
            </button>
          </div>

          <form onSubmit={handleSubmit} className="auth-form">
            <h2>{isLogin ? "Login" : "Sign Up"}</h2>

            <input
              type="text"
              placeholder="Username"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />

            {!isLogin && (
              <>
                <input
                  type="email"
                  placeholder="Email"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />
                <input
                  type="text"
                  placeholder="Role"
                  value={role}
                  onChange={(e) => setRole(e.target.value)}
                />
              </>
            )}

            <input
              type="password"
              placeholder="Password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />

            <button type="submit" className="submit-btn">
              {isLogin ? "Login" : "Sign Up"}
            </button>

            <p className="message">{message}</p>
          </form>
        </div>
      </div>

    </div>
  );
}