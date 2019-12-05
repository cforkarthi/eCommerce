package com.valli.shoppingapp.Cart;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.valli.shoppingapp.Adapter.CartCustomAdapter;
import com.valli.shoppingapp.Dashboard;
import com.valli.shoppingapp.BaseFragment;
import com.valli.shoppingapp.Model.Product;
import com.valli.shoppingapp.OrderDetail.OrderDetailFragment;
import com.valli.shoppingapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.valli.shoppingapp.Constants.list_Size;

public class CartFragment extends BaseFragment {

    /**
     * should display the choosen items
     * the list view should contain image icon, pname and option to delete
     * delete - onclick - alert ( u want to delete this item?)
     * remove that particular data from list
     * save it as a document using cloud fireStore
     */
    private RecyclerView recyclerView;
    private Button btn_placeorder, btn_clear_cart;
    private View view;

    private ImageView logout;
    private TextView title, nodata, total;
    private CartCustomAdapter adapter;
    private LinearLayout layout_total;
    private List<Product> selectedList = new ArrayList<>(), list_from_db = new ArrayList<>(), values;
    private DatabaseReference databaseReference;
    private FirebaseDatabase firebaseDatabase;
    private String userID;
    private FirebaseAuth mfirebaseAuth;
    private FirebaseUser mfirebaseuser;
    private String mUserId;
    private FirebaseFirestore storeDb;
    private Context ctx;
    private Product p;
    private Map<String, Product> pMap = new HashMap<>();
    private Map<String, String> map = new HashMap<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cart_layout, container, false);
        Log.e("Cart", "Oncreate");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        fetchFromDB();
        setUpViews();
        setUpValues();
    }

    private void fetchFromDB() {
        showProgress("Loading..");
        selectedList.clear();
        list_from_db.clear();
        Log.e("Cart", "fetchFromDB");
        firebaseDatabase = FirebaseDatabase.getInstance();
        mfirebaseAuth = FirebaseAuth.getInstance();
        mfirebaseuser = mfirebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mUserId = mfirebaseuser.getUid();
        storeDb = FirebaseFirestore.getInstance();
        values = new ArrayList<>();
        Map<String, Product> td = new HashMap<>();
        databaseReference.child("users").child(mUserId).child("Product").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.e("Check base value", ds.getValue().toString());
                    Log.e("Check key", ds.getKey());
                    Product product1 = ds.getValue(Product.class);
                    td.put(ds.getKey(), product1);
                }
                values = new ArrayList<>(td.values());
                List<String> keys = new ArrayList<>(td.keySet());
//                for (Product p : values)
//                    Log.e("check firebase", p.getUrl());
                list_from_db = values;
                loadDataFromDb(list_from_db);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Check ", databaseError.getMessage()); //Don't ignore errors
            }
        });

//        selectedList.clear();
//        list_from_db.clear();
//        FirebaseFirestore.getInstance().collection("Cart_details").document(mUserId).get()
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        DocumentSnapshot doc = task.getResult();
//                        if (null == doc.getData()) {
//                            removeProgress();
////                              showAlert("No History!");
//                            nodata.setVisibility(View.VISIBLE);
//                            recyclerView.setVisibility(GONE);
//                        }
//                        else {
//                            if (doc != null && doc.exists()) {
//                                removeProgress();
//                                int count = 0;
////                                String document = doc.getData().toString();
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                    count = (int) (Pattern.compile("_pId_").splitAsStream(task.getResult().toString()).count() - 1);
//                                    Log.e("check ord", String.valueOf(count));
//                                }
//                                for (int i = 0; i < count; i++) {
//                                    p = doc.toObject(Product.class);
//                                    p.setProdId((String) doc.get(mUserId + "_pId_" + i));
//                                    p.setProduct((String) doc.get(mUserId + "_pName_" + i));
//                                    p.setUrl((String) doc.get(mUserId + "_pUrl_" + i));
//                                    p.setDescrption((String) doc.get(mUserId + "_pDesc_" + i));
//                                    p.setSelected(Boolean.valueOf((String) doc.get(mUserId + "_pSel_" + i)));
//                                    list_from_db.add(p);
//                                    loadDataFromDb(list_from_db);
//                                }
//                            }
//                        }
//                    } else {
//                        removeProgress();
//                        Log.e("Check ", task.getException().getMessage());
//                        showAlert(task.getException().getMessage());
//                    }
//                });
    }

    private void setUpViews() {
        Log.e("Cart", "setUpViews");
        Toolbar toolbar = getActivity().findViewById(R.id.tbToolbar);
        title = toolbar.findViewById(R.id.title);
        title.setText(getActivity().getString(R.string.menu_cart_title));
        logout = toolbar.findViewById(R.id.logout);
        logout.setOnClickListener(v -> logout());
        recyclerView = view.findViewById(R.id.recycler_cart);
        total = view.findViewById(R.id.tv_total);
        nodata = view.findViewById(R.id.tv_nodata);
        btn_placeorder = view.findViewById(R.id.btn_placeorder);
        btn_clear_cart = view.findViewById(R.id.btn_clearCart);
        layout_total = view.findViewById(R.id.layout_total);
    }


    private void setUpValues() {
        Log.e("Cart", "setUpValues");
        adapter = new CartCustomAdapter(getActivity(), selectedList);
        showProgress("Loading..");
        ctx = getContext();
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        loadDataFromDb();
//        String id = pref.getString(CURRENT_USER_KEY, "");
        if (selectedList.size() > 0) {
            btn_placeorder.setVisibility(VISIBLE);
//            btn_clear_cart.setVisibility(VISIBLE);
        } else {
            btn_placeorder.setVisibility(GONE);
//            btn_clear_cart.setVisibility(GONE);
        }
        btn_placeorder.setOnClickListener(v -> {
            placeOrder();
        });
//        btn_clear_cart.setOnClickListener(v -> {
//            clearCart();
//        });
    }
//
//    private void clearCart() {
//        showAlert("Do you want to remove All items from Cart");
//        showProgress();
//        selectedList.clear();
//        adapter.notifyDataSetChanged();
//        databaseReference.child("users").child(mUserId).child("Product").removeValue();
//        loadValues();
//    }

    private void loadDataFromDb(List<Product> list_from_db) {

        Log.e("Cart", "loadDataFromDb");
        if (this.list_from_db != null && this.list_from_db.size() > 0) {
            removeProgress();
            Log.e("check if size of list ", String.valueOf(this.list_from_db.size()));
            selectedList = list_from_db;
            recyclerView.setVisibility(VISIBLE);
            nodata.setVisibility(GONE);
            layout_total.setVisibility(GONE);
            btn_placeorder.setVisibility(VISIBLE);
//            btn_clear_cart.setVisibility(VISIBLE);
        } else {
            removeProgress();
            Log.e("check else size of lis", String.valueOf(this.list_from_db.size()));
            layout_total.setVisibility(GONE);
            recyclerView.setVisibility(GONE);
            nodata.setVisibility(VISIBLE);
            btn_placeorder.setVisibility(GONE);
//            btn_clear_cart.setVisibility(GONE);
        }
        // title.setText(getActivity().getString(R.string.menu_cart_title));
        loadValues();
    }

    private void loadValues() {
        Log.e("Cart", "loadValues");
        if (selectedList != null && selectedList.size() > 0) {
            Log.e("check loadValue ", String.valueOf(selectedList.size()));
            removeProgress();
//            total.setText(String.valueOf(selectedList.size()));
//            total.setText(String.valueOf(selectedList.size()));
            adapter.setProductList(selectedList);
            recyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    private void placeOrder() {
        showProgress("Loading..");
        Log.e("Check selectedList ", String.valueOf(selectedList.size()));
        editor.putInt(list_Size, selectedList.size());
        editor.commit();

        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(getActivity().getString(R.string.order_confirmation_title))
                .setMessage(getActivity().getString(R.string.order_confirmation_MSG))
                .setPositiveButton(getActivity().getString(R.string.confirm), (dialog, which) -> {

                    // save data to db, move on

                    for (int i = 0; i < selectedList.size(); i++) {
                        map.put(mUserId + "_pId_" + i, selectedList.get(i).getProdId());
                        map.put(mUserId + "_pName_" + i, selectedList.get(i).getProduct());
                        map.put(mUserId + "_pUrl_" + i, selectedList.get(i).getUrl());
                        map.put(mUserId + "_pDesc_" + i, selectedList.get(i).getDescrption());
                        map.put(mUserId + "_pSel_" + i, selectedList.get(i).getSelected().toString());

                        storeDb.collection("Order_Details").document(mUserId).set(map, SetOptions.merge());
                        Log.e("check cart ", storeDb.collection("Order_Details").document(mUserId).get().toString());
                    }

                    databaseReference.child("users").child(mUserId).child("Product").setValue(null);
                    databaseReference.child("users").child(mUserId).child("Product").removeValue((databaseError, databaseReference) -> Log.e("check remove ",databaseReference.toString()));
                    selectedList.clear();
                    adapter.setProductList(selectedList);
                    adapter.notifyDataSetChanged();
//                    FirebaseFirestore.getInstance().collection("Cart_details").document(mUserId).delete();
                    removeProgress();
                    Toast.makeText(ctx, getActivity().getString(R.string.order_confirmed), Toast.LENGTH_SHORT).show();
                    // ? navigate to order detail or enough to show toast
                    loadFragment(new OrderDetailFragment());
                })
                .setNegativeButton(getActivity().getString(R.string.cancel), (dialog, which) -> {
                    // do nothing
                    removeProgress();
                    dialog.dismiss();
                })
                .setIcon(android.R.drawable.ic_dialog_info)
                .show();
    }


    @Override
    public void onPause() {
        Log.e("Cart", "onPause");
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("check onresume ", "test");
        title.setText(getActivity().getString(R.string.menu_cart_title));
        ((Dashboard) getActivity()).navView.setSelectedItemId(R.id.cart);
    }

}