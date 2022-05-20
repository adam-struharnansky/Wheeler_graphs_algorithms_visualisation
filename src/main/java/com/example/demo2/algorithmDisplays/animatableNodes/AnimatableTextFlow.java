package com.example.demo2.algorithmDisplays.animatableNodes;

import com.example.demo2.auxiliary.Colors;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.text.TextFlow;

public class AnimatableTextFlow extends TextFlow implements ColorAnimatable {

    private Color backgroundColor;
    public AnimatableTextFlow(){
        super();
        this.backgroundColor = Color.TRANSPARENT;
    }

    @Override
    public void setColor(Color color) {
        this.backgroundColor = color;
        super.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    @Override
    public Color getColor() {
        return this.backgroundColor;
    }
}
