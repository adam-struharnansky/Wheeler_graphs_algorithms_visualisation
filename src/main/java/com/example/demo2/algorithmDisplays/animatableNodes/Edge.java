package com.example.demo2.algorithmDisplays.animatableNodes;


import com.example.demo2.algorithmDisplays.GraphDisplay;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;
import java.util.ArrayList;

public class Edge implements AppearAnimatable, ColorAnimatable{

    protected final Pane graphPane;
    protected String text;
    protected final Label label;

    protected final ArrayList<CubicCurve> curves = new ArrayList<>();
    protected final ArrayList<Integer> curveIds = new ArrayList<>();
    protected double width = 2.5;
    protected double gap = 5.0;
    protected double textGap = 8.0;
    protected double textMove = 5.0;

    protected final Vertex vertexFrom;
    protected final Vertex vertexTo;
    protected GraphDisplay graphDisplay;

    protected boolean visible = true;
    protected double opacity = 1.0;

    protected Color color;

    protected boolean isLoop;

    Edge(GraphDisplay graphDisplay, Pane graphPane, Vertex vertexFrom, Vertex vertexTo){
        this.graphDisplay = graphDisplay;
        this.graphPane = graphPane;
        this.vertexFrom = vertexFrom;
        this.vertexTo = vertexTo;
        this.isLoop = vertexFrom == vertexTo;
        this.label = new Label();
        this.label.setStyle("-fx-font-weight: bold");
        this.graphPane.getChildren().add(this.label);
        this.color = Color.BLACK;

        addWay(0, Color.BLACK);

        redraw();
    }

    public String getText(){
        return this.text;
    }

    public void setText(String text){
        this.label.setText(text);
        this.text = text;
    }

    public int getWaysNumber(){
        return this.curves.size();
    }

    public void setText(int text){
        setText(text + "");
    }

    public void setText(char text){
        setText(text+"");
    }

    public void delete(){
        //this.graphPane.getChildren().remove(this.line);
        this.graphPane.getChildren().remove(this.label);
        for(CubicCurve curve:this.curves){
            this.graphPane.getChildren().remove(curve);
        }
    }

    public void addWay(int id, Color color){
        //todo - odstranit, ak je iba jedna
        if(!this.curveIds.contains(id)){
            if(this.curveIds.size() == 1 && this.curveIds.get(0) == 0){
                removeWay(0);
            }
            this.curveIds.add(id);
            CubicCurve curve = new CubicCurve();
            curve.setFill(Color.TRANSPARENT);
            curve.setStroke(color);
            curve.setStrokeWidth(width);
            this.curves.add(curve);
            this.graphPane.getChildren().add(curve);
            this.label.toFront();
            redraw();
            toPaneFront();
        }
    }

    public void toPaneFront(){}

    public void removeWay(int id){
        if(this.curveIds.contains(id)){
            this.graphPane.getChildren().remove(this.curves.get(this.curveIds.indexOf(id)));
            this.curves.remove(this.curveIds.indexOf(id));
            this.curveIds.remove(Integer.valueOf(id));
            if(id != 0 && this.curveIds.isEmpty()){
                addWay(0, Color.BLACK);
            }
            redraw();
        }
    }

    public void removeAllWays(){
        for(int i = this.curveIds.size() - 1; i >= 0; i --){
            removeWay(this.curveIds.get(i));
        }
        addWay(0, Color.BLACK);
    }

    private boolean onSameSide(Point2D e1, Point2D e2, Point2D c1, Point2D c2){
        double d1 = (c1.getX() - e1.getX())*(e2.getY() - e1.getY()) - (c1.getY() - e1.getY())*(e2.getX() - e1.getX());
        double d2 = (c2.getX() - e1.getX())*(e2.getY() - e1.getY()) - (c2.getY() - e1.getY())*(e2.getX() - e1.getX());
        return (!(d1 < 0.0) || !(d2 > 0.0)) && (!(d1 > 0.0) || !(d2 < 0.0));
    }

    protected Point2D startPoint;
    protected Point2D startControl;
    protected Point2D endPoint;
    protected Point2D endControl;

    public void redraw(){
        if(this.isLoop){
            Point2D norm = new Point2D(
                    -(vertexFrom.getY() - vertexFrom.edgeEnd(this).getY()),
                    (vertexFrom.getX() - vertexFrom.edgeEnd(this).getX())
                    );
            startPoint = vertexFrom.edgeEnd(this);
            startControl = new Point2D(
                    startPoint.getX() + 2*(startPoint.getX() - vertexFrom.x) + 2*norm.getX(),
                    startPoint.getY() + 2*(startPoint.getY() - vertexFrom.y) + 2*norm.getY());
            endPoint = vertexTo.edgeEnd(this);
            endControl = new Point2D(
                    endPoint.getX() + 2*(endPoint.getX() - vertexTo.x) - 2*norm.getX(),
                    endPoint.getY() + 2*(endPoint.getY() - vertexTo.y) - 2*norm.getY());
        }
        else {
            startPoint = vertexFrom.edgeEnd(this);
            startControl = new Point2D(
                    startPoint.getX() + (startPoint.getX() - vertexFrom.x),
                    startPoint.getY() + (startPoint.getY() - vertexFrom.y));
            endPoint = vertexTo.edgeEnd(this);
            endControl = new Point2D(
                    endPoint.getX() + (endPoint.getX() - vertexTo.x),
                    endPoint.getY() + (endPoint.getY() - vertexTo.y));
        }

        Point2D spv, scv, epv, ecv;
        spv = new Point2D(-(startPoint.getY() - startControl.getY())/(startPoint.distance(startControl)),
                (startPoint.getX() - startControl.getX())/(startPoint.distance(startControl)));
        epv = new Point2D((endPoint.getY() - endControl.getY())/(endPoint.distance(endControl)),
                -(endPoint.getX() - endControl.getX())/(endPoint.distance(endControl)));

        if(onSameSide(startPoint, endPoint, startControl, endControl)){//S
            //todo - Control, what it is doing
            scv = new Point2D((startPoint.getX() - vertexFrom.x)/vertexFrom.radius,
                    (startPoint.getY() - vertexFrom.y)/vertexFrom.radius);
            ecv = new Point2D((endPoint.getX() - vertexTo.x)/vertexTo.radius,
                    (endPoint.getY() - vertexTo.y)/vertexTo.radius);
        }
        else{//U
            scv = new Point2D(-(startPoint.getX() - vertexFrom.x)/vertexFrom.radius,
                    -(startPoint.getY() - vertexFrom.y)/vertexFrom.radius);
            ecv = new Point2D((endPoint.getX() - vertexTo.x)/vertexTo.radius,
                    (endPoint.getY() - vertexTo.y)/vertexTo.radius);
        }

        for(int i = 0;i<this.curves.size();i++){
            this.curves.get(i).setStartX(startPoint.getX());
            this.curves.get(i).setStartY(startPoint.getY());
            this.curves.get(i).setControlX1(startControl.getX() + i*spv.getX()*gap + i*scv.getX()*gap);
            this.curves.get(i).setControlY1(startControl.getY() + i*spv.getY()*gap + i*scv.getY()*gap);
            this.curves.get(i).setControlX2(endControl.getX() + i*epv.getX()*gap + i*ecv.getX()*gap);
            this.curves.get(i).setControlY2(endControl.getY() + i*epv.getY()*gap + i*ecv.getY()*gap);
            this.curves.get(i).setEndX(endPoint.getX());
            this.curves.get(i).setEndY(endPoint.getY());
        }

        this.label.setLayoutX((startControl.getX() + endControl.getX())/2);
        this.label.setLayoutY((startControl.getY() + endControl.getY())/2);
        this.graphDisplay.getEdges().forEach(edge -> {
            if(edge != this){
                Point2D vector = getTextPosition().subtract(edge.getTextPosition());
                double vectorSize = vector.distance(0.0,0.0);
                if(vectorSize < textGap){
                    this.label.setLayoutX(this.label.getLayoutX() + textMove*vector.getX()/vectorSize);
                    this.label.setLayoutY(this.label.getLayoutY() + textMove*vector.getY()/vectorSize);
                }
            }
        });
    }

    public Vertex getVertexFrom() {
        return this.vertexFrom;
    }

    public Vertex getVertexTo(){
        return this.vertexTo;
    }

    public void setVisible(boolean visible){
        this.visible = visible;
        for(CubicCurve curve:this.curves){
            curve.setVisible(visible);
        }
        this.label.setVisible(visible);
    }

    public void incidentVertexVisibilityChanged(){
        setVisible(this.vertexFrom.isVisible() && this.vertexTo.isVisible());
    }

    @Override
    public void appear() {
        this.opacity = 1.0;
        setVisible(true);
    }

    @Override
    public void disappear() {
        this.opacity = 0.0;
        setVisible(false);
    }


    @Override
    public double getOpacity(){
        return this.opacity;
    }

    @Override
    public void setOpacity(double opacity){
        this.opacity = opacity;
        this.label.setOpacity(opacity);
        for(CubicCurve curve:this.curves){
            curve.setOpacity(opacity);
        }
    }

    public boolean isVisible() {
        return this.visible;
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
        if(this.curves.size() == 1){
            this.curves.get(0).setStroke(color);
            this.label.setTextFill(Color.BLACK);
        }
        else {
            this.label.setTextFill(color);
        }
    }

    @Override
    public Color getColor() {
        return this.color;
    }

    public Point2D getTextPosition(){
        return new Point2D(this.label.getLayoutX(), this.label.getLayoutY());
    }
}

