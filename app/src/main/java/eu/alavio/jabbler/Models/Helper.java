package eu.alavio.jabbler.Models;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jan Skála on 12.04.2017.
 */

public final class Helper {
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * Validates email using regex
     * */
    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }
}
