package ru.practicum.users.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewUserRequestDto {
    @NotNull
    @NotBlank
    @Length(min = 2, max = 250)
    String name;
    @NotNull
    @Email
    @Length(min = 6, max = 254)
    String email;
}
