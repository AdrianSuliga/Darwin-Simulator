package projekt.model;

public class GlobeLogic extends MapMovementLogicHandler{

    @Override
    public int getEnergyConsumption() {
        return map.getDefaultEnergyConsumption();
    }

    @Override
    public void moveAnimal(Animal animal, MapDirection direction) {

    }

}
