package eu.alavio.jabbler.Fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.alavio.jabbler.Models.API.ChatHistoryManager;
import eu.alavio.jabbler.Models.API.ChatMessage;
import eu.alavio.jabbler.Models.Adapters.HistoryItemsAdapter;
import eu.alavio.jabbler.R;
import eu.alavio.jabbler.ViewModels.ChatItem;
import eu.alavio.jabbler.ViewModels.DayLine;


/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    @BindView(R.id.history_list)
    ListView vHistoryList;

    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ChatHistoryManager manager = new ChatHistoryManager(getActivity());
        List<Date> dates = manager.getDays();

        //Adapter init
        HistoryItemsAdapter adapter = new HistoryItemsAdapter(getActivity(), R.layout.item_chat);
        adapter.setNotifyOnChange(true);

        vHistoryList.setAdapter(adapter);

        //load old messages
        dates.forEach(date -> {
            adapter.add(new DayLine(date));
            List<ChatMessage> messages = manager.getMessagesFromDay(date);
            adapter.add(new ChatItem(messages.get(messages.size() - 1)));
        });
    }
}
