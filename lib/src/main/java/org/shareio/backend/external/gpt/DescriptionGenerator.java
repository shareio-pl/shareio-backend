package org.shareio.backend.external.gpt;

import org.json.JSONObject;
import org.shareio.backend.EnvGetter;
import org.shareio.backend.exceptions.DescriptionGenerationException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DescriptionGenerator implements DescriptionGeneratorInterface {

    private String generateDescription(String offerData) throws IOException, InterruptedException, DescriptionGenerationException {
        String gptUrl = EnvGetter.getGptApiUrl();
        String gptKey = EnvGetter.getGptApiKey();
        String gptPrompt = EnvGetter.getGptPrompt();
        String requestBody = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \" " + gptPrompt + offerData + "\"}], \"temperature\": 1.0}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(gptUrl))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + gptKey)
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject json = new JSONObject(response.body());
        if (json.isEmpty()) {
            throw new DescriptionGenerationException("Could not generate description");
        } else {
            return json.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
        }
    }

    @Override
    public String generateDescription(String title, String condition, String category, String additionalData) throws IOException, InterruptedException, DescriptionGenerationException {
        String offerData = title + ", " + condition + ", " + category + ", " + additionalData;
        return generateDescription(offerData);
    }

    @Override
    public String generateDescription(String title, String condition, String category) throws IOException, InterruptedException, DescriptionGenerationException {
        String offerData = title + ", " + condition + ", " + category;
        return generateDescription(offerData);
    }
}
