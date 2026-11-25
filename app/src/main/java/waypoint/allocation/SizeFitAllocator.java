
package waypoint.allocation;

import waypoint.model.*;
import java.util.Collection;
import java.util.List;
import java.util.Comparator;

public final class SizeFitAllocator implements SpotAllocator {

    private final List<Spot> spots;

    public SizeFitAllocator(Collection<Spot> spots) {
        if (spots == null) {
            throw new IllegalArgumentException("Spots cannot be null");
        }

        this.spots = List.copyOf(spots);
    }

    private int priority(Spot spot, VehicleSize size) {

        SpotType type = spot.type();

        if (type instanceof SpotType.Compact) {
            return 0;
        }

        if (type instanceof SpotType.Ev) {
            return 1;
        }

        if (type instanceof SpotType.Large) {
            return 2;
        }

        return 3;
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
                .sorted(
                    Comparator
                        .comparingInt(
                            (Spot s) -> priority(s, vehicle.size())
                        )
                        .thenComparingInt(Spot::distanceFromGate)
                )
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
