package org.shareio.backend.external.image;

import org.shareio.backend.exceptions.ImageException;
import org.springframework.core.io.Resource;

import java.util.UUID;

public interface ImageStoreInterface {
    void CreateImage(UUID imageId, Resource image) throws ImageException;
    void DeleteImage(UUID imageId) throws ImageException;
}
