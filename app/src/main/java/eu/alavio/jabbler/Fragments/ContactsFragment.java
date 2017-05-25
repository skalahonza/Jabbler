package eu.alavio.jabbler.Fragments;


import android.app.Fragment;
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
import java.util.function.Consumer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import eu.alavio.jabbler.Models.API.ApiHandler;
import eu.alavio.jabbler.Models.API.Friend;
import eu.alavio.jabbler.Models.Adapters.ContactAdapter;
import eu.alavio.jabbler.Models.Adapters.ContactRequestArrayAdapter;
import eu.alavio.jabbler.Models.Helpers.Dialogs;
import eu.alavio.jabbler.Models.Helpers.NavigationService;
import eu.alavio.jabbler.Models.Helpers.Popups;
import eu.alavio.jabbler.R;
import eu.alavio.jabbler.ViewModels.ContactRequest;


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
    @BindView(R.id.requests_list)
    ListView vRequests;

    //used as a base for filtering
    List<Friend> allContacts = new ArrayList<>();
    ContactAdapter allContactsAdapter;
    ContactRequestArrayAdapter contactRequestArrayAdapter;

    public ContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.bind(this, view);

        //Tabs on top menu (Requests,All)
        tabHost.setup();
        TabHost.TabSpec spec1 = tabHost.newTabSpec("Requests");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("Requests");
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
        allContactsAdapter = new ContactAdapter(getActivity(), allContacts);

        Consumer<String> acceptContact = s -> {
            try {
                ApiHandler.updateContact(s, s.split("@")[0]);
                loadContacts();
            } catch (SmackException.NotConnectedException | SmackException.NotLoggedInException | SmackException.NoResponseException | XMPPException.XMPPErrorException e) {
                Log.e(ContactsFragment.class.getName(), "Error accepting contact: " + s, e);
            }
        };

        Consumer<String> rejectContact = s -> {
            try {
                ApiHandler.removeContact(s);
                loadContacts();
            } catch (SmackException.NotConnectedException | SmackException.NotLoggedInException | SmackException.NoResponseException | XMPPException.XMPPErrorException e) {
                Log.e(ContactsFragment.class.getName(), "Error rejecting contact: " + s, e);
            }
        };

        contactRequestArrayAdapter = new ContactRequestArrayAdapter(getActivity(), R.layout.contact_request_item, acceptContact, rejectContact);
        registerForContextMenu(vAllContacts);

        vAllContacts.setAdapter(allContactsAdapter);
        vRequests.setAdapter(contactRequestArrayAdapter);
        loadContacts();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //All contacts menu
        if (v.getId() == R.id.all_contacts) {
            MenuInflater inflater = getActivity().getMenuInflater();
            inflater.inflate(R.menu.all_contacts_menu, menu);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId()) {
            //Delete pressed from all contacts
            case R.id.remove: {
                Friend contact = allContactsAdapter.getItem(info.position);
                Dialogs.reallyDeleteContact(getActivity(), () -> {
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
                displayContactDetail(allContactsAdapter.getItem(info.position));
                break;
            }
        }
        return true;
    }

    @OnClick(R.id.add_contact)
    void addContact() {
        Popups.addContactDialog(getActivity(), this::loadContacts);
    }

    @OnItemClick(R.id.all_contacts)
    void onItemSelected(int position) {
        displayContactDetail(allContactsAdapter.getItem(position));
    }

    private void loadContacts() {
        try {
            allContactsAdapter.clear();
            contactRequestArrayAdapter.clear();
            allContacts = ApiHandler.getMyContacts();
            allContactsAdapter.addAll(allContacts);

            ApiHandler.getContactRequests().forEach(friend -> {
                contactRequestArrayAdapter.add(new ContactRequest(friend));
            });
        } catch (SmackException.NotLoggedInException | SmackException.NotConnectedException | XMPPException.XMPPErrorException | SmackException.NoResponseException e) {
            Log.e(getActivity().getClass().getName(), "Error loading contacts", e);
        }
    }

    private void displayContactDetail(Friend contact) {
        ContactDetailFragment fragment = ContactDetailFragment.getInstance(contact.getJid());
        NavigationService.getInstance().Navigate(fragment, true, getFragmentManager());
    }
}
