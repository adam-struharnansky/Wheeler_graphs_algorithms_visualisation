package com.example.demo2.animations;

import com.example.demo2.algorithmDisplays.animatableNodes.*;
import com.example.demo2.auxiliary.Auxiliary;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.*;

public class Animation {

    //todo asi nastavit, aby sa toto dalo menit
    private int iterationNumber = 50;
    private int iterate;

    private boolean forward = true;
    private int forwardInt = 1;

    private Timer timer;

    //objekty a miesta kde maju zacat
    private final HashMap<MoveAnimatable, Pair<Double, Double>> moveAnimatableStarts = new HashMap<>();
    //objekty a miesta, kde maju skoncit
    private final HashMap<MoveAnimatable, Pair<Double, Double>> moveAnimatableEnds = new HashMap<>();
    //posun, o ktory sa kazdy krok posunie
    private final HashMap<MoveAnimatable, Pair<Double, Double>> moveAnimatableSteps = new HashMap<>();

    //objekty, a farby s ktorymi maju skoncit
    private final HashMap<ColorAnimatable, Color> colorAnimatableStarts = new HashMap<>();
    //objekty, a farby s ktorymi maju skoncit
    private final HashMap<ColorAnimatable, Color> colorAnimatableEnds = new HashMap<>();
    //zmena farby, o ktory sa kazdy krok posunu
    private final HashMap<ColorAnimatable, ArrayList<Double>> colorAnimatableSteps = new HashMap<>();

    //objekty, ktore sa maju objavit
    private final HashSet<AppearAnimatable> appearAnimatables = new HashSet<>();

    //objekty, sktore maju zmiznut
    private final HashSet<AppearAnimatable> disappearAnimatable = new HashSet<>();

    //objekty, ktore sa maju objavit na konci animacie
    private final HashSet<AppearAnimatable> suddenlyAppearAnimatables = new HashSet<>();

    //objekty, sktore maju zmiznut na konci animacie
    private final HashSet<AppearAnimatable> suddenlyDisappearAnimatable = new HashSet<>();

    //objekty a relativne miesta, kde maju skoncit
    private final HashMap<RelativeMoveAnimatable, Pair<Double, Double>> relativeMoveAnimatableStarts = new HashMap<>();
    //objekty a relativne miesta, kde maju skoncit
    private final HashMap<RelativeMoveAnimatable, Pair<Double, Double>> relativeMoveAnimatableEnds = new HashMap<>();
    //posun, o ktory sa kazdy krok posunie
    private final HashMap<RelativeMoveAnimatable, Pair<Double, Double>> relativeMoveAnimatableSteps = new HashMap<>();

    public Animation(){
        this.timer = new Timer();
    }

    public void addAnimatable(AnimationType type, Animatable animatable, Pair<Double, Double> startPosition,
                              Pair<Double, Double> endPosition){
        switch (type){
            case MoveAnimation -> addMoveAnimatable((MoveAnimatable) animatable, startPosition, endPosition);
            case RelativeMoveAnimation -> addRelativeMoveAnimatable((RelativeMoveAnimatable) animatable, startPosition, endPosition);
        }
    }

    public void addAnimatable(AnimationType type, Animatable animatable){
        switch (type){
            case AppearAnimation -> addAppearAnimatable((AppearAnimatable) animatable);
            case DisappearAnimation -> addDisappearAnimatable((AppearAnimatable) animatable);
            case SuddenlyAppearAnimation -> addSuddenlyAppearAnimatable((AppearAnimatable) animatable);
            case SuddenlyDisappearAnimation -> addSuddenlyDisappearAnimatable((AppearAnimatable) animatable);
        }
    }

    public void addAnimatable(AnimationType type, Animatable animatable, Pair<Double, Double> endPosition){
        switch (type){
            case MoveAnimation -> addMoveAnimatable((MoveAnimatable) animatable,
                    ((MoveAnimatable) animatable).getPosition(), endPosition);
            case RelativeMoveAnimation -> addRelativeMoveAnimatable((RelativeMoveAnimatable) animatable,
                    ((RelativeMoveAnimatable) animatable).getRelativePosition(), endPosition);
        }
    }

    public void addAnimatable(AnimationType type, Animatable animatable, double endX, double endY){
        addAnimatable(type, animatable, new Pair<>(endX, endY));
    }

    public void addAnimatable(AnimationType type, Animatable animatable, Color startColor, Color endColor){
        if (type == AnimationType.ColorAnimation) {
            addColorAnimatable((ColorAnimatable) animatable, startColor, endColor);
        }
    }

    public void addAnimatable(AnimationType type, Animatable animatable, Color endColor){
        if (type == AnimationType.ColorAnimation) {
            addColorAnimatable((ColorAnimatable) animatable,
                    ((ColorAnimatable) animatable).getColor(), endColor);
        }
    }

    private void addMoveAnimatable(MoveAnimatable animatable, Pair<Double, Double> startPosition, Pair<Double,
            Double> endPosition){
        this.moveAnimatableStarts.put(animatable, Auxiliary.newPair(startPosition));
        this.moveAnimatableEnds.put(animatable, Auxiliary.newPair(endPosition));
    }

    private void addRelativeMoveAnimatable(RelativeMoveAnimatable animatable, Pair<Double, Double> startPosition,
                                      Pair<Double, Double> endPosition){
        this.relativeMoveAnimatableStarts.put(animatable, Auxiliary.newPair(startPosition));
        this.relativeMoveAnimatableEnds.put(animatable, Auxiliary.newPair(endPosition));
    }

    private void addColorAnimatable(ColorAnimatable animatable, Color startColor, Color endColor){
        this.colorAnimatableStarts.put(animatable, Auxiliary.newColor(startColor));
        this.colorAnimatableEnds.put(animatable, Auxiliary.newColor(endColor));
    }

    private void addAppearAnimatable(AppearAnimatable animatable){
        this.appearAnimatables.add(animatable);
    }

    private void addDisappearAnimatable(AppearAnimatable animatable){
        this.disappearAnimatable.add(animatable);
    }

    private void addSuddenlyAppearAnimatable(AppearAnimatable animatable){
        this.suddenlyAppearAnimatables.add(animatable);
    }

    private void addSuddenlyDisappearAnimatable(AppearAnimatable animatable){
        this.suddenlyDisappearAnimatable.add(animatable);
    }

    public void setForward(boolean forward){
        this.forward = forward;
        this.forwardInt = (this.forward)? 1 : -1;
    }

    public void startAnimation(){


        this.relativeMoveAnimatableSteps.clear();
        this.colorAnimatableSteps.clear();
        this.moveAnimatableSteps.clear();

        this.relativeMoveAnimatableStarts.forEach((animatable, start) ->{
            Pair<Double, Double> step = new Pair<>(
                    (this.relativeMoveAnimatableEnds.get(animatable).getKey() - start.getKey())/iterationNumber,
                    (this.relativeMoveAnimatableEnds.get(animatable).getValue() - start.getValue())/iterationNumber
            );
            this.relativeMoveAnimatableSteps.put(animatable, step);
        });

        this.moveAnimatableStarts.forEach((animatable, start) ->{
            Pair<Double, Double> step = new Pair<>(
                    (this.moveAnimatableEnds.get(animatable).getKey() - start.getKey())/iterationNumber,
                    (this.moveAnimatableEnds.get(animatable).getValue() - start.getValue())/iterationNumber
            );
            this.moveAnimatableSteps.put(animatable, step);
        });

        this.colorAnimatableStarts.forEach((animatable, start) ->{
            ArrayList<Double> step = new ArrayList<>(Arrays.asList(
                    (this.colorAnimatableEnds.get(animatable).getRed() - start.getRed())/iterationNumber,
                    (this.colorAnimatableEnds.get(animatable).getGreen() - start.getGreen())/iterationNumber,
                    (this.colorAnimatableEnds.get(animatable).getBlue() - start.getBlue())/iterationNumber,
                    (this.colorAnimatableEnds.get(animatable).getOpacity() - start.getOpacity())/iterationNumber
            ));
            this.colorAnimatableSteps.put(animatable, step);
        });

        this.iterate = 0;
        this.timer = new Timer();
        if(this.forward){
            this.moveAnimatableStarts.forEach(MoveAnimatable::setPosition);
            this.relativeMoveAnimatableStarts.forEach(RelativeMoveAnimatable::setRelativePosition);
            this.colorAnimatableStarts.forEach(ColorAnimatable::setColor);

            this.appearAnimatables.forEach(AppearAnimatable::appear);//aby existovali, inak su neviditelne
            this.disappearAnimatable.forEach(AppearAnimatable::appear);
            this.appearAnimatables.forEach(animatable -> animatable.setOpacity(0.0));
            this.disappearAnimatable.forEach(animatable -> animatable.setOpacity(1.0));
            this.suddenlyAppearAnimatables.forEach(AppearAnimatable::disappear);
            this.suddenlyDisappearAnimatable.forEach(AppearAnimatable::appear);
        }
        else{
            this.moveAnimatableEnds.forEach(MoveAnimatable::setPosition);
            this.relativeMoveAnimatableEnds.forEach(RelativeMoveAnimatable::setRelativePosition);
            this.colorAnimatableEnds.forEach(ColorAnimatable::setColor);

            this.appearAnimatables.forEach(AppearAnimatable::appear);//aby existovali, inak su neviditelne
            this.disappearAnimatable.forEach(AppearAnimatable::appear);
            this.appearAnimatables.forEach(animatable -> animatable.setOpacity(1.0));
            this.disappearAnimatable.forEach(animatable -> animatable.setOpacity(0.0));
            this.suddenlyAppearAnimatables.forEach(AppearAnimatable::disappear);
            this.suddenlyDisappearAnimatable.forEach(AppearAnimatable::appear);
        }

        TimerTask animateTask = new TimerTask() {
            @Override
            public void run() {
                iterate++;
                if (iterate >= iterationNumber) {
                    endAnimation();
                } else {
                    moveAnimatableSteps.forEach((animatable, change) -> animatable.setPosition(
                            animatable.getPosition().getKey() + forwardInt * change.getKey(),
                            animatable.getPosition().getValue() + forwardInt * change.getValue()
                    ));

                    colorAnimatableSteps.forEach((animatable, change) -> animatable.setColor(new Color(
                            animatable.getColor().getRed() + forwardInt * change.get(0),
                            animatable.getColor().getGreen() + forwardInt * change.get(1),
                            animatable.getColor().getBlue() + forwardInt * change.get(2),
                            animatable.getColor().getOpacity() + forwardInt * change.get(3)
                    )));

                    relativeMoveAnimatableSteps.forEach((animatable, change) -> animatable.setRelativePosition(
                            animatable.getRelativePosition().getKey() + forwardInt * change.getKey(),
                            animatable.getRelativePosition().getValue() + forwardInt * change.getValue()
                    ));

                    double opacity;
                    if (forward) {
                        opacity = ((double) iterate) / ((double) iterationNumber);
                    } else {
                        opacity = 1.0 - ((double) iterate) / ((double) iterationNumber);
                    }
                    appearAnimatables.forEach(animatable -> animatable.setOpacity(opacity));
                    disappearAnimatable.forEach(animatable -> animatable.setOpacity(1.0 - opacity));
                }
            }
        };
        this.timer.schedule(animateTask, 16, 16);
    }

    public void endAnimation(){
        this.timer.cancel();
        if(this.forward){
            this.moveAnimatableEnds.forEach(MoveAnimatable::setPosition);
            this.relativeMoveAnimatableEnds.forEach(RelativeMoveAnimatable::setRelativePosition);
            this.colorAnimatableEnds.forEach(ColorAnimatable::setColor);

            this.appearAnimatables.forEach(animatable -> animatable.setOpacity(1.0));
            this.disappearAnimatable.forEach(animatable -> animatable.setOpacity(0.0));
            this.appearAnimatables.forEach(AppearAnimatable::appear);
            this.disappearAnimatable.forEach(AppearAnimatable::disappear);
            this.suddenlyAppearAnimatables.forEach(AppearAnimatable::appear);
            this.suddenlyDisappearAnimatable.forEach(AppearAnimatable::disappear);
        }
        else{
            this.moveAnimatableStarts.forEach(MoveAnimatable::setPosition);
            this.relativeMoveAnimatableStarts.forEach(RelativeMoveAnimatable::setRelativePosition);
            this.colorAnimatableStarts.forEach(ColorAnimatable::setColor);

            this.appearAnimatables.forEach(animatable -> animatable.setOpacity(0.0));
            this.disappearAnimatable.forEach(animatable -> animatable.setOpacity(1.0));
            this.appearAnimatables.forEach(AppearAnimatable::disappear);
            this.disappearAnimatable.forEach(AppearAnimatable::appear);
            this.suddenlyAppearAnimatables.forEach(AppearAnimatable::disappear);
            this.suddenlyDisappearAnimatable.forEach(AppearAnimatable::appear);
        }
    }

    public void clear(){
        this.moveAnimatableStarts.clear();
        this.moveAnimatableEnds.clear();
        this.moveAnimatableSteps.clear();

        this.colorAnimatableStarts.clear();
        this.colorAnimatableEnds.clear();
        this.colorAnimatableSteps.clear();

        this.relativeMoveAnimatableStarts.clear();
        this.relativeMoveAnimatableEnds.clear();
        this.relativeMoveAnimatableSteps.clear();

        this.appearAnimatables.clear();
        this.disappearAnimatable.clear();
        this.suddenlyDisappearAnimatable.clear();
        this.suddenlyAppearAnimatables.clear();
    }

    /*
    treba prepocitat vsekty hodnoty v
     */
    public void setSpeed(double speed){
        iterationNumber = (int) (300 - speed*2.5);
    }
}
