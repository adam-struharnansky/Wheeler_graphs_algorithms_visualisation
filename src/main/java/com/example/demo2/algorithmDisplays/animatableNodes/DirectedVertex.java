package com.example.demo2.algorithmDisplays.animatableNodes;

import com.example.demo2.algorithmDisplays.GraphDisplay;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class DirectedVertex extends Vertex {

    private final ArrayList<Edge> incoming;
    private final ArrayList<Edge> outgoing;

    DirectedVertex(GraphDisplay graphDisplay, Pane graphPane){
        super(graphDisplay, graphPane);
        this.incoming = new ArrayList<>();
        this.outgoing = new ArrayList<>();
    }

    @Override
    public void addIncomingEdge(Edge edge){
        super.addIncomingEdge(edge);
        if (!this.incoming.contains(edge)) {
            this.incoming.add(edge);
        }
    }

    @Override
    public void addOutgoingEdge(Edge edge){
        super.addOutgoingEdge(edge);
        if(!this.outgoing.contains(edge)){
            this.outgoing.add(edge);
        }
    }

    @Override
    public void removeEdge(Edge edge){
        super.removeEdge(edge);
        this.outgoing.remove(edge);
        this.incoming.remove(edge);
    }

    public ArrayList<Edge> getOutgoing(){
        return this.outgoing;
    }

    public ArrayList<Edge> getIncoming(){
        return this.incoming;
    }
}
