package com.valli.shoppingapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.valli.shoppingapp.Login.LoginActivity;

import java.util.Objects;

import static com.valli.shoppingapp.Constants.CURRENT_USER_KEY;
import static com.valli.shoppingapp.Constants.USER_EMAIL;

public class BaseFragment extends Fragment {

    public SharedPreferences pref;
    public SharedPreferences.Editor editor;
    public ProgressDialog progressDialog;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private String userID;
    private Context context;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpValues();
    }

    private void setUpValues() {
        context = getContext();
        pref = getContext().getSharedPreferences(Constants.MYPREF, 0); // 0 - for private mode
        editor = pref.edit();
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("users");

    }

    public void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(getActivity().getString(R.string.logout_message))
                .setTitle(R.string.login_error_title)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
//                            SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getContext());
//                            SharedPreferences.Editor editor = mPrefs.edit();
                            editor.clear();
                            editor.commit();
//                                    ((ActivityManager) getActivity().getSystemService(ACTIVITY_SERVICE))
//                                            .clearApplicationUserData();
                            loadLogInView(getActivity());
                        }
                )
                .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss()
                );
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    public void showProgress(String message) {
        progressDialog.setMessage(message);
        progressDialog.show();
    }
//
//    public void showProgress() {
//        progressDialog.show();
//    }

    public void removeProgress() {
        progressDialog.dismiss();
    }

    public void loadLogInView(FragmentActivity activity) {
        Intent it1 = new Intent(activity, LoginActivity.class);
        it1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(it1);


//        editor.putBoolean("loggedIn ", true);
//        editor.commit();
    }

    public void loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
//            if (b != null)
//                fragment.setArguments(b);
            if (getFragmentManager() != null) {
                getFragmentManager()
                        .beginTransaction()
                        .add(R.id.container, fragment)
                        .commit();
            }
        }
    }

//    //Method which returns a current login users key
//    public String getKey() {
//        databaseReference.orderByChild(getContext().getString(R.string.EMAIL)).equalTo(pref.getString(USER_EMAIL, "")).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                    Log.e("Check base value", ds.getValue().toString());
//                    Log.e("Check key", ds.getKey());
//                    userID = ds.getKey();
//                    editor.putString(CURRENT_USER_KEY,userID);
//                    editor.commit();
////                    databaseReference.child("Product").setValue(selectedList);
////                    databaseReference.push();
////                    firebaseDatabase.getReference().child("users").child(userID).child("Product").setValue(selectedList);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//        Log.e("Check return value ",userID.toString());
//        return userID;
//    }

    public void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(message)
                .setTitle(R.string.login_error_title)
                .setPositiveButton(android.R.string.ok, ((dialog, which) -> dialog.dismiss()));
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
}
