package ru.practicum.mapper;

import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;
import ru.practicum.model.dto.UserRegistrationDto;
import ru.practicum.model.dto.UserResponse;
import ru.practicum.model.entity.User;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T20:16:56+0300",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.1 (Amazon.com Inc.)"
)
@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public User fromDto(UserRegistrationDto userRegistrationDto) {
        if ( userRegistrationDto == null ) {
            return null;
        }

        User.UserBuilder user = User.builder();

        user.username( userRegistrationDto.getUsername() );
        user.firstName( userRegistrationDto.getFirstName() );
        user.lastName( userRegistrationDto.getLastName() );
        user.email( userRegistrationDto.getEmail() );
        user.password( userRegistrationDto.getPassword() );
        user.birthDate( userRegistrationDto.getBirthDate() );

        return user.build();
    }

    @Override
    public UserResponse toDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserResponse.UserResponseBuilder userResponse = UserResponse.builder();

        userResponse.username( user.getUsername() );
        userResponse.firstName( user.getFirstName() );
        userResponse.lastName( user.getLastName() );
        userResponse.email( user.getEmail() );
        userResponse.birthDate( user.getBirthDate() );

        return userResponse.build();
    }
}
