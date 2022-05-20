package com.example.demo2.algorithms.wg;

import com.example.demo2.Step.StepManager;
import com.example.demo2.algorithmDisplays.*;
import com.example.demo2.algorithmDisplays.animatableNodes.DirectedEdge;
import com.example.demo2.algorithmDisplays.animatableNodes.DirectedVertex;
import com.example.demo2.algorithmDisplays.animatableNodes.Edge;
import com.example.demo2.algorithmDisplays.animatableNodes.Vertex;
import com.example.demo2.algorithmManager.AlgorithmManager;
import com.example.demo2.algorithms.Algorithm;
import com.example.demo2.animations.Animation;
import com.example.demo2.animations.AnimationManager;
import com.example.demo2.auxiliary.Colors;
import com.example.demo2.auxiliary.WheelerGraphExamples;
import com.example.demo2.multilingualism.LanguageListenerAdder;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class WGBackwardSteps extends Algorithm {

    private final AlgorithmManager algorithmManager;
    private final AnimationManager animationManager = new AnimationManager();
    private final StepManager stepManager = new StepManager();

    private final GraphDisplay graphDisplay = (GraphDisplay) WindowManager.addDisplay(DisplayType.DirectedGraph, "", 2);
    private final MatrixDisplay matrixDisplay = (MatrixDisplay) WindowManager.addDisplay(DisplayType.Matrix, "", 1);
    private final CodeDisplay codeDisplay = (CodeDisplay) WindowManager.addDisplay(DisplayType.Code, "", 1);
    private final TextDisplay textDisplay = (TextDisplay) WindowManager.addDisplay(DisplayType.Text, "", 1);

    private final ArrayList<DirectedVertex> vertices = new ArrayList<>();
    private final ArrayList<DirectedEdge> edges = new ArrayList<>();

    private final static int exampleRow = 1;

    private final static int iColumn = 0;
    private final static int lColumn = 1;
    private final static int outColumn = 2;
    private final static int inColumn = 3;
    private final static int fColumn = 4;
    private final static int srColumn = 5;

    private final Button startButton = new Button();
    private final ArrayList<RadioButton> exampleRadioButtons = new ArrayList<>();
    private final Button setExampleGraph = new Button();

    public WGBackwardSteps(AlgorithmManager algorithmManager) {
        super(algorithmManager);
        this.algorithmManager = algorithmManager;

        setCode();

        this.startButton.setOnAction(actionEvent -> preStart());
        LanguageListenerAdder.addLanguageListener("startAlgorithm", this.startButton);
        WindowManager.addController(this.startButton, 0,0);
        this.startButton.setDisable(true);

        ToggleGroup examples = new ToggleGroup();

        for(int i = 0;i<4;i++) {
            RadioButton radioButton = new RadioButton();
            radioButton.setToggleGroup(examples);
            LanguageListenerAdder.addLanguageListener("example"+(i+1), radioButton);
            WindowManager.addController(radioButton, exampleRow, i);
            this.exampleRadioButtons.add(radioButton);
        }
        this.exampleRadioButtons.get(0).setText("abrakadabra$");
        this.exampleRadioButtons.get(1).setText("easypeasy$");
        this.exampleRadioButtons.get(2).setText("abcabc$");
        this.exampleRadioButtons.get(3).setText("gactacactacat$");


        this.setExampleGraph.setOnAction(actionEvent -> {
            for(int i = 0;i<this.exampleRadioButtons.size();i++){
                if(examples.getSelectedToggle() == this.exampleRadioButtons.get(i)){
                    this.in = WheelerGraphExamples.in(i);
                    this.out = WheelerGraphExamples.out(i);
                    this.l = WheelerGraphExamples.l(i);
                    this.length = WheelerGraphExamples.length(i);
                    this.f.clear();
                    this.f.addAll(l);
                    this.f.sort(Comparator.naturalOrder());
                    setAlphabet();
                    setMatrix();
                    setGraph();
                }
            }
        });
        LanguageListenerAdder.addLanguageListener("setGraphExample", this.setExampleGraph);
        WindowManager.addController(this.setExampleGraph, exampleRow, 5);
    }

    private ArrayList<Character> l = new ArrayList<>();
    private ArrayList<Integer> in = new ArrayList<>();
    private ArrayList<Integer> out = new ArrayList<>();
    private final ArrayList<Character> f = new ArrayList<>();
    private final ArrayList<Character> sr = new ArrayList<>();
    private final ArrayList<Character> alphabet = new ArrayList<>();
    private final ArrayList<Integer> c = new ArrayList<>();
    private int length;

    public WGBackwardSteps(AlgorithmManager algorithmManager, ArrayList<Character> l, ArrayList<Integer> in,
                           ArrayList<Integer> out, int length){
        super(algorithmManager);
        System.out.println("length " +length);
        this.algorithmManager = algorithmManager;
        this.length = length;
        this.in.addAll(in);
        this.out.addAll(out);
        this.l.addAll(l);
        this.f.addAll(l);
        this.f.sort(Comparator.naturalOrder());
        setAlphabet();
        setCode();
        start();
    }

    private void setMatrix(){
        this.matrixDisplay.setMatrixSize(Math.max(this.in.size() + 1, length + 1), 6);
        this.matrixDisplay.setRowText(0, "i", "L[i]", "out[i]", "in[i]", "F[i]", "SR[i]");
        this.matrixDisplay.setSquareTextColor(0, fColumn, Color.GRAY);
        for(int i = 0;i<this.in.size();i++){
            this.matrixDisplay.setSquareText(i + 1, iColumn, i);
            if(i<this.l.size()) {
                this.matrixDisplay.setSquareText(i + 1, lColumn, this.l.get(i));
            }
            this.matrixDisplay.setSquareText(i + 1, outColumn, this.out.get(i));
            this.matrixDisplay.setSquareText(i + 1, inColumn, this.in.get(i));
            if(i<this.f.size()) {
                this.matrixDisplay.setSquareText(i + 1, fColumn, this.f.get(i));
                this.matrixDisplay.setSquareTextColor(i + 1, fColumn, Color.GRAY);
            }
        }
    }

    private void setAlphabet(){
        this.alphabet.clear();
        for(Character character:this.f){
            if(!this.alphabet.contains(character)){
                this.alphabet.add(character);
                this.c.add(0);
            }
        }
        for(Character character:this.l){
            this.c.set(this.alphabet.indexOf(character), this.c.get(this.alphabet.indexOf(character)) + 1);
        }
        int sum = 0;
        for(int i = 0;i<this.c.size();i++){
            int tmp = c.get(i);
            c.set(i, sum);
            sum += tmp;
        }
    }

    private void setCode(){
        this.codeDisplay.addLine("i = L.select($, 0)");
        this.codeDisplay.addLine("offset = 0");
        this.codeDisplay.addLine("for(int k = 0;k<n;k++)");
        this.codeDisplay.addLine("    SR[k] = L[i]");
        this.codeDisplay.addLine("    i = L.C(L[i]) + L.rank(L[i], i)");
        this.codeDisplay.addLine("    nodeRank = in.rank(1, i + 1) - 1");
        this.codeDisplay.addLine("    if(in[i] = 0 || in[i + 1] = 0)");
        this.codeDisplay.addLine("        offset = i - in.select(1, nodeRank)");
        this.codeDisplay.addLine("    i = out.select(1, nodeRank)");
        this.codeDisplay.addLine("    if(out[i + 1] = 0)");
        this.codeDisplay.addLine("        i = i + offset");
        this.codeDisplay.addLine("        offset = 0");
    }

    private void setGraph(){
        this.edges.forEach(this.graphDisplay::removeEdge);
        this.vertices.forEach(this.graphDisplay::removeVertex);
        this.edges.clear();
        this.vertices.clear();

        int verticesNumber = -1;
        for (Integer integer : this.in) {
            verticesNumber += integer;
        }
        Random random = new Random();
        for(int i = 0;i<verticesNumber;i++){
            Vertex vertex = this.graphDisplay.addVertex();
            vertex.setValue(i);

            vertex.setRelativePosition(0.45 + 0.1*random.nextDouble(),0.45 + 0.1*random.nextDouble());
            this.vertices.add((DirectedVertex) vertex);
        }
        int [] occ = new int[this.alphabet.size()];

        for(int i = 0;i<this.l.size();i++){
            int from = -1;
            for(int j = 0;j<=i;j++){
                from +=this.out.get(j);
            }
            char label = l.get(i);
            int to = -1;
            int o = 0;
            for(int j = 0;j<this.in.size();j++){
                to += this.in.get(j);
                if(f.get(j) == label && o == occ[alphabet.indexOf(label)]){
                    break;
                }
                if(f.get(j) == label){
                    o++;
                }
            }
            DirectedEdge edge = (DirectedEdge) this.graphDisplay.addEdge(this.vertices.get(from), this.vertices.get(to));
            edge.setText(label);
            this.edges.add(edge);
            occ[alphabet.indexOf(label)]++;
        }
        this.graphDisplay.beautify(500);
        this.startButton.setDisable(false);
    }

    private void preStart(){
        WindowManager.removeController(this.startButton);
        WindowManager.removeController(this.setExampleGraph);
        this.exampleRadioButtons.forEach(WindowManager::removeController);
        start();
    }

    private void start(){
        setMatrix();
        setGraph();

        this.step = 0;
        this.highestStep = -1;
        this.currentLineNumber = 1;

        super.addBasicControls(1, 0);
        super.backStepButton.setDisable(true);
    }

    boolean firstTime3 = true;
    int currentLineNumber;
    int step, highestStep, endInt = -1;
    int iV, kV, offsetV, nodeRankV;

    private Vertex highlightedVertex = null;

    @Override
    protected void nextStep(boolean animate){
        Animation animation = this.animationManager.getAnimation(step);

        this.textDisplay.clear();
        if(step == highestStep) {
            this.codeDisplay.unhighlightEverything(animation);
            this.matrixDisplay.unhighlightBackgrounds(animation);
            this.graphDisplay.unhighlightEverything();
            if(this.highlightedVertex != null){
                this.stepManager.addSetHighlightVertex(step, this.highlightedVertex, false);
                this.highlightedVertex = null;
            }

            this.codeDisplay.highlightLine(animation, this.currentLineNumber);
            switch (this.currentLineNumber) {
                case 1 -> {
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "WGBackwardStepsAlg1", "", true);
                    iV = 0;
                    for(int i = 0;i<this.l.size();i++){
                        if(l.get(i) == '$'){
                            iV = i;
                            this.matrixDisplay.highlightBackground(animation, iV + 1, lColumn);
                            this.matrixDisplay.highlightBackground(animation, iV + 1, iColumn);
                            break;
                        }
                    }
                    this.codeDisplay.addVariable(animation, "i", iV);
                    this.currentLineNumber = 2;
                }
                case 2 -> {
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "WGBackwardStepsAlg2", "", true);
                    offsetV = 0;
                    this.codeDisplay.addVariable(animation, "offset", 0);
                    this.currentLineNumber = 3;
                }
                case 3 -> {
                    if(this.firstTime3){
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "WGBackwardStepsAlg3First", "", true);
                        kV = -1;
                        this.codeDisplay.addVariable(animation, "k", 0);
                        this.firstTime3 = false;
                        this.stepManager.addSetVariableValue(step, this.codeDisplay, "k", "", (kV+1)+"");
                    }
                    else {
                        this.stepManager.addSetVariableValue(step, this.codeDisplay, "k", kV + "", (kV + 1) + "");
                        this.codeDisplay.removeVariable(animation, "nodeRank");
                    }
                    kV++;
                    if(kV == this.length){
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "WGBackwardStepsAlg3Negative", "", true);
                        this.currentLineNumber = 13;
                    }
                    else{
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "WGBackwardStepsAlg3Positive", "", true);
                        this.currentLineNumber = 4;
                    }
                }
                case 4 -> {
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "WGBackwardStepsAlg4", "", true);
                    this.sr.add(l.get(iV));
                    int from = -1;
                    for(int i = 0;i<iV + 1;i++){
                        from += out.get(i);
                    }
                    for(Edge edge:this.edges){
                        if(edge.getText().charAt(0) == l.get(iV) && Integer.parseInt(edge.getVertexFrom().getValue()) == from){
                            this.stepManager.addWay(step, edge, kV + 2, Colors.getTransitionColor(kV, this.length));
                        }
                    }
                    this.matrixDisplay.highlightBackground(animation, iV + 1, lColumn);
                    this.stepManager.addSetSquareText(step, this.matrixDisplay, kV + 1, srColumn, "", l.get(iV)+"");
                    this.currentLineNumber = 5;
                }
                case 5 -> {
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "WGBackwardStepsAlg5", "", true);
                    int tmp = 0;
                    for(int i = 0;i<iV;i++){//rank
                        if(l.get(i) == l.get(iV)){
                            tmp++;
                        }
                    }
                    tmp += c.get(alphabet.indexOf(l.get(iV)));
                    this.stepManager.addSetVariableValue(step, this.codeDisplay, "i", iV+"", tmp+"");
                    this.matrixDisplay.highlightBackground(animation, tmp + 1, fColumn);
                    iV = tmp;
                    this.currentLineNumber = 6;
                }
                case 6 -> {
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "WGBackwardStepsAlg6", "", true);
                    int tmp = -1;
                    for(int i = 0;i<iV + 1;i++){
                        tmp += in.get(i);
                    }
                    System.out.println("tmp "+tmp);
                    this.stepManager.addSetHighlightVertex(step, this.vertices.get(tmp), true);
                    this.highlightedVertex = this.vertices.get(tmp);
                    this.codeDisplay.addVariable(animation, "nodeRank", tmp+"");
                    this.stepManager.addSetVariableValue(step, this.codeDisplay, "nodeRank", "", tmp+"");
                    nodeRankV = tmp;
                    this.currentLineNumber = 7;
                }
                case 7 -> {
                    this.matrixDisplay.highlightBackground(animation, iV + 1, inColumn);
                    this.matrixDisplay.highlightBackground(animation, iV + 2, inColumn);
                    if(in.get(iV) == 0 || in.get(iV + 1) == 0){
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "WGBackwardStepsAlg7Positive", "", true);
                        this.currentLineNumber = 8;
                    }
                    else{
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "WGBackwardStepsAlg7Negative", "", true);
                        this.currentLineNumber = 9;
                    }
                }
                case 8 -> {
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "WGBackwardStepsAlg8", "", true);
                    int select = 0;
                    int occ = -1;
                    for(int i = 0;i<this.in.size();i++){
                        occ += this.in.get(i);
                        if(occ == nodeRankV){
                            select = i;
                            break;
                        }
                    }
                    //todo - zvyraznit, kde sa nachadza , povedat, ze co to znamena mat offset
                    this.stepManager.addSetVariableValue(step, this.codeDisplay, "offset", offsetV+"", (iV - select)+"");
                    this.offsetV = iV - select;
                    this.currentLineNumber = 9;
                }
                case 9 -> {
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "WGBackwardStepsAlg9", "", true);
                    int select = 0;
                    int occ = -1;
                    for(int i = 0;i<this.out.size();i++){
                        occ += this.out.get(i);
                        if(occ == nodeRankV){
                            select = i;
                            break;
                        }
                    }
                    this.stepManager.addSetVariableValue(step, this.codeDisplay, "i", iV+"", select+"");
                    this.iV = select;
                    this.currentLineNumber = 10;
                }
                case 10 -> {
                    this.matrixDisplay.highlightBackground(animation, iV + 2, outColumn);
                    if(out.get(iV + 1) == 0){
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "WGBackwardStepsAlg10Positive", "", true);
                        this.currentLineNumber = 11;
                    }
                    else{
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "WGBackwardStepsAlg10Negative", "", true);
                        this.currentLineNumber = 3;
                    }
                }
                case 11 -> {
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "WGBackwardStepsAlg11", "", true);
                    this.stepManager.addSetVariableValue(step, this.codeDisplay, "i", iV+"", (iV + offsetV)+"");
                    iV += offsetV;
                    this.currentLineNumber = 12;
                }
                case 12 -> {
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "WGBackwardStepsAlg12", "", true);
                    this.stepManager.addSetVariableValue(step, this.codeDisplay, "offset", offsetV+"", "0");
                    offsetV = 0;
                    this.currentLineNumber = 3;
                }
                case 13 -> {
                    this.endInt = step;
                }
            }
        }

        if(animate){
            animationManager.executeAnimation(step, true);
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
    }


    private void  end(){
        //todo - pridat text mozno nejaky
        super.nextStepButton.setDisable(true);
        //todo presunut sa na creation
    }
}
