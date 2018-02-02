package com.example.user.hakaton;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DealsAdapter extends RecyclerView.Adapter<DealsAdapter.DealViewHolder> implements View.OnClickListener {

    private Context ctx;
    private ArrayList<Deal> dealsData;

    @Override
    public void onClick(final View view) {
        String ownerUID = ((TextView) view.findViewById(R.id.hiddenOwnerUID)).getText().toString();
        Log.d("ED SHEERAN", ownerUID);
        if(ownerUID.isEmpty()) {
            return;
        }
        final AlertDialog.Builder ab = new AlertDialog.Builder(ctx);
        final View v = LayoutInflater.from(ctx).inflate(R.layout.details_alert_layout, null, false);
        ab.setView(v);
        final AlertDialog readyAd = ab.create();

        FirebaseDatabase.getInstance().getReference().child("hakaton/users").
                child(ownerUID).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String name = dataSnapshot.child("name").getValue(String.class);
                        String telephone = dataSnapshot.child("telephone").getValue(String.class);
                        String whatOffers = ((TextView) view.findViewById(R.id.offeres)).getText().toString();
                        String location = dataSnapshot.child("location").getValue(String.class);

                        ((TextView) v.findViewById(R.id.detailUserName)).setText(name);
                        ((TextView) v.findViewById(R.id.detailTelephone)).setText(telephone);
                        ((TextView) v.findViewById(R.id.whatOffers)).setText(whatOffers);

                        ((Button) v.findViewById(R.id.leaveDetailButton)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                readyAd.dismiss();
                            }
                        });
                        ((Button) v.findViewById(R.id.confirmButton)).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Toast.makeText(ctx, "Your offer has been sent!", Toast.LENGTH_SHORT).show();
                                readyAd.dismiss();
                                Log.d("OFFER RESPONDET", "FUTHER IMPLEMENTATION IS EXPECTED");
                            }
                        });
                        readyAd.show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {}
                });
    }

    public class DealViewHolder extends RecyclerView.ViewHolder {

        public TextView userName;
        public TextView offers;
        public TextView hiddenOwnerUID;

        public DealViewHolder(View i) {
            super(i);
            userName = (TextView) i.findViewById(R.id.userName);
            offers = (TextView) i.findViewById(R.id.offeres);
            hiddenOwnerUID = (TextView) i.findViewById(R.id.hiddenOwnerUID);
        }
    }

    public DealsAdapter(Context ctx, ArrayList<Deal> dealsData) {
        this.dealsData = dealsData;
        this.ctx = ctx;
    }

    @Override
    public DealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = (LayoutInflater.from(parent.getContext())).
                inflate(R.layout.item_deal_layout, parent, false);
        v.setOnClickListener(this);
        return new DealViewHolder(v);
    }

    @Override
    public void onBindViewHolder(DealViewHolder holder, int position) {
        Deal d = dealsData.get(position);
        holder.userName.setText("username");
        holder.offers.setText(d.getWhatOffer());
        holder.hiddenOwnerUID.setText(d.getOwnersUID());
    }

    @Override
    public int getItemCount() {
        return dealsData.size();
    }
}
