package com.example.demo2.multilingualism;

import javafx.scene.control.MenuItem;

public class MultilingualMenuItem extends MenuItem implements LanguageListener{
    private final Stringable text;
    private final MenuItem menuItem;

    public MultilingualMenuItem(Stringable text){
        super(text.getString());
        this.text = text;
        this.menuItem = null;
        Languages.addListener(this);
    }

    public MultilingualMenuItem(String text){
        this(new MultilingualString(text));
    }

    //code smell?
    public MultilingualMenuItem(Stringable text, MenuItem menuItem){
        this.menuItem = menuItem;
        this.text = text;
        Languages.addListener(this);
    }

    @Override
    public void changeOfLanguage() {
        if(this.menuItem == null){
            super.setText(this.text.getString());
        }
        else{
            this.menuItem.setText(this.text.getString());
        }
    }

    @Override
    public boolean removable(){
        return false;
    }
}
