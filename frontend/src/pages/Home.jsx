import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../api';

function Home() {
    const [games, setGames] = useState([]);
    const [loading, setLoading] = useState(true);
    const [page, setPage] = useState(0);
    const [totalPages, setTotalPages] = useState(0);

    const [searchQuery, setSearchQuery] = useState("");
    const [debouncedSearch, setDebouncedSearch] = useState("");

    useEffect(() => {
        const timer = setTimeout(() => {
            setDebouncedSearch(searchQuery);
            setPage(0);
        }, 500);

        return () => clearTimeout(timer);
    }, [searchQuery]);

    useEffect(() => {
        setLoading(true);

        let url = `/games?page=${page}&size=15&sort=id,desc`;
        if (debouncedSearch) {
            url += `&search=${debouncedSearch}`;
        }

        api.get(url)
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
    }, [page, debouncedSearch]);

    const handlePrevious = () => {
        if (page > 0) setPage(page - 1);
    };

    const handleNext = () => {
        if (page < totalPages - 1) setPage(page + 1);
    };

    return (
        <div className="container">
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '40px', flexWrap: 'wrap', gap: '20px' }}>
                <h1 style={{ margin: 0 }}>Odkryj najlepsze gry</h1>

                <input
                    type="text"
                    placeholder="Szukaj gry..."
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    style={{
                        padding: '12px',
                        width: '300px',
                        borderRadius: '30px',
                        border: '1px solid #444',
                        backgroundColor: '#1c1c1c',
                        color: 'white',
                        margin: 0
                    }}
                />
            </div>

            {loading ? (
                <div className="container">Ładowanie...</div>
            ) : games.length === 0 ? (
                <div style={{ textAlign: 'center', padding: '50px' }}>
                    <h3>Nie znaleziono gier.</h3>
                    {debouncedSearch && <p>Spróbuj wpisać inną frazę.</p>}
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

                    {totalPages > 1 && (
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
                    )}
                </>
            )}
        </div>
    );
}

export default Home;