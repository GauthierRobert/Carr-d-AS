package lhc.com.carrdas.viewerPage;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class RankingPageAdapter extends FragmentPagerAdapter {

    private ArrayList<Fragment> fragments;

    public RankingPageAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        this.fragments= new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public void addFragment(Fragment fragment)
    {
        fragments.add(fragment);
    }

    //set title

    @Override
    public CharSequence getPageTitle(int position) {
        String title=fragments.get(position).toString();
        return title;
    }

}