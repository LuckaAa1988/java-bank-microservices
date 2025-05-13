package ru.practicum.service.impl;

import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.practicum.client.NotificationClient;
import ru.practicum.mapper.UserMapper;
import ru.practicum.model.dto.NotificationDto;
import ru.practicum.model.dto.UserPatchDto;
import ru.practicum.model.dto.UserResponse;
import ru.practicum.repository.UserRepository;
import ru.practicum.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final DatabaseClient databaseClient;
    private final PasswordEncoder passwordEncoder;
    private final NotificationClient notificationClient;
    private final Keycloak keycloak;

    @Override
    public Mono<Boolean> delete(String username) {
        return databaseClient.sql("SELECT a.currency, a.amount FROM accounts AS a " +
                        "LEFT JOIN users AS u ON a.user_id = u.id WHERE u.username = :username AND a.amount != 0")
                .bind("username", username)
                .fetch()
                .all()
                .collectList()
                .flatMap(rows -> {
                    if (rows.isEmpty()) {
                        return userRepository.deleteByUsername(username);
                    } else {
                        return Mono.empty();
                    }
                }).then(Mono.fromRunnable(() -> {
                            UsersResource usersResource = keycloak.realm("Bank-app").users();
                            List<UserRepresentation> users = usersResource.search(username);

                            String userId = users.get(0).getId();

                            usersResource.delete(userId);
                        }
                ));
    }

    @Override
    public Mono<Void> changePassword(String username, String password) {
        var encryptedPassword = passwordEncoder.encode(password);
        return databaseClient.sql("UPDATE users SET password = :encryptedPassword WHERE username = :username")
                .bind("encryptedPassword", encryptedPassword)
                .bind("username", username)
                .fetch()
                .rowsUpdated()
                .then(notificationClient.sendNotification(NotificationDto.builder()
                        .username(username)
                        .data(String.format("Пароль пользователя %s успешно изменен", username))
                        .build()));
    }

    @Override
    public Mono<Void> update(UserPatchDto userPatchDto) {
        var username = userPatchDto.getUsername();
        var firstName = userPatchDto.getFirstName();
        var lastName = userPatchDto.getLastName();
        var email = userPatchDto.getEmail();
        var birthDate = userPatchDto.getBirthDate();
        return databaseClient.sql("UPDATE users " +
                        "SET first_name = :firstName, last_name = :lastName, email = :email, birth_date = :birthDate " +
                        "WHERE username = :username")
                .bind("firstName", firstName)
                .bind("lastName", lastName)
                .bind("email", email)
                .bind("birthDate", birthDate)
                .bind("username", username)
                .fetch()
                .rowsUpdated()
                .then(notificationClient.sendNotification(NotificationDto.builder()
                        .username(username)
                        .data(String.format("Данные пользователя %s успешно изменены", username))
                        .build()));
    }

    @Override
    public Mono<UserResponse> getUser(String username) {
        return userRepository.findByUsername(username).map(userMapper::toDto);
    }
}
