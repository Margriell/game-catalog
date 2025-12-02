import { BrowserRouter, Routes, Route, Link } from 'react-router-dom';
import Home from './pages/Home';
import Login from './pages/Login';
import GameDetails from './pages/GameDetails';
import './App.css';

function App() {
    const handleLogout = () => {
        localStorage.removeItem('token');
        window.location.reload();
    };

    return (
        <BrowserRouter>
            <nav className="navbar">
                <div className="logo">GameCatalog</div>
                <div className="links">
                    <Link to="/">Lista gier</Link>
                    {/* jak jest token to pokaż Wyloguj, jak nie to Zaloguj */}
                    {localStorage.getItem('token') ? (
                        <button onClick={handleLogout}>Wyloguj</button>
                    ) : (
                        <Link to="/login">Zaloguj się</Link>
                    )}
                </div>
            </nav>

            <Routes>
                <Route path="/" element={<Home />} />
                <Route path="/login" element={<Login />} />
                <Route path="/game/:id" element={<GameDetails />} />
            </Routes>
        </BrowserRouter>
    );
}

export default App;