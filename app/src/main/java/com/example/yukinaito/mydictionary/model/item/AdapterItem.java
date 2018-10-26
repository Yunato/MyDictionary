package com.example.yukinaito.mydictionary.model.item;

//WordNameAdapter表示用
public class AdapterItem {
    private final String id;
    private final String name;
    private final String kana;
    private boolean visible;

    public AdapterItem(String id, String name, String kana){
        this.id = id;
        this.name = name;
        this.kana = kana;
    }

    public AdapterItem(String id, String name, String kana, boolean visible){
        this.id = id;
        this.name = name;
        this.kana = kana;
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

    //読み方の取得
    public String getKana(){
        return this.kana;
    }

    //表示するか
    public boolean getVisible(){
        return this.visible;
    }
}
