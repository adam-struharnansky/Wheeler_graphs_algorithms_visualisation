package com.example.demo2.algorithmDisplays;

import com.example.demo2.algorithmDisplays.animatableNodes.Edge;
import com.example.demo2.algorithmDisplays.animatableNodes.GraphNodesFactory;
import com.example.demo2.algorithmDisplays.animatableNodes.Vertex;
import com.example.demo2.animations.Animation;
import com.example.demo2.animations.AnimationType;
import com.example.demo2.auxiliary.AuxiliaryMath;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class GraphDisplay extends Display{

    //todo - Add un/zoom

    private final GraphNodesFactory graphNodesFactory;
    private boolean directed;

    private final Pane pane;
    private final ArrayList<Vertex> vertices = new ArrayList<>();
    private final ArrayList<Vertex> invisibleVertices = new ArrayList<>();
    private final ArrayList<Vertex> visibleVertices = new ArrayList<>();
    private final ArrayList<Edge> edges = new ArrayList<>();

    private double vertexRadius = 15.0;

    public GraphDisplay(VBox container, String name, int ratio, boolean directed) {
        super(container, name, ratio);
        this.pane = super.getPane();
        this.directed = directed;
        this.graphNodesFactory = new GraphNodesFactory(this, this.pane, this.directed);

        super.addButton("centre", this::centre);
        super.addButton("beautify", this::beautify);
    }

    @Override
    public void centre(){
        double mostLeft = Double.POSITIVE_INFINITY, mostRight = Double.NEGATIVE_INFINITY,
                mostTop = Double.POSITIVE_INFINITY, mostDown = Double.NEGATIVE_INFINITY;
        for(Vertex vertex:this.visibleVertices){
            if(vertex.getX() < mostLeft){
                mostLeft = vertex.getX();
            }
            if(vertex.getX() > mostRight){
                mostRight = vertex.getX();
            }
            if(vertex.getY() < mostTop){
                mostTop = vertex.getY();
            }
            if(vertex.getY() > mostDown){
                mostDown = vertex.getY();
            }
        }
        double horizontalGap = super.getWidth()/(this.visibleVertices.size() + 1),
                verticalGap = super.getHeight()/(this.visibleVertices.size() + 1);
        double newHorizontalSize = super.getWidth() - 2*horizontalGap + 0.1, oldHorizontalSize = mostRight - mostLeft + 0.1,
                newVerticalSize = super.getHeight() - 2*verticalGap + 0.1, oldVerticalSize = mostDown - mostTop + 0.1;
        for(Vertex vertex:this.visibleVertices){
            vertex.setPosition(horizontalGap + newHorizontalSize*(vertex.getX() - mostLeft)/oldHorizontalSize,
                    verticalGap + newVerticalSize*(vertex.getY() - mostTop)/oldVerticalSize);
        }
    }

    public void beautify(){
        beautify(150);
    }

    public void beautify(int stepsNumber){
        Animation animation = new Animation();
        beautify(animation, stepsNumber);
        animation.endAnimation();
    }

    public void beautify(Animation animation){
        beautify(animation, 150);
    }

    public void beautify(Animation animation, int stepsNumber){
        HashMap<Vertex, double[]> positions = new HashMap<>();
        for(Vertex vertex:this.vertices){
            positions.put(vertex, new double[]{vertex.getRelativeX(), vertex.getRelativeY()});
        }
        Random random = new Random();
        double q = 0.0075;//magneticky naboj = zdroj odpudivej sily
        double g = 0.0001;//pseudo-gravitacna sila
        double k = 0.02;//tuhost hran
        double w = 0.003;//nahodny pohyb
        for(int i = 0;i<stepsNumber;i++){
            //kazdy vrchol je nahodne pohnuty
            positions.forEach(((vertex, position) -> {
                position[0] += (0.5 - random.nextDouble())*w;
                position[1] += (0.5 - random.nextDouble())*w;
            }));

            //zabezpecit, aby to stale bolo vnutri
            positions.forEach(((vertex, position) -> {
                position[0] = Math.max(0.0, position[0]);
                position[0] = Math.min(1.0, position[0]);
                position[1] = Math.max(0.0, position[1]);
                position[1] = Math.min(1.0, position[1]);
            }));

            for(int m = 0;m<positions.size();m++){
                for(int n = m;n<positions.size(); n++){
                    //kazdu dvojicu vrcholov navzajom oddialit pomocou coulumbovho zakona
                    Pair<Double, Double> mnVector = new Pair<>(
                            positions.get(vertices.get(n))[0] - positions.get(vertices.get(m))[0],
                            positions.get(vertices.get(n))[1] - positions.get(vertices.get(m))[1]);
                    double r = Math.max(0.0001, AuxiliaryMath.vectorSize(mnVector));
                    double f = (q*q)/(r*r);
                    Pair<Double, Double> unitMN = new Pair<>(mnVector.getKey()/r, mnVector.getValue()/r);
                    positions.get(vertices.get(m))[0] -= f*unitMN.getKey();
                    positions.get(vertices.get(m))[1] -= f*unitMN.getValue();
                    positions.get(vertices.get(n))[0] += f*unitMN.getKey();
                    positions.get(vertices.get(n))[1] += f*unitMN.getValue();
                    //kazda dvojica je priblizena, ale iba linearne, pre vsetky dvojice ide o rovnaku vzdialenost
                    positions.get(vertices.get(m))[0] += g*unitMN.getKey();
                    positions.get(vertices.get(m))[1] += g*unitMN.getValue();
                    positions.get(vertices.get(n))[0] -= g*unitMN.getKey();
                    positions.get(vertices.get(n))[1] -= g*unitMN.getValue();
                }
            }

            //zabezpecit, aby to stale bolo vnutri
            positions.forEach(((vertex, position) -> {
                position[0] = Math.max(0.0, position[0]);
                position[0] = Math.min(1.0, position[0]);
                position[1] = Math.max(0.0, position[1]);
                position[1] = Math.min(1.0, position[1]);
            }));

            //kazdy dvojicu susednych vrcholov priblizit pomocou hooka
            for(Edge edge:this.edges){
                Pair<Double, Double> vector = new Pair<>(
                        positions.get(edge.getVertexTo())[0] - positions.get(edge.getVertexFrom())[0],
                        positions.get(edge.getVertexTo())[1] - positions.get(edge.getVertexFrom())[1]);
                double r = AuxiliaryMath.vectorSize(vector);
                double f = k * r * edge.getOpacity() * edge.getOpacity();//Opaque edges have smaller force
                positions.get(edge.getVertexFrom())[0] += f * vector.getKey();
                positions.get(edge.getVertexFrom())[1] += f * vector.getValue();
                positions.get(edge.getVertexTo())[0] -= f * vector.getKey();
                positions.get(edge.getVertexTo())[1] -= f * vector.getValue();
            }

            //zabezpecit, aby to stale bolo vnutri
            positions.forEach(((vertex, position) -> {
                position[0] = Math.max(0.0, position[0]);
                position[0] = Math.min(1.0, position[0]);
                position[1] = Math.max(0.0, position[1]);
                position[1] = Math.min(1.0, position[1]);
            }));
        }
        positions.forEach(((vertex, position) ->
                animation.addAnimatable(AnimationType.RelativeMoveAnimation, vertex, position[0], position[1])));
    }

    @Override
    public void resize() {
        this.vertices.forEach(Vertex::redraw);
    }

    public ArrayList<Edge> getEdges(){
        return this.edges;
    }

    public double getVertexRadius(){
        return this.vertexRadius;
    }

    public void moveVertexToInvisible(Vertex vertex){
        if(this.visibleVertices.contains(vertex)){
            this.invisibleVertices.add(vertex);
            this.visibleVertices.remove(vertex);
        }
    }

    public void moveVertexToVisible(Vertex vertex){
        if(this.invisibleVertices.contains(vertex)){
            this.visibleVertices.add(vertex);
            this.invisibleVertices.remove(vertex);
        }
    }

    public void addEdge(Edge edge){
        this.edges.add(edge);
    }

    public void addVertex(Vertex vertex){
        this.vertices.add(vertex);
        this.visibleVertices.add(vertex);
    }

    public Vertex addVertex(){
        Vertex vertex = this.graphNodesFactory.newVertex();
        this.vertices.add(vertex);
        this.visibleVertices.add(vertex);
        return vertex;
    }

    public Edge addEdge(Vertex from, Vertex to){
        Edge edge = this.graphNodesFactory.newEdge(from, to);
        this.edges.add(edge);
        return edge;
    }

    public void removeEdge(Edge edge){
        if(this.edges.contains(edge)) {
            edge.delete();
            this.edges.remove(edge);
        }
    }

    public void removeVertex(Vertex vertex){
        if(this.vertices.contains(vertex)) {
            vertex.getEdges().forEach(this::removeEdge);
            vertex.delete();
            this.vertices.remove(vertex);
        }
    }

    public void clear(){
        this.edges.forEach(this::removeEdge);
        this.vertices.forEach(this::removeVertex);
    }

    public void setBeautifyDisable(boolean disable){
        super.setDisable("beautify", disable);
    }

    public void setCenterDisable(boolean disable){
        super.setDisable("centre", disable);
    }

    private final HashSet<Vertex> highlightedVertices = new HashSet<>();
    private final HashSet<Edge> highlightedEdges = new HashSet<>();

    public void highlightVertex(Vertex vertex){
        vertex.highlight();
        this.highlightedVertices.add(vertex);
    }

    public void unhighlightEverything(){
        this.highlightedVertices.forEach(Vertex::unhighlight);
        this.highlightedVertices.clear();
    }

}
