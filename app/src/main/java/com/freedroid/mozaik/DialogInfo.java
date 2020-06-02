package com.freedroid.mozaik;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class DialogInfo extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_info);
        TextView text = findViewById(R.id.textViewInfo);
        Button button = findViewById(R.id.buttonInfo);
        text.setText(getIntent().getStringExtra("text"));   //on récupère le texte passé en extra dans l'intent pour l'afficher

        if(getIntent().getBooleanExtra("needResult", false)) setResult(RESULT_OK);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}