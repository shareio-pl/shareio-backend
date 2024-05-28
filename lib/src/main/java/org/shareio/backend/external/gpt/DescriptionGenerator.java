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
    static final String PROMPT = "Wygeneruj opis przedmiotu oddawanego za darmo na podstawie jego tytułu, stanu, kategorii i dodatkowych informacji";
    static final String APIURL = "https://api.openai.com/v1/chat/completions";

    @Override
    public String generateDescription(String title, String condition, String category, String additionalData) throws IOException, InterruptedException, DescriptionGenerationException {
        String gptKey = EnvGetter.getGptApikey();
        String offerData = title + ", " + condition + ", " + category + ", " + additionalData;
        String requestBody = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \" " + PROMPT + offerData + "\"}], \"temperature\": 1.0}";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(APIURL)).POST(HttpRequest.BodyPublishers
                        .ofString(requestBody)).header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + gptKey).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject json = new JSONObject(response.body());
        if (json.isEmpty()) {
            throw new DescriptionGenerationException("Could not generate description");
        } else {
            return json.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
        }
    }

    @Override
    public String generateDescription(String title, String condition, String category) throws IOException, InterruptedException, DescriptionGenerationException {
        String gptKey = EnvGetter.getGptApikey();
        String offerData = title + ", " + condition + ", " + category;
        String requestBody = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \" " + PROMPT + offerData + "\"}], \"temperature\": 1.0}";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(APIURL)).POST(HttpRequest.BodyPublishers
                        .ofString(requestBody)).header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + gptKey).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject json = new JSONObject(response.body());
        if (json.isEmpty()) {
            throw new DescriptionGenerationException("Could not generate description");
        } else {
            return json.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
        }
    }
}
