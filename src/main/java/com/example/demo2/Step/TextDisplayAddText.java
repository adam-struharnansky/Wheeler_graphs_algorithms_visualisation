package com.example.demo2.Step;

import com.example.demo2.algorithmDisplays.TextDisplay;

import java.util.ArrayList;

public record TextDisplayAddText(TextDisplay textDisplay,
                                 ArrayList<String> keys,
                                 ArrayList<String> styles,
                                 ArrayList<Boolean> translatables ) implements DiscreteChange{

    @Override
    public void backStep() {
        /*
        misuse of Interface.
        It is needed to actualize textDisplay every step, or else it will be emptied
         */
    }

    @Override
    public void forwardStep() {
        for(int i = 0;i<this.keys.size();i++){
            this.textDisplay.addString(this.keys.get(i), this.styles.get(i), this.translatables.get(i));
        }
    }
}
