package eu.alavio.jabbler.Models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Helper class for common routines, like validating email or username
 */

public final class Helper {
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern VALID_USERNAME =
            Pattern.compile("^[a-zA-Z0-9._-]{3,}$", Pattern.CASE_INSENSITIVE);

    /**
     * Validates email using regex
     * */
    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    public static boolean validateUsername(String username){
        Matcher matcher = VALID_USERNAME.matcher(username);
        return matcher.find();
    }
}
