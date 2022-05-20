package com.example.demo2.Step;

import com.example.demo2.algorithmDisplays.MatrixDisplay;

public record SetSquareText(MatrixDisplay matrixDisplay, int row, int column,
                            String valueBefore, String valueAfter) implements DiscreteChange{


    @Override
    public void backStep() {
        this.matrixDisplay.setSquareText(this.row, this.column, this.valueBefore);
    }

    @Override
    public void forwardStep() {
        this.matrixDisplay.setSquareText(this.row, this.column, this.valueAfter);
    }
}
