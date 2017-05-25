package eu.alavio.jabbler.Models.Helpers;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.function.Consumer;

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
                if (!ApiHandler.addContact(String.valueOf(jid.getText()), String.valueOf(nickname.getText()), null)) {
                    Dialogs.userNotCreatedDialog(context, context.getString(R.string.contact_already_added));
                }
                dialog.dismiss();
                onSuccess.run();
            } catch (Exception e) {
                Log.e(context.getClass().getName(), "Add contact dialog failed.", e);
                dialog.dismiss();
                Toast.makeText(context, "Cannot add contact: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        //Cancel clicked
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    public static void contactRequestReceived(Context context, String jid, Consumer<String> confirm) {
        final Dialog dialog = new Dialog(context);

        dialog.setContentView(R.layout.contact_request);
        dialog.setTitle("Custom Alert Dialog");

        TextView jidTb = (TextView) dialog.findViewById(R.id.jid);
        EditText nickname = (EditText) dialog.findViewById(R.id.name);
        FloatingActionButton accept = (FloatingActionButton) dialog.findViewById(R.id.accept);
        FloatingActionButton cancel = (FloatingActionButton) dialog.findViewById(R.id.cancel);

        jidTb.setText(jid);

        //Accept clicked
        accept.setOnClickListener(v -> {
            confirm.accept(String.valueOf(nickname.getText()));
            dialog.dismiss();
        });

        //Cancel clicked
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }
}
