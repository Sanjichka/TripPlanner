package com.example.tripplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TripsListActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    private RecyclerView mRecyclerView;
    private TripsAdapter mAdapter;
    ArrayList<String> tripNamesArray = new ArrayList<String>();
    ArrayList<String> tripDatesArray = new ArrayList<String>();
    ArrayList<String> tripSeasonArray = new ArrayList<String>();
    ArrayList<String> tripReasonArray = new ArrayList<String>();
    ArrayList<String> userIDArray = new ArrayList<String>();
    ArrayList<String> docIDArray = new ArrayList<String>();

    TextView whoseList;
    ImageView plusIcon;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root;
    String userID;
    String name;
    String numTrips;
    String nameTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips_list);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        nameTrip = intent.getStringExtra("nameTrip");
        numTrips = intent.getStringExtra("numTrips");


        firebaseAuth = FirebaseAuth.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        root = db.getReference().child("TripDetails-"+userID);

        root.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userIDArray.clear();
                    docIDArray.clear();
                    tripDatesArray.clear();
                    tripNamesArray.clear();
                    tripReasonArray.clear();
                    tripSeasonArray.clear();
                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        TripDetails details = dataSnapshot.getValue(TripDetails.class);
                        userIDArray.add(details.userID);
                        docIDArray.add(details.docID);
                        tripNamesArray.add(details.name);
                        tripDatesArray.add(details.date);
                        tripSeasonArray.add(details.season);
                        tripReasonArray.add(details.reason);
                    }
                   mRecyclerView = (RecyclerView) findViewById(R.id.listTripsRV);
                   mRecyclerView.setHasFixedSize(true);
                   mRecyclerView.setLayoutManager(new LinearLayoutManager(TripsListActivity.this));
                   mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                   mAdapter = new TripsAdapter(TripsListActivity.this, userIDArray, docIDArray, tripNamesArray, tripDatesArray, tripSeasonArray, tripReasonArray, name, numTrips);
                   mRecyclerView.setAdapter(mAdapter);
               }

               @Override
               public void onCancelled(@NonNull DatabaseError error) {

               }
           });

        whoseList = findViewById(R.id.listTripsText);
        whoseList.setText(name + "'s Trip List");


        plusIcon = findViewById(R.id.plusIconId);

        plusIcon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TripsListActivity.this, PlusTripActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("numTrips", numTrips);
                startActivity(intent);
            }
        });

        Toolbar toolbar_start = findViewById(R.id.toolbar_start);
        setSupportActionBar(toolbar_start);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_start.setLogo(R.drawable.logo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_signout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent = null;

        switch (item.getItemId()) {
            case R.id.action_signout:
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Sign out");
                alert.setMessage("Are you sure you want to sign out?");
                alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(TripsListActivity.this, SignInActivity.class);
                        startActivity(intent);
                    }
                });
                alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // close dialog
                        dialog.cancel();
                    }
                });
                alert.show();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}