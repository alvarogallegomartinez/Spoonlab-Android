package com.example.spoonlab.exceptions;

public class DifferentPasswordException extends Exception {
    public DifferentPasswordException() {
        super("Both passwords should be the same");
    }
}
