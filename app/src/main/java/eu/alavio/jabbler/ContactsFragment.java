package eu.alavio.jabbler;


import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.alavio.jabbler.API.ApiHandler;
import eu.alavio.jabbler.API.Friend;
import eu.alavio.jabbler.Models.Adapters.ContactAdapter;
import eu.alavio.jabbler.Models.Popups;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment {

    @BindView(R.id.tabHost)
    TabHost tabHost;
    @BindView(R.id.add_contact)
    FloatingActionButton vAddContact;
    @BindView(R.id.all_contacts)
    ListView vAllContacts;

    List<Friend> allContacts = new ArrayList<>();
    ContactAdapter adapter;

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
        adapter = new ContactAdapter(getActivity(), allContacts);
        vAllContacts.setAdapter(adapter);
        loadContacts();
    }

    @OnClick(R.id.add_contact)
    void addContact() {
        Popups.addContactDialog(getActivity(), this::loadContacts);
    }

    private void loadContacts() {
        try {
            adapter.clear();
            allContacts = ApiHandler.getMyContacts();
            adapter.addAll(allContacts);
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
