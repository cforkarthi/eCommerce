package com.valli.shoppingapp.OrderDetail;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.valli.shoppingapp.Dashboard;
import com.valli.shoppingapp.BaseFragment;
import com.valli.shoppingapp.Model.Product;
import com.valli.shoppingapp.R;
import com.valli.shoppingapp.Adapter.UsersAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.valli.shoppingapp.Constants.list_Size;

public class OrderDetailFragment extends BaseFragment {
    /**
     * retrieve the stored document from cloudstore and show it to the user
     * with header no options to do here
     */

    Context ctx;
    private View view;
    private TextView title, nodata;
    private View listView;
    private ImageView logout;
    private FirebaseFirestore storeDb;
    private String mUserId;
    private FirebaseAuth mfirebaseAuth;
    private FirebaseUser mfirebaseuser;
    //    private List<Product> list_from_db = new ArrayList<>(), list_to_disp = new ArrayList<>();
    private Product p;
    private int size_of_list = 0;
    private List<Product> productList = new ArrayList();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.order_detail_layout, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViews();
        setUpValues();
        loadDoc();
    }

    private void loadDoc() {

        showProgress("Loading");

        FirebaseFirestore.getInstance().collection("Order_Details").document(mUserId).get()
                .addOnCompleteListener(task -> {
                    Log.e("check ord", String.valueOf(task.getResult()));

                    if (task.isSuccessful()) {
                        DocumentSnapshot doc = task.getResult();
                        if (doc != null && doc.exists()) {
                            if (doc.getData().toString().equalsIgnoreCase("{}")) {
                                removeProgress();
//                              showAlert("No History!");
                                nodata.setVisibility(View.VISIBLE);
                                listView.setVisibility(View.GONE);
                            } else {
                                nodata.setVisibility(View.GONE);
                                listView.setVisibility(View.VISIBLE);
                                int count = 0;
//                                String document = doc.getData().toString();

                                count = (int) (Pattern.compile("_pId_").splitAsStream(task.getResult().toString()).count() - 1);

                                Log.e("check ord", String.valueOf(count));

                                for (int i = 0; i < count; i++) {
                                    p = doc.toObject(Product.class);
                                    p.setProdId((String) doc.get(mUserId + "_pId_" + i));
                                    p.setProduct((String) doc.get(mUserId + "_pName_" + i));
                                    p.setUrl((String) doc.get(mUserId + "_pUrl_" + i));
                                    p.setDescrption((String) doc.get(mUserId + "_pDesc_" + i));
                                    p.setSelected(Boolean.valueOf((String) doc.get(mUserId + "_pSel_" + i)));
                                    productList.add(p);
                                }

                                Log.e("Check ", "onSuccess: " + doc.getData().toString());
                                ArrayList<Product> arrayOfUsers = new ArrayList<>();
                                // Create the adapter to convert the array to views
                                UsersAdapter adapter = new UsersAdapter(ctx, arrayOfUsers);
                                adapter.addAll(productList);
                                // Attach the adapter to a ListView
                                ListView listView = view.findViewById(R.id.listview);
                                removeProgress();
                                listView.setAdapter(adapter);
                            }
                        } else {
                            removeProgress();
                            nodata.setVisibility(View.VISIBLE);
                            listView.setVisibility(View.GONE);
                        }
                    } else {
                        removeProgress();
                        Log.e("Check ", task.getException().getMessage());
                        showAlert(task.getException().getMessage());
                    }
                })
                .addOnFailureListener(e -> {
                    removeProgress();
                    nodata.setVisibility(View.VISIBLE);
                    listView.setVisibility(View.GONE);
                    showAlert(e.getMessage());
                });

    }

    private void setUpValues() {
        ctx = getContext();
        storeDb = FirebaseFirestore.getInstance();
        mfirebaseAuth = FirebaseAuth.getInstance();
        mfirebaseuser = mfirebaseAuth.getCurrentUser();
        mUserId = mfirebaseuser.getUid();
        size_of_list = pref.getInt(list_Size, 10);
    }

    private void setUpViews() {
        Toolbar toolbar = getActivity().findViewById(R.id.tbToolbar);
        title = toolbar.findViewById(R.id.title);
        title.setText(getActivity().getString(R.string.menu_order_title));
        listView = view.findViewById(R.id.listview);
        nodata = view.findViewById(R.id.nodata);
        logout = toolbar.findViewById(R.id.logout);
        logout.setOnClickListener(v -> logout());
    }

    @Override
    public void onResume() {
        super.onResume();
        title.setText(getActivity().getString(R.string.menu_order_title));
        ((Dashboard) getActivity()).navView.setSelectedItemId(R.id.orderDetail);

    }
}
