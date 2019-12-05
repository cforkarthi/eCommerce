package com.valli.shoppingapp.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.valli.shoppingapp.R;

public class SplashActivity extends CommonActivity implements View.OnClickListener {

    private Button btn_register, btn_login;
    private FirebaseAuth mFirebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= M) {
//            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},200);
//        }
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

    private void redirect(Class dest){
        Intent it = new Intent(SplashActivity.this, dest);
        it.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(it);
    }

}
