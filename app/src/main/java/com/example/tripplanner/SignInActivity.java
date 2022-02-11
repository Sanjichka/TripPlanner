package com.example.tripplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;


public class SignInActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    private EditText emailET, passwordET;
    private Button SignInBtn;
    private TextView SignUpTV;
    private ProgressDialog progrDialog;
    private FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    String userID;
    String name = "";
    String numTrips = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        Intent in = getIntent();
        String e = in.getStringExtra("email");

        firebaseAuth = FirebaseAuth.getInstance();
        emailET = findViewById(R.id.email_in);
        passwordET = findViewById(R.id.password_in);
        if(e != null) {
            emailET.setText(e);
        }

        SignInBtn = findViewById(R.id.signInButton);
        progrDialog = new ProgressDialog(this);
        SignUpTV = findViewById(R.id.signUpTV);
        SignInBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                SignIn();
            }
        });
        SignUpTV.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
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
        inflater.inflate(R.menu.menu_signup, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent = null;

        switch (item.getItemId()) {
            case R.id.action_signup:
                intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void SignIn() {
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        if(TextUtils.isEmpty(email)) {
            emailET.setError("Enter your email!");
            return;
        }
        else if(TextUtils.isEmpty(password)) {
            passwordET.setError("Enter your password!");
            return;
        }
        progrDialog.setMessage("Please wait...");
        progrDialog.show();
        progrDialog.setCanceledOnTouchOutside(false);
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(SignInActivity.this, "Successfully logged in", Toast.LENGTH_LONG).show();

                    userID = firebaseAuth.getCurrentUser().getUid();
                    //READ DATA

                    reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
                    reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if(task.isSuccessful()) {
                                if(task.getResult().exists()) {
                                    DataSnapshot dataSnapshot = task.getResult();
                                    User user = dataSnapshot.getValue(User.class);
                                    name = user.getFullName();
                                    numTrips = String.valueOf(user.getNumTrips());
                                    Intent intent = new Intent(SignInActivity.this, TripsListActivity.class);
                                    Log.d(TAG, "on SuccessSSSSSSSSSSSSSSSSSSSSSSSSSSS: " + name);
                                    intent.putExtra("name", name);
                                    intent.putExtra("numTrips", numTrips);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    Toast.makeText(SignInActivity.this, "User doesn't exist!", Toast.LENGTH_SHORT).show();
                                }

                            }
                            else {
                                Toast.makeText(SignInActivity.this, "Failure!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else {
                    Toast.makeText(SignInActivity.this, "Sign In fail!", Toast.LENGTH_LONG).show();
                }
                progrDialog.dismiss();
            }
        });
    }
}