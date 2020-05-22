package com.freedroid.mozaik;

import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import java.util.Objects;
import static android.app.Activity.RESULT_OK;

//prévoir de developper l'orientation au format paysage (et débloquer l'orientation dans le manifest)
//créer un checkbox pour ne pas afficher le nom et le prénom
//copier les photos sélectionnées dans le dossier du logiciel!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//mettre en place ROOM pour conserver les infos des clients
//créer un RV pour lister les clients en BDD et pouvoir les selectionner
//créer une static factory methode pour chaque fragment


public class MosaicFragment extends Fragment {

    private int nbrItems = 0;
    private int nbrColumns = 0;
    private String[] firstNames = null;
    private String[] lastNames = null;
    private int[] imageIds = null;
    private ImageView imageCell = null;
    private TextView firstName = null;
    private TextView lastName = null;
    private TextView textViewSeekBarNbrItems = null;
    private SeekBar seekBarNbrItems = null;
    private GridView grid = null;
    private ImageAdapter adapter = null;
    private View view = null;
    private int currentSelection = -1;
    private Button buttonMore = null;
    private Button buttonLess = null;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nbrItems = 100;
        firstNames = new String[100];
        lastNames = new String[100];
        imageIds = new int[100];

        for(int i = 0; i < nbrItems; i++) {
            firstNames[i] = "firstName";
            lastNames[i] = "lastName";
            imageIds[i] = (i%2 == 0)? R.drawable.user_female : R.drawable.user_male;
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.mosaic_fragment_layout,container, false);

        setNbrColumns();    //calcule le nbr de colonnes necessaire pour afficher en format A4

        textViewSeekBarNbrItems = view.findViewById(R.id.textNbrItems);
        seekBarNbrItems = view.findViewById(R.id.seekBarNbrItems);
        seekBarNbrItems.setProgress(nbrItems);
        textViewSeekBarNbrItems.setText(String.valueOf(nbrItems));
        buttonLess = view.findViewById(R.id.buttonLess);
        buttonMore = view.findViewById(R.id.buttonMore);

        buttonMore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(seekBarNbrItems.getProgress() < 100){
                    seekBarNbrItems.setProgress(seekBarNbrItems.getProgress() + 1);
                    setNbrColumns();                        //calcule le nbr de colonnes necessaire pour afficher en format A4
                    grid.setNumColumns(nbrColumns);         //paramètre le nbr de colonnes
                    adapter = new ImageAdapter(getContext(), firstNames, lastNames, imageIds, nbrColumns, nbrItems);
                    grid.setAdapter(adapter);
                }

            }
        });

        buttonLess.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(seekBarNbrItems.getProgress() > 1){
                    seekBarNbrItems.setProgress(seekBarNbrItems.getProgress() - 1);
                    setNbrColumns();                        //calcule le nbr de colonnes necessaire pour afficher en format A4
                    grid.setNumColumns(nbrColumns);         //paramètre le nbr de colonnes
                    adapter = new ImageAdapter(getContext(), firstNames, lastNames, imageIds, nbrColumns, nbrItems);
                    grid.setAdapter(adapter);
                }
            }
        });

        //CALCUL DU FORMAT A4 pour appliquer à GridView
        Display display = Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        float a4Width = size.x;
        float a4Height = a4Width*1.414f;

        //
        grid = view.findViewById(R.id.PhotoGridView);
        grid.setNumColumns(nbrColumns);         //paramètre le nbr de colonnes
        grid.setLayoutParams(new ConstraintLayout.LayoutParams((int)a4Width, (int)a4Height));
        adapter = new ImageAdapter(getContext(), firstNames, lastNames, imageIds, nbrColumns, nbrItems);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imageCell = view.findViewById(R.id.grid_item_image);
                firstName = view.findViewById(R.id.firstName);
                lastName = view.findViewById(R.id.lastName);
                currentSelection = position;
                choosePicture();
            }
        });

        seekBarNbrItems.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                nbrItems = (progress == 0 )? 1: progress;       //empecher nbrItems = 0
                textViewSeekBarNbrItems.setText(String.valueOf(nbrItems));
            }

            public void onStartTrackingTouch(SeekBar seekBar) { }

            public void onStopTrackingTouch(SeekBar seekBar) {
                setNbrColumns();                        //calcule le nbr de colonnes necessaire pour afficher en format A4
                grid.setNumColumns(nbrColumns);         //paramètre le nbr de colonnes
                adapter = new ImageAdapter(getContext(), firstNames, lastNames, imageIds, nbrColumns, nbrItems);
                grid.setAdapter(adapter);

            }
        });

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123 && resultCode == RESULT_OK) {     //choosePicture Activity result
            Uri selectedFile = data.getData();
            imageCell.setImageURI(selectedFile);

            setName();      //DialogEditText s'ouvre après sélection de la photo
        }
        if(requestCode == 100 && resultCode == RESULT_OK){      //setName Activity result
            firstName.setText(data.getStringExtra("firstName"));
            lastName.setText(data.getStringExtra("lastName"));
            firstNames[currentSelection] = data.getStringExtra("firstName");
            lastNames[currentSelection] = data.getStringExtra("lastName");
            currentSelection = -1;
        }
    }

    private void choosePicture(){
        Intent intent = new Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
    }

    private void setName(){
        Intent intent = new Intent(getActivity(), DialogEditText.class);
        startActivityForResult(intent, 100);
    }

    private void setNbrColumns(){
        if(nbrItems == 1) nbrColumns = 1;
        else if (nbrItems > 1 && nbrItems < 5) nbrColumns = 2;
        else if (nbrItems > 4 && nbrItems < 10) nbrColumns = 3;
        else if (nbrItems > 9 && nbrItems < 17) nbrColumns = 4;
        else if (nbrItems > 16 && nbrItems < 26) nbrColumns = 5;
        else if (nbrItems > 25 && nbrItems < 37) nbrColumns = 6;
        else if (nbrItems > 36 && nbrItems < 50) nbrColumns = 7;
        else if (nbrItems > 49 && nbrItems < 65) nbrColumns = 8;
        else if (nbrItems > 64 && nbrItems < 82) nbrColumns = 9;
        else nbrColumns = 10;
    }
}
