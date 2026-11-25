
package waypoint.allocation;

import waypoint.model.*;
import java.util.Collection;
import java.util.List;
import java.util.Comparator;

public final class NearestFirstAllocator implements SpotAllocator {

    private final List<Spot> spots;

    public NearestFirstAllocator(Collection<Spot> spots) {
        if (spots == null) {
            throw new IllegalArgumentException("Spots cannot be null");
        }

        this.spots = List.copyOf(spots);
    }

    @Override
    public synchronized Spot allocate(Vehicle vehicle)
            throws NoSpotAvailable {

        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle cannot be null");
        }

        return spots.stream()
                .filter(Spot::isFree)
                .filter(s -> SpotType.fits(vehicle.size(), s.type()))
                .sorted(Comparator.comparingInt(Spot::distanceFromGate))
                .filter(Spot::occupy)
                .findFirst()
                .orElseThrow(NoSpotAvailable::new);
    }

    @Override
    public synchronized void release(Spot spot) {
        if (spot == null) {
            return;
        }

        if (spots.contains(spot)) {
            spot.release();
        }
    }
}
