package com.example.demo2.algorithmDisplays;

import com.example.demo2.multilingualism.LanguageListenerAdder;
import com.example.demo2.multilingualism.Languages;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.ArrayList;

public class TextDisplay extends Display{

    /*
    todo reprogram, add possibility to use style for each inputted String
    possible to use commented class TextPart in the end of this file
     */

    private final static double padding = 10.0;

    private final Pane pane;
    private final ArrayList<Text> texts = new ArrayList<>();

    private final ArrayList<Pair<String, Boolean>> keys = new ArrayList<>();

    public TextDisplay(VBox container, String name, int ratio) {
        super(container, name, ratio);
        this.pane = super.getPane();
        LanguageListenerAdder.addLanguageListener(this);
    }

    @Override
    public void centre(){

        this.texts.forEach(text -> this.pane.getChildren().remove(text));
        this.texts.clear();

        StringBuilder stringBuilder = new StringBuilder();
        this.keys.forEach(pair-> {
            if(pair.getValue()){
                stringBuilder.append(Languages.getString(pair.getKey()));
            }
            else{
                stringBuilder.append(pair.getKey());
            }
            stringBuilder.append(" ");
        });

        String content = stringBuilder.toString();
        while(content.length() > 0){
            Text line = new Text();
            this.pane.getChildren().add(line);
            //Binary search to find maximum length of text, that fit in one line
            int str = 0, end = content.length();
            line.setText(content);
            while(end > str + 1){
                int mid = (str + end)/2;
                line.setText(content.substring(0, mid + 1));
                line.autosize();
                if(line.getLayoutBounds().getWidth() >= super.getWidth() - padding*2.0){
                    end = mid;
                }
                else{
                    str = mid;
                }
            }
            //If whole text fit in one line, then end
            if(end == content.length()){
                this.texts.add(line);
                break;
            }
            //Divide line at position of last space
            boolean hasSpace = false;
            for(int i = str;i>0;i--){
                if(content.charAt(i) == ' '){
                    line.setText(content.substring(0,i));
                    content = content.substring(i + 1);
                    hasSpace = true;
                    break;
                }
            }
            //If there was no space in line, add hyphen to end
            if(!hasSpace){
                line.setText(content.substring(0,str)+ "-");
                content = content.substring(str);
            }
            this.texts.add(line);
        }

        double y = 2*padding;
        for(Text text:this.texts){
            text.autosize();
            text.setLayoutY(y);
            y += padding + text.getLayoutBounds().getHeight();
            text.setLayoutX(padding);
        }
    }

    @Override
    public void resize(){
        centre();
    }

    public void addString(String key, String style, boolean translatable){
        //for styles this.textParts.add(new TextPart(key, style, translatable));
        this.keys.add(new Pair<>(key, translatable));
        centre();
    }

    public void clear(){
        this.texts.forEach(text -> this.pane.getChildren().remove(text));
        this.texts.clear();
        this.keys.clear();
        //for styles this.textParts.clear();
    }

    public void changeLanguage(){
        this.texts.forEach(text -> this.pane.getChildren().remove(text));
        this.texts.clear();
        centre();
    }

    /*
    for styles
    private static class TextPart{
        String key;
        Text text;
        String style;
        boolean translatable;
        TextPart(String key, String style, boolean translatable){
            this.key = key;
            this.text = new Text();
            this.text.setStyle(style);
            this.style = style;
            this.translatable = translatable;
            changeLanguage();
        }
        void changeLanguage(){
            if(translatable){
                this.text.setText(Languages.getString(this.key));
            }
            else{
                this.text.setText(this.key);
            }
        }
    }
    private final ArrayList<TextPart> textParts = new ArrayList<>();
    */
}
