package com.example.demo2.algorithmDisplays;

import javafx.scene.layout.Pane;
import javafx.util.Pair;


public class Sector {

    public enum SectorType{Matrix}

    protected final Pane pane;
    protected double leftBorder;
    protected double upperBorder;
    protected double rightBorder;
    protected double bottomBorder;

    public Sector(Pane pane, double leftBorder, double upperBorder, double rightBorder, double bottomBorder){
        this.pane = pane;
        this.leftBorder = leftBorder;
        this.upperBorder = upperBorder;
        this.rightBorder = rightBorder;
        this.bottomBorder = bottomBorder;
    }

    protected void setBorders(double leftBorder, double upperBorder, double rightBorder, double bottomBorder){
        this.leftBorder = leftBorder;
        this.upperBorder = upperBorder;
        this.rightBorder = rightBorder;
        this.bottomBorder = bottomBorder;
    }

    protected Pair<Double, Double> getPosition(double x, double y){
        return new Pair<>(getX(x), getY(y));
    }

    protected double getX(double x){
        return Math.min(Math.max(x, this.leftBorder), this.rightBorder);
    }

    protected double getY(double y){
        return Math.min(Math.max(y, this.upperBorder), this.bottomBorder);
    }

    public void centre(){
    }

    protected Pair<Double, Double> resizedCoordinates(double leftBorder, double upperBorder, double rightBorder,
                                                      double bottomBorder, double x, double y){
        return new Pair<>(resizedX(leftBorder, rightBorder,x ),resizedY(upperBorder, bottomBorder, y));
    }

    protected double resizedX(double leftBorder, double rightBorder, double x){
        return leftBorder + (x - this.leftBorder)/(this.rightBorder - this.leftBorder)*(rightBorder - leftBorder);
    }
    protected double resizedY(double upperBorder, double bottomBorder, double y){
        return upperBorder + (y - this.upperBorder)/(this.bottomBorder - this.upperBorder)*(bottomBorder - upperBorder);
    }
}

