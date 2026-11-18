
package waypoint.model;

public class TicketAlreadyClosed extends RuntimeException {

    public TicketAlreadyClosed() {
        super("Ticket is already closed");
    }
}
