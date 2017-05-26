package eu.alavio.jabbler.Models.Adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
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
    private List<HistoryItem> historyItems = new ArrayList<>();

    public HistoryItemsAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
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

    /**
     * Build view for item
     *
     * @param position    Position of item
     * @param convertView Default view
     * @param parent      Parent ViewGroup
     * @return Built view
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        HistoryItem chatMessageObj = getItem(position);
        return chatMessageObj.getView(this.getContext(), parent);
    }
}
