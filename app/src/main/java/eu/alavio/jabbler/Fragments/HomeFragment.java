package eu.alavio.jabbler.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import eu.alavio.jabbler.Models.API.ChatHistoryManager;
import eu.alavio.jabbler.Models.API.ChatMessage;
import eu.alavio.jabbler.Models.Adapters.HistoryItemsAdapter;
import eu.alavio.jabbler.Models.Helpers.NavigationService;
import eu.alavio.jabbler.R;
import eu.alavio.jabbler.ViewModels.ChatItem;
import eu.alavio.jabbler.ViewModels.HistoryItem;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    @BindView(R.id.chat_feed)
    ListView vChatFeed;
    @BindView(R.id.no_messages)
    TextView vNoMessagesBox;
    @BindView(R.id.more_history)
    FloatingActionButton vMoreHistory;

    HistoryItemsAdapter adapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new HistoryItemsAdapter(getActivity(), R.layout.item_chat);
        adapter.setNotifyOnChange(true);
        vChatFeed.setAdapter(adapter);
        ChatHistoryManager manager = new ChatHistoryManager(getActivity());
        List<ChatMessage> messages = manager.getLatestMessages(2);
        if (messages.isEmpty()) {
            //No messages
            vNoMessagesBox.setVisibility(View.VISIBLE);
            vMoreHistory.setVisibility(View.GONE);
        } else
            messages.forEach(message -> adapter.add(new ChatItem(message)));
    }

    @OnClick(R.id.more_history)
    void moreHistory() {
        NavigationService.getInstance().Navigate(NavigationService.MainPages.HISTORY, false, getFragmentManager());
    }

    @OnItemClick(R.id.chat_feed)
    void onChatFeedClick(int position) {
        HistoryItem item = adapter.getItem(position);
        if (item instanceof ChatItem) {
            ChatItem tmp = (ChatItem) item;
            Fragment chatFragment = ChatFragment.newInstance(tmp.getMessage().getPartner_JID());
            NavigationService.getInstance().Navigate(chatFragment, true, getFragmentManager());
        }
    }
}
