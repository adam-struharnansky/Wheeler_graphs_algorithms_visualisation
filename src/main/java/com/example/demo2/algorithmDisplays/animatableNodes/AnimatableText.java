package com.example.demo2.algorithmDisplays.animatableNodes;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class AnimatableText extends Text implements ColorAnimatable, AppearAnimatable {

    private final Rectangle background;
    private final Pane pane;
    private Color color;

    public AnimatableText(Pane pane){
        super();
        this.pane = pane;
        this.background = new Rectangle();
        this.background.setFill(Color.TRANSPARENT);
        this.pane.getChildren().add(this.background);
    }

    public AnimatableText(Pane pane, String string){
        super(string);
        this.pane = pane;
        this.background = new Rectangle();
        this.background.setFill(Color.TRANSPARENT);
        this.pane.getChildren().add(this.background);
    }

    public void redraw(){
        super.autosize();
        this.background.setLayoutY(super.getLayoutY() + super.getLayoutBounds().getMinY());
        this.background.setLayoutX(super.getLayoutX() + super.getLayoutBounds().getMinX());
        this.background.setHeight(super.getLayoutBounds().getHeight());
        this.background.setWidth(super.getLayoutBounds().getWidth());
    }

    public void removeBackground(){
        this.pane.getChildren().remove(this.background);
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
        this.background.setFill(color);
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public void appear() {
        super.setVisible(true);
    }

    @Override
    public void disappear() {
        super.setVisible(false);
    }
}
