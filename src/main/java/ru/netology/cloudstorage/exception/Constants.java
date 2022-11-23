package ru.netology.cloudstorage.exception;

public enum Constants {
    USER_NOT_FOUND("User not found");

    private String title;

    Constants(String title) {
        this.title = title;
    }

    public String get() {
        return title;
    }
}
