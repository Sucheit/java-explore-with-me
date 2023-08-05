package ru.practicum.mainservice.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class Constants {

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(DATE_TIME_PATTERN);

    public static final LocalDateTime START_DATE = LocalDateTime.of(2023, 1, 1, 0, 0, 0);

    public static final LocalDateTime END_DATE = LocalDateTime.of(2024, 1, 1, 0, 0, 0);
}
