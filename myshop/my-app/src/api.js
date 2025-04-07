// src/api.js
import axios from "axios";

const API_URL = "http://localhost:8081"; // Cổng backend của bạn

export const api = axios.create({
    baseURL: API_URL,
    headers: {
        "Content-Type": "application/json",
    },
});
