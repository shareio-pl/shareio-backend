package org.shareio.backend.infrastructure.dbadapter.adapters;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.shareio.backend.infrastructure.dbadapter.entities.ReviewEntity;
import org.shareio.backend.infrastructure.dbadapter.repositories.ReviewRepository;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class ReviewAdapterTests {

    private AutoCloseable closeable;

    @Mock
    private ReviewRepository reviewRepository;

    private ReviewAdapter reviewAdapter;
    UUID test_fail_user_id;
    UUID test_correct_user_id;

    @BeforeEach
    public void setUp() {
        closeable = openMocks(this);
        test_fail_user_id = UUID.randomUUID();
        test_correct_user_id = UUID.randomUUID();
        reviewAdapter = new ReviewAdapter(reviewRepository);

    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void delete_review_and_throw_NoSuchElementException() {
        when(reviewRepository.findByReviewId(test_fail_user_id)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class,
                () ->reviewAdapter.removeReview(test_fail_user_id));
    }

    @Test
    void delete_review_and_succeed() {
        when(reviewRepository.findByReviewId(test_correct_user_id)).thenReturn(Optional.of(new ReviewEntity()));
        Assertions.assertDoesNotThrow(
                () ->reviewAdapter.removeReview(test_correct_user_id));
        verify(reviewRepository, times(1)).delete(any());
    }

}
