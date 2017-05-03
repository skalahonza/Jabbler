package eu.alavio.jabbler.Models;

import android.app.Fragment;
import android.app.FragmentManager;
import android.util.Log;

import eu.alavio.jabbler.R;

/**
 * Wrapper for android activity/fragment navigation
 */

public final class NavigationService {

    private static final NavigationService INSTANCE = new NavigationService();
    private int mainNavigationView;

    private NavigationService() {
    }

    public static NavigationService getInstance() {
        return INSTANCE;
    }

    public int getMainNavigationView() {
        return mainNavigationView;
    }

    public void setMainNavigationView(int mainNavigationView) {
        this.mainNavigationView = mainNavigationView;
    }

    /** Navigates to given fragment, and present it on default navigation view.
     * @param fragment Fragment to be dispalyed
     * @param saveInBackstack Save current fragment into backstack before navigating
     * @param fragmentManager Provide current fragment manager, calling getFragmentManager()
     * @return True if the navigation was successful
     */
    public boolean Navigate(Fragment fragment, boolean saveInBackstack, FragmentManager fragmentManager) {
        return Navigate(fragment, saveInBackstack, fragmentManager, mainNavigationView);
    }

    /** Navigates to given fragment, and present it on given navigation view.
     * @param fragment Fragment to be dispalyed
     * @param saveInBackstack Save current fragment into backstack before navigating
     * @param fragmentManager Provide current fragment manager, calling getFragmentManager()
     * @param navView Navigation view that will be used as a fragment presenter
     * @return True if the navigation was successful
     */
    public boolean Navigate(Fragment fragment, boolean saveInBackstack, FragmentManager fragmentManager, int navView) {
        try {
            // Insert the fragment by replacing any existing fragment
            if (saveInBackstack) {
                fragmentManager.beginTransaction()
                        .replace(navView, fragment)
                        .addToBackStack(null)
                        .commit();
            } else {
                fragmentManager.beginTransaction()
                        .replace(navView, fragment)
                        .commit();
            }
            return true;
        } catch (Exception e) {
            Log.e("Navigation service", "Error while navigating", e);
            return false;
        }
    }

    /** Checks if the fragment is displayed on a given navigation view
     * @param fragment Fragment to be checked
     * @param fragmentManager Provide current fragment manager, calling getFragmentManager()
     * @param frameId Navigation view id that will be observed
     * @return True fi the fragment is displayed
     */
    public boolean isFragmentDisplayed(Fragment fragment, FragmentManager fragmentManager, int frameId) {
        Fragment f = fragmentManager.findFragmentById(R.id.content_frame);
        return f.getClass() == fragment.getClass();
    }

    /** Checks if the fragment is displayed on a default navigation view
     * @param fragment Fragment to be checked
     * @param fragmentManager Provide current fragment manager, calling getFragmentManager()
     * @return True fi the fragment is displayed
     */
    public boolean isFragmentDisplayed(Fragment fragment, FragmentManager fragmentManager) {
        return isFragmentDisplayed(fragment, fragmentManager, mainNavigationView);
    }
}
