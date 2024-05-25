package org.shareio.backend.external_API.GPT;

import org.json.JSONObject;
import org.shareio.backend.EnvGetter;
import org.shareio.backend.exceptions.DescriptionGenerationException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DescriptionGenerator implements DescriptionGeneratorInterface {
    final String prompt = "Wygeneruj opis przedmiotu oddawanego za darmo na podstawie jego tytułu, stanu, kategorii i dodatkowych informacji";
    final String APIUrl = "https://api.openai.com/v1/chat/completions";

    @Override
    public String generateDescription(String title, String condition, String category, String additionalData) throws IOException, InterruptedException, DescriptionGenerationException {
        String GPTKey = EnvGetter.getAPIKey();
        String offerData = title + ", " + condition + ", " + category + ", " + additionalData;
        String requestBody = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \" " + this.prompt + offerData + "\"}], \"temperature\": 1.0}";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(this.APIUrl)).POST(HttpRequest.BodyPublishers
                        .ofString(requestBody)).header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + GPTKey).build();
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
        String GPTKey = EnvGetter.getAPIKey();
        String offerData = title + ", " + condition + ", " + category;
        String requestBody = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \" " + this.prompt + offerData + "\"}], \"temperature\": 1.0}";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(this.APIUrl)).POST(HttpRequest.BodyPublishers
                        .ofString(requestBody)).header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + GPTKey).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject json = new JSONObject(response.body());
        if (json.isEmpty()) {
            throw new DescriptionGenerationException("Could not generate description");
        } else {
            return json.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
        }
    }
}
