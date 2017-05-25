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

import java.util.List;

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
    private String chatPartnerJid;
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
        return newInstance(chatPartner.getJid());
    }

    public static ChatFragment newInstance(String partnerJID) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM, partnerJID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            try {
                chatPartnerJid = getArguments().getString(ARG_PARAM);
                chatPartner = ApiHandler.getContact(chatPartnerJid);

            } catch (SmackException.NotConnectedException | XMPPException.XMPPErrorException | SmackException.NoResponseException | SmackException.NotLoggedInException e) {
                Log.e(ChatFragment.class.getName(), "Error getting contact by jid", e);
            }
            historyManager = new ChatHistoryManager(getActivity());
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vChatMessageBox = ButterKnife.findById(view, R.id.msg);
        vMessagesListView = ButterKnife.findById(view, R.id.msgview);
        vChatHeader = ButterKnife.findById(view, R.id.chat_partner_name);

        if (chatPartner != null)
            vChatHeader.setText(chatPartner.getName());

            //partner not in contacts
        else
            vChatHeader.setText(chatPartnerJid);

        vChatMessageBox.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                sendMessage();
                return true;
            }
            return false;
        });

        //Adapter init
        chatArrayAdapter = new ChatArrayAdapter(getActivity(), R.layout.right);

        chatArrayAdapter.setNotifyOnChange(true);
        vMessagesListView.setAdapter(chatArrayAdapter);

        //load old messages
        List<ChatMessage> messages = historyManager.getMessagesWith(chatPartnerJid);
        messages.forEach(message -> chatArrayAdapter.add(message));

        //INIT CHAT
        chat = ApiHandler.initChat(chatPartnerJid);
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
            Message message = new Message(chatPartnerJid);
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
        historyManager.saveMessage(message);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String ringtonePreference = prefs.getString("notificationSound", "DEFAULT_NOTIFICATION_URI ");
        Uri ringtoneuri = Uri.parse(ringtonePreference);
        Ringtone r = RingtoneManager.getRingtone(getActivity(), ringtoneuri);
        r.play();
    }
}
