
package waypoint.model;

public record Vehicle(VehicleSize size) {

    public Vehicle {
        if (size == null) {
            throw new IllegalArgumentException("Vehicle size cannot be null");
        }
    }
}
