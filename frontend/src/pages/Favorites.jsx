import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../api';

function Favorites() {
    const [favorites, setFavorites] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchFavorites();
    }, []);

    const fetchFavorites = () => {
        api.get('/users/me/favorites')
            .then(response => {
                setFavorites(response.data);
                setLoading(false);
            })
            .catch(error => {
                console.error("Błąd pobierania ulubionych:", error);
                setLoading(false);
            });
    };

    const downloadFile = (format) => {
        api.get(`/users/me/favorites/export?format=${format}`, { responseType: 'blob' })
            .then((response) => {
                const url = window.URL.createObjectURL(new Blob([response.data]));
                const link = document.createElement('a');
                link.href = url;
                // Ustawiamy nazwę pliku
                link.setAttribute('download', `moje_gry.${format}`);
                document.body.appendChild(link);
                link.click();
                link.remove();
            })
            .catch(error => alert("Błąd pobierania pliku!"));
    };

    if (loading) return <div className="container">Ładowanie ulubionych...</div>;

    return (
        <div className="container">
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '40px' }}>
                <h1>Twoje ulubione gry</h1>

                {favorites.length > 0 && (
                    <div style={{ display: 'flex', gap: '10px' }}>
                        <button onClick={() => downloadFile('txt')} className="nav-btn" style={{border: '1px solid #555'}}>Pobierz TXT</button>
                        <button onClick={() => downloadFile('json')} className="nav-btn" style={{border: '1px solid #555'}}>Pobierz JSON</button>
                        <button onClick={() => downloadFile('xml')} className="nav-btn" style={{border: '1px solid #555'}}>Pobierz XML</button>
                    </div>
                )}
            </div>

            {favorites.length === 0 ? (
                <div style={{ textAlign: 'center', padding: '50px', backgroundColor: '#1c1c1c', borderRadius: '15px' }}>
                    <h3>Nie masz jeszcze ulubionych gier.</h3>
                    <p style={{ color: '#aaa', marginBottom: '20px' }}>Przeglądaj katalog i kliknij "Dodaj do ulubionych" na stronie gry.</p>
                    <Link to="/">
                        <button className="primary-btn" style={{ maxWidth: '200px' }}>Wróć do katalogu</button>
                    </Link>
                </div>
            ) : (
                <div className="game-grid">
                    {favorites.map(fav => (
                        <div key={fav.gameId} className="game-card">
                            <img
                                src={fav.headerImage}
                                alt={fav.gameName}
                                onError={(e) => { e.target.src = 'https://placehold.co/600x400/222/2563eb?text=No+Image'; }}
                            />
                            <div className="card-content">
                                <h3>{fav.gameName}</h3>
                                <Link to={`/game/${fav.gameId}`}>
                                    <button className="primary-btn">Zobacz szczegóły</button>
                                </Link>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}

export default Favorites;