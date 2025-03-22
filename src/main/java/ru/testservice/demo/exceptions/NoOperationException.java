package ru.testservice.demo.exceptions;

public class NoOperationException extends Exception{

    public NoOperationException() {
        super("Не указан тип операции");
    }
}
