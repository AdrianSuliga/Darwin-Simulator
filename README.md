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

- Energia najsilniejszego zwierzęcia na mapie jest przedstawiana jako gradient koloru od <span style="color: rgb(23, 227, 0)">jasno zielonego</span> do <span style="color:rgb(0, 26, 255)">ciemno niebieskiego</span> na danym polu na mapie (im zieleńszy tym zwierzak ma więcej energii).

- Martwe zwierzęta mają kolor czarny - <span style="color: black">X</span>

- Po zatrzymaniu, pola zawierające zwierzęta z najpopularniejszymi genomami mają tekst koloru <span style="color: red">czerwonego</span>. 

- Śledzone zwierzę ma pole o jasnoszarym tle i jest reprezentowane przez swój kierunek zamiast znaku X. 

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