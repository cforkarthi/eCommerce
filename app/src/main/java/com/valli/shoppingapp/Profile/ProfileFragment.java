package com.valli.shoppingapp.Profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.valli.shoppingapp.Constants;
import com.valli.shoppingapp.Dashboard;
import com.valli.shoppingapp.BaseFragment;
import com.valli.shoppingapp.R;

import static com.valli.shoppingapp.Constants.USER_EMAIL;

//import com.github.ivbaranov.mli.MaterialLetterIcon;

public class ProfileFragment extends BaseFragment {

    /**
     * Retrieve the entered information of login user and display it
     * via fireBase Database
     */

    private TextView name, dob, email, mobile, adrs;
    private View view;
    private TextView title;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.profile_layout, container, false);

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViews();
        setUpValues();
    }

    private void setUpViews() {
        name = view.findViewById(R.id.disp_name);
        dob = view.findViewById(R.id.disp_dob);
        email = view.findViewById(R.id.disp_email);
        mobile = view.findViewById(R.id.disp_mob);
        adrs = view.findViewById(R.id.disp_address);
        Toolbar toolbar = getActivity().findViewById(R.id.tbToolbar);
        ImageView logout = toolbar.findViewById(R.id.logout);
        logout.setOnClickListener(v -> logout());
//        profile_img = view.findViewById(R.id.prof_icon);
    }

    private void setUpValues() {
        Toolbar toolbar = getActivity().findViewById(R.id.tbToolbar);
        title = toolbar.findViewById(R.id.title);
        title.setText(getActivity().getString(R.string.MENU_PROFILE_TITLE));
        showProgress(getActivity().getString(R.string.FETCH_DATA));
        DatabaseReference databaseReference = firebaseDatabase.getReference().child(Constants.USERS);
        Log.e("Check profile email ", pref.getString(USER_EMAIL, ""));
        databaseReference.orderByChild(getActivity().getString(R.string.EMAIL)).equalTo(pref.getString(USER_EMAIL, ""))
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Log.e("Check base value", ds.getValue().toString());
                            Log.e("Check key", ds.getKey());
                            name.setText(ds.child(getActivity().getString(R.string.USERNAME)).getValue().toString());
                            email.setText(ds.child(getActivity().getString(R.string.EMAIL)).getValue().toString());
                            mobile.setText(ds.child(getActivity().getString(R.string.MOBILE)).getValue().toString());
                            adrs.setText(ds.child(getActivity().getString(R.string.ADDRESS)).getValue().toString());
                            dob.setText(ds.child(getActivity().getString(R.string.DOB)).getValue().toString());
                            removeProgress();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        removeProgress();
                        Log.e("Check ", databaseError.getMessage()); //Don't ignore errors!
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        title.setText(getActivity().getString(R.string.MENU_PROFILE_TITLE));
        ((Dashboard) getActivity()).navView.setSelectedItemId(R.id.profile);

    }

}
