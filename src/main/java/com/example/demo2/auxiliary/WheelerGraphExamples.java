package com.example.demo2.auxiliary;

import com.example.demo2.algorithmDisplays.GraphDisplay;
import com.example.demo2.algorithmDisplays.animatableNodes.DirectedVertex;
import com.example.demo2.algorithmDisplays.animatableNodes.Edge;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class WheelerGraphExamples {

    private static final ArrayList<ArrayList<DirectedVertex>> graphs = new ArrayList<>();

    static void addEdge(GraphDisplay graphDisplay, ArrayList<DirectedVertex> vertices, int from, int to, char label){
        Edge edge = graphDisplay.addEdge(vertices.get(from), vertices.get(to));
        edge.setText(label);
    }
    static void fulfillVertices(GraphDisplay graphDisplay, ArrayList<DirectedVertex> vertices, int num){
        for(int i = 0;i<num;i++){
            DirectedVertex vertex = (DirectedVertex) graphDisplay.addVertex();
            vertex.setValue(i);
            vertex.setRelativePosition(0.4*Math.cos((2*Math.PI/num)*i) + 0.5,
                    0.4*Math.sin((2*Math.PI/num)*i) + 0.5);
            vertices.add(vertex);
        }
    }
    static void graph1(){
        VBox vBox = new VBox();
        GraphDisplay graphDisplay = new GraphDisplay(vBox, "", 1, true);
        ArrayList<DirectedVertex> vertices = new ArrayList<>();
        fulfillVertices(graphDisplay, vertices, 12);
        addEdge(graphDisplay, vertices, 0, 1, 'a');
        addEdge(graphDisplay, vertices, 1, 10, 'r');
        addEdge(graphDisplay, vertices, 10, 6, 'b');
        addEdge(graphDisplay, vertices, 6, 2, 'a');
        addEdge(graphDisplay, vertices, 2, 8, 'd');
        addEdge(graphDisplay, vertices, 8, 4, 'a');
        addEdge(graphDisplay, vertices, 4, 9, 'k');
        addEdge(graphDisplay, vertices, 9, 5, 'a');
        addEdge(graphDisplay, vertices, 5, 11, 'r');
        addEdge(graphDisplay, vertices, 11, 7, 'b');
        addEdge(graphDisplay, vertices, 7, 3, 'a');
        addEdge(graphDisplay, vertices, 3, 0, '$');
        graphs.add(vertices);
    }

    static void graph2(){
        VBox vBox = new VBox();
        GraphDisplay graphDisplay = new GraphDisplay(vBox, "", 1, true);
        ArrayList<DirectedVertex> vertices = new ArrayList<>();
        fulfillVertices(graphDisplay, vertices, 10);
        addEdge(graphDisplay, vertices, 0, 8, 'y');
        addEdge(graphDisplay, vertices, 8, 6, 's');
        addEdge(graphDisplay, vertices, 6, 1, 'a');
        addEdge(graphDisplay, vertices, 1, 3, 'e');
        addEdge(graphDisplay, vertices, 3, 5, 'p');
        addEdge(graphDisplay, vertices, 5, 9, 'y');
        addEdge(graphDisplay, vertices, 9, 7, 's');
        addEdge(graphDisplay, vertices, 7, 2, 'a');
        addEdge(graphDisplay, vertices, 2, 4, 'e');
        addEdge(graphDisplay, vertices, 4, 0, '$');
        graphs.add(vertices);
    }

    static void graph3(){
        VBox vBox = new VBox();
        GraphDisplay graphDisplay = new GraphDisplay(vBox, "", 1, true);
        ArrayList<DirectedVertex> vertices = new ArrayList<>();
        fulfillVertices(graphDisplay, vertices, 7);
        addEdge(graphDisplay, vertices, 0, 5, 'c');
        addEdge(graphDisplay, vertices, 5, 3, 'b');
        addEdge(graphDisplay, vertices, 3, 1, 'a');
        addEdge(graphDisplay, vertices, 1, 6, 'c');
        addEdge(graphDisplay, vertices, 6, 4, 'b');
        addEdge(graphDisplay, vertices, 4, 2, 'a');
        addEdge(graphDisplay, vertices, 2, 0, '$');
        graphs.add(vertices);
    }

    static void graph4(){
        VBox vBox = new VBox();
        GraphDisplay graphDisplay = new GraphDisplay(vBox, "", 1, true);
        ArrayList<DirectedVertex> vertices = new ArrayList<>();
        fulfillVertices(graphDisplay, vertices, 14);
        addEdge(graphDisplay, vertices, 0, 11, 't');
        addEdge(graphDisplay, vertices, 11, 5, 'a');
        addEdge(graphDisplay, vertices, 5, 7, 'c');
        addEdge(graphDisplay, vertices, 7, 2, 'a');
        addEdge(graphDisplay, vertices, 2, 13, 't');
        addEdge(graphDisplay, vertices, 13, 9, 'c');
        addEdge(graphDisplay, vertices, 9, 4, 'a');
        addEdge(graphDisplay, vertices, 4, 6, 'c');
        addEdge(graphDisplay, vertices, 6, 1, 'a');
        addEdge(graphDisplay, vertices, 1, 12, 't');
        addEdge(graphDisplay, vertices, 12, 8, 'c');
        addEdge(graphDisplay, vertices, 8, 3, 'a');
        addEdge(graphDisplay, vertices, 3, 10, 'g');
        addEdge(graphDisplay, vertices, 10, 0, '$');
        graphs.add(vertices);
    }

    static {
        graph1();
        graph2();
        graph3();
        graph4();
    }

    public static ArrayList<DirectedVertex> vertices(int graphNumber){
        if(0 > graphNumber || graphNumber >= graphs.size()){
            return null;
        }
        return graphs.get(graphNumber);
    }

    private static final ArrayList<ArrayList<Character>> ls = new ArrayList<>();
    private static final ArrayList<ArrayList<Integer>> outs = new ArrayList<>();
    private static final ArrayList<ArrayList<Integer>> ins = new ArrayList<>();
    private static final ArrayList<Integer> lengths = new ArrayList<>();

    static {
        ArrayList<Character> l1 = new ArrayList<>(List.of('a','r','d','$','k','r','a','a','a','b'));
        ls.add(l1);
        ArrayList<Integer> out1 = new ArrayList<>(List.of(1,1,1,0,1,1,1,1,1,1,1));
        outs.add(out1);
        ArrayList<Integer> in1 = new ArrayList<>(List.of(1,1,1,1,1,1,1,1,1,0,1));
        ins.add(in1);
        lengths.add(12);

        ArrayList<Character> l2 = new ArrayList<>(List.of('y','e','p','$','y','a','s'));
        ls.add(l2);
        ArrayList<Integer> out2 = new ArrayList<>(List.of(1,1,1,0,1,1,1,1));
        outs.add(out2);
        ArrayList<Integer> in2 = new ArrayList<>(List.of(1,1,1,1,1,1,0,1));
        ins.add(in2);
        lengths.add(10);

        ArrayList<Character> l3 = new ArrayList<>(List.of('c','c','$','a','b'));
        ls.add(l3);
        ArrayList<Integer> out3 = new ArrayList<>(List.of(1,1,0,1,1,1));
        outs.add(out3);
        ArrayList<Integer> in3 = new ArrayList<>(List.of(1,1,1,1,0,1));
        ins.add(in3);
        lengths.add(7);

        ArrayList<Character> l4 = new ArrayList<>(List.of('t','t','g','c','c','a','a','$','a','c'));
        ls.add(l4);
        ArrayList<Integer> out4 = new ArrayList<>(List.of(1,1,1,0,1,1,1,1,1,1,1));
        outs.add(out4);
        ArrayList<Integer> in4 = new ArrayList<>(List.of(1,1,1,1,1,0,1,1,1,1,1));
        ins.add(in4);
        lengths.add(14);
    }

    public static ArrayList<Character> l(int graphNumber){
        if(0 > graphNumber || graphNumber >= ls.size()){
            return null;
        }
        return ls.get(graphNumber);
    }

    public static ArrayList<Integer> in(int graphNumber){
        if(0 > graphNumber || graphNumber >= ls.size()){
            return null;
        }
        return ins.get(graphNumber);
    }

    public static ArrayList<Integer> out(int graphNumber){
        if(0 > graphNumber || graphNumber >= ls.size()){
            return null;
        }
        return outs.get(graphNumber);
    }

    public static int length(int graphNumber){
        if(0 > graphNumber || graphNumber >= lengths.size()){
            return 0;
        }
        return lengths.get(graphNumber);
    }
}
