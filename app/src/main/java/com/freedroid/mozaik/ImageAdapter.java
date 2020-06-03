package com.freedroid.mozaik;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private MainFragment mainFragment;
    private float textSize;

    ImageAdapter(Context c, MainFragment mainFragment) {

        mContext = c;
        this.mainFragment = mainFragment;
        this.textSize = (mainFragment.textSize/(float)mainFragment.nbrColumns) - (mainFragment.padding/20.0f);    //la taille du texte est adaptée au nb de colonnes et au padding
    }

    public int getCount() {
        return mainFragment.nbrItems;
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return 0;
    }

    @SuppressLint("InflateParams")
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView;

        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        assert wm != null;
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        float a4Width = size.x / (float)mainFragment.nbrColumns;
        float a4Height = (a4Width*1.414f);

        if (convertView == null) {
            assert inflater != null;
            gridView = inflater.inflate(R.layout.cell_layout, null);    //ne sait pas comment accéder au ViewGroup à la place de null

            TextView textViewFirstName = gridView.findViewById(R.id.firstName);
            textViewFirstName.setText(mainFragment.firstNames[position]);
            textViewFirstName.setTextSize(textSize);
            textViewFirstName.setTextColor(mainFragment.colorText);

            TextView textViewLastName = gridView.findViewById(R.id.lastName);
            textViewLastName.setText(mainFragment.lastNames[position]);
            textViewLastName.setTextSize(textSize);
            textViewLastName.setTextColor(mainFragment.colorText);

            if(mainFragment.textBold && !mainFragment.textItalic){
                textViewFirstName.setTypeface(textViewFirstName.getTypeface(), Typeface.BOLD);
                textViewLastName.setTypeface(textViewLastName.getTypeface(), Typeface.BOLD);
            }
            else if(mainFragment.textItalic && !mainFragment.textBold){
                textViewFirstName.setTypeface(textViewFirstName.getTypeface(), Typeface.ITALIC);
                textViewLastName.setTypeface(textViewLastName.getTypeface(), Typeface.ITALIC);
            }
            else if(mainFragment.textBold){  //représente textBold && textItalique car les autres possibilités ont été utilisées au dessus
                textViewFirstName.setTypeface(textViewFirstName.getTypeface(), Typeface.BOLD_ITALIC);
                textViewLastName.setTypeface(textViewLastName.getTypeface(), Typeface.BOLD_ITALIC);
            }
            else{
                textViewFirstName.setTypeface(textViewFirstName.getTypeface(), Typeface.NORMAL);
                textViewLastName.setTypeface(textViewLastName.getTypeface(), Typeface.NORMAL);
            }

            gridView.setPaddingRelative(mainFragment.padding, mainFragment.padding, mainFragment.padding, mainFragment.padding);

            final ImageView imageView = gridView.findViewById(R.id.grid_item_image);
            imageView.setLayoutParams(new LinearLayout.LayoutParams((int)a4Width - mainFragment.padding*2, (int)(a4Height - mainFragment.padding*2 - textSize * 9)));

            Handler handlerRead = new Handler();
            if ((mainFragment.sourcesFiles[position] != null) && mainFragment.oldPositionViewPager != 0 && mainFragment.oldPositionViewPager != 1) {            //utilisé lors de l'initialisation et du changement de configuration
                final Bitmap[] imageSource = {null};
                Runnable runnableRead = new Runnable() {
                    public void run() {
                        imageSource[0] = IO_BitmapImage.readImage(mainFragment.sourcesFiles[position],mainFragment.currentSizeImage);
                        imageView.setImageBitmap(imageSource[0]);
                    }
                };
                handlerRead.post(runnableRead);
            } else {
                Runnable runnableRead = new Runnable() {
                    public void run() {
                        imageView.setImageResource(mainFragment.imageIds[position]);
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
}