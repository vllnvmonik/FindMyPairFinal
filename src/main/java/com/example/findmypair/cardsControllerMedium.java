package com.example.findmypair;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.ResourceBundle;

public class cardsControllerMedium implements Initializable {

    private int score;
    private int matchedButtons = 0;

    private static final Integer STARTTIME = 40;

    private final IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME * 100);

    private Timeline timeline;

    ArrayList<Button> buttons = new ArrayList<>();

    GameMediumMode GameMediumMode = new GameMediumMode();

    @FXML
    private Button button0, button1, button2, button3, button4, button5, button6, button7, button8, button9, button10, button11, button12, button13, button14, button15, button16, button17, backButton;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Label scoreLabel;

    Timeline hideTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> hideButtons()));
    Timeline showTimeline = new Timeline(new KeyFrame(Duration.seconds(0.8), e -> showButtons()));
    Timeline hideReveal = new Timeline(new KeyFrame(Duration.seconds(2), e -> hideAll()));

    private boolean firstButtonClicked = false;

    private int firstButtonIndex;
    private int secondButtonIndex;
    private boolean match;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GameMediumMode.setupGame();

        buttons.addAll(Arrays.asList(button0, button1, button2, button3, button4,
                button5, button6, button7, button8, button9, button10, button11,
                button12, button13, button14, button15, button16, button17));

        progressBar.setProgress(1.0);
        progressBar.progressProperty().bind(timeSeconds.divide(STARTTIME * 100.0));
        timeSeconds.set((STARTTIME + 1) * 100);

        backButton.setDisable(true);
        backButton.setOpacity(1);

        for (int i = 0; i < 18; i++) {
            buttons.get(i).setBackground(new Background(new BackgroundFill(Color.rgb(64, 64, 79), null, null)));
            buttons.get(i).setDisable(true);
            buttons.get(i).setOpacity(1);
            buttons.get(i).setStyle(null);
        }
        showTimeline.play();
    }

    @FXML
    void buttonClicked(ActionEvent event) throws IOException {
        showTimeline.stop();
        if(!firstButtonClicked){

            if(!match){
                hideButtons();
                hideTimeline.stop();
            }
            match = false;
            firstButtonClicked = true;

            String buttonId = ((Control)event.getSource()).getId();
            if(buttonId.length() == 7){
                firstButtonIndex = Integer.parseInt(buttonId.substring(buttonId.length() - 1));
            }else {
                firstButtonIndex = Integer.parseInt(buttonId.substring(buttonId.length() - 2));
            }

            Color color = GameMediumMode.getOptionAtIndex(firstButtonIndex);
            BackgroundFill backgroundFill = new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY);
            Background background = new Background(backgroundFill);
            buttons.get(firstButtonIndex).setBackground(background);
            buttons.get(firstButtonIndex).setDisable(true);
            buttons.get(firstButtonIndex).setOpacity(1);

            cardSEffects();

            return;
        }

        cardSEffects();

        String buttonId = ((Control)event.getSource()).getId();
        if(buttonId.length() == 7){
            secondButtonIndex = Integer.parseInt(buttonId.substring(buttonId.length() - 1));
        }else {
            secondButtonIndex = Integer.parseInt(buttonId.substring(buttonId.length() - 2));
        }

        Color color = GameMediumMode.getOptionAtIndex(secondButtonIndex);
        BackgroundFill backgroundFill = new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        buttons.get(secondButtonIndex).setBackground(background);
        buttons.get(secondButtonIndex).setDisable(true);
        buttons.get(secondButtonIndex).setOpacity(1);

        cardSEffects();

        firstButtonClicked = false;

        if(GameMediumMode.checkTwoPositions(firstButtonIndex,secondButtonIndex)){
            score += 50;
            updateLabels();
            match = true;
            matchedButtons += 2;

            matchSEffects();
            hideTimeline.stop();

            if (matchedButtons == 18) {
                loadScoreWindow();
            }
            return;

        }else{
            score -= 5;
            if (score < 0){
                score = 0;
            }
            updateLabels();
        }
        hideTimeline.play();
    }

    private void updateLabels(){
        scoreLabel.setText(Integer.toString(score));

    }

    private void startTimer() {
        if (timeline != null) {
            timeline.stop();
        }

        timeline = new Timeline();

        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(STARTTIME + 1), new KeyValue(timeSeconds, 0)));
        timeline.playFromStart();

        timeline.setOnFinished(event -> {
            try {
                loadScoreWindow();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void saveScores() {
        try {
            FileWriter fileWriter = new FileWriter("C:\\Users\\Monica Villanueva\\IntelliJ Projects\\findMyPairFinal\\src\\main\\java\\scoresTextFiles\\mediumScores.txt", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("" + score);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            bufferedWriter.close();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error while saving highscore");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    public void loadScoreWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("score.fxml"));
        Parent root = loader.load();
        HelloController helloController = loader.getController();
        helloController.displayScore(score);
        Scene newScene = new Scene(root);

        timeline.stop();
        scoreSEffects();

        saveScores();

        // Get the current stage and set the new scene
        Stage currentStage = (Stage) button0.getScene().getWindow();
        currentStage.setScene(newScene);
        currentStage.show();
    }

    private void hideButtons(){
        buttons.get(firstButtonIndex).setBackground(new Background(new BackgroundFill(Color.rgb(64, 64, 79), null, null)));
        buttons.get(secondButtonIndex).setBackground(new Background(new BackgroundFill(Color.rgb(64, 64, 79), null, null)));
        buttons.get(firstButtonIndex).setDisable(false);
        buttons.get(secondButtonIndex).setDisable(false);
    }

    private void showButtons(){
        for (int i = 0; i < 18; i++) {
            Color color = GameMediumMode.getOptionAtIndex(i);
            BackgroundFill backgroundFill = new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY);
            Background background = new Background(backgroundFill);
            buttons.get(i).setBackground(background);

            hideReveal.play();
            flipSEffects();
        }
    }

    private void hideAll(){
        flipSEffects();
        for (int i = 0; i < 18; i++) {
            buttons.get(i).setBackground(new Background(new BackgroundFill(Color.rgb(64, 64, 79), null, null)));
            buttons.get(i).setDisable(false);
            flipSEffects();
        }
        backButton.setDisable(false);
        startTimer();
    }
    public void removeStyle(){
        buttons.get(firstButtonIndex).setStyle(null);
        buttons.get(secondButtonIndex).setStyle(null);
    }

    public void startGame(ActionEvent event) throws IOException {
        buttonSEffects();
        timeline.stop();

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("startGame.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public void cardSEffects() {
        Media sound = new Media(new File("C:\\Users\\Monica Villanueva\\IntelliJ Projects\\findMyPairFinal\\src\\sounds\\bubble.mp3").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }
    public void matchSEffects() {
        Media sound = new Media(new File("C:\\Users\\Monica Villanueva\\IntelliJ Projects\\findMyPairFinal\\src\\sounds\\twinkle.mp3").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }
    public void flipSEffects() {
        Media sound = new Media(new File("C:\\Users\\Monica Villanueva\\IntelliJ Projects\\findMyPairFinal\\src\\sounds\\flip.mp3").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }
    public void scoreSEffects() {
        Media sound = new Media(new File("C:\\Users\\Monica Villanueva\\IntelliJ Projects\\findMyPairFinal\\src\\sounds\\score.mp3").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }
    public void buttonSEffects() {
        Media sound = new Media(new File("C:\\Users\\Monica Villanueva\\IntelliJ Projects\\findMyPairFinal\\src\\sounds\\buttons.mp3").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

}