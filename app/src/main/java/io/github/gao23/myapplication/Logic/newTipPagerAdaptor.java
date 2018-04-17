package io.github.gao23.myapplication.Logic;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import io.github.gao23.myapplication.UI.fragments.*;

/**
 * Created by xgao on 4/17/18.
 */

public class newTipPagerAdaptor extends FragmentStatePagerAdapter{
    private int numOfTabs;

   public newTipPagerAdaptor(FragmentManager fm, int NumberOFTabs){
        super(fm);
        numOfTabs = NumberOFTabs;
    }


    /***
     * this returns the fragment, which is the tab in this case, at a specific int
     * @param position is the position of the tab, computer tab fragment will be 0
     * @return
     */
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                new_computer_fragment computerTab = new new_computer_fragment();
                return computerTab;
            case 1:
                new_cash_fragment cashTab = new new_cash_fragment();
                return cashTab;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
