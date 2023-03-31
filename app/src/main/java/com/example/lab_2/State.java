package com.example.lab_2;

public class State {

    private String name; // название
    private int image; // ресурс флага

    public State(String name, int image) {

        this.name = name;
        this.image = image;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImage() {
        return this.image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
