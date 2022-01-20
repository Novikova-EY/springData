package ru.novikova.hibernate_springboot.controller;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
