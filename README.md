# Game Catalog Application

**Repozytorium:** https://github.com/Margriell/game-catalog

Aplikacja webowa do przeglądania katalogu gier, zarządzania ulubionymi tytułami oraz dodawania recenzji. Projekt składa się z backendu opartego o **Java Spring Boot** oraz frontendu napisanego w **React**.

---

## Funkcjonalności

### Użytkownik

* **Rejestracja i Logowanie** – zabezpieczone za pomocą JWT Authentication.
* **Katalog Gier** – przeglądanie gier z paginacją, sortowaniem i wyszukiwaniem.
* **Szczegóły Gry** – informacje o cenie, wydawcy, platformach i wymaganiach.
* **System Rekomendacji** – propozycje gier z tego samego gatunku.
* **Ulubione** – dodawanie i usuwanie gier z listy ulubionych.
* **Eksport Danych** – pobieranie ulubionych gier w formatach `.txt`, `.json`, `.xml`.
* **Recenzje** – dodawanie ocen i komentarzy (walidacja: jedna recenzja na grę).
* **Profil** – edycja danych osobowych oraz zmiana hasła.

### Administrator

* **Moderacja** – możliwość usuwania dowolnych recenzji użytkowników.
* **Konto Admina** – automatycznie tworzone przy starcie aplikacji.

---

## Technologie

### Backend

* Java 21
* Spring Boot 3 (Web, Data JPA, Security, Validation)
* PostgreSQL
* Hibernate / JPA
* JWT
* MapStruct
* Lombok
* Jackson
* SpringDoc OpenAPI (Swagger UI)
* Docker Compose

### Frontend

* React 19
* Vite
* React Router
* Axios
* CSS Modules

---

## Uruchomienie projektu

### 1. Baza Danych (Docker)

W głównym katalogu uruchom:

```bash
docker-compose up -d
```

---

### 2. Backend (Spring Boot)

Wymagane **JDK 21**.
Przy pierwszym uruchomieniu aplikacja automatycznie zaimportuje dane z pliku `steam_games_database.json`.

Uruchamianie:

```bash
./mvnw spring-boot:run
```

Domyślny port backendu: **8080**

**Domyślne konto administratora:**

* Email: `admin@gamecatalog.com`
* Hasło: `admin123`

---

### 3. Frontend (React)

Przejdź do katalogu `frontend` i uruchom:

```bash
cd frontend
npm install
npm run dev
```

Aplikacja dostępna pod adresem:
[http://localhost:5173](http://localhost:5173)

---

## Dokumentacja API (Swagger UI)

Aplikacja posiada wbudowaną dokumentację API generowaną automatycznie.
Po uruchomieniu backendu jest ona dostępna pod adresem:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

Umożliwia ona testowanie endpointów (np. logowanie, pobieranie gier) bezpośrednio z przeglądarki.

---

## Testy

Projekt zawiera:

* Testy Jednostkowe – JUnit 5, Mockito
* Testy Integracyjne – MockMvc

Uruchamianie testów backendu:

```bash
./mvnw test
```

---
