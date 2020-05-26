package com.freedroid.mozaik;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PagerAdapter extends FragmentStateAdapter {

    private MosaicFragment mosaicFragment;

    public PagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
        this.mosaicFragment = (MosaicFragment) fragment;
    }

    @NonNull
    public Fragment createFragment(int position) {
        if(position == 0)return(new StructureFragment(mosaicFragment));
        else if(position == 1)return(new StructureFragment(mosaicFragment));
        else if(position == 2)return(new StructureFragment(mosaicFragment));
        else return new StructureFragment(mosaicFragment);
    }

    public int getItemCount() {
        return 3;
    }
}
