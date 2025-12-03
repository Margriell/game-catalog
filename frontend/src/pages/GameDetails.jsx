import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import api from '../api';

function GameDetails() {
    const { id } = useParams();
    const [game, setGame] = useState(null);
    const [reviews, setReviews] = useState([]);
    const [isFavorite, setIsFavorite] = useState(false);

    const [rating, setRating] = useState(5);
    const [reviewText, setReviewText] = useState("");

    const [hasReviewed, setHasReviewed] = useState(
        !!localStorage.getItem(`reviewed_game_${id}`)
    );

    const isLoggedIn = !!localStorage.getItem('token');
    const userRole = localStorage.getItem('userRole');

    const fetchReviews = () => {
        api.get(`/games/${id}/reviews`).then(res => setReviews(res.data));
    };

    useEffect(() => {
        api.get(`/games/${id}`).then(res => setGame(res.data));
        fetchReviews();

        if (isLoggedIn) {
            api.get('/users/me/favorites')
                .then(res => {
                    const found = res.data.some(fav => fav.gameId == id);
                    setIsFavorite(found);
                })
                .catch(() => {});
        }
    }, [id]);

    const toggleFavorite = () => {
        api.post(`/games/${id}/favourite`)
            .then(() => setIsFavorite(!isFavorite))
            .catch((err) => {
                if (err.response && err.response.status === 403) alert("Musisz być zalogowany!");
                else alert("Wystąpił błąd.");
            });
    };

    const handleAddReview = async (e) => {
        e.preventDefault();
        try {
            await api.post(`/games/${id}/reviews`, { rating, reviewText });
            alert("Recenzja została dodana!");
            setReviewText("");
            fetchReviews();
            setHasReviewed(true);
            localStorage.setItem(`reviewed_game_${id}`, 'true');
        } catch (error) {
            if (error.response && error.response.status === 500) {
                alert("Już oceniłeś tę grę.");
                setHasReviewed(true);
                localStorage.setItem(`reviewed_game_${id}`, 'true');
            } else {
                alert("Nie udało się dodać recenzji.");
            }
        }
    };

    const handleDeleteReview = (reviewId) => {
        if(!window.confirm("Czy na pewno chcesz usunąć tę recenzję?")) return;

        api.delete(`/games/${reviewId}`)
            .then(() => {
                alert("Usunięto recenzję.");
                fetchReviews(); // Odśwież listę po usunięciu
            })
            .catch(err => {
                console.error(err);
                alert("Nie udało się usunąć recenzji. Sprawdź uprawnienia.");
            });
    };

    if (!game) return <div className="container">Ładowanie...</div>;

    return (
        <div className="container">
            <div className="details-layout">
                <div className="details-image">
                    <img
                        src={game.headerImage || 'https://placehold.co/600x400/222/2563eb?text=No+Image'}
                        alt={game.name}
                    />
                </div>

                <div className="details-info">
                    <h1 className="details-title">{game.name}</h1>
                    <p style={{ fontSize: '1.2rem', color: '#ccc' }}>{game.shortDescription}</p>

                    <div style={{ margin: '20px 0', lineHeight: '1.8' }}>
                        <p><strong>Gatunek:</strong> {game.genre}</p>
                        <p><strong>Wydawca:</strong> {game.publisher}</p>
                        <p><strong>Cena:</strong> <span style={{ color: 'var(--accent-color)', fontWeight: 'bold' }}>
                            {game.price > 0 ? `${game.price} ${game.currency}` : 'Free to Play'}
                        </span></p>
                    </div>

                    <button
                        onClick={toggleFavorite}
                        className="primary-btn"
                        style={{ backgroundColor: isFavorite ? '#ef4444' : 'var(--accent-color)', width: 'auto' }}
                    >
                        {isFavorite ? "💔 Usuń z ulubionych" : "❤ Dodaj do ulubionych"}
                    </button>
                </div>
            </div>

            {isLoggedIn && !hasReviewed && (
                <div className="auth-container" style={{ margin: '40px 0', width: '100%', maxWidth: '95%', textAlign: 'left' }}>
                    <h3>Dodaj swoją recenzję</h3>
                    <form onSubmit={handleAddReview}>
                        <label>Twoja ocena</label>
                        <select
                            value={rating}
                            onChange={e => setRating(Number(e.target.value))}
                            style={{
                                width: '100%', padding: '10px', marginBottom: '15px',
                                backgroundColor: '#2a2a2a', color: 'white', border: '1px solid #444', borderRadius: '8px'
                            }}
                        >
                            <option value="5">★★★★★ (5)</option>
                            <option value="4">★★★★ (4)</option>
                            <option value="3">★★★ (3)</option>
                            <option value="2">★★ (2)</option>
                            <option value="1">★ (1)</option>
                        </select>

                        <label>Komentarz</label>
                        <textarea
                            value={reviewText}
                            onChange={e => setReviewText(e.target.value)}
                            placeholder="Napisz co myślisz o grze..."
                            required
                            rows="3"
                            style={{
                                width: '100%', padding: '10px', marginBottom: '15px',
                                backgroundColor: '#2a2a2a', color: 'white', border: '1px solid #444', borderRadius: '8px', fontFamily: 'Inter'
                            }}
                        />

                        <button type="submit" className="primary-btn" style={{ width: '200px' }}>
                            Opublikuj recenzję
                        </button>
                    </form>
                </div>
            )}

            {isLoggedIn && hasReviewed && (
                <div style={{ margin: '40px 0', padding: '20px', backgroundColor: '#1c1c1c', borderRadius: '10px', borderLeft: '5px solid var(--accent-color)' }}>
                    <h3 style={{margin: 0}}>Dziękujemy za ocenę!</h3>
                    <p style={{color: '#aaa', margin: '5px 0 0 0'}}>Twoja recenzja została dodana.</p>
                </div>
            )}

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

                                    <div style={{ display: 'flex', gap: '15px', alignItems: 'center' }}>
                                        <span style={{ color: '#ffd700' }}>{'★'.repeat(review.rating)}</span>

                                        {userRole === 'ADMIN' && (
                                            <button
                                                onClick={() => handleDeleteReview(review.id)}
                                                style={{
                                                    background: 'transparent',
                                                    border: '1px solid #ef4444',
                                                    color: '#ef4444',
                                                    cursor: 'pointer',
                                                    borderRadius: '4px',
                                                    padding: '2px 8px',
                                                    fontSize: '0.7rem',
                                                    fontWeight: 'bold'
                                                }}
                                            >
                                                USUŃ
                                            </button>
                                        )}
                                    </div>
                                </div>
                                <p>{review.reviewText}</p>
                                <small style={{color: '#555'}}>{new Date(review.createdAt).toLocaleDateString()}</small>
                            </li>
                        ))}
                    </ul>
                )}
            </div>
        </div>
    );
}

export default GameDetails;