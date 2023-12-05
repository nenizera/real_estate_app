package luishenrique.zipimoveis.utils;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentUtils {

    private FragmentUtils() {}

    public static void loadFragment(FragmentManager fragmentManager, Fragment fragment, int contentFragment, String name) {

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if(fragment.isAdded()) {
            transaction.show(fragment);
        } else {
            transaction.addToBackStack(name);
            transaction.replace(contentFragment, fragment);
        }
        transaction.commit();
    }

}
