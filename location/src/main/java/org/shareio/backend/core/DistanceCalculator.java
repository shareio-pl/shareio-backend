package org.shareio.backend.core;

import org.shareio.backend.core.model.vo.Location;

public class DistanceCalculator {
    public static Double calculateDistance(Location l1, Location l2) {
        return Math.sqrt(
                Math.pow((l1.getLatitude() - l2.getLatitude()), 2)
                        +
                        Math.pow((l1.getLongitude() - l2.getLongitude()), 2)
        );
    }
}
