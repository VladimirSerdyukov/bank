package ru.testservice.demo.exceptions;

public class NoUuidInMemory extends Exception{
    public NoUuidInMemory() {
        super("Такого UUID нет в базе данных");
    }
}
