package com.freedroid.mozaik;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PagerAdapter extends FragmentStateAdapter {

    private MainFragment mainFragment;

    public PagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
        this.mainFragment = (MainFragment) fragment;
    }

    @NonNull
    public Fragment createFragment(int position) {
        if(position == 0)return(new StructureFragment(mainFragment));
        else if(position == 1)return(new TextsAndBG(mainFragment));
        else if(position == 2)return(new FinalizationFragment(mainFragment));
        else return new StructureFragment(mainFragment);
    }

    public int getItemCount() {
        return 3;
    }
}
