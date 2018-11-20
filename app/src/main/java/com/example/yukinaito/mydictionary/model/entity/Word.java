package com.example.yukinaito.mydictionary.model.entity;

import java.io.Serializable;

public class Word implements Serializable{
    /** 単語情報パラメータ */
    private final String name;
    private final String kana;
    private final String field;
    private final String mean;
    private final int accesscount;
    private final int leastaccesscount;
    private final int date;

    /**
     * コンストラクタ
     * @param name 単語名
     * @param kana 読み方
     * @param field 分野名
     * @param mean 単語の意味
     * @param accesscount アクセス数
     * @param leastaccesscount 最後にアクセスしてから, 他の単語にアクセスした回数
     * @param date 単語の登録日時
     */
    public Word(String name, String kana, String field, String mean, int accesscount, int leastaccesscount, int date){
        this.name = name;
        this.kana = kana;
        this.field = field;
        this.mean = mean;
        this.accesscount = accesscount;
        this.leastaccesscount = leastaccesscount;
        this.date = date;
    }

    /** 単語名を取得する */
    public String getName(){
        return name;
    }

    /** 読み方を取得する */
    public String getKana(){
        return kana;
    }

    /** 分野名を取得する */
    public String getField(){
        return field;
    }

    /** 単語の意味を取得する */
    public String getMean(){
        return mean;
    }

    /** アクセス数を取得する */
    public int getAccesscount(){
        return accesscount;
    }

    /** 最後にアクセスしてから, 他の単語にアクセスした回数を取得する */
    public int getLeastaccesscount(){
        return leastaccesscount;
    }

    /** 単語の登録日時を取得する */
    public int getDate(){
        return this.date;
    }
}
