package projekt.model;

import projekt.interfaces.WorldElement;

import java.util.ArrayList;
import java.util.List;

public class Animal implements WorldElement {
    private Vector2d position;
    private MapDirection direction;
    private int energy;
    private final List<Integer> genes;
    private int activeGeneIdx;
    private int daysLived;
    private int plantsEaten;
    private int childrenMade;
    private int deathDay;

    public Animal(Vector2d position, int energy, List<Integer> genes) {
        this.position = position;
        this.direction = MapDirection.fromInt((int)(Math.random() * 8));
        this.energy = energy;
        this.genes = new ArrayList<>(genes);
        this.activeGeneIdx = (int)(Math.random() * this.genes.size());
        this.daysLived = 1;
        this.plantsEaten = 0;
        this.childrenMade = 0;
        this.deathDay = -1;
    }

    public boolean isAt(Vector2d position) {
        return this.position.equals(position);
    }

    // zwierzak obraca się, potem idzie w daną strone
    public void move(MapMovementLogicHandler handler) {
        direction = direction.add(genes.get(activeGeneIdx)); // dodajemy do obecnego kierunku ten z genomu
        Vector2d newPosition = position.add(direction.toUnitVector());
        activeGeneIdx = (activeGeneIdx + 1) % genes.size();
        this.energy -= handler.getEnergyConsumption(this);
        handler.moveAnimal(this,newPosition);
        this.daysLived++;
        if (this.energy <= 0) {
            this.deathDay = this.daysLived;
        }
    }

    public void eat(int energyGranted) {
        this.energy += energyGranted;
        this.plantsEaten++;
    }

    public void breed(int energyConsumed) {
        this.energy -= energyConsumed;
        this.childrenMade++;
    }

    @Override
    public Vector2d getPosition() {
        return this.position;
    }

    @Override
    public String toString() {
        return direction.toString() + " " + this.energy;
    }

    public MapDirection getDirection() {
        return this.direction;
    }

    public int getEnergy() {
        return this.energy;
    }

    public List<Integer> getGenes() {
        return this.genes;
    }

    public int getActiveGeneIdx() {
        return this.activeGeneIdx;
    }

    public int getDaysLived() {
        return this.daysLived;
    }

    public int getPlantsEaten() {
        return this.plantsEaten;
    }

    public int getChildrenMade() {
        return this.childrenMade;
    }

    public int getDeathDay() {
        return this.deathDay;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public void setDirection(MapDirection direction) {
        this.direction = direction;
    }

    public void setDaysLived(int daysLived) {
        this.daysLived = daysLived;
    }

    public void setChildrenMade(int childrenMade) {
        this.childrenMade = childrenMade;
    }

    public String getStatistics(){
        return "kierunek: "+direction+ '\n'
                +"genom: "+genes+ '\n'
                +"aktywny: "+genes.get(activeGeneIdx)+ '\n'
                +"energia: "+energy+ '\n'
                +"zjedzone rosliny: "+plantsEaten+ '\n'
                +"ilosc dzieci: "+childrenMade+ '\n'
                + (deathDay==-1 ? "wiek: "+daysLived+ '\n' : "dzien smierci: "+deathDay);
    }
}
