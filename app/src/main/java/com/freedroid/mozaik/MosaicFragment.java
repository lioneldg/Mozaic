package com.freedroid.mozaik;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import static android.app.Activity.RESULT_OK;

//prévoir de developper l'orientation au format paysage (et débloquer l'orientation dans le manifest)
//créer un checkbox pour ne pas afficher le nom et le prénom
//mettre en place ROOM pour conserver les infos des clients
//créer un RV pour lister les clients en BDD et pouvoir les selectionner
//créer une static factory methode pour chaque fragment
//creer un seekbar pour modifier le padding du gridView
// choisir le taux de compression d'enregistrement de l'image
// paramétrer le taux de compression d'affichage en fonction de la qualité primaire et du nombre d'items
//passer en java nio2 pour la lecture et l'écriture
//si pas d'externalDir accessible prévenir l'utilisateur et utiliser le cache
//utiliser pageFragment pour afficher les outils et vérouiller les fonctionnalités en fonction de l'affichage en cours



public class MosaicFragment extends Fragment {

    protected int nbrItems = 0;
    private int nbrColumns = 0;
    private String[] firstNames = null;
    private String[] lastNames = null;
    private int[] imageIds = null;
    private File[] sourcesFiles = null;
    private TextView currentFirstName = null;
    private TextView currentLastName = null;
    private GridView grid = null;
    private int currentSelection = -1;
    private Date currentImageName = null;
    private int maxSizeImage = 0;
    private Button buttonQuality = null;
    private boolean goodQualityImage = false;
    private ViewPager2 viewPager = null;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        maxSizeImage = 30;
        nbrItems = 100;                         //initialisation des 100 items
        firstNames = new String[100];
        lastNames = new String[100];
        imageIds = new int[100];
        sourcesFiles = new File[100];

        for(int i = 0; i < nbrItems; i++) {     //écriture de firstName et lastName sous chaque item
            firstNames[i] = "firstName";
            lastNames[i] = "lastName";
            imageIds[i] = (i%2 == 0)? R.drawable.user_female : R.drawable.user_male;    //alternace image femme / image homme
        }
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.mosaic_fragment_layout, container, false);


        nbrItems = 20;                                      // démarrer l'application à 20 items

        //buttonQuality = view.findViewById(R.id.buttonQuality);!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

        /*buttonQuality.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(goodQualityImage == false) {
                    maxSizeImage = 200;
                    buttonQuality.setText("Show preview quality");
                    setColumnsAndAdapter();
                    goodQualityImage = true;
                }
                else{
                    maxSizeImage = 30;
                    buttonQuality.setText("Show good quality");
                    setColumnsAndAdapter();
                    goodQualityImage = false;
                }
            }
        });*/

        viewPager = view.findViewById(R.id.viewPager);
        grid = view.findViewById(R.id.PhotoGridView);
        FragmentStateAdapter adapter=  new PagerAdapter(this);
        viewPager.setAdapter(adapter);

        setGridLayoutParamsA4();                            //calcul du format A4 à appliquer au LayoutParams de GridView

        setColumnsAndAdapter();                             //première initialisation de la gridView (par la méthode onCreateView)

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentFirstName = view.findViewById(R.id.firstName);
                currentLastName = view.findViewById(R.id.lastName);
                currentSelection = position;
                choosePicture();
            }
        });


        return view;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 123 && resultCode == RESULT_OK) {     //choosePicture Activity result
            Uri selectedFile = data.getData();                  //data.getData retourne l'uri de l'image choisie par l'utilisateur
            Bitmap bitmapSelectedFile = null;
            try {
                bitmapSelectedFile = MediaStore.Images.Media.getBitmap(Objects.requireNonNull(getActivity()).getContentResolver(), selectedFile);   //récupération de l'image et convertion en bitmap
            } catch (IOException e) { e.printStackTrace(); }

            //enregistrement image
            currentImageName = Calendar.getInstance().getTime();        //nom du fichier image = date précise
            sourcesFiles[currentSelection] = IO_BitmapImage.saveImage(getActivity(),bitmapSelectedFile,currentImageName.toString(), 50);    //enregistrement de l'image sélectionnée avec plus basse qualité

            //lecture image
            ImageView currentImage = grid.getChildAt(currentSelection).findViewById(R.id.grid_item_image);
            Bitmap imageSource = IO_BitmapImage.readImage(getContext(), currentImageName.toString(),maxSizeImage);//lecture et compression de l'image à afficher
            currentImage.setImageBitmap(imageSource);

            setName();                                                  //DialogEditText s'ouvre après sélection de la photo
        }
        if(requestCode == 100 && resultCode == RESULT_OK){      //setName Activity result
            currentFirstName.setText(data.getStringExtra("firstName"));
            currentLastName.setText(data.getStringExtra("lastName"));
            firstNames[currentSelection] = data.getStringExtra("firstName");
            lastNames[currentSelection] = data.getStringExtra("lastName");
            currentSelection = -1;
        }
    }

    protected void setColumnsAndAdapter(){
        setNbrColumns();                        //calcule le nbr de colonnes necessaire pour afficher en format A4
        grid.setNumColumns(nbrColumns);         //paramètre le nbr de colonnes
        ImageAdapter adapter = new ImageAdapter(getContext(), firstNames, lastNames, imageIds, sourcesFiles, nbrColumns, nbrItems, maxSizeImage);
        grid.setAdapter(adapter);
    }

    private void setGridLayoutParamsA4(){       //calcul du format A4 à appliquer au LayoutParams de GridView
        Display display = Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        float a4Width = size.x;
        float a4Height = a4Width*1.414f;
        grid.setLayoutParams(new ConstraintLayout.LayoutParams((int)a4Width, (int)a4Height));
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

    private void setNbrColumns(){           //calcule le nombre de colonnes necessaires pour afficher au format A4
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
