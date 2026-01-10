package com.sacreson.tasktracker.api.exceptions;

import com.sacreson.tasktracker.common.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//
//        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
//            // put автоматически перезапишет старое значение, ну или можно тоже склеить
//            errors.put(error.getField(), error.getDefaultMessage());
//        }

        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existing, replacement) -> existing + ", " + replacement //  Если ключ дублируется, склеиваем сообщения через запятую
                ));

        return ResponseEntity.badRequest().body(errors);
    }

    // ловим ResponseStatusException (400, 404, и тд)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorDto> handleResponseStatusException(ResponseStatusException ex) {
        return ResponseEntity
                .status(ex.getStatusCode())
                .body(ErrorDto.builder()
                        .error(ex.getStatusCode().toString())
                        .message(ex.getReason())
                        .build());
    }

//    // ошибка аутификации (неверный пароль)
//    @ExceptionHandler(AuthenticationException.class)
//    public ResponseEntity<ErrorDto> handleAuthException(AuthenticationException ex) {
//        return ResponseEntity
//                .status(HttpStatus.UNAUTHORIZED)
//                .body(ErrorDto.builder()
//                        .error("UNAUTHORIZED")
//                        .message("Неверный логин или пароль") // Пишем понятно
//                        .build());
//    }

    // все остальные ошибки
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleGenericException(Exception ex) {
        ex.printStackTrace(); // Пишем в консоль, чтобы мы видели баг
        return ResponseEntity
                .internalServerError()
                .body(ErrorDto.builder()
                        .error("INTERNAL_SERVER_ERROR")
                        .message("Произошла внутренняя ошибка сервера. Мы уже работаем над этим.")
                        .build());
    }
}
