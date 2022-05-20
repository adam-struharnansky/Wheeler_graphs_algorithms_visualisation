package com.example.demo2.Step;

import com.example.demo2.algorithmDisplays.animatableNodes.Edge;
import javafx.scene.paint.Color;

public record AddWay (Edge edge, int id, Color color) implements DiscreteChange{


    @Override
    public void backStep() {
        this.edge.removeWay(this.id);
    }

    @Override
    public void forwardStep() {
        this.edge.addWay(this.id, this.color);
    }
}
