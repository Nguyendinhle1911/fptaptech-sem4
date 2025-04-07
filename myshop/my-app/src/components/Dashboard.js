import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from '../axios';

const Dashboard = () => {
    const [userInfo, setUserInfo] = useState(null);
    const navigate = useNavigate(); // Sử dụng useNavigate thay vì useHistory

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            // Lấy thông tin người dùng từ API
            axios.get('/api/user/me', {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            })
                .then(response => {
                    setUserInfo(response.data);
                })
                .catch(error => {
                    console.error('There was an error fetching the user info!', error);
                });
        }
    }, []);

    const handleLogout = () => {
        localStorage.removeItem('token');
        navigate('/login'); // Dùng navigate thay vì history.push
    };

    if (!userInfo) {
        return <p>Loading...</p>;
    }

    return (
        <div>
            <h2>Welcome, {userInfo.username}</h2>
            <p>Email: {userInfo.email}</p>
            <p>Role: {userInfo.role}</p>
            <button onClick={handleLogout}>Logout</button>
        </div>
    );
};

export default Dashboard;
