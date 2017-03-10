package com.doubleL.werewolf.exception;

/**
 * Created by andreling on 3/5/17.
 */
public class GameException extends Exception {
    public GameException(String msg) {
        super(msg);
    }

    public GameException(String msg, Throwable e) {
        super(msg, e);
    }
}
