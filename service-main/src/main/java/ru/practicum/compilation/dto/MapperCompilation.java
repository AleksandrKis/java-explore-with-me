package ru.practicum.compilation.dto;

import ru.practicum.compilation.models.Compilation;
import ru.practicum.events.dto.EventMapperDto;

import java.util.Collections;
import java.util.stream.Collectors;

public class MapperCompilation {
    public static CompilationDto mapToDto(Compilation compilation) {
        return CompilationDto.builder().id(compilation.getId())
                .events(compilation.getEvents() != null ? compilation.getEvents().stream()
                        .map(EventMapperDto::mapToShortEventDto).collect(Collectors.toList())
                        : Collections.emptyList())
                .pinned(compilation.getPinned()).title(compilation.getTitle()).build();
    }

    public static Compilation mapToEntity(NewCompilationDto newDto) {
        return Compilation.builder()
                .pinned(newDto.getPinned() != null ? newDto.getPinned() : false)
                .title(newDto.getTitle()).build();
    }
}