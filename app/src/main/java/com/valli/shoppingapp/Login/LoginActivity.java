package com.valli.shoppingapp.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.valli.shoppingapp.Dashboard;
import com.valli.shoppingapp.Constants;
import com.valli.shoppingapp.R;

public class LoginActivity extends CommonActivity {

    private EditText email, pass;
    private Button btnLogin;
    private FirebaseAuth firebaseAuth;
    String email_from_db;
    private FirebaseDatabase mCloudDb;
    private long current_value;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        setUpViews();
        intentCheck();
        setUpValues();
    }

    public void intentCheck() {
//        if(firebaseAuth.getCurrentUser().getEmail() != null)
//            email_from_db = firebaseAuth.getCurrentUser().getEmail();
//        else
        email_from_db = pref.getString(Constants.USER_EMAIL, "");
        Log.e("Check email ", email_from_db);
        current_value = getIntent().getLongExtra(getString(R.string.LAST_REGISTERATION), 0);
    }


    private void setUpValues() {
        Log.e("Check ", email_from_db);
        email.setText(email_from_db);
//        pass.setText("123456");
    }

    private void setUpViews() {
        mCloudDb = FirebaseDatabase.getInstance();
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        btnLogin = findViewById(R.id.login);
        btnLogin.setOnClickListener((v) -> login());
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void login() {
        if (pass.getText().toString().isEmpty() || email.getText().toString().isEmpty()) {
            showAlert("Fields cannot be empty");
        } else {
            showProgress("Loading..");
            editor.putString(Constants.USER_EMAIL, email.getText().toString());
            editor.commit();
            firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    removeProgress();
                    checkIfEmailVerified();
                } else {
                    removeProgress();
                    showAlert(task.getException().getMessage());
                }
            });
//                    .addOnSuccessListener(authResult -> {
//                        String userID = authResult.getUser().getUid();
//                    });

        }
    }

    private void checkIfEmailVerified() {
        Intent intent = new Intent(LoginActivity.this, Dashboard.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        overridePendingTransition(0,0);

    }
}
