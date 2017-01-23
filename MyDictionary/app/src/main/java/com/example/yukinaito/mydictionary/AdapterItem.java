package com.example.yukinaito.mydictionary;

//WordNameAdapter表示用
public class AdapterItem {
    private String id;
    private String name;
    private String kana;

    public AdapterItem(String id, String name, String kana){
        this.id = id;
        this.name = name;
        this.kana = kana;
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
}
