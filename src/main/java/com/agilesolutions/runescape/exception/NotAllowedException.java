package com.agilesolutions.runescape.exception;

public class NotAllowedException extends Exception  {
    public NotAllowedException(String message) {
        super("Not Allowed: " + message);
    }
}
