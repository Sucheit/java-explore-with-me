package ru.practicum.statsserver.error;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ApiError {

    String errors;

    String message;

    String reason;

    String status;

    String timestamp;
}
