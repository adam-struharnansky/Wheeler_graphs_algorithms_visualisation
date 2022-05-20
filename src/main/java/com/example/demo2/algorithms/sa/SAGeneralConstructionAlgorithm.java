package com.example.demo2.algorithms.sa;

import com.example.demo2.algorithmDisplays.WindowManager;
import com.example.demo2.algorithmDisplays.MatrixDisplay;
import com.example.demo2.algorithmDisplays.TextDisplay;
import com.example.demo2.algorithmDisplays.DisplayType;
import com.example.demo2.algorithmManager.AlgorithmManager;
import com.example.demo2.algorithmManager.AlgorithmType;
import com.example.demo2.algorithms.Algorithm;
import com.example.demo2.multilingualism.LanguageListenerAdder;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class SAGeneralConstructionAlgorithm extends Algorithm {

    private final AlgorithmManager algorithmManager;

    private final MatrixDisplay matrixDisplay = (MatrixDisplay) WindowManager.addDisplay(DisplayType.Matrix, "", 1);
    private final TextDisplay textDisplay = (TextDisplay) WindowManager.addDisplay(DisplayType.Text, "description", 1);

    private String input;
    private ArrayList<String> suffixes;

    private final TextField inputTextField = new TextField("abrakadabra");
    private final Button startButton = new Button();
    private final Button endButton = new Button();

    public SAGeneralConstructionAlgorithm(AlgorithmManager algorithmManager) {
        super(algorithmManager);
        this.algorithmManager = algorithmManager;

        this.inputTextField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if(!newValue.contains("\\$")){
                this.inputTextField.setText(newValue.replaceAll("\\$", ""));
            }
        }));
        WindowManager.addController(this.inputTextField, 0,0);

        this.startButton.setOnAction(actionEvent -> start(this.inputTextField.getText()));
        LanguageListenerAdder.addLanguageListener("start", this.startButton);
        WindowManager.addController(this.startButton, 0,1);

        assert this.textDisplay != null;
        this.textDisplay.addString("inputTextForAlgorithm", "", true);
    }

    private void start(String input){
        this.input = input + '$';
        this.suffixes = new ArrayList<>();
        this.matrixDisplay.setMatrixSize(this.input.length() + 1, 4);
        this.matrixDisplay.setSquareText(0,1, "i");
        this.matrixDisplay.setSquareText(0,2, "S[i, n - 1]");
        StringBuilder end = new StringBuilder();
        for(int i = this.input.length() - 1; i>=0; i--){
            end.insert(0, this.input.charAt(i));
            this.suffixes.add(end.toString());
        }
        for(int i = 0;i<this.input.length();i++){
            this.matrixDisplay.setSquareText(i + 1, 1, i);
            this.matrixDisplay.setSquareText(i + 1, 2, suffixes.get(i));
        }
        this.textDisplay.clear();
        this.textDisplay.addString("SAGeneralAlgorithmStart", "", true);

        WindowManager.removeController(this.startButton);
        WindowManager.removeController(this.inputTextField);

        LanguageListenerAdder.addLanguageListener("sort", this.endButton);
        this.endButton.setOnAction(actionEvent -> end());
        WindowManager.addController(this.endButton, 0, 0);
    }

    private void end(){

        int [] sa = new int[input.length()];
        int [] lcpArray = new int[input.length() - 1];
        this.matrixDisplay.setSquareText(0,0, "i");
        this.matrixDisplay.setSquareText(0,1, "LCP[i]");
        this.matrixDisplay.setSquareText(0,2, "SA[i]");
        this.matrixDisplay.setSquareText(0,3, "S[SA[i], n - 1]");
        this.suffixes.sort(Comparator.naturalOrder());
        for(int i = 0; i<this.input.length(); i++){
            this.matrixDisplay.setSquareText(i + 1, 0, i);
            if(i != 0){
                int lcp = 0;
                while(lcp < this.suffixes.get(i).length() && lcp < this.suffixes.get(i - 1).length()){
                    if(this.suffixes.get(i).charAt(lcp) == this.suffixes.get(i - 1).charAt(lcp)){
                        lcp++;
                    }
                    else{
                        break;
                    }
                }
                this.matrixDisplay.setSquareText(i + 1, 1, lcp);
                lcpArray[i - 1] = lcp;
            }
            else{
                this.matrixDisplay.setSquareText(i + 1, 1, "");
            }
            this.matrixDisplay.setSquareText(i + 1, 2, this.input.length() - this.suffixes.get(i).length());
            sa[i] = this.input.length() - this.suffixes.get(i).length();
            this.matrixDisplay.setSquareText(i + 1, 3, this.suffixes.get(i));
        }


        this.textDisplay.clear();
        this.textDisplay.addString("SAGeneralAlgorithmEnd1", "", true);
        this.textDisplay.addString(" SA = "+ Arrays.toString(sa)+". ", "", false);
        this.textDisplay.addString("SAGeneralAlgorithmEnd2", "", true);
        this.textDisplay.addString(" LCP = "+ Arrays.toString(lcpArray), "", false);
        WindowManager.removeController(this.endButton);

        Button retryButton = new Button();
        WindowManager.addController(retryButton, 0,0);
        LanguageListenerAdder.addLanguageListener("retry", retryButton);
        retryButton.setOnAction(actionEvent -> this.algorithmManager.changeAlgorithm(AlgorithmType.SAIntroduction));

        //todo: Add exact pattern matching algorithm using suffix array, and then add here connection with it
    }
}
