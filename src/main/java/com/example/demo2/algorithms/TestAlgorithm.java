package com.example.demo2.algorithms;

import com.example.demo2.algorithmDisplays.*;
import com.example.demo2.algorithmDisplays.DisplayType;
import com.example.demo2.algorithmDisplays.animatableNodes.Edge;
import com.example.demo2.algorithmDisplays.animatableNodes.Vertex;
import com.example.demo2.algorithmManager.AlgorithmManager;
import com.example.demo2.animations.Animation;
import com.example.demo2.animations.AnimationManager;
import com.example.demo2.animations.AnimationType;
import com.example.demo2.auxiliary.Colors;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TestAlgorithm extends Algorithm {

    private GraphDisplay graphDisplay;
    private MatrixDisplay matrixDisplay;
    private TextDisplay textDisplay;

    private CodeDisplay codeDisplay1;
    private CodeDisplay codeDisplay2;
    private CodeDisplay codeDisplay3;

    private  final ArrayList<Vertex> vertices = new ArrayList<>();;
    private final ArrayList<Vertex> visibleVertices = new ArrayList<>();

    private final AnimationManager animationManager = new AnimationManager();

    private final Random random = new Random();

    private static int V = 9;

    private void addEdge(int start, int end, char val){
        this.matrixDisplay.setSquareText(end + 1, start + 1, val);
        this.textDisplay.addString("("+start+", "+end+") = "+val+"\n ", "", false);
        Edge edge = this.graphDisplay.addEdge(this.vertices.get(start), this.vertices.get(end));
        edge.setColor(Colors.getColor(alphabet.indexOf(val)));
        edge.setText(val);

    }

    ArrayList<Character> alphabet = new ArrayList<>(List.of('$', 'a', 'b', 'd', 'k', 'r'));
    public TestAlgorithm(AlgorithmManager algorithmManager){
        super(algorithmManager);
        this.graphDisplay = (GraphDisplay) WindowManager.addDisplay(DisplayType.DirectedGraph, "",1);
        this.matrixDisplay = (MatrixDisplay) WindowManager.addDisplay(DisplayType.Matrix,"", 1);
        this.matrixDisplay.setMatrixSize(V+1,V+1);
        for(int i = 0;i<V;i++){
            this.matrixDisplay.setSquareText(i + 1, 0, i);
            this.matrixDisplay.setSquareText(0, i + 1, i);
        }
        this.textDisplay = (TextDisplay) WindowManager.addDisplay(DisplayType.Text, "", 1);
        this.textDisplay.addString("    \n ","", false);
        for(int i = 0;i<V;i++){
            Vertex v = this.graphDisplay.addVertex();
            v.setRelativePosition(random.nextDouble(),random.nextDouble());
            v.setValue(i);
            this.vertices.add(v);
            this.visibleVertices.add(v);
        }
        addEdge(0, 1, 'a');
        addEdge(1, 8, 'r');
        addEdge(4, 8, 'r');
        addEdge(7, 4, 'a');
        addEdge(3, 7, 'k');
        addEdge(6, 3, 'a');
        addEdge(2, 6, 'd');
        addEdge(8, 5, 'b');
        addEdge(5, 2, 'a');
        addEdge(2, 0, '$');

        super.addNextBackAnimateControls(0,1,0,0,0,2);
    }

    int step = 0;
    int highestStep = 0;

    class Step{
        Vertex v1;
        Vertex v2;
        Vertex vn;
    }

    final HashMap<Integer, Step> steps = new HashMap<>();



    public void nextStep(boolean animate) {
        animate = true;

        Animation animation = animationManager.getAnimation(step);

        if(step == highestStep){
            for(Vertex vertex:this.vertices){
                if(random.nextDouble() <= 0.25){
                    if(visibleVertices.contains(vertex)){
                        animation.addAnimatable(AnimationType.DisappearAnimation, vertex);
                        double x = random.nextDouble();
                        double y = random.nextDouble();
                        animation.addAnimatable(AnimationType.RelativeMoveAnimation, vertex, x, y);
                        visibleVertices.remove(vertex);
                    }
                    else{
                        animation.addAnimatable(AnimationType.AppearAnimation, vertex);
                        visibleVertices.add(vertex);
                    }
                }
                else {
                    if(visibleVertices.contains(vertex)) {
                        double x = random.nextDouble();
                        double y = random.nextDouble();
                        animation.addAnimatable(AnimationType.RelativeMoveAnimation, vertex, x, y);
                        animation.addAnimatable(AnimationType.ColorAnimation, vertex,
                                new Color(x, y, 1.0 - x, 1.0));
                    }
                }
            }
        }

        if(animate){
            animationManager.executeAnimation(step, true);
        }
        else{
            animationManager.endCurrentAnimation(true);
            animation.endAnimation();
        }

        step++;
        highestStep = Math.max(highestStep, step);
        super.backStepButton.setDisable(false);
    }

    @Override
    protected void backStep(boolean animate){
        animate = true;
        if(step > 0) {
            step--;
            if (animate) {
                animationManager.executeAnimation(step, false);
            } else {
                animationManager.endAnimation(step, false);
            }
        }
        if(step == 0){
            super.backStepButton.setDisable(true);
        }
    }

    private final ArrayList<Vertex> invisibleVertices = new ArrayList<>();
    private final ArrayList<Edge> edges = new ArrayList<>();



    void test1(String s){
        codeDisplay1.addLine(s);
        codeDisplay2.addLine(s);
        codeDisplay3.addLine(s);
    }
    void test2(String s, int i){
        codeDisplay1.addVariable(s, i);
        codeDisplay2.addVariable(s,i);
        codeDisplay3.addVariable(s,i);
    }

    void testHL(int i){
        codeDisplay1.highlightLine(i);
        codeDisplay2.highlightLine(i);
        codeDisplay3.highlightLine(i);
    }

    void testHV(String s){
        codeDisplay1.highlightVariable(s);
        codeDisplay2.highlightVariable(s);
        codeDisplay3.highlightVariable(s);
    }

}
