package com.abhi.android.sciencebowl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by Abhinand on 6/22/2016.
 */
public class StartupActivity extends AppCompatActivity {

    Button mLoginButton;
    Button mSignupButton; //Generate default user settings during signup

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        mLoginButton = (Button) findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginStartupIntent = new Intent(StartupActivity.this, LoginActivity.class);
                startActivity(loginStartupIntent);
            }
        });
    }
}
