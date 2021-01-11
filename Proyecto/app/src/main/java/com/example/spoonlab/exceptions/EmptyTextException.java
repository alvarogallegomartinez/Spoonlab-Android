package com.example.spoonlab.exceptions;

public class EmptyTextException extends Exception {
    public EmptyTextException() {
        super("All the text boxes should be filled");
    }
}
