package com.clawdbot.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApiKeyService {
    private final String apiKey;

    public ApiKeyService(@Value("${clawdbot.api.api-key:}") String apiKey) {
        this.apiKey = apiKey;
    }

    public boolean isEnabled() {
        return apiKey != null && !apiKey.isBlank();
    }

    public boolean isValid(String providedKey) {
        return isEnabled() && apiKey.equals(providedKey);
    }
}




