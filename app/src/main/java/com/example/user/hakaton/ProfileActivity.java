package com.example.user.hakaton;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText name;
    private EditText telephone;
    private EditText location;
    private Button locationButton;
    private Button saveButton;
    private Button cancelButton;
    private TextView signOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getWindow().setStatusBarColor(getResources().getColor(R.color.DigitalFarmersDark));

        name = (EditText) findViewById(R.id.userNameEditText);
        telephone = (EditText) findViewById(R.id.phoneNumberEditText);
        location = (EditText) findViewById(R.id.locationField);
        locationButton = (Button) findViewById(R.id.getMyLocationButton);
        saveButton = (Button) findViewById(R.id.saveProfileButton);
        cancelButton = (Button) findViewById(R.id.cancelProfileButton);
        signOutButton = (TextView) findViewById(R.id.signOutButton);

        saveButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        signOutButton.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseDatabase.getInstance().getReference().child("hakaton/users").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        ).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserData ud = dataSnapshot.getValue(UserData.class);
                name.setText(ud.name);
                telephone.setText(ud.telephone);
                location.setText(ud.location);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.saveProfileButton:
                UserData ud = new UserData(
                        FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                        telephone.getText().toString(),
                        location.getText().toString(),
                        name.getText().toString());
                FirebaseDatabase.getInstance().getReference().child("hakaton/users").child(
                        FirebaseAuth.getInstance().getCurrentUser().getUid()
                ).setValue(ud, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError != null) {
                            Toast.makeText(getApplicationContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }
                });
                break;
            case R.id.cancelProfileButton:
                finish();
                break;
            case R.id.signOutButton:
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish();
                break;
            default:
                break;
        }
    }
}
