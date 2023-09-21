package com.example.findmypair;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainMenu.fxml")));

        Scene scene = new Scene(root, 1000, 650);
        Image gamelogo_image = new Image("C:\\Users\\Monica Villanueva\\IntelliJ Projects\\findMyPairFinal\\src\\images\\logo.png");
        stage.getIcons().add(gamelogo_image);

        stage.setTitle("Find My Pair: A Color Matching Game");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}