package eu.alavio.jabbler;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import butterknife.ButterKnife;
import eu.alavio.jabbler.API.ApiHandler;
import eu.alavio.jabbler.API.Friend;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactDetailFragment extends Fragment {

    TextView vFullName;
    TextView vJid;
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
        //@BindView(R.id.jid_box)
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
}
