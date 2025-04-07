import axios from 'axios';

// Cấu hình base URL cho API Gateway
const instance = axios.create({
    baseURL: 'http://localhost:8080', // Đảm bảo API Gateway chạy trên cổng này
    headers: {
        'Content-Type': 'application/json',
    },
});

export default instance;
