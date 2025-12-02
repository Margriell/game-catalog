import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import api from '../api';

function GameDetails() {
    const { id } = useParams(); // Pobiera ID gry z adresu URL
    const [game, setGame] = useState(null);
    const [reviews, setReviews] = useState([]);

    // Pobieranie danych po wejściu na stronę
    useEffect(() => {
        api.get(`/games/${id}`).then(res => setGame(res.data));
        api.get(`/games/${id}/reviews`).then(res => setReviews(res.data));
    }, [id]);

    const addToFavorites = () => {
        api.post(`/game/${id}/favourite`)
            .then(res => alert(res.data))
            .catch(() => alert("Musisz być zalogowany!"));
    };

    if (!game) return <div>Ładowanie...</div>;

    return (
        <div className="container">
            <div className="details-header">
                <img src={game.headerImage} alt={game.name} style={{maxWidth: '100%'}}/>
                <div>
                    <h1>{game.name}</h1>
                    <p>{game.shortDescription}</p>
                    <p><strong>Gatunek:</strong> {game.genre}</p>
                    <p><strong>Wydawca:</strong> {game.publisher}</p>
                    <button onClick={addToFavorites} style={{marginTop: '10px', backgroundColor: '#e91e63'}}>
                        ❤ Dodaj do ulubionych
                    </button>
                </div>
            </div>

            <div className="reviews-section">
                <h3>Recenzje użytkowników</h3>
                {reviews.length === 0 ? <p>Brak recenzji.</p> : (
                    <ul>
                        {reviews.map(review => (
                            <li key={review.id} style={{marginBottom: '10px', borderBottom: '1px solid #ccc'}}>
                                <strong>{review.userName}</strong> (Ocena: {review.rating}/5)
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