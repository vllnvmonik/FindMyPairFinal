package com.example.findmypair;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;



public class HelloController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Button noExit;
    @FXML
    private Label scoreTotalLabelOneDigit, scoreTotalLabelTwoDigit, scoreTotalLabel;

    @FXML
    void initialize() {
    }

    // this sets the score label
    public void displayScore(int score){
        if(score < 10){
            scoreTotalLabelOneDigit.setText(Integer.toString(score));
            scoreTotalLabelTwoDigit.setOpacity(0);
            scoreTotalLabel.setOpacity(0);
        }else if(score < 100){
            scoreTotalLabelTwoDigit.setText(Integer.toString(score));
            scoreTotalLabelOneDigit.setOpacity(0);
            scoreTotalLabel.setOpacity(0);
        }else{
            scoreTotalLabel.setText(Integer.toString(score));
            scoreTotalLabelOneDigit.setOpacity(0);
            scoreTotalLabelTwoDigit.setOpacity(0);
        }
}

    public void mainMenu(ActionEvent event) throws IOException {
        buttonSEffects();
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mainMenu.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void startGame(ActionEvent event) throws IOException {
        buttonSEffects();
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("startGame.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void easy(ActionEvent event) throws IOException {
        buttonSEffects();
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("easyMode.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void medium(ActionEvent event) throws IOException {
        buttonSEffects();
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("mediumMode.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void hard(ActionEvent event) throws IOException {
        buttonSEffects();
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("hardMode.fxml")));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void scoreboard(ActionEvent event) throws IOException {
        buttonSEffects();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("scoreBoard.fxml"));
        Parent root = loader.load();
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();

        File easyScoresFile = new File("C:\\Users\\Monica Villanueva\\IntelliJ Projects\\findMyPairFinal\\src\\main\\java\\scoresTextFiles\\easyScores.txt");
        File mediumScoresFile = new File("C:\\Users\\Monica Villanueva\\IntelliJ Projects\\findMyPairFinal\\src\\main\\java\\scoresTextFiles\\mediumScores.txt");
        File hardScoresFile = new File("C:\\Users\\Monica Villanueva\\IntelliJ Projects\\findMyPairFinal\\src\\main\\java\\scoresTextFiles\\hardScores.txt");

        Scanner scanEasyScores = new Scanner(easyScoresFile);
        Scanner scanMediumScores = new Scanner(mediumScoresFile);
        Scanner scanHardScores = new Scanner(hardScoresFile);


        // create a new list to store all the scores and another list for the top payb
        ArrayList<Integer> easyAllScores = new ArrayList<Integer>();
        ArrayList<Integer> mediumAllScores = new ArrayList<Integer>();
        ArrayList<Integer> hardAllScores = new ArrayList<Integer>();

        List<Integer> EasyHighestValues = new ArrayList<>();
        List<Integer> MediumHighestValues = new ArrayList<>();
        List<Integer> HardHighestValues = new ArrayList<>();

        // dito iri-read yung every line in our text file
        while(scanEasyScores.hasNextLine()){
            easyAllScores.add(Integer.valueOf(scanEasyScores.nextLine()));
        }

        while(scanMediumScores.hasNextLine()){
            mediumAllScores.add(Integer.valueOf(scanMediumScores.nextLine()));
        }

        while(scanHardScores.hasNextLine()){
            hardAllScores.add(Integer.valueOf(scanHardScores.nextLine()));
        }

        // after i-sort yung all scores, ige-get niya yung index 0-4 at i-aadd sa highscore list
        easyAllScores.sort(Collections.reverseOrder());
        mediumAllScores.sort(Collections.reverseOrder());
        hardAllScores.sort(Collections.reverseOrder());


        for (int i = 0; i < 5; i++) {
            EasyHighestValues.add(easyAllScores.get(i));
            MediumHighestValues.add(mediumAllScores.get(i));
            HardHighestValues.add(hardAllScores.get(i));
        }

        ObservableList<Integer> easyObservableList = FXCollections.observableArrayList(EasyHighestValues);
        ObservableList<Integer> mediumObservableList = FXCollections.observableArrayList(MediumHighestValues);
        ObservableList<Integer> hardObservableList = FXCollections.observableArrayList(HardHighestValues);

        ListView<Integer> easyListView = new ListView<>();
        easyListView.setStyle("-fx-font-size: 18px;");
        easyListView.prefHeightProperty().bind(Bindings.size(easyObservableList).multiply(37.4)); // size ng cells
        easyListView.setItems(easyObservableList);
        easyListView.setPrefWidth(200);

        ListView<Integer> mediumListView = new ListView<>();
        mediumListView.setStyle("-fx-font-size: 18px;");
        mediumListView.prefHeightProperty().bind(Bindings.size(mediumObservableList).multiply(37.4)); // size ng cells
        mediumListView.setItems(mediumObservableList);
        mediumListView.setPrefWidth(200);

        ListView<Integer> hardListView = new ListView<>();
        hardListView.setStyle("-fx-font-size: 18px;");
        hardListView.prefHeightProperty().bind(Bindings.size(hardObservableList).multiply(37.4)); // size ng cells
        hardListView.setItems(hardObservableList);
        hardListView.setPrefWidth(200);

        HBox hboxScores = new HBox();
        hboxScores.setSpacing(10);
        hboxScores.getChildren().addAll(easyListView, mediumListView, hardListView);
        ((Pane) root).getChildren().add(hboxScores);
        hboxScores.setLayoutX(190);
        hboxScores.setLayoutY(280);

        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void clearScores(ActionEvent event) throws IOException {
        Path filePath1 = Paths.get("C:\\Users\\Monica Villanueva\\IntelliJ Projects\\findMyPairFinal\\src\\main\\java\\scoresTextFiles\\easyScores.txt");
        Path filePath2 = Paths.get("C:\\Users\\Monica Villanueva\\IntelliJ Projects\\findMyPairFinal\\src\\main\\java\\scoresTextFiles\\mediumScores.txt");
        Path filePath3 = Paths.get("C:\\Users\\Monica Villanueva\\IntelliJ Projects\\findMyPairFinal\\src\\main\\java\\scoresTextFiles\\hardScores.txt");

        // Read all the lines from the file
        List<String> lines1 = Files.readAllLines(filePath1);
        List<String> lines2 = Files.readAllLines(filePath2);
        List<String> lines3 = Files.readAllLines(filePath3);

        // Select the first five lines
        List<String> firstFiveLines1 = lines1.subList(0, 5);
        List<String> firstFiveLines2 = lines2.subList(0, 5);
        List<String> firstFiveLines3 = lines3.subList(0, 5);

        // Write the first five lines back to the file, overwriting the original contents
        Files.write(filePath1, firstFiveLines1);
        Files.write(filePath2, firstFiveLines2);
        Files.write(filePath3, firstFiveLines3);

        scoreboard(event);
    }

    public void Exit(){
        buttonSEffects();
        Platform.exit();
    }
    public void closeExit(){
        buttonSEffects();
        Stage currentStage = (Stage) noExit.getScene().getWindow();
        currentStage.close();
    }
    public void exitt() throws IOException {
        buttonSEffects();
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("exit.fxml")));
        Stage stage = new Stage();
        scene = new Scene(root);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.show();
    }

    public void buttonSEffects() {
        Media sound = new Media(new File("C:\\Users\\Monica Villanueva\\IntelliJ Projects\\findMyPairFinal\\src\\sounds\\buttons.mp3").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

}