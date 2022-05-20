package com.example.demo2.algorithms.bwt;

import com.example.demo2.Step.StepManager;
import com.example.demo2.algorithmDisplays.*;
import com.example.demo2.algorithmDisplays.DisplayType;
import com.example.demo2.algorithmManager.AlgorithmManager;
import com.example.demo2.algorithmManager.AlgorithmType;
import com.example.demo2.algorithms.Algorithm;
import com.example.demo2.animations.Animation;
import com.example.demo2.animations.AnimationManager;
import com.example.demo2.multilingualism.LanguageListenerAdder;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class BWTDecodeAlgorithm extends Algorithm {

    private final AlgorithmManager algorithmManager;
    private final AnimationManager animationManager = new AnimationManager();
    private final StepManager stepManager = new StepManager();

    private final MatrixDisplay matrixDisplay = (MatrixDisplay) WindowManager.addDisplay(DisplayType.Matrix, "memory", 1);
    private final CodeDisplay codeDisplay = (CodeDisplay) WindowManager.addDisplay(DisplayType.Code, "pseudocode", 1);
    private final TextDisplay textDisplay = (TextDisplay) WindowManager.addDisplay(DisplayType.Text, "description", 1);

    private final static int cColumn = 0;
    private final static int ccColumn = 1;
    private final static int iColumn = 2;
    private final static int lfColumn = 3;
    private final static int fColumn = 4;
    private final static int lColumn = 5;
    private final static int sColumn = 6;

    private final Button retryDecode = new Button();
    private final Button bwtButton = new Button();

    private final TextField inputTextField = new TextField("annb$aa");
    private final Button startButton = new Button();
    private final Button jumpTo9 = new Button();
    private final Button jumpTo13 = new Button();

    private final ArrayList<Character> alphabet = new ArrayList<>();
    private String input;
    private String sortedInput;

    private final boolean withInputTextField;

    public BWTDecodeAlgorithm(AlgorithmManager algorithmManager) {
        super(algorithmManager);
        this.algorithmManager = algorithmManager;
        setCode();
        withInputTextField = true;

        WindowManager.addController(this.inputTextField, 0,0);

        LanguageListenerAdder.addLanguageListener("startAlgorithm", this.startButton);
        this.startButton.setOnAction(actionEvent -> preStart(this.inputTextField.getText()));
        WindowManager.addController(this.startButton, 0,1);
    }

    public BWTDecodeAlgorithm(AlgorithmManager algorithmManager, String input) {
        super(algorithmManager);
        this.algorithmManager = algorithmManager;
        withInputTextField = false;
        setCode();
        preStart(input);
    }

    private void setCode(){
        this.codeDisplay.addLine("C = [\u03c3]");
        this.codeDisplay.addLine("for i := 0 to n - 1 do");
        this.codeDisplay.addLine("    C[L[i]] := C[L[i]] + 1");
        this.codeDisplay.addLine("sum := 0");
        this.codeDisplay.addLine("for i := 0 to sigma - 1 do");
        this.codeDisplay.addLine("    tmp := C[i]");
        this.codeDisplay.addLine("    C[i] := sum");
        this.codeDisplay.addLine("    sum := sum + tmp");
        this.codeDisplay.addLine("LF = [n]");
        this.codeDisplay.addLine("for i := 0 to n - 1 do");
        this.codeDisplay.addLine("    LF[i] := C[L[i]]");
        this.codeDisplay.addLine("    C[L[i]] := C[L[i]] + 1");
        this.codeDisplay.addLine("S = [n]");
        this.codeDisplay.addLine("S[n - 1] := $");
        this.codeDisplay.addLine("j  := 0");
        this.codeDisplay.addLine("for i := n - 2 to 0 do");
        this.codeDisplay.addLine("    S[i] := L[j]");
        this.codeDisplay.addLine("    j := LF[j]");
    }

    private void preStart(String input){
        if(this.withInputTextField) {
            WindowManager.removeController(this.inputTextField);
            WindowManager.removeController(this.startButton);
        }
        this.input = input;
        start();
    }

    private void start(){
        this.matrixDisplay.setMatrixSize(input.length() + 1, 7);
        this.matrixDisplay.setRowText(0, "c", "", "i", "", "", "L[i]", "");

        for(int i = 0;i<this.input.length();i++){
            boolean containsCharacter = false;
            for (Character character : this.alphabet) {
                if (character == this.input.charAt(i)) {
                    containsCharacter = true;
                    break;
                }
            }
            if(!containsCharacter){
                this.alphabet.add(this.input.charAt(i));
            }
        }
        this.alphabet.sort(Comparator.naturalOrder());
        for(int i = 0;i<this.alphabet.size();i++){
            this.matrixDisplay.setSquareText(i + 1, cColumn, this.alphabet.get(i));
        }

        char [] charArrayInput = input.toCharArray();
        Arrays.sort(charArrayInput);

        StringBuilder stringBuilderSorted = new StringBuilder();
        for(int i = 0; i < this.input.length(); i++){
            this.matrixDisplay.setSquareText(i + 1, iColumn, i);
            this.matrixDisplay.setSquareText(i + 1, lColumn, this.input.charAt(i));
            stringBuilderSorted.append(charArrayInput[i]);
        }
        this.sortedInput = stringBuilderSorted.toString();

        this.firstTime = new ArrayList<>();
        for(int i = 0;i<19;i++){
            this.firstTime.add(true);
        }
        this.cV = new int[alphabet.size()];
        this.lfV = new int[input.length()];
        this.sV = new char[input.length()];

        super.addBasicControls(0, 0);
        super.backStepButton.setDisable(true);

        this.jumpTo9.setOnAction(actionEvent -> jumpTo9());
        LanguageListenerAdder.addLanguageListener("jumpTo9", this.jumpTo9);
        WindowManager.addController(this.jumpTo9, 1, 0);

        this.jumpTo13.setOnAction(actionEvent -> jumpTo13());
        LanguageListenerAdder.addLanguageListener("jumpTo13", this.jumpTo13);
        WindowManager.addController(this.jumpTo13, 1, 1);

    }

    int stepOfLine9 = Integer.MAX_VALUE;
    int stepOfLine13 = Integer.MAX_VALUE;

    private void setDisableStepButtons(boolean disable){
        super.backStepButton.setDisable(disable);
        super.nextStepButton.setDisable(disable);
        this.jumpTo13.setDisable(disable);
        this.jumpTo9.setDisable(disable);
    }

    private void jumpTo9(){
        setDisableStepButtons(true);
        while(step <= stepOfLine9){
            nextStep(true);
        }
        while(step > stepOfLine9){
            backStep(true);
        }
        setDisableStepButtons(false);
    }

    private void jumpTo13(){
        setDisableStepButtons(true);
        while(step <= stepOfLine13){
            nextStep(true);
        }
        while(step > stepOfLine13){
            backStep(true);
        }
        setDisableStepButtons(false);
    }


    private ArrayList<Boolean> firstTime;
    private int it, sum, tmp, jt;
    private int[] cV;
    private int[] lfV;
    private char[] sV;
    private int endInt = Integer.MAX_VALUE;
    private int currentLineNumber = 1;
    private int highestStep = 0, step = 0;

    @Override
    protected void nextStep(boolean animate){
        this.animationManager.endAllAnimations();
        Animation animation = this.animationManager.getAnimation(step);
        this.textDisplay.clear();

        if(highestStep == step){
            this.codeDisplay.unhighlightEverything(animation);
            this.matrixDisplay.unhighlightEverything(animation);
            this.matrixDisplay.clearMatrixArrows(animation);
            this.codeDisplay.highlightLine(animation, this.currentLineNumber);

            switch (this.currentLineNumber){
                case 1 -> {
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtDecodeAlg1", "", true);
                    this.stepManager.addSetSquareText(step, this.matrixDisplay, 0, ccColumn, "", "C[c]");
                    for(int i = 0;i<this.alphabet.size();i++){
                        this.matrixDisplay.setSquareText(i + 1, ccColumn, 0);
                        this.stepManager.addSetSquareText(step, this.matrixDisplay, i + 1, ccColumn, "","0" );
                    }
                    currentLineNumber = 2;
                }
                case 2 -> {//for i = 0; i<n-1
                    if(this.firstTime.get(2)){
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtDecodeAlg2First", "", true);
                        it = -1;
                        this.codeDisplay.addVariable(animation, "i", 0);
                        this.firstTime.set(2, false);
                        this.stepManager.addSetVariableValue(step, this.codeDisplay, "i", "", (it+1)+"");
                    }
                    else {
                        this.stepManager.addSetVariableValue(step, this.codeDisplay, "i", it + "", (it + 1) + "");
                    }
                    it++;
                    if(it == this.input.length()){
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtDecodeAlg2Negative", "", true);
                        this.currentLineNumber = 4;
                    }
                    else{
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtDecodeAlg2Positive", "", true);
                        this.matrixDisplay.highlightBackground(animation, it + 1, iColumn);
                        this.currentLineNumber = 3;
                    }
                }
                case 3 -> {//C[L[i]] = C[L[i]] + 1
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtDecodeAlg3", "", true);
                    this.matrixDisplay.highlightBackground(animation, it + 1, iColumn);
                    this.matrixDisplay.highlightBackground(animation, it + 1, lColumn);
                    for(int i = 0;i<this.alphabet.size();i++){
                        if(alphabet.get(i) == this.input.charAt(it)){
                            this.matrixDisplay.highlightBackground(animation, i + 1, ccColumn);
                            this.stepManager.addSetSquareText(step, this.matrixDisplay, i + 1, ccColumn,
                                    cV[i]+"", (cV[i]+1)+"");
                            cV[i] = cV[i] + 1;
                            this.matrixDisplay.highlightBackground(animation, i + 1, cColumn);
                        }
                    }
                    this.currentLineNumber = 2;
                }
                case 4 -> {
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtDecodeAlg4", "", true);
                    this.codeDisplay.removeVariable(animation, "i");
                    this.codeDisplay.addVariable(animation, "sum", 0);
                    sum = 0;
                    currentLineNumber = 5;
                }
                case 5 -> {
                    if(this.firstTime.get(5)){
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtDecodeAlg5First", "", true);
                        this.stepManager.addSetVariableValue(step, this.codeDisplay, "i", it + "", "0");
                        it = -1;
                        this.codeDisplay.addVariable(animation, "i", 0);
                        this.firstTime.set(5, false);
                    }
                    else {
                        this.stepManager.addSetVariableValue(step, this.codeDisplay, "i", it + "", (it + 1) + "");
                    }
                    it++;
                    if(it == this.alphabet.size()){
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtDecodeAlg5Negative", "", true);
                        this.currentLineNumber = 9;
                    }
                    else{
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtDecodeAlg5Positive", "", true);
                        this.matrixDisplay.highlightBackground(animation, it + 1, cColumn);
                        this.currentLineNumber = 6;
                    }
                    this.codeDisplay.removeVariable(animation, "tmp");
                }
                case 6 -> {
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtDecodeAlg6", "", true);
                    System.out.println(Arrays.toString(cV));
                    tmp = cV[it];
                    this.codeDisplay.addVariable(animation, "tmp", tmp);
                    this.stepManager.addSetVariableValue(step, this.codeDisplay, "tmp", "", tmp+"");
                    this.matrixDisplay.highlightBackground(animation, it + 1, cColumn);
                    this.matrixDisplay.highlightBackground(animation, it + 1, ccColumn);
                    this.currentLineNumber = 7;
                }
                case 7 -> {
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtDecodeAlg7", "", true);
                    this.stepManager.addSetSquareText(step, this.matrixDisplay, it + 1, ccColumn,
                            cV[it]+"", sum+"");
                    cV[it] = sum;
                    this.matrixDisplay.highlightBackground(animation, it + 1, cColumn);
                    this.matrixDisplay.highlightBackground(animation, it + 1, ccColumn);
                    this.currentLineNumber = 8;
                }
                case 8 -> {
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtDecodeAlg8", "", true);
                    this.codeDisplay.highlightVariable(animation, "sum");
                    this.stepManager.addSetVariableValue(step, this.codeDisplay, "sum", sum+"", (sum+tmp)+"");
                    sum = sum + tmp;
                    this.currentLineNumber = 5;
                }
                case 9 -> {
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtDecodeAlg9", "", true);
                    this.stepManager.addSetSquareText(step, this.matrixDisplay, 0, lfColumn, "", "LF[i]");
                    this.codeDisplay.removeVariable(animation, "i");
                    this.codeDisplay.removeVariable(animation, "sum");
                    this.currentLineNumber = 10;
                    this.stepOfLine9 = step;
                }
                case 10 -> {
                    if(this.firstTime.get(10)){
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtDecodeAlg10First", "", true);
                        this.stepManager.addSetVariableValue(step, this.codeDisplay, "i", it + "", "0");
                        it = -1;
                        this.codeDisplay.addVariable(animation, "i", 0);
                        this.firstTime.set(10, false);
                    }
                    else{
                        this.stepManager.addSetVariableValue(step, this.codeDisplay, "i", it + "", (it + 1) + "");
                    }
                    it++;
                    if(it == this.input.length()){
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtDecodeAlg10Negative", "", true);
                        this.currentLineNumber = 13;
                    }
                    else{
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtDecodeAlg10Positive", "", true);
                        this.matrixDisplay.highlightBackground(animation, it + 1, iColumn);
                        this.currentLineNumber = 11;
                    }
                }
                case 11 -> {//LF[i] = C[L[i]]
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtDecodeAlg11", "", true);
                    this.matrixDisplay.highlightBackground(animation, it + 1, lColumn);
                    this.matrixDisplay.highlightBackground(animation, it + 1, iColumn);
                    for(int i = 0;i<this.alphabet.size();i++){
                        if(alphabet.get(i) == this.input.charAt(it)){
                            this.matrixDisplay.highlightBackground(animation, i + 1, ccColumn);
                            this.matrixDisplay.highlightBackground(animation, i + 1, cColumn);
                            lfV[it] = cV[i];
                            this.stepManager.addSetSquareText(step, this.matrixDisplay, it + 1, lfColumn,
                                    "", lfV[it]+ "");
                            this.matrixDisplay.highlightBackground(animation, it + 1, lfColumn);
                        }
                    }
                    this.currentLineNumber = 12;
                }
                case 12 -> {//C[L[i]] = C[L[i]] + 1
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtDecodeAlg12", "", true);
                    this.matrixDisplay.highlightBackground(animation, it + 1, lColumn);
                    this.matrixDisplay.highlightBackground(animation, it + 1, iColumn);
                    for(int i = 0;i<this.alphabet.size();i++){
                        if(alphabet.get(i) == this.input.charAt(it)){
                            this.matrixDisplay.highlightBackground(animation, i + 1, ccColumn);
                            this.stepManager.addSetSquareText(step, this.matrixDisplay, i + 1, ccColumn,
                                    cV[i]+"", (cV[i]+1)+"");
                            cV[i] = cV[i] + 1;
                            this.matrixDisplay.highlightBackground(animation, i + 1, cColumn);
                        }
                    }
                    this.currentLineNumber = 10;
                }
                case 13 -> {
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtDecodeAlg13", "", true);
                    this.stepManager.addSetSquareText(step, this.matrixDisplay, 0, sColumn, "", "S[i]");
                    this.codeDisplay.removeVariable(animation,"i");
                    sV = new char[this.input.length()];
                    for(int i = 0;i<this.input.length();i++){
                        sV[i] = ' ';
                    }
                    this.currentLineNumber = 14;
                    this.stepOfLine13 = step;
                }
                case 14 -> {
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtDecodeAlg14", "", true);
                    sV[sV.length - 1] = '$';
                    this.stepManager.addSetSquareText(step, this.matrixDisplay, sV.length, sColumn, "", "$");
                    this.currentLineNumber = 15;
                }
                case 15 -> {
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtDecodeAlg15", "", true);
                    jt = 0;
                    this.codeDisplay.addVariable(animation, "j", 0);
                    this.currentLineNumber = 16;
                }
                case 16 -> {
                    if(this.firstTime.get(16)){
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtDecodeAlg16First", "", true);
                        it = this.input.length() - 1;
                        this.codeDisplay.addVariable(animation, "i", 0);
                        this.stepManager.addSetSquareText(step, this.matrixDisplay, 0, fColumn, "", "F[i]");
                        this.matrixDisplay.setSquareTextColor(0, fColumn, Color.GRAY);
                        for(int i = 0;i<this.sortedInput.length();i++){
                            this.stepManager.addSetSquareText(step, this.matrixDisplay, i + 1, fColumn,
                                    "", sortedInput.charAt(i)+"");
                            this.matrixDisplay.setSquareTextColor(i + 1, fColumn, Color.GRAY);
                        }
                        int[] occurrences = new int[this.alphabet.size()];
                        for(int i = 0;i<this.sortedInput.length();i++){
                            for(int j = 0 ;j<this.alphabet.size();j++){
                                if(this.alphabet.get(j) == this.sortedInput.charAt(i)){
                                    this.matrixDisplay.setSquareIndex(i + 1, fColumn, occurrences[j]);
                                    this.stepManager.addSetSquareIndex(step, this.matrixDisplay, i + 1, fColumn,
                                            "", occurrences[j] + "");
                                    occurrences[j]++;
                                }
                            }
                        }
                        occurrences = new int[this.alphabet.size()];
                        for(int i = 0;i<this.input.length();i++){
                            for(int j = 0 ;j<this.alphabet.size();j++){
                                if(this.alphabet.get(j) == this.input.charAt(i)){
                                    this.stepManager.addSetSquareIndex(step, this.matrixDisplay, i + 1, lColumn,
                                            "", occurrences[j] + "");
                                    occurrences[j]++;
                                }
                            }
                        }
                        this.firstTime.set(16, false);
                    }
                    it--;
                    this.stepManager.addSetVariableValue(step, this.codeDisplay, "i", (it+1)+"", it+"");
                    if(it == -1){
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtDecodeAlg16Negative", "", true);
                        this.currentLineNumber = 19;
                    }
                    else{
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtDecodeAlg16Positive", "", true);
                        this.currentLineNumber = 17;
                    }
                }
                case 17 -> {
                    //todo - Give variables more colors, to distinguish between j and i
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtDecodeAlg17", "", true);
                    sV[it] = input.charAt(jt);
                    this.stepManager.addSetSquareText(step, this.matrixDisplay, it + 1, sColumn, "", sV[it]+"");
                    this.matrixDisplay.highlightBackground(animation, it + 1, sColumn);
                    this.matrixDisplay.highlightBackground(animation, jt + 1, lColumn);
                    this.matrixDisplay.addMatrixArrow(animation, jt + 1, fColumn, jt + 1, lColumn);
                    this.currentLineNumber = 18;
                }
                case 18 -> {
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtDecodeAlg18", "", true);
                    this.stepManager.addSetVariableValue(step, this.codeDisplay, "j", jt, lfV[jt]);
                    this.matrixDisplay.highlightBackground(animation, jt + 1, lfColumn);
                    this.matrixDisplay.highlightBackground(animation, jt + 1, iColumn);
                    this.matrixDisplay.addMatrixArrow(animation, jt + 1, lColumn, lfV[jt] + 1, fColumn);
                    jt = lfV[jt];
                    this.currentLineNumber = 16;
                }
                case 19 -> {
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtDecodeAlgEnd1", "", true);
                    this.stepManager.addTextDisplayText(step, this.textDisplay, " L = "+this.input+" ", "", false);
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtDecodeAlgEnd2", "", true);
                    this.stepManager.addTextDisplayText(step, this.textDisplay, " S = "+Arrays.toString(sV), "", false);
                    endInt = step;
                    end();
                }
            }
        }

        animation.setForward(true);
        if(animate){
            animation.setSpeed(super.animateSpeedSlider.getValue());
            animationManager.executeAnimation(step, true);
        }
        else{
            animation.endAnimation();
        }


        this.stepManager.forwardStep(step);
        if(step >= endInt){
            end();
        }
        step++;
        highestStep = Math.max(highestStep, step);

        super.backStepButton.setDisable(false);
    }

    @Override
    protected void backStep(boolean animate){
        this.animationManager.endAllAnimations();
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
        retryDecode.setDisable(true);
        bwtButton.setDisable(true);
    }

    private void end(){
        super.nextStepButton.setDisable(true);
        this.retryDecode.setDisable(false);
        this.bwtButton.setDisable(false);

        this.retryDecode.setOnAction(actionEvent -> this.algorithmManager.changeAlgorithm(AlgorithmType.BWTDecode));
        LanguageListenerAdder.addLanguageListener("retry", this.retryDecode);
        WindowManager.addController(this.retryDecode, 2,0);

        //todo add change to encode bwt algorithm with given string

        this.bwtButton.setOnAction(actionEvent -> this.algorithmManager.changeAlgorithm(AlgorithmType.BWT));
        LanguageListenerAdder.addLanguageListener("returnToBWT", bwtButton);
        WindowManager.addController(this.bwtButton, 2,1);
    }
}
