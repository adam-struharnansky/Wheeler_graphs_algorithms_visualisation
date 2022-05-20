package com.example.demo2.algorithmDisplays.animatableNodes;

import javafx.scene.paint.Color;

public interface AppearAnimatable extends Animatable{
    void appear();
    void disappear();
    void setOpacity(double opacity);
    double getOpacity();
}
