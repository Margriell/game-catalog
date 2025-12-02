import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import api from '../api';

function Login() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();

    const handleLogin = async (e) => {
        e.preventDefault();
        try {
            const response = await api.post('/auth/login', { email, password });
            // Zapisujemy token w przeglądarce
            localStorage.setItem('token', response.data.token);
            localStorage.setItem('userRole', response.data.role);
            alert('Zalogowano pomyślnie!');
            navigate('/');
        } catch (error) {
            alert('Błąd logowania. Sprawdź dane.');
        }
    };

    return (
        <div className="login-container">
            <h2>Logowanie</h2>
            <form onSubmit={handleLogin}>
                <input
                    type="email"
                    placeholder="Email"
                    value={email}
                    onChange={e => setEmail(e.target.value)}
                    required
                />
                <input
                    type="password"
                    placeholder="Hasło"
                    value={password}
                    onChange={e => setPassword(e.target.value)}
                    required
                />
                <button type="submit">Zaloguj się</button>
            </form>
        </div>
    );
}

export default Login;