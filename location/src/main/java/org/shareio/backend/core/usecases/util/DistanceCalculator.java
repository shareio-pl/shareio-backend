package org.shareio.backend.core.usecases.util;

//import org.locationtech.jts.geom.Coordinate;
import org.shareio.backend.core.model.vo.Location;

public class DistanceCalculator {
//    public static Double calculateDistance(Location l1, Location l2) {
//        Coordinate point1 = new Coordinate(l1.getLatitude(), l1.getLongitude());
//        Coordinate point2 = new Coordinate(l2.getLatitude(), l2.getLongitude());
//        return point1.distance(point2) / 1000; // .distance() returns distance in meters
//    }

    public static Double calculateDistance(Location l1, Location l2) {
        double lat1Rad = Math.toRadians(l1.getLatitude());
        double lat2Rad = Math.toRadians(l2.getLatitude());
        double lon1Rad = Math.toRadians(l1.getLongitude());
        double lon2Rad = Math.toRadians(l2.getLongitude());

        double x = (lon2Rad - lon1Rad) * Math.cos((lat1Rad + lat2Rad) / 2);
        double y = (lat2Rad - lat1Rad);
        return Math.sqrt(x * x + y * y) * 6371;
    }

    private DistanceCalculator() {
        throw new IllegalStateException("Utility class");
    }
}
