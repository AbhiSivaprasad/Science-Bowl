package com.abhi.android.sciencebowl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Abhinand on 6/22/2016.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText mUsernameButton;
    private Button mLoginConfirmationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsernameButton = (EditText) findViewById(R.id.username);

        mLoginConfirmationButton = (Button) findViewById(R.id.login_confirmation);
        mLoginConfirmationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String username = mUsernameButton.getText().toString();
                UserInformation.setUsername(username);

                Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
    }
}
