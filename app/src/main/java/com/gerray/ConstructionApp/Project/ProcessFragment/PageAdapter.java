package com.gerray.ConstructionApp.Project.ProcessFragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.gerray.ConstructionApp.Project.InfoFragment.InfoFragment;
import com.gerray.ConstructionApp.Project.MediaFragment.MediaFragment;


public class PageAdapter extends FragmentPagerAdapter {
    private final int tabNum;

    public PageAdapter(@NonNull FragmentManager fm, int tabNum) {
        super(fm, tabNum);
        this.tabNum = tabNum;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ProcessFragment();
            case 1:
                return new InfoFragment();
            case 2:
                return new MediaFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabNum;
    }
}
