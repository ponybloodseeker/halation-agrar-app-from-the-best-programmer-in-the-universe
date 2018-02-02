package com.example.user.hakaton;

import android.content.Intent;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView formScreenLogo;
    private TextView label;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button logInButton;
    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setStatusBarColor(getResources().getColor(R.color.DigitalFarmersGreen));

        formScreenLogo = (ImageView) findViewById(R.id.formScreenLogo);
        label = (TextView) findViewById(R.id.label);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        logInButton = (Button) findViewById(R.id.logInButton);
        signInButton = (Button) findViewById(R.id.signInButton);

        animateTheElement();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startDealsListActivity("LOGEDIN");
        }

        logInButton.setOnClickListener(this);
        signInButton.setOnClickListener(this);
    }

    private void animateTheElement() {
        formScreenLogo.setAlpha(0f);
        formScreenLogo.setTranslationY(-32f);
        label.setAlpha(0f);
        int width = getWindowManager().getDefaultDisplay().getWidth();
        emailEditText.setTranslationX(-width);
        passwordEditText.setTranslationX(-width);
        logInButton.setAlpha(0f);
        logInButton.setScaleX(0.8f);
        logInButton.setScaleY(0.8f);
        signInButton.setAlpha(0f);
        signInButton.setScaleX(0.8f);
        signInButton.setScaleY(0.8f);

        label.animate().alpha(1f).setDuration(250).setStartDelay(150);
        emailEditText.animate().translationX(0).setDuration(250).setStartDelay(250);
        passwordEditText.animate().translationX(0).setDuration(250).setStartDelay(300);
        logInButton.animate().alpha(1f).
                scaleX(1f).
                scaleY(1f).
                setDuration(250).setStartDelay(400);
        signInButton.animate().alpha(1f).
                scaleX(1f).
                scaleY(1f).
                setDuration(250).setStartDelay(500);
        formScreenLogo.animate().alpha(1f).translationYBy(0).setDuration(500).setStartDelay(1000).start();
    }

    @Override
    public void onClick(View view) {
        if (emailEditText.getText().toString().isEmpty() ||
                passwordEditText.getText().toString().isEmpty()) {
            return;
        }
        switch (view.getId()) {
            case R.id.logInButton:
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString()
                ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        startDealsListActivity("LOGEDIN");
                    }
                });
                break;
            case R.id.signInButton:
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                        emailEditText.getText().toString(),
                        passwordEditText.getText().toString()
                ).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        UserData ud = new UserData(
                                emailEditText.getText().toString(),
                                "Unknown",
                                "Unknown",
                                "Unknown"
                        );
                        FirebaseDatabase.getInstance().getReference().child("hakaton/users").child(
                                FirebaseAuth.getInstance().getCurrentUser().getUid()
                        ).
                                setValue(ud, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if (databaseError != null) {
                                            Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        startDealsListActivity("SIGNEDIN");
                                    }
                                });
                    }
                });
                break;
            default:
                break;
        }
    }

    private void startDealsListActivity(String was) {
        Intent i = new Intent(MainActivity.this, DealsListActivity.class);
        i.putExtra("WAS", was);
        startActivity(i);
        finish();
    }
}
