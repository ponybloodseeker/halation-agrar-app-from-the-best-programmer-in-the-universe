package com.example.user.hakaton;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsersDealsActivity extends AppCompatActivity implements View.OnClickListener {

    private DealsAdapter da;
    private RecyclerView dealsList;
    private ImageButton settingsButton;
    private ImageButton addNewDealButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_deals);
        getWindow().setStatusBarColor(getResources().getColor(R.color.DigitalFarmersDark));

        settingsButton = (ImageButton) findViewById(R.id.settingsButton);
        settingsButton.setRotation(45);
        settingsButton.setScaleX(0f);
        settingsButton.setScaleY(0f);
        settingsButton.animate().rotation(0).
                scaleX(1f).
                scaleY(1f).
                setDuration(300).
                setStartDelay(500);
        settingsButton.setOnClickListener(this);

        addNewDealButton = (ImageButton) findViewById(R.id.addNewDealButton);
        addNewDealButton.setRotation(45);
        addNewDealButton.setScaleX(0f);
        addNewDealButton.setScaleY(0f);
        addNewDealButton.animate().rotation(0).
                scaleX(1f).
                scaleY(1f).
                setDuration(300).
                setStartDelay(600);
        addNewDealButton.setOnClickListener(this);

        final ArrayList<Deal> data = new ArrayList<Deal>();

        FirebaseDatabase.getInstance().getReference().child("hakaton/deals").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        ).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();
                    for(DataSnapshot j: dataSnapshot.getChildren()) {
                        data.add(new Deal(
                                j.child("offer").getValue(String.class),
                                null
                        ));
                    }
                da.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        da = new DealsAdapter(this, data);
        dealsList = (RecyclerView) findViewById(R.id.dealsList);
        dealsList.setLayoutManager(new LinearLayoutManager(this));
        dealsList.setAdapter(da);
        FirebaseDatabase.getInstance().getReference().child("hakaton/users").child(
                FirebaseAuth.getInstance().getCurrentUser().getUid()
        ).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ((TextView) findViewById(R.id.titleAtTheTop)).setText(
                        dataSnapshot.child("name").getValue(String.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.settingsButton:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.addNewDealButton:
                startActivity(new Intent(this, DealFactoryActivity.class));
                break;
            default:
                break;
        }
    }
}
