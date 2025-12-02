import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import api from '../api';

function Home() {
    const [games, setGames] = useState([]);

    useEffect(() => {
        api.get('/games')
            .then(response => {
                // Spring zwraca dane w polu 'content'
                setGames(response.data.content);
            })
            .catch(error => console.error("Błąd pobierania gier:", error));
    }, []);

    return (
        <div className="container">
            <h1>Katalog Gier</h1>
            <div className="game-grid">
                {games.map(game => (
                    <div key={game.id} className="game-card">
                        <img src={game.headerImage} alt={game.name} />
                        <h3>{game.name}</h3>
                        <p>{game.price > 0 ? `${game.price} ${game.currency}` : 'Za darmo'}</p>
                        {/* Link do szczegółów gry */}
                        <Link to={`/game/${game.id}`}>
                            <button>Szczegóły</button>
                        </Link>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default Home;