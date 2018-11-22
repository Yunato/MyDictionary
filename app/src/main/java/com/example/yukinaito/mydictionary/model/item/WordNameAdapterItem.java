package com.example.yukinaito.mydictionary.model.item;

public class WordNameAdapterItem {
    /** 単語名 (ラベル名含む) 格納リスト  */
    private final String id;
    private final String name;
    private final boolean visible;

    /**
     * コンストラクタ
     * @param id 単語に対応した SQLite の ID
     * @param name 単語名
     * @param visible 可視化
     */
    public WordNameAdapterItem(String id, String name, boolean visible){
        this.id = id;
        this.name = name;
        this.visible = visible;
    }

    /**
     * 単語に対応した SQLite の ID を取得する
     * @return 単語に対応した ID
     */
    public String getId(){
        return this.id;
    }

    /**
     * 単語名を取得する
     * @return 単語名
     */
    public String getName(){
        return this.name;
    }

    /**
     * 可視化の許可を取得する
     * @return 可視化の許可
     */
    public boolean getVisible(){
        return this.visible;
    }
}
