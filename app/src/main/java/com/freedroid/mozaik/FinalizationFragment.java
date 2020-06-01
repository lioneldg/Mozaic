package com.freedroid.mozaik;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.io.File;
import java.io.IOException;
import static android.app.Activity.RESULT_OK;

public class FinalizationFragment extends Fragment {
    private MosaicFragment mosaicFragment;


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
        Button buttonRefit = view.findViewById(R.id.buttonRefit);
        Button buttonExport = view.findViewById(R.id.buttonExport);

        buttonRefit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mosaicFragment.refit();
            }
        });

        buttonExport.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExportDialogEditText.class);
                startActivityForResult(intent, 582);
            }
        });

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 582 && resultCode == RESULT_OK) {
            String fileName = data.getStringExtra("fileName");
            Bitmap bitmap = IO_BitmapImage.getBitmapFromView(mosaicFragment.grid);

            try {
                IO_BitmapImage.saveImage(getContext(), bitmap, fileName + ".JPEG", true);     //enregistrement de l'image finale au format A4
            } catch (IOException e) { e.printStackTrace(); }
            Toast.makeText(getActivity(), "Document saved in Pictures folder!", Toast.LENGTH_LONG).show();

            IO_BitmapImage.deletePhotos(getActivity(), mosaicFragment.sourcesFiles);   //suppression des images temporaires
        }
    }
}
