# BookTracker

## Opis aplikacji
BookTracker to aplikacja mobilna na Androida (Java), która umożliwia użytkownikowi zarządzanie własną biblioteką książek. Użytkownik może dodawać książki ręcznie lub wyszukiwać je przez Google Books API, oznaczać jako przeczytane, oceniać oraz przeglądać szczegóły.

## Podział pracy
- **Osoba 1:** Interfejs użytkownika, nawigacja, ikona aplikacji
- **Osoba 2:** Baza danych lokalna, integracja z Google Books API, dokumentacja

## Opis bazy danych

Tabela `books`:
- `id` (Int, PK)
- `title` (String)
- `author` (String)
- `description` (String)
- `coverUrl` (String)
- `isRead` (Boolean)
- `rating` (Int)

### Diagram bazy danych

```
+-------+---------+----------+-------------+----------+--------+--------+
|  id   |  title  |  author  | description | coverUrl | isRead | rating |
+-------+---------+----------+-------------+----------+--------+--------+
```

## Schemat nawigacji

```
[Ekran główny] 
   |---> [Dodaj książkę]
   |---> [Szczegóły książki]
   |---> [Wyszukiwanie (API)]
   |---> [Statystyki/Profil]
```
