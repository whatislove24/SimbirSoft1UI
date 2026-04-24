package utils;

import java.time.Duration;

public final class TestConfig {

    private TestConfig() {
    }

    public static final String BASE_URL = "https://automationteststore.com/";
    public static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(15);
}