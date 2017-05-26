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
import eu.alavio.jabbler.Models.API.Friend;
import eu.alavio.jabbler.Models.Adapters.ContactAdapter;
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
    @BindView(R.id.favourite_contacts)
    ListView vFavouriteListView;
    @BindView(R.id.no_messages)
    TextView vNoMessagesBox;
    @BindView(R.id.more_history)
    FloatingActionButton vMoreHistory;

    HistoryItemsAdapter historyItemsAdapter;
    ContactAdapter favContactAdapter;

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
        historyItemsAdapter = new HistoryItemsAdapter(getActivity(), R.layout.item_chat);
        historyItemsAdapter.setNotifyOnChange(true);
        vChatFeed.setAdapter(historyItemsAdapter);
        ChatHistoryManager manager = new ChatHistoryManager(getActivity());
        List<ChatMessage> messages = manager.getLatestMessages(3);
        if (messages.isEmpty()) {
            //No messages
            vNoMessagesBox.setVisibility(View.VISIBLE);
            vMoreHistory.setVisibility(View.GONE);
        } else
            messages.forEach(message -> historyItemsAdapter.add(new ChatItem(message)));

        //Favourite contacts
        List<Friend> favourites = manager.determineFavouriteContacts(5);
        favContactAdapter = new ContactAdapter(getActivity());
        if (favourites.size() > 0) {
            favContactAdapter.addAll(favourites);
            //Init favourite contacts list
            vFavouriteListView.setAdapter(favContactAdapter);
        }
    }

    @OnClick(R.id.more_history)
    void moreHistory() {
        NavigationService.getInstance().Navigate(NavigationService.MainPages.HISTORY, true, getFragmentManager());
    }

    @OnItemClick(R.id.chat_feed)
    void onChatFeedClick(int position) {
        HistoryItem item = historyItemsAdapter.getItem(position);
        if (item instanceof ChatItem) {
            ChatItem tmp = (ChatItem) item;
            Fragment chatFragment = ChatFragment.newInstance(tmp.getMessage().getPartner_JID());
            NavigationService.getInstance().Navigate(chatFragment, true, getFragmentManager());
        }
    }

    @OnItemClick(R.id.favourite_contacts)
    void onFavouriteClick(int position) {
        Friend contact = favContactAdapter.getItem(position);
        if (contact != null) {
            NavigationService.getInstance().Navigate(ContactDetailFragment.getInstance(contact.getJid()), true, getFragmentManager());
        }
    }
}
