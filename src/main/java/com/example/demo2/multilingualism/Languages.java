package com.example.demo2.multilingualism;

import java.util.*;

public class Languages {
    //For adding support for new language, add its code into supportedLanguagesCodes, and write new properties file
    private static final ArrayList<String> supportedLanguagesCodes = new ArrayList<>(Arrays.asList("sk", "en"));
    private static String currentLanguage = "en";

    private static final HashMap<String, Locale> locales = new HashMap<>();
    private static final HashMap<String, ResourceBundle> messages = new HashMap<>();
    private static final ArrayList<LanguageListener> listeners = new ArrayList<>();

    static{
        for(String languageCode:supportedLanguagesCodes){
            locales.put(languageCode, new Locale(languageCode));
            messages.put(languageCode, ResourceBundle.getBundle("message", locales.get(languageCode)));
        }
    }

    public static void addListener(LanguageListener languageListener){
        listeners.add(languageListener);
    }

    public static void setLanguage(String string){
        if(!supportedLanguagesCodes.contains(string)){
            return;
        }
        currentLanguage = string;
        for(LanguageListener languageListener:listeners){
            languageListener.changeOfLanguage();
        }
    }

    public static String getString(String string){
        if(string == null){
            return "null";
        }
        try{
            return messages.get(currentLanguage).getString(string);
        }
        catch (MissingResourceException e){
            if(!string.isEmpty()) {
                System.err.println(e.getMessage());
            }
            return string;
        }
    }

    public static void clearRemovableListeners(){
        listeners.removeIf(LanguageListener::removable);
    }
}
