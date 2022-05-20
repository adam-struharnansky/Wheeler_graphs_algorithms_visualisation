package com.example.demo2.auxiliary;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Random;

public class Colors {

    public final static Color highlightingColor = Color.color(1.0,1.0,0.0,0.3);

    private static final ArrayList<Color> colors = new ArrayList<>();
    private static final Random random = new Random();

    static {
        colors.add(Color.MAGENTA);
        colors.add(Color.TURQUOISE);
        colors.add(Color.GREEN);
        colors.add(Color.BLUE);
        colors.add(Color.RED);
        //todo - Add some more distinct colors
    }


    public static Color getColor(int colorNumber){
        if(colorNumber < 0 ){
            return null;
        }
        while(colors.size() <= colorNumber){
            colors.add(new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), 1.0));
        }
        return colors.get(colorNumber);
    }

    private static final Color startingColor = Color.BLUE;
    private static final Color endingColor = Color.ORANGE;
    public static Color getTransitionColor(int number, int whole){
        if(number < 0 || number > whole || whole <= 0){
            return null;
        }
        return new Color(
                startingColor.getRed() + (endingColor.getRed() - startingColor.getRed())*((double) number / (double) whole),
                startingColor.getGreen() + (endingColor.getGreen() - startingColor.getGreen())*((double) number / (double) whole),
                startingColor.getBlue() + (endingColor.getBlue() - startingColor.getBlue())*((double) number / (double) whole),
                startingColor.getOpacity() + (endingColor.getOpacity() - startingColor.getOpacity())*((double) number / (double) whole)
                );
    }
}
