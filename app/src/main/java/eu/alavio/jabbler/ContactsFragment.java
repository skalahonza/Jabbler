package eu.alavio.jabbler;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {

    @BindView(R.id.tabHost)
    TabHost tabHost;

    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.bind(this, view);

        tabHost.setup();

        TabHost.TabSpec spec1 = tabHost.newTabSpec("Tab 1");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("Tab 1");

        TabHost.TabSpec spec2 = tabHost.newTabSpec("Tab 2");
        spec2.setIndicator("Tab 2");
        spec2.setContent(R.id.tab2);

        tabHost.addTab(spec1);
        tabHost.addTab(spec2);
        return view;
    }
}
