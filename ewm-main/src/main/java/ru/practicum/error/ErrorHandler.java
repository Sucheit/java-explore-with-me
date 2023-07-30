package ru.practicum.error;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.practicum.error.exception.BadRequestException;
import ru.practicum.error.exception.InternalServerErrorException;
import ru.practicum.error.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Arrays;

import static ru.practicum.utils.Constants.DATE_TIME_FORMATTER;

@ControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable e) {
        return ApiError.builder()
                .errors(Arrays.toString(e.getStackTrace()))
                .reason("Server error.")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .timestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(final NotFoundException e) {
        return ApiError.builder()
                .errors(Arrays.toString(e.getStackTrace()))
                .message(e.getMessage())
                .reason("The required object was not found.")
                .status(HttpStatus.NOT_FOUND.toString())
                .timestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleBadRequestException(final BadRequestException e) {
        return ApiError.builder()
                .errors(Arrays.toString(e.getStackTrace()))
                .message(e.getMessage())
                .reason("Incorrectly made request.")
                .status(HttpStatus.BAD_REQUEST.toString())
                .timestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(final DataIntegrityViolationException e) {
        return ApiError.builder()
                .errors(Arrays.toString(e.getStackTrace()))
                .message(e.getMessage())
                .reason("Incorrectly made request.")
                .status(HttpStatus.CONFLICT.toString())
                .timestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handlerMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return ApiError.builder()
                .errors(String.format("Field: %s. Error: %s. Value: %s",
                        e.getFieldError().getField(), e.getMessage(), e.getFieldError().getRejectedValue()))
                .message(e.getMessage())
                .reason("Incorrectly made request.")
                .status(HttpStatus.CONFLICT.toString())
                .timestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER))
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleInternalServerErrorException(final InternalServerErrorException e) {
        return ApiError.builder()
                .errors(Arrays.toString(e.getStackTrace()))
                .message(e.getMessage())
                .reason("Internal server error.")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .timestamp(LocalDateTime.now().format(DATE_TIME_FORMATTER))
                .build();
    }
}
