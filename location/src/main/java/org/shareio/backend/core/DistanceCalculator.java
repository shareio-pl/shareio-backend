package org.shareio.backend.core;

import org.locationtech.jts.geom.Coordinate;
import org.shareio.backend.core.model.vo.Location;

public class DistanceCalculator {
    public static Double calculateDistance(Location l1, Location l2) {
        Coordinate point1 = new Coordinate(l1.getLatitude(), l1.getLongitude());
        Coordinate point2 = new Coordinate(l2.getLatitude(), l2.getLongitude());
        return point1.distance(point2) / 1000; // .distance() returns distance in meters
    }
}
