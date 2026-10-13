package com.clawdbot.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Component
public class RateLimitFilter extends OncePerRequestFilter {
    private final boolean enabled;
    private final int maxRequests;
    private final long windowSeconds;
    private final Set<String> whitelist;
    private final Map<String, WindowCounter> counters = new ConcurrentHashMap<>();

    public RateLimitFilter(
            @Value("${clawdbot.rate-limit.enabled:false}") boolean enabled,
            @Value("${clawdbot.rate-limit.max-requests:120}") int maxRequests,
            @Value("${clawdbot.rate-limit.window-seconds:60}") long windowSeconds,
            @Value("${clawdbot.rate-limit.whitelist:}") String whitelist
    ) {
        this.enabled = enabled;
        this.maxRequests = maxRequests;
        this.windowSeconds = windowSeconds;
        this.whitelist = parseWhitelist(whitelist);
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        if (!enabled) {
            filterChain.doFilter(request, response);
            return;
        }

        String key = request.getRemoteAddr();
        if (whitelist.contains(key)) {
            filterChain.doFilter(request, response);
            return;
        }
        WindowCounter counter = counters.computeIfAbsent(key, ignored -> new WindowCounter());
        if (!counter.tryAcquire(maxRequests, windowSeconds)) {
            response.setStatus(HttpServletResponse.SC_TOO_MANY_REQUESTS);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private static class WindowCounter {
        private long windowStartEpoch;
        private final AtomicInteger count = new AtomicInteger(0);

        synchronized boolean tryAcquire(int maxRequests, long windowSeconds) {
            long now = Instant.now().getEpochSecond();
            if (now - windowStartEpoch >= windowSeconds) {
                windowStartEpoch = now;
                count.set(0);
            }
            return count.incrementAndGet() <= maxRequests;
        }
    }

    private Set<String> parseWhitelist(String value) {
        if (value == null || value.isBlank()) {
            return Set.of();
        }
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(item -> !item.isBlank())
                .collect(Collectors.toSet());
    }
}

