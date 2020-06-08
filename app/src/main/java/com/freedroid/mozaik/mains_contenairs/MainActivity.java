package com.freedroid.mozaik.mains_contenairs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.freedroid.mozaik.R;
import com.freedroid.mozaik.dialogs_.DialogInfo;
import com.freedroid.mozaik.tools.IO_BitmapImage;

public class MainActivity extends AppCompatActivity {

    MainFragment mainFragment;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle(getString(R.string.layout_1_2));

        //si la mémoire est inaccessible on prévient l'utilisateur et on ferme l'application dans onActivityResult
        testMemoryAccess();
        createMainFragment();
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 864 && resultCode == RESULT_OK) finish();
    }

    private void testMemoryAccess(){
        if(!Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(Environment.getExternalStorageState()) ){
            Intent intent = new Intent(MainActivity.this, DialogInfo.class);
            String str = getString(R.string.memory_inaccessible);
            intent.putExtra("text",str);
            intent.putExtra("needResult", true);
            startActivityForResult(intent, 864);    //demande un résultat pour pouvoir arrêter l'application lors du click sur OK
        }
    }

    private void createMainFragment(){
        mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.main, mainFragment,"tagMosaicFragment").show(mainFragment).commit();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.new_project:
                IO_BitmapImage.deleteCacheFiles(this);  //on vide le cache de l'application
                this.getPreferences(Context.MODE_PRIVATE).edit().clear().apply();
                getSupportFragmentManager().beginTransaction().remove(mainFragment).commit();   //on supprime le MainFragment
                createMainFragment();                                                           //on recrée le MainFragment
                Toast.makeText(this, getString(R.string.new_project), Toast.LENGTH_LONG).show();
                return true;
            case R.id.about:
                aboutDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void aboutDialog(){
        Intent intent = new Intent(MainActivity.this, DialogInfo.class);
        String str = getString(R.string.about_dialog);
        intent.putExtra("text",str);
        intent.putExtra("needResult", false);
        startActivity(intent);
    }
}
