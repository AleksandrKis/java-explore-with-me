package ru.practicum.compilation;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationDto;
import ru.practicum.compilation.service.CompilationService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationController {
    static final String ADMIN = "/admin/compilations";
    static final String PUBLIC = "/compilations";
    static final String COMP_ID = "/{compId}";
    static final String ANSWER = "CompilationController: received request for ";
    final CompilationService service;

    @PostMapping(ADMIN)
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto create(@Valid @RequestBody NewCompilationDto newCompilationDto) {
        log.info(ANSWER + "add new Compilation, from admin by title:{}", newCompilationDto.getTitle());
        return service.createNewCompilation(newCompilationDto);
    }

    @GetMapping(PUBLIC + COMP_ID)
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto getById(@PathVariable long compId) {
        log.info(ANSWER + "get Compilation by ID:{}", compId);
        return service.getCompilationById(compId);
    }

    @PatchMapping(ADMIN + COMP_ID)
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto update(@PathVariable long compId, @Valid @RequestBody UpdateCompilationDto updateDto) {
        log.info(ANSWER + "update Compilation by ID:{}, from admin by title:{}", compId, updateDto.getTitle());
        return service.updateCompilation(compId, updateDto);
    }

    @DeleteMapping(ADMIN + COMP_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable long compId) {
        log.info(ANSWER + "delete Compilation by ID:{}", compId);
        service.deleteCompilation(compId);
    }

    @GetMapping(PUBLIC)
    @ResponseStatus(HttpStatus.OK)
    public List<CompilationDto> getAll(@RequestParam(required = false) Boolean pinned,
                                       @RequestParam(required = false, defaultValue = "0") int from,
                                       @RequestParam(required = false, defaultValue = "10") int size) {
        log.info(ANSWER + "get all Compilations");
        return service.getAllPinnedCompilation(pinned, from, size);
    }
}
