package com.freedroid.mozaik;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    FragmentManager fm = null;
    FragmentTransaction ft = null;
    MosaicFragment mosaicFragment = null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mosaicFragment = new MosaicFragment();
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.main, mosaicFragment,"tagMosaicFragment");
        ft.show(mosaicFragment);
        ft.commit();



    }
}
