package com.example.demo2.Step;

import com.example.demo2.algorithmDisplays.CodeDisplay;

public record SetVariableValue(CodeDisplay codeDisplay, String name,
                               String valueBefore, String valueAfter) implements DiscreteChange {

    @Override
    public void backStep() {
        this.codeDisplay.setVariableValue(this.name, this.valueBefore);
    }

    @Override
    public void forwardStep() {
        this.codeDisplay.setVariableValue(this.name, this.valueAfter);
    }
}