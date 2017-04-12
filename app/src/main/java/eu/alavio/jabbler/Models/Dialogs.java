package eu.alavio.jabbler.Models;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import eu.alavio.jabbler.API.ApiHandler;
import eu.alavio.jabbler.R;

/**
 * Utility class for presenting common dialogs.
 */

public final class Dialogs {
    /** Shows (Ae you sure you want to logout?) dialog
     * @param context Typically current activity or frame
     * @param confirmed Method following that runs after dialog confirmation
     */
    public static void logoutDialog(Context context, Runnable confirmed) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.areYouSureLogout);
        builder.setPositiveButton(R.string.yes, (dialog, which) -> {
            dialog.dismiss();
            confirmed.run();
            ApiHandler.logout();
        });
        builder.setNegativeButton(R.string.no, (dialog, which) -> {
            dialog.dismiss();
        });
        builder.show();
    }
}
