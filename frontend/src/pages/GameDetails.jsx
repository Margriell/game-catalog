import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import api from '../api';

function GameDetails() {
    const { id } = useParams();
    const [game, setGame] = useState(null);
    const [reviews, setReviews] = useState([]);
    const [isFavorite, setIsFavorite] = useState(false);

    useEffect(() => {
        api.get(`/games/${id}`).then(res => setGame(res.data));

        api.get(`/games/${id}/reviews`).then(res => setReviews(res.data));

        const token = localStorage.getItem('token');
        if (token) {
            api.get('/users/me/favorites')
                .then(res => {
                    const found = res.data.some(fav => fav.gameId == id);
                    setIsFavorite(found);
                })
                .catch(err => console.error("Błąd sprawdzania ulubionych", err));
        }
    }, [id]);

    const toggleFavorite = () => {
        api.post(`/game/${id}/favourite`)
            .then(res => {
                setIsFavorite(!isFavorite);
            })
            .catch((err) => {
                if (err.response && err.response.status === 403) {
                    alert("Musisz być zalogowany!");
                } else {
                    alert("Wystąpił błąd.");
                }
            });
    };

    if (!game) return <div className="container">Ładowanie...</div>;

    return (
        <div className="container">
            <div className="details-header" style={{ display: 'flex', gap: '40px', alignItems: 'flex-start' }}>
                <img
                    src={game.headerImage || 'https://placehold.co/600x400/222/2563eb?text=No+Image'}
                    alt={game.name}
                    style={{ maxWidth: '500px', borderRadius: '10px', boxShadow: '0 5px 15px rgba(0,0,0,0.5)' }}
                />

                <div>
                    <h1 style={{ fontSize: '3rem', marginBottom: '10px' }}>{game.name}</h1>
                    <p style={{ fontSize: '1.2rem', color: '#ccc' }}>{game.shortDescription}</p>

                    <div style={{ margin: '20px 0', lineHeight: '1.8' }}>
                        <p><strong>Gatunek:</strong> {game.genre}</p>
                        <p><strong>Wydawca:</strong> {game.publisher}</p>
                        <p><strong>Platforma:</strong> {game.platform}</p>
                        <p><strong>Cena:</strong> <span style={{ color: 'var(--accent-color)', fontWeight: 'bold' }}>
                            {game.price > 0 ? `${game.price} ${game.currency}` : 'Free to Play'}
                        </span></p>
                    </div>

                    <button
                        onClick={toggleFavorite}
                        className="primary-btn"
                        style={{
                            backgroundColor: isFavorite ? '#ef4444' : 'var(--accent-color)', // Czerwony jeśli ulubione, niebieski jeśli nie
                            width: 'auto',
                            padding: '12px 30px'
                        }}
                    >
                        {isFavorite ? "💔 Usuń z ulubionych" : "❤ Dodaj do ulubionych"}
                    </button>
                </div>
            </div>

            <div className="reviews-section" style={{ marginTop: '60px' }}>
                <h3 style={{ borderBottom: '1px solid #333', paddingBottom: '15px' }}>Recenzje użytkowników</h3>
                {reviews.length === 0 ? <p style={{ color: '#888', fontStyle: 'italic' }}>Brak recenzji.</p> : (
                    <ul style={{ listStyle: 'none', padding: 0 }}>
                        {reviews.map(review => (
                            <li key={review.id} style={{
                                backgroundColor: '#1c1c1c',
                                padding: '20px',
                                marginBottom: '15px',
                                borderRadius: '8px'
                            }}>
                                <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '10px' }}>
                                    <strong style={{ color: 'var(--accent-color)' }}>{review.userName}</strong>
                                    <span style={{ color: '#ffd700' }}>★ {review.rating}/5</span>
                                </div>
                                <p>{review.reviewText}</p>
                            </li>
                        ))}
                    </ul>
                )}
            </div>
        </div>
    );
}

export default GameDetails;