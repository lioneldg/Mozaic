package com.freedroid.mozaik;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
//mettre en place ROOM pour conserver les infos des clients
//créer un RV pour lister les clients en BDD et pouvoir les selectionner
//passer en java nio2 pour la lecture et l'écriture!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//si pas d'externalDir accessible prévenir l'utilisateur et utiliser le cache!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//limiter le padding en fonction du nbr d'items!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//créer un bouton refit pour retirer les espaces vides!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//desenregistrer le lister de viewPager lors de la gestion du cycle de vie

// FAIT !!!!paramétrer le taux de compression maxSizeImage en fonction du nombre d'items!!!!!!!!!!!!!!!!!L70  maxSizeImageFull, et L195 dans setNbrColumns()


public class MosaicFragment extends Fragment {

    protected int nbrItems = 0;
    private int nbrColumns = 0;
    private String[] firstNames = null;
    private String[] lastNames = null;
    private int[] imageIds = null;
    private File[] sourcesFiles = null;
    private TextView currentFirstName = null;
    private TextView currentLastName = null;
    protected GridView grid = null;
    private int currentSelection = -1;
    private Date currentImageName = null;
    protected int currentMaxSizeImage = 0;
    private int maxSizeImageFull = 0;
    private int maxSizeImageReduced = 0;
    protected int maxSizeImageFinal = 0;
    private ViewPager2 viewPager = null;
    protected int padding = 0;
    Point windowSize = null;
    protected boolean goodQualityImage = false;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        windowSize = new Point();
        maxSizeImageFull = 800;
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

        viewPager = view.findViewById(R.id.viewPager);
        grid = view.findViewById(R.id.PhotoGridView);
        grid.setEnabled(false);
        FragmentStateAdapter adapter=  new PagerAdapter(this);
        viewPager.setAdapter(adapter);

        setWindowSize();                                    //calcul de la taille de l'écran
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

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {      //listener de viewPager pour bloquer/donner l'acces au GridView en fonction des outils affichés
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {//désenregistre ce listener!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                switch (position){
                    case 0:                                 //première vue
                        grid.setEnabled(false);             //interdit de sélectionner des photos
                        setColumnsAndAdapter();             //le but ici est de choisir sa mise en page
                        break;
                    case 1:
                        grid.setEnabled(true);              //deuxieme vue
                        currentMaxSizeImage = maxSizeImageReduced; //la qualité des images affichée est très basse (pixélisée) pour ne pas surcharger la mémoire
                        goodQualityImage = false;           //c'est dans cette vue qu'on choisis les photos à afficher
                        setColumnsAndAdapter();
                        break;
                    case 2:
                        grid.setEnabled(false);             //troisième vue
                }                                           //c'est ici que l'on affiche les photos en bonne qualité et que l'on peux en extraire un screenshot format A4
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
            currentImageName = Calendar.getInstance().getTime();                //nom du fichier image = date précise
            assert bitmapSelectedFile != null;
            float quality = 115000.0f/bitmapSelectedFile.getWidth();            //ajuste la qualité pour enregistrer une image moins lourde
            sourcesFiles[currentSelection] = IO_BitmapImage.saveImage(getActivity(),bitmapSelectedFile,currentImageName.toString()+".JPEG", (int)quality);    //enregistrement de l'image sélectionnée avec plus basse qualité

            //lecture image
            ImageView currentImage = grid.getChildAt(currentSelection).findViewById(R.id.grid_item_image);
            Bitmap imageSource = IO_BitmapImage.readImage(getContext(), currentImageName.toString()+".JPEG",currentMaxSizeImage);//lecture et compression de l'image à afficher
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

    private void setWindowSize(){
        Display display = Objects.requireNonNull(getActivity()).getWindowManager().getDefaultDisplay();
        display.getSize(windowSize);
    }

    protected void setColumnsAndAdapter(){
        setNbrColumns();                        //calcule le nbr de colonnes necessaire pour afficher en format A4
        grid.setNumColumns(nbrColumns);         //paramètre le nbr de colonnes
        ImageAdapter adapter = new ImageAdapter(getContext(), firstNames, lastNames, imageIds, sourcesFiles, nbrColumns, nbrItems, currentMaxSizeImage, padding, viewPager);
        grid.setAdapter(adapter);
    }

    private void setGridLayoutParamsA4(){       //calcul du format A4 à appliquer au LayoutParams de GridView
        float a4Width = windowSize.x;
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
        float compress = 5.0f;              //compression des images de previsualisation
        if(nbrItems == 1){
            maxSizeImageReduced = maxSizeImageFull;
            maxSizeImageFinal = maxSizeImageFull;
            nbrColumns = 1;
        }
        else if (nbrItems > 1 && nbrItems < 5){
            maxSizeImageReduced = (int)((float) maxSizeImageFull/(2.0f*compress));
            maxSizeImageFinal = (int)((float) maxSizeImageFull/2.0f);
            nbrColumns = 2;
        }
        else if (nbrItems > 4 && nbrItems < 10){
            maxSizeImageReduced = (int)((float) maxSizeImageFull/(3.0f*compress));
            maxSizeImageFinal = (int)((float) maxSizeImageFull/3.0f);
            nbrColumns = 3;
        }
        else if (nbrItems > 9 && nbrItems < 17){
            maxSizeImageReduced = (int)((float) maxSizeImageFull/(4.0f*compress));
            maxSizeImageFinal = (int)((float) maxSizeImageFull/4.0f);
            nbrColumns = 4;
        }
        else if (nbrItems > 16 && nbrItems < 26){
            maxSizeImageReduced = (int)((float) maxSizeImageFull/(5.0f*compress));
            maxSizeImageFinal = (int)((float) maxSizeImageFull/5.0f);
            nbrColumns = 5;
        }
        else if (nbrItems > 25 && nbrItems < 37){
            maxSizeImageReduced = (int)((float) maxSizeImageFull/(6.0f*compress));
            maxSizeImageFinal = (int)((float) maxSizeImageFull/6.0f);
            nbrColumns = 6;
        }
        else if (nbrItems > 36 && nbrItems < 50){
            maxSizeImageReduced = (int)((float) maxSizeImageFull/(7.0f*compress));
            maxSizeImageFinal = (int)((float) maxSizeImageFull/7.0f);
            nbrColumns = 7;
        }
        else if (nbrItems > 49 && nbrItems < 65){
            maxSizeImageReduced = (int)((float) maxSizeImageFull/(8.0f*compress));
            maxSizeImageFinal = (int)((float) maxSizeImageFull/8.0f);
            nbrColumns = 8;
        }
        else if (nbrItems > 64 && nbrItems < 82){
            maxSizeImageReduced = (int)((float) maxSizeImageFull/(9.0f*compress));
            maxSizeImageFinal = (int)((float) maxSizeImageFull/9.0f);
            nbrColumns = 9;
        }
        else{
            maxSizeImageReduced = (int)((float) maxSizeImageFull/(10.0f*compress));
            maxSizeImageFinal = (int)((float) maxSizeImageFull/10.0f);
            nbrColumns = 10;
        }
    }
}
