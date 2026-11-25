
package waypoint.model;

public class NoSpotAvailable extends Exception {

    public NoSpotAvailable(String message) {
        super(message);
    }

    public NoSpotAvailable() {
        super("No suitable parking spot available");
    }
}
