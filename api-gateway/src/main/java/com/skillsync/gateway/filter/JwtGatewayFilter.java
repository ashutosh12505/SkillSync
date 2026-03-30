package com.skillsync.gateway.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.nio.charset.StandardCharsets;

@Component
public class JwtGatewayFilter extends AbstractGatewayFilterFactory<JwtGatewayFilter.Config> {

    private final String secret;

    public JwtGatewayFilter(
            @Value("${jwt.secret:mysecretkeymysecretkeymysecretkey12345}") String secret) {
        super(Config.class);
        this.secret = secret;
    }

    public static class Config {
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            String path = exchange.getRequest().getURI().getPath();

            String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = authHeader.substring(7);

            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String email = claims.getSubject();
                String role = claims.get("role", String.class);

                // ROLE-BASED ACCESS CONTROL

                // ADMIN only
                if (path.contains("/admin") && !role.equals("ROLE_ADMIN")) {
                    exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }

                // USERS endpoint - both allowed
                if (path.contains("/users") &&
                        !(role.equals("ROLE_ADMIN") || role.equals("ROLE_LEARNER"))) {
                    exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }

                //  FORWARD HEADERS
                ServerWebExchange mutatedExchange = exchange.mutate()
                        .request(builder -> builder
                                .header("X-User-Email", email)
                                .header("X-User-Role", role)
                        )
                        .build();

                return chain.filter(mutatedExchange);

            } catch (Exception e) {
                exchange.getResponse().setStatusCode(org.springframework.http.HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
        };
    }
}