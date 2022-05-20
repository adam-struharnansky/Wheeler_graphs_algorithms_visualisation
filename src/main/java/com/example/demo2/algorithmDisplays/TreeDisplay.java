package com.example.demo2.algorithmDisplays;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class TreeDisplay extends Display{

    private final Pane pane;
    private final ArrayList<Vertex> roots;
    private final ArrayList<Vertex> vertices;

    private double zoom;

    public class Vertex{
        private Vertex parent;
        private final ArrayList<Vertex> children;
        private final ArrayList<Edge> edges;
        //todo - ako pridat ine hrany?

        private final Group group;
        private final Circle circle;
        private final Text text;

        public Vertex(){
            this.parent = null;
            this.children = new ArrayList<>();
            this.edges = new ArrayList<>();
            this.group = new Group();
            this.circle = new Circle();
            this.text = new Text();
            //todo zmenit graficke veci a pridat ich do pane a group
        }

        void redraw(){
            //podla toho ci ma otca, alebo nie. ak nie, tak ho iba niekam hod,
            //ak ma, tak treba prekreslit cely strom
            //toto aj tak sa bude volat na strom vonku
            //moze to byt iba na zmenu veci?
        }

        void setParent(Vertex parent){
            if(this.parent == null){
                roots.remove(this);
            }
            this.parent = parent;
            if(this.parent == null){
                roots.add(this);
            }
            //redraw()
        }

    }

    public class Edge{
        private Vertex parent;
        private Vertex children;
        private final boolean normalEdge;//taka ktora je v strome, nejde mimo na tvorbu inych veci

        private final Line line;
        private final Text text;

        public Edge(){
               this.normalEdge = true;
               this.line = new Line();
               this.text = new Text();
        }
    }

    public TreeDisplay(VBox container, String name, int ratio) {
        super(container, name, ratio);
        this.pane = super.getPane();
        this.roots = new ArrayList<>();
        this.vertices = new ArrayList<>();
    }

    @Override
    public void centre() {
        super.centre();
    }

    void redraw(){
        //zoberie si vsetkych otcov, zoberie ako su rozkonareny, a podla toho im da priestor
        //potom vykresli synov
        //je mozne, ze pri tom bude musiet zoomovat, alebo odzoomovat tak, aby sa tam vsetko pekne vopchalo
    }

    Vertex addVertex(Vertex parent){
        if(parent == null){
            Vertex vertex = new Vertex();
            this.vertices.add(vertex);
            this.roots.add(vertex);
            return vertex;
        }
        else if(this.vertices.contains(parent)){
            Vertex vertex = new Vertex();
            vertex.setParent(parent);
            this.vertices.add(vertex);
            return vertex;
        }
        return null;
    }

    void setEdgeText(String text){
        //todo
    }
}
