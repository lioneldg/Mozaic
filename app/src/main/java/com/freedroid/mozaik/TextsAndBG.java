package com.freedroid.mozaik;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TextsAndBG extends Fragment {

    MosaicFragment mosaicFragment;

    public TextsAndBG(MosaicFragment mosaicFragment) {
        this.mosaicFragment = mosaicFragment;
    }

    Spinner spinnerFont;
    Spinner spinnerColorText;
    Spinner spinnerColorBG;
    CheckBox checkBoxBold;
    CheckBox checkBoxItalic;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.texts_and_bg,container);

        spinnerFont = view.findViewById(R.id.spinnerFont);
        spinnerColorText = view.findViewById(R.id.spinnerColorText);
        spinnerColorBG = view.findViewById(R.id.spinnerColorBG);
        checkBoxBold = view.findViewById(R.id.checkBoxBold);
        checkBoxItalic = view.findViewById(R.id.checkBoxItalic);

        checkBoxBold.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkVerif();
            }
        });

        checkBoxItalic.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                checkVerif();
            }
        });

        return view;
    }

    private void checkVerif(){
        if(checkBoxBold.isChecked()) mosaicFragment.textBold = true;
        else mosaicFragment.textBold = false;
        if(checkBoxItalic.isChecked()) mosaicFragment.textItalic = true;
        else mosaicFragment.textItalic = false;

        mosaicFragment.setColumnsAndAdapter();
    }
}
