package com.whiterabbit.entities;

public enum Category {
    GENERAL(new String[]{"technology", "international"}),DEVELOPPEMENT(new String[]{"java"});

    private String[] sousCategory;

    private Category(String[] sousCategory){
        this.sousCategory = sousCategory;
    }

    public String[] getSubcategory() {
        return sousCategory;
    }

}
