package com.example.yukinaito.mydictionary.model.entity;

import com.example.yukinaito.mydictionary.model.item.WordNameAdapterItem;

import java.util.Comparator;

public class WordComparator implements Comparator<WordNameAdapterItem>{
    @Override
    public int compare(WordNameAdapterItem word1, WordNameAdapterItem word2){
        if(word1.getName().compareToIgnoreCase(word2.getName()) < 0)
            return -1;
        else if(word1.getName().compareToIgnoreCase(word2.getName()) > 0)
            return 1;
        else
            return 0;
    }
}
