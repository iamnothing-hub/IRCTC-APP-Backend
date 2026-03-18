package com.irctc.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jboss.logging.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class CorrelationIdFilter extends OncePerRequestFilter {
    //* The key used in the log configuration (e.g., in logback.xml)
    private static final String CORRELATION_ID = "correlationID";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        //* 1. Generate a unique ID for this specific request
        String correlationId = UUID.randomUUID().toString();

        //* 2. Put the ID into the MDC (Mapped Diagnostic Context).
        //* Any log statement called during this request will now automatically include this ID.
        MDC.put(CORRELATION_ID, correlationId);

        try {
            //* 3. Add the ID to the HTTP response header so the client/frontend can see it
            response.setHeader("X-Correlation-Id", correlationId);

            //* 4. Continue with the rest of the filters and the actual API logic
            filterChain.doFilter(request, response);
        }
        finally {
            //* 5. IMPORTANT: Clear the MDC after the request is finished.
            //* Since threads are reused in a pool, failing to clear this would "leak"
            //* the ID to a completely different user's request later.
            MDC.clear();
        }
    }
}
