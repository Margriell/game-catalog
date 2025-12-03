import { useState, useEffect } from 'react';
import api from '../api';

function Profile() {
    const [user, setUser] = useState(null);
    const [details, setDetails] = useState({ firstName: '', lastName: '' });
    const [passwords, setPasswords] = useState({ oldPassword: '', newPassword: '' });

    useEffect(() => {
        api.get('/users/me')
            .then(res => {
                setUser(res.data);
                setDetails({
                    firstName: res.data.firstName || '',
                    lastName: res.data.lastName || ''
                });
            })
            .catch(err => console.error(err));
    }, []);

    const handleUpdateProfile = (e) => {
        e.preventDefault();
        api.patch(`/users/me?firstName=${details.firstName}&lastName=${details.lastName}`)
            .then(() => {
                alert("Dane zaktualizowane");
                setUser(prev => ({ ...prev, firstName: details.firstName, lastName: details.lastName }));
            })
            .catch(err => alert("Błąd aktualizacji danych"));
    };

    const handleChangePassword = (e) => {
        e.preventDefault();
        api.post('/users/me/password', passwords)
            .then(() => {
                alert("Hasło zmienione");
                setPasswords({ oldPassword: '', newPassword: '' });
            })
            .catch(err => alert("Błąd zmiany hasła. Sprawdź stare hasło."));
    };

    if (!user) return <div className="container">Ładowanie...</div>;

    return (
        <div className="auth-container" style={{ maxWidth: '600px' }}>
            <h1 style={{ marginBottom: '30px' }}>Mój Profil</h1>

            <div style={{ textAlign: 'left', marginBottom: '40px', padding: '20px', backgroundColor: '#1c1c1c', borderRadius: '8px' }}>
                <p><strong>Email:</strong> {user.email}</p>
                <p><strong>Imię i nazwisko:</strong> {user.firstName} {user.lastName}</p>
                <p><strong>Rola:</strong> {user.role}</p>
            </div>

            <div style={{ marginBottom: '40px' }}>
                <h3>Edytuj dane osobowe</h3>
                <form onSubmit={handleUpdateProfile}>
                    <label>Imię</label>
                    <input
                        type="text"
                        value={details.firstName}
                        onChange={e => setDetails({ ...details, firstName: e.target.value })}
                        required
                    />
                    <label>Nazwisko</label>
                    <input
                        type="text"
                        value={details.lastName}
                        onChange={e => setDetails({ ...details, lastName: e.target.value })}
                        required
                    />
                    <button type="submit" className="primary-btn">Zapisz zmiany</button>
                </form>
            </div>

            <div>
                <h3>Zmiana hasła</h3>
                <form onSubmit={handleChangePassword}>
                    <label>Stare hasło</label>
                    <input
                        type="password"
                        value={passwords.oldPassword}
                        onChange={e => setPasswords({ ...passwords, oldPassword: e.target.value })}
                        required
                    />
                    <label>Nowe hasło (min. 6 znaków)</label>
                    <input
                        type="password"
                        value={passwords.newPassword}
                        onChange={e => setPasswords({ ...passwords, newPassword: e.target.value })}
                        required
                    />
                    <button type="submit" className="primary-btn" style={{ backgroundColor: '#444' }}>Zmień hasło</button>
                </form>
            </div>
        </div>
    );
}

export default Profile;