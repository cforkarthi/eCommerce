package com.valli.shoppingapp.Home;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.valli.shoppingapp.APIClient.ProductApiClient;
import com.valli.shoppingapp.APIInterface.ApiInterface;
import com.valli.shoppingapp.Adapter.CustomAdapter;
import com.valli.shoppingapp.Constants;
import com.valli.shoppingapp.DAO.ProductDao;
import com.valli.shoppingapp.Dashboard;
import com.valli.shoppingapp.BaseFragment;
import com.valli.shoppingapp.Cart.CartFragment;
import com.valli.shoppingapp.Database.Database;
import com.valli.shoppingapp.Model.Product;
import com.valli.shoppingapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends BaseFragment implements View.OnClickListener {

    /**
     * after successful login, load this page with response of given api
     * by default this should be selected
     * search bar, selection of list items,
     * add to cart
     */
    private List<Product> productList;
    private RecyclerView recyclerView;
    private CustomAdapter adapter;

    private EditText etSearchList;
    private TextView tvNoData;
    private Call<List<Product>> call;

    private View view;
    private TextView title;
    private List<Product> selectedList;
    private List<Product> union;
    private List<Product> list_from_db = new ArrayList<>();
    private DatabaseReference databaseReference;
    private String mUserId;
    private Map<String, Product> mapData = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_layout, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViews();
        setUpValues();
    }

    private void setUpValues() {
        adapter = new CustomAdapter(getActivity(), productList);
//        CustomAdapter adapter1 = new CustomAdapter(getActivity(), productList);
        recyclerView.setAdapter(adapter);
        ApiInterface model = ProductApiClient.getClient().create(ApiInterface.class);
        call = model.getProductDetails();
        showProgress(getActivity().getString(R.string.load_data));
//        showProgress("Loading data");
        databaseReference = firebaseDatabase.getReference();
        FirebaseAuth mfirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mfirebaseuser = mfirebaseAuth.getCurrentUser();
        mUserId = mfirebaseuser.getUid();
        loadValues();
    }

    private void setUpViews() {
        productList = new ArrayList<>();
//        pref = getActivity().getSharedPreferences("MyPref", 0); // 0 - for private mode
//        editor = pref.edit();
        recyclerView = view.findViewById(R.id.recyler_view);
        Button addCart = view.findViewById(R.id.add_cart);
        addCart.setOnClickListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Toolbar toolbar = getActivity().findViewById(R.id.tbToolbar);
        title = toolbar.findViewById(R.id.title);
        title.setText(getActivity().getString(R.string.menu_home_title));
        tvNoData = view.findViewById(R.id.no_data);
//        search = view.findViewById(R.id.searchView);
        etSearchList = view.findViewById(R.id.et_search_list);
//        search.setOnClickListener(this);
        ImageView logout = toolbar.findViewById(R.id.logout);
        logout.setOnClickListener(this);

        etSearchList.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence userInput, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence userInput, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable userInput) {

                if (userInput.length() == 0) {
                    adapter.setProductList(productList);
                    recyclerView.setVisibility(View.VISIBLE);
                    tvNoData.setVisibility(View.GONE);
                } else {
                    showProgress("Searching . .");
                    searchAdapter(userInput.toString());
                }

            }
        });
    }

    private void loadValues() {
        call.enqueue(new Callback<List<Product>>() {

            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful()) {
                    productList = response.body();
                    adapter.setProductList(productList);
                    removeProgress();
                } else
                    Log.e("Check ", response.message());
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.d("TAG", "Response = " + t.toString());
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_cart:
                addToCart();
                break;
            case R.id.logout:
                logout();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        title.setText(getActivity().getString(R.string.menu_home_title));
        etSearchList.getText().clear();
        adapter.setProductList(productList);
        ((Dashboard) getActivity()).navView.setSelectedItemId(R.id.home);

    }


    // Search - min 2 letters
    // if result found display it, else show the list

    private void searchAdapter(String searchQuery) {

        searchQuery = searchQuery.toLowerCase();

        final List<Product> searchList = new ArrayList<>();

        if (searchQuery.length() > 2) {
            for (Product p : productList) {
                if (p.getProduct().toLowerCase().contains(searchQuery))
                    searchList.add(p);
            }
        } else {
            adapter.setProductList(productList);
            recyclerView.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.GONE);
        }
        //        if (searchList.isEmpty()) {
//            recyclerView.setVisibility(View.GONE);
//            tvNoData.setVisibility(View.VISIBLE);
//        } else
        if (searchList != null && searchList.size() > 0) {
            removeProgress();
            adapter.setProductList(searchList);
            recyclerView.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.GONE);// data set changed
        } else {
            removeProgress();
            recyclerView.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
            // no record found message should show
        }
    }


    // Add Cart - Save the selected items in firebase db
    private void addToCart() {
        Log.e("Check list_from_db 0", String.valueOf(list_from_db.size()));
        selectedList = new ArrayList<>();
        selectedList = adapter.getSelectedList();
        Log.e("check >>> ", String.valueOf(selectedList.size()));


        Database db = Database.getInstance(getContext());
        for (int i = 0; i < selectedList.size(); i++) {
            db.productDao().insertProduct(selectedList.get(i));
        }

        if (selectedList != null && adapter.getSelectedList().size() > 0) {
            Log.e("Check sel_list 1", String.valueOf(selectedList.size()));
            databaseReference.child(Constants.USERS).child(mUserId).child(Constants.PRODUCT).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds != null) {
                            Log.e("Check base value", ds.getValue().toString());
                            Log.e("Check key", ds.getKey());
                            Product product1 = ds.getValue(Product.class);
                            mapData.put(ds.getKey(), product1);
                            ArrayList<Product> values = new ArrayList<>(mapData.values());
                            List<String> keys = new ArrayList<>(mapData.keySet());
//                            for (Product p : values) {
//                                Log.e("firebase", p.getUrl());
//                            }
                            list_from_db = values;
                        } else {
                            list_from_db.clear();
                        }
                    }
                    Log.e("Check list_from_db 1", String.valueOf(list_from_db.size()));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("Check ", databaseError.getMessage()); //Don't ignore errors
                }
            });
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Do you want to add these " + selectedList.size() + "  items to cart ?")
                    .setTitle(R.string.login_error_title)
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        //Get list from db, add current selected items to the db_list
                        union = Stream.concat(list_from_db.stream(), selectedList.stream())
                                .distinct()
                                .collect(Collectors.toList());
                        databaseReference.child(Constants.USERS).child(mUserId).child(Constants.PRODUCT).setValue(union);
//                        databaseReference.child(Constants.USERS).child(mUserId).child(Constants.PRODUCT).removeValue();

                        //Clear Selection
                        for (int i = 0; i < selectedList.size(); i++) {
                            selectedList.get(i).setSelected(false);
                        }
                        loadFragment(new CartFragment());
                    })
                    .setNegativeButton(android.R.string.no, (dialog, which) -> {
                        dialog.dismiss();
                        for (int i = 0; i < selectedList.size(); i++) {
                            selectedList.get(i).setSelected(false);
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } else
            showAlert(getActivity().getString(R.string.add_cart_message1));
    }
}
//                        int count = union.size();
//                        for (int i = 0; i < count; i++) {
//                            if (i + 1 < count && union.get(i).getUrl().equals(union.get(i + 1).getUrl())) {
//                                union.remove(i);
//                                i--;
//                                count--;
//                            }
//                        }
//                          Map<String, String> map = new HashMap<>();
//                          FirebaseFirestore storeDb = FirebaseFirestore.getInstance();
//                        FirebaseFirestore.getInstance().collection("Cart_details").document(mUserId).get()
//                                .addOnCompleteListener(task -> {
//                                    if (task.isSuccessful()) {
//                                        DocumentSnapshot doc = task.getResult();
//                                        if (doc != null && doc.exists()) {
//                                            if (doc.getData().toString().equalsIgnoreCase("{}")) {
//                                                removeProgress();
//                                            } else {
//                                                int count = 0;
////                                String document = doc.getData().toString();
//                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                                    count = (int) (Pattern.compile("_pId_").splitAsStream(task.getResult().toString()).count() - 1);
//                                                    Log.e("check ord", String.valueOf(count));
//                                                }
//                                                for (int i = 0; i < count; i++) {
//                                                    p = doc.toObject(Product.class);
//                                                    p.setProdId((String) doc.get(mUserId + "_pId_" + i));
//                                                    p.setProduct((String) doc.get(mUserId + "_pName_" + i));
//                                                    p.setUrl((String) doc.get(mUserId + "_pUrl_" + i));
//                                                    p.setDescrption((String) doc.get(mUserId + "_pDesc_" + i));
//                                                    p.setSelected(Boolean.valueOf((String) doc.get(mUserId + "_pSel_" + i)));
//                                                    list_from_db.add(p);
//                                                }
//                                            }
//                                        }
//                                    } else {
//                                        removeProgress();
//                                        Log.e("Check ", task.getException().getMessage());
//                                        showAlert(task.getException().getMessage());
//
//                                    }
//                                });
//
//                        union = Stream.concat(list_from_db.stream(), selectedList.stream())
//                                    .distinct()
//                                    .collect(Collectors.toList());

//                        for (int i = 0; i < selectedList.size(); i++) {
//                            map.put(mUserId + "_pId_" + i, selectedList.get(i).getProdId());
//                            map.put(mUserId + "_pName_" + i, selectedList.get(i).getProduct());co
//                            map.put(mUserId + "_pUrl_" + i, selectedList.get(i).getUrl());
//                            map.put(mUserId + "_pDesc_" + i, selectedList.get(i).getDescrption());
//                            map.put(mUserId + "_pSel_" + i, selectedList.get(i).getSelected().toString());
//                            storeDb.collection("Cart_details").document(mUserId).set(map,SetOptions.merge());
//                        }







