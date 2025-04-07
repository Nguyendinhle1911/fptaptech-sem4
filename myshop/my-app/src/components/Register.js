// src/components/Register.js
import React, { useState } from "react";
import { api } from "../api";
import { useNavigate } from "react-router-dom"; // Sử dụng useNavigate thay vì useHistory

const Register = () => {
    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [role, setRole] = useState("USER"); // Mặc định là USER
    const [error, setError] = useState(null);
    const navigate = useNavigate(); // Sử dụng useNavigate thay vì useHistory

    const handleRegister = async (e) => {
        e.preventDefault();

        try {
            const response = await api.post("/register", {
                username,
                password,
                role,
            });
            navigate("/login"); // Dùng navigate thay vì history.push để chuyển hướng
        } catch (err) {
            setError("Registration failed. Please try again.");
        }
    };

    return (
        <div>
            <h2>Register</h2>
            <form onSubmit={handleRegister}>
                <div>
                    <label>Email:</label>
                    <input
                        type="email"
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Password:</label>
                    <input
                        type="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        required
                    />
                </div>
                <div>
                    <label>Role:</label>
                    <select value={role} onChange={(e) => setRole(e.target.value)}>
                        <option value="USER">User</option>
                        <option value="ADMIN">Admin</option>
                    </select>
                </div>
                {error && <div>{error}</div>}
                <button type="submit">Register</button>
            </form>
        </div>
    );
};

export default Register;
