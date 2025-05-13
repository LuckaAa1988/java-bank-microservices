package ru.practicum.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationEntryPoint;
import reactor.core.publisher.Mono;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityFilterChain(ServerHttpSecurity http) {
        return http
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/actuator/**",
                                "/api/auth/**",
                                "/login",
                                "/registration",
                                "/keycloak/**").permitAll()
                        .anyExchange().authenticated()
                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((exchange, ex) -> {
                            if (exchange.getRequest().getPath().toString().startsWith("/api")) {
                                return Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));
                            }
                            return new RedirectServerAuthenticationEntryPoint("/oauth2/authorization/keycloak")
                                    .commence(exchange, ex);
                        })
                )
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .oauth2Login(withDefaults())
                .oauth2Client(withDefaults())
                .cors(withDefaults())
                .oauth2ResourceServer(serverSpec -> serverSpec
                        .jwt(jwtSpec -> {
                            ReactiveJwtAuthenticationConverter jwtAuthenticationConverter =
                                    new ReactiveJwtAuthenticationConverter();
                            jwtSpec.jwtAuthenticationConverter(jwtAuthenticationConverter);
                        }))
                .build();
    }
}
