package projekt;

import projekt.model.MapDirection;
import projekt.model.Vector2d;
import projekt.model.WorldMap;
import projekt.util.Simulation;

public class DarwinWorld {

    public static void main(String[] args) {
        Simulation simulation = new Simulation(20, 20, 5,
                5, 5, 3, 10,
                10, 20, 5, 0,
                 false, false);
        simulation.run();
    }
}
