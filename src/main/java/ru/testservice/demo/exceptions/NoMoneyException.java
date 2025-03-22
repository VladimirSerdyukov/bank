package ru.testservice.demo.exceptions;


public class NoMoneyException extends Exception{

    public NoMoneyException(){
        super("На счете не достаточно средств");
    }
}
