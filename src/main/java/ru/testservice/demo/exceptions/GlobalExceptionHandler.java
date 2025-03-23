package ru.testservice.demo.exceptions;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<String> handlerException(InvalidFormatException e) {
        logError(e);
        return ResponseEntity.badRequest().body("Проверьте корректность ввода данных");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handlerException(HttpMessageNotReadableException e){
        logError(e);
        return ResponseEntity.badRequest().body("JSON не валиден");
    }

    @ExceptionHandler(NoMoneyException.class)
    public ResponseEntity<String> handlerException(NoMoneyException e) {
        logError(e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NoOperationException.class)
    public ResponseEntity<String> handlerException(NoOperationException e) {
        logError(e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handlerException(NullPointerException e) {
        logError(e);
        return ResponseEntity.badRequest().body("JSON не валиден");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<String> handlerException(MethodArgumentTypeMismatchException e) {
        logError(e);
        return ResponseEntity.badRequest().body("UUID отсутствует в БД");
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<String> handlerException(NoResourceFoundException e) {
        logError(e);
        return ResponseEntity.badRequest().body("Не корректный запрос");
    }

    @ExceptionHandler(NoUuidInMemory.class)
    public ResponseEntity<String> handlerException(NoUuidInMemory e) {
        logError(e);
        return ResponseEntity.badRequest().body("UUID отсутствует в БД");
    }
    private void logError(Exception e) {
        System.out.println(e.getMessage());
    }

}
