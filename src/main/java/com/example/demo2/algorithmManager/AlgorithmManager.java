package com.example.demo2.algorithmManager;

import com.example.demo2.algorithmDisplays.WindowManager;
import com.example.demo2.algorithmDisplays.animatableNodes.DirectedVertex;
import com.example.demo2.algorithms.StartScreen;
import com.example.demo2.algorithms.TestAlgorithm;
import com.example.demo2.algorithms.bwt.*;
import com.example.demo2.algorithms.sa.SAGeneralConstructionAlgorithm;
import com.example.demo2.algorithms.wg.*;
import com.example.demo2.multilingualism.Languages;

import java.util.ArrayList;

public class AlgorithmManager {

    public void changeAlgorithm(AlgorithmType algorithmType){
        WindowManager.clearWindow();
        Languages.clearRemovableListeners();
        switch (algorithmType){
            case Test -> {
                WindowManager.setAlgorithmName("test");
                new TestAlgorithm(this);
            }
            case BWT -> {
                WindowManager.setAlgorithmName("bwt");
                new BWTDivisionAlgorithm(this);
            }
            case BWTDecode -> {
                WindowManager.setAlgorithmName("bwtDecode");
                new BWTDecodeAlgorithm(this);
            }
            case BWTEncode -> {
                WindowManager.setAlgorithmName("bwt");
                new BWTGeneralAlgorithm(this);
            }
            case Start -> {
                WindowManager.setAlgorithmName("start");
                new StartScreen(this);
            }
            case SAIntroduction -> {
                WindowManager.setAlgorithmName("sa");
                new SAGeneralConstructionAlgorithm(this);
            }
            case BWTFromSA -> {
                WindowManager.setAlgorithmName("BWTUsingSA");
                new BWTFromSAAlgorithm(this);
            }
            case WG -> {
                WindowManager.setAlgorithmName("wg");
                new WGDivision(this);
            }
            case WGCreation -> {
                WindowManager.setAlgorithmName("");
                new WGCreation(this);
            }
            case WGTunneling -> {
                WindowManager.setAlgorithmName("WGTunneling");
                new WGTunneling(this);
            }
            case WGSearch -> {
                WindowManager.setAlgorithmName("");
                new WGSearch(this);
            }
            case WGFromBWT -> {
                WindowManager.setAlgorithmName("WGFromBWT");
                new WGFromBWT(this);
            }
            case BWTSearch -> {
                WindowManager.setAlgorithmName("BWTPatternMatching");
                new BWTSearch(this);
            }
            case WGInverse -> {
                WindowManager.setAlgorithmName("");
                new WGBackwardSteps(this);
            }
        }
    }

    public void changeAlgorithm(AlgorithmType algorithmType, String input){
        WindowManager.clearWindow();
        Languages.clearRemovableListeners();
        switch (algorithmType){
            case BWTDecode -> {
                WindowManager.setAlgorithmName("");
                new BWTDecodeAlgorithm(this, input);
            }
            case BWTFromSA -> {
                WindowManager.setAlgorithmName("");
                new BWTFromSAAlgorithm(this, input);
            }
            case BWTSearch -> {
                WindowManager.setAlgorithmName("BWTPatternMatching");
                new BWTSearch(this, input);
            }
            case WGFromBWT -> {
                WindowManager.setAlgorithmName("WGFromBWT");
                new WGFromBWT(this, input);
            }
        }
    }

    public void changeAlgorithm(AlgorithmType algorithmType, ArrayList<DirectedVertex> vertices){
        WindowManager.clearWindow();
        Languages.clearRemovableListeners();
        switch (algorithmType){
            case WGCreation -> {
                WindowManager.setAlgorithmName("");
                new WGCreation(this, vertices);
            }
            case WGTunneling -> {
                WindowManager.setAlgorithmName("WGTunneling");
                new WGTunneling(this, vertices);
            }
            case WGSearch -> {
                WindowManager.setAlgorithmName("");
                new WGSearch(this, vertices);
            }
            case WGInverse -> {
                WindowManager.setAlgorithmName("");
                new WGBackwardSteps(this);
            }
        }
    }

    public void changeAlgorithm(AlgorithmType algorithmType, ArrayList<Character> c,
                                ArrayList<Integer> i, ArrayList<Integer> o, int m){
        WindowManager.clearWindow();
        Languages.clearRemovableListeners();
        switch (algorithmType){
            case WGInverse -> {
                WindowManager.setAlgorithmName("");
                new WGBackwardSteps(this, c, i, o, m);
            }
        }
    }
}
