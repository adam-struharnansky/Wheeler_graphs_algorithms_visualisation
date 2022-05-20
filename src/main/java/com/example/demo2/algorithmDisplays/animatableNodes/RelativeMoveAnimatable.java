package com.example.demo2.algorithmDisplays.animatableNodes;

import javafx.util.Pair;

public interface RelativeMoveAnimatable extends Animatable{
    void setRelativePosition(double x, double y);
    void setRelativePosition(Pair<Double, Double> position);
    Pair<Double, Double> getRelativePosition();
}
