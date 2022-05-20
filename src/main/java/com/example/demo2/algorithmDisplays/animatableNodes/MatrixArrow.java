package com.example.demo2.algorithmDisplays.animatableNodes;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Polygon;
import javafx.util.Pair;

import java.util.ArrayList;

public class MatrixArrow implements ColorAnimatable, AppearAnimatable{

    private final Pane pane;
    private double size;
    private double arrowMargin;
    private final ArrayList<ArrayList<AnimatableTextFlow>> textFlowMatrix;

    private final CubicCurve curve;
    private final Polygon triangle;
    private Color color;

    private final int startRow;
    private final int startColumn;
    private final int endRow;
    private final int endColumn;

    private boolean isHighlighted = false;
    private Direction triangleDirection = Direction.up;

    public MatrixArrow(Pane pane, ArrayList<ArrayList<AnimatableTextFlow>> textFlowMatrix,
                       double size, double arrowMargin, int startRow, int startColumn, int endRow, int endColumn){
        this.pane = pane;
        this.size = size;
        this.arrowMargin = arrowMargin;
        this.textFlowMatrix = textFlowMatrix;
        //todo vyriesit, ze niekedy to moze pretat text - ak su nerovnako dlhe texty v matici - a mozno aj nie...
        this.startColumn = startColumn;
        this.startRow = startRow;
        this.endColumn = endColumn;
        this.endRow = endRow;
        this.color = Color.BLACK;
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

    public void delete(){
        pane.getChildren().remove(this.curve);
        pane.getChildren().remove(this.triangle);
    }

    public boolean hasCoordinates(int startRow, int startColumn, int endRow, int endColumn){
        return this.startRow == startRow && this.startColumn == startColumn && this.endRow == endRow
                && this.endColumn == endColumn;
    }

    public Pair<Integer, Integer> startCoordinates(){
        return new Pair<>(this.startRow, this.startColumn);
    }

    public Pair<Integer, Integer> endCoordinates(){
        return new Pair<>(this.endRow, this.endColumn);
    }

    @Override
    public Color getColor(){
        return this.color;
    }

    @Override
    public void setColor(Color color){
        this.color = color;
        this.triangle.setStroke(color);
        this.triangle.setFill(color);
        this.curve.setStroke(color);
    }

    public void highlight(){
        this.curve.setStrokeWidth(size*1.8);
        this.triangle.setScaleX(size*1.5);
        this.triangle.setScaleY(size*1.5);
        this.isHighlighted = true;
        redrawTriangle(this.endRow, this.endColumn, this.triangleDirection);
    }

    public void unhighlight(){
        this.curve.setStrokeWidth(size);
        this.triangle.setScaleX(size);
        this.triangle.setScaleY(size);
        this.isHighlighted = false;
        redrawTriangle(this.endRow, this.endColumn, this.triangleDirection);
    }

    public void changeSize(){
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
            Direction startDirection, endDirection;
            int directionChange;
            if(this.endColumn - this.startColumn == 1){
                startDirection = Direction.right;
                endDirection = Direction.left;
                directionChange = 1;
            }
            else{
                startDirection = Direction.left;
                endDirection = Direction.right;
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
            Direction startDirection, endDirection;
            int directionChange;
            if(this.endRow - this.startRow == 1){
                startDirection = Direction.down;
                endDirection = Direction.up;
                directionChange = 1;
            }
            else{
                startDirection = Direction.up;
                endDirection = Direction.down;
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
                    - directionChange*gap(this.endRow, this.endColumn, Direction.up)/2);

            this.curve.setEndX(textEdgeX(this.endRow, this.endColumn, endDirection));
            this.curve.setEndY(textEdgeY(this.endRow, this.endColumn, endDirection)
                    - directionChange*arrowMargin*size);
            redrawTriangle(this.endRow, this.endColumn, endDirection);
        }
        else if(this.startColumn == this.endColumn){
            this.curve.setStartX(textEdgeX(this.startRow, this.startColumn, Direction.right) + arrowMargin*size);
            this.curve.setStartY(textEdgeY(this.startRow, this.startColumn, Direction.right));

            this.curve.setControlX1(textEdgeX(this.startRow, this.startColumn, Direction.right)
                    + gap(this.startRow, this.startColumn, Direction.right));
            this.curve.setControlY1(textEdgeY(this.startRow, this.startColumn, Direction.right));
            this.curve.setControlX2(textEdgeX(this.endRow, this.endColumn, Direction.right)
                    + gap(this.endRow, this.endColumn, Direction.right));
            this.curve.setControlY2(textEdgeY(this.endRow, this.endColumn, Direction.right));

            this.curve.setEndX(textEdgeX(this.endRow, this.endColumn, Direction.right) + arrowMargin*size);
            this.curve.setEndY(textEdgeY(this.endRow, this.endColumn, Direction.right));
            redrawTriangle(this.endRow, this.endColumn, Direction.right);
        }
        else if(this.startRow == this.endRow){
            this.curve.setStartX(textEdgeX(this.startRow, this.startColumn, Direction.up));
            this.curve.setStartY(textEdgeY(this.startRow, this.startColumn, Direction.up) + arrowMargin*size);

            this.curve.setControlX1(textEdgeX(this.startRow, this.startColumn, Direction.up));
            this.curve.setControlY1(textEdgeY(this.startRow, this.startColumn, Direction.up)
                    - gap(this.startRow, this.startColumn, Direction.up)/2);
            this.curve.setControlX2(textEdgeX(this.endRow, this.endColumn, Direction.up));
            this.curve.setControlY2(textEdgeY(this.endRow, this.endColumn, Direction.up)
                    - gap(this.endRow, this.endColumn, Direction.up)/2);

            this.curve.setEndX(textEdgeX(this.endRow, this.endColumn, Direction.up));
            this.curve.setEndY(textEdgeY(this.endRow, this.endColumn, Direction.up) + arrowMargin*size);
            redrawTriangle(this.endRow, this.endColumn, Direction.up);
        }
        else{
            //todo - spojenia, ktore budu dalej ako vedlajsich stlpcoch/riadkoch. Tam bude treba asi aj inu krivku, respektive dalsiu
            //urobil som nieco podobne so 5timi kontrolnymi bodmi
            //asi najskor vytvorit vlastnu krivku, vo vlastnej triede, ako je napisane vyssie
        }
    }

    private double textEdgeX(int row, int column, Direction direction){
        textFlowMatrix.get(row).get(column).autosize();
        double result = 0.0;
        if(direction == Direction.up || direction == Direction.down){
            result = textFlowMatrix.get(row).get(column).getLayoutX()
                    + textFlowMatrix.get(row).get(column).getLayoutBounds().getCenterX();
        }
        else if(direction == Direction.right){
            result = textFlowMatrix.get(row).get(column).getLayoutX()
                    + textFlowMatrix.get(row).get(column).getLayoutBounds().getMaxX();
        }
        else if(direction == Direction.left){
            result = textFlowMatrix.get(row).get(column).getLayoutX()
                    + textFlowMatrix.get(row).get(column).getLayoutBounds().getMinX();
        }
        return result;
    }

    private double textEdgeY(int row, int column, Direction direction){
        textFlowMatrix.get(row).get(column).autosize();
        double result = 0.0;
        if(direction == Direction.left || direction == Direction.right){
            result = textFlowMatrix.get(row).get(column).getLayoutY()
                    + textFlowMatrix.get(row).get(column).getLayoutBounds().getCenterY();
        }
        else if(direction == Direction.down){
            result = textFlowMatrix.get(row).get(column).getLayoutY()
                    + textFlowMatrix.get(row).get(column).getLayoutBounds().getMaxY();
        }
        else if(direction == Direction.up){
            result = textFlowMatrix.get(row).get(column).getLayoutY()
                    + textFlowMatrix.get(row).get(column).getLayoutBounds().getMinY();
        }
        return result;
    }

    private double gap(int row, int column, Direction direction){
        textFlowMatrix.get(row).get(column).autosize();
        if(direction == Direction.up || direction == Direction.down){
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
        else if(direction == Direction.left){
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
        else if(direction == Direction.right){
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
    private void redrawTriangle(int row, int column, Direction direction){
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

    @Override
    public void appear() {
        this.curve.setVisible(true);
        this.triangle.setVisible(true);
    }

    @Override
    public void disappear() {
        this.curve.setVisible(false);
        this.triangle.setVisible(false);
    }

    @Override
    public void setOpacity(double opacity) {
        setColor(new Color(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), opacity));
    }

    @Override
    public double getOpacity() {
        return this.color.getOpacity();
    }

    enum Direction{up, down, right, left}
}


