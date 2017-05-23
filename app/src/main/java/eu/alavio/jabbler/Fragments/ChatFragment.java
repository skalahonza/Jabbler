package eu.alavio.jabbler.Fragments;


import android.app.Fragment;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.packet.Message;

import butterknife.ButterKnife;
import eu.alavio.jabbler.Models.API.ApiHandler;
import eu.alavio.jabbler.Models.API.ChatHistoryManager;
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
    private Chat chat;
    private ChatHistoryManager historyManager;

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
            try {
                historyManager = new ChatHistoryManager(ApiHandler.getCurrentUser());
            } catch (SmackException.NotConnectedException | XMPPException.XMPPErrorException | SmackException.NoResponseException e) {
                Log.e(ChatFragment.class.getName(), "Error getting current user.", e);
                e.printStackTrace();
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

        //load old messages
        chatArrayAdapter.addAll(historyManager.getMessagesFrom(chatPartner));

        chatArrayAdapter.setNotifyOnChange(true);
        vMessagesListView.setAdapter(chatArrayAdapter);

        //INIT CHAT
        chat = ApiHandler.initChat(chatPartner);
        Handler handler = new Handler();
        if (chat != null)
            chat.addMessageListener((chat1, message) -> {

                //Call on UI thread
                handler.post(() -> {
                    receivedMessage(ChatMessage.ReceivedMessage(message));
                });
            });
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
            Message message = new Message(chatPartner.getJid());
            message.setBody(String.valueOf(vChatMessageBox.getText()));
            try {
                message.setFrom(ApiHandler.getCurrentUser().getJid());
            } catch (SmackException.NotConnectedException | XMPPException.XMPPErrorException | SmackException.NoResponseException e) {
                Log.e(ChatFragment.class.getName(), "Error getting current user info", e);
                e.printStackTrace();
            }
            ChatMessage chatMessage = ChatMessage.ToBeSendMessage(message);
            try {
                chat.sendMessage(message);
                historyManager.saveMessage(chatMessage);

            } catch (SmackException.NotConnectedException e) {
                Log.e(ChatFragment.class.getName(), "Connection lost during sending of a message", e);
                Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
            chatArrayAdapter.add(chatMessage);
            vChatMessageBox.setText("");
        }
    }

    private void receivedMessage(ChatMessage message) {
        chatArrayAdapter.add(message);
        chatArrayAdapter.notifyDataSetChanged();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String ringtonePreference = prefs.getString("notificationSound", "DEFAULT_NOTIFICATION_URI ");
        Uri ringtoneuri = Uri.parse(ringtonePreference);
        Ringtone r = RingtoneManager.getRingtone(getActivity(), ringtoneuri);
        r.play();
    }
}
