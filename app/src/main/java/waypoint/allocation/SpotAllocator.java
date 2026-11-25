
package waypoint.allocation;

import waypoint.model.*;

 /**
  * Contract for parking spot allocation and release.
  *
  * PRE:
  * The vehicle must not be null. The allocator must have
  * a collection of valid parking spots.
  *
  * POST:
  * allocate() returns one suitable, previously free spot.
  * The returned spot becomes occupied.
  * If no suitable spot exists, NoSpotAvailable is thrown.
  *
  * INV:
  * A spot cannot be allocated to two vehicles simultaneously.
  *
  * Timing:
  * Allocation and release complete in finite time.
  *
  * Side effects:
  * Allocation changes a spot from free to occupied.
  * Release changes an allocated spot back to free.
  *
  * Idempotency:
  * Releasing an already released spot has no additional effect.
  *
  * Threading:
  * Concurrent allocation operations must not allocate
  * the same spot twice.
  *
  * Ordering:
  * Each implementation follows its own allocation strategy.
  */
public interface SpotAllocator {

    Spot allocate(Vehicle vehicle) throws NoSpotAvailable;

    void release(Spot spot);
}
