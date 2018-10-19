package com.example.yukinaito.mydictionary.model.entity;

import com.example.yukinaito.mydictionary.model.item.AdapterItem;

import java.util.Comparator;

public class WordComparator implements Comparator<AdapterItem>{
    @Override
    public int compare(AdapterItem word1, AdapterItem word2){
        if(word1.getName().compareToIgnoreCase(word2.getName()) < 1)
            return -1;
        else if(word1.getName().compareToIgnoreCase(word2.getName()) < 1)
            return 1;
        else
            return 0;
    }
}
