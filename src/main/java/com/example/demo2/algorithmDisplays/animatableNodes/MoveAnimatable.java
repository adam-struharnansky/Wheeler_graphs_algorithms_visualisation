package com.example.demo2.algorithmDisplays.animatableNodes;

import javafx.util.Pair;

public interface MoveAnimatable extends Animatable {
    void setPosition(double x, double y);
    void setPosition(Pair<Double, Double> position);
    Pair<Double, Double> getPosition();
}
