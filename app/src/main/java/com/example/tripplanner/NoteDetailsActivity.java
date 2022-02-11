package com.example.tripplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NoteDetailsActivity extends AppCompatActivity {

    String name;
    String content;
    String nameTrip;
    String numTrips;
    String nameFile;
    ImageView editButton;
    TextView contentTV;
    public static final String TAG = "TAG";

    private FirebaseAuth firebaseAuth;
    String userID;
    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        firebaseAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        numTrips = intent.getStringExtra("numTrips");
        nameTrip = intent.getStringExtra("nameTrip");
        nameFile = intent.getStringExtra("nameFile");

        contentTV = findViewById(R.id.noteContentETId);

        RenderContent();

        editButton = findViewById(R.id.editIconActivitiesId);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(NoteDetailsActivity.this, EditNoteActivity.class);
                intent1.putExtra("nameFile", nameFile);
                intent1.putExtra("numTrips", numTrips);
                intent1.putExtra("nameTrip", nameTrip);
                intent1.putExtra("name", name);
                startActivity(intent1);
            }
        });


        Toolbar toolbar_start = findViewById(R.id.toolbar_start);
        setSupportActionBar(toolbar_start);
        toolbar_start.setTitle(nameFile);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void RenderContent() {

        userID = firebaseAuth.getCurrentUser().getUid();

        root.child("Files-"+userID).child(nameTrip).child(nameFile).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {
                    if(task.getResult().exists()) {
                        DataSnapshot dataSnapshot = task.getResult();
                        Note note = dataSnapshot.getValue(Note.class);
                        content = note.getContent();
                        contentTV.setText(content);
                    }
                    else {
                        //doesn't exist
                        contentTV.setText("No content yet...");
                    }

                }
                else {
                    Toast.makeText(NoteDetailsActivity.this, "Failure!", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                        Intent intent = new Intent(NoteDetailsActivity.this, SignInActivity.class);
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
                intent = new Intent(this, FilesListActivity.class);
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