package com.freedroid.mozaik;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager2.widget.ViewPager2;

import java.io.File;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private int[] imageId;
    private File[] imageFiles;
    private String[] firstName;
    private String[] lastName;
    private int nbrColumns;
    private float textSize;
    private int nbrItems;
    private int maxSizeImage;
    private int padding;
    private ViewPager2 viewPager;
    private int positionViewPager;

    ImageAdapter(Context c, String[] firstName, String[] lastName, int[] imageId, File imageFiles[], int nbrColumns, int nbrItems, int maxSizeImage, int padding, ViewPager2 viewPager, int positionViewPager) {
        mContext = c;
        this.imageId = imageId;
        this.imageFiles = imageFiles;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nbrColumns = nbrColumns;
        this.textSize = (40.0f/(float)nbrColumns) - (padding/20.0f);    //la taille du texte est adaptée au nb de colonnes et au padding
        this.nbrItems = nbrItems;
        this.maxSizeImage = maxSizeImage;
        this.padding = padding;
        this.viewPager = viewPager;
        this.positionViewPager = positionViewPager; //récupère oldPositionViewpager pour connaitre la position du viewPager sans le décalage causé par le filtre posé dans mozaicFragment pour éviter les répétitons de code du lister de ViewPager
    }

    public int getCount() {
        return nbrItems;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        assert wm != null;
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        float a4Width = size.x / (float)nbrColumns;
        float a4Height = (a4Width*1.414f);

        if (convertView == null) {
            gridView = new View(mContext);
            assert inflater != null;
            gridView = inflater.inflate(R.layout.cell_layout, null);    //ne sait pas comment accéder au ViewGroup à la place de null

            TextView textViewFirstName = gridView.findViewById(R.id.firstName);
            textViewFirstName.setText(firstName[position]);
            textViewFirstName.setTextSize(textSize);

            TextView textViewLastName = gridView.findViewById(R.id.lastName);
            textViewLastName.setText(lastName[position]);
            textViewLastName.setTextSize(textSize);
            gridView.setPaddingRelative(padding,padding,padding,padding);

            final ImageView imageView = gridView.findViewById(R.id.grid_item_image);
            imageView.setLayoutParams(new LinearLayout.LayoutParams((int)a4Width - padding*2, (int)(a4Height - padding*2 - textSize * 9)));//!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

            Handler handlerRead = new Handler();
            if ((imageFiles[position] != null) && positionViewPager != 0) {            //utilisé lors de l'initialisation et du changement de configuration
                final Bitmap[] imageSource = {null};
                Runnable runnableRead = new Runnable() {
                    public void run() {
                        imageSource[0] = IO_BitmapImage.readImage(imageFiles[position],maxSizeImage);
                        imageView.setImageBitmap(imageSource[0]);
                    }
                };
                handlerRead.post(runnableRead);
                Log.d("DEBUG","current item = "+positionViewPager);
            } else {
                Runnable runnableRead = new Runnable() {
                    public void run() {
                        imageView.setImageResource(imageId[position]);
                    }
                };
                handlerRead.post(runnableRead);
            }
        }
        else {
            gridView = convertView;
        }
        return gridView;
    }

    private int freePositions(){      //calcul du nombre d'items non utilisés
        int free = 0;
        for(int i = 0; i<nbrItems; i++){
            if (imageFiles[i] == null) free++;
        }
        return free;
    }

    private void refit(){
        for(int i = 0; i<nbrItems; i++){
            if (imageFiles[i] == null){
                imageFiles[i] = imageFiles[i+1];    //si l'image est null on copie l'image de l'emplacement suivant
                imageFiles[i+1] = null;             //on supprime l'image de l'emplacement suivant pour ne pas avoir de doublon
            }
        }
        //supprimer les derniers vides en copmptant avec freepositions!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    }

}