package org.example.fishtank.exception.message;

public enum Message {

    ACCESS,
    APP_USER,
    FISH,
    POST,
    SEX,
    WATER_TYPE;

    public String notFound() {

        return switch(this){
            case ACCESS -> "Access not found in the database.";
            case APP_USER -> "AppUser not found in the database.";
            case FISH -> "Fish not found in the database.";
            case POST -> "Post not found in the database.";
            case SEX -> "Sex not found in the database.";
            case WATER_TYPE -> "Water type not found in the database.";
        };
    }
}
