package eu.alavio.jabbler.Models.Helpers;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import eu.alavio.jabbler.Models.API.ApiHandler;
import eu.alavio.jabbler.R;

/**
 * Wrapper (async) for common in app popups
 */

public final class Popups {

    public static void addContactDialog(Context context, Runnable onSuccess) {
        final Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.add_contat_dialog);
        dialog.setTitle("Custom Alert Dialog");

        EditText jid = (EditText) dialog.findViewById(R.id.jid);
        EditText nickname = (EditText) dialog.findViewById(R.id.name);
        FloatingActionButton accept = (FloatingActionButton) dialog.findViewById(R.id.accept);
        FloatingActionButton cancel = (FloatingActionButton) dialog.findViewById(R.id.cancel);

        //Accept clicked
        accept.setOnClickListener(v -> {
            try {
                ApiHandler.addContact(String.valueOf(jid.getText()), String.valueOf(nickname.getText()), null);
                dialog.dismiss();
                onSuccess.run();
            } catch (SmackException.NotLoggedInException | XMPPException.XMPPErrorException | SmackException.NotConnectedException | SmackException.NoResponseException e) {
                Log.e(context.getClass().getName(), "Add contact dialog failed.", e);
                Toast.makeText(context, "Cannot add contact: " + e.getMessage(), Toast.LENGTH_LONG);
            }
        });

        //Cancel clicked
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }
}
