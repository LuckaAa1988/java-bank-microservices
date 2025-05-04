package ru.practicum.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.validation.Adult;

import java.time.LocalDate;

@Data
@Builder
public class UserRegistrationDto {
    @NotNull
    @NotBlank
    @Size(min = 2, max = 50)
    private String username;
    @NotNull
    @NotBlank
    @Size(min = 2, max = 50)
    private String firstName;
    @NotNull
    @NotBlank
    @Size(min = 2, max = 100)
    private String lastName;
    @NotNull
    @NotBlank
    @Email
    private String email;
    @NotNull
    @NotBlank
    @Size(min = 6, max = 100)
    private String password;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Adult
    private LocalDate birthDate;
}
