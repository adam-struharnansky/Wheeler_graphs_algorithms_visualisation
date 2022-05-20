package com.example.demo2.multilingualism;

import javafx.scene.control.Button;

public class MultilingualButton extends Button implements LanguageListener{
    private Stringable text;

    public MultilingualButton(Stringable text){
        super(text.getString());
        this.text = text;
        Languages.addListener(this);
    }

    public MultilingualButton(String text){
        this(new MultilingualString(text));
    }

    @Override
    public void changeOfLanguage() {
        super.setText(this.text.getString());
    }

    @Override
    public boolean removable(){
        return true;
    }

    //chceme triedu, ktora bude mat to, ze vieme vlozit objekt,
    //ktory bude mat funkciu
    //vsetky su z java.scene.control, pozriet sa od koho to beru
    //a potom vsetky vieme pridat asi do nej
    //bude mat konstruktor, ktory berie string a
}
