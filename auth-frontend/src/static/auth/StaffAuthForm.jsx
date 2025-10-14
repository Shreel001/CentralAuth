import { useState } from "react";
import "./AuthForm.css";
import { useNavigate } from "react-router-dom";


export default function StaffAuthForm() {
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
      ? "http://localhost:8080/auth/staff/login"
      : "http://localhost:8080/auth/staff/signup";

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
      if (isLogin) localStorage.setItem("token", data.token);
        localStorage.setItem("token", data.token);
        localStorage.setItem("user", JSON.stringify(data));
        navigate("/dashboard");
      setMessage(isLogin ? "Login successful!" : "Signup successful!");
    } catch (err) {
      setMessage("Error: " + err.message);
    }
  };

  return (
    <div className="auth-container">
      <div className="auth-form-container" id="left-container">
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
                <select
                  value={role}
                  onChange={(e) => setRole(e.target.value)}
                  required
                >
                  <option value="">Select Role</option>
                  <option value="CUSTOMER_REP">CUSTOMER_REP</option>
                  <option value="PERSONAL_BANKER">PERSONAL_BANKER</option>
                  <option value="ACCOUNT_MANAGER">ACCOUNT_MANAGER</option>
                  <option value="LOAN_OFFICER">LOAN_OFFICER</option>
                  <option value="BRANCH_MANAGER">BRANCH_MANAGER</option>
                  <option value="REGIONAL_MANAGER">REGIONAL_MANAGER</option>
                  <option value="ADMIN">ADMIN</option>
                  <option value="COMPLIANCE_OFFICER">COMPLIANCE_OFFICER</option>
                  <option value="SECURITY_ADMIN">SECURITY_ADMIN</option>
                  <option value="API_USER">API_USER</option>
                  <option value="REPORTING_SERVICE">REPORTING_SERVICE</option>
                  <option value="AUDIT_SERVICE">AUDIT_SERVICE</option>
                </select>
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
      <div className="auth-image">
        <img
          src="https://img.freepik.com/premium-vector/media-planning-isolated-cartoon-vector-illustrations_107173-22693.jpg"
          alt="Library"
        />
      </div>

    </div>
  );
}