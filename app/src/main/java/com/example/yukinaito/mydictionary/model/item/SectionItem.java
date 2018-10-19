package com.example.yukinaito.mydictionary.model.item;

public class SectionItem {
    private String label;
    private int position;

    public SectionItem(String label, int position){
        this.label = label;
        this.position = position;
    }

    public int getPositon(){
        return this.position;
    }
}
