package com.example.demo2.algorithmDisplays;

import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;

import java.util.ArrayList;

public class MultiDisplay extends Display{

    private final ArrayList<Sector> sectors;
    private final ArrayList<Pair<Integer, Integer>> ratios;
    private final Pane pane;


    public MultiDisplay(VBox container, String name, int ratio) {
        super(container, name, ratio);
        this.sectors = new ArrayList<>();
        this.ratios = new ArrayList<>();
        this.pane = super.getPane();
    }

    public void addSector(Sector.SectorType sectorType, int xRatio, int yRatio){
        xRatio = Math.max(1, xRatio);
        yRatio = Math.max(1, yRatio);
        switch (sectorType){
            case Matrix -> {this.sectors.add(new Matrix(this.pane, 0,0,0,0));
                this.ratios.add(new Pair<>(xRatio, yRatio));}
        }
        resize();
    }

    @Override
    public void resize(){
        //najskor si zistit, aky moze byt najviac kvoli tomu, aby to bolo najviac
        double maxSize = Double.POSITIVE_INFINITY;
        int xRatiosSum = 0;
        for(Pair<Integer, Integer> ratio:this.ratios){
            maxSize = Math.min(maxSize, this.pane.getHeight()/ratio.getValue());
            xRatiosSum +=ratio.getKey();
        }
        double unit = Math.min(maxSize, this.pane.getWidth()/xRatiosSum);
        //vsetkym nastavit hranice..

        //treba si ich vycentrovat, a potom zistit, kde to ma zacat, a potom ist v cykle
        //a kazdemu nastavit svoje hranice
        double start = this.pane.getWidth()/2 - unit*xRatiosSum/2;

        for(int i = 0; i<this.sectors.size(); i++){
            this.sectors.get(i).setBorders(start, this.pane.getHeight()/2 - unit*this.ratios.get(i).getValue()/2,
                    start + unit*ratios.get(i).getKey(),this.pane.getHeight()/2 + unit*this.ratios.get(i).getValue()/2);
        }
    }
}
