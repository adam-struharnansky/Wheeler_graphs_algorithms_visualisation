package com.example.demo2;

import com.example.demo2.algorithmDisplays.WindowManager;
import com.example.demo2.multilingualism.LanguageListenerAdder;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    private static final double startingHeight = 750;
    private static final double startingWidth = 1000;

    private void addingObjectWithTextToLanguageListener(VBox vBox){
        MenuBar menuBar = (MenuBar) ((VBox) vBox.getChildren().get(0)).getChildren().get(0);
        //todo - Add LanguageListener also for submenus, sub-submenus, etc...
        for(Menu menu:menuBar.getMenus()){
            LanguageListenerAdder.addLanguageListener(menu.getText(), menu, false);
            for(MenuItem menuItem: menu.getItems()){
                LanguageListenerAdder.addLanguageListener(menuItem.getText(), menuItem, false);
            }
        }
        ((HBox) vBox.getChildren().get(1)).setFillHeight(true);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        VBox vBox = fxmlLoader.load();
        addingObjectWithTextToLanguageListener(vBox);
        Scene scene = new Scene(vBox, startingWidth, startingHeight);

        stage.setTitle("Wheeler graphs");//todo zmenit podla toho, aky nazov to bude mat nakoniec
        stage.setScene(scene);
        stage.show();

        WindowManager.setDisplayHBox((HBox) vBox.getChildren().get(1));

        Pane controllerPane = (Pane) vBox.getChildren().get(2);
        BackgroundFill controllerBackgroundFill = new BackgroundFill(
                        Color.LIGHTGRAY, new CornerRadii(5), new Insets(5));
        Background controllerBackground = new Background(controllerBackgroundFill);
        controllerPane.setBackground(controllerBackground);
        WindowManager.setControllerPane(controllerPane);

        Pane algorithmNamePane = ((Pane)((VBox) vBox.getChildren().get(0)).getChildren().get(1));
        BackgroundFill nameBackgroundFill = new BackgroundFill(
                Color.LIGHTGRAY, new CornerRadii(5), new Insets(5));
        Background nameBackground = new Background(nameBackgroundFill);
        algorithmNamePane.setBackground(nameBackground);
        WindowManager.setAlgorithmNamePane(algorithmNamePane);

        scene.heightProperty().addListener((observableValue, number, t1) -> WindowManager.changeHeight(t1.doubleValue()));
        scene.widthProperty().addListener((observableValue, number, t1) -> WindowManager.changeWidth(t1.doubleValue()));

        WindowManager.changeHeight(startingHeight);
        WindowManager.changeWidth(startingWidth);
    }
    public static void main(String[] args) {
        System.out.println("zaciatok");
        test();
        launch();
    }

    static void test(){
    }
}
