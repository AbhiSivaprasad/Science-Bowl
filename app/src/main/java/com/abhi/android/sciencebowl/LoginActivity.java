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
    private static final String EXTRA_USERNAME =
            "com.abhi.android.sb.username";

    private EditText mUsernameButton;
    private Button mLoginConfirmationButton;

    private static String mUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mUsernameButton = (EditText) findViewById(R.id.username);
        mUsernameButton.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mUsername = charSequence.toString();
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        mLoginConfirmationButton = (Button) findViewById(R.id.login_confirmation);
        mLoginConfirmationButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = MainMenuActivity.newIntent(LoginActivity.this, mUsername);
                startActivity(intent);
            }
        });
    }


    public static String getUsername(Intent data) {
        return data.getStringExtra(EXTRA_USERNAME);
    }
}
