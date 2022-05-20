package com.example.demo2.Step;

import com.example.demo2.algorithmDisplays.CodeDisplay;
import com.example.demo2.algorithmDisplays.MatrixDisplay;
import com.example.demo2.algorithmDisplays.TextDisplay;
import com.example.demo2.algorithmDisplays.animatableNodes.Edge;
import com.example.demo2.algorithmDisplays.animatableNodes.Vertex;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class StepManager {

    int stepsNumber = 0;// = setVariables.size() = setSquares.size() = ...

    private final ArrayList<ArrayList<SetVariableValue>> setVariableValues = new ArrayList<>();
    private final ArrayList<ArrayList<SetSquareText>> setSquareTexts = new ArrayList<>();
    private final ArrayList<ArrayList<SetSquareIndex>> setSquareIndexes = new ArrayList<>();
    private final ArrayList<ArrayList<TextDisplayAddText>> textDisplayTexts = new ArrayList<>();
    private final ArrayList<ArrayList<SetHighlightSquare>> setHighlightsSquares = new ArrayList<>();
    private final ArrayList<ArrayList<AddWay>> addWays = new ArrayList<>();
    private final ArrayList<ArrayList<SetHighlightVertex>> setHighlightVertices = new ArrayList<>();

    public void backStep(int step){
        if(step < 0 || step >= this.stepsNumber){
            return;
        }
        this.setVariableValues.get(step).forEach(SetVariableValue::backStep);
        this.setSquareTexts.get(step).forEach(SetSquareText::backStep);
        this.setSquareIndexes.get(step).forEach(SetSquareIndex::backStep);
        if(step >= 1) {//todo - maybe implement similarly with all types of DiscreteChanges
            this.textDisplayTexts.get(step - 1).forEach(TextDisplayAddText::forwardStep);
        }
        this.setHighlightsSquares.get(step).forEach(SetHighlightSquare::backStep);
        this.addWays.get(step).forEach(AddWay::backStep);
        this.setHighlightVertices.get(step).forEach(SetHighlightVertex::backStep);
    }

    public void forwardStep(int step){
        if(step < 0 || step >= this.stepsNumber){
            return;
        }
        this.setVariableValues.get(step).forEach(SetVariableValue::forwardStep);
        this.setSquareTexts.get(step).forEach(SetSquareText::forwardStep);
        this.setSquareIndexes.get(step).forEach(SetSquareIndex::forwardStep);
        this.textDisplayTexts.get(step).forEach(TextDisplayAddText::forwardStep);
        this.setHighlightsSquares.get(step).forEach(SetHighlightSquare::forwardStep);
        this.addWays.get(step).forEach(AddWay::forwardStep);
        this.setHighlightVertices.get(step).forEach(SetHighlightVertex::forwardStep);
    }

    private void fulfilToValue(int step){
        if(step < this.stepsNumber){
            return;
        }
        while(step >= this.setSquareTexts.size()){
            this.setSquareTexts.add(new ArrayList<>());
        }
        while(step >= this.setVariableValues.size()){
            this.setVariableValues.add(new ArrayList<>());
        }
        while(step >= this.setSquareIndexes.size()){
            this.setSquareIndexes.add(new ArrayList<>());
        }
        while(step >= this.textDisplayTexts.size()){
            this.textDisplayTexts.add(new ArrayList<>());
        }
        while(step >= this.setHighlightsSquares.size()){
            this.setHighlightsSquares.add(new ArrayList<>());
        }
        while(step >= this.addWays.size()){
            this.addWays.add(new ArrayList<>());
        }
        while(step >= this.setHighlightVertices.size()){
            this.setHighlightVertices.add(new ArrayList<>());
        }
        this.stepsNumber = step + 1;
    }

    public void addSetVariableValue(int step, CodeDisplay codeDisplay, String name, String valueBefore,
                                    String valueAfter){
        fulfilToValue(step);
        this.setVariableValues.get(step).add(new SetVariableValue(codeDisplay, name, valueBefore, valueAfter));
    }

    public void addSetVariableValue(int step, CodeDisplay codeDisplay, String name, int valueBefore, int valueAfter){
        addSetVariableValue(step, codeDisplay, name, valueBefore + "", valueAfter+"");
    }

    public void addSetSquareText(int step, MatrixDisplay matrixDisplay, int row, int column, String valueBefore,
                                  String valueAfter){
        fulfilToValue(step);
        this.setSquareTexts.get(step).add(new SetSquareText(matrixDisplay, row, column, valueBefore, valueAfter));
    }

    public void addSetSquareIndex(int step, MatrixDisplay matrixDisplay, int row, int column, String valueBefore,
                                  String valueAfter){
        fulfilToValue(step);
        this.setSquareIndexes.get(step).add(new SetSquareIndex(matrixDisplay, row, column, valueBefore, valueAfter));
    }


    public void addTextDisplayText(int step, TextDisplay textDisplay, String key, String style, boolean translatable){
        fulfilToValue(step);
        this.textDisplayTexts.get(step).add(new TextDisplayAddText(textDisplay,
                new ArrayList<>(List.of(key)),
                new ArrayList<>(List.of(style)),
                new ArrayList<>(List.of(translatable))));
    }

    public void addSetHighlightSquare(int step, MatrixDisplay matrixDisplay, int row, int column, boolean highlight){
        fulfilToValue(step);
        this.setHighlightsSquares.get(step).add(new SetHighlightSquare(matrixDisplay, row, column, highlight));
    }

    public void addWay(int step, Edge edge, int id, Color color){
        fulfilToValue(step);
        this.addWays.get(step).add(new AddWay(edge, id, color));
    }

    public void addSetHighlightVertex(int step, Vertex vertex, boolean highlight){
        fulfilToValue(step);
        this.setHighlightVertices.get(step).add(new SetHighlightVertex(vertex, highlight));
    }
}
