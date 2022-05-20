package com.example.demo2.algorithms.wg;

import com.example.demo2.algorithmDisplays.*;
import com.example.demo2.algorithmDisplays.animatableNodes.DirectedVertex;
import com.example.demo2.algorithmDisplays.DisplayType;
import com.example.demo2.algorithmDisplays.animatableNodes.Edge;
import com.example.demo2.algorithmDisplays.animatableNodes.Vertex;
import com.example.demo2.algorithmManager.AlgorithmManager;
import com.example.demo2.algorithms.Algorithm;
import com.example.demo2.auxiliary.WheelerGraphExamples;
import com.example.demo2.multilingualism.LanguageListenerAdder;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

import java.util.ArrayList;

public class WGSearch extends Algorithm {

    private final GraphDisplay graphDisplay = (GraphDisplay) WindowManager.addDisplay(DisplayType.DirectedGraph, "", 3);
    private final MatrixDisplay matrixDisplay = (MatrixDisplay) WindowManager.addDisplay(DisplayType.Matrix, "", 1);
    private final CodeDisplay codeDisplay = (CodeDisplay) WindowManager.addDisplay(DisplayType.Code, "", 2);
    private final TextDisplay textDisplay = (TextDisplay) WindowManager.addDisplay(DisplayType.Text, "", 2);

    private final ArrayList<DirectedVertex> vertices = new ArrayList<>();

    private final static int searchRow = 0;
    private final static int exampleRow = 1;
    private final static int changeRow = 2;

    private final TextField inputTextField = new TextField();
    private final Button startButton = new Button();
    private final ArrayList<RadioButton> exampleRadioButtons = new ArrayList<>();
    private final Button setExampleGraph = new Button();

    public WGSearch(AlgorithmManager algorithmManager) {
        super(algorithmManager);
        addControllers(algorithmManager);
    }

    public WGSearch(AlgorithmManager algorithmManager, ArrayList<DirectedVertex> vertices){
        super(algorithmManager);
        addControllers(algorithmManager);
        setGraph(vertices);
    }

    private void setGraph(ArrayList<DirectedVertex> vertices){
        this.graphDisplay.clear();
        this.vertices.clear();

        for(DirectedVertex oldVertex:vertices){
            DirectedVertex vertex = (DirectedVertex) this.graphDisplay.addVertex();
            vertex.setValue(oldVertex.getValue());
            vertex.setRelativePosition(oldVertex.getRelativePosition());
            this.vertices.add(vertex);
        }

        for(DirectedVertex oldVertex:vertices){
            for(Edge edge:oldVertex.getOutgoing()){
                Vertex fromVertex = null, toVertex = null;
                for(Vertex vertex:this.vertices){
                    if(vertex.getValue().compareTo(edge.getVertexFrom().getValue()) == 0){
                        fromVertex = vertex;
                    }
                    if(vertex.getValue().compareTo(edge.getVertexTo().getValue()) == 0){
                        toVertex = vertex;
                    }
                    if(fromVertex != null && toVertex != null){
                        Edge newEdge = this.graphDisplay.addEdge(fromVertex, toVertex);
                        newEdge.setText(edge.getText());
                        break;
                    }
                }
            }
        }
        rewriteSuccinctRepresentation();
    }

    private void addControllers(AlgorithmManager algorithmManager){

        WindowManager.addController(this.inputTextField, searchRow, 0);

        this.startButton.setOnAction(actionEvent -> start());
        LanguageListenerAdder.addLanguageListener("searchGraph", this.startButton);
        WindowManager.addController(this.startButton, searchRow,1);

        ToggleGroup examples = new ToggleGroup();

        for(int i = 0;i<5;i++) {
            RadioButton radioButton = new RadioButton();
            radioButton.setToggleGroup(examples);
            LanguageListenerAdder.addLanguageListener("example"+i, radioButton);
            WindowManager.addController(radioButton, exampleRow, i);
            this.exampleRadioButtons.add(radioButton);
        }

        this.setExampleGraph.setOnAction(actionEvent -> {
            for(int i = 0;i<this.exampleRadioButtons.size();i++){
                if(examples.getSelectedToggle() == this.exampleRadioButtons.get(i)){
                    setGraph(WheelerGraphExamples.vertices(i));
                }
            }
        });
        LanguageListenerAdder.addLanguageListener("setExampleGraph", this.setExampleGraph);
        WindowManager.addController(this.setExampleGraph, exampleRow, 5);

        //todo - pridat change algorithm buttony
    }

    private void rewriteSuccinctRepresentation(){
        int edgesNumber = 0;
        for(Vertex vertex:this.vertices){
            edgesNumber += vertex.getEdges().size();
        }
        this.matrixDisplay.setMatrixSize(edgesNumber + 1, 6);
        this.matrixDisplay.setRowText(0, "i", "L[i]", "I[i]", "O[i]");

        /*
        naplnit -
        pridat tu aj F, aj C


         */
    }

    private void setCode(){
        //todo - pridat text kodu algoritmu
    }

    private void start(){

        //todo - odstranit startButton a pridat back/next/animate
        //todo - zakazat sa v tomto case presunut na ine algoritmy? toto asi nie je potrebne, tu sa to nemeni

    }

    @Override
    protected void nextStep(boolean animate){

    }

    @Override
    protected void backStep(boolean animate){

    }

    private void end(){
        //todo - odstranit nepotrebne veci
    }

    /*
    vyberme si patern, ktory je zo vstupu

    zoberieme posledne pismeno zo stringu, a zapamatame si ho



    for(int i = pattern.size, i >= 0, i --)
        najst hodnoty pomocou C, ktore hovoria, ktore vstupne hrany maju znak c

        2. tieto veci su zhodne s nulami v I, takze i-ta 0 v I je hrana s labelom F[i]
        ak urobime dva selecty0 v I, tak dostaneme pozicie prvej a poslednej 0
        ak urobime rank1 s tymito cislami, tak nam vrati ktore vrcholy maju dane hrany



     */
}
