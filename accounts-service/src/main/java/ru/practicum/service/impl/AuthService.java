package ru.practicum.service.impl;

import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.practicum.model.dto.UserRegistrationDto;
import ru.practicum.model.entity.User;
import ru.practicum.repository.UserRepository;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Keycloak keycloak;
    @Value("${keycloak.realmName}")
    private String realmName;

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
                    userRepresentation.setFirstName(user.getFirstName());
                    userRepresentation.setLastName(user.getLastName());
                    userRepresentation.setEmail(user.getEmail());
                    userRepresentation.setUsername(user.getUsername());
                    userRepresentation.setEnabled(true);
                    Response response = keycloak.realm(realmName).users().create(userRepresentation);
                    if (response.getStatus() == 201) {
                        String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");

                        CredentialRepresentation credential = new CredentialRepresentation();
                        credential.setType(CredentialRepresentation.PASSWORD);
                        credential.setValue(userRegistrationDto.getPassword());
                        credential.setTemporary(false);

                        keycloak.realm(realmName)
                                .users()
                                .get(userId)
                                .resetPassword(credential);

                        log.info("{}", user.getUsername());
                    }
                    return Mono.just(user);
                });
    }
}
