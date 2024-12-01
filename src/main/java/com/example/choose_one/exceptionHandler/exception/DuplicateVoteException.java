package com.example.choose_one.exceptionHandler.exception;

public class DuplicateVoteException extends RuntimeException{
    public DuplicateVoteException(String message){
        super(message);
    }
}
