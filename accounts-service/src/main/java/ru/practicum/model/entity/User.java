package ru.practicum.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
}
