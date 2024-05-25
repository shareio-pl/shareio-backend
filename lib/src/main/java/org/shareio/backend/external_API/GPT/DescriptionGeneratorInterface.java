package org.shareio.backend.external_API.GPT;

import org.shareio.backend.exceptions.DescriptionGenerationException;

import java.io.IOException;

public interface DescriptionGeneratorInterface {
    String generateDescription(String title, String condition, String category, String additionalData) throws IOException, InterruptedException, DescriptionGenerationException;
    String generateDescription(String title, String condition, String category) throws IOException, InterruptedException, DescriptionGenerationException;
}
