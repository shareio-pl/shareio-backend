package org.shareio.backend.external.gpt;

import org.shareio.backend.exceptions.DescriptionGenerationException;

import java.io.IOException;

public interface DescriptionGeneratorInterface {
    String generateDescription(String title, String condition, String category, String additionalData) throws IOException, InterruptedException, DescriptionGenerationException;
}
