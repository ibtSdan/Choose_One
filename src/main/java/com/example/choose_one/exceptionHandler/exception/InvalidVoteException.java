package com.example.choose_one.exceptionHandler.exception;

public class InvalidVoteException extends RuntimeException{
    public InvalidVoteException(String message){
        super(message);
    }
}
