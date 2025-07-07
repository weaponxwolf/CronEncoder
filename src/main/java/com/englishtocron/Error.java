package com.englishtocron;

public class Error extends Exception {
    private String type;
    private String state;
    private String token;
    private String value;
    private String description;

    private Error(String type, String state, String token, String value, String description) {
        this.type = type;
        this.state = state;
        this.token = token;
        this.value = value;
        this.description = description;
    }

    public static Error invalidInput() {
        return new Error("InvalidInput", null, null, null, null);
    }

    public static Error capture(String state, String token) {
        return new Error("Capture", state, token, null, null);
    }

    public static Error parseToNumber(String state, String value) {
        return new Error("ParseToNumber", state, null, value, null);
    }

    public static Error incorrectValue(String state, String error) {
        return new Error("IncorrectValue", state, null, null, error);
    }

    @Override
    public String getMessage() {
        switch (type) {
            case "InvalidInput":
                return "Please enter human readable";
            case "Capture":
                return String.format("Could not capture: %s in state: %s", token, state);
            case "ParseToNumber":
                return String.format("Could not parse: %s to number. state: %s", value, state);
            case "IncorrectValue":
                return String.format("value is invalid in state: %s. description: %s", state, description);
            default:
                return "An unknown error occurred";
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Error other = (Error) obj;
        return type.equals(other.type) &&
               java.util.Objects.equals(state, other.state) &&
               java.util.Objects.equals(token, other.token) &&
               java.util.Objects.equals(value, other.value) &&
               java.util.Objects.equals(description, other.description);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(type, state, token, value, description);
    }
}