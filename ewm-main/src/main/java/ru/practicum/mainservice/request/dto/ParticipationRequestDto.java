package ru.practicum.mainservice.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.mainservice.request.model.Status;

import static ru.practicum.mainservice.utils.Constants.DATE_TIME_PATTERN;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ParticipationRequestDto {

    Integer id;

    @JsonFormat(pattern = DATE_TIME_PATTERN)
    @DateTimeFormat(pattern = DATE_TIME_PATTERN)
    String created;

    Integer event;

    Integer requester;

    Status status;
}
