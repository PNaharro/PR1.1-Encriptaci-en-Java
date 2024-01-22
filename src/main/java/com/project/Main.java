package com.project;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        final int windowWidth = 600;
        final int windowHeight = 400;

        UtilsViews.parentContainer.setStyle("-fx-font: 14 arial;");
        UtilsViews.addView(getClass(), "layout1", "/assets/layout1.fxml");
        UtilsViews.addView(getClass(), "encriptar", "/assets/encriptar.fxml");
        UtilsViews.addView(getClass(), "desencriptar", "/assets/desencriptar.fxml");
        UtilsViews.addView(getClass(), "ok", "/assets/layout2.fxml");
        UtilsViews.addView(getClass(), "error", "/assets/layout3.fxml");

        Scene scene = new Scene(UtilsViews.parentContainer);
        
        stage.setScene(scene);
        stage.setTitle("Eina d'encriptació");
        stage.setMinWidth(windowWidth);
        stage.setMinHeight(windowHeight);
        stage.show();

        // Add icon only if not Mac
        if (!System.getProperty("os.name").contains("Mac")) {
            Image icon = new Image("file:/icons/icon.png");
            stage.getIcons().add(icon);
        }
    }
}