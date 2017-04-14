package eu.alavio.jabbler.Models;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.View;

import eu.alavio.jabbler.API.ApiHandler;
import eu.alavio.jabbler.R;

/**
 * Utility class for presenting common dialogs.
 */

public final class Dialogs {
    /**
     * Shows (Ae you sure you want to logout?) dialog
     *
     * @param context   Typically current activity or frame
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

    public static void userCreatedDialog(Context context, Runnable okPressed) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(R.string.user_registered);
        builder.setNeutralButton(R.string.ok, (dialog, which) -> {
            dialog.dismiss();
            okPressed.run();
            ApiHandler.logout();
        });
        builder.show();
    }

    private static void operationErrorDialog(Context context, String title, String reason, Runnable okPressed) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(reason);
        builder.setNeutralButton(R.string.ok, (dialog, which) -> {
            dialog.dismiss();
            okPressed.run();
            ApiHandler.logout();
        });
        builder.show();
    }

    public static void userNotCreatedDialog(Context context, String reason) {
        operationErrorDialog(context, context.getString(R.string.user_reg_failed), reason, () -> {
        });
    }

    public static void loginFailed(Context context, String reason) {
        operationErrorDialog(context, context.getString(R.string.login_failed), reason, () -> {
        });
    }

    /**
     * Shows or hides the in progress view.
     *
     * @param show true to show false to hide
     * @param view View used for displaying the progress
     */
    public static void operateProgressView(final boolean show, View view) {
        {
            // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
            // for very easy animations. If available, use these APIs to fade-in
            // the progress spinner.
            int shortAnimTime = 200;

            view.setVisibility(show ? View.VISIBLE : View.GONE);
            view.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        }
    }
}
