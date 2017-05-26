package eu.alavio.jabbler.Models.Helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
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
     * Validates email using special regex
     * @param emailStr Email to be validated
     * @return True if email is valid
     */
    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

    /**
     * Validates username using special regex
     *
     * @param username Username to be validated
     * @return True if valid
     */
    public static boolean validateUsername(String username){
        Matcher matcher = VALID_USERNAME.matcher(username);
        return matcher.find();
    }

    /**
     * Checks if the network is available
     * @param context Obtain by getActivity();
     * @return True if the network is available
     */
    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    /**
     * Check if the app can connect to the internet
     * @param context Obtain by getActivity();
     * @return True if the internet is reachable
     */
    public static boolean isInternetAccessible(Context context) {
        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Log.e("Internet connection", "Couldn't check internet connection", e);
            }
        } else {
            Log.d("Internet connection", "Internet not available!");
        }
        return false;
    }
}
