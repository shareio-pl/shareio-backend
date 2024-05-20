package org.shareio.backend.core.usecases.util;

import org.shareio.backend.core.model.vo.Location;
import org.shareio.backend.exceptions.LocationCalculationException;
import org.shareio.backend.external_API.OSM.CoordinatesCalculator;

import java.io.IOException;
import java.util.Map;

public class LocationCalculator {
    public static Location getLocationFromAddress(String country, String city, String street, String houseNumber) throws LocationCalculationException, IOException, InterruptedException {
        CoordinatesCalculator calculator = new CoordinatesCalculator();
        Map<String, Double> coordinates = calculator.getCoordinatesFromAddress(country, city, street, houseNumber);
        return new Location(coordinates.get("lat"), coordinates.get("lon"));
    }
}
