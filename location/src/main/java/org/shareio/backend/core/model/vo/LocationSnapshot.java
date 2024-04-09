package org.shareio.backend.core.model.vo;

public record LocationSnapshot(Double latitude, Double longitude) {
    public LocationSnapshot(Location location) {
        this(location.getLatitude(), location.getLongitude());
    }
}
