package com.valli.shoppingapp.Model;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity(tableName = "product")
public class Product implements Serializable {
    @NonNull
    @PrimaryKey(autoGenerate = false)
    private String prodId;
    @ColumnInfo(name = "pname")
    private String product;
    @ColumnInfo(name = "pdesc")
    private String descrption;
    @ColumnInfo(name = "purl")
    private String url;
    private Boolean selected;

    public Product(){}

    public Product(String id, String name, String desc) {
        this.prodId = id;
        this.product = name;
        this.descrption = desc;
    }


    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getDescrption() {
        return descrption;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }
}

