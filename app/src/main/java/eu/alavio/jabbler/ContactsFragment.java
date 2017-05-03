package eu.alavio.jabbler;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
import eu.alavio.jabbler.Models.Dialogs;
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

        //Tabs on top menu (Favourite,All)
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
        registerForContextMenu(vAllContacts);
        vAllContacts.setAdapter(adapter);
        loadContacts();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //All contacts menu
        if (v.getId() == R.id.all_contacts) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.all_contacts_menu, menu);
        }

        //Favourite contacts menu
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            //Delete pressed from all contacts
            case R.id.remove: {
                Friend contact = adapter.getItem(info.position);
                Dialogs.reallyDeleteContact(getActivity(), contact, () -> {
                    try {
                        ApiHandler.removeContact(contact);
                        loadContacts();
                    } catch (SmackException.NotLoggedInException | XMPPException.XMPPErrorException | SmackException.NotConnectedException | SmackException.NoResponseException e) {
                        Log.e(getActivity().getClass().getName(), getString(R.string.error_contact_delete), e);
                        Dialogs.deletingContactFailed(getActivity(), e.getLocalizedMessage());
                    }
                });
                break;
            }
            //View detail
            case R.id.detail: {
                Friend contact = adapter.getItem(info.position);
                ContactDetailFragment fragment = ContactDetailFragment.getInstance(contact.getJid());

                //TODO navigate to fragment
                break;
            }
        }
        return true;
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
        } catch (SmackException.NotLoggedInException | SmackException.NotConnectedException | XMPPException.XMPPErrorException | SmackException.NoResponseException e) {
            Log.e(getActivity().getClass().getName(), "Error loading contacts", e);
        }
    }
}
