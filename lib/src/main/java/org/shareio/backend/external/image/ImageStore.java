package org.shareio.backend.external.image;

import org.shareio.backend.Const;
import org.shareio.backend.exceptions.ImageException;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.NoSuchElementException;
import java.util.UUID;

public class ImageStore implements ImageStoreInterface {
    private final String imageServiceUrl;

    public ImageStore(String imageServiceUrl) {
        this.imageServiceUrl = imageServiceUrl;
    }

    @Override
    public void CreateImage(UUID imageId, Resource image) throws ImageException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", image);
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        String serverUrl = imageServiceUrl + "/image/create/";
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;
        try {
            response = restTemplate.postForEntity(serverUrl + imageId, requestEntity, String.class);
        } catch (RestClientException e) {
            throw new ImageException(e.toString());
        }
        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new ImageException("Photo could not be created: " + response.getBody());
        }
    }

    @Override
    public void DeleteImage(UUID imageId) throws NoSuchElementException, ImageException {
        if (imageId != Const.DEFAULT_PHOTO_ID) {
            String serverUrl = imageServiceUrl + "/image/delete/";
            RestTemplate restTemplate = new RestTemplate();
            try {
                restTemplate.delete(serverUrl + imageId, String.class);
            } catch (Exception e) {
                throw new ImageException(e.toString());
            }
            // TODO: check for error 404 and throw NoSuchElementException
        }
    }
}
