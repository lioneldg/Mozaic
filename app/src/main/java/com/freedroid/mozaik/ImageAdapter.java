package com.freedroid.mozaik;

import android.content.Context;
import android.graphics.Point;
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
    private int[] imageId;
    private String[] firstName;
    private String[] lastName;
    private int nbrColumns;
    private float textSize;
    private int nbrItems;

    public ImageAdapter(Context c, String[] firstName, String[] lastName, int[] imageId, int nbrColumns, int nbrItems) {
        mContext = c;
        this.imageId = imageId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.nbrColumns = nbrColumns;
        this.textSize = 30.0f/(float)nbrColumns;
        this.nbrItems = nbrItems;
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

    public View getView(int position, View convertView, ViewGroup parent) {
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
            gridView = inflater.inflate(R.layout.cell_layout, null);

            TextView textViewFirstName = gridView.findViewById(R.id.firstName);
            textViewFirstName.setText(firstName[position]);
            textViewFirstName.setTextSize(textSize);

            TextView textViewLastName = gridView.findViewById(R.id.lastName);
            textViewLastName.setText(lastName[position]);
            textViewLastName.setTextSize(textSize);

            //Toast.makeText(mContext, "textSize = "+textSize+"\nheightFirstname = "+heightFirstName, Toast.LENGTH_SHORT).show();
            ImageView imageView = gridView.findViewById(R.id.grid_item_image);
            imageView.setLayoutParams(new LinearLayout.LayoutParams((int)a4Width, (int)(a4Height - textSize * 9)));
            imageView.setImageResource(imageId[position]);
        }
        else {
            gridView = convertView;
        }
        return gridView;
    }
}