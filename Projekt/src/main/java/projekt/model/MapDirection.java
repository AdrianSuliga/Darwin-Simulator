package projekt.model;

public enum MapDirection {
    NORTH,
    NORTHEAST,
    EAST,
    SOUTHEAST,
    SOUTH,
    SOUTHWEST,
    WEST,
    NORTHWEST;

    @Override
    public String toString() {
        return switch (this) {
            case EAST -> "E";
            case WEST -> "W";
            case NORTH -> "N";
            case SOUTH -> "S";
            case NORTHEAST -> "NE";
            case NORTHWEST -> "NW";
            case SOUTHEAST -> "SE";
            case SOUTHWEST -> "SW";
        };
    }

    public Vector2d toUnitVector() {
        return switch (this) {
            case EAST -> new Vector2d(1, 0);
            case WEST -> new Vector2d(-1, 0);
            case NORTH -> new Vector2d(0, 1);
            case SOUTH -> new Vector2d(0, -1);
            case NORTHEAST -> new Vector2d(1, 1);
            case NORTHWEST -> new Vector2d(-1, 1);
            case SOUTHEAST -> new Vector2d(1, -1);
            case SOUTHWEST -> new Vector2d(-1, -1);
        };
    }
}
