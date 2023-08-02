package ru.practicum.mainservice.compilation.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.mainservice.compilation.dto.CompilationDto;
import ru.practicum.mainservice.compilation.dto.NewCompilationDto;
import ru.practicum.mainservice.compilation.dto.UpdateCompilationRequest;
import ru.practicum.mainservice.compilation.model.Compilation;
import ru.practicum.mainservice.compilation.repository.CompilationRepository;
import ru.practicum.mainservice.error.exception.NotFoundException;
import ru.practicum.mainservice.event.dto.EventShortDto;
import ru.practicum.mainservice.event.mapper.EventMapper;
import ru.practicum.mainservice.event.model.Event;
import ru.practicum.mainservice.event.repository.EventRepository;
import ru.practicum.mainservice.event.service.EventService;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.mainservice.compilation.mapper.CompilationMapper.mapToCompilation;
import static ru.practicum.mainservice.compilation.mapper.CompilationMapper.mapToCompilationDto;
import static ru.practicum.mainservice.utils.Utility.getPageRequest;

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

    @Transactional(readOnly = true)
    private List<EventShortDto> getEventShortDtoList(Compilation compilation) {
        return compilation.getEvents().stream()
                .map(event -> eventService.findEventFullDtoById(event.getId()))
                .map(EventMapper::mapEventFullDtoToEventShortDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public CompilationDto getCompilationDtoById(int compId) {
        Compilation compilation = getCompilationById(compId);
        return mapToCompilationDto(compilation, getEventShortDtoList(compilation));
    }

    @Transactional(readOnly = true)
    private Compilation getCompilationById(int compId) {
        return compilationRepository.findById(compId).orElseThrow(
                () -> new NotFoundException(String.format("Compilation with id=%s was not found", compId)));
    }

    @Transactional
    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = eventRepository.findByIdIn(newCompilationDto.getEvents());
        Compilation compilation = mapToCompilation(newCompilationDto, events);
        compilationRepository.save(compilation);
        return mapToCompilationDto(compilation, getEventShortDtoList(compilation));
    }

    @Transactional
    @Override
    public void deleteCompilation(int comId) {
        getCompilationById(comId);
        compilationRepository.deleteById(comId);
    }

    @Transactional
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
