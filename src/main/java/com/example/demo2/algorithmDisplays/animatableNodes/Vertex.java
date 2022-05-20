package com.example.demo2.algorithmDisplays.animatableNodes;

import com.example.demo2.algorithmDisplays.GraphDisplay;
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

/*
Undirected Vertex
 */
public class Vertex implements MoveAnimatable, ColorAnimatable, RelativeMoveAnimatable, AppearAnimatable {

    private final static double relativePadding = 0.01;

    protected final Pane pane;
    protected final Group group;
    protected final Circle circle;
    protected final Text text;
    protected Color color = Color.LIGHTGRAY;
    protected Color strokeColor = Color.DARKGRAY;
    private boolean visible = true;
    protected double radius = 15.0;

    protected double relativeX;
    protected double relativeY;

    protected double x;
    protected double y;

    private final ArrayList<Edge> edges;

    protected GraphDisplay graphDisplay;

    Vertex(GraphDisplay graphDisplay, Pane pane){
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
        this.group.setOnMouseDragged(mouseEvent -> {
            //System.out.println(mouseEvent.getY()+" "+ mouseEvent.getSceneY()+" "+mouseEvent.getScreenY());
            setPosition(this.x + mouseEvent.getX(), this.y + mouseEvent.getY());
        });
        this.edges = new ArrayList<>();
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

    void addIncomingEdge(Edge edge){
        if(!this.edges.contains(edge)){
            this.edges.add(edge);
            redraw();
        }
    }

    void addOutgoingEdge(Edge edge){
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

        ArrayList<Pair<Point2D, ArrayList<Edge>>> ends = new ArrayList<>();
        ArrayList<Double> angles = new ArrayList<>();
        int endsSize = this.edges.size() + 6;
        for(int i = 0; i<endsSize;i++){
            ends.add(new Pair<>(
                    new Point2D(
                            Math.cos(2*Math.PI*i/endsSize)*this.radius + this.x,
                            Math.sin(2*Math.PI*i/endsSize)*this.radius + this.y),
                    new ArrayList<>()));
            angles.add((2*Math.PI*i)/(double)endsSize);
        }

        //Give every edge the best to attach itself (computed by angle)
        for(Edge edge:this.edges){
            double angle = 0.0;
            if(edge.getVertexFrom() == this){
                angle = AuxiliaryMath.circularAngle(this.getPosition(), edge.getVertexTo().getPosition());
            }
            else{
                angle = AuxiliaryMath.circularAngle(this.getPosition(), edge.getVertexFrom().getPosition());
            }
            angle = (angle > Math.PI/2)?(angle - Math.PI/2):(angle + 3*Math.PI/2);
            double lowestDifference = Double.POSITIVE_INFINITY;
            ArrayList<Edge> best = new ArrayList<>();
            for(int i = 0;i<ends.size();i++){
                if(lowestDifference > Math.abs(angle - angles.get(i))){
                    lowestDifference = Math.abs(angle - angles.get(i));
                    best = ends.get(i).getValue();
                }
            }
            best.add(edge);
        }

        //Only one edge on one edge point - if there are more, move all but one counter-clockwise
        ArrayList<Edge> multipleEdges = new ArrayList<>();
        int it = 0;
        do {
            int i = it % endsSize;
            ends.get(i).getValue().addAll(multipleEdges);
            multipleEdges.clear();
            while (ends.get(i).getValue().size() > 1) {
                //todo - Choose one with biggest loss if moved
                multipleEdges.add(ends.get(i).getValue().remove(0));
            }
            it++;
        } while (it <= endsSize || !multipleEdges.isEmpty());

        for (Pair<Point2D, ArrayList<Edge>> end : ends) {
            Point2D endPoint = end.getKey();
            end.getValue().forEach(edge -> this.edgeEnds.put(edge, endPoint));
        }
        this.edges.forEach(Edge::redraw);
    }

    private final HashMap<Edge, Point2D> edgeEnds = new HashMap<>();

    protected Point2D edgeEnd(Edge edge){
        if(this.edgeEnds.containsKey(edge)) {
            return this.edgeEnds.get(edge);
        }
        return new Point2D(this.x, this.y);
    }

    public void removeEdge(Edge edge){
        this.edges.remove(edge);
    }

    public void delete(){
        this.edges.forEach(Edge::delete);
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
        this.circle.setStrokeWidth(5.0);
        this.text.setStyle("-fx-font-weight: bold");
    }

    public void unhighlight(){
        this.circle.setStrokeWidth(3.0);
        this.text.setStyle("-fx-font-weight: normal");
    }

    @Override
    public void appear() {
        this.visible = true;
        this.group.setVisible(true);
        this.graphDisplay.moveVertexToVisible(this);
        this.edges.forEach(Edge::incidentVertexVisibilityChanged);
    }

    @Override
    public void disappear() {
        this.visible = false;
        this.group.setVisible(false);
        this.graphDisplay.moveVertexToInvisible(this);
        this.edges.forEach(Edge::incidentVertexVisibilityChanged);
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

    public ArrayList<Edge> getEdges(){
        return this.edges;
    }


    public void setFromMemento(VertexMemento vertexMemento){
        setRelativePosition(relativeX, relativeY);
        setValue(vertexMemento.value);
        this.edges.clear();
        for(int i = 0;i<vertexMemento.edgesFrom.size();i++){
            Edge edge = this.graphDisplay.addEdge(vertexMemento.neighboursFrom.get(i), this);
            edge.setText(vertexMemento.edgesFrom.get(i));
        }
        for(int i = 0;i<vertexMemento.edgesTo.size();i++){
            Edge edge = this.graphDisplay.addEdge(this, vertexMemento.neighboursTo.get(i));
            edge.setText(vertexMemento.edgesTo.get(i));
        }
    }

    public VertexMemento getMemento(){
        return new VertexMemento(this);
    }

    public static class VertexMemento{
        double relativeX, relativeY;
        String value;
        ArrayList<String> edgesTo = new ArrayList<>();
        ArrayList<Vertex> neighboursTo = new ArrayList<>();
        ArrayList<String> edgesFrom = new ArrayList<>();
        ArrayList<Vertex> neighboursFrom = new ArrayList<>();

        VertexMemento(Vertex vertex){
            this.relativeX = vertex.getRelativeX();
            this.relativeY = vertex.getRelativeY();
            this.value = vertex.getValue();
            for(Edge edge:vertex.edges){
                if(edge.getVertexFrom() == vertex){
                    this.edgesTo.add(edge.getText());
                    this.neighboursTo.add(edge.getVertexTo());
                }
                else{
                    this.edgesFrom.add(edge.getText());
                    this.neighboursFrom.add(edge.getVertexFrom());
                }
            }
        }
    }
}
