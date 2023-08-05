package ru.practicum.categories.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ToString
public class NewCategoryRequestDto {
    @NotNull
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
}
