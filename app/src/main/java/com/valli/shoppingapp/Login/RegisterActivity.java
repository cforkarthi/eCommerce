package com.valli.shoppingapp.Login;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.valli.shoppingapp.Constants;
import com.valli.shoppingapp.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

import static com.valli.shoppingapp.Constants.user_ID;

public class RegisterActivity extends CommonActivity {

    // store the registered user details into database
    // FireBase url - "https://console.firebase.google.com/project/shopping-app-dc998/database/shopping-app-dc998/data"


    private EditText username, dob, mobile, address, email, password;
    private TextView submit;
    private FirebaseAuth firebaseAuth;
    private String uName, uDob, uMobile, uAdrs, uEmail, uPass;
    private DatabaseReference db;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
        setUpViews();
        setUpValues();
    }

    private void setUpViews() {
        username = findViewById(R.id.uname);
        dob = findViewById(R.id.dob);
        mobile = findViewById(R.id.mobile);
        address = findViewById(R.id.address);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        submit = findViewById(R.id.submit);
//        progressDialog = new ProgressDialog(RegisterActivity.this);
        dob.setOnClickListener(v -> new DatePickerDialog(RegisterActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show());
    }

    private void setUpValues() {
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference().child("users");
        submit.setOnClickListener(v -> {
            registerUser();
        });
    }

    final Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
        // TODO Auto-generated method stub
        myCalendar.set(Calendar.YEAR, year);
        myCalendar.set(Calendar.MONTH, monthOfYear);
        myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateLabel();
    };

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        dob.setText(sdf.format(myCalendar.getTime()));

    }

    private void registerUser() {
        uName = username.getText().toString();
        uDob = dob.getText().toString();
        uMobile = mobile.getText().toString();
        uAdrs = address.getText().toString();
        uEmail = email.getText().toString();
        uPass = password.getText().toString();

        DatabaseReference newUserRef = db.push();

        String postId = newUserRef.getKey();
        Log.e("Check register userId ", postId);
        editor.putString(user_ID, postId);
        editor.commit();
        //Registering User
        if (uName.isEmpty() || uDob.isEmpty() || uMobile.isEmpty() || uAdrs.isEmpty() || uEmail.isEmpty() || uPass.isEmpty()) {
            showAlert("Fields cannot be empty");
        } else if (!android.util.Patterns.PHONE.matcher(uMobile).matches() || uMobile.length()<10) {
            showAlert("Mobile number is not valid");

        } else {
            showProgress("Loading..");
            firebaseAuth.createUserWithEmailAndPassword(uEmail, uPass).addOnCompleteListener(RegisterActivity.this, task -> {
                if (task.isSuccessful()) {

                    newUserRef.child(getString(R.string.USERNAME)).setValue(uName);
                    newUserRef.child(getString(R.string.DOB)).setValue(uDob);
                    newUserRef.child(getString(R.string.MOBILE)).setValue(uMobile);
                    newUserRef.child(getString(R.string.ADDRESS)).setValue(uAdrs);
                    newUserRef.child(getString(R.string.EMAIL)).setValue(uEmail);
                    newUserRef.child(getString(R.string.PASSWORD)).setValue(uPass);

                    //removed as suggested
//                    db.child(String.valueOf(click_count + 1)).setValue("users");

                    newUserRef.push();
                    sendVerificationMail();
                    removeProgress();
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setMessage(getString(R.string.SUCCESSFUL_REGISTERATION_MSG))
                            .setTitle(R.string.login_error_title)
                            .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                overridePendingTransition(0, 0);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            });
                    AlertDialog dialog = builder.create();
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    editor.putString(Constants.USER_EMAIL, uEmail);
                    editor.commit();

                } else {
                    removeProgress();
                    showAlert(task.getException().getMessage());
                }
            });

        }
    }

    private void sendVerificationMail() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Objects.requireNonNull(firebaseUser).sendEmailVerification().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseAuth.getInstance().signOut();
            } else {
                showAlert(task.getException().getMessage());
            }
        });
    }
}
