package org.shareio.backend.external.gpt;

import org.json.JSONObject;
import org.shareio.backend.EnvGetter;
import org.shareio.backend.exceptions.DescriptionGenerationException;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;

public class SearchImprover implements SearchImproverInterface {
    @Override
    public List<String> generatePotentialWords(String input) throws IOException, InterruptedException, DescriptionGenerationException {
        String gptUrl = EnvGetter.getGptApiUrl();
        String gptKey = EnvGetter.getGptApiKey();
        String gptPrompt = "Wygeneruj wyrazy podobne do podanego, aby ulepszyć wyszukiwanie: ";
        String requestBody = "{\"model\": \"gpt-3.5-turbo\", \"messages\": [{\"role\": \"user\", \"content\": \" " + gptPrompt + " " + input + "\"}], \"temperature\": 1.0}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(gptUrl))
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + gptKey)
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject json = new JSONObject(response.body());
        if (json.isEmpty()) {
            throw new DescriptionGenerationException("Could not determine similar words");
        } else {
            String content = json.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");

            return Arrays.asList(content.split(",\\s*|\\s+"));
        }
    }
}
