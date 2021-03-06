package com.example.joshua.ljc;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private com.google.android.gms.common.SignInButton googleSignInBtn;
    private static final int RC_SIGN_IN = 0;
    private GoogleApiClient googleApiClient;
    private FirebaseAuth authentication;
    private static final String TAG = "LoginActivity Activity";
    private FirebaseAuth.AuthStateListener authorisationListener;
    private DatabaseReference database;
    private Toast toast;
    private ProgressDialog progressDialog;
    private View userGuide;
    private Button closeGuide;
    private ImageButton informationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);;
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);
        database = FirebaseDatabase.getInstance().getReference().child("Users");
        authentication = FirebaseAuth.getInstance();
        userGuide = findViewById(R.id.guide_alertDialogue);
        closeGuide = (Button) userGuide.findViewById(R.id.guideDialog_Confirm_Button);
        informationButton = (ImageButton) findViewById(R.id.information_ImageButton);
        toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        authorisationListener = new FirebaseAuth.AuthStateListener(){

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null){
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (authentication.getCurrentUser().getUid().equals("4LjgxfVwYoWGevl4MOBl8jLjRX52")) {
                        startActivity(new Intent(LoginActivity.this, MainMenuActivity.class));
                    }else{
                        toast.setText("You don't have permission to access this application.");
                        toast.show();
                    }
                    progressDialog.hide();
                }
            }
        };

        informationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userGuide.setVisibility(View.VISIBLE);
            }
        });

        closeGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userGuide.setVisibility(View.INVISIBLE);
            }
        });

        //Initialise interface
        googleSignInBtn = (com.google.android.gms.common.SignInButton) findViewById(R.id.google_Button);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(getApplicationContext()).enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
            @Override
            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                Toast.makeText(LoginActivity.this, "An error occured", Toast.LENGTH_LONG).show();
            }
        }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signIn();

            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
        authentication.addAuthStateListener(authorisationListener);
    }

    private void signIn() {
        Auth.GoogleSignInApi.signOut(googleApiClient);
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        progressDialog.setTitle("LJC Construction administration application.");
        progressDialog.setMessage("Retrieving data...");
        progressDialog.show();
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                progressDialog.hide();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        authentication.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });

    }
}
