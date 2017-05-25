package eu.alavio.jabbler.Models.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.function.Consumer;

import butterknife.ButterKnife;
import eu.alavio.jabbler.R;
import eu.alavio.jabbler.ViewModels.ContactRequest;

/**
 * Contact requests array adapter used in ContactsFragment
 */

public class ContactRequestArrayAdapter extends ArrayAdapter<ContactRequest> {

    private Consumer<String> accept;
    private Consumer<String> reject;

    public ContactRequestArrayAdapter(@NonNull Context context, @LayoutRes int resource, Consumer<String> accept, Consumer<String> reject) {
        super(context, resource);
        this.accept = accept;
        this.reject = reject;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ContactRequest contact = getItem(position);

        //If view not loaded yet
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.contact_request_item, parent, false);
        }

        TextView jidBox = ButterKnife.findById(convertView, R.id.jid_contact_box);
        Button acceptButton = ButterKnife.findById(convertView, R.id.accept);
        Button denyBt = ButterKnife.findById(convertView, R.id.deny);

        if (contact != null) {
            jidBox.setText(contact.getContact().getJid());

            acceptButton.setOnClickListener(v -> accept.accept(contact.getContact().getJid()));
            denyBt.setOnClickListener(v -> reject.accept(contact.getContact().getJid()));
        }
        return convertView;
    }
}
