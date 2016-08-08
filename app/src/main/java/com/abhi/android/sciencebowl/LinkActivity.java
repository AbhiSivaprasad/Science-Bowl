package com.abhi.android.sciencebowl;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.List;
public class LinkActivity extends AppCompatActivity
        implements GoogleApiClient.OnConnectionFailedListener{

    private static final int RC_SIGN_IN =9001 ;
    private static final String TAG = "LOGIN";
    private static final String DEF_SUBJECT = "111111";
    private static final int DEF_DIFFICULTY = 3;
    private EditText etEmail;
    private EditText etPass;
    private Button bLogin;
    private SignInButton bGSignIn;
    private Button bOpenEmail;

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private CallbackManager mCallbackManager;
    private LoginButton loginButton;
    private FrameLayout spinHolder;
    private AuthCredential credential;
    boolean go = true;
    boolean first = true;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_link);

        spinHolder = (FrameLayout) findViewById(R.id.spinHolder);

        mCallbackManager = CallbackManager.Factory.create();

        loginButton = (LoginButton) findViewById(R.id.button_facebook_login);
        loginButton.setReadPermissions("email", "public_profile");
        LoginManager.getInstance().logOut();
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                openAnimation();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                Toast.makeText(LinkActivity.this,"Sign-in failed.",Toast.LENGTH_SHORT).show();
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                Toast.makeText(LinkActivity.this,"Sign-in error.",Toast.LENGTH_SHORT).show();
                // ...
            }
        });

        bGSignIn = (SignInButton) findViewById(R.id.bGSignIn);
        bGSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(first){
                    first = false;
                    return;
                }
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    // User is signed in
                    LinkTask t = new LinkTask();
                    t.execute();
                    //logIn(currentUser);
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + currentUser.getUid());
                }
                // ...
            }
        };
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        bOpenEmail = (Button) findViewById(R.id.bOpenEmail);
        bOpenEmail.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                findViewById(R.id.signInHolder).setVisibility(View.VISIBLE);
            }
        });
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPass = (EditText) findViewById(R.id.etPass);
        bLogin = (Button) findViewById(R.id.bSignIn);
        bLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = etEmail.getText().toString();
                final String pass = etPass.getText().toString();
                if(email.isEmpty()||pass.isEmpty()){
                    Toast t = Toast.makeText(LinkActivity.this,"Please enter both an email and password.", Toast.LENGTH_LONG);
                    t.show();
                    return;
                }
                credential = EmailAuthProvider.getCredential(email,pass);
                LinkTask login = new LinkTask();
                login.execute();
            }
        });
    }

    private void signIn() {
        openAnimation();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                //firebaseAuthWithGoogle();
                LinkTask login = new LinkTask();
                login.execute();
            }else{
                Toast.makeText(LinkActivity.this, result.getStatus().getStatusCode()+ ": " + result.getStatus().getStatusMessage(),Toast.LENGTH_SHORT).show();
                closeAnimation();
            }

        }else{
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        credential = FacebookAuthProvider.getCredential(token.getToken());
        LinkTask login = new LinkTask();
        login.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }


    private void openAnimation(){
        AlphaAnimation inAnimation = new AlphaAnimation(0f, 1f);
        inAnimation.setDuration(200);
        spinHolder.setAnimation(inAnimation);
        spinHolder.setVisibility(View.VISIBLE);
    }

    private void closeAnimation(){
        AlphaAnimation outAnimation = new AlphaAnimation(1f, 0f);
        outAnimation.setDuration(200);
        spinHolder.setAnimation(outAnimation);
        spinHolder.setVisibility(View.GONE);
    }
    class LinkTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            openAnimation();
        }

        @Override
        protected Void doInBackground(Void... bools) {
            mAuth.getCurrentUser().linkWithCredential(credential).addOnCompleteListener(LinkActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        Toast.makeText(LinkActivity.this, "Link failed.",
                                Toast.LENGTH_SHORT).show();
                        LoginManager.getInstance().logOut();
                        return;
                    }
                    Toast.makeText(LinkActivity.this, "Link successful!",
                            Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            closeAnimation();
            if(!go)
                return;
        }
    }
}
