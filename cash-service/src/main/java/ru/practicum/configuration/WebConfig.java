package ru.practicum.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig {

    @Value("${spring.application.name}")
    private String appName;
    @Value("${gateway.url}")
    private String gatewayUrl;

    @Bean
    public ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2Client(
            ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {

        var oauth2 = new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        oauth2.setDefaultClientRegistrationId(appName);
        return oauth2;
    }

    @Bean
    public WebClient webClient(ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2Client) {
        return WebClient.builder()
                .baseUrl(gatewayUrl)
                .filter(oauth2Client)
                .build();
    }

}
