package com.example.yukinaito.mydictionary;

//WordNameAdapter表示用
public class AdapterItem {
    private String name;
    private String kana;

    public AdapterItem(String name, String kana){
        this.name = name;
        this.kana = kana;
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
