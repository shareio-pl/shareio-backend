package org.shareio.backend.core.usecases.port.in;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface SearchOffersUseCaseInterface {
    List<UUID> getOfferListMeetingCriteria(
            UUID userId,
            String title,
            String category,
            String condition,
            Double distance,
            Double score,
            LocalDate creationDate,
            String sortType
    );
}

