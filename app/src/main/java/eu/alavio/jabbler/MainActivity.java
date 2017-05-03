package eu.alavio.jabbler;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
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

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.alavio.jabbler.API.ApiHandler;
import eu.alavio.jabbler.API.User;
import eu.alavio.jabbler.Models.Dialogs;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.nav_view)
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Fill in user info
        try {
            User user = ApiHandler.getCurrentUser();

            View headerLayout = navigationView.getHeaderView(0);
            //navigationView.inflateHeaderView(R.layout.nav_header_main);
            TextView vFullName = ButterKnife.findById(headerLayout, R.id.full_name);
            TextView vUserName = ButterKnife.findById(headerLayout, R.id.userName);

            if (user != null) {
                vFullName.setText(user.getName());
                vUserName.setText(user.getJid());
            }
        } catch (SmackException.NotConnectedException | XMPPException.XMPPErrorException | SmackException.NoResponseException e) {
            e.printStackTrace();
        }


        navigationView.setNavigationItemSelectedListener(this);
        //Select first - home item
        navigationView.getMenu().getItem(0).setChecked(true);
        navigate(new HomeFragment());
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
                //if settings not already displayed
                Fragment settings = new SettingsScreen();
                if (!isFragmentDisplayed(settings))
                    navigate(settings, true);
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
        Fragment fragment = null;
        switch (id) {
            case R.id.nav_home:
                fragment = new HomeFragment();
                break;
            case R.id.nav_history:
                fragment = new HistoryFragment();
                break;
            case R.id.nav_settings:
                //If settings already displayed, end method
                Fragment settings = new SettingsScreen();
                if (!isFragmentDisplayed(settings))
                    fragment = settings;
                break;
            case R.id.nav_about:
                fragment = new AboutFragment();
                break;
            case R.id.nav_contacts:
                fragment = new ContactsFragment();
                break;
        }

        if (fragment != null)
            navigate(fragment, true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Performs navigation to a fragment, without saving him to backstack
     *
     * @param fragment Fragment to be navigated to
     */
    private void navigate(Fragment fragment) {
        navigate(fragment, false);
    }

    /**
     * Performs navigation to a frame.
     *
     * @param fragment        Fragment to be navigated to
     * @param saveInBackStack True - saves current fragment in backstack; false - current fragment won't be saved
     */
    private void navigate(Fragment fragment, boolean saveInBackStack) {
        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        if (saveInBackStack) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();
        }
    }

    /**
     * Check if any instance of given fragment is now displayed
     *
     * @param fragment Examined fragment
     * @return True if the fragment is displayed
     */
    private boolean isFragmentDisplayed(Fragment fragment) {
        Fragment f = getFragmentManager().findFragmentById(R.id.content_frame);
        return f.getClass() == fragment.getClass();
    }
}
