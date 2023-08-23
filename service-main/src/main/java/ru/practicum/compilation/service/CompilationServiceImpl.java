package ru.practicum.compilation.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.MapperCompilation;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationDto;
import ru.practicum.compilation.models.Compilation;
import ru.practicum.compilation.storage.CompilationRepository;
import ru.practicum.events.service.EventService;
import ru.practicum.exceptions.CompilationNotFoundException;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.compilation.dto.MapperCompilation.mapToDto;
import static ru.practicum.compilation.dto.MapperCompilation.mapToEntity;
import static ru.practicum.utility.Constant.getPage;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompilationServiceImpl implements CompilationService {
    final EventService eventService;
    final CompilationRepository compRepo;

    @Override
    @Transactional
    public CompilationDto createNewCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = mapToEntity(newCompilationDto);
        checkAndSetEventList(newCompilationDto.getEvents(), compilation);
        return mapToDto(compRepo.save(compilation));
    }

    @Override
    public CompilationDto getCompilationById(long compilationId) {
        return mapToDto(compRepo.findById(compilationId)
                .orElseThrow(() -> new CompilationNotFoundException("compilation by ID:" + compilationId + "not found")));
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(long compilationId, UpdateCompilationDto updateDto) {
        Compilation toUpdateCompilation = checkValidCompilation(compilationId);
        titleUpdate(updateDto, toUpdateCompilation);
        pinnedUpdate(updateDto, toUpdateCompilation);
        checkAndSetEventList(updateDto.getEvents(), toUpdateCompilation);
        return MapperCompilation.mapToDto(compRepo.save(toUpdateCompilation));
    }

    @Override
    @Transactional
    public void deleteCompilation(long compilationId) {
        compRepo.deleteById(compilationId);
    }

    @Override
    public List<CompilationDto> getAllPinnedCompilation(Boolean pinned, int from, int size) {
        if (pinned != null) {
            return getDtoList(compRepo.findAllByPinned(pinned, getPage(from, size)));
        } else {
            return getDtoList(compRepo.findAll(getPage(from, size)).getContent());
        }
    }

    private List<CompilationDto> getDtoList(List<Compilation> compRepo) {
        return compRepo
                .stream()
                .map(MapperCompilation::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Compilation checkValidCompilation(long compilationId) {
        return compRepo.findById(compilationId)
                .orElseThrow(() -> new CompilationNotFoundException("not found compilation by ID:" + compilationId));
    }

    private void checkAndSetEventList(List<Long> list, Compilation compilation) {
        if (list != null) {
            compilation.setEvents(list
                    .stream()
                    .map(eventService::checkValidEvent)
                    .collect(Collectors.toSet()));
        } else {
            compilation.setEvents(Collections.emptySet());
        }
    }

    private static void pinnedUpdate(UpdateCompilationDto updateDto, Compilation compilation) {
        if (updateDto.getPinned() != null) {
            compilation.setPinned(updateDto.getPinned());
        }
    }

    private static void titleUpdate(UpdateCompilationDto updateDto, Compilation compilation) {
        if (updateDto.getTitle() != null) {
            compilation.setTitle(updateDto.getTitle());
        }
    }
}
