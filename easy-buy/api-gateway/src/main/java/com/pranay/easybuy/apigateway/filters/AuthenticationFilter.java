package com.pranay.easybuy.apigateway.filters;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;


@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    Logger logger = LoggerFactory.getLogger(this.getClass());


    // 1. Changed to SecretKey
    private final SecretKey secretKey;

    public AuthenticationFilter(@Value("${jwt.secret}") String secretString) {
        // 2. You can hardcode Config.class here to simplify instantiation
        super(Config.class);

        // 3. Explicitly defined the UTF-8 charset for absolute consistency across environments
        this.secretKey = Keys.hmacShaKeyFor(secretString.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public GatewayFilter apply(Config config) {
        //logic for token varification:

        //this is our logic for now:
        //public url --> allow
        //api/products--> GET [public]
        //api/users/login
        //api/users/ -->[POST]
        //api/privacy-policy
        //admin url --> admin role
        //api/products--> POST [admin]
        //guest/user url--> GUEST/USER role
        //api/carts -->GET [GUEST]
        //api/orders/checkout--POST [GUEST/ADMIN]


        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
//            /api/users/login
            String path = request.getURI().getPath();
//            POST
            String method = request.getMethod().name();

            logger.info("path  {}", path);
            logger.info("method  {}", method);

            // STEP 1: Bypass security check for public endpoints
            if (isPublicEndpoint(path, method)) {
                return chain.filter(exchange);
            }

            // STEP 2: Extract the "Authorization" header
            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return onError(exchange, "Missing or Invalid Authorization Header", HttpStatus.UNAUTHORIZED);
            }

            // STEP 3: Verify the JWT token
            String token = authHeader.substring(7); // Remove the "Bearer " prefix

            try {
                Claims claims = Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parseSignedClaims(token)
                        .getPayload();

                String tokenUserId = String.valueOf(claims.get("id"));
                String role = String.valueOf(claims.get("role"));

                logger.info("Extracted from token - userId: {}, role: {}", tokenUserId, role);

                // STEP 4: Ensure the user has a valid recognized role in the system
                if (!isValidRole(role)) {
                    return onError(exchange, "Forbidden: Invalid User Role", HttpStatus.FORBIDDEN);
                }

                // STEP 5: Enforce Admin-only endpoints
                if (isAdminOnlyEndpoint(path, method) && !"ROLE_ADMIN".equalsIgnoreCase(role)) {
                    return onError(exchange, "Forbidden: Admin access required", HttpStatus.FORBIDDEN);
                }

                // STEP 6: Enforce Resource Ownership (Self-Access check for USER and GUEST)
                if (isUserOrGuest(role)) {
                    String targetUserId = extractUserIdFromPath(path);

                    // If path is a user resource, ensure it matches the user owning the token
//                    urluserid=== tokenUserId
                    if (targetUserId != null && !targetUserId.equalsIgnoreCase(tokenUserId)) {
                        return onError(exchange, "Forbidden: You cannot access another user's data", HttpStatus.FORBIDDEN);
                    }
                }

                // STEP 7: Propagate verified user details as headers to downstream microservices
                ServerHttpRequest mutatedRequest = request.mutate()
                        .header("X-User-Id", tokenUserId)
                        .header("X-User-Email", claims.getSubject())
                        .header("X-User-Role", role)
                        .build();

                return chain.filter(exchange.mutate().request(mutatedRequest).build());

            } catch (Exception e) {
                logger.error("Token validation failed: {}", e.getMessage(), e);
                return onError(exchange, "Unauthorized: Invalid or expired token", HttpStatus.UNAUTHORIZED);
            }
        };
    }

    /**
     * Helper to verify if an endpoint is public (can be accessed without a token).
     */
    //api/users/login--POST
    private boolean isPublicEndpoint(String path, String method) {
        return path.contains("/public/") ||
                path.contains("/api/users/login") ||
                path.contains("/api/users/refresh") ||
                (path.contains("/api/users") && "POST".equalsIgnoreCase(method)) || // User registration
                (path.contains("/api/products") && "GET".equalsIgnoreCase(method)) || // View products
                (path.contains("/api/categories") && "GET".equalsIgnoreCase(method)) || // View categories
                (path.contains("/api/reviews") && "GET".equalsIgnoreCase(method)); // View reviews
        //public mentions
    }

    /**
     * Helper to check if a role is a valid role supported by easybuy.
     */
    private boolean isValidRole(String role) {
        return "ROLE_ADMIN".equalsIgnoreCase(role) ||
                "ROLE_USER".equalsIgnoreCase(role) ||
                "ROLE_GUEST".equalsIgnoreCase(role);
    }

    /**
     * Helper to check if a user belongs to non-admin customer roles.
     */
    private boolean isUserOrGuest(String role) {
        return "ROLE_USER".equalsIgnoreCase(role) || "ROLE_GUEST".equalsIgnoreCase(role);
    }

    /**
     * Helper to check if the route requires Admin permissions.
     */
    private boolean isAdminOnlyEndpoint(String path, String method) {
        // 1. Updating role mapping
        if (path.contains("/api/users/change-role")) return true;

        // 2. Querying list of all users (exclude single profile path `/api/users/123-uuid`)
        if (path.contains("/api/users") && "GET".equalsIgnoreCase(method) && !path.matches(".*/api/users/[a-fA-F0-9-]+"))
            return true;

        // 3. Modifying catalog (POST/PUT/DELETE products, categories, reviews)
        if ((path.contains("/api/products") || path.contains("/api/categories") || path.contains("/api/reviews")) && !"GET".equalsIgnoreCase(method))
            return true;

        // 4. Modifying inventory details
        if (path.contains("/api/inventories") && (method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("PUT") || method.equalsIgnoreCase("DELETE") || method.equalsIgnoreCase("PATCH")))
            return true;

        return false;
    }

    /**
     * Safely extracts the target userId from paths of different microservices.
     * Supports:
     * - Carts: /api/carts/{userId}/**
     * - Orders: /api/orders/user/{userId}/** and /api/orders/{userId}/checkout
     * - Users: /api/users/{userId}
     */
    private String extractUserIdFromPath(String path) {
        String[] prefixes = {"/api/carts/", "/api/orders/user/", "/api/orders/", "/api/users/"};

        for (String prefix : prefixes) {
            int index = path.indexOf(prefix);
            if (index != -1) {
                String sub = path.substring(index + prefix.length());

                // If it ends with '/checkout' (e.g. /api/orders/{userId}/checkout) remove it
                if (sub.endsWith("/checkout")) {
                    sub = sub.replace("/checkout", "");
                }

                // Extract segment before next slash if nested (e.g. /api/carts/{userId}/items)
                int slashIndex = sub.indexOf("/");
                String extractedId = (slashIndex != -1) ? sub.substring(0, slashIndex) : sub;

                // Avoid returning static endpoints as userIds
                if (extractedId.equals("login") || extractedId.equals("refresh") || extractedId.equals("change-role")) {
                    continue;
                }
                return extractedId;
            }
        }
        return null;
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.writeAndFlushWith(body -> Mono.just("Internal Server Error: " + err));
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    public static class Config {
    }
}
