package com.example.demo2.algorithms.wg;

import com.example.demo2.algorithmDisplays.*;
import com.example.demo2.algorithmDisplays.animatableNodes.*;
import com.example.demo2.algorithmDisplays.DisplayType;
import com.example.demo2.algorithmManager.AlgorithmManager;
import com.example.demo2.algorithmManager.AlgorithmType;
import com.example.demo2.algorithms.Algorithm;
import com.example.demo2.animations.Animation;
import com.example.demo2.animations.AnimationType;
import com.example.demo2.auxiliary.Colors;
import com.example.demo2.multilingualism.LanguageListenerAdder;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class WGCreation extends Algorithm {

    private final GraphDisplay graphDisplay
            = (GraphDisplay) WindowManager.addDisplay(DisplayType.DirectedGraph, "graph", 5);;
    private final MatrixDisplay matrixDisplay
            = (MatrixDisplay) WindowManager.addDisplay(DisplayType.Matrix, "succinctRepresentation", 2);
    private final SimpleGraphDisplay doubleGraph
            = (SimpleGraphDisplay) WindowManager.addDisplay(DisplayType.SimpleDirectedGraph, "", 2);
    private final TextDisplay textDisplay
            = (TextDisplay) WindowManager.addDisplay(DisplayType.Text, "definition", 1);

    private final ArrayList<DirectedVertex> vertices = new ArrayList<>();
    private final ArrayList<Edge> edges = new ArrayList<>();

    private final ArrayList<SimpleVertex> simpleVerticesFrom = new ArrayList<>();
    private final ArrayList<SimpleVertex> simpleVerticesTo = new ArrayList<>();
    private final ArrayList<SimpleDirectedEdge> doubleGraphEdges = new ArrayList<>();
    private double doubleGraphVertexRadius = 15.0;

    private final Animation animation = new Animation();

    private final ArrayList<String> edgesValues = new ArrayList<>();

    public WGCreation(AlgorithmManager algorithmManager) {
        super(algorithmManager);
        addButtons(algorithmManager);
    }

    public WGCreation(AlgorithmManager algorithmManager, ArrayList<DirectedVertex> vertices){
        super(algorithmManager);

        for(int i = 0;i<vertices.size();i++){
            addVertex(false);
        }
        for(DirectedVertex oldVertex:vertices){
            for(Edge edge:oldVertex.getOutgoing()){
                addEdge(edge.getText(), edge.getVertexFrom().getValue(), edge.getVertexTo().getValue(), false);
            }
        }
        for(int i = 0;i<vertices.size();i++){
            this.vertices.get(i).setRelativePosition(vertices.get(i).getRelativePosition());
        }
        rewriteSuccinctRepresentation();
        addButtons(algorithmManager);
    }

    private void addButtons(AlgorithmManager algorithmManager){
        int addVertexRow = 0, addEdgeRow = 1, changeAlgorithmRow = 2;

        CheckBox animateCheckBox = new CheckBox();
        animateCheckBox.setSelected(true);
        LanguageListenerAdder.addLanguageListener("animate", animateCheckBox);
        WindowManager.addController(animateCheckBox, addVertexRow, 2);

        Label vertexFromLabel = new Label();
        LanguageListenerAdder.addLanguageListener("vertexFrom", vertexFromLabel);
        WindowManager.addController(vertexFromLabel, addEdgeRow,0);
        Label vertexToLabel = new Label();
        LanguageListenerAdder.addLanguageListener("vertexTo", vertexToLabel);
        WindowManager.addController(vertexToLabel, addEdgeRow, 2);
        Label edgeValueLabel = new Label();
        LanguageListenerAdder.addLanguageListener("edgeValue", edgeValueLabel);
        WindowManager.addController(edgeValueLabel, addEdgeRow, 4);

        TextField edgeFromTextField = new TextField();
        edgeFromTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                edgeFromTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        WindowManager.addController(edgeFromTextField, addEdgeRow, 1);
        TextField edgeToTextField = new TextField();
        edgeToTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                edgeToTextField.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        WindowManager.addController(edgeToTextField, addEdgeRow, 3);
        TextField edgeValueTextField = new TextField();
        WindowManager.addController(edgeValueTextField, addEdgeRow, 5);

        Button addVertexButton = new Button();
        addVertexButton.setOnAction(actionEvent -> addVertex(animateCheckBox.isSelected()));
        LanguageListenerAdder.addLanguageListener("addVertex", addVertexButton);
        WindowManager.addController(addVertexButton, addVertexRow, 1);

        Button addEdgeButton = new Button();
        addEdgeButton.setOnAction(actionEvent ->
                addEdge(edgeValueTextField.getText(), edgeFromTextField.getText(),edgeToTextField.getText(), animateCheckBox.isSelected()));
        LanguageListenerAdder.addLanguageListener("addEdge", addEdgeButton);
        WindowManager.addController(addEdgeButton, addEdgeRow, 6);

        Button checkButton = new Button();
        checkButton.setOnAction(actionEvent -> checkWheelerGraph());
        LanguageListenerAdder.addLanguageListener("checkWG", checkButton);
        WindowManager.addController(checkButton, changeAlgorithmRow, 0);

        Button returnButton = new Button();
        returnButton.setOnAction(actionEvent -> algorithmManager.changeAlgorithm(AlgorithmType.WG));
        LanguageListenerAdder.addLanguageListener("returnToWG", returnButton);
        WindowManager.addController(returnButton, changeAlgorithmRow, 1);

        Button searchButton = new Button();
        searchButton.setOnAction(actionEvent -> {
            if(checkWheelerGraph()) {
                algorithmManager.changeAlgorithm(AlgorithmType.WGSearch);
            }
        });
        LanguageListenerAdder.addLanguageListener("searchInGraph", searchButton);
        WindowManager.addController(searchButton, changeAlgorithmRow, 2);

        Button tunnelingButton = new Button();
        tunnelingButton.setOnAction(actionEvent -> {
            if(checkWheelerGraph()) {
                algorithmManager.changeAlgorithm(AlgorithmType.WGTunneling);
            }
        });
        LanguageListenerAdder.addLanguageListener("tunnelGraph", tunnelingButton);
        WindowManager.addController(tunnelingButton, changeAlgorithmRow, 3);
    }

    public void rewriteSuccinctRepresentation(){
        this.matrixDisplay.clearMatrixTexts();
        this.matrixDisplay.setMatrixSize(this.edges.size()+2, 5);
        boolean correctForm = true;
        for(DirectedVertex vertex:this.vertices){
            if(vertex.getIncoming().isEmpty() || vertex.getOutgoing().isEmpty()){
                correctForm = false;
                break;
            }
        }
        if(correctForm){
            this.matrixDisplay.setRowText(0, "i", "L[i]", "out[i]", "in[i]", "F[i]");
            this.matrixDisplay.setSquareTextColor(0, 4, Color.GRAY);
            StringBuilder l = new StringBuilder();
            StringBuilder out = new StringBuilder();
            StringBuilder in = new StringBuilder();
            StringBuilder f = new StringBuilder();
            for(int i = 0;i<this.vertices.size();i++){
                for(DirectedVertex vertex:this.vertices){
                    if(vertex.getValue().compareTo(i+"") == 0){
                        out.append(1);
                        in.append(1);
                        for(int j = 0;j<vertex.getOutgoing().size();j++){
                            l.append(vertex.getOutgoing().get(j).getText());
                            if(j>=1){
                                out.append(0);
                            }
                        }
                        for(int j = 0;j<vertex.getIncoming().size();j++){
                            f.append(vertex.getIncoming().get(j).getText());
                            if(j>=1){
                                in.append(0);
                            }
                        }
                    }
                }
            }
            String lString = l.toString();
            String outString = out.toString();
            String inString = in.toString();
            String fString = f.toString();
            System.out.println(l);
            System.out.println(out);
            System.out.println(in);
            System.out.println(f);
            for(int i = 0;i<this.edges.size();i++){
                this.matrixDisplay.setSquareText(i + 1, 0, i);
                this.matrixDisplay.setSquareText(i + 1, 1, lString.charAt(i));
                this.matrixDisplay.setSquareText(i + 1, 2, outString.charAt(i));
                this.matrixDisplay.setSquareText(i + 1, 3, inString.charAt(i));
                this.matrixDisplay.setSquareText(i + 1, 4, fString.charAt(i));
                this.matrixDisplay.setSquareTextColor(i + 1, 4, Color.GRAY);

            }
            this.matrixDisplay.setSquareText(this.edges.size() + 1, 2, "1");
            this.matrixDisplay.setSquareText(this.edges.size() + 1, 3, "1");
        }
        else{
            //todo - neda sa to zobrazit
            System.out.println("neda sa");
        }
        System.out.println();
        System.out.println("-------");
    }

    public void addVertex(boolean animate){
        this.animation.endAnimation();
        this.animation.clear();
        for(Vertex vertex:this.vertices){
            if(vertex.getValue().compareTo(vertices.size()+"") == 0){//This should never happen
                this.animation.addAnimatable(AnimationType.ColorAnimation, vertex, Color.RED, Color.AQUA);
                this.animation.startAnimation();
                return;
            }
        }
        DirectedVertex vertex = (DirectedVertex) this.graphDisplay.addVertex();
        vertex.setValue(vertices.size());
        Random random = new Random();
        vertex.setRelativePosition(0.5 + random.nextDouble()/8,0.5 + random.nextDouble()/8);
        this.vertices.add(vertex);
        this.animation.addAnimatable(AnimationType.AppearAnimation, vertex);

        this.graphDisplay.beautify(animation);

        SimpleVertex simpleVertexFrom = this.doubleGraph.addSimpleVertex();
        simpleVertexFrom.setValue(simpleVerticesFrom.size());
        simpleVertexFrom.setRelativePosition(0.1,0.9);
        simpleVertexFrom.setRadius(this.doubleGraphVertexRadius);
        this.animation.addAnimatable(AnimationType.AppearAnimation, simpleVertexFrom);
        this.simpleVerticesFrom.add(simpleVertexFrom);

        SimpleVertex simpleVertexTo = this.doubleGraph.addSimpleVertex();
        simpleVertexTo.setValue(simpleVerticesTo.size());
        simpleVertexTo.setRelativePosition(0.9,0.9);
        simpleVertexTo.setRadius(this.doubleGraphVertexRadius);
        this.animation.addAnimatable(AnimationType.AppearAnimation, simpleVertexTo);
        this.simpleVerticesTo.add(simpleVertexTo);

        double newDoubleGraphVertexRadius = this.doubleGraphVertexRadius;
        if(this.doubleGraph.getHeight()*0.8 < this.simpleVerticesFrom.size()*this.doubleGraphVertexRadius){
            newDoubleGraphVertexRadius = this.doubleGraphVertexRadius*0.9;
        }
        newDoubleGraphVertexRadius = Math.max(newDoubleGraphVertexRadius, 7.0);

        for(int i = 0;i<this.simpleVerticesFrom.size();i++){
            if(newDoubleGraphVertexRadius != this.doubleGraphVertexRadius){
                this.simpleVerticesFrom.get(i).setRadius(newDoubleGraphVertexRadius);
                this.simpleVerticesTo.get(i).setRadius(newDoubleGraphVertexRadius);
            }
            animation.addAnimatable(AnimationType.RelativeMoveAnimation, this.simpleVerticesFrom.get(i), 0.1, 0.08 + i*(1.0/(this.simpleVerticesFrom.size() + 1)));
            animation.addAnimatable(AnimationType.RelativeMoveAnimation, this.simpleVerticesTo.get(i), 0.9, 0.08 + i*(1.0/(this.simpleVerticesFrom.size() + 1)));
        }
        this.doubleGraphVertexRadius = newDoubleGraphVertexRadius;

        if(animate) {
            this.animation.startAnimation();
        }
        else {
            this.animation.endAnimation();
        }


        rewriteSuccinctRepresentation();
    }

    public void addEdge(String value, String from, String to, boolean animate){
        this.animation.endAnimation();
        this.animation.clear();
        Vertex vertexFrom = null, vertexTo = null;
        for(Vertex vertex:this.vertices){
            if(vertex.getValue().compareTo(from) == 0){
                vertexFrom = vertex;
            }
            if(vertex.getValue().compareTo(to) == 0){
                vertexTo = vertex;
            }
        }
        if(vertexFrom != null && vertexTo != null){

            Edge edge = graphDisplay.addEdge(vertexFrom, vertexTo);
            edge.setText(value);
            this.edges.add(edge);
            this.animation.addAnimatable(AnimationType.AppearAnimation, edge);
            //this.graphDisplay.beautify(animation);

            //This should be safe, because the doubleGraph has same vertices
            SimpleDirectedEdge simpleEdge = (SimpleDirectedEdge) doubleGraph.addSimpleEdge(
                    this.simpleVerticesFrom.get(Integer.parseInt(from)), this.simpleVerticesTo.get(Integer.parseInt(to)));
            this.doubleGraphEdges.add(simpleEdge);
            this.animation.addAnimatable(AnimationType.AppearAnimation, simpleEdge);

            boolean containedValue = false;
            for(int i = 0;i<this.edgesValues.size();i++){
                if(this.edgesValues.get(i).compareTo(value) == 0){
                    edge.setColor(Colors.getColor(i));
                    simpleEdge.setColor(Colors.getColor(i));
                    containedValue = true;
                    break;
                }
            }
            if(!containedValue) {
                edge.setColor(Colors.getColor(this.edgesValues.size()));
                simpleEdge.setColor(Colors.getColor(this.edgesValues.size()));
                this.edgesValues.add(value);
            }

            int multipleEdge = 0;
            for(Edge edgeI : this.edges){
                if(edgeI.getVertexTo() == vertexTo && edgeI.getVertexFrom() == vertexFrom){
                    multipleEdge++;
                }
            }

            if(multipleEdge > 1){
                simpleEdge.setText(multipleEdge+"x");
            }

            if(animate){
                this.animation.startAnimation();
            }
            else{
                this.animation.endAnimation();
            }
            rewriteSuccinctRepresentation();
        }
    }

    public boolean checkWheelerGraph(){
        this.animation.endAnimation();
        this.animation.clear();
        boolean isWheelerGraph = true;

        for(int i = 0;i<2000;i++){
            this.animation.addAnimatable(AnimationType.ColorAnimation, vertices.get(0), Color.RED, Color.LIGHTGRAY);
        }

        for(int i = 0;i<this.vertices.size();i++){
            if(this.vertices.get(i).getIncoming().isEmpty()){
                this.animation.addAnimatable(AnimationType.ColorAnimation, vertices.get(i), Color.RED, Color.LIGHTGRAY);
                this.animation.addAnimatable(AnimationType.ColorAnimation, simpleVerticesTo.get(i), Color.RED, Color.LIGHTGRAY);
                isWheelerGraph = false;
            }
            if(this.vertices.get(i).getOutgoing().isEmpty()){
                this.animation.addAnimatable(AnimationType.ColorAnimation, vertices.get(i), Color.RED, Color.LIGHTGRAY);
                this.animation.addAnimatable(AnimationType.ColorAnimation, simpleVerticesFrom.get(i), Color.RED, Color.LIGHTGRAY);
                isWheelerGraph = false;
            }
        }

        for(Edge edge0:this.edges){
            for(Edge edge1:this.edges){
                int cmpE = edge0.getText().compareTo(edge1.getText());
                int cmpU = Integer.compare(Integer.parseInt(edge0.getVertexFrom().getValue()),
                        Integer.parseInt(edge1.getVertexFrom().getValue()));
                int cmpV = Integer.compare(Integer.parseInt(edge0.getVertexTo().getValue()),
                        Integer.parseInt(edge1.getVertexTo().getValue()));
                boolean correctPair = true;
                if(cmpE < 0 && cmpV > 0){
                    correctPair = false;
                }
                if(cmpE == 0 && cmpU < 0 && cmpV > 0){
                    correctPair = false;
                }

                if(!correctPair){
                    this.animation.addAnimatable(AnimationType.ColorAnimation, edge0, Color.RED, edge0.getColor());
                    this.animation.addAnimatable(AnimationType.ColorAnimation, edge1, Color.RED, edge1.getColor());
                    this.animation.addAnimatable(AnimationType.ColorAnimation, edge0.getVertexFrom(), Color.RED, Color.LIGHTGRAY);
                    this.animation.addAnimatable(AnimationType.ColorAnimation, edge0.getVertexTo(), Color.RED, Color.LIGHTGRAY);
                    this.animation.addAnimatable(AnimationType.ColorAnimation, edge1.getVertexFrom(), Color.RED, Color.LIGHTGRAY);
                    this.animation.addAnimatable(AnimationType.ColorAnimation, edge1.getVertexTo(), Color.RED, Color.LIGHTGRAY);

                    for(SimpleDirectedEdge sde:this.doubleGraphEdges){
                        if(sde.vertexFrom().getValue().compareTo(edge0.getVertexFrom().getValue()) == 0 &&
                        sde.vertexTo().getValue().compareTo(edge0.getVertexTo().getValue()) == 0){
                            this.animation.addAnimatable(AnimationType.ColorAnimation, sde, Color.RED, sde.getColor());
                        }
                        else if(sde.vertexFrom().getValue().compareTo(edge1.getVertexFrom().getValue()) == 0 &&
                                sde.vertexTo().getValue().compareTo(edge1.getVertexTo().getValue()) == 0){
                            this.animation.addAnimatable(AnimationType.ColorAnimation, sde, Color.RED, sde.getColor());
                        }
                    }
                    isWheelerGraph = false;

                }
            }
        }
        if(!isWheelerGraph){
            this.animation.startAnimation();
        }
        return isWheelerGraph;
    }





    /*
    pridat vec na pridanie vrchola - da sa pridat iba vrchol s dalsim poradovim cislom

    ak sa to da, tak to vypise, ze ako je to ulozene
    ak to bude privelmi dlhe, tak to vlozi do riadkov

    prida tu button na tunelovanie


     */
}
