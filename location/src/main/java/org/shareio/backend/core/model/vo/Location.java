package org.shareio.backend.core.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.shareio.backend.core.usecases.port.dto.LocationGetDto;

@Getter
@Setter
@AllArgsConstructor
public class Location {
    private Double latitude;
    private Double longitude;

    public static Location fromDto(LocationGetDto locationGetDto) {
        return new Location(locationGetDto.latitude(), locationGetDto.longitude());
    }

    public LocationSnapshot toSnapshot() {
        return new LocationSnapshot(this);
    }
}
