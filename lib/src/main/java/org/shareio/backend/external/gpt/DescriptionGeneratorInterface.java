package org.shareio.backend.external.gpt;

import org.shareio.backend.exceptions.DescriptionGenerationException;
import org.shareio.backend.exceptions.ExternalServiceException;

import java.io.IOException;

public interface DescriptionGeneratorInterface {
    String generateDescription(String title, String condition, String category, String additionalData) throws ExternalServiceException, IOException, InterruptedException, DescriptionGenerationException;
}
