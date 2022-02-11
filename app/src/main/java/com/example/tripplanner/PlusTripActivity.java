package com.example.tripplanner;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PlusTripActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    String season = "winter";
    String reason = "rest";
    String tripName = "";
    String tripDate = "";
    EditText dateET;
    EditText tripNameET;
    String name;
    String numTrips;
    AppCompatButton createTripButton;
    DatePickerDialog.OnDateSetListener setListener;

    private FirebaseAuth firebaseAuth;
    String userID;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference();
    private DatabaseReference root1 = db.getReference().child("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plus_trip);

        firebaseAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        numTrips = intent.getStringExtra("numTrips");

        dateET = findViewById(R.id.tripDate);
        dateET.setInputType(InputType.TYPE_NULL);
        Calendar calendar = Calendar.getInstance();
        dateET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);

                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

                        dateET.setText(simpleDateFormat.format(calendar.getTime()));
                        tripDate = simpleDateFormat.format(calendar.getTime());
                        dateET.setTextColor(getResources().getColor(R.color.darkest));
                    }
                };
                new DatePickerDialog(PlusTripActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });



        tripNameET = findViewById(R.id.tripName);

        createTripButton = findViewById(R.id.addTripButton);
        createTripButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                createTrip();
            }
        });

        Toolbar toolbar_start = findViewById(R.id.toolbar_start);
        setSupportActionBar(toolbar_start);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar_start.setLogo(R.drawable.logo);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void createTrip() {
        tripName = tripNameET.getText().toString().trim();

        userID = firebaseAuth.getCurrentUser().getUid();

        if(tripName.equals("")) {
            tripNameET.setError("please enter trip name");
        }
        else if(tripDate.equals("")) {
            dateET.setError("please enter trip date");
        }
        else {
            TripDetails details = new TripDetails(userID, numTrips, tripName, tripDate, season, reason);
            root.child("TripDetails-"+userID).child(numTrips).setValue(details);

            //update numTrips in Users +1
            int n = Integer.parseInt(numTrips)+1;
            root1.child(userID).child("numTrips").setValue(n);

            Intent intent = new Intent(PlusTripActivity.this, TripsListActivity.class);
            intent.putExtra("name", name);
            intent.putExtra("numTrips", String.valueOf(n));
            startActivity(intent);
            finish();
        }

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
                        Intent intent = new Intent(PlusTripActivity.this, SignInActivity.class);
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
            case android.R.id.home:
                intent = new Intent(this, TripsListActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("numTrips", numTrips);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }


    public void radioClick(View view) {
        if(view.getId()==R.id.seasonRB1) {
            season = "winter";
        }
        else if(view.getId()==R.id.seasonRB2) {
            season = "spring";
        }
        else if(view.getId()==R.id.seasonRB3) {
            season = "summer";
        }
        else if(view.getId()==R.id.seasonRB4) {
            season = "autumn";
        }
    }

    public void radioClick1(View view) {
        if(view.getId()==R.id.reasonRB1) {
            reason = "work";
        }
        else if(view.getId()==R.id.reasonRB2) {
            reason = "friends";
        }
        else if(view.getId()==R.id.reasonRB3) {
            reason = "rest";
        }
    }
}