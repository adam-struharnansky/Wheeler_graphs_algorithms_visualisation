package com.example.demo2.algorithmDisplays.animatableNodes;

import com.example.demo2.algorithmDisplays.GraphDisplay;
import com.example.demo2.auxiliary.AuxiliaryMath;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.util.Pair;

public class DirectedEdge extends Edge {

    private final Polygon triangle;

    DirectedEdge(GraphDisplay graphDisplay, Pane graphPane, Vertex vertexFrom, Vertex vertexTo){
        super(graphDisplay,graphPane, vertexFrom, vertexTo);
        this.triangle = new Polygon(4.0, 0.0, 0.0, 8.0, 8.0, 8.0);//todo - Dynamic size change
        this.graphPane.getChildren().add(this.triangle);
        this.triangle.toFront();
        redraw();
    }

    @Override
    public void delete() {
        super.delete();
        this.graphPane.getChildren().remove(this.triangle);
    }

    @Override
    public void redraw() {
        super.redraw();
        if(this.triangle == null){//If is it call from Edge constructor
            return;
        }

        Pair<Double, Double> directionVector = new Pair<>(
                this.vertexTo.edgeEnd(this).getX() - this.vertexTo.getPosition().getKey(),
                this.vertexTo.edgeEnd(this).getY() - this.vertexTo.getPosition().getValue());
        double directionVectorSize = AuxiliaryMath.vectorSize(directionVector);
        if (directionVectorSize == 0.0) {
            directionVectorSize = 0.1;
        }
        directionVector = new Pair<>(this.graphDisplay.getVertexRadius() * directionVector.getKey() / directionVectorSize,
                this.graphDisplay.getVertexRadius() * directionVector.getValue() / directionVectorSize);

        //todo Dynamic size change (change 4.0 to something computed)
        this.triangle.setLayoutX(this.vertexTo.getPosition().getKey() + directionVector.getKey() - 4.0);
        this.triangle.setLayoutY(this.vertexTo.getPosition().getValue() + directionVector.getValue() - 4.0);

        double rotationAngle = AuxiliaryMath.circularAngle(new Pair<>(super.endControl.getX(), super.endControl.getY()),
                new Pair<>(super.endPoint.getX(), super.endPoint.getY()));
        this.triangle.setRotate(rotationAngle * 57.2957795);
    }

    @Override
    public void setVisible(boolean visible){
        super.setVisible(visible);
        this.triangle.setVisible(visible);
    }

    @Override
    public void setOpacity(double opacity){
        super.setOpacity(opacity);
        this.triangle.setOpacity(opacity);
    }

    @Override
    public void setColor(Color color){
        super.setColor(color);
        if(super.getWaysNumber() == 1){
            this.triangle.setStroke(color);
            this.triangle.setFill(color);
        }
    }

    @Override
    public void toPaneFront(){
        if(this.triangle != null) {
            this.triangle.toFront();
        }
    }
}
