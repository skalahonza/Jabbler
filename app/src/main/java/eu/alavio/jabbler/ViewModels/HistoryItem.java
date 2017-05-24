package eu.alavio.jabbler.ViewModels;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

/**
 * Interface, defining methods that are called by HistoryItemsArrayAdapter on history fragment
 */

public interface HistoryItem {
    View getView(Context context, ViewGroup container);
}
