package com.freedroid.mozaik;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FinalizationFragment extends Fragment {
    private MosaicFragment mosaicFragment;
    private Button buttonRefit;

    public FinalizationFragment(MosaicFragment mosaicFragment) {
        this.mosaicFragment = mosaicFragment;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.finalization_fragment_layout, container, false);
        buttonRefit = view.findViewById(R.id.buttonRefit);
        buttonRefit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mosaicFragment.refit();
            }
        });
        return view;
    }
}
