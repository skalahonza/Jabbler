package eu.alavio.jabbler.Models;

import android.app.Fragment;
import android.app.FragmentManager;
import android.util.Log;

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

    public boolean Navigate(Fragment fragment, boolean saveInBackstack, FragmentManager fragmentManager) {
        return Navigate(fragment, saveInBackstack, fragmentManager, mainNavigationView);
    }

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
}
