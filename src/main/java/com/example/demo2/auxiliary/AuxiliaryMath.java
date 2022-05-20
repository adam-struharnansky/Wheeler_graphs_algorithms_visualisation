package com.example.demo2.auxiliary;

import javafx.util.Pair;

public final class AuxiliaryMath {

    public static double distance(Pair<Double, Double> f, Pair<Double,Double> s){
        return Math.sqrt((f.getKey() - s.getKey())*(f.getKey() - s.getKey())
                + (f.getValue() - s.getValue())*(f.getValue() - s.getValue()));
    }

    public static double distance(double fX, double fY, double sX, double sY){
        return Math.sqrt((fX - sX)*(fX - sX) + (fY - sY)*(fY - sY));
    }

    public static double vectorSize(Pair<Double, Double> vector) {
        return Math.sqrt(vector.getKey()*vector.getKey() + vector.getValue()*vector.getValue());
    }

    public static double circularAngle(Pair<Double, Double> middle, Pair<Double, Double> outer){

        double rotationAngle;
        Pair<Double, Double> p = new Pair<>(middle.getKey(), middle.getValue() - 40);
        double a = AuxiliaryMath.distance(
                middle.getKey(), middle.getValue(),
                outer.getKey(), outer.getValue());
        double b = 40.0; // vectorSize(this.from.getPosition(), p) = 40.0
        double c = AuxiliaryMath.distance(outer, p);
        if(a == 0.0){
            return 0.0;
        }

        double aCos = Math.min(-(c*c - a*a - b*b)/(2*a*b), 0.999);
        aCos = Math.max(aCos, -0.999);
        rotationAngle = Math.acos(aCos);
        if(middle.getKey() > outer.getKey()){
            rotationAngle = 2*Math.PI - rotationAngle;
        }
        return rotationAngle;
    }
}
