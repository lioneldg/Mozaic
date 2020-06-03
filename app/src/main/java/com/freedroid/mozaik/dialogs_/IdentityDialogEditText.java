package com.freedroid.mozaik.dialogs_;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.freedroid.mozaik.R;

public class IdentityDialogEditText extends AppCompatActivity {

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.identity_dialog_edit_text);

        final EditText firstName = findViewById(R.id.editTextFirstName);
        final EditText lastName = findViewById(R.id.editTextLastName);
        Button buttonOK = findViewById(R.id.buttonOK);

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
