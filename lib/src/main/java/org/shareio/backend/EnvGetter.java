package org.shareio.backend;


public class EnvGetter {


    public static String getImage() {
        if (System.getenv("IMAGE_ADDRESS").isBlank()) {
            throw new IllegalStateException("Could not load IMAGE_ADDRESS from envs!");

        } else {
            return System.getenv("IMAGE_ADDRESS");
        }
    }

    public static String getGptApiUrl() {
        if (System.getenv("CHAT_GPT_API_URL").isBlank()) {
            throw new IllegalStateException("Could not load CHAT_GPT_API_URL from envs!");
        } else {
            return System.getenv("CHAT_GPT_API_URL");
        }
    }

    public static String getGptApiKey() {
        if (System.getenv("CHAT_GPT_API_KEY").isBlank()) {
            throw new IllegalStateException("Could not load CHAT_GPT_API_KEY from envs!");
        } else {
            return System.getenv("CHAT_GPT_API_KEY");
        }
    }

    public static String getGptPrompt() {
        if (System.getenv("CHAT_GPT_PROMPT").isBlank()) {
            throw new IllegalStateException("Could not load CHAT_GPT_PROMPT from envs!");
        } else {
            return System.getenv("CHAT_GPT_PROMPT");
        }
    }

    private EnvGetter() {
        throw new IllegalStateException("Utility class");
    }
}