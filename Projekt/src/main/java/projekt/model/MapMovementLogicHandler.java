package projekt.model;

public abstract class MapMovementLogicHandler {
    protected WorldMap map;

    public abstract int getEnergyConsumption();

    public abstract void moveAnimal(Animal animal);
}
