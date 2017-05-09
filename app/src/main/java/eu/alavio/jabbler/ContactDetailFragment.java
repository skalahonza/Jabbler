package eu.alavio.jabbler;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.alavio.jabbler.API.ApiHandler;
import eu.alavio.jabbler.API.Friend;
import eu.alavio.jabbler.Models.Dialogs;
import eu.alavio.jabbler.Models.NavigationService;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactDetailFragment extends Fragment {

    EditText vFullName, vJid;
    View view;

    Friend contact;

    /**
     * Params: [tpye:contact/user] [data:FriendObject/UserObject]
     */
    public ContactDetailFragment() {
        // Required empty public constructor
    }

    public static ContactDetailFragment getInstance(String jid) {
        ContactDetailFragment fragment = new ContactDetailFragment();
        Bundle args = new Bundle();
        args.putString("jid", jid);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_contact_detail, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        vFullName = ButterKnife.findById(view, R.id.full_name);
        vJid = ButterKnife.findById(view, R.id.jid_box);

        //Get contact to be displayed
        Bundle bundle = getArguments();
        try {
            contact = ApiHandler.getContact(bundle.getString("jid"));
        } catch (SmackException.NotConnectedException | XMPPException.XMPPErrorException | SmackException.NoResponseException e) {
            Log.e(getActivity().getClass().getName(), "Displaying contact error", e);
        }

        //Fill in UI
        vFullName.setText(contact.getName());
        vJid.setText(contact.getJid());
    }

    @OnClick(R.id.delete_button)
    void deleteContact() {
        Dialogs.reallyDeleteContact(getActivity(), () -> {
            try {
                ApiHandler.removeContact(contact);
                NavigationService.getInstance().goBack(getFragmentManager());
            } catch (SmackException.NotLoggedInException | XMPPException.XMPPErrorException | SmackException.NotConnectedException | SmackException.NoResponseException e) {
                Log.e(getActivity().getClass().getName(), "Removing contact failed", e);
                Dialogs.deletingContactFailed(getActivity(), e.getLocalizedMessage());
            }
        });
    }

    @OnClick(R.id.chat_button)
    void initiateChat() {
        //TODO Implement this
        Toast.makeText(getActivity(), "Start chat", Toast.LENGTH_LONG).show();
    }
}
