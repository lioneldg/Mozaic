package com.freedroid.mozaik;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class ExportDialogEditText extends AppCompatActivity {

    EditText editTextFileName = null;
    Button buttonSend = null;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.export_dialog_edit_text);

        editTextFileName = findViewById(R.id.editTextFileName);
        buttonSend = findViewById(R.id.buttonSend);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(editTextFileName.getText().toString().length()<1) Toast.makeText(getApplication(),"Please, type a file name!", Toast.LENGTH_LONG).show();
                else {
                    Intent intent = new Intent();
                    intent.putExtra("fileName", editTextFileName.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
}
