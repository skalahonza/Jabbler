package eu.alavio.jabbler.Models.Helpers;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.util.Log;

import eu.alavio.jabbler.Fragments.AboutFragment;
import eu.alavio.jabbler.Fragments.ContactsFragment;
import eu.alavio.jabbler.Fragments.HistoryFragment;
import eu.alavio.jabbler.Fragments.HomeFragment;
import eu.alavio.jabbler.Fragments.SettingsScreen;

/**
 * Wrapper for android activity/fragment navigation
 */

public final class NavigationService {

    private static final NavigationService INSTANCE = new NavigationService();
    private int mainNavigationFrameId;
    private NavigationView mainNavigationView;

    private NavigationService() {
    }


    public enum MainPages {
        HOME {
            @Override
            public Fragment GetDefaultFragment() {
                return new HomeFragment();
            }
        },
        HISTORY {
            @Override
            public Fragment GetDefaultFragment() {
                return new HistoryFragment();
            }
        },
        CONTACTS {
            @Override
            public Fragment GetDefaultFragment() {
                return new ContactsFragment();
            }
        },
        SETTINGS {
            @Override
            public Fragment GetDefaultFragment() {
                return new SettingsScreen();
            }
        },
        ABOUT {
            @Override
            public Fragment GetDefaultFragment() {
                return new AboutFragment();
            }
        };

        public abstract Fragment GetDefaultFragment();
    }

    public static NavigationService getInstance() {
        return INSTANCE;
    }

    public NavigationView getMainNavigationView() {
        return mainNavigationView;
    }

    public void setMainNavigationView(NavigationView mainNavigationView) {
        this.mainNavigationView = mainNavigationView;
    }

    public int getMainNavigationFrameId() {
        return mainNavigationFrameId;
    }

    public void setMainNavigationFrameId(int mainNavigationFrameId) {
        this.mainNavigationFrameId = mainNavigationFrameId;
    }

    /**
     * Navigates to given fragment, and present it on default navigation view.
     *
     * @param fragment        Fragment to be dispalyed
     * @param saveInBackstack Save current fragment into backstack before navigating
     * @param fragmentManager Provide current fragment manager, calling getFragmentManager()
     * @return True if the navigation was successful
     */
    public boolean Navigate(Fragment fragment, boolean saveInBackstack, FragmentManager fragmentManager) {
        return Navigate(fragment, saveInBackstack, fragmentManager, mainNavigationFrameId);
    }

    /**
     * Navigates to given fragment, and present it on given navigation view.
     *
     * @param fragment        Fragment to be dispalyed
     * @param saveInBackstack Save current fragment into backstack before navigating
     * @param fragmentManager Provide current fragment manager, calling getFragmentManager()
     * @param frameId         Navigation view that will be used as a fragment presenter
     * @return True if the navigation was successful
     */
    public boolean Navigate(Fragment fragment, boolean saveInBackstack, FragmentManager fragmentManager, int frameId) {
        try {
            // Insert the fragment by replacing any existing fragment
            if (saveInBackstack) {
                fragmentManager.beginTransaction()
                        .replace(frameId, fragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                fragmentManager.beginTransaction()
                        .replace(frameId, fragment)
                        .commit();
            }
            return true;
        } catch (Exception e) {
            Log.e("Navigation service", "Error while navigating", e);
            return false;
        }
    }

    /**
     * Checks if the fragment is displayed on a given navigation view
     *
     * @param fragment        Fragment to be checked
     * @param fragmentManager Provide current fragment manager, calling getFragmentManager()
     * @param frameId         Navigation view id that will be observed
     * @return True fi the fragment is displayed
     */
    public boolean isFragmentDisplayed(Fragment fragment, FragmentManager fragmentManager, int frameId) {
        Fragment f = fragmentManager.findFragmentById(frameId);
        return f.getClass() == fragment.getClass();
    }

    public boolean Navigate(MainPages page, boolean saveInBackstack, FragmentManager fragmentManager, int frameId) {
        //Select item in a menu
        mainNavigationView.getMenu().getItem(page.ordinal()).setChecked(true);
        return Navigate(page.GetDefaultFragment(), saveInBackstack, fragmentManager, frameId);
    }

    public boolean Navigate(MainPages page, boolean saveInBackstack, FragmentManager fragmentManager) {
        return Navigate(page, saveInBackstack, fragmentManager, mainNavigationFrameId);
    }

    /**
     * Checks if the fragment is displayed on a default navigation view
     *
     * @param fragment        Fragment to be checked
     * @param fragmentManager Provide current fragment manager, calling getFragmentManager()
     * @return True fi the fragment is displayed
     */
    public boolean isFragmentDisplayed(Fragment fragment, FragmentManager fragmentManager) {
        return isFragmentDisplayed(fragment, fragmentManager, mainNavigationFrameId);
    }

    /**
     * Checks if back navigation can be performed
     *
     * @param fragmentManager Provide current fragment manager, calling getFragmentManager()
     * @return True if back navigation is possible (backstack not empty)
     */
    public boolean canGoBack(FragmentManager fragmentManager) {
        return fragmentManager.getBackStackEntryCount() > 0;
    }

    /**
     * Checks if back navigation can be performed and if so, it navigates back
     *
     * @param fragmentManager Provide current fragment manager, calling getFragmentManager()
     * @return True if back navigation is possible (backstack not empty)
     */
    public boolean goBack(FragmentManager fragmentManager) {
        if (!canGoBack(fragmentManager)) return false;
        fragmentManager.popBackStack();
        return true;
    }
}
