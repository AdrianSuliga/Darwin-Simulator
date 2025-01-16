package projekt.util;

import projekt.model.Vector2d;
import projekt.model.WorldMap;

import java.util.ArrayList;
import java.util.List;

public class RandomPositionGenerator {
    private final WorldMap map;
    private int equatorMinHeight;
    private int equatorMaxHeight;

    public RandomPositionGenerator(WorldMap map) {
        this.map = map;
    }

    public List<Vector2d> generateNewPlants() {
        List<Vector2d> result = new ArrayList<>();
        List<Vector2d> occupiedPositions = new ArrayList<>(map.getPlantList().keySet());
        List<Vector2d> allPossiblePositions = generateAllPossiblePositions(occupiedPositions);
        List<Vector2d> equatorPositions = new ArrayList<>();
        List<Vector2d> nonEquatorPositions = new ArrayList<>();

        equatorMinHeight = (int)(Math.floor(this.map.getHeight() * 0.4));
        equatorMaxHeight = equatorMinHeight + (int)(Math.floor(this.map.getWidth() * 0.2));

        for (Vector2d vector : allPossiblePositions) {
            if (vector.getY() >= equatorMinHeight && vector.getY() <= equatorMaxHeight) {
                equatorPositions.add(vector);
            }
        }

        for (Vector2d vector : allPossiblePositions) {
            if (vector.getY() < equatorMinHeight || vector.getY() > equatorMaxHeight) {
                nonEquatorPositions.add(vector);
            }
        }

        int equatorPositionsSize = equatorPositions.size();
        int nonEquatorPositionsSize = nonEquatorPositions.size();

        for (int i = 0; i < this.map.getPlantsPerDay(); i++) {
            if (equatorPositionsSize > 0 && nonEquatorPositionsSize > 0) {
                // if there is space both on an equator and outside than we draw
                if (Math.random() < 0.8) {
                    result.add(drawPositionFrom(equatorPositions, equatorPositionsSize));
                    equatorPositionsSize--;
                } else {
                    result.add(drawPositionFrom(nonEquatorPositions, nonEquatorPositionsSize));
                    nonEquatorPositionsSize--;
                }
            } else if (equatorPositionsSize > 0 && nonEquatorPositionsSize == 0) {
                // if the space is only on one part of the map, there is no point to drawing
                result.add(drawPositionFrom(equatorPositions, equatorPositionsSize));
                equatorPositionsSize--;
            } else if (equatorPositionsSize == 0 && nonEquatorPositionsSize > 0) {
                result.add(drawPositionFrom(nonEquatorPositions, nonEquatorPositionsSize));
                nonEquatorPositionsSize--;
            } else {
                break; // edge case when there is no space left to spawn enough plants
            }
        }

        return result;
    }

    private Vector2d drawPositionFrom(List<Vector2d> source, int size) {
        int chosenIndex = (int)(Math.floor(Math.random() * size));
        Vector2d result = source.get(chosenIndex);
        swapListItems(source, chosenIndex, size - 1);
        source.remove(size - 1);
        return result;
    }

    private void swapListItems(List<Vector2d> list, int i, int j) {
        Vector2d temp = list.get(i);
        list.set(i, list.get(j));
        list.set(j, temp);
    }

    private List<Vector2d> generateAllPossiblePositions(List<Vector2d> occupiedPositions) {
        List<Vector2d> result = new ArrayList<>();
        for (int i = 0; i < this.map.getWidth(); i++) {
            for (int j = 0; j < this.map.getHeight(); j++) {
                Vector2d consideredVector = new Vector2d(i, j);
                if (!occupiedPositions.contains(consideredVector)) {
                    result.add(consideredVector);
                }
            }
        }
        return result;
    }

    public boolean isPositionInEquator(int y){
        return y>=equatorMinHeight && y<=equatorMaxHeight;
    }
}
