package org.shareio.backend.security;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@NoArgsConstructor
@Service
public class ProfileHandler {
    public boolean checkTestProfile(String activeProfile) {
        return Objects.equals(activeProfile, "test");
    }

    public boolean checkProdProfile(String activeProfile) {
        return Objects.equals(activeProfile, "prod");
    }
}
