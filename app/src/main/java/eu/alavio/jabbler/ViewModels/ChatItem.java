package eu.alavio.jabbler.ViewModels;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.text.SimpleDateFormat;

import butterknife.ButterKnife;
import eu.alavio.jabbler.Models.API.ApiHandler;
import eu.alavio.jabbler.Models.API.ChatMessage;
import eu.alavio.jabbler.Models.API.Friend;
import eu.alavio.jabbler.R;

/**
 * Wrapper for displaying chat message in history fragment list view
 */

public class ChatItem implements HistoryItem {

    private ChatMessage message;

    public ChatItem(ChatMessage message) {
        this.message = message;
    }

    public ChatMessage getMessage() {
        return message;
    }

    @Override
    public View getView(Context context, ViewGroup container) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.item_chat, container, false);

        TextView vSenderBox = ButterKnife.findById(row, R.id.sender_box);
        TextView vSenderJidBox = ButterKnife.findById(row, R.id.sender_jid_box);
        TextView vMessageBox = ButterKnife.findById(row, R.id.message_box);
        TextView vDateBox = ButterKnife.findById(row, R.id.date_box);

        try {
            Friend contact = ApiHandler.getContact(message.getPartner_JID());
            if (contact != null) {
                vSenderBox.setText(contact.getName());
            } else {
                vSenderBox.setText("Unknown");
            }
        } catch (SmackException.NotConnectedException | XMPPException.XMPPErrorException | SmackException.NoResponseException | SmackException.NotLoggedInException e) {
            Log.e(this.getClass().getName(), "Error getting contact", e);
            vSenderBox.setText("Error");
        }

        vSenderJidBox.setText(message.getPartner_JID());
        vMessageBox.setText(message.getMessage().getBody());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy \n HH:MM");
        vDateBox.setText(sdf.format(message.getTimestamp()));
        return row;
    }
}
