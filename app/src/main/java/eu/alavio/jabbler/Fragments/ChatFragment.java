package eu.alavio.jabbler.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import butterknife.ButterKnife;
import eu.alavio.jabbler.Models.API.ApiHandler;
import eu.alavio.jabbler.Models.API.ChatMessage;
import eu.alavio.jabbler.Models.API.Friend;
import eu.alavio.jabbler.Models.Adapters.ChatArrayAdapter;
import eu.alavio.jabbler.R;

/**
 * ChatFragment holding the chat UI
 * A simple {@link Fragment} subclass.
 * Use the {@link ChatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatFragment extends Fragment {
    private static final String ARG_PARAM = "jid";

    private Friend chatPartner;
    private View view;

    EditText vChatMessageBox;
    ListView vMessagesListView;
    TextView vChatHeader;
    private ChatArrayAdapter chatArrayAdapter;

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vChatMessageBox = ButterKnife.findById(view, R.id.msg);
        vMessagesListView = ButterKnife.findById(view, R.id.msgview);
        vChatHeader = ButterKnife.findById(view, R.id.chat_partner_name);

        vChatHeader.setText(chatPartner.getName());

        vChatMessageBox.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                sendMessage();
                return true;
            }
            return false;
        });

        //Adapter init
        chatArrayAdapter = new ChatArrayAdapter(getActivity(), R.layout.right);
        vMessagesListView.setAdapter(chatArrayAdapter);

        //TODO INIT CHAT
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat2, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    void sendMessage() {
        if (!String.valueOf(vChatMessageBox.getText()).isEmpty()) {
            chatArrayAdapter.add(ChatMessage.ToBeSendMessage(String.valueOf(vChatMessageBox.getText())));
            chatArrayAdapter.add(ChatMessage.ReceivedMessage(String.valueOf(vChatMessageBox.getText())));
            vChatMessageBox.setText("");
        }
    }
}
