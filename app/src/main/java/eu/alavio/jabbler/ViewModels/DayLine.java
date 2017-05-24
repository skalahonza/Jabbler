package eu.alavio.jabbler.ViewModels;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import eu.alavio.jabbler.R;

/**
 * Wrapper for day line separator used in history fragment list view
 */

public class DayLine implements HistoryItem {
    private Date day;

    public DayLine(Date day) {
        this.day = day;
    }

    @Override
    public View getView(Context context, ViewGroup container) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.item_dayline, container, false);

        TextView vDayBox = ButterKnife.findById(row, R.id.day_box);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        vDayBox.setText(simpleDateFormat.format(day));
        return row;
    }
}
