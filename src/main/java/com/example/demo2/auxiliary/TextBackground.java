package com.example.demo2.auxiliary;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class TextBackground{

    private final Rectangle background;
    private final Text text;
    private final Pane pane;

    public TextBackground(Text text, Pane pane){
        this.text = text;
        this.background = new Rectangle();
        this.background.setFill(Color.TRANSPARENT);
        //this.background.setStroke(Color.BLACK); //pre debugovanie
        this.pane = pane;
        pane.getChildren().add(this.background);
        redraw();
    }

    public void redraw(){
        this.text.autosize();
        this.background.setLayoutY(this.text.getLayoutY() + this.text.getLayoutBounds().getMinY());
        this.background.setLayoutX(this.text.getLayoutX() + this.text.getLayoutBounds().getMinX());
        this.background.setHeight(this.text.getLayoutBounds().getHeight());
        this.background.setWidth(this.text.getLayoutBounds().getWidth());
    }

    public void highlight(){
        //todo dat to na nejaku peknu farbu
        this.background.setFill(Color.color(1.0,1.0,0.0,0.3));
    }

    public void unhighlight(){
        this.background.setFill(Color.TRANSPARENT);
    }

    public void setColor(Color color){
        this.background.setFill(color);
    }

    public void erase(){
        this.background.setFill(Color.TRANSPARENT);
        this.pane.getChildren().remove(this.background);
    }
}