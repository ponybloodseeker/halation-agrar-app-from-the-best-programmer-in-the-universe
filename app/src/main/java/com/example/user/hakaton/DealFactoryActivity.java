package com.example.user.hakaton;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DealFactoryActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView dealFactoryLabel;
    private EditText dealFactoryIWantEditText;
    private Button cancelButton;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deal_factory);
        getWindow().setStatusBarColor(getResources().getColor(R.color.DigitalFarmersDark));

        dealFactoryLabel = (TextView) findViewById(R.id.dealFactoryLabel);
        dealFactoryIWantEditText = (EditText) findViewById(R.id.dealFactoryIWantEditField);
        cancelButton = (Button) findViewById(R.id.cancelDealButton);
        addButton = (Button) findViewById(R.id.addDealButton);

        int width = getWindowManager().getDefaultDisplay().getWidth();
        dealFactoryLabel.setTranslationX(width);
        dealFactoryLabel.setAlpha(0f);
        dealFactoryLabel.animate().alpha(1f).
                translationX(0).
                setDuration(200).
                setStartDelay(250);
        dealFactoryIWantEditText.setTranslationX(-width);
        dealFactoryIWantEditText.setAlpha(0f);
        dealFactoryIWantEditText.animate().alpha(1f).
                translationX(0).
                setDuration(200).
                setStartDelay(450);
        cancelButton.setScaleX(1.2f);
        cancelButton.setScaleY(1.2f);
        cancelButton.setAlpha(0f);
        cancelButton.animate().alpha(1f).
                scaleX(1f).
                scaleY(1f).
                setDuration(200).
                setStartDelay(600);
        addButton.setScaleX(1.2f);
        addButton.setScaleY(1.2f);
        addButton.setAlpha(0f);
        addButton.animate().alpha(1f).
                scaleX(1f).
                scaleY(1f).
                setDuration(200).
                setStartDelay(750);
        addButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.cancelDealButton:
                finish();
                break;
            case R.id.addDealButton:
                FirebaseDatabase.getInstance().getReference().child("hakaton/deals").child(
                        FirebaseAuth.getInstance().getCurrentUser().getUid()
                ).push().child("offer").setValue(dealFactoryIWantEditText.getText().toString(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        finish();
                    }
                });
                break;
            default:
                break;
        }
    }
}
