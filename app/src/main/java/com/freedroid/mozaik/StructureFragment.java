package com.freedroid.mozaik;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class StructureFragment extends Fragment {

    private TextView textViewSeekBarNbrItems = null;
    private SeekBar seekBarNbrItems = null;
    private MosaicFragment mosaicFragment;
    private int nbrItems = 0;

    StructureFragment(MosaicFragment mosaicFragment) {
        this.mosaicFragment = mosaicFragment;
        this.nbrItems = mosaicFragment.nbrItems;
    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.structure_fragment_layout, container, false);
        textViewSeekBarNbrItems = view.findViewById(R.id.textNbrItems);
        seekBarNbrItems = view.findViewById(R.id.seekBarNbrItems);
        Button buttonLess = view.findViewById(R.id.buttonLess);
        Button buttonMore = view.findViewById(R.id.buttonMore);

        seekBarNbrItems.setProgress(nbrItems);
        textViewSeekBarNbrItems.setText(String.valueOf(nbrItems));


        buttonMore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {                   //clic sur le bouton + =>un item de plus
                if(seekBarNbrItems.getProgress() < 100){
                    nbrItems++;
                    mosaicFragment.nbrItems++;
                    seekBarNbrItems.setProgress(seekBarNbrItems.getProgress() + 1);
                    mosaicFragment.setColumnsAndAdapter();
                }
            }
        });

        buttonLess.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {                   //clic sur le bouton - => un item de moins
                if(seekBarNbrItems.getProgress() > 1){
                    nbrItems--;
                    mosaicFragment.nbrItems--;
                    seekBarNbrItems.setProgress(seekBarNbrItems.getProgress() - 1);
                    mosaicFragment.setColumnsAndAdapter();
                }
            }
        });

        seekBarNbrItems.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {  //modification du nombre d'item avec la seekBar
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                nbrItems = (progress == 0 )? 1: progress;   //empecher nbrItems = 0
                mosaicFragment.nbrItems = nbrItems;
                textViewSeekBarNbrItems.setText(String.valueOf(nbrItems));
            }

            public void onStartTrackingTouch(SeekBar seekBar) { }

            public void onStopTrackingTouch(SeekBar seekBar) { mosaicFragment.setColumnsAndAdapter(); }

        });

        return view;
    }
}
