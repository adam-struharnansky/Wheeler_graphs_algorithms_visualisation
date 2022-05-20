package com.example.demo2.algorithmDisplays;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.List;

public class Matrix extends Sector{

    private final double textSize = 12.0;
    private final double arrowMargin = 3.0;
    private final double indexConstant = 0.6;

    private final Pane pane;
    private final ArrayList<ArrayList<TextFlow>> textFlowMatrix;
    private final ArrayList<ArrayList<Text>> textMatrix;
    private final ArrayList<ArrayList<Text>> indexMatrix;
    private int numberOfColumns = 0, numberOfRows = 0;
    private final ArrayList<Arrow> arrows;

    private double size = 1.0;

    private class Arrow {

        private final CubicCurve curve;
        private final Polygon triangle;

        private final int startRow;
        private final int startColumn;
        private final int endRow;
        private final int endColumn;

        private boolean isHighlighted = false;
        private Arrow.Direction triangleDirection = Arrow.Direction.up;

        private Arrow(int startRow, int startColumn, int endRow, int endColumn){
            //todo vyriesit, ze niekedy to moze pretat text - ak su nerovnako dlhe texty v matici - a mozno aj nie...
            this.startColumn = startColumn;
            this.startRow = startRow;
            this.endColumn = endColumn;
            this.endRow = endRow;
            this.curve = new CubicCurve();
            this.curve.setFill(Color.TRANSPARENT);
            this.curve.setStroke(Color.BLACK);
            //todo - nastavit hrubku ciary, podla toho, aby to bolo co najlepsie. Mozno nechat aj tak, a default je ok
            double ts = 1.8;
            this.triangle = new Polygon(ts*size, 0.0, 0.0, 3*ts*size, 2*ts*size, 3*ts*size);
            pane.getChildren().add(this.curve);
            pane.getChildren().add(this.triangle);
            redraw();
        }

        private void delete(){
            pane.getChildren().remove(this.curve);
            pane.getChildren().remove(this.triangle);
        }

        private void setColor(Color color){
            this.triangle.setStroke(color);
            this.triangle.setFill(color);
            this.curve.setStroke(color);
        }

        void highlight(){
            this.curve.setStrokeWidth(size*1.8);
            this.triangle.setScaleX(size*1.5);
            this.triangle.setScaleY(size*1.5);
            this.isHighlighted = true;
            redrawTriangle(this.endRow, this.endColumn, this.triangleDirection);
        }

        void unhighlight(){
            this.curve.setStrokeWidth(size);
            this.triangle.setScaleX(size);
            this.triangle.setScaleY(size);
            this.isHighlighted = false;
            redrawTriangle(this.endRow, this.endColumn, this.triangleDirection);
        }

        void changeSize(){
            if(this.isHighlighted){
                highlight();
            }
            else{
                unhighlight();
            }
            redraw();
        }

        void redraw(){
            if(this.startColumn == this.endColumn && this.startRow == this.endRow){
                //todo
                //sipka do sameho seba
                System.out.println("sipka do seba");
            }
            else if(Math.abs(this.endColumn - this.startColumn) == 1){
                Arrow.Direction startDirection, endDirection;
                int directionChange;
                if(this.endColumn - this.startColumn == 1){
                    startDirection = Arrow.Direction.right;
                    endDirection = Arrow.Direction.left;
                    directionChange = 1;
                }
                else{
                    startDirection = Arrow.Direction.left;
                    endDirection = Arrow.Direction.right;
                    directionChange = -1;
                }
                this.curve.setStartX(textEdgeX(this.startRow, this.startColumn, startDirection)
                        + directionChange*arrowMargin*size);
                this.curve.setStartY(textEdgeY(this.startRow, this.startColumn, startDirection));

                this.curve.setControlX1(textEdgeX(this.startRow, this.startColumn, startDirection)
                        + directionChange*gap(this.startRow, this.startColumn, startDirection));
                this.curve.setControlY1(textEdgeY(this.startRow, this.startColumn, startDirection));
                this.curve.setControlX2(textEdgeX(this.endRow, this.endColumn, endDirection)
                        - directionChange*gap(this.endRow, this.endColumn, endDirection));
                this.curve.setControlY2(textEdgeY(this.endRow, this.endColumn, endDirection));

                this.curve.setEndX(textEdgeX(this.endRow, this.endColumn, endDirection)
                        - directionChange*arrowMargin*size);
                this.curve.setEndY(textEdgeY(this.endRow, this.endColumn, endDirection));
                redrawTriangle(this.endRow, this.endColumn, endDirection);
            }
            else if(Math.abs(this.endRow - this.startRow) == 1){
                Arrow.Direction startDirection, endDirection;
                int directionChange;
                if(this.endRow - this.startRow == 1){
                    startDirection = Arrow.Direction.down;
                    endDirection = Arrow.Direction.up;
                    directionChange = 1;
                }
                else{
                    startDirection = Arrow.Direction.up;
                    endDirection = Arrow.Direction.down;
                    directionChange = -1;
                }
                this.curve.setStartX(textEdgeX(this.startRow, this.startColumn, startDirection));
                this.curve.setStartY(textEdgeY(this.startRow, this.startColumn, startDirection)
                        + directionChange*arrowMargin*size);

                this.curve.setControlX1(textEdgeX(this.startRow, this.startColumn, startDirection));
                this.curve.setControlY1(textEdgeY(this.startRow, this.startColumn, startDirection)
                        + directionChange*gap(this.startRow, this.startColumn, startDirection)/2);
                this.curve.setControlX2(textEdgeX(this.endRow, this.endColumn, endDirection));
                this.curve.setControlY2(textEdgeY(this.endRow, this.endColumn, endDirection)
                        - directionChange*gap(this.endRow, this.endColumn, Arrow.Direction.up)/2);

                this.curve.setEndX(textEdgeX(this.endRow, this.endColumn, endDirection));
                this.curve.setEndY(textEdgeY(this.endRow, this.endColumn, endDirection)
                        - directionChange*arrowMargin*size);
                redrawTriangle(this.endRow, this.endColumn, endDirection);
            }
            else if(this.startColumn == this.endColumn){
                this.curve.setStartX(textEdgeX(this.startRow, this.startColumn, Arrow.Direction.right) + arrowMargin*size);
                this.curve.setStartY(textEdgeY(this.startRow, this.startColumn, Arrow.Direction.right));

                this.curve.setControlX1(textEdgeX(this.startRow, this.startColumn, Arrow.Direction.right)
                        + gap(this.startRow, this.startColumn, Arrow.Direction.right));
                this.curve.setControlY1(textEdgeY(this.startRow, this.startColumn, Arrow.Direction.right));
                this.curve.setControlX2(textEdgeX(this.endRow, this.endColumn, Arrow.Direction.right)
                        + gap(this.endRow, this.endColumn, Arrow.Direction.right));
                this.curve.setControlY2(textEdgeY(this.endRow, this.endColumn, Arrow.Direction.right));

                this.curve.setEndX(textEdgeX(this.endRow, this.endColumn, Arrow.Direction.right) + arrowMargin*size);
                this.curve.setEndY(textEdgeY(this.endRow, this.endColumn, Arrow.Direction.right));
                redrawTriangle(this.endRow, this.endColumn, Arrow.Direction.right);
            }
            else if(this.startRow == this.endRow){
                this.curve.setStartX(textEdgeX(this.startRow, this.startColumn, Arrow.Direction.up));
                this.curve.setStartY(textEdgeY(this.startRow, this.startColumn, Arrow.Direction.up) + arrowMargin*size);

                this.curve.setControlX1(textEdgeX(this.startRow, this.startColumn, Arrow.Direction.up));
                this.curve.setControlY1(textEdgeY(this.startRow, this.startColumn, Arrow.Direction.up)
                        - gap(this.startRow, this.startColumn, Arrow.Direction.up)/2);
                this.curve.setControlX2(textEdgeX(this.endRow, this.endColumn, Arrow.Direction.up));
                this.curve.setControlY2(textEdgeY(this.endRow, this.endColumn, Arrow.Direction.up)
                        - gap(this.endRow, this.endColumn, Arrow.Direction.up)/2);

                this.curve.setEndX(textEdgeX(this.endRow, this.endColumn, Arrow.Direction.up));
                this.curve.setEndY(textEdgeY(this.endRow, this.endColumn, Arrow.Direction.up) + arrowMargin*size);
                redrawTriangle(this.endRow, this.endColumn, Arrow.Direction.up);
            }
            else{
                //todo - spojenia, ktore budu dalej ako vedlajsich stlpcoch/riadkoch. Tam bude treba asi aj inu krivku, respektive dalsiu
                //urobil som nieco podobne so 5timi kontrolnymi bodmi
                //asi najskor vytvorit vlastnu krivku, vo vlastnej triede, ako je napisane vyssie
            }
        }

        private double textEdgeX(int row, int column, Arrow.Direction direction){
            textFlowMatrix.get(row).get(column).autosize();
            double result = 0.0;
            if(direction == Arrow.Direction.up || direction == Arrow.Direction.down){
                result = textFlowMatrix.get(row).get(column).getLayoutX()
                        + textFlowMatrix.get(row).get(column).getLayoutBounds().getCenterX();
            }
            else if(direction == Arrow.Direction.right){
                result = textFlowMatrix.get(row).get(column).getLayoutX()
                        + textFlowMatrix.get(row).get(column).getLayoutBounds().getMaxX();
            }
            else if(direction == Arrow.Direction.left){
                result = textFlowMatrix.get(row).get(column).getLayoutX()
                        + textFlowMatrix.get(row).get(column).getLayoutBounds().getMinX();
            }
            return result;
        }

        private double textEdgeY(int row, int column, Arrow.Direction direction){
            textFlowMatrix.get(row).get(column).autosize();
            double result = 0.0;
            if(direction == Arrow.Direction.left || direction == Arrow.Direction.right){
                result = textFlowMatrix.get(row).get(column).getLayoutY()
                        + textFlowMatrix.get(row).get(column).getLayoutBounds().getCenterY();
            }
            else if(direction == Arrow.Direction.down){
                result = textFlowMatrix.get(row).get(column).getLayoutY()
                        + textFlowMatrix.get(row).get(column).getLayoutBounds().getMaxY();
            }
            else if(direction == Arrow.Direction.up){
                result = textFlowMatrix.get(row).get(column).getLayoutY()
                        + textFlowMatrix.get(row).get(column).getLayoutBounds().getMinY();
            }
            return result;
        }

        private double gap(int row, int column, Arrow.Direction direction){
            textFlowMatrix.get(row).get(column).autosize();
            if(direction == Arrow.Direction.up || direction == Arrow.Direction.down){
                //toto je pre vsetky rovnake, tato medzera je nezavisla na dlzke textu, iba na jeho vyske - ta je rovnaka
                if(textFlowMatrix.size() > 1){
                    textFlowMatrix.get(0).get(0).autosize();
                    textFlowMatrix.get(1).get(0).autosize();
                    return (textFlowMatrix.get(1).get(0).getLayoutY() - textFlowMatrix.get(0).get(0).getLayoutY()
                            - textFlowMatrix.get(row).get(column).getLayoutBounds().getWidth());
                }
                else{
                    //todo
                }
            }
            else if(direction == Arrow.Direction.left){
                if(column > 0){
                    textFlowMatrix.get(row).get(column - 1).autosize();
                    return (textFlowMatrix.get(row).get(column).getLayoutX()
                            - textFlowMatrix.get(row).get(column - 1).getLayoutX()
                            - textFlowMatrix.get(row).get(column - 1).getLayoutBounds().getWidth());
                }
                else{
                    //todo
                }
            }
            else if(direction == Arrow.Direction.right){
                textFlowMatrix.get(row).get(column + 1).autosize();
                if(column < textFlowMatrix.get(0).size() - 1){
                    return (textFlowMatrix.get(row).get(column + 1).getLayoutX()
                            - textFlowMatrix.get(row).get(column).getLayoutX()
                            - textFlowMatrix.get(row).get(column).getLayoutBounds().getWidth());
                }
                else {
                    //todo
                }
            }
            return 0.0;
        }

        //miesto, kde ukazuje dany trojuholnik, a smerom, odkial to ide (teda bude ukzaovat opacne)
        private void redrawTriangle(int row, int column, Arrow.Direction direction){
            //todo - posunut, aby bol na stred, pri otacanie sa otaca okolo svojho horneho laveho rohu, nie stredu
            this.triangle.setLayoutX(textEdgeX(row, column, direction) - this.triangle.getLayoutBounds().getCenterX());
            this.triangle.setLayoutY(textEdgeY(row, column, direction) - this.triangle.getLayoutBounds().getCenterY());
            this.triangleDirection = direction;
            switch (direction){
                case up -> this.triangle.setRotate(180.0);
                case right -> this.triangle.setRotate(270.0);
                case down -> this.triangle.setRotate(0.0);
                case left -> this.triangle.setRotate(90.0);
            }
        }

        @Override
        public String toString(){
            return "MatrixDisplay[" +
                    "startRow:"+this.startRow+"; "+
                    "startColumn:"+this.startColumn+"; "+
                    "endRow:"+this.endRow+"; "+
                    "endColumn:"+this.endColumn+"; "+
                    "isHighlighted:"+this.isHighlighted+";"+
                    "]";
        }

        enum Direction{up, down, right, left}
    }

    public Matrix(Pane pane, double leftBorder, double upperBorder, double rightBorder, double bottomBorder) {
        super(pane, leftBorder, upperBorder, rightBorder, bottomBorder);
        this.pane = pane;
        this.arrows = new ArrayList<>();
        this.textFlowMatrix = new ArrayList<>();
        this.textMatrix = new ArrayList<>();
        this.indexMatrix = new ArrayList<>();
    }


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

        double horizontalGap = Math.max(0.0, (super.rightBorder - super.leftBorder
                - longestWordsPrefixSum.get(longestWordsPrefixSum.size() - 1))/(this.numberOfColumns + 1));
        double verticalGap = Math.max(0.0, (super.bottomBorder - super.upperBorder)/(this.numberOfRows + 1));

        for(int i = 0;i<this.numberOfRows;i++){
            for(int j = 0;j<this.numberOfColumns;j++){
                this.textFlowMatrix.get(i).get(j).setLayoutX(horizontalGap + longestWordsPrefixSum.get(j) + j*horizontalGap);
                this.textFlowMatrix.get(i).get(j).setLayoutY(verticalGap + i*verticalGap);
            }
        }
        this.arrows.forEach(Arrow::changeSize);
    }

    @Override
    protected void setBorders(double leftBorder, double upperBorder, double rightBorder, double bottomBorder){
        //vsetko treba preskaloat
        //ale treba im dat na zaciatku nejake veci, inak navzdy ostanu 0
        centre();
        System.out.println("brd "+this.rightBorder+" "+this.bottomBorder+" "+rightBorder+" "+bottomBorder);
        for(ArrayList<TextFlow> textFlows:this.textFlowMatrix){
            for(TextFlow textFlow:textFlows){
                textFlow.setLayoutX(super.resizedX(leftBorder, rightBorder, textFlow.getLayoutX()));
                textFlow.setLayoutY(super.resizedY(upperBorder, bottomBorder, textFlow.getLayoutY()));
            }
        }
        super.setBorders(leftBorder, upperBorder, rightBorder, bottomBorder);
    }

    public void setMatrixSize(int numberOfRows, int numberOfColumns){
        this.arrows.forEach(Arrow::delete);
        this.arrows.clear();
        this.numberOfColumns = numberOfColumns;
        this.numberOfRows = numberOfRows;

        this.pane.getChildren().clear();
        this.textFlowMatrix.clear();
        this.textMatrix.clear();
        this.indexMatrix.clear();

        for(int i = 0;i<this.numberOfRows;i++){
            ArrayList<TextFlow> rowOfTextFlows = new ArrayList<>(this.numberOfColumns);
            ArrayList<Text> rowOfTexts = new ArrayList<>();
            ArrayList<Text> rowOfIndexes = new ArrayList<>();
            for(int j = 0; j < this.numberOfColumns; j++){
                Text text = new Text();
                text.setStyle("-fx-font-size: "+(this.textSize*this.size)+";");
                Text index = new Text();
                index.setStyle("-fx-font-size: "+(this.indexConstant*this.textSize*this.size)+";");
                TextFlow textFlow = new TextFlow();
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

    public void highlightSquare(int rowNumber, int columnNumber){
        if(isInMatrix(rowNumber, columnNumber)) {
            this.textFlowMatrix.get(rowNumber).get(columnNumber).setStyle("-fx-font-weight: bold");
            //todo - nastavit rovnaku farbu ako je pri texte
            this.textFlowMatrix.get(rowNumber).get(columnNumber).setBackground(
                    new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
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
        if(isInMatrix(rowNumber, columnNumber)) {
            this.textFlowMatrix.get(rowNumber).get(columnNumber).setStyle("-fx-font-weight: normal");
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

    public void setSquareColor(int rowNumber, int columnNumber, Color color){
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

    private Arrow storedArrow(int startRow, int startColumn, int endRow, int endColumn){
        if(isInMatrix(startRow, startColumn) && isInMatrix(endRow, endColumn)){
            for(Arrow arrow:this.arrows){
                if((arrow.startRow == startRow) && (arrow.startColumn == startColumn) && (arrow.endRow == endRow)
                        && (arrow.endColumn == endColumn)){
                    return arrow;
                }
            }
        }
        return null;
    }

    public void addArrow(int startRow, int startColumn, int endRow, int endColumn){
        if(storedArrow(startRow, startColumn, endRow, endColumn) == null){
            this.arrows.add(new Arrow(startRow, startColumn, endRow, endColumn));
        }
    }

    public void removeArrow(int startRow, int startColumn, int endRow, int endColumn){
        Arrow arrow = storedArrow(startRow, startColumn, endRow, endColumn);
        if(arrow != null){
            arrow.delete();
            this.arrows.remove(arrow);
        }
    }

    public void setArrowColor(int startRow, int startColumn, int endRow, int endColumn, Color color){
        Arrow arrow = storedArrow(startRow, startColumn, endRow, endColumn);
        if(arrow != null){
            arrow.setColor(color);
        }
    }

    public void highlightArrow(int startRow, int startColumn, int endRow, int endColumn){
        Arrow arrow = storedArrow(startRow, startColumn, endRow, endColumn);
        if(arrow != null){
            arrow.highlight();
        }
    }

    public void unhighlightArrow(int startRow, int startColumn, int endRow, int endColumn){
        Arrow arrow = storedArrow(startRow, startColumn, endRow, endColumn);
        if(arrow != null){
            arrow.unhighlight();
        }
    }

    public void clearArrows(){
        //todo
    }

    public void setSize(double size){
        this.size = size;
        centre();
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

    //ak by sme vytvorili tuto triedu, potom matrixDisplay stráca zmysel.
    //potom vsetky displaye stratia zmysel, iba bude multi, ktoremu povieme, co chceme
    //ak nemame tuto triedu, potom urobit nieco zlozitejsie sa takmer neda, musime povolit vkladat jednotlive
    /*
    prvky, co zacne sposobovat velmi skarede veci. Ale netreba nic pridavat. A nebude sa ale dat ani robit nic s viacerymi
    maticami. asi najlepsie bude len to vytvorit. Len matrixDisplay bude robit takmer to. Len nema arrayclassu,
    a potom nechat cloveka robit s tymito vecami? ako to..
    matrix display netreba. ale ako menit tymto veciam rozmer?
    v podstate to moze fungovat, display kontroluje, ked sa zmeni jeho velkost
    tak sa zmeni aj pane.
    a nechame na multi, ako to vyriesi, len aby to bolo dobre.
    pri vlozeni do multi nedame velkost, ale pomer stran. a tiez treba vymysliet, ako dat ku sebe tie veci
    mozno teraz staci iba do jedneho
    a potom sa mozu vymysliet ine veci, ako mozu byt na seba? alebo budu v riadkoch, na nejakcyh vecach?
    este porozmyslat
     */

}
