package ru.practicum.service;

import reactor.core.publisher.Mono;
import ru.practicum.model.dto.UserPatchDto;
import ru.practicum.model.dto.UserResponse;

public interface UserService {
    Mono<Boolean> delete(String username);

    Mono<Long> changePassword(String username, String password);

    Mono<Long> update(UserPatchDto userPatchDto);

    Mono<UserResponse> getUser(String username);
}
