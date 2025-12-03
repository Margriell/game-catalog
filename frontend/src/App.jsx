import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import GameDetails from './pages/GameDetails';
import Favorites from './pages/Favorites';
import Profile from './pages/Profile';
import './App.css';

function App() {
    const handleLogout = () => {
        localStorage.removeItem('token');
        localStorage.removeItem('userRole');
        localStorage.removeItem('userEmail');
        window.location.href = '/';
    };

    const isLoggedIn = !!localStorage.getItem('token');

    return (
        <BrowserRouter>
            <nav className="navbar">
                <div className="logo">
                    <Link to="/" style={{ color: 'white', textDecoration: 'none', marginLeft: 0 }}>GameCatalog</Link>
                </div>
                <div className="links">
                    <Link to="/">Wszystkie Gry</Link>

                    {isLoggedIn ? (
                        <>
                            <Link to="/favorites" style={{ color: 'var(--accent-color)' }}>❤ Ulubione</Link>
                            <Link to="/profile">Mój Profil</Link>
                            <button onClick={handleLogout} className="nav-btn">Wyloguj</button>
                        </>
                    ) : (
                        <>
                            <Link to="/login">Logowanie</Link>
                            <Link to="/register" style={{color: 'var(--accent-color)'}}>Rejestracja</Link>
                        </>
                    )}
                </div>
            </nav>

            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/login" element={<Login />} />
                <Route path="/register" element={<Register />} />
                <Route path="/favorites" element={<Favorites />} />
                <Route path="/game/:id" element={<GameDetails />} />
                <Route path="/profile" element={<Profile />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;