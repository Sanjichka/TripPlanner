package com.example.tripplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    private EditText emailET, passwordET, passwordET1;
    private Button SignUpBtn;
    private TextView SignInTV;
    private ProgressDialog progrDialog;
    private FirebaseAuth firebaseAuth;
    String userID;
    private EditText fullnameET;

    private FirebaseDatabase db = FirebaseDatabase.getInstance();
    private DatabaseReference root = db.getReference().child("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        firebaseAuth = FirebaseAuth.getInstance();
        emailET = findViewById(R.id.email_up);
        passwordET = findViewById(R.id.password_up);
        passwordET1 = findViewById(R.id.confirm_password_up);
        fullnameET = findViewById(R.id.fullname_up);
        SignUpBtn = findViewById(R.id.signUpButton);
        progrDialog = new ProgressDialog(this);


        SignInTV = findViewById(R.id.signInTV);
        SignUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                SignUp();


            }
        });
        SignInTV.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
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
        inflater.inflate(R.menu.menu_signin, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        Intent intent = null;

        switch (item.getItemId()) {
            case R.id.action_signin:
                intent = new Intent(this, SignInActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }



    private void SignUp() {
        String email = emailET.getText().toString().trim();
        String password = passwordET.getText().toString().trim();
        String password1 = passwordET1.getText().toString().trim();
        String fullname = fullnameET.getText().toString().trim();
        if(TextUtils.isEmpty(email)) {
            emailET.setError("Enter your email!");
            return;
        }
        else if(TextUtils.isEmpty(password)) {
            passwordET.setError("Enter your password!");
            return;
        }
        else if(TextUtils.isEmpty(password1)) {
            passwordET1.setError("Confirm your password!");
            return;
        }
        else if(password.length()<4) {
            passwordET.setError("Length should be >4!");
            return;
        }
        else if(!password1.equals(password)) {
            passwordET1.setError("Incorrect confirmation!");
            return;
        }
        else if(!isValidEmail(email)) {
            emailET.setError("Invalid email");
            return;
        }



        progrDialog.setMessage("Please wait...");
        progrDialog.show();
        progrDialog.setCanceledOnTouchOutside(false);
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Successfully registered", Toast.LENGTH_LONG).show();

                    userID = firebaseAuth.getCurrentUser().getUid();

                    User user = new User(email, fullname, 0);

                    root.child(userID).setValue(user);

                    Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    finish();

                }
                else {
                    Toast.makeText(SignUpActivity.this, "Error! " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
                progrDialog.dismiss();
            }
        });


    }

    private boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }


}