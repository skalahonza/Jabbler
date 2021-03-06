package eu.alavio.jabbler.Models.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import eu.alavio.jabbler.Models.API.ChatMessage;
import eu.alavio.jabbler.R;

public class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {

    private TextView chatText;
    private List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
    private TextView timeBox;

    @Override
    public void add(ChatMessage object) {
        chatMessageList.add(object);
        super.add(object);
    }

    public ChatArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public int getCount() {
        return this.chatMessageList.size();
    }

    /**
     * Get item from collection
     *
     * @param index Index of item
     * @return Item at index
     */
    public ChatMessage getItem(int index) {
        return this.chatMessageList.get(index);
    }

    /**
     * Build view for item
     *
     * @param position    Position of item
     * @param convertView Default view
     * @param parent      Parent ViewGroup
     * @return Built view
     */
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage chatMessageObj = getItem(position);
        View row;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (chatMessageObj.isReceived()) {
            row = inflater.inflate(R.layout.right, parent, false);
        } else {
            row = inflater.inflate(R.layout.left, parent, false);
        }
        chatText = ButterKnife.findById(row, R.id.msgr);
        timeBox = ButterKnife.findById(row, R.id.time_box);
        chatText.setText(chatMessageObj.getMessage().getBody());

        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        timeBox.setText(df.format(chatMessageObj.getTimestamp()));
        return row;
    }
}