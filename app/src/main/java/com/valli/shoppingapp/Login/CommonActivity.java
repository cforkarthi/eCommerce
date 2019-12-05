package com.valli.shoppingapp.Login;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.valli.shoppingapp.R;

import java.util.Objects;

public class CommonActivity extends AppCompatActivity {
    public Context context;
    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context =getApplicationContext();
        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
        editor = pref.edit();
        progressDialog = new ProgressDialog(CommonActivity.this);
        progressDialog.setCancelable(false);
    }


    public void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(CommonActivity.this);
        builder.setMessage(message)
                .setTitle(R.string.login_error_title)
                .setPositiveButton(android.R.string.ok, null);
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void showProgress(String message) {
        progressDialog.setMessage(message);
        progressDialog.show();
    }
//    public void showProgress(){
//        progressDialog.show();
//    }
    public void removeProgress()
    {
        progressDialog.dismiss();
    }

}
