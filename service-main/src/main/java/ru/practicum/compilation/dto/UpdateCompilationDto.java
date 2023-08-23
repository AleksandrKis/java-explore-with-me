package ru.practicum.compilation.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateCompilationDto {
    @Length (min = 1, max = 50)
    String title;
    Boolean pinned;
    List<Long> events;
}