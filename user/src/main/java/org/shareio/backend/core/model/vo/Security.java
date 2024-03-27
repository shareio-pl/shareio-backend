package org.shareio.backend.core.model.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Security {
    private String pwHash;
    private LocalDateTime registrationDate;
    private LocalDateTime lastLoginDate;
}
