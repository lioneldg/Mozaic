package com.freedroid.mozaik.view_pager2_;

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

import com.freedroid.mozaik.mains_contenairs.MainFragment;
import com.freedroid.mozaik.R;

public class StructureFragment extends Fragment{

    private final MainFragment mainFragment;

    public StructureFragment(MainFragment mainFragment) {
        this.mainFragment = mainFragment;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.structure_fragment_layout, container, false);
        final TextView textViewSeekBar = view.findViewById(R.id.textViewSeekBar);
        final SeekBar seekBar = view.findViewById(R.id.seekBar);
        Button buttonLess = view.findViewById(R.id.buttonLess);
        Button buttonMore = view.findViewById(R.id.buttonMore);
        final RadioButton radioNumberItems = view.findViewById(R.id.radioNumberItems);
        final RadioButton radioPadding = view.findViewById(R.id.radioPadding);
        final RadioButton radioTextSize = view.findViewById(R.id.radioTextSize);

        seekBar.setProgress(mainFragment.getNbrItems());
        textViewSeekBar.setText(String.valueOf(mainFragment.getNbrItems()));


        buttonMore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(seekBar.getProgress() < 100) {
                    if (radioNumberItems.isChecked()) {     //clic sur le bouton + =>un item de plus
                        mainFragment.setNbrItems(mainFragment.getNbrItems() + 1);
                        seekBar.setProgress(seekBar.getProgress() + 1);
                        mainFragment.setColumnsAndAdapter();
                    }
                    if (radioPadding.isChecked()) {         //clic sur le bouton + => padding plus grand
                        mainFragment.setPadding(mainFragment.getPadding() + 1);
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
                        mainFragment.setNbrItems(mainFragment.getNbrItems() - 1);
                        seekBar.setProgress(seekBar.getProgress() - 1);
                        mainFragment.setColumnsAndAdapter();
                    }
                    if (radioPadding.isChecked()) {         //clic sur le bouton - => padding moins grand
                        mainFragment.setPadding(mainFragment.getPadding() - 1);
                        seekBar.setProgress(seekBar.getProgress() - 1);
                        mainFragment.setColumnsAndAdapter();
                    }
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(radioNumberItems.isChecked()) {                              //modification du nombre d'item avec la seekBar
                    mainFragment.setNbrItems( (progress == 0) ? 1 : progress) ;   //empecher nbrItems = 0
                    textViewSeekBar.setText(String.valueOf(mainFragment.getNbrItems()));
                }
                if(radioPadding.isChecked()) {                                 //modification du padding
                    mainFragment.setPadding(progress);
                    textViewSeekBar.setText(String.valueOf(mainFragment.getPadding()));
                    mainFragment.setColumnsAndAdapter();                      //modification du padding en temps réel
                }
                if(radioTextSize.isChecked()){
                    mainFragment.setTextSize( (float)progress/2 );
                    textViewSeekBar.setText(String.valueOf(mainFragment.getTextSize()));
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
                seekBar.setProgress(mainFragment.getNbrItems());
                textViewSeekBar.setText(String.valueOf(mainFragment.getNbrItems()));
            }
        });

        radioPadding.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                seekBar.setProgress(mainFragment.getPadding());
                textViewSeekBar.setText(String.valueOf(mainFragment.getPadding()));
            }
        });

        radioTextSize.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                seekBar.setProgress((int) mainFragment.getTextSize() * 2);
                textViewSeekBar.setText(String.valueOf(mainFragment.getTextSize()));
            }
        });
        return view;
    }
}
