package com.example.demo2.algorithmDisplays;

import com.example.demo2.multilingualism.LanguageListenerAdder;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.HashMap;

public class Display{

    private final VBox container;
    private final ToolBar toolBar;
    private boolean isMinimized;
    private final String name;
    private final Pane pane;
    private final int ratio;

    private double width;
    private double height;

    private static double menuHeight = 20.0;

    public Display(VBox container, String name, int ratio){
        this.container = container;
        this.name = name;
        this.ratio = ratio;
        this.isMinimized = false;
        this.pane = new Pane();
        container.setFillWidth(true);

        this.toolBar = new ToolBar();
        this.toolBar.setStyle("-fx-border-color: black");
        Label nameLabel = new Label();
        LanguageListenerAdder.addLanguageListener(name, nameLabel);
        this.toolBar.getItems().add(nameLabel);
        this.toolBar.setPrefWidth(menuHeight);

        Button changeSizeButton = new Button("*");//todo - porozmyslat, ako toto vykrelsit co najlepsie
        changeSizeButton.setOnAction(actionEvent -> {
            if (isMinimized) {
                maximize();
            } else {
                minimize();
            }
        });

        this.toolBar.getItems().add(changeSizeButton);
        this.container.getChildren().add(this.toolBar);
        this.container.getChildren().add(this.pane);
    }

    public  void centre(){
        resize();
    }

    public void resize(){
    }

    private void minimize(){
        if(this.isMinimized){
            return;
        }
        this.container.getChildren().remove(this.pane);
        setSize(20.0, this.height);//todo - toto sa mozno zmeni
        this.isMinimized = true;
        WindowManager.redrawDisplays();
    }

    private void maximize(){
        if(!this.isMinimized){
            return;
        }
        this.container.getChildren().add(this.pane);
        this.isMinimized = false;
        WindowManager.redrawDisplays();
    }

    public boolean isMinimized(){
        return this.isMinimized;
    }

    public double getWidth(){
        return this.width;
    }

    public double getHeight(){
        toolBar.autosize();
        return this.height - toolBar.getHeight();
    }

    protected Pane getPane(){
        return this.pane;
    }

    protected Pair<Double, Double> getPosition(){
        return new Pair<>(this.container.getLayoutX(), this.container.getLayoutY());
    }

    protected int getRatio(){
        return this.ratio;
    }

    protected void setSize(double width, double height){
        this.toolBar.setMinWidth(width);
        this.toolBar.setPrefWidth(width);

        this.pane.setMaxSize(width - 20.0, height);
        this.pane.setPrefSize(width -20.0, height);
        this.container.setMaxSize(width, height);

        this.width = width;
        this.height = height;
    }

    public String getName(){
        return this.name;
    }

    //napr centre, beautify, mozno zmena velksti pisma? to by bolo faj mat vsade
    interface ControlButtonAction{
        void doAction();
    }

    private final HashMap<String, Button> namesButtonsMap = new HashMap<>();

    protected void addButton(String name, ControlButtonAction controlButtonAction){
        Button controlButton = new Button();
        controlButton.setOnAction(actionEvent -> controlButtonAction.doAction());
        LanguageListenerAdder.addLanguageListener(name, controlButton);
        this.toolBar.getItems().add(1, controlButton);
        this.namesButtonsMap.put(name, controlButton);
    }

    protected void setDisable(String name, boolean disable){
        if(namesButtonsMap.containsKey(name)){
            namesButtonsMap.get(name).setDisable(disable);
        }
    }

    protected void disableButtons(){
        this.toolBar.getItems().forEach((item) -> item.setDisable(true));
    }

    protected void enableButtons(){
        this.toolBar.getItems().forEach((item) -> item.setDisable(false));
    }

    protected void delete(){
        this.pane.getChildren().remove(this.container);
    }

}
