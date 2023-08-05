package ru.practicum.compilation.service;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationDto;
import ru.practicum.compilation.models.Compilation;

import java.util.List;

public interface CompilationService {
    CompilationDto createNewCompilation(NewCompilationDto newCompilationDto);

    CompilationDto getCompilationById(long compilationId);

    CompilationDto updateCompilation(long compilationId, UpdateCompilationDto updateDto);

    void deleteCompilation(long compilationId);

    List<CompilationDto> getAllPinnedCompilation(Boolean pinned, int from, int size);

    Compilation checkValidCompilation(long compilationId);
}
