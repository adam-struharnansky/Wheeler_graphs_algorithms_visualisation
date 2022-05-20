package com.example.demo2.auxiliary;

import javafx.scene.paint.Color;
import javafx.util.Pair;

public class Auxiliary {

    public static Color newColor(Color color){
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getOpacity());
    }

    public static Pair<Double, Double> newPair(Pair<Double, Double> pair){
        return new Pair<>(pair.getKey(), pair.getValue());
    }

    public static boolean isArabInteger(String string){
        return string.matches("-?\\d+(\\.\\d+)?");
    }

    public static boolean isArabNumeric(String string){
        return string.matches("-?\\d+(\\.\\d+)?");
    }
}
