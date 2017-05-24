package eu.alavio.jabbler.ViewModels;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import butterknife.ButterKnife;
import eu.alavio.jabbler.Models.API.ChatMessage;
import eu.alavio.jabbler.R;

/**
 * Wrapper for displaying chat message in history fragment list view
 */

public class ChatItem implements HistoryItem {

    private ChatMessage message;

    public ChatItem(ChatMessage message) {
        this.message = message;
    }

    @Override
    public View getView(Context context, ViewGroup container) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.item_chat, container, false);

        TextView vSenderBox = ButterKnife.findById(row, R.id.sender_box);
        TextView vMessageBox = ButterKnife.findById(row, R.id.message_box);
        TextView vDateBox = ButterKnife.findById(row, R.id.date_box);

        vSenderBox.setText(message.getPartner_JID());
        vMessageBox.setText(message.getMessage().getBody());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:MM");
        vDateBox.setText(sdf.format(message.getTimestamp()));
        return row;
    }
}
