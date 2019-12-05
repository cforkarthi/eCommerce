package com.valli.shoppingapp.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;
import com.valli.shoppingapp.Cart.CartFragment;
import com.valli.shoppingapp.Model.Product;
import com.valli.shoppingapp.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class CartCustomAdapter extends RecyclerView.Adapter<CartCustomAdapter.CartViewHolder> {
    private Context ctx;
    private List<Product> productList, pd1;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;


    public CartCustomAdapter(Context ctx, List<Product> list) {
        this.ctx = ctx;
        this.productList = list;
    }

    public void setProductList(List<Product> products) {
        this.productList = products;
        this.pd1 = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.row_item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        final Product currentItem = productList.get(position);
        holder.prod_name.setText(currentItem.getProduct());
        holder.prod_id.setText(currentItem.getProdId());
        Picasso.get().load(currentItem.getUrl()).priority(Picasso.Priority.HIGH).resize(45, 45).centerCrop().into(holder.prod_icon);

        holder.trash.setOnClickListener(v -> {
            deleteItemFromList(v, position);

        });

    }

    private void deleteItemFromList(View v, int position) {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        FirebaseAuth mfirebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser mfirebaseuser = mfirebaseAuth.getCurrentUser();
        String mUserId = mfirebaseuser.getUid();
        Map<String, String> map = new HashMap<>();
        FirebaseFirestore storeDb =  FirebaseFirestore.getInstance();
        AlertDialog.Builder build = new AlertDialog.Builder(ctx);
        build.setMessage(ctx.getString(R.string.delete_confirmation_msg))
                .setTitle(R.string.login_error_title)
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    productList.remove(position);
                    notifyDataSetChanged();
                    Toast.makeText(ctx, ctx.getString(R.string.item_deleted), Toast.LENGTH_SHORT).show();
//                    storeDb.collection("Cart_details").document(mUserId).delete();
//                    for (int i = 0; i < productList.size(); i++) {
//                        map.put(mUserId + "_pId_" + i, productList.get(i).getProdId());
//                        map.put(mUserId + "_pName_" + i, productList.get(i).getProduct());
//                        map.put(mUserId + "_pUrl_" + i, productList.get(i).getUrl());
//                        map.put(mUserId + "_pDesc_" + i, productList.get(i).getDescrption());
//                        map.put(mUserId + "_pSel_" + i, productList.get(i).getSelected().toString());
//                        storeDb.collection("Cart_details").document(mUserId).set(map);
//                    }
                    databaseReference.child("users").child(mUserId).child("Product").setValue(productList);
                    ProgressDialog pd = new ProgressDialog(ctx);
                    pd.show();
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    Fragment myFragment = new CartFragment();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.container, myFragment)
                            .setTransition(FragmentTransaction.TRANSIT_NONE).addToBackStack(null).commit();
                    pd.dismiss();
                })
                .setNegativeButton(android.R.string.no, (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = build.create();
        dialog.show();

    }

//    public List<Product> getUpdatedList() {
//        return pd1;
//    }


    @Override
    public int getItemCount() {
        return productList.size();
    }

    class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView trash;
        CircleImageView prod_icon;
        TextView prod_id, prod_name;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            prod_icon = itemView.findViewById(R.id.image_cart);
            trash = itemView.findViewById(R.id.qty_trash);
            prod_id = itemView.findViewById(R.id.tv_prod_id);
            prod_name = itemView.findViewById(R.id.tv_prod_name);
        }
    }
}
