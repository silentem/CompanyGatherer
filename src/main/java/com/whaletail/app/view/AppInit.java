package com.whaletail.app.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Whaletail
 */
public class AppInit extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/initScene.fxml"));
        Scene scene = new Scene(new Group(root));
        primaryStage.setScene(scene);
        primaryStage.setMaximized(false);
        primaryStage.setTitle("Company Gatherer");
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
