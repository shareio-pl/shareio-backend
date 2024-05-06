package org.shareio.backend;

import lombok.Getter;

@Getter
public class EnvGetter {
    private final String backend;

    public EnvGetter() {
        this.backend = System.getenv("BACKEND_ADDRESS");
        if (this.backend.isBlank()) {
            throw new RuntimeException("Could not load service addressed from envs!");
        }
    }
}