package com.example.user.hakaton;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DealsListActivity extends AppCompatActivity implements View.OnClickListener {

    private DealsAdapter da;
    private RecyclerView dealsList;
    private ImageButton settingsButton;
    private ImageButton usersDealsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deals_list);
        getWindow().setStatusBarColor(getResources().getColor(R.color.DigitalFarmersDark));

        if(getIntent().getStringExtra("WAS").equals("SIGNEDIN")) {
            Intent i = new Intent(this, ProfileActivity.class);
            startActivity(i);
        }

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

        usersDealsButton = (ImageButton) findViewById(R.id.usersDealsButton);
        usersDealsButton.setRotation(45);
        usersDealsButton.setScaleX(0f);
        usersDealsButton.setScaleY(0f);
        usersDealsButton.animate().rotation(0).
                scaleX(1f).
                scaleY(1f).
                setDuration(300).
                setStartDelay(600);
        usersDealsButton.setOnClickListener(this);

        final ArrayList<Deal> data = new ArrayList<Deal>();

        FirebaseDatabase.getInstance().getReference().child("hakaton/deals").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                data.clear();
                for(DataSnapshot i: dataSnapshot.getChildren()) {
                    if(i.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        continue;
                    }
                    for(DataSnapshot j: i.getChildren()) {
                        data.add(new Deal(
                                j.child("offer").getValue(String.class),
                                i.getKey()
                        ));
                    }
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
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.settingsButton:
                startActivity(new Intent(this, ProfileActivity.class));
                break;
            case R.id.usersDealsButton:
                startActivity(new Intent(this, UsersDealsActivity.class));
                break;
            default:
                break;
        }
    }
}
