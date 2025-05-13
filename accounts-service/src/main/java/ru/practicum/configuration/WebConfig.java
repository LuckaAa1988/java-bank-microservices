package ru.practicum.configuration;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig {

    @Bean
    public ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2Client(
            ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {

        var oauth2 = new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        oauth2.setDefaultClientRegistrationId("accounts-service");
        return oauth2;
    }

    @Bean
    public WebClient webClient(ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2Client) {
        return WebClient.builder()
                .baseUrl("http://api-gateway:8090")
                .filter(oauth2Client)
                .build();
    }

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl("http://keycloak:8080")
                .realm("Bank-app")
                .clientId("accounts-service")
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientSecret("wXm9jO5pvKvDcIvheI6HRrVJgGuZboVS")
                .build();
    }

}
