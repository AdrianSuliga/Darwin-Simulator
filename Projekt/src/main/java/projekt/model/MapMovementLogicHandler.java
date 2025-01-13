package projekt.model;

public abstract class MapMovementLogicHandler {
    protected WorldMap map;

    public abstract int getEnergyConsumption(Animal animal);

    public abstract void moveAnimal(Animal animal,Vector2d newPosition);

    public void setMap(WorldMap map) {
        this.map = map;
    }
}
