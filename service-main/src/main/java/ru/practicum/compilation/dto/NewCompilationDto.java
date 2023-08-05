package ru.practicum.compilation.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCompilationDto {
    @NotNull @NotBlank @Size(min = 1, max = 50)
    String title;
    Boolean pinned;
    List<Long> events;
}