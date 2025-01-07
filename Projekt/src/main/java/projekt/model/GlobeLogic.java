package projekt.model;

public class GlobeLogic extends MapMovementLogicHandler{
    @Override
    public int getEnergyConsumption(Animal animal) {
        return map.getDefaultEnergyConsumption();
    }

    @Override
    public void moveAnimal(Animal animal) {
        Vector2d newPosition = animal.getPosition().add(animal.getDirection().toUnitVector());
        //pojscie w kierunku bieguna kuli ziemskiej
        if (newPosition.getY() >= map.getHeight() || newPosition.getY() < 0) {
            animal.setDirection(animal.getDirection().reverse());
            return;
        }
        //dookola świata
        else if ( newPosition.getX()<0){
            newPosition = new Vector2d(map.getWidth()-1, newPosition.getY());
        }
        else if (newPosition.getX()>=map.getWidth()){
            newPosition = new Vector2d(0, newPosition.getY());
        }
        animal.setPosition(newPosition);
    }
}
