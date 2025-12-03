import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import api from '../api';

function Login() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await api.post('/auth/login', { email, password });
            localStorage.setItem('token', response.data.token);
            localStorage.setItem('userRole', response.data.role);
            localStorage.setItem('userEmail', response.data.email);
            window.location.href = '/';
        } catch (error) {
            alert('Błąd logowania. Sprawdź email i hasło.');
        }
    };

    return (
        <div className="auth-container">
            <h2 style={{ textAlign: 'center', marginBottom: '30px' }}>Witaj ponownie</h2>

            <form onSubmit={handleLogin}>
                <label>Email</label>
                <input
                    type="email"
                    placeholder="Wpisz swój email"
                    value={email}
                    onChange={e => setEmail(e.target.value)}
                    required
                />

                <label>Hasło</label>
                <input
                    type="password"
                    placeholder="Wpisz hasło"
                    value={password}
                    onChange={e => setPassword(e.target.value)}
                    required
                />

                <button type="submit" className="primary-btn">Zaloguj się</button>
            </form>

            <p style={{ marginTop: '20px', fontSize: '0.9rem', color: '#aaa' }}>
                Nie masz konta?{' '}
                <Link to="/register" style={{ color: 'var(--accent-color)', textDecoration: 'none', fontWeight: 'bold' }}>
                    Zarejestruj się
                </Link>
            </p>
        </div>
    );
}

export default Login;