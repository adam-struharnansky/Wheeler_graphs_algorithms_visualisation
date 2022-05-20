package com.example.demo2.algorithmDisplays;

import com.example.demo2.algorithmManager.AlgorithmManager;
import com.example.demo2.algorithmManager.AlgorithmType;
import com.example.demo2.multilingualism.LanguageListenerAdder;
import com.example.demo2.multilingualism.Languages;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class SelectorDisplay extends Display{

    private final ArrayList<Text> texts = new ArrayList<>();
    private final Text header = new Text();
    private final Button button = new Button();
    private final Pane pane;
    private String nameKey;
    private String descriptionKey;

    static final double padding = 10.0;


    public SelectorDisplay(VBox container, String name, int ratio) {
        super(container, name, ratio);
        this.pane = super.getPane();
        this.button.setText("\u27A4");
        this.header.setStyle("-fx-font-weight: bold");
        this.pane.getChildren().add(this.button);
        this.pane.getChildren().add(this.header);
        LanguageListenerAdder.addLanguageListener(this);
    }

    public void setChoice(String name, String description, AlgorithmType algorithmType,
                          AlgorithmManager algorithmManager){
        this.descriptionKey = description;
        this.nameKey = name;
        this.button.setOnAction(actionEvent -> algorithmManager.changeAlgorithm(algorithmType));
        centre();
    }

    @Override
    public void centre(){
        this.texts.forEach(text -> this.pane.getChildren().remove(text));
        this.texts.clear();

        this.header.setText(Languages.getString(this.nameKey));
        this.header.autosize();
        this.header.setLayoutX(super.getWidth()/2 - this.header.getLayoutBounds().getCenterX());
        this.header.setLayoutY(2*padding);

        String description = Languages.getString(this.descriptionKey);
        while(description.length() > 0){
            Text line = new Text();
            this.pane.getChildren().add(line);
            //Binary search to find maximum length of text, that fit in one line
            int str = 0, end = description.length();
            line.setText(description);
            while(end > str + 1){
                int mid = (str + end)/2;
                line.setText(description.substring(0, mid + 1));
                line.autosize();
                if(line.getLayoutBounds().getWidth() >= super.getWidth() - padding*2.0){
                    end = mid;
                }
                else{
                    str = mid;
                }
            }
            //If whole text fit in one line, then end
            if(end == description.length()){
                this.texts.add(line);
                break;
            }
            //Divide line at position of last space
            boolean hasSpace = false;
            for(int i = str;i>0;i--){
                if(description.charAt(i) == ' '){
                    line.setText(description.substring(0,i));
                    description = description.substring(i + 1);
                    hasSpace = true;
                    break;
                }
            }
            //If there was no space in line, add hyphen to end
            if(!hasSpace){
                line.setText(description.substring(0,str)+ "-");
                description = description.substring(str);
            }
            this.texts.add(line);
        }

        double y = 4*padding + this.header.getLayoutBounds().getHeight();
        for(Text text:this.texts){
            text.setLayoutY(y);
            y += padding + text.getLayoutBounds().getHeight();
            text.setLayoutX(padding);
        }
        this.button.autosize();
        this.button.setLayoutX(super.getWidth()/2 - this.button.getLayoutBounds().getCenterX());
        this.button.setLayoutY(Math.min(y + padding*2, super.getHeight()-padding));
    }

    public void changeLanguage(){
        centre();
    }

    @Override
    public void resize(){
        centre();
    }
}
