import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../api';

function Home() {
    const [games, setGames] = useState([]);
    const [loading, setLoading] = useState(true);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);

    useEffect(() => {
        setLoading(true);

        api.get(`/games?page=${page}&size=15&sort=id,desc`)
            .then(response => {
                setGames(response.data.content);
                setTotalPages(response.data.totalPages);
                setLoading(false);
                window.scrollTo(0, 0);
            })
            .catch(error => {
                console.error("Błąd:", error);
                setLoading(false);
            });
    }, [page]);

    const handlePrevious = () => {
        if (page > 0) setPage(page - 1);
    };

    const handleNext = () => {
        if (page < totalPages - 1) setPage(page + 1);
    };

    if (loading) return <div className="container">Ładowanie...</div>;

    return (
        <div className="container">
            <h1 style={{ marginBottom: '40px' }}>Odkryj najlepsze gry</h1>

            {games.length === 0 ? (
                <div style={{ textAlign: 'center', padding: '50px' }}>
                    <h3>Brak gier do wyświetlenia.</h3>
                </div>
            ) : (
                <>
                    <div className="game-grid">
                        {games.map(game => (
                            <div key={game.id} className="game-card">
                                <img
                                    src={game.headerImage || 'https://placehold.co/600x400/222/2563eb?text=No+Image'}
                                    alt={game.name}
                                />
                                <div className="card-content">
                                    <h3>{game.name}</h3>
                                    <span className="price-tag">
                                        {game.price > 0 ? `${game.price} ${game.currency}` : 'Free to Play'}
                                    </span>
                                    <Link to={`/game/${game.id}`}>
                                        <button className="primary-btn">Zobacz szczegóły</button>
                                    </Link>
                                </div>
                            </div>
                        ))}
                    </div>

                    <div className="pagination">
                        <button
                            onClick={handlePrevious}
                            disabled={page === 0}
                            className="nav-btn-page"
                        >
                            ← Poprzednia
                        </button>

                        <span className="page-info">
                            Strona {page + 1} z {totalPages}
                        </span>

                        <button
                            onClick={handleNext}
                            disabled={page === totalPages - 1}
                            className="nav-btn-page"
                        >
                            Następna →
                        </button>
                    </div>
                </>
            )}
        </div>
    );
}

export default Home;