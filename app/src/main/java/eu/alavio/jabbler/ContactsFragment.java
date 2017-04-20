package eu.alavio.jabbler;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.Toast;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.alavio.jabbler.API.ApiHandler;
import eu.alavio.jabbler.API.Friend;
import eu.alavio.jabbler.Models.Popups;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {

    @BindView(R.id.tabHost)
    TabHost tabHost;
    @BindView(R.id.add_contact)
    FloatingActionButton vAddContact;

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

        TabHost.TabSpec spec1 = tabHost.newTabSpec("Favourite");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("Favourite");

        TabHost.TabSpec spec2 = tabHost.newTabSpec("All");
        spec2.setIndicator("All");
        spec2.setContent(R.id.tab2);

        tabHost.addTab(spec1);
        tabHost.addTab(spec2);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadContacts();
    }

    @OnClick(R.id.add_contact)
    void addContact() {
        Popups.addContactDialog(getActivity());
        loadContacts();
    }

    private void loadContacts() {
        try {
            List<Friend> contacts = ApiHandler.getMyContacts();
            return;
        } catch (SmackException.NotLoggedInException e) {
            e.printStackTrace();
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();
        }
    }
}
