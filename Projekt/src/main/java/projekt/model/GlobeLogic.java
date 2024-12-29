package projekt.model;

public class GlobeLogic extends MapMovementLogicHandler{
    @Override
    public int getEnergyConsumption() {
        return map.getDefaultEnergyConsumption();
    }

    @Override
    public void moveAnimal(Animal animal) {
        Vector2d newPosition = animal.getPosition().add(animal.getDirection().toUnitVector());
        if (newPosition.getY() >= map.getHeight() || newPosition.getY() < 0) {
            animal.setDirection(animal.getDirection().reverse());
        }
    }
}
