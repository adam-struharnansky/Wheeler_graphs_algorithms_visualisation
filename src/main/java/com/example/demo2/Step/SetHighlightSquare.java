package com.example.demo2.Step;

import com.example.demo2.algorithmDisplays.MatrixDisplay;

public record SetHighlightSquare(MatrixDisplay matrixDisplay, int row, int column,
                                 boolean highlight) implements DiscreteChange {
    @Override
    public void backStep() {
        if(this.highlight){
            this.matrixDisplay.unhighlightSquare(this.row, this.column);
        }
        else{
            this.matrixDisplay.highlightSquare(this.row, this.column);
        }
    }

    @Override
    public void forwardStep() {
        if(this.highlight){
            this.matrixDisplay.highlightSquare(this.row, this.column);
        }
        else{
            this.matrixDisplay.unhighlightSquare(this.row, this.column);
        }
    }
}
