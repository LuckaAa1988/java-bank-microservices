package ru.practicum.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.http.MediaType;
//import org.springframework.security.authentication.ReactiveAuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.UserLoginDto;
import ru.practicum.model.dto.UserRegistrationDto;
import ru.practicum.model.entity.User;
import ru.practicum.repository.UserRepository;

import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final WebClient.Builder webClient;
//    private final ReactiveAuthenticationManager authenticationManager;

    public Mono<User> registration(UserRegistrationDto userRegistrationDto) {
        return userRepository.findByUsername(userRegistrationDto.getUsername())
                .switchIfEmpty(Mono.defer(() -> {
                    User newUser = User.builder()
                            .username(userRegistrationDto.getUsername())
                            .email(userRegistrationDto.getEmail())
                            .firstName(userRegistrationDto.getFirstName())
                            .lastName(userRegistrationDto.getLastName())
                            .birthDate(userRegistrationDto.getBirthDate())
                            .password(passwordEncoder.encode(userRegistrationDto.getPassword()))
                            .build();
                    return userRepository.save(newUser);
                }))
                .flatMap(user -> {
                    UserRepresentation userRepresentation = new UserRepresentation();
                    CredentialRepresentation credential = new CredentialRepresentation();
//                    credential.setType(CredentialRepresentation.PASSWORD);
//                    credential.setValue(user.getPassword());
//                    credential.setTemporary(false);
                    userRepresentation.setFirstName(user.getFirstName());
                    userRepresentation.setLastName(user.getLastName());
                    userRepresentation.setEmail(user.getEmail());
                    userRepresentation.setUsername(user.getUsername());
                    userRepresentation.setEnabled(true);
//                    userRepresentation.setEmailVerified(false);
//                    userRepresentation.setCredentials(List.of(credential));
                    return createUser(userRepresentation)
                            .thenReturn(user);
                });
    }


    public Mono<Void> createUser(UserRepresentation user) {
        return webClient.build().post()
                                .uri("http://keycloak:8080/admin/realms/Bank-app/users")
                                .bodyValue(user)
                                .retrieve().bodyToMono(Void.class);
    }

//    public Mono<Authentication> login(UserLoginDto userLoginDto) {
//            return webClient.build().post()
//                    .uri("http://keycloak:8080/realms/Bank-app/protocol/openid-connect/token")
//                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                    .body(BodyInserters.fromFormData("grant_type", "password")
//                            .with("client_id", "accounts-service")
//                            .with("username", userLoginDto.getUsername())
//                            .with("password", userLoginDto.getPassword()))
//                    .retrieve()
//                    .bodyToMono(JsonNode.class)
//                    .map(tokenResponse -> {
//                        String token = tokenResponse.get("access_token").asText();
//                        return new UsernamePasswordAuthenticationToken(
//                                userLoginDto.getUsername(),
//                                token,
//                                List.of()
//                        );
//                    });
//    }
}
