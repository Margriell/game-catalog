Game Catalog Application

Aplikacja webowa do przeglądania katalogu gier, zarządzania ulubionymi tytułami oraz dodawania recenzji. Projekt składa się z backendu opartego o Java Spring Boot oraz frontendu napisanego w React.

Funkcjonalności

Użytkownik:
- Rejestracja i Logowanie (JWT Authentication).
- Przeglądanie gier z paginacją i wyszukiwaniem.
- System Rekomendacji: gry z tego samego gatunku.
- Dodawanie gier do ulubionych.
- Eksport listy ulubionych gier w formatach TXT, JSON, XML.
- Dodawanie ocen i komentarzy (z blokadą wielokrotnych recenzji).
- Edycja imienia, nazwiska oraz zmiana hasła.

Administrator:
- Możliwość usuwania dowolnych recenzji użytkowników.
- Konto administratora tworzone przy starcie aplikacji.

Technologie:

Backend:
- Java 21
- Spring Boot 3 (Web, Data JPA, Security, Validation)
- PostgreSQL (Baza danych)
- Hibernate / JPA
- JWT (JSON Web Tokens)
- Jackson & Jackson XML (Obsługa JSON/XML)
- Docker Compose (Konteneryzacja bazy danych)

Frontend:
- React 19
- Vite
- React Router
- Axios
- CSS Modules / Styled Components

Uruchomienie projektu

1. Baza Danych
   Wymagany Docker. W głównym katalogu uruchom:
   docker-compose up -d

2. Backend (Spring Boot)
   Wymagane JDK 21. Uruchom aplikację z poziomu IntelliJ lub terminala:

./mvnw spring-boot:run
Domyślnie serwer startuje na porcie 8080.

Domyślne konto administratora:

Email: admin@gamecatalog.com
Hasło: admin123

3. Frontend (React)
   Przejdź do katalogu frontend i uruchom aplikację:

npm run dev
Aplikacja będzie dostępna pod adresem: http://localhost:5173

Projekt posiada:

Testy Jednostkowe: Weryfikacja logiki serwisów (JUnit 5, Mockito).
Testy Integracyjne: Testowanie endpointów REST API (MockMvc).

Aby uruchomić testy backendu:
./mvnw test