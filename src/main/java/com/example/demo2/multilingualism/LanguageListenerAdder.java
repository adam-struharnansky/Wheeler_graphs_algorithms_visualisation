package com.example.demo2.multilingualism;

import com.example.demo2.algorithmDisplays.SelectorDisplay;
import com.example.demo2.algorithmDisplays.TextDisplay;
import javafx.scene.control.Labeled;
import javafx.scene.control.MenuItem;

public class LanguageListenerAdder {

    public static void addLanguageListener(String key, Labeled labeled){
        labeled.setText(Languages.getString(key));
        class LabeledLanguageListener implements LanguageListener{
            @Override
            public void changeOfLanguage() {
                labeled.setText(Languages.getString(key));
            }
            @Override
            public boolean removable(){
                return true;
            }
        }
        Languages.addListener(new LabeledLanguageListener());
    }

    //For Menu, MenuItem, ...
    public static void addLanguageListener(String key, MenuItem menuItem){
        menuItem.setText(Languages.getString(key));
        class MenuItemLanguageListener implements LanguageListener{
            @Override
            public void changeOfLanguage(){
                menuItem.setText(Languages.getString(key));
            }
            @Override
            public boolean removable(){
                return true;
            }
        }
        Languages.addListener(new MenuItemLanguageListener());
    }

    //For Menu, MenuItem, ..., with possibility to
    public static void addLanguageListener(String key, MenuItem menuItem, boolean removable){
        menuItem.setText(Languages.getString(key));
        class MenuItemLanguageListener implements LanguageListener{
            @Override
            public void changeOfLanguage(){
                menuItem.setText(Languages.getString(key));
            }
            @Override
            public boolean removable(){
                return removable;
            }
        }
        Languages.addListener(new MenuItemLanguageListener());
    }

    public static void addLanguageListener(TextDisplay textDisplay){
        textDisplay.changeLanguage();
        class MenuItemLanguageListener implements LanguageListener{
            @Override
            public void changeOfLanguage(){
                textDisplay.changeLanguage();
            }
            @Override
            public boolean removable(){
                return true;
            }
        }
        Languages.addListener(new MenuItemLanguageListener());
    }

    public static void addLanguageListener(SelectorDisplay selectorDisplay){
        class MenuItemLanguageListener implements LanguageListener{
            @Override
            public void changeOfLanguage(){
                selectorDisplay.changeLanguage();
            }
            @Override
            public boolean removable(){
                return true;
            }
        }
        Languages.addListener(new MenuItemLanguageListener());
    }
}
