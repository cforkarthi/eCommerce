package com.valli.shoppingapp.Adapter;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.valli.shoppingapp.R;

public class ProductViewHolder extends RecyclerView.ViewHolder {

    ImageView imageView;
    TextView textView1, textView2, textView;
    CheckBox checkBox;

//    ImageView checkBox;
    public ProductViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.image);
        textView1 = itemView.findViewById(R.id.prodName);
        textView2 = itemView.findViewById(R.id.prodDesc);
        textView = itemView.findViewById(R.id.prodId);
        checkBox = itemView.findViewById(R.id.checkBox);

    }

}
