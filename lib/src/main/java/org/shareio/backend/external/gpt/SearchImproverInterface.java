package org.shareio.backend.external.gpt;

import org.shareio.backend.exceptions.DescriptionGenerationException;

import java.io.IOException;
import java.util.List;

public interface SearchImproverInterface {
    List<String> generatePotentialWords(String input) throws DescriptionGenerationException, IOException, InterruptedException;
}
