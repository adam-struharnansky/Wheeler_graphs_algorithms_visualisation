package com.example.demo2.algorithmDisplays.animatableNodes;

import com.example.demo2.algorithmDisplays.GraphDisplay;
import javafx.scene.layout.Pane;

public class GraphNodesFactory {

    private final GraphDisplay graphDisplay;
    private final Pane graphPane;
    private boolean directed = true;


    public GraphNodesFactory(GraphDisplay graphDisplay, Pane graphPane, boolean directed){
        this.graphDisplay = graphDisplay;
        this.graphPane = graphPane;
        this.directed = directed;
    }

    private Edge createEdge(Vertex from, Vertex to){
        Edge edge = new Edge(this.graphDisplay, this.graphPane, from, to);
        from.addOutgoingEdge(edge);
        to.addIncomingEdge(edge);
        edge.redraw();
        return edge;
    }

    private DirectedEdge createDirectedEdge(Vertex from, Vertex to){
        DirectedEdge edge = new DirectedEdge(this.graphDisplay, this.graphPane, from, to);
        from.addOutgoingEdge(edge);
        to.addIncomingEdge(edge);
        edge.redraw();
        return edge;
    }

    public Edge newEdge(Vertex from, Vertex to){
        if(this.directed){
            return createDirectedEdge(from, to);
        }
        else{
            return createEdge(from, to);
        }
    }

    public Edge newEdge(boolean directed, Vertex from, Vertex to){
        if(directed){
            return createDirectedEdge(from, to);
        }
        else{
            return createEdge(from, to);
        }
    }

    public Vertex newVertex(){
        if(this.directed){
            return new DirectedVertex(this.graphDisplay, this.graphPane);
        }
        else{
            return new Vertex(this.graphDisplay, this.graphPane);
        }
    }

    public Vertex newVertex(boolean directed){
        if(directed){
            return new DirectedVertex(this.graphDisplay, this.graphPane);
        }
        else{
            return new Vertex(this.graphDisplay, this.graphPane);
        }
    }
}
