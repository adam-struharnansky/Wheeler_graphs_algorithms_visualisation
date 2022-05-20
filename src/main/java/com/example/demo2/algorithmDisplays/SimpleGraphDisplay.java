package com.example.demo2.algorithmDisplays;

import com.example.demo2.algorithmDisplays.animatableNodes.SimpleEdge;
import com.example.demo2.algorithmDisplays.animatableNodes.SimpleGraphNodesFactory;
import com.example.demo2.algorithmDisplays.animatableNodes.SimpleVertex;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

public class SimpleGraphDisplay extends Display{

    private final SimpleGraphNodesFactory graphNodesFactory;
    private boolean directed;

    private final Pane pane;
    private final ArrayList<SimpleVertex> vertices = new ArrayList<>();
    private final ArrayList<SimpleVertex> invisibleVertices = new ArrayList<>();
    private final ArrayList<SimpleVertex> visibleVertices = new ArrayList<>();
    private final ArrayList<SimpleEdge> edges = new ArrayList<>();

    private double vertexRadius = 15.0;

    public SimpleGraphDisplay(VBox container, String name, int ratio, boolean directed) {
        super(container, name, ratio);
        this.pane = super.getPane();
        this.directed = directed;
        this.graphNodesFactory = new SimpleGraphNodesFactory(this, this.pane, this.directed);

        super.addButton("centre", this::centre);
    }

    @Override
    public void centre(){
        //todo - nastavit, aby sa zmenil zoom na pociatocny
        //todo - mozno aj niekde pouzit zoom
        double mostLeft = Double.POSITIVE_INFINITY, mostRight = Double.NEGATIVE_INFINITY,
                mostTop = Double.POSITIVE_INFINITY, mostDown = Double.NEGATIVE_INFINITY;
        for(SimpleVertex vertex:this.visibleVertices){
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
        for(SimpleVertex vertex:this.visibleVertices){
            vertex.setPosition(horizontalGap + newHorizontalSize*(vertex.getX() - mostLeft)/oldHorizontalSize,
                    verticalGap + newVerticalSize*(vertex.getY() - mostTop)/oldVerticalSize);
        }
    }


    @Override
    public void resize() {
        this.vertices.forEach(SimpleVertex::redraw);
    }

    public double getSimpleVertexRadius(){
        return this.vertexRadius;
    }

    public void moveSimpleVertexToInvisible(SimpleVertex vertex){
        if(this.visibleVertices.contains(vertex)){
            this.invisibleVertices.add(vertex);
            this.visibleVertices.remove(vertex);
        }
    }

    public void moveSimpleVertexToVisible(SimpleVertex vertex){
        if(this.invisibleVertices.contains(vertex)){
            this.visibleVertices.add(vertex);
            this.invisibleVertices.remove(vertex);
            //todo - treba mu dat niekde miesto asi
        }
    }

    public void addSimpleEdge(SimpleEdge edge){
        this.edges.add(edge);
    }

    public void addSimpleVertex(SimpleVertex vertex){
        this.vertices.add(vertex);
        this.visibleVertices.add(vertex);
    }

    public SimpleVertex addSimpleVertex(){
        SimpleVertex vertex = this.graphNodesFactory.newSimpleVertex();
        this.vertices.add(vertex);
        this.visibleVertices.add(vertex);
        return vertex;
    }

    public SimpleEdge addSimpleEdge(SimpleVertex from, SimpleVertex to){
        SimpleEdge edge = this.graphNodesFactory.newSimpleEdge(from, to);
        this.edges.add(edge);
        return edge;
    }

    public void removeSimpleEdge(SimpleEdge edge){
        if(this.edges.contains(edge)) {
            edge.delete();
            this.edges.remove(edge);
        }
    }

    public void removeSimpleVertex(SimpleVertex vertex){
        if(this.vertices.contains(vertex)) {
            vertex.getSimpleEdges().forEach(this::removeSimpleEdge);
            vertex.delete();
            this.vertices.remove(vertex);
        }
    }

    public void clear(){
        this.edges.forEach(this::removeSimpleEdge);
        this.vertices.forEach(this::removeSimpleVertex);
    }

    public void setCenterDisable(boolean disable){
        super.setDisable("centre", disable);
    }

    public void setSimpleEdgesColorByLabels(){
        //todo
    }
}
