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

import java.util.ArrayList;
import java.util.Comparator;

public class BWTFromSAAlgorithm extends Algorithm {

    private final AlgorithmManager algorithmManager;
    private final AnimationManager animationManager = new AnimationManager();
    private final StepManager stepManager = new StepManager();

    private String input;
    private final StringBuilder output = new StringBuilder();

    private TextField inputTextField;
    private Button startButton;
    private final boolean fromTextField;
    private final Button retryButton = new Button();
    private final Button bwtButton = new Button();
    private final Button decodeButton = new Button();

    private final MatrixDisplay bwtDisplay = (MatrixDisplay) WindowManager.addDisplay(DisplayType.Matrix, "burrowsWheelerMatrix", 3);
    private final MatrixDisplay saDisplay = (MatrixDisplay) WindowManager.addDisplay(DisplayType.Matrix, "sa", 3);
    private final MatrixDisplay relationDisplay = (MatrixDisplay) WindowManager.addDisplay(DisplayType.Matrix, "memory", 2);
    private final CodeDisplay codeDisplay = (CodeDisplay) WindowManager.addDisplay(DisplayType.Code, "pseudocode", 3);
    private final TextDisplay textDisplay = (TextDisplay) WindowManager.addDisplay(DisplayType.Text, "description", 3);

    private static final int iColumn = 0;
    private static final int saColumn = 1;
    private static final int sColumn = 2;
    private static final int bwtColumn = 3;

    private int n;
    private char [] lV;
    private int [] saV;

    public BWTFromSAAlgorithm(AlgorithmManager algorithmManager) {
        super(algorithmManager);
        this.algorithmManager = algorithmManager;
        setCode();

        this.inputTextField = new TextField("abrakadabra");
        WindowManager.addController(this.inputTextField, 0,0);
        this.startButton = new Button();
        WindowManager.addController(this.startButton, 0,1);
        this.fromTextField = true;

        LanguageListenerAdder.addLanguageListener("startAlgorithm", this.startButton);
        this.startButton.setOnAction(actionEvent -> preStart(this.inputTextField.getText()));
    }

    public BWTFromSAAlgorithm(AlgorithmManager algorithmManager, String input){
        super(algorithmManager);
        this.algorithmManager = algorithmManager;
        setCode();

        this.fromTextField = false;
        preStart(input);
    }

    private void preStart(String input){
        if(this.fromTextField){
            WindowManager.removeController(this.startButton);
            WindowManager.removeController(this.inputTextField);
        }
        this.input = input.endsWith("$")?input:input+"$";
        this.n  = this.input.length();
        this.bwtDisplay.setMatrixSize(this.n + 2, this.n + 1);
        this.saDisplay.setMatrixSize(this.n + 2, this.n + 1);
        this.saDisplay.setSquareText(0,0, "SA");
        this.relationDisplay.setMatrixSize(this.n + 1, 4);
        this.relationDisplay.setSquareText(0, iColumn, "i");
        this.relationDisplay.setSquareText(0, saColumn , "SA[i]");
        this.relationDisplay.setSquareText(0, sColumn, "S");
        this.relationDisplay.setSquareText(0, bwtColumn,"BWT[i]");
        start();
    }

    private void start(){

        ArrayList<String> rotations = new ArrayList<>();
        this.saV = new int[this.n];
        this.lV = new char[this.n];

        StringBuilder start = new StringBuilder(this.input);
        StringBuilder end = new StringBuilder();
        for(int i = 0; i<this.input.length(); i++){
            rotations.add(String.valueOf(start) + end);
            end.append(start.charAt(0));
            start.deleteCharAt(0);
        }
        rotations.sort(Comparator.naturalOrder());

        this.bwtDisplay.setSquareText(0,0, "F");
        this.bwtDisplay.setSquareText(0, input.length() - 1, "L");
        for(int i = 0;i<this.input.length();i++){
            boolean suffix = true;
            for(int j= 0;j<this.input.length(); j++){
                if(suffix){
                    this.saDisplay.setSquareText(i + 1, j + 1, rotations.get(i).charAt(j));
                }
                if(rotations.get(i).charAt(j) == '$'){
                    this.saDisplay.setSquareText(i + 1, 0, (this.input.length() - j - 1));
                    this.saV[i] = (this.input.length() - j - 1);
                    this.relationDisplay.setSquareText(i + 1,saColumn ,(this.input.length() - j - 1));
                    suffix = false;
                }
                this.bwtDisplay.setSquareText(i + 1, j, rotations.get(i).charAt(j));
                this.relationDisplay.setSquareText(i + 1, iColumn, i);
                this.relationDisplay.setSquareText(i + 1, sColumn, this.input.charAt(i));
            }
        }
        super.addBasicControls(0, 0);
        super.backStepButton.setDisable(true);
    }

    private void setCode(){
        this.codeDisplay.addLine("for i := 0 to n - 1 do");
        this.codeDisplay.addLine("    if(SA[i] > 0)");
        this.codeDisplay.addLine("        L[i] := S[SA[i] - 1]");
        this.codeDisplay.addLine("    else");
        this.codeDisplay.addLine("        L[i] := $");
    }

    private int currentLineNumber = 1;
    private boolean first = true;
    private int it;
    private int highestStep = 0, step = 0;
    private int endInt = Integer.MAX_VALUE;

    @Override
    protected void nextStep(boolean animate){
        this.animationManager.endAllAnimations();
        Animation animation = this.animationManager.getAnimation(step);
        this.textDisplay.clear();

        if(highestStep == step) {
            this.codeDisplay.unhighlightEverything(animation);
            this.codeDisplay.highlightLine(animation, this.currentLineNumber);
            this.relationDisplay.unhighlightEverything(animation);
            this.saDisplay.unhighlightEverything(animation);
            this.bwtDisplay.unhighlightEverything(animation);

            switch (this.currentLineNumber) {

                case 1 -> {
                    if (first) {
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtFromSAAlg1First", "", true);
                        it = -1;
                        this.codeDisplay.addVariable(animation, "i", it);
                        first = false;
                        this.stepManager.addSetVariableValue(step, this.codeDisplay, "i", "", "0");
                    }
                    else{
                        this.stepManager.addSetVariableValue(step, this.codeDisplay, "i", (it-1)+"", it+"");
                    }
                    it++;
                    if (it == this.input.length()) {
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtFromSAAlg1Negative", "", true);
                        this.currentLineNumber = 6;
                    } else {
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtFromSAAlg1Positive", "", true);
                        this.currentLineNumber = 2;
                    }
                }
                case 2 -> {
                    this.relationDisplay.highlightBackground(animation, it + 1, iColumn);
                    this.relationDisplay.highlightBackground(animation, it + 1, saColumn);
                    if (saV[it] != 0) {
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtFromSAAlg2True", "", true);
                        this.currentLineNumber = 3;
                    } else {
                        this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtFromSAAlg2False", "", true);
                        this.currentLineNumber = 5;
                    }
                }
                case 3 -> {
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtFromSAAlg3", "", true);
                    lV[it] = input.charAt(saV[it] - 1);
                    this.relationDisplay.highlightBackground(animation, it + 1, iColumn);
                    this.relationDisplay.highlightBackground(animation, it + 1, saColumn);
                    this.relationDisplay.highlightBackground(animation, saV[it], sColumn);
                    this.stepManager.addSetSquareText(step, this.relationDisplay, it + 1, bwtColumn, "", lV[it]+"");
                    this.relationDisplay.highlightBackground(animation, it + 1, bwtColumn);
                    this.saDisplay.highlightBackground(animation, it + 1, 0);
                    for(int i = 0;i<this.input.length() - saV[it];i++){
                        this.saDisplay.highlightBackground(animation, it + 1, i + 1);
                    }
                    for(int i = 0;i<saV[it] + 1;i++){
                        this.bwtDisplay.highlightBackground(animation, it + 1, input.length() - 1 - i);
                    }
                    this.currentLineNumber = 1;
                }
                case 4 -> {
                }
                case 5 -> {
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtFromSAAlg5", "", true);
                    lV[it] = '$';
                    this.relationDisplay.highlightBackground(animation, it + 1, iColumn);
                    this.stepManager.addSetSquareText(step, this.relationDisplay, it+1, bwtColumn, "", lV[it]+"");
                    this.relationDisplay.highlightBackground(animation, it + 1, bwtColumn);
                    for(int i = 0;i<this.input.length() - saV[it];i++){
                        this.saDisplay.highlightBackground(animation, it + 1, i + 1);
                    }
                    for(int i = 0;i<saV[it] + 1;i++){
                        this.bwtDisplay.highlightBackground(animation, it + 1, input.length() - 1 - i);
                    }
                    this.currentLineNumber = 1;
                }
                case 6 -> {
                    this.stepManager.addTextDisplayText(step, this.textDisplay, "bwtFromSAAlgEnd", "", true);
                    for (char c : this.lV) {
                        output.append(c);
                    }
                    this.stepManager.addTextDisplayText(step, this.textDisplay, " BWT = "+output.toString(), "", false);
                    this.codeDisplay.removeVariable(animation, "i");
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
        retryButton.setDisable(true);
        bwtButton.setDisable(true);
        decodeButton.setDisable(true);
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
        retryButton.setDisable(true);
        bwtButton.setDisable(true);
        decodeButton.setDisable(true);
    }

    private void end(){
        super.nextStepButton.setDisable(true);
        retryButton.setDisable(false);
        bwtButton.setDisable(false);
        decodeButton.setDisable(false);

        retryButton.setOnAction(actionEvent -> this.algorithmManager.changeAlgorithm(AlgorithmType.BWTFromSA));
        LanguageListenerAdder.addLanguageListener("retry", retryButton);
        WindowManager.addController(retryButton,  1 ,0);

        bwtButton.setOnAction(actionEvent -> this.algorithmManager.changeAlgorithm(AlgorithmType.BWT));
        LanguageListenerAdder.addLanguageListener("returnToBWT", bwtButton);
        WindowManager.addController(bwtButton, 1,1);

        decodeButton.setOnAction(actionEvent -> this.algorithmManager.changeAlgorithm(AlgorithmType.BWTDecode, output.toString()));
        LanguageListenerAdder.addLanguageListener("decode", decodeButton);
        WindowManager.addController(decodeButton, 1,2);
    }
}
