package com.example.demo2.auxiliary;

import javafx.geometry.Point2D;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;

import java.util.ArrayList;

public class MultiCurve {

    /*
    prvy index pathNumber - hovori, o ceste z bodu A do bodu B
    druhy index curveNumber - hovori o krivke na ceste (respektive vsetkych krivkach na vsetkych cestach)

    prvy viaPoint je zaciatok, posledny je koniec.
     */

    private int pathsNumber;
    private int pointsNumber;
    private int curvesNumber;//= pointsNumber - 1;

    private final ArrayList<ArrayList<CubicCurve>> curves;
    private final ArrayList<Color> colors;
    private final ArrayList<Point2D> controlPoints;
    private final ArrayList<Point2D> viaPoints;
    private final Pane pane;

    private double width = 1.0;

    public MultiCurve(Pane pane){
        this.curves = new ArrayList<>();
        this.pane = pane;
        this.controlPoints = new ArrayList<>();
        this.viaPoints = new ArrayList<>();
        this.colors = new ArrayList<>();
        this.pathsNumber = 0;
        this.curvesNumber = 0;
        this.pointsNumber = 0;
    }

    public void addPath(){
        ArrayList<CubicCurve> path = new ArrayList<>();
        for(int i = 0;i<this.curvesNumber;i++){
            CubicCurve curve = new CubicCurve();
            this.pane.getChildren().add(curve);
            path.add(curve);
        }
        this.curves.add(path);
        this.pathsNumber++;
        redrawPath(this.pathsNumber - 1);
    }

    public void removePath(int pathNumber){
        if(existsPath(pathNumber)) {
            for (CubicCurve curve : this.curves.get(pathNumber)) {
                this.pane.getChildren().remove(curve);
            }
            this.curves.remove(pathNumber);
            this.pathsNumber--;
        }
    }

    public void setColor(int pathNumber, Color color){
        if(existsPath(pathNumber)){
            this.colors.set(pathNumber, color);
            this.curves.get(pathNumber).forEach(cubicCurve -> {cubicCurve.setStroke(color); cubicCurve.setFill(color);});
        }
    }

    public void addViaPoint(double x, double y){
        this.viaPoints.add(new Point2D(x, y));
        this.controlPoints.add(new Point2D(x, y));
        if(this.pointsNumber > 0) {
            for (int i = 0; i < this.pathsNumber; i++) {
                CubicCurve curve = new CubicCurve();
                curve.setStroke(this.colors.get(i));
                curve.setFill(this.colors.get(i));
                this.curves.get(i).add(curve);
                this.curvesNumber++;
            }
        }
        this.pointsNumber++;
    }

    public void setControlPoint(int pointNumber, double x, double y){
        if(existsPoint(pointNumber)){
            this.controlPoints.set(pointNumber, new Point2D(x, y));
            redrawSection(pointNumber - 1);
            redrawSection(pointNumber);
        }
    }

    public void setViaPoint(int pointNumber, double x, double y){
        if(existsPoint(pointNumber)){
            this.viaPoints.set(pointNumber, new Point2D(x, y));
            redrawSection(pointNumber - 1);
            redrawSection(pointNumber);
        }
    }

    public void setWidth(double width) {
        this.width = width;
        redraw();
    }

    void redraw(){
        for(int i = 0;i<this.curves.size();i++){
            redrawSection(i);
        }
    }

    void redrawSection(int curveNumber){
        if(existsCurve(curveNumber)) {
            for (int i = 0; i < this.pathsNumber; i++) {
                redrawCurve(curveNumber, i);
            }
        }
    }

    void redrawPath(int pathNumber){
        if(existsPath(pathNumber)){
            for(int i = 0; i < this.curvesNumber;i++){
                redrawCurve(i, pathNumber);
            }
        }
    }

    void redrawCurve(int curveNumber, int pathNumber){
        if(!existsCurve(curveNumber) || !existsPath(pathNumber)){
            return;
        }

        Point2D start = new Point2D(this.viaPoints.get(curveNumber).getX(), this.viaPoints.get(curveNumber).getY());
        Point2D end = new Point2D(this.viaPoints.get(curveNumber + 1).getX(), this.viaPoints.get(curveNumber + 1).getY());
        Point2D controlStart = new Point2D(this.controlPoints.get(curveNumber).getX(), this.controlPoints.get(curveNumber).getY());
        Point2D controlEnd = new Point2D(this.controlPoints.get(curveNumber + 1).getX(), this.controlPoints.get(curveNumber + 1).getY());


        /*
        todo
        vypocitat ako to vyzera, ci je to S, alebo U
        tiez je potrbene vediet, do ktorej strany sa otacame
        to je vsak potrebne prepocitat uz pri zmene bodu, ci


        na to treba najako orientaciu
        posunut potom spravne body tak, aby to bolo dobre

         */

        this.curves.get(pathNumber).get(curveNumber).setStartX(start.getX());
        this.curves.get(pathNumber).get(curveNumber).setStartY(start.getY());
        this.curves.get(pathNumber).get(curveNumber).setEndX(end.getX());
        this.curves.get(pathNumber).get(curveNumber).setEndY(end.getY());
        this.curves.get(pathNumber).get(curveNumber).setControlX1(controlStart.getX());
        this.curves.get(pathNumber).get(curveNumber).setControlY1(controlStart.getY());
        this.curves.get(pathNumber).get(curveNumber).setControlX2(controlEnd.getX());
        this.curves.get(pathNumber).get(curveNumber).setControlY2(controlEnd.getY());
    }

    public boolean existsPath(int pathNumber){
        return 0 <= pathNumber && pathNumber < this.pathsNumber;
    }

    public boolean existsPoint(int pointNumber){
        return 0 <= pointNumber && pointNumber < this.pointsNumber;
    }

    public boolean existsCurve(int curveNumber){
        return 0 <= curveNumber && curveNumber < this.curvesNumber;
    }
}
