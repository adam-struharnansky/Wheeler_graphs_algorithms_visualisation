package com.example.demo2.algorithmDisplays;

import com.example.demo2.algorithmDisplays.animatableNodes.AnimatableText;
import com.example.demo2.algorithmDisplays.animatableNodes.AppearAnimatable;
import com.example.demo2.algorithmDisplays.animatableNodes.ColorAnimatable;
import com.example.demo2.animations.Animation;
import com.example.demo2.animations.AnimationType;
import com.example.demo2.auxiliary.Colors;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;


public class CodeDisplay extends Display{

    //todo - Fix bug: when narrowing display, the program freezes
    //todo - Set some color to "programing" words: int, for, if, while, ...
    //todo - If line contains only space, don't draw it
    //todo - Not possible to use highlighting arrows with and without animation in the same time (program crashes)

    static final double padding = 3.0;

    private final ArrayList<CodeLine> codeLines;
    private final ArrayList<Variable> variables;
    private final Line separator;
    private double size;
    private final Pane pane;

    private final HashSet<CodeLine> highlightedCodeLines;
    private final HashSet<Variable> highlightedVariables;

    class CodeLine implements ColorAnimatable {
        final ArrayList<AnimatableText> contentTexts;
        final AnimatableText numberText;
        final int number;
        String content;
        boolean isHighlighted;

        CodeLine(String text, int lineNumber){
            this.numberText = new AnimatableText(pane, lineNumber+"");
            this.number = lineNumber;
            getPane().getChildren().add(this.numberText);
            this.contentTexts = new ArrayList<>();
            this.isHighlighted = false;
            setText(text);
        }

        void setText(String content){
            this.content = content;
            redraw();
        }

        void redraw(){
            this.contentTexts.forEach(AnimatableText::removeBackground);
            this.contentTexts.forEach(contentText -> getPane().getChildren().remove(contentText));
            this.contentTexts.clear();

            this.numberText.setFont(Font.font(12.0*size));

            String tmp = this.content;
            while(tmp.length() > 0){
                AnimatableText line = new AnimatableText(pane, tmp);
                line.setFont(Font.font(12.0*size));
                this.contentTexts.add(line);
                line.setLayoutX(this.numberText.getLayoutBounds().getWidth() + padding);
                getPane().getChildren().add(line);
                //kontrola ci sa to cely text uz nevojde an jeden riadok
                //todo -pridat do toho aj sirku numberTextu
                if(line.getLayoutBounds().getWidth() <= getWidth() - 2*5*padding){
                    line.redraw();
                    break;
                }
                int str = 0, end = tmp.length();
                while(end > str + 1){
                    int mid = (str + end)/2;
                    line.setText(tmp.substring(0, mid + 1));
                    line.autosize();
                    if(line.getLayoutBounds().getWidth() >= getWidth() - 2.5*padding){
                        end = mid;
                    }
                    else{
                        str = mid;
                    }
                }
                //najst poslednu medzeru
                boolean hasSpace = false;
                for(int i = str;i>0;i--){
                    if(tmp.charAt(i) == ' '){
                        line.setText(tmp.substring(0,i));
                        tmp = tmp.substring(i);
                        hasSpace = true;
                        break;
                    }
                }
                //ak nemal medzeru, tak to iba rozdeli an nejakom mieste
                if(!hasSpace){
                    line.setText(tmp.substring(0,str));
                    tmp = tmp.substring(str);
                }
                line.redraw();
            }
            setHighlighted(this.isHighlighted);
            setColor(getColor());
        }

        public void setHighlighted(boolean highlighted) {
            this.isHighlighted = highlighted;
            if(this.isHighlighted){
                highlight();
            }
            else{
                unhighlight();
            }
        }

        void highlight(){
            this.isHighlighted = true;
            setColor(Colors.highlightingColor);
        }

        void unhighlight(){
            this.isHighlighted = false;
            setColor(Color.TRANSPARENT);
        }

        void setY(double y){
            this.numberText.setLayoutY(y - this.numberText.getLayoutBounds().getCenterY());
            this.numberText.redraw();
            for(AnimatableText text:this.contentTexts){
                text.autosize();
                text.setLayoutY(y - text.getLayoutBounds().getCenterY());
                text.redraw();
                y += text.getLayoutBounds().getHeight();
            }
        }

        double getHeight(){
            this.numberText.autosize();
            double contentHeight = 0.0;
            for(AnimatableText text:this.contentTexts){
                text.autosize();
                contentHeight += text.getLayoutBounds().getHeight() + padding*size;
            }
            if(!this.contentTexts.isEmpty()){//za poslednym podriadkom nie je nutne davat dalsiu medzeru, o to sa stara ina cast
                contentHeight -= padding*size;
            }
            return Math.max(this.numberText.getLayoutBounds().getHeight(), contentHeight);
        }

        void erase(){
            this.contentTexts.forEach(AnimatableText::removeBackground);
            this.contentTexts.forEach(text -> getPane().getChildren().remove(text));
            getPane().getChildren().remove(this.numberText);
        }

        @Override
        public void setColor(Color color) {
            this.numberText.setColor(color);
            this.contentTexts.forEach(text -> text.setColor(color));
        }

        @Override
        public Color getColor() {
            return this.numberText.getColor();
        }
    }

    class Variable implements ColorAnimatable, AppearAnimatable {
        String name;
        String value;
        AnimatableText text;
        boolean isHighlighted;
        Variable(String name, String value){
            this.name = name;
            this.value = value;
            this.text = new AnimatableText(pane);
            getPane().getChildren().add(this.text);
            this.isHighlighted = false;
            redraw();
        }

        void setContent(String name, String value){
            this.name = name;
            this.value = value;
            redraw();
        }

        void setName(String name){
            this.name = name;
            redraw();
        }

        void setValue(String value) {
            this.value = value;
            redraw();
        }

        void redraw(){
            this.text.setFont(Font.font(12.0*size));
            this.text.setText(this.name+" = "+this.value);
            this.text.redraw();
            setHighlighted(this.isHighlighted);
            setColor(this.getColor());
        }

        public void setHighlighted(boolean highlighted) {
            this.isHighlighted = highlighted;
            if(this.isHighlighted){
                highlight();
            }
            else{
                unhighlight();
            }
        }

        void highlight(){
            this.isHighlighted = true;
            this.setColor(Colors.highlightingColor);
        }

        void unhighlight(){
            this.isHighlighted = false;
            this.setColor(Color.TRANSPARENT);
        }

        void setY(double y){
            this.text.autosize();
            this.text.setLayoutY(y - this.text.getLayoutBounds().getMinY());
            this.text.redraw();
        }

        void setX(double x){
            this.text.autosize();
            this.text.setLayoutX(x - this.text.getLayoutBounds().getMinX());
            this.text.redraw();
        }

        double getHeight(){
            this.text.autosize();
            return this.text.getLayoutBounds().getHeight();
        }

        double getWidth(){
            this.text.autosize();
            return this.text.getLayoutBounds().getWidth();
        }

        void erase(){
            this.text.removeBackground();
            getPane().getChildren().remove(this.text);
        }

        @Override
        public void appear() {
            this.text.appear();
        }

        @Override
        public void disappear() {
            this.text.disappear();
        }

        @Override
        public void setOpacity(double opacity) {
            this.text.setOpacity(opacity);
        }

        @Override
        public double getOpacity() {
            return this.text.getOpacity();
        }

        @Override
        public void setColor(Color color) {
            this.text.setColor(color);
        }

        @Override
        public Color getColor() {
            return this.text.getColor();
        }
    }

    public CodeDisplay(VBox container, String name, int ratio) {
        super(container, name, ratio);
        this.pane = super.getPane();
        this.codeLines = new ArrayList<>();
        this.variables = new ArrayList<>();
        this.separator = new Line();
        this.highlightedCodeLines = new HashSet<>();
        this.highlightedVariables = new HashSet<>();
        this.size = 1.0;
        getPane().getChildren().add(this.separator);
        centre();
    }

    @Override
    public void resize(){
        super.resize();
        this.codeLines.forEach(CodeLine::redraw);
        this.variables.forEach(Variable::redraw);
        centre();
    }

    @Override
    public void centre(){
        double y = padding*this.size;
        if(!this.codeLines.isEmpty()){
            y += this.codeLines.get(0).getHeight()/2;
        }
        for(CodeLine codeLine:this.codeLines){
            codeLine.setY(y);
            y += codeLine.getHeight() + padding*this.size;
        }
        this.separator.setStrokeWidth(2.0);
        this.separator.setStartX(padding*this.size);
        this.separator.setStartY(y);
        this.separator.setEndX(getWidth() - padding*this.size);
        this.separator.setEndY(y);
        y += padding*this.size;

        if(!this.variables.isEmpty()){
            y += this.variables.get(0).getHeight()/2;
        }

        int i = 0;
        double x = 0.0;
        double rowHeight = 0.0;
        while(i < this.variables.size()){
            if(x == 0.0 || x + this.variables.get(i).getWidth() + 3.0*this.size*padding < super.getWidth()){
                x += 3.0*this.size*padding;
                this.variables.get(i).setY(y);
                this.variables.get(i).setX(x);
                x += this.variables.get(i).getWidth();
                rowHeight = Math.max(rowHeight, this.variables.get(i).getHeight());
                i++;
            }
            else{
                y += rowHeight + padding*this.size;
                x = 0.0;
            }
        }
    }

    public void addLine(String line){
        CodeLine codeLine = new CodeLine(line, codeLines.size() + 1);
        this.codeLines.add(codeLine);
        centre();
    }

    public void removeLine(){
        if(!this.codeLines.isEmpty()){
            this.codeLines.get(this.codeLines.size() - 1).erase();
            this.codeLines.remove(this.codeLines.get(this.codeLines.size() - 1));
        }
    }

    public void setLineText(int lineNumber, String lineText){
        if(containsLineNumber(lineNumber)){
            int innerLineNumber = lineNumber - 1;
            this.codeLines.get(innerLineNumber).setText(lineText);
            centre();
        }
    }

    public void addVariable(Animation animation, String name, String value){
        if(!containsVariable(name)){
            Variable variable = new Variable(name, value);
            this.variables.add(variable);
            centre();
            animation.addAnimatable(AnimationType.AppearAnimation, variable);
        }
        else{
            animation.addAnimatable(AnimationType.AppearAnimation, storedVariable(name));
        }
    }

    public void addVariable(Animation animation, String name, int value){
        addVariable(animation, name, value+"");
    }

    public void addVariable(Animation animation, String name, char value){
        addVariable(animation, name, value+"");
    }

    public void addVariable(String name, String value){
        if(!containsVariable(name)) {
            Variable variable = new Variable(name, value);
            this.variables.add(variable);
            centre();
        }
    }

    public void addVariable(String name, int value){
        addVariable(name, value+"");
    }

    public void addVariable(String name, char value){
        addVariable(name, value+"");
    }

    public void removeVariable(Animation animation, String name){
        Variable variable = storedVariable(name);
        if(variable != null){
            animation.addAnimatable(AnimationType.DisappearAnimation, variable);
        }
    }

    public void removeVariable(String name){
        Variable variable = storedVariable(name);
        if(variable != null){
            variable.erase();
            this.variables.remove(variable);
        }
    }

    public void setVariableValue(String name, String value){
        Variable variable = storedVariable(name);
        if(variable != null){
            variable.setValue(value);
        }
    }

    public void setVariableValue(String name, int value){
        setVariableValue(name, value+"");
    }

    public void setVariableValue(String name, char value){
        setVariableValue(name, value+"");
    }

    public void highlightVariable(Animation animation, String name){
        Variable variable = storedVariable(name);
        if(variable != null){
            animation.addAnimatable(AnimationType.ColorAnimation, variable, Color.TRANSPARENT, Colors.highlightingColor);
            //variable.setHighlighted(true);
            this.highlightedVariables.add(variable);
        }
    }

    public void highlightVariable(String name){
        Variable variable = storedVariable(name);
        if(variable != null){
            variable.setHighlighted(true);
            this.highlightedVariables.add(variable);
        }
    }

    public void unhighlightVariable(Animation animation, String name){
        Variable variable = storedVariable(name);
        if(variable != null){
            animation.addAnimatable(AnimationType.ColorAnimation, variable, Colors.highlightingColor, Color.TRANSPARENT);
            variable.setHighlighted(true);
            this.highlightedVariables.add(variable);
        }
    }

    public void unhighlightVariable(String name){
        Variable variable = storedVariable(name);
        if(variable != null){
            variable.setHighlighted(false);
            this.highlightedVariables.remove(variable);
        }
    }

    public void highlightLine(Animation animation, int lineNumber){
        if(containsLineNumber(lineNumber)){
            int innerLineNumber = lineNumber - 1;
            this.codeLines.get(innerLineNumber).setHighlighted(true);
            this.highlightedCodeLines.add(this.codeLines.get(innerLineNumber));
            animation.addAnimatable(AnimationType.ColorAnimation, this.codeLines.get(innerLineNumber),
            Color.TRANSPARENT, Colors.highlightingColor);
        }
    }

    public void highlightLine(int lineNumber){
        if(containsLineNumber(lineNumber)){
            int innerLineNumber = lineNumber - 1;
            this.codeLines.get(innerLineNumber).setHighlighted(true);
            this.highlightedCodeLines.add(this.codeLines.get(innerLineNumber));
        }
    }

    public void unhighlightLine(Animation animation, int lineNumber){
        if(containsLineNumber(lineNumber)){
            int innerLineNumber = lineNumber - 1;
            this.codeLines.get(innerLineNumber).setHighlighted(false);
            this.highlightedCodeLines.remove(this.codeLines.get(innerLineNumber));
            animation.addAnimatable(AnimationType.ColorAnimation, this.codeLines.get(innerLineNumber),
                    Colors.highlightingColor, Color.TRANSPARENT);
        }
    }

    public void unhighlightLine(int lineNumber){
        if(containsLineNumber(lineNumber)){
            int innerLineNumber = lineNumber - 1;
            this.codeLines.get(innerLineNumber).setHighlighted(false);
            this.highlightedCodeLines.remove(this.codeLines.get(innerLineNumber));
        }
    }

    public void setSize(double size) {
        this.size = size;
        resize();
    }

    public void unhighlightEverything(){
        this.highlightedCodeLines.forEach(CodeLine::unhighlight);
        this.highlightedVariables.forEach(Variable::unhighlight);
        this.highlightedCodeLines.clear();
        this.highlightedVariables.clear();
    }

    public void unhighlightEverything(Animation animation){
        this.highlightedVariables.forEach(variable -> {
            animation.addAnimatable(AnimationType.ColorAnimation, variable, Colors.highlightingColor, Color.TRANSPARENT);
            variable.unhighlight();
        });
        this.highlightedVariables.clear();
        this.highlightedCodeLines.forEach(codeLine -> {
            animation.addAnimatable(AnimationType.ColorAnimation, codeLine, Colors.highlightingColor, Color.TRANSPARENT);
            codeLine.unhighlight();
        });
        this.highlightedCodeLines.clear();
    }

    public void removeAllVariables(){
        this.variables.forEach(Variable::erase);
        this.variables.clear();
    }

    Variable storedVariable(String name){
        for(Variable variable:this.variables){
            if(variable.name.equals(name)){
                return variable;
            }
        }
        return null;
    }

    public boolean containsVariable(String name){
        return storedVariable(name) != null;
    }

    public boolean containsLineNumber(int lineNumber){
        return 0 < lineNumber && lineNumber <= this.codeLines.size();
    }






    public CodeMemento saveToMemento(){
        return new CodeMemento(this);
    }

    public void restoreFromMemento(CodeMemento codeMemento){
        int cli = 0;
        while(cli < codeMemento.codeLines.size() || cli < this.codeLines.size()){
            if(cli < codeMemento.codeLines.size() && cli < this.codeLines.size()){
                this.codeLines.get(cli).setText(codeMemento.codeLines.get(cli));
            }
            else if(cli < codeMemento.codeLines.size() && cli >= this.codeLines.size()){
                this.codeLines.add(new CodeLine(codeMemento.codeLines.get(cli), cli + 1));
            }
            else{
                break;
            }
            cli++;
        }
        while(cli<this.codeLines.size()){
            this.codeLines.get(cli).erase();
            this.codeLines.remove(cli);
        }

        int vi = 0;
        while(vi < codeMemento.variables.size() || vi < this.variables.size()){
            if(vi < codeMemento.variables.size() && vi < this.variables.size()){
                this.variables.get(vi).setContent(codeMemento.variables.get(vi).getKey(),
                        codeMemento.variables.get(vi).getValue());
            }
            else if(vi < codeMemento.variables.size() && vi >= this.variables.size()){
                this.variables.add(new Variable(codeMemento.variables.get(vi).getKey(),
                        codeMemento.variables.get(vi).getValue()));
            }
            else {
                break;
            }
            vi++;
        }
        while(vi<this.variables.size()){
            this.variables.get(vi).erase();
            this.variables.remove(vi);
        }

        for(Iterator<Variable> i = this.highlightedVariables.iterator();i.hasNext();){
            Variable variable = i.next();
            if(!codeMemento.highlightedVariables.contains(variable.name)){
                variable.unhighlight();
                i.remove();
            }
        }
        for(String name: codeMemento.highlightedVariables){
            highlightVariable(name);
        }

        for(Iterator<CodeLine> i = this.highlightedCodeLines.iterator();i.hasNext();){
            CodeLine codeLine = i.next();
            if(!codeMemento.highlightedCodeLines.contains(codeLine.number)){
                codeLine.unhighlight();
                i.remove();
            }
        }
        for(Integer i: codeMemento.highlightedCodeLines){
            highlightLine(i);
        }
    }

    public static class CodeMemento{
        private final ArrayList<String> codeLines;
        private final ArrayList<Pair<String, String>> variables;
        private final HashSet<Integer> highlightedCodeLines;
        private final HashSet<String> highlightedVariables;
        public CodeMemento(CodeDisplay codeDisplay){

            this.codeLines = new ArrayList<>();
            for(CodeLine codeLine:codeDisplay.codeLines){
                this.codeLines.add(codeLine.content);
            }

            this.variables = new ArrayList<>();
            for(Variable variable:codeDisplay.variables){
                this.variables.add(new Pair<>(variable.name, variable.value));
            }

            this.highlightedCodeLines = new HashSet<>();
            for(CodeLine codeLine: codeDisplay.codeLines){
                this.highlightedCodeLines.add(codeLine.number);
            }

            this.highlightedVariables = new HashSet<>();
            for(Variable variable:codeDisplay.highlightedVariables){
                this.highlightedVariables.add(variable.name);
            }
        }
    }
}


