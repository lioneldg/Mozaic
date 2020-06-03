package com.freedroid.mozaik.dialogs_;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.freedroid.mozaik.R;

public class ExportDialogEditText extends AppCompatActivity {

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.export_dialog_edit_text);

        final String text = getString(R.string.please_type_a_file_name);
        final EditText editTextFileName = findViewById(R.id.editTextFileName);
        Button buttonSend = findViewById(R.id.buttonSend);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(editTextFileName.getText().toString().length()<1) Toast.makeText(getApplication(),text, Toast.LENGTH_LONG).show();
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
