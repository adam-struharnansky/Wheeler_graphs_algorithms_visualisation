package com.example.demo2;

import com.example.demo2.algorithmManager.*;
import com.example.demo2.multilingualism.Languages;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class HelloController {

    @FXML
    private Pane algorithmController;

    @FXML
    private HBox algorithmDisplay;

    private AlgorithmManager algorithmManager;

    @FXML
    protected void changeLanguageToSlovak(){
        Languages.setLanguage("sk");
    }

    @FXML
    protected void changeLanguageToEnglish(){
        Languages.setLanguage("en");
    }

    private void setAlgorithm(AlgorithmType algorithmType){
        this.algorithmController.getChildren().clear();
        this.algorithmDisplay.getChildren().clear();
        if(this.algorithmManager == null){
            this.algorithmManager = new AlgorithmManager();
        }
        this.algorithmManager.changeAlgorithm(algorithmType);
    }

    @FXML
    protected void setAlgorithmToTest(){
        setAlgorithm(AlgorithmType.Test);
    }

    @FXML
    protected void setAlgorithmToBWT(){
        setAlgorithm(AlgorithmType.BWT);
    }

    @FXML
    protected void setAlgorithmToSA(){setAlgorithm(AlgorithmType.SAIntroduction);}

    @FXML
    protected void setAlgorithmToWG(){setAlgorithm(AlgorithmType.WG);}
}
