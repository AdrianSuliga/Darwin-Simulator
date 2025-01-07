package projekt.model;


public class PolarLogic extends MapMovementLogicHandler{
    @Override
    public int getEnergyConsumption(Animal animal) {
        double distanceFromEquator = Math.abs((map.getHeight()/2)-animal.getPosition().getY())*2/(double)map.getHeight();
        return (int) Math.floor(Math.pow(10.0, distanceFromEquator ));
    }

    @Override
    public void moveAnimal(Animal animal) {
        Vector2d newPosition = animal.getPosition().add(animal.getDirection().toUnitVector());
        if(newPosition.follows(map.getBoundary().lowerLeft()) &&
                newPosition.precedes(map.getBoundary().upperRight())){
            animal.setPosition(newPosition);
        }
    }
}
