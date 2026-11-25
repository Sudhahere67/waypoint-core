
package waypoint.allocation;

import waypoint.model.*;

public final class BrokenAllocator implements SpotAllocator {

    @Override
    public Spot allocate(Vehicle vehicle) {
        // Deliberately violates the allocation postcondition.
        return null;
    }

    @Override
    public void release(Spot spot) {
        // Deliberately violates the idempotency requirement.
        throw new IllegalStateException(
            "Broken allocator cannot release spots"
        );
    }
}
