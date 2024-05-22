package org.shareio.backend;


public class EnvGetter {


    public static String getImage() {
        if (System.getenv("IMAGE_ADDRESS").isBlank()) {
            throw new RuntimeException("Could not load service addressed from envs!");

        } else {
            return System.getenv("IMAGE_ADDRESS");
        }
    }
}