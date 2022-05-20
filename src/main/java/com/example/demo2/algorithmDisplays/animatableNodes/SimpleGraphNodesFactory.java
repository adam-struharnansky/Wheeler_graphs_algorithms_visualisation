package com.example.demo2.algorithmDisplays.animatableNodes;

import com.example.demo2.algorithmDisplays.SimpleGraphDisplay;
import javafx.scene.layout.Pane;

public class SimpleGraphNodesFactory {

    private final SimpleGraphDisplay graphDisplay;
    private final Pane graphPane;
    private boolean directed = true;

    public SimpleGraphNodesFactory(SimpleGraphDisplay graphDisplay, Pane graphPane, boolean directed){
        this.graphDisplay = graphDisplay;
        this.graphPane = graphPane;
        this.directed = directed;
    }


    private SimpleEdge createSimpleEdge(SimpleVertex from, SimpleVertex to){
        SimpleEdge edge = new SimpleEdge(this.graphDisplay, this.graphPane, from, to);
        from.addOutgoingEdge(edge);
        to.addIncomingEdge(edge);
        edge.redraw();
        return edge;
    }

    private SimpleDirectedEdge newSimpleDirectedEdge(SimpleVertex from, SimpleVertex to){
        SimpleDirectedEdge edge = new SimpleDirectedEdge(this.graphDisplay, this.graphPane, from, to);
        from.addOutgoingEdge(edge);
        to.addIncomingEdge(edge);
        edge.redraw();
        return edge;
    }

    public SimpleEdge directedEdge(SimpleVertex from, SimpleVertex to){
        if(this.directed){
            return newSimpleDirectedEdge(from, to);
        }
        else{
            return createSimpleEdge(from, to);
        }
    }

    public SimpleEdge newSimpleEdge(SimpleVertex from, SimpleVertex to){
        if(directed){
            return newSimpleDirectedEdge(from, to);
        }
        else{
            return createSimpleEdge(from, to);
        }
    }

    public SimpleEdge newSimpleEdge(boolean directed, SimpleVertex from, SimpleVertex to){
        if(directed){
            return newSimpleDirectedEdge(from, to);
        }
        else{
            return createSimpleEdge(from, to);
        }
    }

   public SimpleVertex newSimpleVertex(){
        return new SimpleVertex(this.graphDisplay, this.graphPane);
    }

}
