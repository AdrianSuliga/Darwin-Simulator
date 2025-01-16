package projekt.interfaces;

import projekt.model.WorldMap;

@FunctionalInterface
public interface MapChangeListener {
    void mapChanged(WorldMap map);
}
