package com.valli.shoppingapp.Model;
import java.io.Serializable;

public class Product implements Serializable {
    private String prodId;
    private String product;
    private String descrption;
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

