package ru.practicum.model.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
public class UserResponse {
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
}
