import React, { useState } from "react";

function Login({ setIsAuthenticated, setRole }) {
  // Login form state
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  // Signup form state
  const [isSignup, setIsSignup] = useState(false);
  const [name, setName] = useState("");
  const [email, setEmail] = useState("");
  const [phone, setPhone] = useState("");
  const [signupPassword, setSignupPassword] = useState("");
  const [roleInput, setRoleInput] = useState("user");
  // Feedback
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setSuccess("");

    if (isSignup) {
      if (!name || !email || !signupPassword || !roleInput) {
        setError("Please fill all required fields.");
        return;
      }
      try {
        const response = await fetch("http://localhost:8090/api/signup", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            name: name.trim(),
            email: email.trim(),
            phone: phone.trim(),
            password: signupPassword,
            role: roleInput
          })
        });
        const data = await response.json();
        if (!response.ok || data.success === false) {
          setError(data.message || "Signup failed");
          return;
        }
        // Always redirect back to login form after successful signup
        setSuccess(data.message || "Signup successful. Please log in.");
        setIsSignup(false);
        setUsername(email); // prefill login email
        // Clear signup-specific fields
        setName("");
        setEmail("");
        setPhone("");
        setSignupPassword("");
        setRoleInput("user");
      } catch (err) {
        setError(err.message || "Signup error");
      }
      return;
    }

    // 🔹 Local logic for demo/testing
  if (username === "admin" && password === "1234") {
      setIsAuthenticated(true);
      setRole("admin");
      localStorage.setItem("userId", "1"); // Demo admin userId
      console.log("Set userId in localStorage:", localStorage.getItem("userId"));
      return;
    } else if (username === "user" && password === "1234") {
      setIsAuthenticated(true);
      setRole("user");
      localStorage.setItem("userId", "2"); // Demo user userId
      console.log("Set userId in localStorage:", localStorage.getItem("userId"));
      return;
    }
    // 🔹 Backend login for production
    try {
      const response = await fetch("http://localhost:8090/api/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email: username, password }),
      });
      const data = await response.json();
      console.log("Backend login response:", data); // 👀 Debug
      if (response.ok && data.success) {
        setIsAuthenticated(true);
        // 🔹 Get role and userId from backend response
        const userRole = (data.user && data.user.role) ? data.user.role.toLowerCase() : null;
        const userId = (data.user && data.user.id) ? data.user.id : null;
        if (userId) {
          localStorage.setItem("userId", userId);
          console.log("Set userId in localStorage:", localStorage.getItem("userId"));
        }
        if (userRole === "admin") {
          setRole("admin");
        } else if (userRole === "user") {
          setRole("user");
        } else {
          setRole(null); // fallback
        }
      } else {
        setError(data.message || "Invalid username or password.");
      }
    } catch (err) {
      setError("Login error: " + err.message);
    }
  };

  return (
    <div className="login-container">
      <div className="login-box text-center">
        <h1 className="login-title">Online Training System</h1>
        <p className="login-subtitle mb-4">{isSignup ? "Create an account" : "Sign in to continue"}</p>

        {success && <p className="text-green-600 text-sm mb-3">{success}</p>}
        {error && <p className="login-error mb-3">{error}</p>}

        <form onSubmit={handleSubmit} className="login-form text-left">
          {isSignup ? (
            <>
              <div className="form-group">
                <label htmlFor="name" className="form-label">Name <span className="text-red-500">*</span></label>
                <input
                  id="name"
                  type="text"
                  className="form-input"
                  placeholder="Full name"
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="email" className="form-label">Email <span className="text-red-500">*</span></label>
                <input
                  id="email"
                  type="email"
                  className="form-input"
                  placeholder="Email address"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="phone" className="form-label">Phone <span className="text-red-500">*</span></label>
                <input
                  id="phone"
                  type="tel"
                  className="form-input"
                  placeholder="Phone"
                  value={phone}
                  onChange={(e) => setPhone(e.target.value)}
                />
              </div>
              <div className="form-group">
                <label htmlFor="signupPassword" className="form-label">Password <span className="text-red-500">*</span></label>
                <input
                  id="signupPassword"
                  type="password"
                  className="form-input"
                  placeholder="Create a password"
                  value={signupPassword}
                  onChange={(e) => setSignupPassword(e.target.value)}
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="role" className="form-label">Role <span className="text-red-500">*</span></label>
                <select
                  id="role"
                  className="form-input"
                  value={roleInput}
                  onChange={(e) => setRoleInput(e.target.value)}
                  required
                >
                  <option value="user">User</option>
                  <option value="admin">Admin</option>
                </select>
              </div>
              <button type="submit" className="login-button w-full">Sign Up</button>
            </>
          ) : (
            <>
              <div className="form-group">
                <label htmlFor="username" className="form-label">Email <span className="text-red-500">*</span></label>
                <input
                  id="username"
                  type="text"
                  className="form-input"
                  placeholder="Enter your email"
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="password" className="form-label">Password <span className="text-red-500">*</span></label>
                <input
                  id="password"
                  type="password"
                  className="form-input"
                  placeholder="Enter your password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  required
                />
              </div>
              <button type="submit" className="login-button w-full">Login</button>
            </>
          )}
        </form>

        <div className="mt-6 text-sm text-gray-600">
          {isSignup ? (
            <>
              <span>Already have an account? </span>
              <button
                type="button"
                className="text-purple-600 font-medium hover:underline"
                onClick={() => {
                  setIsSignup(false);
                  setError("");
                  setSuccess("");
                }}
              >
                Log in
              </button>
            </>
          ) : (
            <>
              <span>Don't have an account? </span>
              <button
                type="button"
                className="text-purple-600 font-medium hover:underline"
                onClick={() => {
                  setIsSignup(true);
                  setError("");
                  setSuccess("");
                }}
              >
                Sign up
              </button>
            </>
          )}
        </div>
      </div>
    </div>
  );
}

export default Login;