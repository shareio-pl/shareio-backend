package org.shareio.backend.external_API.OSM;

import java.util.Map;

public interface CoordinatesCalculatorInterface {
    Map<String, Double> getCoordinatesFromAddress(String country, String city, String street, String houseNumber) throws Exception;
}
