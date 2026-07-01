package be.parcial.exceptions;

public final class Messages {

    private Messages() {
    }

    public static final String DUMMY_NOT_FOUND = "Dummy not found with id: %s";
    public static final String USER_NOT_FOUND = "User not found with email: %s";
    public static final String USER_ALREADY_EXISTS = "User already exists with email: %s";
    public static final String INVALID_CREDENTIALS = "Invalid email or password";
    public static final String REFRESH_TOKEN_NOT_FOUND = "Refresh token not found";
    public static final String REFRESH_TOKEN_EXPIRED = "Refresh token has expired";
    public static final String REFRESH_TOKEN_REVOKED = "Refresh token has been revoked";
    public static final String UNAUTHORIZED = "You are not authorized to access this resource";
    public static final String VALIDATION_ERROR = "Validation error";
}
