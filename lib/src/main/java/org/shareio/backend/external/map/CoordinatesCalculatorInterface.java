package org.shareio.backend.external.map;

import org.shareio.backend.exceptions.ExternalServiceException;
import org.shareio.backend.exceptions.LocationCalculationException;

import java.io.IOException;
import java.util.Map;

public interface CoordinatesCalculatorInterface {
    Map<String, Double> getCoordinatesFromAddress(String country, String city, String street, String houseNumber) throws ExternalServiceException, LocationCalculationException, IOException, InterruptedException;
}
