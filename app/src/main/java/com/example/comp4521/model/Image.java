package com.example.comp4521.model;

import java.io.Serializable;

public class Image implements Serializable {
    private String[] imageUrls;

    public String[] getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String[] new_imageurls) {
        for(int i=0; i<3; i++){
            imageUrls[i] = new_imageurls[i];
        }
    }
}
