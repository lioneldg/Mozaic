package com.freedroid.mozaik;

import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import java.util.Objects;
import static android.app.Activity.RESULT_OK;


public class MosaicFragment extends Fragment {

    private String[] firstName = {"firstName","firstName","firstName","firstName","firstName","firstName","firstName","firstName","firstName","firstName","firstName","firstName","firstName","firstName","firstName","firstName","firstName","firstName","firstName","firstName"};
    private String[] lastName = {"lastName","lastName","lastName","lastName","lastName","lastName","lastName","lastName","lastName","lastName","lastName","lastName","lastName","lastName","lastName","lastName","lastName","lastName","lastName","lastName"};
    private int[] imageId = new int[20];
    private ImageView imageCell = null;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.mosaic_fragment_layout,container, false);

        final ImageAdapter adapter = new ImageAdapter(getContext(), firstName, lastName, imageId);
        GridView grid = view.findViewById(R.id.PhotoGridView);

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
        }
    }

    private void choosePicture(){
        Intent intent = new Intent()
                .setType("*/*")
                .setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select a file"), 123);
    }
}
