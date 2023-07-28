package ru.practicum.compilation.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;
import ru.practicum.error.exception.NotFoundException;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.event.service.EventService;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.compilation.mapper.CompilationMapper.mapToCompilation;
import static ru.practicum.compilation.mapper.CompilationMapper.mapToCompilationDto;
import static ru.practicum.utils.Utility.getPageRequest;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CompilationServiceImpl implements CompilationService {

    CompilationRepository compilationRepository;

    EventService eventService;

    EventRepository eventRepository;

    @Transactional(readOnly = true)
    @Override
    public List<CompilationDto> getCompilations(boolean pinned, int from, int size) {
        List<Compilation> compilations = compilationRepository.findByPinned(pinned, getPageRequest(from, size));
        return compilations.stream()
                .map(compilation -> mapToCompilationDto(compilation, getEventShortDtoList(compilation)))
                .collect(Collectors.toList());
    }

    private List<EventShortDto> getEventShortDtoList(Compilation compilation) {
        return compilation.getEvents().stream()
                .map(event -> eventService.findEventFullDtoById(event.getId()))
                .map(EventMapper::mapEventFullDtoToEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationDtoById(int compId) {
        Compilation compilation = getCompilationById(compId);
        return mapToCompilationDto(compilation, getEventShortDtoList(compilation));
    }

    private Compilation getCompilationById(int compId) {
        return compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException(String.format("Compilation with id=%s was not found", compId)));
    }

    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = eventRepository.findByIdIn(newCompilationDto.getEvents());
        Compilation compilation = mapToCompilation(newCompilationDto, events);
        return mapToCompilationDto(compilation, getEventShortDtoList(compilation));
    }

    @Override
    public void deleteCompilation(int comId) {
        getCompilationById(comId);
        compilationRepository.deleteById(comId);
    }

    @Override
    public CompilationDto patchCompilation(int comId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = getCompilationById(comId);
        if (updateCompilationRequest.getEvents() != null) {
            List<Event> events = eventRepository.findByIdIn(updateCompilationRequest.getEvents());
            compilation.setEvents(events);
        }
        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }
        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }
        compilationRepository.save(compilation);
        return mapToCompilationDto(compilation, getEventShortDtoList(compilation));
    }
}
