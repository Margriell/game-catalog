import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api';

function Register() {
    const [formData, setFormData] = useState({
        firstName: '',
        lastName: '',
        email: '',
        password: ''
    });
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleRegister = async (e) => {
        e.preventDefault();
        try {
            await api.post('/auth/register', formData);
            alert('Rejestracja udana! Możesz się zalogować.');
            navigate('/login');
        } catch (error) {
            console.error(error);
            alert('Błąd rejestracji. Sprawdź czy email nie jest zajęty.');
        }
    };

    return (
        <div className="auth-container">
            <h2 style={{textAlign: 'center', marginBottom: '30px'}}>Dołącz do nas</h2>
            <form onSubmit={handleRegister}>
                <label>Imię</label>
                <input name="firstName" onChange={handleChange} required />

                <label>Nazwisko</label>
                <input name="lastName" onChange={handleChange} required />

                <label>Email</label>
                <input name="email" type="email" onChange={handleChange} required />

                <label>Hasło (min. 6 znaków)</label>
                <input name="password" type="password" onChange={handleChange} required />

                <button type="submit" className="primary-btn">Zarejestruj się</button>
            </form>
        </div>
    );
}

export default Register;