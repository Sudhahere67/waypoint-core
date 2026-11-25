
package waypoint.allocation;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import waypoint.allocation.*;
import waypoint.model.NoSpotAvailable;
import waypoint.model.Spot;
import waypoint.model.SpotType;
import waypoint.model.Vehicle;
import waypoint.model.VehicleSize;

import java.util.List;
import java.util.stream.Stream;

class AllocatorContractTest {

    // Creates three fresh parking spots for every implementation.
    static List<Spot> threeFreeSpots() {
        return List.of(
            new Spot(new SpotType.Compact(), 1, 10),
            new Spot(new SpotType.Compact(), 2, 20),
            new Spot(new SpotType.Large(), 1, 30)
        );
    }

    static Vehicle car() {
        return new Vehicle(VehicleSize.COMPACT);
    }

    static Vehicle motorcycle() {
        return new Vehicle(VehicleSize.MOTORCYCLE);
    }

    // Supplies all three implementations to the same test suite.
    static Stream<SpotAllocator> implementations() {
        return Stream.of(
            new NearestFirstAllocator(threeFreeSpots()),
            new LevelBalancingAllocator(threeFreeSpots()),
            new SizeFitAllocator(threeFreeSpots())
        );
    }

    // Fills every available spot.
    static void drainAllSpots(SpotAllocator allocator) {
        while (true) {
            try {
                allocator.allocate(car());
            } catch (NoSpotAvailable expected) {
                return;
            }
        }
    }

    @ParameterizedTest
    @MethodSource("implementations")
    void neverReturnsNull(SpotAllocator allocator) throws NoSpotAvailable {
        assertNotNull(allocator.allocate(car()));
    }

    @ParameterizedTest
    @MethodSource("implementations")
    void allocatedSpotIsNoLongerFree(SpotAllocator allocator)
            throws NoSpotAvailable {

        Spot spot = allocator.allocate(car());

        assertFalse(spot.isFree());
    }

    @ParameterizedTest
    @MethodSource("implementations")
    void acceptsEveryVehicleSize(SpotAllocator allocator) {
        assertDoesNotThrow(() -> allocator.allocate(motorcycle()));
    }

    @ParameterizedTest
    @MethodSource("implementations")
    void throwsWhenNothingFits(SpotAllocator allocator) {
        drainAllSpots(allocator);

        assertThrows(
            NoSpotAvailable.class,
            () -> allocator.allocate(car())
        );
    }

    @ParameterizedTest
    @MethodSource("implementations")
    void releaseIsIdempotent(SpotAllocator allocator)
            throws NoSpotAvailable {

        Spot spot = allocator.allocate(car());

        allocator.release(spot);

        assertDoesNotThrow(() -> allocator.release(spot));

        assertTrue(spot.isFree());
    }

    @ParameterizedTest
    @MethodSource("implementations")
    void neverAllocatesTheSameSpotTwice(SpotAllocator allocator)
            throws NoSpotAvailable {

        Spot first = allocator.allocate(car());

        for (int i = 0; i < 2; i++) {
            try {
                Spot next = allocator.allocate(car());

                assertNotSame(first, next);
            } catch (NoSpotAvailable expected) {
                return;
            }
        }
    }
}
