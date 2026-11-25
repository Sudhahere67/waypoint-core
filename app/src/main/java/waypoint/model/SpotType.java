
package waypoint.model;

public sealed interface SpotType
        permits SpotType.Compact, SpotType.Large, SpotType.Ev {

    record Compact() implements SpotType {}

    record Large() implements SpotType {}

    record Ev(int kw) implements SpotType {
        public Ev {
            if (kw <= 0) {
                throw new IllegalArgumentException(
                    "Charging power must be positive"
                );
            }
        }
    }

    static boolean fits(VehicleSize size, SpotType spot) {
        if (size == null || spot == null) {
            return false;
        }

        return switch (spot) {
            case Compact ignored ->
                size == VehicleSize.MOTORCYCLE ||
                size == VehicleSize.COMPACT;

            case Large ignored ->
                true;

            case Ev ignored ->
                true;
        };
    }
}
