package com.freedroid.mozaik;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {

    MosaicFragment mosaicFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //si la mémoire est inaccessible on prévient l'utilisateur et on ferme l'application dans onActivityResult
        if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState()) ){
            Intent intent = new Intent(MainActivity.this, DialogInfo.class);
            String str = "Memory is inaccessible!\nTry restarting the application or inserting an SD card";
            intent.putExtra("text",str);
            intent.putExtra("needResult", true);
            startActivityForResult(intent, 864);
        }

        mosaicFragment = new MosaicFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.main, mosaicFragment,"tagMosaicFragment");
        ft.show(mosaicFragment);
        ft.commit();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 864 && resultCode == RESULT_OK) finish();
    }
}
