package eu.alavio.jabbler.Models.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import eu.alavio.jabbler.Models.API.Friend;
import eu.alavio.jabbler.R;

/**
 * Adapter for displaying contacts (friends)
 */

public class ContactAdapter extends ArrayAdapter<Friend> {

    public ContactAdapter(Context context, List<Friend> contacts) {
        super(context, 0, contacts);
    }

    public ContactAdapter(Context context) {
        super(context, R.layout.item_contact);
    }

    @NonNull
    @Override
    /** Build view for item
     * @param position Position of item
     * @param convertView Default view
     * @param parent Parent ViewGroup
     * @return Built view
     */
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Friend contact = getItem(position);

        //If view not loaded yet
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_contact, parent, false);
        }

        TextView vNickname = ButterKnife.findById(convertView, R.id.name);
        TextView vUsername = ButterKnife.findById(convertView, R.id.username);

        if (contact != null) {
            vNickname.setText(contact.getName());
            vUsername.setText(contact.getJid());
        }
        return convertView;
    }
}
