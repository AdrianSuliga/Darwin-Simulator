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

    public static MapDirection fromInt(int i) {
        return switch (i) {
            case 1 -> NORTHEAST;
            case 2 -> EAST;
            case 3 -> SOUTHEAST;
            case 4 -> SOUTH;
            case 5 -> SOUTHWEST;
            case 6 -> WEST;
            case 7 -> NORTHWEST;
            default -> NORTH;
        };
    }

    public int toInt() {
        return switch (this) {
            case EAST -> 2;
            case WEST -> 6;
            case NORTH -> 0;
            case SOUTH -> 4;
            case NORTHEAST -> 1;
            case NORTHWEST -> 7;
            case SOUTHEAST -> 3;
            case SOUTHWEST -> 5;
        };
    }
    public MapDirection add(MapDirection d){
        return fromInt((this.toInt()+d.toInt())%8);
    }

    public MapDirection reverse(){
        return MapDirection.fromInt((toInt()+4)%8);
    }
}
