package com.example.demo2.algorithmDisplays;

import com.example.demo2.multilingualism.LanguageListenerAdder;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class WindowManager {

    private static double width = 0.0;
    private static double height = 0.0;

    private static Label algorithmNameLabel = null;
    private static HBox displaysHBox = null;
    private static final ArrayList<Display> displays = new ArrayList<>();

    private static Pane controllerPane = null;
    private static Pane algorithmNamePane = null;
    private final static int maxColumn = 10;
    private final static int maxRow = 4;
    private final static double controllerPanePadding = 12.0;
    private final static double controllerHeight = 200.0;
    private final static double namePaneHeight = 35.0;
    private final static double gapHeight = 65.0;

    private final static HashMap<Node, Pair<Integer, Integer>> nodePositionMap = new HashMap<>();
    private final static Node[][] positionNodeMap = new Node[maxRow][maxColumn];

    public static void changeWidth(double width){
        WindowManager.width = width;
        redrawDisplays();
        redrawControllers();
        redrawAlgorithmNameLabel();
    }

    public static void changeHeight(double height){
        //todo - do this somehow different, this does not look right
        displaysHBox.setMinHeight(height - controllerHeight - namePaneHeight - gapHeight);
        displaysHBox.setPrefHeight(height - controllerHeight - namePaneHeight - gapHeight);
        displaysHBox.setMaxHeight(height - controllerHeight - namePaneHeight - gapHeight);

        controllerPane.setMinHeight(controllerHeight);
        controllerPane.setPrefHeight(controllerHeight);
        controllerPane.setMaxHeight(controllerHeight);

        algorithmNamePane.setMinHeight(namePaneHeight);
        algorithmNamePane.setPrefHeight(namePaneHeight);
        algorithmNamePane.setMaxHeight(namePaneHeight);
        WindowManager.height = height;
        redrawDisplays();
        //controllerPane has fixed height, no need to redraw
    }

    public static void autosize(){
        changeWidth(width);
        changeHeight(height);
    }


    public static Display addDisplay(DisplayType displayType, String name, int sizeRatio){
        if(WindowManager.displaysHBox == null){
            return null;
        }
        VBox vBox = new VBox();
        switch (displayType) {
            case Matrix -> {
                displaysHBox.getChildren().add(vBox);
                displays.add(new MatrixDisplay(vBox, name, sizeRatio));
                redrawDisplays();
                return displays.get(displays.size() - 1);
            }
            case Text -> {
                displaysHBox.getChildren().add(vBox);
                displays.add(new TextDisplay(vBox, name, sizeRatio));
                redrawDisplays();
                return displays.get(displays.size() - 1);
            }
            case DirectedGraph-> {
                displaysHBox.getChildren().add(vBox);
                GraphDisplay graphDisplay = new GraphDisplay(vBox, name, sizeRatio, true);
                displays.add(graphDisplay);
                redrawDisplays();
                return graphDisplay;
            }
            case UndirectedGraph -> {
                displaysHBox.getChildren().add(vBox);
                GraphDisplay graphDisplay = new GraphDisplay(vBox, name, sizeRatio, false);
                displays.add(graphDisplay);
                redrawDisplays();
                return graphDisplay;
            }
            case Code -> {
                displaysHBox.getChildren().add(vBox);
                CodeDisplay codeDisplay= new CodeDisplay(vBox, name, sizeRatio);
                displays.add(codeDisplay);
                redrawDisplays();
                return codeDisplay;
            }
            case Selector -> {
                displaysHBox.getChildren().add(vBox);
                SelectorDisplay selectorDisplay = new SelectorDisplay(vBox, name, sizeRatio);
                displays.add(selectorDisplay);
                redrawDisplays();
                return selectorDisplay;
            }
            case Tree -> {
                displaysHBox.getChildren().add(vBox);
                TreeDisplay treeDisplay = new TreeDisplay(vBox, name, sizeRatio);
                displays.add(treeDisplay);
                redrawDisplays();
                return treeDisplay;
            }
            case SimpleDirectedGraph -> {
                displaysHBox.getChildren().add(vBox);
                SimpleGraphDisplay graphDisplay = new SimpleGraphDisplay(vBox, name, sizeRatio, true);
                displays.add(graphDisplay);
                redrawDisplays();
                return graphDisplay;
            }
            case SimpleUndirectedGraph -> {
                displaysHBox.getChildren().add(vBox);
                SimpleGraphDisplay graphDisplay = new SimpleGraphDisplay(vBox, name, sizeRatio, false);
                displays.add(graphDisplay);
                redrawDisplays();
                return graphDisplay;
            }
        }
        return null;
    }

    public static void addController(Node node, int row, int column){
        if(0 <= column && column < maxColumn && 0 <= row  && row < maxRow){
            if(positionNodeMap[row][column] == null && !nodePositionMap.containsKey(node)){
                positionNodeMap[row][column] = node;
                nodePositionMap.put(node, new Pair<>(row, column));
                controllerPane.getChildren().add(node);
                redrawControllers();
                node.layoutBoundsProperty().addListener((observableValue, bounds, t1) -> redrawControllers());
            }
        }
    }

    public static void removeController(Node node){
        if(nodePositionMap.containsKey(node)){
            positionNodeMap[nodePositionMap.get(node).getKey()][nodePositionMap.get(node).getValue()] = null;
            nodePositionMap.remove(node);
            controllerPane.getChildren().remove(node);
            node.layoutBoundsProperty().removeListener((observableValue, bounds, t1) -> redrawControllers());
            /*
            todo - This probably does not work, those observers should be probably stored, to by removed
            node.layoutBoundsProperty().addListener(new ChangeListener<Bounds>() {
                @Override
                public void changed(ObservableValue<? extends Bounds> observableValue, Bounds bounds, Bounds t1) {

                }
            });
             */
        }
    }

    public static void clearWindow(){
        displays.clear();
        displaysHBox.getChildren().clear();
        nodePositionMap.clear();
        for(int i = 0;i<maxRow;i++){
            for (int j = 0;j<maxColumn;j++){
                if(positionNodeMap[i][j] != null){
                    controllerPane.getChildren().remove(positionNodeMap[i][j]);
                    positionNodeMap[i][j] = null;
                }
            }
        }
    }

    public static void setAlgorithmName(String key){
        LanguageListenerAdder.addLanguageListener(key, WindowManager.algorithmNameLabel);
    }


    public static void setDisplayHBox(HBox hbox){
        if(WindowManager.displaysHBox == null) {
            WindowManager.displaysHBox = hbox;
        }
    }

    public static void setControllerPane(Pane pane){
        if(WindowManager.controllerPane == null){
            WindowManager.controllerPane = pane;
        }
    }

    public static void setAlgorithmNamePane(Pane pane){
        if(WindowManager.algorithmNameLabel == null){
            WindowManager.algorithmNamePane = pane;
            WindowManager.algorithmNameLabel = (Label) pane.getChildren().get(0);
            //todo - change font name
            WindowManager.algorithmNameLabel.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
            WindowManager.algorithmNameLabel.layoutBoundsProperty().addListener(
                    (observableValue, bounds, t1) -> redrawAlgorithmNameLabel());
        }
    }


    public static void redrawDisplays(){
        double usableWidth = (WindowManager.width == 0.0)? WindowManager.displaysHBox.getWidth(): WindowManager.width;
        double usableHeight = height - controllerHeight - namePaneHeight - gapHeight;
        double allRations = 0.0;
        for(Display display: displays){
            if(display.isMinimized()){
                usableWidth -= 20.0;
            }
            else{
                allRations += display.getRatio();
            }
        }
        for(Display display: displays){
            if(!display.isMinimized()){
                display.setSize((usableWidth/allRations)*display.getRatio(), usableHeight);
                display.resize();
            }
            else{
                display.setSize(20.0, usableHeight);
            }
        }
    }

    private static void redrawControllers(){
        double y = controllerPanePadding;//toto bude o hornom okraji
        for(int i = 0;i<maxRow;i++){
            ArrayList<Node> row = new ArrayList<>();
            double contentWidth = 0.0;
            double maxHeight = -1.0;
            for(int j = 0;j<maxColumn;j++){
                if(positionNodeMap[i][j] != null){
                    row.add(positionNodeMap[i][j]);
                    positionNodeMap[i][j].autosize();
                    contentWidth += positionNodeMap[i][j].getLayoutBounds().getWidth();
                    maxHeight = Math.max(maxHeight, positionNodeMap[i][j].getLayoutBounds().getHeight());
                }
            }
            contentWidth += (row.size() - 1)*controllerPanePadding;
            double x;
            if(width != 0.0){
                x = (width / 2) - (contentWidth / 2);
            }
            else {
                x = (controllerPane.getWidth() / 2) - (contentWidth / 2);
            }
            y += maxHeight/2;
            for(Node node:row){
                node.setLayoutY(y);
                node.setLayoutX(x);
                x += node.getLayoutBounds().getWidth() + controllerPanePadding;
            }
            y += maxHeight;
        }
    }

    private static void redrawAlgorithmNameLabel(){
        WindowManager.algorithmNameLabel.setLayoutX(WindowManager.width/2
                - WindowManager.algorithmNameLabel.getWidth()/2);
        WindowManager.algorithmNameLabel.setLayoutY(WindowManager.namePaneHeight/2
                - WindowManager.algorithmNameLabel.getHeight()/2);
    }
}
