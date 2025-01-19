# Darwin-Simulator

Projekt na zaliczenie przedmiotu _Programowanie obiektowe_ na AGH.

## Intro

Wersja projektu: A1

- A - Bieguny (wariant mapy)
- 1 - Lekka korekta (wariant mutacji)

Autorzy:

- [Adrian Suliga](https://github.com/AdrianSuliga)
- [Aleksander Matyka](https://github.com/alekmatyka)

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