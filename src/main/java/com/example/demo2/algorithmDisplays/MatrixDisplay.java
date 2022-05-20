package com.example.demo2.algorithmDisplays;

import com.example.demo2.algorithmDisplays.animatableNodes.AnimatableText;
import com.example.demo2.algorithmDisplays.animatableNodes.AnimatableTextFlow;
import com.example.demo2.algorithmDisplays.animatableNodes.MatrixArrow;
import com.example.demo2.animations.Animation;
import com.example.demo2.animations.AnimationType;
import com.example.demo2.auxiliary.Colors;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.*;

public class MatrixDisplay extends Display{

    private static final double textSize = 12.0;
    private static final double arrowMargin = 3.0;
    private static final double indexConstant = 0.6;
    private double size = 1.0;

    private final Pane pane;
    private int numberOfColumns = 0, numberOfRows = 0;
    private final ArrayList<ArrayList<AnimatableTextFlow>> textFlowMatrix;
    private final ArrayList<ArrayList<AnimatableText>> textMatrix;
    private final ArrayList<ArrayList<AnimatableText>> indexMatrix;
    private final ArrayList<MatrixArrow> arrows;
    private final HashSet<Pair<Integer, Integer>> highlightedSquares;
    private final HashSet<Pair<Integer, Integer>> highlightedBackgrounds;
    private final HashSet<MatrixArrow> highlightedMatrixArrows;

    public MatrixDisplay(VBox container, String name, int ratio) {
        super(container, name, ratio);
        this.pane = super.getPane();
        this.arrows = new ArrayList<>();
        this.textFlowMatrix = new ArrayList<>();
        this.textMatrix = new ArrayList<>();
        this.indexMatrix = new ArrayList<>();
        this.highlightedSquares = new HashSet<>();
        this.highlightedBackgrounds = new HashSet<>();
        this.highlightedMatrixArrows = new HashSet<>();
    }

    @Override
    public void centre(){

        //todo - zmenit ako su sipky v arrow, ako tam funguju tie medzery - aby to bolo v strede, kedze gapy su rozdielne
        //todo - nejak zmena velkosti

        ArrayList<Double> longestWordsPrefixSum = new ArrayList<>(List.of(0.0));

        for(int i = 0;i<this.numberOfColumns;i++){
            double longestWord = 0.0;
            for(int j = 0;j<this.numberOfRows;j++){
                textFlowMatrix.get(j).get(i).autosize();
                textMatrix.get(j).get(i).autosize();
                longestWord = Math.max(longestWord, textFlowMatrix.get(j).get(i).getLayoutBounds().getWidth());
            }
            longestWordsPrefixSum.add(longestWordsPrefixSum.get(i) + longestWord);
        }
        double horizontalGap = Math.max(0.0, (super.getWidth() - 0.0
                - longestWordsPrefixSum.get(longestWordsPrefixSum.size() - 1))/(this.numberOfColumns + 1));
        double verticalGap = Math.max(0.0, (super.getHeight() - 0.0)/(this.numberOfRows + 1));

        for(int i = 0;i<this.numberOfRows;i++){
            for(int j = 0;j<this.numberOfColumns;j++){
                this.textFlowMatrix.get(i).get(j).setLayoutX(horizontalGap + longestWordsPrefixSum.get(j) + j*horizontalGap);
                this.textFlowMatrix.get(i).get(j).setLayoutY(verticalGap + i*verticalGap);
            }
        }
        this.arrows.forEach(MatrixArrow::changeSize);
    }

    @Override
    public void resize(){
        centre();
    }

    public void setMatrixSize(int numberOfRows, int numberOfColumns){
        this.arrows.forEach(MatrixArrow::delete);
        this.arrows.clear();
        this.highlightedBackgrounds.clear();
        this.highlightedMatrixArrows.clear();
        this.highlightedSquares.clear();
        this.numberOfColumns = numberOfColumns;
        this.numberOfRows = numberOfRows;

        this.pane.getChildren().clear();
        this.textFlowMatrix.clear();
        this.textMatrix.clear();
        this.indexMatrix.clear();

        for(int i = 0;i<this.numberOfRows;i++){
            ArrayList<AnimatableTextFlow> rowOfTextFlows = new ArrayList<>(this.numberOfColumns);
            ArrayList<AnimatableText> rowOfTexts = new ArrayList<>();
            ArrayList<AnimatableText> rowOfIndexes = new ArrayList<>();
            for(int j = 0; j < this.numberOfColumns; j++){
                AnimatableText text = new AnimatableText(this.pane);
                text.setStyle("-fx-font-size: "+(textSize*this.size)+";");
                AnimatableText index = new AnimatableText(this.pane);
                index.setStyle("-fx-font-size: "+(indexConstant*textSize*this.size)+";");
                AnimatableTextFlow textFlow = new AnimatableTextFlow();
                textFlow.getChildren().add(text);
                textFlow.getChildren().add(index);
                rowOfTextFlows.add(textFlow);
                rowOfTexts.add(text);
                rowOfIndexes.add(index);
                textFlow.autosize();
                this.pane.getChildren().add(textFlow);
            }
            this.textFlowMatrix.add(rowOfTextFlows);
            this.textMatrix.add(rowOfTexts);
            this.indexMatrix.add(rowOfIndexes);
        }
        centre();

    }

    public void highlightBackground(int rowNumber, int columnNumber){
        if(isInMatrix(rowNumber, columnNumber)) {
            //todo - nastavit rovnaku farbu ako je pri texte
            this.textFlowMatrix.get(rowNumber).get(columnNumber).setBackground(
                    new Background(new BackgroundFill(Colors.highlightingColor, CornerRadii.EMPTY, Insets.EMPTY)));
            this.highlightedBackgrounds.add(new Pair<>(rowNumber, columnNumber));
        }
    }

    public void highlightBackgroundsRow(int rowNumber){
        if(isRowInMatrix(rowNumber)){
            for(int c = 0; c < this.numberOfColumns; c++){
                highlightBackground(rowNumber, c);
            }
        }
    }

    public void highlightBackgroundsColumn(int columnNumber){
        if(isColumnInMatrix(columnNumber)){
            for(int r = 0; r < this.numberOfRows; r++){
                highlightBackground(r, columnNumber);
            }
        }
    }

    public void unhighlightBackground(int rowNumber, int columnNumber){
        if(isInMatrix(rowNumber, columnNumber)){
            this.textFlowMatrix.get(rowNumber).get(columnNumber).setBackground(
                    new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
            this.highlightedBackgrounds.remove(new Pair<>(rowNumber, columnNumber));
        }
    }

    public void unhighlightBackgroundsRow(int rowNumber){
        if(isRowInMatrix(rowNumber)){
            for(int c = 0; c < this.numberOfColumns; c++){
                unhighlightBackground(rowNumber, c);
            }
        }
    }

    public void unhighlightBackgroundsColumn(int columnNumber){
        if(isColumnInMatrix(columnNumber)){
            for(int r = 0; r < this.numberOfRows; r++){
                unhighlightBackground(r, columnNumber);
            }
        }
    }

    public void highlightSquare(int rowNumber, int columnNumber){
        if(isInMatrix(rowNumber, columnNumber)) {
            this.textFlowMatrix.get(rowNumber).get(columnNumber).setStyle("-fx-font-weight: bold");
            this.highlightedSquares.add(new Pair<>(rowNumber, columnNumber));
        }
    }

    public void highlightRow(int rowNumber){
        if(isRowInMatrix(rowNumber)){
            for(int c = 0; c < this.numberOfColumns; c++){
                highlightSquare(rowNumber, c);
            }
        }
    }

    public void highlightColumn(int columnNumber){
        if(isColumnInMatrix(columnNumber)){
            for(int r = 0; r < this.numberOfRows; r++){
                highlightSquare(r, columnNumber);
            }
        }
    }

    public void unhighlightSquare(int rowNumber, int columnNumber){
        if(this.highlightedSquares.contains(new Pair<>(rowNumber, columnNumber))) {
            this.textFlowMatrix.get(rowNumber).get(columnNumber).setStyle("-fx-font-weight: normal");
            this.highlightedSquares.remove(new Pair<>(rowNumber, columnNumber));
        }
    }

    public void unhighlightRow(int rowNumber){
        if(isRowInMatrix(rowNumber)){
            for(int c = 0; c < this.numberOfColumns; c++){
                unhighlightSquare(rowNumber, c);
            }
        }
    }

    public void unhighlightColumn(int columnNumber){
        if(isColumnInMatrix(columnNumber)){
            for(int r = 0; r < this.numberOfRows; r++){
                unhighlightSquare(r, columnNumber);
            }
        }
    }

    public void setSquareTextColor(int rowNumber, int columnNumber, Color color){
        if(isInMatrix(rowNumber, columnNumber)) {
            this.textMatrix.get(rowNumber).get(columnNumber).setFill(color);
            this.indexMatrix.get(rowNumber).get(columnNumber).setFill(color);
        }
    }

    public void setSquareText(int rowNumber, int columnNumber, String text){
        if(isInMatrix(rowNumber, columnNumber)) {
            this.textFlowMatrix.get(rowNumber).get(columnNumber).autosize();
            this.textMatrix.get(rowNumber).get(columnNumber).setText(text);
            centre();
        }
    }

    public void setSquareText(int rowNumber, int columnNumber, int text){
        setSquareText(rowNumber, columnNumber, text+"");
    }

    public void setSquareText(int rowNumber, int columnNumber, char c){
        setSquareText(rowNumber, columnNumber, c+"");
    }

    public void setRowText(int rowNumber, String ... rowText){
        if(isRowInMatrix(rowNumber)) {
            int column = 0;
            for (Iterator<String> iterator = Arrays.stream(rowText).iterator(); iterator.hasNext(); ) {
                setSquareText(rowNumber, column, iterator.next());
                column++;
            }
        }
    }

    public void setColumnText(int columnNumber, String ... rowText){
        if(isColumnInMatrix(columnNumber)) {
            int row = 0;
            for (Iterator<String> iterator = Arrays.stream(rowText).iterator(); iterator.hasNext(); ) {
                setSquareText(row, columnNumber, iterator.next());
                row++;
            }
        }
    }

    public void setSquareIndex(int rowNumber, int columnNumber, String index){
        if(isInMatrix(rowNumber, columnNumber)) {
            this.indexMatrix.get(rowNumber).get(columnNumber).setText(index);
        }
    }

    public void setSquareIndex(int rowNumber, int columnNumber, int text){
        setSquareIndex(rowNumber, columnNumber, text+"");
    }

    public void setSquareIndex(int rowNumber, int columnNumber, char c){
        setSquareIndex(rowNumber, columnNumber, c+"");
    }

    private MatrixArrow storedMatrixArrow(int startRow, int startColumn, int endRow, int endColumn){
        if(isInMatrix(startRow, startColumn) && isInMatrix(endRow, endColumn)){
            for(MatrixArrow arrow:this.arrows){
                if(arrow.hasCoordinates(startRow, startColumn, endRow, endColumn)){
                    return arrow;
                }
            }
        }
        return null;
    }

    public void addMatrixArrow(int startRow, int startColumn, int endRow, int endColumn){
        if(storedMatrixArrow(startRow, startColumn, endRow, endColumn) == null){
            this.arrows.add(new MatrixArrow(this.pane, this.textFlowMatrix, this.size, arrowMargin,
                    startRow, startColumn, endRow, endColumn));
        }
    }

    public void removeMatrixArrow(int startRow, int startColumn, int endRow, int endColumn){
        MatrixArrow arrow = storedMatrixArrow(startRow, startColumn, endRow, endColumn);
        if(arrow != null){
            arrow.delete();
            this.arrows.remove(arrow);
        }
    }

    public void removeMatrixArrow(Animation animation, int startRow, int startColumn, int endRow, int endColumn){
        MatrixArrow arrow = storedMatrixArrow(startRow, startColumn, endRow, endColumn);
        if(arrow != null){
            animation.addAnimatable(AnimationType.DisappearAnimation, arrow);
        }
    }

    public void setMatrixArrowColor(int startRow, int startColumn, int endRow, int endColumn, Color color){
        MatrixArrow arrow = storedMatrixArrow(startRow, startColumn, endRow, endColumn);
        if(arrow != null){
            arrow.setColor(color);
        }
    }

    public void highlightMatrixArrow(int startRow, int startColumn, int endRow, int endColumn){
        MatrixArrow arrow = storedMatrixArrow(startRow, startColumn, endRow, endColumn);
        if(arrow != null){
            arrow.highlight();
            this.highlightedMatrixArrows.add(arrow);
        }
    }

    public void unhighlightMatrixArrow(int startRow, int startColumn, int endRow, int endColumn){
        MatrixArrow arrow = storedMatrixArrow(startRow, startColumn, endRow, endColumn);
        if(arrow != null){
            arrow.unhighlight();
            this.highlightedMatrixArrows.remove(arrow);
        }
    }

    public void clearMatrixArrows(){
        this.arrows.forEach(MatrixArrow::delete);
        this.arrows.clear();
    }

    public void clearMatrixTexts(){
        for(int i = 0;i<this.numberOfRows;i++){
            for(int j = 0;j<this.numberOfColumns;j++){
                this.textMatrix.get(i).get(j).setText("");
                this.indexMatrix.get(i).get(j).setText("");
            }
        }
    }

    public void clearMatrixArrows(Animation animation){
        //todo - treba pridat matrixArrowom dis/appear
        this.arrows.forEach(matrixArrow -> removeMatrixArrow(animation,
                matrixArrow.startCoordinates().getKey(), matrixArrow.startCoordinates().getValue(),
                matrixArrow.endCoordinates().getKey(), matrixArrow.endCoordinates().getValue()));
        this.arrows.clear();//?
    }

    public void unhighlightBackgrounds(){
        this.highlightedBackgrounds.forEach(pair -> this.textFlowMatrix.get(pair.getKey()).get(pair.getValue()).setBackground(
                new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY))));
        this.highlightedBackgrounds.clear();
    }

    public void unhighlightBackgrounds(Animation animation){
        this.highlightedBackgrounds.forEach(pair -> animation.addAnimatable(AnimationType.ColorAnimation,
                this.textFlowMatrix.get(pair.getKey()).get(pair.getValue()), Colors.highlightingColor, Color.TRANSPARENT));
        this.highlightedBackgrounds.clear();
    }

    public void unhighlightSquares(){
        this.highlightedSquares.forEach(pair -> this.textFlowMatrix.get(pair.getKey()).get(pair.getValue()).setStyle("-fx-font-weight: normal"));
        this.highlightedSquares.clear();
    }

    public void unhighlightSquares(Animation animation){
        //todo - zistit, ci sa toto da nejako, alebo nie
        this.highlightedSquares.forEach(pair ->
                this.textFlowMatrix.get(pair.getKey()).get(pair.getValue()).setStyle("-fx-font-weight: normal"));
        this.highlightedSquares.clear();
    }

    public void unhighlightMatrixArrows(){
        this.highlightedMatrixArrows.forEach(MatrixArrow::unhighlight);
        this.highlightedMatrixArrows.clear();
    }

    public void unhighlightMatrixArrows(Animation animation){
        //todo - treba toto este premysliet, ako to pojde, zatial ziadne sipky nezvyraznovat
        this.highlightedMatrixArrows.forEach(MatrixArrow::unhighlight);
        this.highlightedMatrixArrows.clear();
    }

    public void unhighlightEverything(){
        unhighlightBackgrounds();
        unhighlightMatrixArrows();
        unhighlightSquares();
    }

    public void unhighlightEverything(Animation animation){
        unhighlightBackgrounds(animation);
        unhighlightMatrixArrows(animation);
        unhighlightSquares(animation);
    }


    public void setSize(double size){
        this.size = size;
        centre();
    }

    /*-----------------------------------------------------------------------------------------------------------------*/

    public void highlightBackground(Animation animation, int rowNumber, int columnNumber){
        if(isInMatrix(rowNumber, columnNumber)) {
            animation.addAnimatable(AnimationType.ColorAnimation, this.textFlowMatrix.get(rowNumber).get(columnNumber),
                    Colors.highlightingColor);
            this.highlightedBackgrounds.add(new Pair<>(rowNumber, columnNumber));
        }
    }

    public void highlightBackgroundsRow(Animation animation, int rowNumber){
        if(isRowInMatrix(rowNumber)){
            for(int c = 0; c < this.numberOfColumns; c++){
                highlightBackground(animation, rowNumber, c);
            }
        }
    }

    public void highlightBackgroundsColumn(Animation animation, int columnNumber){
        if(isColumnInMatrix(columnNumber)){
            for(int r = 0; r < this.numberOfRows; r++){
                highlightBackground(animation, r, columnNumber);
            }
        }
    }

    public void unhighlightBackground(Animation animation, int rowNumber, int columnNumber){
        if(isInMatrix(rowNumber, columnNumber)){
            animation.addAnimatable(AnimationType.ColorAnimation, this.textFlowMatrix.get(rowNumber).get(columnNumber),
                    Color.TRANSPARENT);
            this.highlightedBackgrounds.remove(new Pair<>(rowNumber, columnNumber));
        }
    }

    public void unhighlightBackgroundsRow(Animation animation, int rowNumber){
        if(isRowInMatrix(rowNumber)){
            for(int c = 0; c < this.numberOfColumns; c++){
                unhighlightBackground(animation, rowNumber, c);
            }
        }
    }

    public void unhighlightBackgroundsColumn(Animation animation, int columnNumber){
        if(isColumnInMatrix(columnNumber)){
            for(int r = 0; r < this.numberOfRows; r++){
                unhighlightBackground(animation, r, columnNumber);
            }
        }
    }

    public void setSquareTextColor(Animation animation, int rowNumber, int columnNumber, Color color){
        if(isInMatrix(rowNumber, columnNumber)) {
            animation.addAnimatable(AnimationType.ColorAnimation, this.textFlowMatrix.get(rowNumber).get(columnNumber), color);
        }
    }

    public void addMatrixArrow(Animation animation, int startRow, int startColumn, int endRow, int endColumn){
        addMatrixArrow(animation, startRow, startColumn, endRow, endColumn, Color.BLACK);
    }

    public void addMatrixArrow(Animation animation, int startRow, int startColumn, int endRow, int endColumn, Color color){
        if(storedMatrixArrow(startRow, startColumn, endRow, endColumn) == null){
            MatrixArrow arrow = new MatrixArrow(this.pane, this.textFlowMatrix, this.size, arrowMargin,
                    startRow, startColumn, endRow, endColumn);
            this.arrows.add(arrow);
            animation.addAnimatable(AnimationType.AppearAnimation, arrow);
        }
    }


    public void setMatrixArrowColor(Animation animation, int startRow, int startColumn, int endRow, int endColumn, Color color){
        MatrixArrow arrow = storedMatrixArrow(startRow, startColumn, endRow, endColumn);
        if(arrow != null){
            animation.addAnimatable(AnimationType.ColorAnimation, arrow, color);
        }
    }

    private boolean isInMatrix(int rowNumber, int columnNumber){
        return (isColumnInMatrix(columnNumber) && isRowInMatrix(rowNumber));
    }

    private boolean isColumnInMatrix(int columnNumber){
        return (columnNumber >= 0 && columnNumber < this.numberOfColumns);
    }

    private boolean isRowInMatrix(int rowNumber){
        return (rowNumber >= 0 && rowNumber < this.numberOfRows);
    }

    public MatrixMemento saveToMemento(){
        return new MatrixMemento(this);
    }

    public void restoreFromMemento(MatrixMemento memento){
        if(memento.numberOfColumns != this.numberOfColumns || memento.numberOfRows != this.numberOfRows){
            setMatrixSize(memento.numberOfRows, memento.numberOfColumns);
        }
        for(int row = 0;row<this.numberOfRows;row++){
            for(int column = 0;column<this.numberOfColumns;column++){
                this.textMatrix.get(row).get(column).setText(memento.matrixStrings.get(row).get(column));
                this.indexMatrix.get(row).get(column).setText(memento.matrixIndexes.get(row).get(column));
                this.textFlowMatrix.get(row).get(column).autosize();
            }
        }
        centre();

        for(Iterator<MatrixArrow> i = this.arrows.iterator();i.hasNext();){
            MatrixArrow arrow = i.next();
            boolean contains = false;
            for(Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> a: memento.arrows){
                if(storedMatrixArrow(a.getKey().getKey(), a.getKey().getValue(), a.getValue().getKey(), a.getValue().getValue())
                        != null){
                    contains = true;
                    break;
                }
            }
            if(!contains){
                arrow.delete();
                i.remove();
            }
        }
        for(Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> a: memento.arrows){
            addMatrixArrow(a.getKey().getKey(), a.getKey().getValue(), a.getValue().getKey(), a.getValue().getValue());
        }

        //prejst cez arrows, a najskor pridat tie, ktore tu nie su
        //potom odstranit vsetky, ktore nie su v memente
        //todo naprogramovat navrat
        //ked robime krok spat, chceme, aby sa vsetky
    }

    public static class MatrixMemento {
        private final int numberOfColumns, numberOfRows;
        private final ArrayList<ArrayList<String>> matrixStrings;
        private final ArrayList<ArrayList<String>> matrixIndexes;
        private final ArrayList<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> arrows;
        private final HashSet<Pair<Integer, Integer>> highlightedSquares;
        private final HashSet<Pair<Integer, Integer>> highlightedBackgrounds;
        private final HashSet<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> highlightedMatrixArrows;

        public MatrixMemento(MatrixDisplay matrixDisplay){
            this.numberOfColumns = matrixDisplay.numberOfColumns;
            this.numberOfRows = matrixDisplay.numberOfRows;

            this.matrixStrings = new ArrayList<>();
            for(int i = 0;i<this.numberOfRows;i++){
                ArrayList<String> row = new ArrayList<>();
                for(int j = 0;j<this.numberOfColumns;j++){
                    row.add(matrixDisplay.textMatrix.get(i).get(j).getText());
                }
                this.matrixStrings.add(row);
            }

            this.matrixIndexes = new ArrayList<>();
            for(int i = 0;i<this.numberOfRows;i++){
                ArrayList<String> row = new ArrayList<>();
                for(int j = 0;j<this.numberOfColumns;j++){
                    row.add(matrixDisplay.indexMatrix.get(i).get(j).getText());
                }
                this.matrixIndexes.add(row);
            }

            this.arrows = new ArrayList<>();
            for(MatrixArrow arrow:matrixDisplay.arrows){
                this.arrows.add(new Pair<>(arrow.startCoordinates(), arrow.endCoordinates()));
            }

            this.highlightedSquares = new HashSet<>(matrixDisplay.highlightedSquares);
            this.highlightedBackgrounds = new HashSet<>(matrixDisplay.highlightedBackgrounds);

            this.highlightedMatrixArrows = new HashSet<>();
            for(MatrixArrow arrow:matrixDisplay.highlightedMatrixArrows){
                this.highlightedMatrixArrows.add(new Pair<>(arrow.startCoordinates(), arrow.endCoordinates()));
            }
        }
    }
}

