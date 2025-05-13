//package ru.practicum.configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.security.oauth2.client.AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager;
//import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager;
//import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientProviderBuilder;
//import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientService;
//import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
//import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
//import org.springframework.web.reactive.function.client.WebClient;
//
//@Configuration
//public class WebConfig {
//
//    @Bean
//    public ReactiveOAuth2AuthorizedClientManager auth2AuthorizedClientManager(
//            ReactiveClientRegistrationRepository clientRegistrationRepository,
//            ReactiveOAuth2AuthorizedClientService authorizedClientService
//    ) {
//        AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager manager =
//                new AuthorizedClientServiceReactiveOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService);
//
//        manager.setAuthorizedClientProvider(ReactiveOAuth2AuthorizedClientProviderBuilder.builder()
//                .clientCredentials()
//                .refreshToken()
//                .build()
//        );
//
//        return manager;
//    }
//
//    @Bean
//    public ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2Client(
//            ReactiveOAuth2AuthorizedClientManager authorizedClientManager) {
//
//        var oauth2 = new ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
//        oauth2.setDefaultClientRegistrationId("accounts-service");
//        return oauth2;
//    }
//
//    @Bean
//    public WebClient keycloakWebClient(WebClient.Builder builder,
//                                       ServerOAuth2AuthorizedClientExchangeFilterFunction oauth2Client) {
//        return builder
//                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .filter(oauth2Client)
//                .build();
//    }
//}
