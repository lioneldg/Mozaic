package com.freedroid.mozaik;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TextsAndBG extends Fragment {

    MainFragment mainFragment;

    public TextsAndBG(MainFragment mainFragment) {
        this.mainFragment = mainFragment;
    }

    Spinner spinnerColorText;
    Spinner spinnerColorBG;
    CheckBox checkBoxBold;
    CheckBox checkBoxItalic;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.texts_and_bg,container);
        spinnerColorText = view.findViewById(R.id.spinnerColorText);
        spinnerColorBG = view.findViewById(R.id.spinnerColorBG);
        checkBoxBold = view.findViewById(R.id.checkBoxBold);
        checkBoxItalic = view.findViewById(R.id.checkBoxItalic);

        List<String> colors = new ArrayList<>();
        colors.add(getString(R.string.WHITE));
        colors.add(getString(R.string.BLACK));
        colors.add(getString(R.string.BLUE));
        colors.add(getString(R.string.CYAN));
        colors.add(getString(R.string.DARK_GRAY));
        colors.add(getString(R.string.GRAY));
        colors.add(getString(R.string.LIGHT_GRAY));
        colors.add(getString(R.string.GREEN));
        colors.add(getString(R.string.RED));
        colors.add(getString(R.string.MAGENTA));
        colors.add(getString(R.string.YELLOW));

        ArrayAdapter<String> colorArrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), android.R.layout.simple_spinner_item, colors);
        colorArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerColorText.setAdapter(colorArrayAdapter);
        spinnerColorBG.setAdapter(colorArrayAdapter);
        spinnerColorText.setSelection(1);
        spinnerColorBG.setSelection(0);

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

        spinnerColorText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mainFragment.colorText = getColor(position);
                mainFragment.setColumnsAndAdapter();
            }

            public void onNothingSelected(AdapterView<?> parent) { }
        });

        spinnerColorBG.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mainFragment.grid.setBackgroundColor(getColor(position));
            }

            public void onNothingSelected(AdapterView<?> parent) { }
        });

        return view;
    }

    private void checkVerif(){
        mainFragment.textBold = checkBoxBold.isChecked();
        mainFragment.textItalic = checkBoxItalic.isChecked();
        mainFragment.setColumnsAndAdapter();
    }

    private int getColor(int position){
        int color = 0;
        switch (position) {
            case 0:
                color = Color.WHITE;
                break;
             case 1:
                color = Color.BLACK;
                break;
             case 2:
                color = Color.BLUE;
                break;
             case 3:
                color = Color.CYAN;
                break;
             case 4:
                color = Color.DKGRAY;
                break;
             case 5:
                color = Color.GRAY;
                break;
             case 6:
                color = Color.LTGRAY;
                break;
             case 7:
                color = Color.GREEN;
                break;
             case 8:
                color = Color.RED;
                break;
             case 9:
                color = Color.MAGENTA;
                break;
             case 10:
                color = Color.YELLOW;
        }
        return color;
    }
}