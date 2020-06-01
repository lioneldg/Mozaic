package com.freedroid.mozaik;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class IdentityDialogEditText extends AppCompatActivity {

    EditText firstName = null;
    EditText lastName = null;
    Button buttonOK = null;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.identity_dialog_edit_text);
        firstName = findViewById(R.id.editTextFirstName);
        lastName = findViewById(R.id.editTextLastName);
        buttonOK = findViewById(R.id.buttonOK);

        buttonOK.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent result = new Intent();
                result.putExtra("firstName", firstName.getText().toString());
                result.putExtra("lastName", lastName.getText().toString());
                setResult(RESULT_OK, result);
                finish();
            }
        });
    }
}