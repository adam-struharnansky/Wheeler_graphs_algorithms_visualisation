package com.example.demo2.algorithmDisplays.animatableNodes;

import com.example.demo2.algorithmDisplays.SimpleGraphDisplay;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

public class SimpleEdge implements ColorAnimatable, AppearAnimatable{

    private final Line line = new Line();
    private final Text text = new Text();
    private final SimpleVertex vertexFrom;
    private final SimpleVertex vertexTo;
    private final SimpleGraphDisplay graphDisplay;
    private final Pane graphPane;
    private Color color = Color.BLACK;

    SimpleEdge(SimpleGraphDisplay graphDisplay, Pane graphPane, SimpleVertex vertexFrom, SimpleVertex vertexTo){
        this.graphDisplay = graphDisplay;
        this.graphPane = graphPane;
        this.vertexFrom = vertexFrom;
        this.vertexTo = vertexTo;
        this.line.setStrokeWidth(2.0);
        this.graphPane.getChildren().add(this.text);
        this.graphPane.getChildren().add(this.line);
        redraw();
    }

    public void setText(String text){
        this.text.setText(text);
    }

    public void redraw(){
        this.line.setStartX(this.vertexFrom.edgeEnd(this).getX());
        this.line.setStartY(this.vertexFrom.edgeEnd(this).getY());

        this.line.setEndX(this.vertexTo.edgeEnd(this).getX());
        this.line.setEndY(this.vertexTo.edgeEnd(this).getY());

        this.text.setLayoutX((this.vertexFrom.edgeEnd(this).getX() + this.vertexTo.edgeEnd(this).getX())/2
                - this.text.getLayoutBounds().getWidth()/2);
        this.text.setLayoutY((this.vertexFrom.edgeEnd(this).getY() + this.vertexTo.edgeEnd(this).getY())/2
                - this.text.getLayoutBounds().getHeight()/2);
    }

    public SimpleVertex vertexFrom(){
        return vertexFrom;
    }

    public SimpleVertex vertexTo(){
        return vertexTo;
    }

    public void delete(){
        this.graphPane.getChildren().remove(this.line);

    }

    @Override
    public void setColor(Color color) {
        this.color = color;
        this.line.setFill(color);
        this.line.setStroke(color);
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    @Override
    public void appear() {
        this.line.setVisible(true);
    }

    @Override
    public void disappear() {
        this.line.setVisible(false);
    }

    @Override
    public void setOpacity(double opacity) {
        setColor(new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), opacity));
    }

    @Override
    public double getOpacity() {
        return this.color.getOpacity();
    }
}
