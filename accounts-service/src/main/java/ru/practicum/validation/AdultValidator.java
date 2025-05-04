package ru.practicum.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

public class AdultValidator implements ConstraintValidator<Adult, LocalDate> {

    private int age;

    @Override
    public void initialize(Adult constraintAnnotation) {
        age = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext constraintValidatorContext) {
        if (birthDate == null) {
            return false;
        }
        LocalDate today = LocalDate.now();
        Period period = Period.between(birthDate, today);
        return period.getYears() >= age;
    }
}
