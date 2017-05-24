package eu.alavio.jabbler.Models.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.List;

import eu.alavio.jabbler.ViewModels.HistoryItem;

/**
 * Display history items in listview, such as Day separators and messages
 */

public class HistoryItemsAdapter extends ArrayAdapter<HistoryItem> {
    private List<HistoryItem> contacts = new ArrayList<>();
    private List<HistoryItem> historyItems = new ArrayList<>();

    public HistoryItemsAdapter(Context context, List<HistoryItem> contacts) {
        super(context, 0, contacts);
        this.contacts = contacts;
    }

    @Override
    public void add(HistoryItem object) {
        historyItems.add(object);
        super.add(object);
    }

    public int getCount() {
        return this.historyItems.size();
    }

    public HistoryItem getItem(int index) {
        return this.historyItems.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryItem chatMessageObj = getItem(position);
        return chatMessageObj.getView(this.getContext(), parent);
    }
}
