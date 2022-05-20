package com.example.demo2.algorithms.wg;

import com.example.demo2.Step.StepManager;
import com.example.demo2.algorithmDisplays.*;
import com.example.demo2.algorithmDisplays.animatableNodes.DirectedEdge;
import com.example.demo2.algorithmDisplays.animatableNodes.DirectedVertex;
import com.example.demo2.algorithmDisplays.DisplayType;
import com.example.demo2.algorithmManager.AlgorithmManager;
import com.example.demo2.algorithmManager.AlgorithmType;
import com.example.demo2.algorithms.Algorithm;
import com.example.demo2.animations.Animation;
import com.example.demo2.animations.AnimationManager;
import com.example.demo2.animations.AnimationType;
import com.example.demo2.auxiliary.Algorithms;
import com.example.demo2.auxiliary.Colors;
import com.example.demo2.multilingualism.LanguageListenerAdder;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Comparator;

public class WGFromBWT extends Algorithm {

    private final AlgorithmManager algorithmManager;
    private final StepManager stepManager = new StepManager();

    private final GraphDisplay graphDisplay = (GraphDisplay) WindowManager.addDisplay(DisplayType.DirectedGraph, "wg", 2);
    private final MatrixDisplay matrixDisplay = (MatrixDisplay) WindowManager.addDisplay(DisplayType.Matrix, "BWTWithLF", 1);
    private final TextDisplay textDisplay = (TextDisplay) WindowManager.addDisplay(DisplayType.Text, "description", 1);

    private Label startLabel;
    private TextField inputTextField;
    private Button startButton;
    private final boolean fromTextField;
    private final Button changeButton = new Button();
    private final Button inverseButton = new Button();
    private final Button tunnelButton = new Button();

    private static final int iColumn = 0;
    private static final int lfColumn = 1;
    private static final int fColumn = 3;//todo - Add arrows, when going through LF-mapping in matrix
    private static final int lColumn = 2;

    private final AnimationManager animationManager = new AnimationManager();

    private String input;
    private String lV;
    private int[] lfV;
    private final ArrayList<DirectedVertex> vertices = new ArrayList<>();

    private final ArrayList<Character> l = new ArrayList<>();
    private final ArrayList<Integer> in = new ArrayList<>();
    private final ArrayList<Integer> out = new ArrayList<>();

    private final ArrayList<Character> alphabet = new ArrayList<>();

    public WGFromBWT(AlgorithmManager algorithmManager) {
        super(algorithmManager);
        this.algorithmManager = algorithmManager;

        this.startLabel = new Label();
        LanguageListenerAdder.addLanguageListener("inputText", this.startLabel);
        WindowManager.addController(this.startLabel, 0, 0);

        this.inputTextField = new TextField("abrakadabra");
        WindowManager.addController(this.inputTextField, 0,1);

        this.startButton = new Button();
        LanguageListenerAdder.addLanguageListener("start", this.startButton);
        WindowManager.addController(this.startButton, 0,2);

        this.fromTextField = true;
        this.startButton.setOnAction(actionEvent -> preStart(this.inputTextField.getText()));

        this.textDisplay.addString("WGFromBWTPreStart","", true);
    }

    public WGFromBWT(AlgorithmManager algorithmManager, String input){
        super(algorithmManager);
        this.algorithmManager = algorithmManager;
        this.fromTextField = false;
        preStart(input);
    }

    private void preStart(String input){

        this.input = (input.endsWith("$"))?input:input+"$";
        for(int i = 0;i<this.input.length();i++){
            if(!this.alphabet.contains(this.input.charAt(i))){
                this.alphabet.add(this.input.charAt(i));
            }
        }
        this.alphabet.sort(Comparator.naturalOrder());

        String bwt = Algorithms.bwt(this.input);
        for (int i = 0; i < bwt.length(); i++) {
            this.l.add(bwt.charAt(i));
        }
        if(this.fromTextField){
            WindowManager.removeController(this.startLabel);
            WindowManager.removeController(this.startButton);
            WindowManager.removeController(this.inputTextField);
        }
        start();
    }

    private void start(){
        this.textDisplay.clear();
        this.textDisplay.addString("WGFromBWTStart","", true);
        this.lV = Algorithms.bwt(this.input);
        String fV = Algorithms.f(this.input);
        this.lfV = Algorithms.lfMapping(lV);
        this.matrixDisplay.setMatrixSize(this.input.length() + 1, 4);
        this.matrixDisplay.setRowText(0, "i", "LF[i]", "L[i]");
        for(int i = 0;i<this.input.length();i++){
            this.matrixDisplay.setSquareText(i + 1, iColumn, i );
            this.matrixDisplay.setSquareText(i + 1, lfColumn, lfV[i]);
            //this.matrixDisplay.setSquareText(i + 1, fColumn, fV.charAt(i)); todo - add with LF mapping with arrows
            this.matrixDisplay.setSquareText(i + 1, lColumn, lV.charAt(i));
        }
        for(int i = 0; i<this.input.length();i++){
            DirectedVertex vertex = (DirectedVertex) this.graphDisplay.addVertex();
            vertex.setRelativePosition(0.4*Math.cos((2*Math.PI/this.input.length())*i) + 0.5,
                    0.4*Math.sin((2*Math.PI/this.input.length())*i) + 0.5);
            vertex.setValue(i);
            this.vertices.add(vertex);
        }
        super.addNextBackAnimateControls(0,1,0,0,0,2);
        super.backStepButton.setDisable(true);
    }

    private int step = 0;
    private int highestStep = 0;
    private int it = 0;
    private int endInt = -1;

    @Override
    protected void nextStep(boolean animate){
        this.textDisplay.clear();
        Animation animation = this.animationManager.getAnimation(step);
        if(highestStep == step) {
            this.matrixDisplay.unhighlightEverything(animation);
            this.matrixDisplay.clearMatrixArrows(animation);
            if (step == this.input.length()) {
                endInt = step;
            }
            else {
                int jt = lfV[it];
                this.matrixDisplay.highlightBackground(animation, it + 1, iColumn);
                this.matrixDisplay.highlightBackground(animation, it + 1, lfColumn);
                this.matrixDisplay.highlightBackground(animation, it + 1, lColumn);
                DirectedEdge edge = (DirectedEdge) this.graphDisplay.addEdge(this.vertices.get(it), this.vertices.get(jt));
                edge.setText(lV.charAt(it));
                edge.setColor(Colors.getColor(this.alphabet.indexOf(lV.charAt(it))));
                animation.addAnimatable(AnimationType.AppearAnimation, edge);
                this.stepManager.addTextDisplayText(step, this.textDisplay, "WGFromBWTAlgPart1", "", true);
                this.stepManager.addTextDisplayText(step, this.textDisplay, " "+it, "", false);
                this.stepManager.addTextDisplayText(step, this.textDisplay, "WGFromBWTAlgPart2", "", true);
                this.stepManager.addTextDisplayText(step, this.textDisplay, " "+jt, "", false);
                this.stepManager.addTextDisplayText(step, this.textDisplay, "WGFromBWTAlgPart3", "", true);
                this.stepManager.addTextDisplayText(step, this.textDisplay, " "+lV.charAt(it), "", false);
                it = jt;
            }
        }
        if(animate){
            this.animationManager.executeAnimation(step, true);
        }
        else{
            animation.setForward(true);
            animation.endAnimation();
        }
        this.stepManager.forwardStep(step);
        if(step == endInt){
            end();
        }
        step++;
        highestStep = Math.max(highestStep, step);
        super.backStepButton.setDisable(false);
    }

    @Override
    protected void backStep(boolean animate){
        this.textDisplay.clear();
        if(step > 0) {
            step--;
            this.stepManager.backStep(step);
            if (animate) {
                animationManager.executeAnimation(step, false);
            } else {
                animationManager.endAnimation(step, false);
            }
        }
        if(step == 0){
            super.backStepButton.setDisable(true);
        }
        super.nextStepButton.setDisable(false);
        this.changeButton.setDisable(true);
        this.inverseButton.setDisable(true);
        this.tunnelButton.setDisable(true);
    }

    private void end(){
        super.nextStepButton.setDisable(true);
        this.textDisplay.addString("WGFromBWTEnd", "", true);
        this.changeButton.setOnAction(actionEvent -> this.algorithmManager.changeAlgorithm(AlgorithmType.WGCreation, this.vertices));
        LanguageListenerAdder.addLanguageListener("transformGraph", changeButton);
        changeButton.setDisable(false);
        WindowManager.addController(changeButton, 1, 0);

        for (int i = 0; i < this.vertices.size() + 1; i++) {
            this.in.add(1);
            this.out.add(1);
        }
        this.inverseButton.setOnAction(actionEvent -> this.algorithmManager.changeAlgorithm(AlgorithmType.WGInverse,
                this.l, this.in, this.out, this.l.size()));
        LanguageListenerAdder.addLanguageListener("inverse", inverseButton);
        inverseButton.setDisable(false);
        WindowManager.addController(inverseButton, 1, 1);

        this.tunnelButton.setOnAction(actionEvent -> this.algorithmManager.changeAlgorithm(AlgorithmType.WGTunneling, this.vertices));
        LanguageListenerAdder.addLanguageListener("tunnelGraph", tunnelButton);
        tunnelButton.setDisable(false);
        WindowManager.addController(tunnelButton, 1, 2);
    }
}
