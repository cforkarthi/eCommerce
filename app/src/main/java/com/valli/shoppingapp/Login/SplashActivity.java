package com.valli.shoppingapp.Login;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.valli.shoppingapp.R;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.VERSION_CODES.M;
import static android.text.TextUtils.concat;

public class SplashActivity extends CommonActivity implements View.OnClickListener {

    private Button btn_register, btn_login;
    private FirebaseAuth mFirebaseAuth;
    String[] appPermissions;
    int PERMISSIONS_REQUEST_CODE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkConnectivity();
        if (Build.VERSION.SDK_INT >= M) {
            checkandRequestPermissions();
//            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},200);
        }
        setContentView(R.layout.splash);
        this.context = getApplicationContext();
        setUpViews();
//        boolean loggedIn = pref.getBoolean("loggedIn", false);
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            btn_register.setVisibility(View.VISIBLE);
            btn_login.setVisibility(View.VISIBLE);
        } else {
            Log.e("Check ", mFirebaseAuth.getCurrentUser().toString());
            btn_register.setVisibility(View.GONE);
            btn_login.setVisibility(View.VISIBLE);
        }
    }

    private boolean checkConnectivity() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm.getActiveNetworkInfo() == null)
            return false;
        else
            Log.e("check network",cm.getActiveNetworkInfo().toString());
        return cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public void setUpViews() {
        btn_login = findViewById(R.id.login_btn);
        btn_register = findViewById(R.id.register_btn);
        mFirebaseAuth = FirebaseAuth.getInstance();
        btn_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_btn:
                //have to check already registered or not
                //if not show register screen
                redirect(RegisterActivity.class);
//                editor.putBoolean("loggedIn ", true);
//                editor.commit();
                break;
            case R.id.login_btn:
                //show login btn on every launch
                redirect(LoginActivity.class);
                break;

        }
    }

    public void showDialog1(String s, String s1, String s2, DialogInterface.OnClickListener onClickListener,
                            String s3, DialogInterface.OnClickListener onClickListener1) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(s);
        builder.setCancelable(true);
        builder.setMessage(s1);
        builder.setPositiveButton(s2, onClickListener);
        builder.setNegativeButton(s3, onClickListener1);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        TextView textView = (TextView) alertDialog.findViewById(android.R.id.message);
        Spannable word = new SpannableString(concat(getString(R.string.RPS_PERMISSION), getString(R.string.RPS_PERMISSION1)));
        word.setSpan(new RelativeSizeSpan(0.8f), 86, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(word);
        return;
    }

    public void showDialog2(String s, String s1, String s2, DialogInterface.OnClickListener onClickListener,
                            String s3, DialogInterface.OnClickListener onClickListener1) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(s);
        builder.setCancelable(true);
        builder.setMessage(s1);
        builder.setPositiveButton(s2, onClickListener);
        builder.setNegativeButton(s3, onClickListener1);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void checkandRequestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // We do not have this permission. Let's ask the user
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSIONS_REQUEST_CODE);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE) {
//            HashMap<String, Integer> permissionResults = new HashMap<>();
//            int deniedCount = 0;
//            boolean isEnable = true;
            for (int i = 0; i < grantResults.length; i++) {
//                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
//                    permissionResults.put(permissions[i], grantResults[i]);
//                    deniedCount++;
//                }
                if (permissions[i].equalsIgnoreCase("android.permission.READ_PHONE_STATE") && grantResults[i] == -1) {
//                    isEnable = false;
                    if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, "android.permission.READ_PHONE_STATE")) {
                        showDialog1("", getString(R.string.RPS_PERMISSION) + "\n" + getString(R.string.RPS_PERMISSION1),
                                "OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSIONS_REQUEST_CODE);
                                    }
                                },
                                "Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        finishAffinity();
//                                        System.exit(0);
                                    }
                                });
                    } else {
//                        showDialog2("", "You have denied Telephone Permission at [Settings] -> [Permissions]",
//                                "Go to Settings", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
//                                                Uri.fromParts("package", getPackageName(), null));
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(intent);
//                                        finish();
//                                    }
//                                },
//                                "No,Exit App", new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                        finishAffinity();
////                                        System.exit(0);
//                                    }
//                                });
                    }
                }
                else if (permissions[i].equalsIgnoreCase("android.permission.READ_PHONE_STATE") && grantResults[i] == 0){
                   finish();
                }
            }
        }
    }

    private void redirect(Class dest) {
        Intent it = new Intent(SplashActivity.this, dest);
        it.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(it);
    }

}
