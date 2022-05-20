package com.example.demo2.algorithms.bwt;

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
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Comparator;

public class BWTGeneralAlgorithm extends Algorithm {

    private final AlgorithmManager algorithmManager;
    private final TextDisplay textDisplay;
    private final MatrixDisplay matrixDisplay;

    private String input;
    private String output;
    private ArrayList<String> rotations;

    private final TextField inputTextField;
    private final Button startButton;
    private Button endButton;

    public BWTGeneralAlgorithm(AlgorithmManager algorithmManager) {
        super(algorithmManager);
        this.matrixDisplay = (MatrixDisplay) WindowManager.addDisplay(DisplayType.Matrix, "BWTMatrix", 1);
        this.textDisplay = (TextDisplay) WindowManager.addDisplay(DisplayType.Text, "algorithmDescription", 1);
        this.algorithmManager = algorithmManager;
        this.inputTextField = new TextField("abrakadabra");
        this.inputTextField.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            if(!newValue.contains("\\$")){
                this.inputTextField.setText(newValue.replaceAll("\\$", ""));
            }
        }));
        WindowManager.addController(this.inputTextField, 0,0);

        this.startButton = new Button();
        LanguageListenerAdder.addLanguageListener("start", this.startButton);
        this.startButton.setOnAction(actionEvent -> start(this.inputTextField.getText()));
        WindowManager.addController(this.startButton, 0,1);

        assert this.textDisplay != null;
        this.textDisplay.addString("inputTextForAlgorithm", "", true);
    }

    private void start(String input){
        this.input = input + '$';
        this.rotations = new ArrayList<>();
        this.matrixDisplay.setMatrixSize(this.input.length() + 1, this.input.length());

        StringBuilder start = new StringBuilder(this.input);
        StringBuilder end = new StringBuilder();
        for(int i = 0; i<this.input.length(); i++){
            this.rotations.add(String.valueOf(start) + end);
            for(int j = 0;j<this.input.length();j++){
                this.matrixDisplay.setSquareText(i + 1, j, this.rotations.get(i).charAt(j));
            }
            end.append(start.charAt(0));
            start.deleteCharAt(0);
        }
        this.textDisplay.clear();
        this.textDisplay.addString("BWTGeneralAlgorithmStart", "", true);

        WindowManager.removeController(this.startButton);
        WindowManager.removeController(this.inputTextField);

        this.endButton = new Button();
        LanguageListenerAdder.addLanguageListener("sort", this.endButton);
        this.endButton.setOnAction(actionEvent -> end());
        WindowManager.addController(this.endButton, 0, 0);
    }

    private void end(){
        this.matrixDisplay.setSquareText(0,0, "F");
        this.matrixDisplay.setSquareText(0, this.input.length() - 1, "L");
        this.matrixDisplay.setSquareTextColor(0,0, Color.BLUE);
        this.matrixDisplay.highlightSquare(0,0);
        this.matrixDisplay.setSquareTextColor(0, this.input.length() - 1, Color.RED);
        this.matrixDisplay.highlightSquare(0,this.input.length() - 1);

        this.rotations.sort(Comparator.naturalOrder());
        StringBuilder stringBuilderOutput = new StringBuilder();
        for(int i = 0; i<this.input.length(); i++){
            for(int j = 0;j<this.input.length();j++){
                this.matrixDisplay.setSquareText(i + 1, j, this.rotations.get(i).charAt(j));
            }
            this.matrixDisplay.setSquareTextColor(i + 1, 0, Color.BLUE);
            this.matrixDisplay.highlightSquare(i + 1, 0);
            this.matrixDisplay.setSquareTextColor(i + 1, this.input.length() - 1, Color.RED);
            this.matrixDisplay.highlightSquare(i + 1, this.input.length() - 1);
            stringBuilderOutput.append(this.rotations.get(i).charAt(input.length()-1));
        }
        WindowManager.removeController(this.endButton);
        this.output = stringBuilderOutput.toString();

        this.textDisplay.clear();
        this.textDisplay.addString("BWTGeneralAlgorithmEnd", "", true);
        this.textDisplay.addString(" L = "+this.output, "", false);

        Button retryButton = new Button();
        retryButton.setOnAction(actionEvent -> this.algorithmManager.changeAlgorithm(AlgorithmType.BWTEncode));
        LanguageListenerAdder.addLanguageListener("retry", retryButton);
        WindowManager.addController(retryButton, 0,0);

        Button decodeButton = new Button();
        decodeButton.setOnAction(actionEvent -> this.algorithmManager.changeAlgorithm(AlgorithmType.BWTDecode, this.output));
        LanguageListenerAdder.addLanguageListener("retransform", decodeButton);
        WindowManager.addController(decodeButton, 0,1);

        Button bwtButton = new Button();
        bwtButton.setOnAction(actionEvent -> this.algorithmManager.changeAlgorithm(AlgorithmType.BWT));
        LanguageListenerAdder.addLanguageListener("returnToBWT", bwtButton);
        WindowManager.addController(bwtButton, 0,2);

        Button wgButton = new Button();
        wgButton.setOnAction(actionEvent -> this.algorithmManager.changeAlgorithm(AlgorithmType.WGFromBWT, this.input));
        LanguageListenerAdder.addLanguageListener("connectionWithWG", wgButton);
        WindowManager.addController(wgButton, 1,0);
    }
}
