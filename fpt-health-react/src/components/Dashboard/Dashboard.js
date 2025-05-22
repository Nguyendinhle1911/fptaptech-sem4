import React, {useEffect, useState} from 'react';
import './Dashboard.css';
import bannerImg from '../img/pexels-pixabay-40568.jpg';
import axios from "axios";
import logo from "../img/fpt-health-high-resolution-logo-transparent-white.png";

function Dashboard() {
    const [patientData, setPatientData] = useState(null);
    const [imageValid, setImageValid] = useState(false);
    const [imagePath, setImagePath] = useState('');
    const [appointmentVisible, setAppointmentVisible] = useState(true);
    const patientId = sessionStorage.getItem('patient_id');
    const [currentAppointmentPage, setCurrentAppointmentPage] = useState(1);
    const [currentRecordPage, setCurrentRecordPage] = useState(1);
    const itemsPerPage = 3;
    const [openEditAppointment, setOpenEditAppointment] = useState(false);
    const [openEditPatient, setOpenEditPatient] = useState(false);
    const [editAppointmentData, setEditAppointmentData] = useState(null);
    const [bookedSlots, setBookedSlots] = useState([]);
    const [availableSlots, setAvailableSlots] = useState([]);

    const toggleAppointmentVisible = () => {
        setAppointmentVisible(!appointmentVisible);
        setCurrentRecordPage(1);
        setCurrentAppointmentPage(1);
    }

    const timeSlots = [
        {label: '08:00 AM - 09:00 AM', value: 1, start: '08:00', end: '09:00'},
        {label: '09:00 AM - 10:00 AM', value: 2, start: '09:00', end: '10:00'},
        {label: '10:00 AM - 11:00 AM', value: 3, start: '10:00', end: '11:00'},
        {label: '11:00 AM - 12:00 AM', value: 4, start: '11:00', end: '12:00'},
        {label: '01:00 PM - 02:00 PM', value: 5, start: '13:00', end: '14:00'},
        {label: '02:00 PM - 03:00 PM', value: 6, start: '14:00', end: '15:00'},
        {label: '03:00 PM - 04:00 PM', value: 7, start: '15:00', end: '16:00'},
        {label: '04:00 PM - 05:00 PM', value: 8, start: '16:00', end: '17:00'}
    ];

    const formatTimeSlot = (slot) => {
        switch (slot) {
            case 1:
                return '8:00 AM - 9:00 AM';
            case 2:
                return '9:00 AM - 10:00 AM';
            case 3:
                return '10:00 AM - 11:00 AM';
            case 4:
                return '11:00 AM - 12:00 AM';
            case 5:
                return '01:00 PM - 02:00 PM';
            case 6:
                return '02:00 PM - 03:00 PM';
            case 7:
                return '03:00 PM - 04:00 PM';
            case 8:
                return '04:00 PM - 05:00 PM';
            default:
                return 'Slot Time Not Defined';
        }
    };

    const [formData, setFormData] = useState({
        date: '',
        timeSlot: ''
    });

    const handleCancelEditAppointment = () => {
        setFormData({
            date: '',
            timeSlot: ''
        });
        setBookedSlots([]);
        setAvailableSlots([]);
        setEditAppointmentData(null);
        setOpenEditAppointment(false);
    };

    useEffect(() => {
        if (formData.date) {
            axios.get(`http://localhost:8081/api/v1/appointments/${editAppointmentData.doctor_id}/slots`)
                .then(response => {
                    setBookedSlots(response.data);
                })
                .catch(error => {
                    console.error('Error fetching booked slots!', error);
                });

            axios.get(`http://localhost:8081/api/v1/appointments/check-locked-slots?doctorId=${editAppointmentData.doctor_id}&date=${formData.date}`)
                .then(response => {
                    const lockedSlots = response.data;
                    const available = timeSlots.filter(slot => !lockedSlots.includes(slot.value));
                    setAvailableSlots(available);
                    // Reset formData.timeSlot if it's no longer available
                    if (!available.find(slot => slot.value === formData.timeSlot)) {
                        setFormData({
                            ...formData,
                            timeSlot: ''
                        });
                    }
                })
                .catch(error => {
                    console.error('Error fetching locked slots!', error);
                });
        }
    }, [formData.date]);


    useEffect(() => {
        if (formData.date && bookedSlots.length > 0) {
            const bookedSlotsForDate = bookedSlots.filter(slot => {
                const slotDate = new Date(slot.medical_day).toISOString().split('T')[0];
                return slotDate === formData.date;
            }).map(slot => slot.slot);
            const available = timeSlots.filter(slot => !bookedSlotsForDate.includes(slot.value));
            setAvailableSlots(available);
        } else {
            setAvailableSlots(timeSlots);
        }
    }, [formData.date, bookedSlots]);

    const handleDateChange = (date) => {
        setFormData({
            ...formData,
            date: date,
            timeSlot: ''
        });
    };

    const handleTimeSlotChange = (slot) => {
        // Check and lock the selected slot
        axios.post('http://localhost:8081/api/v1/appointments/lock-slot', {
            doctorId: editAppointmentData.doctor_id,
            date: formData.date,
            time: slot
        }).then(response => {
            setFormData({
                ...formData,
                timeSlot: slot
            });

            // Schedule to release lock after 5 minutes if not confirmed
            setTimeout(() => {
                axios.post('http://localhost:8081/api/v1/appointments/unlock-slot', {
                    doctorId: formData.doctor,
                    date: formData.date,
                    time: slot
                }).catch(error => {
                    console.error('Error unlocking slot!', error);
                });
            }, 300000);
        }).catch(error => {
            console.error('Error locking slot!', error);
        });
    };

    const renderDateButtons = () => {
        const dates = generateDateButtons();
        return (
            <div className="date-container">
                <label>Date</label>
                <div className="date-select">
                    <div className="date-buttons">
                        {dates.map(date => (
                            <button
                                key={date.value}
                                className={formData.date === date.value ? 'selected' : ''}
                                onClick={() => handleDateChange(date.value)}
                            >
                                {date.label}
                            </button>
                        ))}
                    </div>
                    <span>OR</span>
                    <input
                        type="date"
                        value={formData.date}
                        onChange={(e) => handleDateChange(e.target.value)}
                        min={new Date().toISOString().split('T')[0]}
                        className="ipSelectDate"
                    />
                </div>
            </div>
        );
    };

    const renderTimeSlots = () => {
        return (
            <div className="time-container">
                <label>Time</label>
                <div className="time-slots">
                    {availableSlots.map(slot => (
                        <button
                            key={slot.value}
                            className={formData.timeSlot === slot.value ? 'selected' : ''}
                            onClick={() => handleTimeSlotChange(slot.value)}
                            disabled={isTimeSlotPast(formData.date, slot.start)}
                            style={{
                                backgroundColor: isTimeSlotPast(formData.date, slot.start) ? '#d3d3d3' : '',
                                pointerEvents: isTimeSlotPast(formData.date, slot.start) ? 'none' : 'auto'
                            }}
                        >
                            {slot.label}
                        </button>
                    ))}
                </div>
            </div>
        );
    };

    useEffect(() => {
        if (openEditAppointment) {
            document.body.classList.add('no-scroll');
        } else {
            document.body.classList.remove('no-scroll');
        }
    }, [openEditAppointment]);

    const isTimeSlotPast = (date, startTime) => {
        const appointmentDate = new Date(date);
        const currentDate = new Date();
        const [startHour, startMinute] = startTime.split(':').map(Number);

        appointmentDate.setHours(startHour, startMinute, 0, 0);

        return appointmentDate < currentDate;
    };

    const generateDateButtons = () => {
        const today = new Date();
        const dates = [];
        for (let i = 0; i < 3; i++) {
            const date = new Date(today);
            date.setDate(today.getDate() + i);
            const dateString = date.toISOString().split('T')[0];
            dates.push({
                label: i === 0 ? `Today (${dateString})` : (i === 1 ? `Tomorrow (${dateString})` : `Day after tomorrow (${dateString})`),
                value: dateString
            });
        }
        return dates;
    };

    const handleConfirmEditAppointment = () => {
        if (editAppointmentData) {
            handleEditAppointment(editAppointmentData);
        }
        setOpenEditAppointment(false);
    };

    const handleEditAppointment = async (appointment) => {
        try {
            await axios.put(`http://localhost:8081/api/v1/appointments/update`, {
                appointment_id: appointment.appointment_id,
                medical_day: formData.date,
                slot: formData.timeSlot,
                status: 'Pending'
            });
            sessionStorage.setItem('appointmentMessage', 'Appointment updated successfully!');
            window.location.reload();
        } catch (error) {
            console.error("Failed to update the appointment.", error);
        }
    };

    useEffect(() => {
        const fetchPatientData = async () => {
            try {
                if (patientId) {
                    const response = await axios.get(`http://localhost:8081/api/v1/patients/search?patient_id=${patientId}`);
                    const patientData = response.data[0];
                    setPatientData(patientData);
                    if (patientData.patient_img) {
                        setImagePath(`http://localhost:8080/${patientData.patient_img}`);
                        setImageValid(true);
                    } else {
                        setImageValid(false);
                    }
                } else {
                    console.error('Invalid patientId:', patientId);
                }
            } catch (error) {
                console.error('Error fetching patient data:', error);
            }
        };

        fetchPatientData();
    }, [patientId]);

    const appointments = patientData ? patientData.appointmentsList : [];
    const medicalRecords = patientData ? patientData.medicalrecordsList : [];
    const sortedAppointments = appointments && appointments.slice().sort((a, b) => new Date(b.medical_day) - new Date(a.medical_day));
    const sortedMedicalRecords = medicalRecords && medicalRecords.slice().sort((a, b) => new Date(b.follow_up_date) - new Date(a.follow_up_date));

    const totalAppointmentPages = Math.ceil(appointments.length / itemsPerPage);
    const startAppointmentIndex = (currentAppointmentPage - 1) * itemsPerPage;
    const currentAppointments = sortedAppointments.slice(startAppointmentIndex, startAppointmentIndex + itemsPerPage);

    const totalRecordPages = Math.ceil(medicalRecords.length / itemsPerPage);
    const startRecordIndex = (currentRecordPage - 1) * itemsPerPage;
    const currentRecords = sortedMedicalRecords.slice(startRecordIndex, startRecordIndex + itemsPerPage);


    const handleImageChange = async (e) => {
        const file = e.target.files[0];
        const formData = new FormData();
        formData.append('image', file);
        formData.append('patient_id', patientId);

        try {
            const response = await axios.post('http://localhost:8081/api/v1/patients/upload-image', formData, {
                headers: {
                    'Content-Type': 'multipart/form-data'
                }
            });
            setImagePath(`http://localhost:8081/${response.data.filePath}`);
            const updatedPatient = {...patientData, patient_img: response.data.filePath};
            setPatientData(updatedPatient);
            setImageValid(true);
        } catch (error) {
            console.error('Error uploading image:', error);
        }
    };

    const getStatusIcon = (status) => {
        switch (status) {
            case 'Pending':
                return <span className="status-icon pending"></span>;
            case 'Completed':
                return <span className="status-icon completed"></span>;
            case 'Cancelled':
                return <span className="status-icon cancelled"></span>;
            default:
                return <span className="status-icon"></span>;
        }
    };

    const [doctors, setDoctors] = useState([]);
    const [departments, setDepartments] = useState([]);
    useEffect(() => {
        const fetchDetails = async () => {
            try {
                const [patientResponse, doctorsResponse, departmentsResponse] = await Promise.all([
                    axios.get('http://localhost:8081/api/v1/patients/list'),
                    axios.get('http://localhost:8081/api/v1/doctors/list'),
                    axios.get('http://localhost:8081/api/v1/departments/list')
                ]);

                setDoctors(doctorsResponse.data);
                setDepartments(departmentsResponse.data);
            } catch (error) {
                console.error('Error fetching details', error);
            }
        };

        fetchDetails();
    }, []);

    const getDepartmentName = (doctorId) => {
        const doctor = doctors.find(doc => doc.doctor_id === doctorId);
        if (doctor) {
            const department = departments.find(dep => dep.department_id === doctor.department_id);
            return department ? department.department_name : 'Unknown Department';
        }
        return 'Unknown Department';
    };

    const handleOpenEditAppointment = (appointment) => {
        setEditAppointmentData(appointment);
        setOpenEditAppointment(true);
    };

    const isEditCancelDisabled = (appointmentDate, appointmentSlot) => {
        const startTime = appointmentSlot.split(' - ')[0];
        const appointmentDateTime = new Date(`${appointmentDate}T${convertTo24Hour(startTime)}`);
        const currentTime = new Date();
        const timeDifference = appointmentDateTime - currentTime;
        const twoHoursInMillis = 2 * 60 * 60 * 1000;
        return timeDifference < twoHoursInMillis;
    };

    const convertTo24Hour = (time) => {
        const [timePart, modifier] = time.split(' ');
        let [hours, minutes] = timePart.split(':');
        if (modifier === 'PM' && hours !== '12') {
            hours = parseInt(hours, 10) + 12;
        }
        if (modifier === 'AM' && hours === '12') {
            hours = '00';
        }
        hours = hours < 10 ? `0${hours}` : hours;

        return `${hours}:${minutes}:00`;
    };

    const handleChange = (e) => {
        const { name, value } = e.target;
        setPatientData((prevData) => ({
            ...prevData,
            [name]: value,
        }));
    };

    const handleSavePatient = async () => {
        try {
            await axios.put('http://localhost:8081/api/v1/patients/update', {
                ...patientData
            });
            setOpenEditPatient(false);
        } catch (error) {
            console.error('Error updating patient data:', error);
        }
    };

    return (
        <main className="dashboard-container">
            {openEditAppointment && (
                <div className="appointments-popup-container">
                    <div className="appointments-popup-overlay"></div>
                    <div className="appointments-popup">
                        {renderDateButtons()}
                        {formData.date && renderTimeSlots()}
                        <div className="edit-appointment-action">
                            <button className="appointments-cancel" onClick={handleCancelEditAppointment}>Cancel
                            </button>
                            <button className="appointments-save" onClick={handleConfirmEditAppointment}
                                    disabled={!formData.date || !formData.timeSlot}>Save
                            </button>
                        </div>
                    </div>
                </div>
            )}
            <section className="dashboard-banner">
                <img className="dashboard-banner-img" src={bannerImg} alt="dashboard-banner-img"/>
                <h4>Patient Dashboard</h4>
                <div className="dashboard-overlay"></div>
            </section>
            {patientData ? (
                <section className="dashboard-content">
                    <div className="patient-container">
                        {imageValid ? (
                            <img id="patientImg" src={imagePath} alt="Patient"/>
                        ) : (
                            <img id="patientImg" width="150" height="150"
                                 src="https://img.icons8.com/ios-filled/200/004b91/user-male-circle.png"
                                 alt="user-male-circle"/>
                        )}
                        <div className="img-overlay" onClick={() => document.getElementById('ipPatientImg').click()}>
                            <img
                                width="30" height="30"
                                src="https://img.icons8.com/ios-filled/200/FFFFFF/available-updates.png"
                                alt="available-updates"/>Change Image
                        </div>
                        <input id="ipPatientImg" type="file" accept="image/*" onChange={handleImageChange}
                               style={{display: 'none'}}/>
                        <div className="container-title">Personal Information
                            {!openEditPatient ? (
                                <div className="container-title-action"><a onClick={() => setOpenEditPatient(true)}>Edit
                                    Information</a>/<a>Change Password</a></div>
                            ) : (
                                <div className="container-title-action"><a onClick={handleSavePatient}>Save</a>/<a onClick={() => setOpenEditPatient(false)}>Cancel</a></div>
                            )}
                        </div>
                        <div className="patient-info">
                            {openEditPatient ? (
                                <>
                                    <div className="patient-info-item">
                                        <h4>Full Name</h4>
                                        <input
                                            type="text"
                                            name="patient_name"
                                            value={patientData.patient_name ?? ''}
                                            onChange={handleChange}
                                            required
                                        />
                                    </div>
                                    <div className="patient-info-item">
                                        <h4>Date of Birth</h4>
                                        <input
                                            type="date"
                                            name="patient_dob"
                                            value={patientData.patient_dob ?? ''}
                                            onChange={handleChange}
                                            required
                                        />
                                    </div>
                                    <div className="patient-info-item">
                                        <h4>Gender</h4>
                                        <select
                                            name="patient_gender"
                                            value={patientData.patient_gender ?? ''}
                                            onChange={handleChange}
                                            required
                                        >
                                            <option value="Male">Male</option>
                                            <option value="Female">Female</option>
                                            <option value="Other">Other</option>
                                        </select>
                                    </div>
                                    <div className="patient-info-item">
                                        <h4>Email Address</h4>
                                        <input
                                            type="email"
                                            name="patient_email"
                                            value={patientData.patient_email ?? ''}
                                            onChange={handleChange}
                                            required
                                        />
                                    </div>
                                    <div className="patient-info-item">
                                        <h4>Phone Number</h4>
                                        <input
                                            type="number"
                                            name="patient_phone"
                                            value={patientData.patient_phone ?? ''}
                                            onChange={handleChange}
                                            required
                                        />
                                    </div>
                                    <div className="patient-info-item">
                                        <h4>Address</h4>
                                        <input
                                            type="text"
                                            name="patient_address"
                                            value={patientData.patient_address ?? ''}
                                            onChange={handleChange}
                                            required
                                        />
                                    </div>
                                </>
                            ) : (
                                <>
                                    <div className="patient-info-item">
                                        <h4>Full Name</h4>
                                        <p>{patientData.patient_name ?? 'N/A'}</p>
                                    </div>
                                    <div className="patient-info-item">
                                        <h4>Date of Birth</h4>
                                        <p>{patientData.patient_dob ?? 'N/A'}</p>
                                    </div>
                                    <div className="patient-info-item">
                                        <h4>Gender</h4>
                                        <p>{patientData.patient_gender ?? 'N/A'}</p>
                                    </div>
                                    <div className="patient-info-item">
                                        <h4>Email Address</h4>
                                        <p>{patientData.patient_email ?? 'N/A'}</p>
                                    </div>
                                    <div className="patient-info-item">
                                        <h4>Phone Number</h4>
                                        <p>{patientData.patient_phone ?? 'N/A'}</p>
                                    </div>
                                    <div className="patient-info-item">
                                        <h4>Address</h4>
                                        <p>{patientData.patient_address ?? 'N/A'}</p>
                                    </div>
                                </>
                            )}
                        </div>
                        <div className="container-title">Appointments & Medical Records</div>
                        <div className="medical-info">
                            <ul>
                                <li className={`${appointmentVisible ? 'active' : ''}`}
                                    onClick={toggleAppointmentVisible}>Appointments
                                </li>
                                <li className={`${appointmentVisible ? '' : 'active'}`}
                                    onClick={toggleAppointmentVisible}>Medical Records
                                </li>
                            </ul>
                            {appointmentVisible ? (
                                <div className="appointment-container">
                                    {currentAppointments.length > 0 ? (
                                        currentAppointments.map(app => (
                                            <div className="appointment-item" key={app.appointment_id}>
                                                <div className="appointment-item-header">
                                                    <h3>{new Date(app.appointment_date).toLocaleDateString()}</h3>
                                                    <span className="appointment-status">
                                                    {getStatusIcon(app.status)}
                                                        <p>{app.status}</p>
                                                </span>
                                                </div>
                                                <p>
                                                    <strong>Doctor:</strong> {app.doctor && app.doctor.length > 0 ? app.doctor[0].doctor_name : 'N/A'}
                                                </p>
                                                <p>
                                                    <strong>Department:</strong> {app.doctor && app.doctor.length > 0 && app.doctor[0].department && app.doctor[0].department.length > 0 ? app.doctor[0].department[0].department_name : 'N/A'}
                                                </p>
                                                <p>
                                                    <strong>Staff
                                                        Name:</strong> {app.staff && app.staff.length > 0 ? app.staff[0].staff_name : 'N/A'}
                                                </p>
                                                <p>
                                                    <strong>Appointment
                                                        Date:</strong> {new Date(app.medical_day).toLocaleDateString()}
                                                </p>
                                                <p>
                                                    <strong>Appointment Time:</strong> {formatTimeSlot(app.slot)}
                                                </p>
                                                {app.status !== 'Cancelled' && app.status !== 'Completed' && (
                                                    <>
                                                        <div className="appointment-action">
                                                            <button className="edit-appointment-button"
                                                                    onClick={() => handleOpenEditAppointment(app)}
                                                                    disabled={isEditCancelDisabled(app.medical_day, formatTimeSlot(app.slot))}>Edit
                                                            </button>
                                                            <button className="cancel-appointment-button"
                                                                    disabled={isEditCancelDisabled(app.medical_day, formatTimeSlot(app.slot))}>Cancel
                                                            </button>
                                                        </div>
                                                    </>
                                                )}
                                            </div>
                                        ))
                                    ) : (
                                        <p>No appointments available.</p>
                                    )}
                                    <div className="pagination-controls">
                                        <a
                                            onClick={() => setCurrentAppointmentPage(1)}
                                        >
                                            <img width="18" height="18"
                                                 src="https://img.icons8.com/ios-filled/200/004b91/first-1.png"
                                                 alt="first-1"/>
                                        </a>
                                        <a
                                            onClick={() => setCurrentAppointmentPage(prev => Math.max(prev - 1, 1))}
                                        >
                                            <img width="18" height="18"
                                                 src="https://img.icons8.com/ios-filled/200/004b91/back.png"
                                                 alt="back"/>
                                        </a>
                                        <span>Page {currentAppointmentPage} of {totalAppointmentPages}</span>
                                        <a
                                            onClick={() => setCurrentAppointmentPage(prev => Math.min(prev + 1, totalAppointmentPages))}
                                        >
                                            <img width="18" height="18"
                                                 src="https://img.icons8.com/ios-filled/200/004b91/forward.png"
                                                 alt="forward"/>
                                        </a>
                                        <a
                                            onClick={() => setCurrentAppointmentPage(totalAppointmentPages)}
                                        >
                                            <img width="18" height="18"
                                                 src="https://img.icons8.com/ios-filled/200/004b91/last-1.png"
                                                 alt="last-1"/>
                                        </a>
                                    </div>
                                </div>
                            ) : (
                                <div className="record-container">
                                    {currentRecords.length > 0 ? (
                                        currentRecords.map(record => (
                                            <div key={record.record_id} className="record-item">
                                                <div className="record-item-header">
                                                    <h3>{record.follow_up_date}</h3>
                                                </div>
                                                <p>
                                                    <strong>Department:</strong> {record.doctors && record.doctors.length > 0 ? getDepartmentName(record.doctors[0].department_id) : 'No Department Info'}
                                                </p>
                                                <p>
                                                    <strong>Doctor:</strong> {record.doctors && record.doctors.length > 0 ? record.doctors[0].doctor_name : 'No Doctor Info'}
                                                </p>
                                                <p><strong>Symptoms:</strong> {record.symptoms}</p>
                                                <p><strong>Diagnosis:</strong> {record.diagnosis}</p>
                                                <div className="record-action">
                                                    <button className="record-detail-button">View Details</button>
                                                </div>
                                            </div>
                                        ))
                                    ) : (
                                        <p>No records available.</p>
                                    )}
                                    <div className="pagination-controls">
                                        <a
                                            onClick={() => setCurrentRecordPage(1)}
                                        >
                                            <img width="18" height="18"
                                                 src="https://img.icons8.com/ios-filled/200/004b91/first-1.png"
                                                 alt="first-1"/>
                                        </a>
                                        <a
                                            onClick={() => setCurrentRecordPage(prev => Math.max(prev - 1, 1))}
                                        >
                                            <img width="18" height="18"
                                                 src="https://img.icons8.com/ios-filled/200/004b91/back.png"
                                                 alt="back"/>
                                        </a>
                                        <span>Page {currentRecordPage} of {totalRecordPages}</span>
                                        <a
                                            onClick={() => setCurrentRecordPage(prev => Math.min(prev + 1, totalRecordPages))}
                                        >
                                            <img width="18" height="18"
                                                 src="https://img.icons8.com/ios-filled/200/004b91/forward.png"
                                                 alt="forward"/>
                                        </a>
                                        <a
                                            onClick={() => setCurrentRecordPage(totalRecordPages)}
                                        >
                                            <img width="18" height="18"
                                                 src="https://img.icons8.com/ios-filled/200/004b91/last-1.png"
                                                 alt="last-1"/>
                                        </a>
                                    </div>
                                </div>
                            )}

                        </div>
                    </div>
                </section>
            ) : (
                <h1 className="dashboard-alert">To see the information, please sign in and refresh this page.</h1>
            )}
            <footer>
                <div className="footer-container-top">
                    <div className="footer-logo">
                        <img src={logo} alt="fpt-health" style={{width: 140 + 'px', height: 40 + 'px'}}/>
                    </div>
                    <div className="footer-social">
                        <div className="fb-icon">
                            <img width="30" height="30"
                                 src="https://img.icons8.com/ios-filled/50/FFFFFF/facebook--v1.png"
                                 alt="facebook--v1"/>
                        </div>
                        <div className="zl-icon">
                            <img width="30" height="30" src="https://img.icons8.com/ios-filled/50/FFFFFF/zalo.png"
                                 alt="zalo"/>
                        </div>
                        <div className="ms-icon">
                            <img width="30" height="30"
                                 src="https://img.icons8.com/ios-filled/50/FFFFFF/facebook-messenger.png"
                                 alt="facebook-messenger"/>
                        </div>
                    </div>
                </div>
                <div className="footer-container-middle">
                    <div className="footer-content">
                        <h4>FPT Health</h4>
                        <p>FPT Health Hospital is committed to providing you and your family with the highest quality
                            medical services, featuring a team of professional doctors and state-of-the-art facilities.
                            Your health is our responsibility.</p>
                    </div>
                    <div className="footer-hours-content">
                        <h4>Opening Hours</h4>
                        <div className="footer-hours">
                            <div className="footer-content-item"><span>Monday - Friday:</span>
                                <span>7:00 AM - 8:00 PM</span></div>
                            <div className="footer-content-item"><span>Saturday:</span> <span>7:00 AM - 6:00 PM</span>
                            </div>
                            <div className="footer-content-item"><span>Sunday:</span> <span>7:30 AM - 6:00 PM</span>
                            </div>
                        </div>
                    </div>
                    <div className="footer-content">
                        <h4>Contact</h4>
                        <div className="footer-contact">
                            <div className="footer-contact-item">
                                <div>
                                    <img width="20" height="20"
                                         src="https://img.icons8.com/ios-filled/50/FFFFFF/marker.png" alt="marker"/>
                                </div>
                                <p>8 Ton That Thuyet, My Dinh Ward, Nam Tu Liem District, Ha Noi</p>
                            </div>
                            <div className="footer-contact-item">
                                <div>
                                    <img width="20" height="20"
                                         src="https://img.icons8.com/ios-filled/50/FFFFFF/phone.png" alt="phone"/>
                                </div>
                                <p>+84 987 654 321</p>
                            </div>
                            <div className="footer-contact-item">
                                <div>
                                    <img width="20" height="20"
                                         src="https://img.icons8.com/ios-filled/50/FFFFFF/new-post.png" alt="new-post"/>
                                </div>
                                <p>fpthealth@gmail.com</p>
                            </div>
                        </div>
                    </div>
                </div>
                <div className="footer-container-bottom">
                    <div>© 2024 FPT Health. All rights reserved.</div>
                    <div><a>Terms of use</a> | <a>Privacy Policy</a></div>
                </div>
            </footer>
        </main>
    );
}

export default Dashboard;