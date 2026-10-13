package com.clawdbot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class StartupValidationRunner implements ApplicationRunner {
    private static final Logger logger = LoggerFactory.getLogger(StartupValidationRunner.class);

    private final boolean enabled;
    private final boolean strict;

    public StartupValidationRunner(
            @Value("${clawdbot.validation.enabled:true}") boolean enabled,
            @Value("${clawdbot.validation.strict:true}") boolean strict
    ) {
        this.enabled = enabled;
        this.strict = strict;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!enabled) {
            return;
        }

        List<String> missing = new ArrayList<>();
        requireEnv(missing, "BINANCE_API_KEY");
        requireEnv(missing, "BINANCE_SECRET_KEY");
        requireEnv(missing, "CRYPTOCOMPARE_API_KEY");
        requireEnv(missing, "AI_MODEL_API_KEY");
        requireEnv(missing, "JWT_SECRET");
        requireEnv(missing, "API_KEY");

        if (!missing.isEmpty()) {
            String message = "Variaveis de ambiente ausentes: " + String.join(", ", missing);
            if (strict) {
                throw new IllegalStateException(message);
            }
            logger.warn(message);
        }
    }

    private void requireEnv(List<String> missing, String key) {
        String value = System.getenv(key);
        if (value == null || value.isBlank()) {
            missing.add(key);
        }
    }
}




