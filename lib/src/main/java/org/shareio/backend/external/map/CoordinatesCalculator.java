package org.shareio.backend.external.map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.shareio.backend.exceptions.LocationCalculationException;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CoordinatesCalculator implements CoordinatesCalculatorInterface {
    @Override
    public Map<String, Double> getCoordinatesFromAddress(String country, String city, String street, String houseNumber) throws LocationCalculationException, IOException, InterruptedException {
        Map<String, Double> coordinates = new HashMap<>();
        String address = country + "," + city + "," + street + "," + houseNumber;
        String apiUrl = "https://nominatim.openstreetmap.org/search?q=" + URLEncoder.encode(address, StandardCharsets.UTF_8) + "&format=json";

        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(apiUrl)).GET().build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        JSONArray array = null;
        try {
            array = new JSONArray(response.body());
        } catch (JSONException e) {
            throw new LocationCalculationException("Timeout on API!");
        }
        JSONObject object = array.getJSONObject(0);
        if (object.has("place_id")) {
            coordinates.put("lat", object.getDouble("lat"));
            coordinates.put("lon", object.getDouble("lon"));

            return coordinates;
        } else {
            throw new LocationCalculationException("Couldn't determine coordinates for given address");
        }
    }
}
