package com.valli.shoppingapp;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.valli.shoppingapp.Cart.CartFragment;
import com.valli.shoppingapp.Contacts.ContactsFragment;
import com.valli.shoppingapp.Home.HomeFragment;
import com.valli.shoppingapp.OrderDetail.OrderDetailFragment;
import com.valli.shoppingapp.Profile.ProfileFragment;

public class Dashboard extends AppCompatActivity {
    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment2 = new CartFragment();
    final Fragment fragment3 = new ProfileFragment();
    final Fragment fragment4 = new OrderDetailFragment();
    final Fragment fragment5 = new ContactsFragment();
    public BottomNavigationView navView;

    public SharedPreferences pref;
    public SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baselayout);
        setUpViews();
    }

    private void setUpViews() {
        navView = findViewById(R.id.navigation);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        pref = getApplicationContext().getSharedPreferences(Constants.MYPREF, 0); // 0 - for private mode
        editor = pref.edit();
        navView.setSelectedItemId(R.id.home);
    }

    public BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.home:
                fragment = fragment1;
                break;
            case R.id.cart:
                fragment = fragment2;
                break;

            case R.id.profile:
                fragment = fragment3;
                break;

            case R.id.orderDetail:
                fragment = fragment4;
                break;

            case R.id.contacts:
                fragment = fragment5;
                break;
        }
        return loadFragment(fragment);
    };

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .replace(R.id.container, fragment)
                    .commit();
            return true;
        }
        return false;
    }


    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Dashboard.this);
        builder.setMessage(getString(R.string.exit_alert_message))
                .setTitle(R.string.login_error_title)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    finishAffinity();
                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
