
package waypoint.model;

public class CurrencyMismatch extends RuntimeException {

    public CurrencyMismatch() {
        super("Cannot perform operation with different currencies");
    }
}
