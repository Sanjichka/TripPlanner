package com.example.tripplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class FilesListActivity extends AppCompatActivity {

    String name;
    String nameTrip;
    String numTrips;
    String nameFile;
    TextView whichTrip;
    CardView activitiesCV, placesCV, clothesCV, accessoriesCV, shoppingListCV, mealIdeasCV, buyFoodCV,takeFoodCV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_files_list);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        nameTrip = intent.getStringExtra("nameTrip");
        numTrips = intent.getStringExtra("numTrips");

        whichTrip = findViewById(R.id.listFilesText);
        whichTrip.setText("Trip Name: " + nameTrip);

        activitiesCV = findViewById(R.id.activitiesToDoCV);
        placesCV = findViewById(R.id.placesToVisitCV);
        clothesCV = findViewById(R.id.clothesCV);
        accessoriesCV = findViewById(R.id.accessoriesCV);
        shoppingListCV = findViewById(R.id.shoppingListCV);
        mealIdeasCV = findViewById(R.id.mealIdeasCV);
        buyFoodCV = findViewById(R.id.buyFoodCV);
        takeFoodCV = findViewById(R.id.takeFoodCV);

        activitiesCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameFile = "Activities to do";
                Intent intent1 = new Intent(FilesListActivity.this, NoteDetailsActivity.class);
                intent1.putExtra("name", name);
                intent1.putExtra("nameTrip", nameTrip);
                intent1.putExtra("numTrips", numTrips);
                intent1.putExtra("nameFile", nameFile);
                startActivity(intent1);
            }
        });

        placesCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameFile = "Places to visit";
                Intent intent1 = new Intent(FilesListActivity.this, NoteDetailsActivity.class);
                intent1.putExtra("name", name);
                intent1.putExtra("nameTrip", nameTrip);
                intent1.putExtra("numTrips", numTrips);
                intent1.putExtra("nameFile", nameFile);
                startActivity(intent1);
            }
        });

        clothesCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameFile = "Clothes";
                Intent intent1 = new Intent(FilesListActivity.this, NoteDetailsActivity.class);
                intent1.putExtra("name", name);
                intent1.putExtra("nameTrip", nameTrip);
                intent1.putExtra("numTrips", numTrips);
                intent1.putExtra("nameFile", nameFile);
                startActivity(intent1);
            }
        });

        accessoriesCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameFile = "Accessories";
                Intent intent1 = new Intent(FilesListActivity.this, NoteDetailsActivity.class);
                intent1.putExtra("name", name);
                intent1.putExtra("nameTrip", nameTrip);
                intent1.putExtra("numTrips", numTrips);
                intent1.putExtra("nameFile", nameFile);
                startActivity(intent1);
            }
        });

        shoppingListCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameFile = "Shopping List";
                Intent intent1 = new Intent(FilesListActivity.this, NoteDetailsActivity.class);
                intent1.putExtra("name", name);
                intent1.putExtra("nameTrip", nameTrip);
                intent1.putExtra("numTrips", numTrips);
                intent1.putExtra("nameFile", nameFile);
                startActivity(intent1);
            }
        });

        mealIdeasCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameFile = "Meal Ideas";
                Intent intent1 = new Intent(FilesListActivity.this, NoteDetailsActivity.class);
                intent1.putExtra("name", name);
                intent1.putExtra("nameTrip", nameTrip);
                intent1.putExtra("numTrips", numTrips);
                intent1.putExtra("nameFile", nameFile);
                startActivity(intent1);
            }
        });

        buyFoodCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameFile = "Food to buy";
                Intent intent1 = new Intent(FilesListActivity.this, NoteDetailsActivity.class);
                intent1.putExtra("name", name);
                intent1.putExtra("nameTrip", nameTrip);
                intent1.putExtra("numTrips", numTrips);
                intent1.putExtra("nameFile", nameFile);
                startActivity(intent1);
            }
        });

        takeFoodCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameFile = "Food to take";
                Intent intent1 = new Intent(FilesListActivity.this, NoteDetailsActivity.class);
                intent1.putExtra("name", name);
                intent1.putExtra("nameTrip", nameTrip);
                intent1.putExtra("numTrips", numTrips);
                intent1.putExtra("nameFile", nameFile);
                startActivity(intent1);
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
                        Intent intent = new Intent(FilesListActivity.this, SignInActivity.class);
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
                intent.putExtra("nameTrip", nameTrip);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}