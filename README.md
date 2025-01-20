# Darwin-Simulator

Projekt na zaliczenie przedmiotu _Programowanie obiektowe_ na AGH.

## Intro

Wersja projektu: A1

- A - Bieguny (wariant mapy)
- 1 - Lekka korekta (wariant mutacji)

Autorzy:

- [Adrian Suliga](https://github.com/AdrianSuliga)
- [Aleksander Matyka](https://github.com/alekmatyka)

## Legenda mapy

- Zwierzęta są reprezentowane jako X, trawa jako *. Zwierzęta mają pierwszeństwo nad trawą jeśli są na tym samym polu.

- Energia najsilniejszego zwierzęcia na mapie jest przedstawiana jako gradient koloru od jasno zielonego do ciemno niebieskiego na danym polu na mapie (im zieleńszy tym zwierzak ma więcej energii).

- Martwe zwierzęta mają kolor czarny

- Po zatrzymaniu, pola zawierające zwierzęta z najpopularniejszymi genomami mają tekst koloru czerwonego. 

- Śledzone zwierzę ma pole o jasnoszarym tle i jest reprezentowane przez swój kierunek zamiast znaku X. 

## Opisy klas

### AbstractGeneMutator
Klasa abstrakcyjna logiki mutacji i kreacji nowego genomu

### RandomMutator
Klasa odpowiadająca za mutacje i kreacje genomu zgodnie z opcją "pełna losowość"

### StepMutator
Klasa odpowiadająca za mutacjec i kreacje genomu zgodnie z opcją "lekka korekta"

### MapMovementLogicHandler
Klasa abstrakcyjna logiki poruszania się i konsumpcji energii na mapie

### GlobeLogic
Klasa odpowiadająca za koszt energi i ruch po mapie zgodnie z opcją "kula ziemska"

### PolarLogic
Klasa odpowiadająca za koszt energi ruch po mapie zgodnie z opcją "bieguny"

### Statistics
Klasa rekordu przedstatwiająca obecne statystyki mapy

### StatisticsManager
Klasa obliczjąca statystyki symulacji

### AnimalComparator
Klasa porównująca obiekty klasy Animal

### Animal
Klasa reprezentująca zwierzaka

### Boundary
Klasa rekordu reprezentująca granice mapy

### WorldMap
Klasa reprezentująca mapę. Odpowiada za ruch i usuwanie martwych zwierząt, stawianie i konsumowanie roślin.

### MapDirection
Klasa wyliczeniowa reprezentująca kierunek zwierzaka na mapie

### Vector2d
Klasa reprezentująca pozycje elementów interfejsu WorldElement

### WorldElement
Interfejs implementujący logike elementu obecnego na mapie

### MapChangeListener
Listener zmian na mapie

### CSVDataWriter
Klasa zajmująca się zapisem statystyk symulacji do plików .csv

### MapVisualizer
Klasa przedstawiająca mapę w formacie tekstowym

### RandomPositionGenerator
Klasa losująca pozycje na mapie zgodnie z zasadą Pareto

### Simulation
Klasa obsługujaca logikę symulacji, odpowiada za rozmnażanie zwierząt

### SimulationApp 
Klasa dziedzicząca po Application, uruchamia okno konfiguracyjne aplikacji

### SimulationPresenter
Klasa odpowiadająca za tworzenie nowych okien symulacji i pobieranie danych z okna konfiguracyjnego

## Podział pracy:

### Aleksander Matyka:
- Setup projektu
- Enum MapDirection
- Klasa Vector2d
- Logika mapy, klasy: GlobeLogic, MapMovementLogicHandler, WorldMap
- Logika mutacji genów, klasy: AbstractGeneMutator, StepMutator, RandomMutator
- Interakcja pomiędzy mapą, a symulacją
- Statystyki aplikacji (w tym ich graficzna reprezentacja)

### Adrian Suliga
- Interfejsy WorldElement i MapChangeListener
- Klasy Animal, Plant
- Testy do klas Vector2d, Plant, MapDirection, Animal
- Logika wzrostu roślin, klasa: RandomPositionGenerator
- Logika symulacji, klasa: Simulation
- Interfejs graficzny w JavaFX (okna i animacja mapy)
- Zapis statystyk do pliku, klasa: CSVDataWriter
