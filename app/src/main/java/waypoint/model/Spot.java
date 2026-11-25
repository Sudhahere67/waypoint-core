
package waypoint.model;

public class Spot {

    private final SpotType type;
    private final int level;
    private final int distanceFromGate;

    private boolean free = true;

    public Spot(SpotType type, int level, int distanceFromGate) {
        if (type == null) {
            throw new IllegalArgumentException("Spot type cannot be null");
        }

        if (level < 0 || distanceFromGate < 0) {
            throw new IllegalArgumentException("Invalid spot location");
        }

        this.type = type;
        this.level = level;
        this.distanceFromGate = distanceFromGate;
    }

    public SpotType type() {
        return type;
    }

    public int level() {
        return level;
    }

    public int distanceFromGate() {
        return distanceFromGate;
    }

    public synchronized boolean isFree() {
        return free;
    }

    public synchronized boolean occupy() {
        if (!free) {
            return false;
        }

        free = false;
        return true;
    }

    public synchronized void release() {
        free = true;
    }
}
