package com.example.demo2.algorithms.bwt;

import com.example.demo2.Step.StepManager;
import com.example.demo2.algorithmDisplays.CodeDisplay;
import com.example.demo2.algorithmDisplays.WindowManager;
import com.example.demo2.algorithmDisplays.MatrixDisplay;
import com.example.demo2.algorithmDisplays.TextDisplay;
import com.example.demo2.algorithmDisplays.DisplayType;
import com.example.demo2.algorithmManager.AlgorithmManager;
import com.example.demo2.algorithmManager.AlgorithmType;
import com.example.demo2.algorithms.Algorithm;
import com.example.demo2.animations.Animation;
import com.example.demo2.animations.AnimationManager;
import com.example.demo2.auxiliary.Algorithms;
import com.example.demo2.multilingualism.LanguageListenerAdder;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Comparator;

public class BWTSearch extends Algorithm {

    private final AlgorithmManager algorithmManager;
    private final AnimationManager animationManager = new AnimationManager();
    private final StepManager stepManager = new StepManager();

    private final MatrixDisplay bwtDisplay = (MatrixDisplay) WindowManager.addDisplay(DisplayType.Matrix, "BWTMatrix", 3);
    private final MatrixDisplay matrixDisplay = (MatrixDisplay) WindowManager.addDisplay(DisplayType.Matrix, "memory", 1);
    private final CodeDisplay codeDisplay = (CodeDisplay) WindowManager.addDisplay(DisplayType.Code, "pseudocode", 2);
    private final TextDisplay textDisplay = (TextDisplay) WindowManager.addDisplay(DisplayType.Text, "description", 2);

    private static final int iColumn = 0;
    private static final int inputColumn = 1;
    private static final int saColumn = 2;

    private final Button retryButton = new Button();
    private final Button retryWithInput = new Button();
    private final Button bwtButton = new Button();

    private final Label inputLabel = new Label();
    private final TextField inputTextField = new TextField();
    private final Label patternLabel = new Label();
    private final TextField patternTextField = new TextField();
    private final Button startButton = new Button();

    private String input;
    private String pattern;
    private final ArrayList<Pair<Character, Integer>> c = new ArrayList<>();
    private char[] l;

    private final ArrayList<String> bwt = new ArrayList<>();
    private int[] suffixArray;

    public BWTSearch(AlgorithmManager algorithmManager) {
        super(algorithmManager);
        this.algorithmManager = algorithmManager;

        WindowManager.addController(this.inputLabel, 0,0);
        LanguageListenerAdder.addLanguageListener("string", this.inputLabel);

        this.inputTextField.setText("banana");
        WindowManager.addController(this.inputTextField, 0,1);
        this.inputTextField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if(!newValue.contains("\\$")){
                this.inputTextField.setText(newValue.replaceAll("\\$", ""));
            }
            if(patternTextField.getText().length() > newValue.length()){
                patternTextField.setText(patternTextField.getText().substring(0, newValue.length()));
            }
        }));

        WindowManager.addController(this.patternLabel, 1,0);
        LanguageListenerAdder.addLanguageListener("pattern", this.patternLabel);

        this.patternTextField.setText("na");
        WindowManager.addController(this.patternTextField, 1, 1);
        this.patternTextField.textProperty().addListener((observableValue, s, t1) -> {
            if(t1.length() > inputTextField.getText().length()){
                patternTextField.setText(t1.substring(0, inputTextField.getText().length()));
            }
        });

        WindowManager.addController(this.startButton, 2,0);
        LanguageListenerAdder.addLanguageListener("startAlgorithm", this.startButton);
        this.startButton.setOnAction(actionEvent -> preStart(this.inputTextField.getText(), this.patternTextField.getText()));

        setCode();
    }

    public BWTSearch(AlgorithmManager algorithmManager, String input){
        super(algorithmManager);
        this.algorithmManager = algorithmManager;

        WindowManager.addController(this.inputLabel, 0,0);
        LanguageListenerAdder.addLanguageListener("string", this.inputLabel);

        this.inputTextField.setText(input);
        this.inputTextField.setDisable(true);
        WindowManager.addController(this.inputTextField, 0,1);

        WindowManager.addController(this.patternLabel, 1,0);
        LanguageListenerAdder.addLanguageListener("pattern", this.patternLabel);

        this.patternTextField.setText("");
        this.patternTextField.textProperty().addListener((observableValue, s, t1) -> {
            if(t1.length() > inputTextField.getText().length()){
                patternTextField.setText(t1.substring(0, t1.length() - 1));
            }
        });

        WindowManager.addController(this.patternTextField, 1, 1);

        WindowManager.addController(this.startButton, 2,0);
        LanguageListenerAdder.addLanguageListener("startAlgorithm", this.startButton);
        this.startButton.setOnAction(actionEvent -> preStart(input, this.patternTextField.getText()));

        this.input = (input.endsWith("$"))? input : input + "$";
        WindowManager.autosize();
        setCode();
        setMatrices();
    }

    private void preStart(String input, String pattern){
        this.inputTextField.setDisable(true);
        this.patternTextField.setDisable(true);

        WindowManager.removeController(this.startButton);

        this.input = (input.endsWith("$"))? input : input + "$";
        this.pattern = pattern;

        setMatrices();
        start();
    }

    private void setMatrices(){
        //bwtDisplay:
        this.bwtDisplay.setMatrixSize(this.input.length() + 1, this.input.length() + 1);
        this.bwtDisplay.setSquareText(0,1, "F");
        this.bwtDisplay.setSquareText(0, input.length(), "L");

        StringBuilder start = new StringBuilder(this.input);
        StringBuilder end = new StringBuilder();
        for(int i = 0; i<this.input.length(); i++){
            this.bwt.add(String.valueOf(start) + end);
            end.append(start.charAt(0));
            start.deleteCharAt(0);
        }
        this.bwt.sort(Comparator.naturalOrder());
        for(int i = 0; i<this.input.length(); i++){
            this.bwtDisplay.setSquareText(i + 1, 0, i);
            for(int j = 0;j<this.input.length();j++){
                this.bwtDisplay.setSquareText(i + 1, j + 1, this.bwt.get(i).charAt(j));
                if(j != this.input.length() - 1){
                    this.bwtDisplay.setSquareTextColor(i + 1, j + 1, Color.GRAY);
                }
            }
        }
        //matrixDisplay:
        this.matrixDisplay.setMatrixSize(this.input.length() + 1, 3);
        this.matrixDisplay.setRowText(0, "i", "S[i]", "SA[i]");
        this.suffixArray = Algorithms.suffixArray(this.input);
        for(int i = 0;i<this.input.length();i++){
            this.matrixDisplay.setSquareText(i + 1, iColumn, i);
            this.matrixDisplay.setSquareText(i + 1, inputColumn, this.input.charAt(i));
            this.matrixDisplay.setSquareText(i + 1, saColumn, this.suffixArray[i]);
        }
    }

    private void setCode(){
        this.codeDisplay.addLine("top := 0");
        this.codeDisplay.addLine("bottom := n");
        this.codeDisplay.addLine("for i := m - 1 to 0 do");
        this.codeDisplay.addLine("    c := P[i]");
        this.codeDisplay.addLine("    top := L.C(c) + L.rank(c, top)");
        this.codeDisplay.addLine("    bottom := L.C(c) + L.rank(c, bottom)");
        this.codeDisplay.addLine("    if top >= bottom");
        this.codeDisplay.addLine("        return");
        this.codeDisplay.addLine("return [SA[top], SA[bottom-1]]");
    }

    private void start(){
        for(int i = 0;i<11;i++){
            firstTime[i] = true;
        }
        for(int i = 0;i<input.length();i++){
            boolean contains = false;
            for(Pair<Character, Integer> pair:this.c){
                if(pair.getKey() == input.charAt(i)){
                    contains = true;
                    break;
                }
            }
            if(!contains){
                c.add(new Pair<>(input.charAt(i), 0));
            }
        }
        c.sort(Comparator.comparing(Pair::getKey));
        int [] occurrenceL = new int[input.length()], occurrenceF = new int[input.length()];
        for(int i = 0;i<this.input.length();i++){
            for(int j = 0;j<this.c.size();j++){
                if(this.c.get(j).getKey() == this.bwt.get(i).charAt(0)){
                    occurrenceF[i] = this.c.get(j).getValue();
                    this.c.set(j, new Pair<>(this.c.get(j).getKey(), this.c.get(j).getValue() + 1));
                }
            }
        }
        for(int i = 0;i<c.size();i++){
            c.set(i, new Pair<>(c.get(i).getKey(), 0));
        }
        for(int i = 0;i<this.input.length();i++){
            for(int j = 0;j<this.c.size();j++){
                if(this.c.get(j).getKey() == this.bwt.get(i).charAt(input.length() - 1)){
                    occurrenceL[i] = this.c.get(j).getValue();
                    this.c.set(j, new Pair<>(this.c.get(j).getKey(), this.c.get(j).getValue() + 1));
                }
            }
        }
        for(int i = 0;i<c.size();i++){
            c.set(i, new Pair<>(c.get(i).getKey(), 0));
        }
        for(int i = 0;i<this.input.length();i++){
            this.bwtDisplay.setSquareIndex(i + 1, 1, occurrenceF[i]);
            this.bwtDisplay.setSquareIndex(i + 1, this.input.length(), occurrenceL[i]);
        }
        for(int i = 0;i<input.length();i++){
            for(int j = 0;j<this.c.size();j++){
                if(this.c.get(j).getKey() > this.input.charAt(i)){
                    this.c.set(j, new Pair<>(this.c.get(j).getKey(), this.c.get(j).getValue() + 1));
                }
            }
        }
        l = new char[input.length()];
        for(int i = 0;i<this.input.length();i++){
            l[i] = bwt.get(i).charAt(this.input.length() - 1);
        }
        this.textDisplay.addString("bwtSearchStart", "", true);
        super.addBasicControls(2,0);
        super.backStepButton.setDisable(true);
    }

    private final boolean [] firstTime = new boolean[11];
    private int step = 0;
    private int highestStep = 0;
    private int it;
    private int top;
    private int bottom;
    private char ct;
    private int endInt = -1;
    private int currentLineNumber = 1;

    @Override
    protected void nextStep(boolean animate){
        this.animationManager.endAllAnimations();
        Animation animation = this.animationManager.getAnimation(step);
        this.textDisplay.clear();

        if(highestStep == step) {
            this.matrixDisplay.unhighlightEverything(animation);

            this.codeDisplay.unhighlightEverything(animation);
            this.codeDisplay.highlightLine(animation, this.currentLineNumber);

            switch (this.currentLineNumber){
                case 1 ->{
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtSearchAlg1", "", true);
                    this.codeDisplay.addVariable(animation, "top", 0);
                    this.top = 0;
                    this.currentLineNumber = 2;
                }
                case 2 ->{
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtSearchAlg2", "", true);
                    this.bottom = input.length();
                    this.codeDisplay.addVariable(animation, "bottom", bottom);
                    this.currentLineNumber = 3;
                }
                case 3 ->{
                    if(this.firstTime[3]){
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtSearchAlg3First", "", true);
                        it = this.pattern.length();
                        this.codeDisplay.addVariable(animation,"i", (it-1));
                        this.stepManager.addSetVariableValue(step, this.codeDisplay, "i", "", (it-1)+"");
                        this.firstTime[3] = false;
                    }
                    else{
                        this.stepManager.addSetVariableValue(step, this.codeDisplay, "i", it+"", (it-1)+"");
                        this.codeDisplay.removeVariable(animation,"c");
                    }
                    it--;
                    if(it == -1){
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtSearchAlg3Negative", "", true);
                        this.currentLineNumber = 9;
                    }
                    else{
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtSearchAlg3Positive", "", true);
                        for(int i = top;i<bottom;i++){
                            this.bwtDisplay.highlightBackground(animation, i + 1, input.length());
                        }
                        this.currentLineNumber = 4;
                    }
                }
                case 4 ->{
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtSearchAlg4", "", true);
                    this.bwtDisplay.unhighlightEverything(animation);
                    this.codeDisplay.addVariable(animation, "c", this.pattern.charAt(it));
                    this.stepManager.addSetVariableValue(step, this.codeDisplay, "c", "", this.pattern.charAt(it)+"");
                    this.ct = this.pattern.charAt(it);
                    this.currentLineNumber = 5;
                }
                case 5 ->{
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtSearchAlg5", "", true);
                    int tmp = 0;
                    for(Pair<Character, Integer> pair:this.c){//c
                        if(pair.getKey() == ct){
                            tmp = pair.getValue();
                            break;
                        }
                    }
                    for(int i = 0; i< top; i++){//rank
                        if(l[i] == ct){
                            tmp++;
                        }
                    }
                    this.stepManager.addSetVariableValue(step, this.codeDisplay, "top", top+"", tmp+"" );
                    top = tmp;
                    this.codeDisplay.highlightVariable(animation, "top");
                    this.currentLineNumber = 6;
                }
                case 6 ->{
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtSearchAlg6", "", true);
                    int tmp = 0;
                    for(Pair<Character, Integer> pair:this.c){//c
                        if(pair.getKey() == ct){
                            tmp = pair.getValue();
                            break;
                        }
                    }
                    for(int i = 0; i< bottom; i++){//rank
                        if(l[i] == ct){
                            tmp++;
                        }
                    }
                    this.stepManager.addSetVariableValue(step, this.codeDisplay, "bottom", bottom+"", tmp+"");
                    bottom = tmp;
                    this.codeDisplay.highlightVariable(animation, "bottom");
                    this.currentLineNumber = 7;
                }
                case 7 ->{
                    for(int i = top;i<bottom;i++){
                        for(int j = 0;j<this.pattern.length() - it;j++){
                            this.bwtDisplay.highlightBackground(animation, i + 1, j + 1);
                        }
                    }
                    this.codeDisplay.highlightVariable(animation, "top");
                    this.codeDisplay.highlightVariable(animation, "bottom");
                    if(top >= bottom){
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtSearchAlg7Positive", "", true);
                        this.currentLineNumber = 8;
                    }
                    else{
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtSearchAlg7Negative", "", true);
                        this.currentLineNumber = 3;
                    }
                }
                case 8 ->{
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtSearchAlgEndNegative", "", true);
                    endInt = step;
                    end();
                }
                case 9 ->{
                    if(bottom - top > 1) {
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtSearchAlgEndPositivePl", "", true);
                    }
                    else{
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtSearchAlgEndPositiveSg", "", true);
                    }
                    for(int i = top;i<bottom;i++){
                        if(i == top) {
                            this.stepManager.addTextDisplayText(step, this.textDisplay, "" + suffixArray[i], "", false);
                        }
                        else {
                            this.stepManager.addTextDisplayText(step, this.textDisplay, ", " + suffixArray[i], "", false);
                        }
                    }
                    for(int i = top; i< bottom; i++){
                        this.matrixDisplay.highlightBackground(animation, i + 1, saColumn);
                    }
                    for(int i = top;i<bottom;i++){
                        for(int j = 0;j<this.pattern.length() - it - 1;j++){
                            this.bwtDisplay.highlightBackground(animation, i + 1, j + 1);
                        }
                    }
                    endInt = step;
                    end();
                }
            }
        }

        if(animate){
            animation.setSpeed(super.animateSpeedSlider.getValue());
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
                Animation animation = this.animationManager.getAnimation(step);
                animation.setSpeed(super.animateSpeedSlider.getValue());
                animationManager.executeAnimation(step, false);
            } else {
                animationManager.endAnimation(step, false);
            }
        }
        if(step == 0){
            super.backStepButton.setDisable(true);
        }
        super.nextStepButton.setDisable(false);
        retryButton.setDisable(true);
        retryWithInput.setDisable(true);
        bwtButton.setDisable(true);
    }

    private void end(){
        super.nextStepButton.setDisable(true);
        retryButton.setDisable(false);
        retryWithInput.setDisable(false);
        bwtButton.setDisable(false);

        retryButton.setOnAction(actionEvent -> this.algorithmManager.changeAlgorithm(AlgorithmType.BWTSearch));
        LanguageListenerAdder.addLanguageListener("retry", retryButton);
        WindowManager.addController(retryButton, 3,0);

        retryWithInput.setOnAction(actionEvent -> this.algorithmManager.changeAlgorithm(AlgorithmType.BWTSearch, this.input));
        LanguageListenerAdder.addLanguageListener("retryWithSameString", retryWithInput);
        WindowManager.addController(retryWithInput, 3,1);

        bwtButton.setOnAction(actionEvent -> this.algorithmManager.changeAlgorithm(AlgorithmType.BWT));
        LanguageListenerAdder.addLanguageListener("returnToBWT", bwtButton);
        WindowManager.addController(bwtButton, 3,2);
    }
}
