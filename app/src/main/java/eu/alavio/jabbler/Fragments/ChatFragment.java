package eu.alavio.jabbler.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import butterknife.ButterKnife;
import eu.alavio.jabbler.Models.API.ApiHandler;
import eu.alavio.jabbler.Models.API.Friend;
import eu.alavio.jabbler.R;

/**
 * ChatFragment holding the chat UI
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    private static final String ARG_PARAM = "jid";

    Friend chatPartner;
    private View view;

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment newInstance(Friend chatPartner) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, chatPartner.getJid());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            try {
                chatPartner = ApiHandler.getContact(getArguments().getString(ARG_PARAM));
            } catch (SmackException.NotConnectedException | XMPPException.XMPPErrorException | SmackException.NoResponseException e) {
                Log.e(ChatFragment.class.getName(), "Error getting contact by jid", e);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat2, container, false);
        ButterKnife.bind(this, view);
        return view;
    }
}
