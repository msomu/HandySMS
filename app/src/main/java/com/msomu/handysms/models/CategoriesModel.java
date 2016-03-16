package com.msomu.handysms.models;

/**
 * Created by msomu on 16/03/16.
 */
public class CategoriesModel {
    private int id;
    private String categoryName;

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
