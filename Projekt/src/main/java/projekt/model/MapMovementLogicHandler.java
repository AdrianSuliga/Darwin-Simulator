package projekt.model;

public abstract class MapMovementLogicHandler {
    protected WorldMap map;

    public abstract int getEnergyConsumption(Animal animal);

    public abstract void moveAnimal(Animal animal);

    public void setMap(WorldMap map) {
        this.map = map;
    }
}
