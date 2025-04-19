package org.example.fishtank.exception.message;

/**
 * Enum containing entity types and providing standardized error messages.
 */
public enum Message {

    ACCESS,
    APP_USER,
    FISH,
    POST,
    SEX,
    WATER_TYPE;

    /**
     * Returns a standardized "not found" message for the selected entity type.
     * @return A string message indicating that the entity was not found in the database.
     */
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
