package eu.alavio.jabbler.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.RosterListener;

import java.util.Collection;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.alavio.jabbler.Models.API.ApiHandler;
import eu.alavio.jabbler.Models.API.ChatHistoryManager;
import eu.alavio.jabbler.Models.API.ChatMessage;
import eu.alavio.jabbler.Models.API.User;
import eu.alavio.jabbler.Models.Helpers.Dialogs;
import eu.alavio.jabbler.Models.Helpers.NavigationService;
import eu.alavio.jabbler.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    RosterListener rosterListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        //Hamburger menu button
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Fill in user info
        try {
            User user = ApiHandler.getCurrentUser();

            View headerLayout = navigationView.getHeaderView(0);
            TextView vFullName = ButterKnife.findById(headerLayout, R.id.full_name);
            TextView vUserName = ButterKnife.findById(headerLayout, R.id.userName);

            if (user != null) {
                vFullName.setText(user.getName());
                vUserName.setText(user.getJid());
            }
        } catch (SmackException.NotConnectedException | XMPPException.XMPPErrorException | SmackException.NoResponseException e) {
            e.printStackTrace();
        }

        //Hamburger menu item selected listener
        navigationView.setNavigationItemSelectedListener(this);

        //Navigation initialization
        NavigationService.getInstance().setMainNavigationFrameId(R.id.content_frame);
        NavigationService.getInstance().setMainNavigationView(navigationView);
        NavigationService.getInstance().Navigate(NavigationService.MainPages.HOME, false, getFragmentManager());

        //Delegates Runnables on UI thread
        Handler handler = new Handler();

        rosterListener = new RosterListener() {
            @Override
            public void entriesAdded(Collection<String> addresses) {
                if (addresses.size() == 1) {
                    String jid = (String) addresses.toArray()[0];
                    handler.post(() -> Toast.makeText(getApplicationContext(), jid + " wants to add you as a contact.", Toast.LENGTH_LONG).show());
                }
            }

            @Override
            public void entriesUpdated(Collection<String> addresses) {
            }

            @Override
            public void entriesDeleted(Collection<String> addresses) {
            }

            @Override
            public void presenceChanged(Presence presence) {
            }
        };

        try {
            ApiHandler.getCurrentRoster().addRosterListener(rosterListener);
        } catch (SmackException.NotConnectedException | SmackException.NotLoggedInException e) {
            e.printStackTrace();
        }

        //Activate background listener for chat
        ChatManager manager = ApiHandler.backgroundChatManager();
        Context context = this;
        ChatHistoryManager historyManager = new ChatHistoryManager(this);
        if (manager != null) {
            manager.addChatListener((chat, createdLocally) -> chat.addMessageListener((chat1, message) -> {
                //Save message to history
                ChatMessage chatMessage = ChatMessage.ReceivedMessage(message);
                historyManager.saveMessage(chatMessage);

                //Notify user
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                String ringtonePreference = prefs.getString("notificationSound", "DEFAULT_NOTIFICATION_URI ");
                Uri ringtone = Uri.parse(ringtonePreference);
                Ringtone r = RingtoneManager.getRingtone(context, ringtone);
                r.play();

                if (!ApiHandler.isChatInProgress(chatMessage.getPartner_JID())) {
                    //Show popup if not current chat partner - delegate t UI thread
                    handler.post(() -> {
                        Toast.makeText(context, chatMessage.getPartner_JID() + ": " + message.getBody(), Toast.LENGTH_LONG).show();
                    });
                }
            }));
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            Dialogs.logoutDialog(this, super::onBackPressed);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                NavigationService.getInstance().Navigate(NavigationService.MainActivities.SETTINGS, this);
                return true;
            case R.id.Logout:
                logout();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Pops up dialog, if user press yes, he will be logged out.
     */
    private void logout() {
        Dialogs.logoutDialog(this, () -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                NavigationService.getInstance().Navigate(NavigationService.MainPages.HOME,
                        false, getFragmentManager());
                break;
            case R.id.nav_history:
                NavigationService.getInstance().Navigate(NavigationService.MainPages.HISTORY,
                        false, getFragmentManager());
                break;
            case R.id.nav_about:
                NavigationService.getInstance().Navigate(NavigationService.MainPages.ABOUT,
                        false, getFragmentManager());
                break;
            case R.id.nav_contacts:
                NavigationService.getInstance().Navigate(NavigationService.MainPages.CONTACTS,
                        false, getFragmentManager());
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
