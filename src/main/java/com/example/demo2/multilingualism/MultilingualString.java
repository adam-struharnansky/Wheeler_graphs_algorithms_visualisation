package com.example.demo2.multilingualism;

public class MultilingualString implements Stringable{
    String text;

    public MultilingualString(String text){
        this.text = text;
    }

    @Override
    public String getString(){
        return Languages.getString(text);
    }
}
