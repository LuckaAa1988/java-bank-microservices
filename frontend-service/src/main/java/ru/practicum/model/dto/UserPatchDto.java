package ru.practicum.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserPatchDto {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate birthDate;
}
