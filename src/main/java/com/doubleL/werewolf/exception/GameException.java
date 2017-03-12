package com.doubleL.werewolf.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by andreling on 3/5/17.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class GameException extends Exception {
    public GameException(String msg) {
        super(msg);
    }

    public GameException(String msg, Throwable e) {
        super(msg, e);
    }
}
