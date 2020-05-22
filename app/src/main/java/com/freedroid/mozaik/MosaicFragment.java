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
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.Objects;
import static android.app.Activity.RESULT_OK;

//recupérer le nom et le prénom dans la boite de dialogue!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//prévoir de developper l'orientation au format paysage (et débloquer l'orientation dans le manifest)
//créer un slider pour régler le nbr de colonnes
//créer un checkbox pour ne pas afficher le nom et le prénom
//copier les photos sélectionnées dans le dossier du logiciel
//mettre en place ROOM pour conserver les infos des clients
//créer un RV pour lister les clients en BDD et pouvoir les selectionner
//créer une static factory methode pour chaque fragment


public class MosaicFragment extends Fragment {

    private int nbrItems = 20;
    private int nbrColumns = 0;
    private String[] firstNames = null;
    private String[] lastNames = null;
    private int[] imageIds = null;
    private ImageView imageCell = null;
    private TextView firstName = null;
    private TextView lastName = null;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setNbrColumns();

        firstNames = new String[nbrItems];
        lastNames = new String[nbrItems];
        imageIds = new int[nbrItems];

        for(int i = 0; i < nbrItems; i++) {
            firstNames[i] = "firstName";
            lastNames[i] = "lastName";
            imageIds[i] = (i%2 == 0)? R.drawable.user_female : R.drawable.user_male;
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.mosaic_fragment_layout,container, false);

        final ImageAdapter adapter = new ImageAdapter(getContext(), firstNames, lastNames, imageIds, nbrColumns);
        GridView grid = view.findViewById(R.id.PhotoGridView);
        grid.setNumColumns(nbrColumns);

        Display display = Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        float a4Width = size.x;
        float a4Height = a4Width/21f*29.7f;

        grid.setLayoutParams(new FrameLayout.LayoutParams((int)a4Width, (int)a4Height));
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imageCell = view.findViewById(R.id.grid_item_image);
                firstName = view.findViewById(R.id.firstName);
                lastName = view.findViewById(R.id.lastName);
                choosePicture();
            }
        });

        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123 && resultCode == RESULT_OK) {
            Uri selectedFile = data.getData();
            imageCell.setImageURI(selectedFile);

            setName(); //la boite de dialogue set name s'ouvre après sélection de la photo
        }
        if(requestCode == 100 && resultCode == RESULT_OK){
            Log.d("FIRSTNAME",""+data.getStringExtra("firstName"));
            firstName.setText(data.getStringExtra("firstName"));
            lastName.setText(data.getStringExtra("lastName"));
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
