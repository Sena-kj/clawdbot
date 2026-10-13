package com.clawdbot.api;

import com.clawdbot.api.dto.TokenRequest;
import com.clawdbot.api.dto.TokenResponse;
import com.clawdbot.security.ApiKeyService;
import com.clawdbot.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestHeader;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final JwtService jwtService;
    private final ApiKeyService apiKeyService;

    public AuthController(JwtService jwtService, ApiKeyService apiKeyService) {
        this.jwtService = jwtService;
        this.apiKeyService = apiKeyService;
    }

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponse createToken(@RequestBody TokenRequest request) {
        String subject = request.getSubject();
        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("subject obrigatorio");
        }
        return new TokenResponse(jwtService.generateToken(subject));
    }

    @PostMapping("/admin/token")
    public ResponseEntity<TokenResponse> createAdminToken(
            @RequestHeader(value = "X-API-KEY", required = false) String apiKey,
            @RequestBody TokenRequest request
    ) {
        if (!apiKeyService.isValid(apiKey)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String subject = request.getSubject();
        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("subject obrigatorio");
        }
        return ResponseEntity.ok(new TokenResponse(jwtService.generateToken(subject)));
    }
}

