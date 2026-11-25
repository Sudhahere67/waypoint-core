
package waypoint.allocation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import waypoint.allocation.*;
import waypoint.model.Vehicle;
import waypoint.model.VehicleSize;

class CanaryTest {

    static Vehicle car() {
        return new Vehicle(VehicleSize.COMPACT);
    }

    @Test
    void theSuiteRejectsADeliberatelyBrokenImplementation() {

        SpotAllocator broken = new BrokenAllocator();

        // Check 1: BrokenAllocator returns null.
        assertThrows(
            AssertionError.class,
            () -> assertNotNull(broken.allocate(car())),
            "The suite must reject a null-returning allocator"
        );

        // Check 2: BrokenAllocator throws instead of releasing safely.
        assertThrows(
            IllegalStateException.class,
            () -> broken.release(null),
            "The suite must reject a non-idempotent release"
        );
    }
}
