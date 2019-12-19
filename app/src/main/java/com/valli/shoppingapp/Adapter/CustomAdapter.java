package com.valli.shoppingapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.valli.shoppingapp.Model.Product;
import com.valli.shoppingapp.R;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<ProductViewHolder>/* implements Filterable*/ {

    private Context ctx;
    private List<Product> productList, pd2 = new ArrayList<>();

    public CustomAdapter(Context ctx, List<Product> list) {
        this.ctx = ctx;
        this.productList = list;
    }

    public void setProductList(List<Product> products) {
        this.productList = products;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.row_item_home, parent, false);
        return new ProductViewHolder(view);

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, int position) {

        final Product currentItem = productList.get(position);
        holder.textView1.setText(currentItem.getProduct());
        holder.textView2.setText(currentItem.getDescrption());
        holder.textView.setText(currentItem.getProdId());
        Picasso.get().load(productList.get(position).getUrl()).priority(Picasso.Priority.HIGH).resize(300, 100).centerCrop().into(holder.imageView);

        //For selection
      /*  holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pd2.contains(currentItem)) {
                    pd2.remove(currentItem);
                    currentItem.setSelected(false);
                    holder.checkBox.setChecked(false);
//                unhighlightView(holder);
                } else {
                    pd2.add(currentItem);
//                highlightView(holder);
                    currentItem.setSelected(true);
                    holder.checkBox.setChecked(true);
                }
            }
        });*/

        holder.checkBox.setOnClickListener(view -> {
            if (pd2.contains(currentItem)) {
                pd2.remove(currentItem);
                currentItem.setSelected(false);
                holder.checkBox.setChecked(false);
//                holder.checkBox.setImageDrawable(ctx.getResources().getDrawable(R.drawable.add_cart));
//                unhighlightView(holder);
            } else {
                pd2.add(currentItem);
//                highlightView(holder);
                currentItem.setSelected(true);
                holder.checkBox.setChecked(true);
//                holder.checkBox.setImageDrawable(ctx.getResources().getDrawable(R.drawable.ic_dlt));
//                holder.checkBox.animate();
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public List<Product> getSelectedList() {
        return pd2;
    }

}