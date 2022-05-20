package com.example.demo2.Step;

import com.example.demo2.algorithmDisplays.animatableNodes.Vertex;

public record SetHighlightVertex (Vertex vertex, boolean highlight) implements DiscreteChange{


    @Override
    public void backStep() {
        if(this.highlight){
            this.vertex.unhighlight();
        }
        else{
            this.vertex.highlight();
        }
    }

    @Override
    public void forwardStep() {
        if(this.highlight){
            this.vertex.highlight();
        }
        else{
            this.vertex.unhighlight();
        }
    }
}
