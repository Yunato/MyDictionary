package com.example.yukinaito.mydictionary;

import android.content.ContentValues;
import android.content.Context;

import java.io.Serializable;

//単語情報をすべて保持するクラス
public class Word implements Serializable{
    //単語の名前、読み方、分類、意味、アクセス数、どのくらいアクセスされていないか
    private String name;
    private String kana;
    private String classification;
    private String mean;
    private int accesscount;
    private int leastaccesscount;

    public Word(String name, String kana, String classification, String mean, int accesscount, int leastaccesscount){
        this.name = name;
        this.kana = kana;
        this.classification = classification;
        this.mean = mean;
        this.accesscount = accesscount;
        this.leastaccesscount = leastaccesscount;
    }

    //名前の取得
    public String getName(){
        return name;
    }

    //読み方の取得
    public String getKana(){
        return kana;
    }

    //分類の取得
    public String getClassification(){
        return classification;
    }

    //意味の取得
    public String getMean(){
        return mean;
    }

    //アクセス数の取得
    public int getAccesscount(){
        return accesscount;
    }

    //どのくらいアクセスされていないか取得
    public int getLeastaccesscount(){
        return leastaccesscount;
    }
}
