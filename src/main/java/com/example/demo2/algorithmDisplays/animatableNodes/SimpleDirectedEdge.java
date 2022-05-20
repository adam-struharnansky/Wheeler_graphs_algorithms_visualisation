package com.example.demo2.algorithmDisplays.animatableNodes;

import com.example.demo2.algorithmDisplays.SimpleGraphDisplay;
import com.example.demo2.auxiliary.AuxiliaryMath;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class SimpleDirectedEdge extends SimpleEdge{

    private final Polygon triangle;
    private final Pane graphPane;


    SimpleDirectedEdge(SimpleGraphDisplay graphDisplay, Pane graphPane, SimpleVertex vertexFrom, SimpleVertex vertexTo){
        super(graphDisplay, graphPane, vertexFrom, vertexTo);
        this.graphPane = graphPane;
        this.triangle = new Polygon(4.0, 0.0, 0.0, 8.0, 8.0, 8.0);
        this.graphPane.getChildren().add(this.triangle);
    }

    @Override
    public void redraw(){
        super.redraw();
        if(this.triangle == null){//called from SimpleEdge constructor
            return;
        }
        double angle = AuxiliaryMath.circularAngle(super.vertexFrom().getPosition(), super.vertexTo().getPosition());
        this.triangle.setRotate(angle*57.2957795);
        this.triangle.setLayoutX(super.vertexTo().edgeEnd(this).getX() - 3.0);
        this.triangle.setLayoutY(super.vertexTo().edgeEnd(this).getY() - 3.0);
    }

    @Override
    public void delete(){
        super.delete();
        this.graphPane.getChildren().add(this.triangle);
    }

    @Override
    public void setColor(Color color) {
        super.setColor(color);
        this.triangle.setFill(color);
        this.triangle.setStroke(color);
    }
}
