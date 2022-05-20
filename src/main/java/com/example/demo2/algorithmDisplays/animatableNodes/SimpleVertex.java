package com.example.demo2.algorithmDisplays.animatableNodes;

import com.example.demo2.algorithmDisplays.SimpleGraphDisplay;
import com.example.demo2.auxiliary.AuxiliaryMath;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class SimpleVertex implements MoveAnimatable, ColorAnimatable, RelativeMoveAnimatable, AppearAnimatable {

    private final static double relativePadding = 0.01;

    protected final Pane pane;
    protected final Group group;
    protected final Circle circle;//todo - nemoze byt lokalny, ak budeme chciet zmenit jeho velkost dynamicky - zoom
    protected final Text text;
    protected Color color = Color.LIGHTGRAY;
    protected Color strokeColor = Color.DARKGRAY;
    private boolean visible = true;
    protected double radius = 15.0;

    protected double relativeX;
    protected double relativeY;

    protected double x;
    protected double y;

    private final ArrayList<SimpleEdge> edges = new ArrayList<>();

    protected SimpleGraphDisplay graphDisplay;

    SimpleVertex(SimpleGraphDisplay graphDisplay, Pane pane){
        this.graphDisplay = graphDisplay;
        this.pane = pane;

        this.group = new Group();
        this.circle = new Circle();
        this.circle.setFill(this.color);
        this.circle.setRadius(this.radius);
        this.circle.setStrokeWidth(3.0);
        this.circle.setStroke(this.strokeColor);
        this.text = new Text();
        this.group.getChildren().add(this.circle);
        this.group.getChildren().add(this.text);
        this.pane.getChildren().add(this.group);
        redraw();
    }

    public void setValue(String value){
        this.text.setText(value);
        this.text.autosize();
        this.text.setLayoutX(-this.text.getLayoutBounds().getWidth()/2);
        this.text.setLayoutY(this.text.getLayoutBounds().getHeight()/4);//?
    }

    public void setValue(int value){
        setValue(value+"");
    }

    public void setValue(char value){
        setValue(value+"");
    }

    @Override
    public void setPosition(double x, double y){
        if( x < this.graphDisplay.getWidth()*relativePadding || y < this.graphDisplay.getHeight()*relativePadding ||
                x > this.graphDisplay.getWidth() - this.graphDisplay.getWidth()*relativePadding ||
                y > this.graphDisplay.getHeight() - this.graphDisplay.getHeight()*relativePadding){
            return;
        }
        this.x = x;
        this.y = y;
        this.relativeX = x / (this.graphDisplay.getWidth() + 0.1);
        this.relativeY = y / (this.graphDisplay.getHeight() + 0.1);
        this.group.setLayoutX(x);
        this.group.setLayoutY(y);
        redraw();
    }

    @Override
    public void setPosition(Pair<Double, Double> position){
        setPosition(position.getKey(), position.getValue());
    }

    @Override
    public void setRelativePosition(double relativeX, double relativeY){
        if( relativeX < relativePadding || relativeY < relativePadding || relativeX > 1 - relativePadding || relativeY > 1 - relativePadding){
            return;
        }
        this.x = relativeX * this.graphDisplay.getWidth();
        this.y = relativeY * this.graphDisplay.getHeight();
        this.relativeX = relativeX;
        this.relativeY = relativeY;
        this.group.setLayoutX(this.x);
        this.group.setLayoutY(this.y);
        redraw();
    }

    @Override
    public void setRelativePosition(Pair<Double, Double> position){
        setRelativePosition(position.getKey(), position.getValue());
    }

    @Override
    public Pair<Double, Double> getRelativePosition(){
        return new Pair<>(this.relativeX, this.relativeY);
    }

    public double getRelativeX(){
        return this.relativeX;
    }

    public double getRelativeY() {
        return this.relativeY;
    }

    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }

    @Override
    public Pair<Double, Double> getPosition(){
        return new Pair<>(getX(), getY());
    }

    void addIncomingEdge(SimpleEdge edge){
        if(!this.edges.contains(edge)){
            this.edges.add(edge);
            redraw();
        }
    }

    void addOutgoingEdge(SimpleEdge edge){
        if(!this.edges.contains(edge)){
            this.edges.add(edge);
            redraw();
        }
    }

    public void redraw(){

        this.x = this.relativeX*this.graphDisplay.getWidth();
        this.y = this.relativeY*this.graphDisplay.getHeight();
        this.group.setLayoutX(this.x);
        this.group.setLayoutY(this.y);

        ArrayList<Point2D> ends = new ArrayList<>();
        for(int i = 0; i<this.edges.size();i++){
            ends.add(new Point2D(
                    this.x,
                    0
            ));
        }

        for(int i = 0;i<this.edges.size();i++){
            this.edgeEnds.put(this.edges.get(i), ends.get(i));
        }

        this.edges.forEach(SimpleEdge::redraw);
    }

    private final HashMap<SimpleEdge, Point2D> edgeEnds = new HashMap<>();

    protected Point2D edgeEnd(SimpleEdge edge){
        double angle;
        if(edge.vertexFrom() == this){
            angle = AuxiliaryMath.circularAngle(this.getPosition(), edge.vertexTo().getPosition());
        }
        else {
            angle = AuxiliaryMath.circularAngle(this.getPosition(), edge.vertexFrom().getPosition());
        }
        angle+=Math.PI/2;//Why it works?
        return new Point2D(this.x - radius*Math.cos(angle), this.y - radius*Math.sin(angle));
    }

    public void removeSimpleEdge(Edge edge){
        this.edges.remove(edge);
    }

    public void delete(){
        this.edges.forEach(SimpleEdge::delete);
        this.pane.getChildren().remove(this.group);
    }

    public String getValue(){
        return this.text.getText();
    }


    @Override
    public void setColor(Color color){
        this.color = color;
        this.circle.setFill(this.color);
        Color strokeColor = new Color(this.strokeColor.getRed(), this.strokeColor.getGreen(), this.strokeColor.getBlue(), color.getOpacity());
        this.circle.setStroke(strokeColor);
    }

    @Override
    public Color getColor(){
        return this.color;
    }

    public void highlight(){
        //todo ?
        this.circle.setStrokeWidth(2.0);
        this.text.setStyle("-fx-font-weight: bold");
    }

    public void unhighlight(){
        //todo
        this.circle.setStrokeWidth(1.0);
        this.text.setStyle("-fx-font-weight: normal");
    }

    @Override
    public void appear() {
        this.visible = true;
        this.group.setVisible(true);
        //this.graphDisplay.moveVertexToVisible(this);
        //this.edges.forEach(SimpleEdge::incidentVertexVisibilityChanged);
    }

    @Override
    public void disappear() {
        this.visible = false;
        this.group.setVisible(false);
        //this.graphDisplay.moveVertexToInvisible(this);
        //this.edges.forEach(SimpleEdge::incidentVertexVisibilityChanged);
    }

    @Override
    public double getOpacity(){
        return this.color.getOpacity();
    }

    @Override
    public void setOpacity(double opacity){
        this.text.setFill(new Color(0.0,0.0,0.0,opacity));
        setColor(new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), opacity));
    }

    public boolean isVisible() {
        return this.visible;
    }

    public ArrayList<SimpleEdge> getSimpleEdges(){
        return this.edges;
    }

    public void setRadius(double radius){
        this.radius = radius;
        this.circle.setRadius(radius);
    }
}
