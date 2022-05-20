package com.example.demo2.animations;

import com.example.demo2.algorithmDisplays.animatableNodes.Animatable;
import com.example.demo2.algorithmDisplays.animatableNodes.ColorAnimatable;
import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.ArrayList;

public class AnimationManager {

    private int currentAnimation = -1;
    private final ArrayList<Animation> animations = new ArrayList<>();

    private final ArrayList<Integer> possiblyRunningAnimations = new ArrayList<>();

    public void endAllAnimations(){
        this.possiblyRunningAnimations.forEach(integer -> animations.get(integer).endAnimation());
    }

    public void endCurrentAnimation(boolean forward){
        if(this.currentAnimation != -1) {
            this.animations.get(currentAnimation).endAnimation();
            possiblyRunningAnimations.remove(Integer.valueOf(this.currentAnimation));
        }
    }

    public void endAnimation(int step, boolean forward){
        if(0 <= step && step < this.animations.size()) {
            this.animations.get(step).setForward(forward);
            this.animations.get(step).endAnimation();
            possiblyRunningAnimations.remove(Integer.valueOf(this.currentAnimation));
        }
    }

    public void executeAnimation(int step, boolean forward){
        endCurrentAnimation(true);
        if(0 <= step && step < this.animations.size()) {
            this.currentAnimation = step;
            this.animations.get(currentAnimation).setForward(forward);
            this.animations.get(currentAnimation).startAnimation();
        }
    }

    private void fulfilAnimationsToStep(int step){
        for(int i = animations.size() - 1; i < step;i++){
            this.animations.add(new Animation());
        }
    }

    public void addAnimatable(AnimationType type, int step, Animatable animatable,  double endX, double endY){
        fulfilAnimationsToStep(step);
        this.animations.get(step).addAnimatable(type, animatable, new Pair<>(endX, endY));
    }

    public void addAnimatable(AnimationType type, int step, Animatable animatable,  Pair<Double, Double> endPosition){
        fulfilAnimationsToStep(step);
        this.animations.get(step).addAnimatable(type, animatable, endPosition);
    }

    public void addAnimatable(AnimationType type, int step, Animatable animatable){
        fulfilAnimationsToStep(step);
        this.animations.get(step).addAnimatable(type, animatable);
    }

    public void addAnimatable(AnimationType type, int step, Animatable animatable, Color endColor){
        fulfilAnimationsToStep(step);
        this.animations.get(step).addAnimatable(type, animatable, ((ColorAnimatable)animatable).getColor(), endColor);
    }

    public int getCurrentAnimation(){
        return this.currentAnimation;
    }

    public int animationsNumber(){
        return this.animations.size();
    }

    public Animation getAnimation(int step){
        fulfilAnimationsToStep(step);
        this.possiblyRunningAnimations.add(step);
        return this.animations.get(step);
    }
}
