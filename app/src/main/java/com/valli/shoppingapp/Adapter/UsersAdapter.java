package com.valli.shoppingapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.valli.shoppingapp.Model.Product;
import com.valli.shoppingapp.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends ArrayAdapter<Product> {

    public ArrayList<Product> productList;
    public Context context;

    public UsersAdapter(Context context, ArrayList<Product> productList) {
        super(context, R.layout.item_prod, productList);
        this.context = context;
        this.productList = productList;
    }

    @Override
    public int getCount() {
        return productList.size();
    }

//        @Override
//        public Object getItem(int position) {
//            return productList.get(position);
//        }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class ViewHolder {
        TextView mSNo;
        TextView mProduct;
        TextView mCategory;
        CircleImageView imageView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_prod, parent, false);
            holder = new ViewHolder();
            holder.mSNo = convertView.findViewById(R.id.sNo);
            holder.mProduct = convertView.findViewById(R.id.product);
            holder.mCategory = convertView.findViewById(R.id.category);
            holder.imageView = convertView.findViewById(R.id.list_avatar);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Product item = productList.get(position);
        holder.mSNo.setText(item.getProdId());
        holder.mProduct.setText(item.getProduct());
        holder.mCategory.setText(item.getDescrption());

        Picasso.get().load(item.getUrl()).priority(Picasso.Priority.HIGH).resize(60, 60).centerCrop().into(holder.imageView);

        return convertView;
    }

}
