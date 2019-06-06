package com.utstar.integral.Exception;

/**
 * created by UTSC1244 at 2019/5/10 0010
 */
public class LoginException extends RuntimeException{
    public LoginException() {
        super();
    }

    public LoginException(String message) {
        super(message);
    }
}
