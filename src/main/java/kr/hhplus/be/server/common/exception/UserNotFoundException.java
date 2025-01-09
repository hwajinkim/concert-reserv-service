package kr.hhplus.be.server.common.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(String message){super(message);}
}
