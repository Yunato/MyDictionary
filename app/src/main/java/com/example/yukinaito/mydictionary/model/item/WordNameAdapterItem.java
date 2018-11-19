package com.example.yukinaito.mydictionary.model.item;

public class WordNameAdapterItem {
    /** 単語名 (ラベル名含む) 格納リスト  */
    private final String id;
    private final String name;
    private final boolean visible;

    public WordNameAdapterItem(String id, String name, boolean visible){
        this.id = id;
        this.name = name;
        this.visible = visible;
    }

    //idの取得
    public String getId(){
        return this.id;
    }

    //名前の取得
    public String getName(){
        return this.name;
    }

    //表示するか
    public boolean getVisible(){
        return this.visible;
    }
}
