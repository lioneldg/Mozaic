package com.freedroid.mozaik;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StructureFragment extends Fragment{

    private TextView textViewSeekBar = null;
    private SeekBar seekBar = null;
    private MainFragment mainFragment;
    private RadioButton radioNumberItems;
    private RadioButton radioPadding;
    private RadioButton radioTextSize;

    StructureFragment(MainFragment mainFragment) {
        this.mainFragment = mainFragment;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.structure_fragment_layout, container, false);
        textViewSeekBar = view.findViewById(R.id.textViewSeekBar);
        seekBar = view.findViewById(R.id.seekBar);
        Button buttonLess = view.findViewById(R.id.buttonLess);
        Button buttonMore = view.findViewById(R.id.buttonMore);
        radioNumberItems = view.findViewById(R.id.radioNumberItems);
        radioPadding = view.findViewById(R.id.radioPadding);
        radioTextSize = view.findViewById(R.id.radioTextSize);

        seekBar.setProgress(mainFragment.nbrItems);
        textViewSeekBar.setText(String.valueOf(mainFragment.nbrItems));


        buttonMore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(seekBar.getProgress() < 100) {
                    if (radioNumberItems.isChecked()) {     //clic sur le bouton + =>un item de plus
                        mainFragment.nbrItems++;
                        seekBar.setProgress(seekBar.getProgress() + 1);
                        mainFragment.setColumnsAndAdapter();
                    }
                    if (radioPadding.isChecked()) {         //clic sur le bouton + => padding plus grand
                        mainFragment.padding++;
                        seekBar.setProgress(seekBar.getProgress() + 1);
                        mainFragment.setColumnsAndAdapter();
                    }
                }
            }
        });

        buttonLess.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(seekBar.getProgress() > 1) {     //clic sur le bouton - => un item de moins
                    if (radioNumberItems.isChecked()) {
                        mainFragment.nbrItems--;
                        seekBar.setProgress(seekBar.getProgress() - 1);
                        mainFragment.setColumnsAndAdapter();
                    }
                    if (radioPadding.isChecked()) {         //clic sur le bouton - => padding moins grand
                        mainFragment.padding--;
                        seekBar.setProgress(seekBar.getProgress() - 1);
                        mainFragment.setColumnsAndAdapter();
                    }
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(radioNumberItems.isChecked()) {                              //modification du nombre d'item avec la seekBar
                    mainFragment.nbrItems = (progress == 0) ? 1 : progress;   //empecher nbrItems = 0
                    textViewSeekBar.setText(String.valueOf(mainFragment.nbrItems));
                }
                if(radioPadding.isChecked()) {                                 //modification du padding
                    mainFragment.padding = progress;
                    textViewSeekBar.setText(String.valueOf(mainFragment.padding));
                    mainFragment.setColumnsAndAdapter();                      //modification du padding en temps réel
                }
                if(radioTextSize.isChecked()){
                    mainFragment.textSize = (float)progress/2;
                    textViewSeekBar.setText(String.valueOf(mainFragment.textSize));
                    mainFragment.setColumnsAndAdapter();                      //modification du textSize en temps réel
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) { }

            public void onStopTrackingTouch(SeekBar seekBar) {
                if(radioNumberItems.isChecked()) {
                    mainFragment.setColumnsAndAdapter();                      //modification du nbr d'items lors du retrait du doigt
                }
            }
        });

        radioNumberItems.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                seekBar.setProgress(mainFragment.nbrItems);
                textViewSeekBar.setText(String.valueOf(mainFragment.nbrItems));
            }
        });

        radioPadding.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                seekBar.setProgress(mainFragment.padding);
                textViewSeekBar.setText(String.valueOf(mainFragment.padding));
            }
        });

        radioTextSize.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                seekBar.setProgress((int) mainFragment.textSize*2);
                textViewSeekBar.setText(String.valueOf(mainFragment.textSize));
            }
        });
        return view;
    }
}
