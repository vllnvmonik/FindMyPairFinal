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

import java.io.*;
import java.net.URL;
import java.util.*;


public class cardsControllerEasy implements Initializable {
    private static final Integer STARTTIME = 20;
    private final IntegerProperty timeSeconds = new SimpleIntegerProperty(STARTTIME * 100);
    private Timeline timeline;
    private int score;
    private int matchedButtons = 0;
    private boolean firstButtonClicked = false;
    private int firstButtonIndex;
    private int secondButtonIndex;
    private boolean match;
    @FXML
    private Label scoreLabel;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private Button button0, button1, button2, button3, button4, button5, button6, button7, backButton;

    // this is where u will store the buttons
    ArrayList<Button> buttons = new ArrayList<>();
    GameEasyMode GameEasyMode = new GameEasyMode();

    // timelines
    Timeline hideTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> hideButtons()));
    Timeline showTimeline = new Timeline(new KeyFrame(Duration.seconds(0.3), e -> showButtons()));
    Timeline hideReveal = new Timeline(new KeyFrame(Duration.seconds(1.5), e -> hideAll()));

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        GameEasyMode.setupGame(); // set up munaaa

        // add all the buttons to the array
        buttons.addAll(Arrays.asList(button0, button1, button2, button3, button4,
                button5, button6, button7));
        // for the timer, progress is equal to 1.0 para puno then mauubos pag nag start na ang time.
        progressBar.setProgress(1.0);
        progressBar.progressProperty().bind(timeSeconds.divide(STARTTIME * 100.0));
        timeSeconds.set((STARTTIME + 1) * 100);

        // disabling the backbutton para hindi mai-back while the buttons/colors are in reveal mode
        backButton.setDisable(true);
        backButton.setOpacity(1);

        for (int i = 0; i < 8; i++) {
            buttons.get(i).setBackground(new Background(new BackgroundFill(Color.rgb(64, 64, 79), null, null)));
            buttons.get(i).setDisable(true);
            buttons.get(i).setOpacity(1);
            buttons.get(i).setStyle(null);
        }
        showTimeline.play();
    }

    @FXML
    void buttonClicked(ActionEvent event) throws IOException {
        // kapag clinick-----
        showTimeline.stop();
        if(!firstButtonClicked){

            // if hindi nag-match
            if(!match){
                hideButtons(); // magha-hide. nasa baba this methooood
                hideTimeline.stop();
            }
            match = false;
            firstButtonClicked = true;

            //----------------------- FIRST BUTTON -----------------------

            // ang purpose nito is to get the index of first clicked button based sa last character ng ID nila (e.g. button3 is index 3, button0 is index 0)
            String buttonId = ((Control)event.getSource()).getId();
            firstButtonIndex = Integer.parseInt(buttonId.substring(buttonId.length() - 1));

            // change clicked button color
            Color color = GameEasyMode.getOptionAtIndex(firstButtonIndex);
            BackgroundFill backgroundFill = new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY);
            Background background = new Background(backgroundFill);
            buttons.get(firstButtonIndex).setBackground(background);
            buttons.get(firstButtonIndex).setDisable(true); // disable it after na ma-click para di p'wede i-match sa sarili
            buttons.get(firstButtonIndex).setOpacity(1);

            cardSEffects();

            return;
        }

        //----------------------- SECOND BUTTON -----------------------

        // getting the index of the second button
        String buttonId = ((Control)event.getSource()).getId();
        secondButtonIndex = Integer.parseInt(buttonId.substring(buttonId.length() - 1));

        // change clicked button color
        Color color = GameEasyMode.getOptionAtIndex(secondButtonIndex);
        BackgroundFill backgroundFill = new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(backgroundFill);
        buttons.get(secondButtonIndex).setBackground(background);
        buttons.get(secondButtonIndex).setDisable(true);
        buttons.get(secondButtonIndex).setOpacity(1);

        cardSEffects();

        firstButtonClicked = false;

        // check if the two clicked button match
        if(GameEasyMode.checkTwoPositions(firstButtonIndex,secondButtonIndex)){
            score += 50;
            updateLabels();
            match = true;
            matchedButtons += 2;

            matchSEffects();
            hideTimeline.stop();

            // it will load a new scene if na-match na lahat (loads the score.fxml)
            if (matchedButtons == 8) {
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

    // inu-update nito yung label( score text na malaki at napapalitan kung ano man ang score na nakuha mo) nung malaking score
    // naol nag-a-update ng label :,<
    private void updateLabels(){
        scoreLabel.setText(Integer.toString(score));
    }

    // this is for the timer ;>
    private void startTimer() {
        if (timeline != null) {
            timeline.stop();
        }

        timeline = new Timeline();

        timeline.getKeyFrames().add(new KeyFrame(Duration.seconds(STARTTIME + 1), new KeyValue(timeSeconds, 0)));
        timeline.playFromStart();

        timeline.setOnFinished(event -> {
            try {
                loadScoreWindow(); // when the time ran out maglo-load itong method na ito which calls the score.fxml
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    // this method saves/stores the easyMode scores sa "easyScores.txt"
    private void saveScores() {
        try {
            FileWriter fileWriter = new FileWriter("C:\\Users\\Monica Villanueva\\IntelliJ Projects\\findMyPairFinal\\src\\main\\java\\scoresTextFiles\\easyScores.txt", true);
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

    // this method ay para sa paglo-load ng score, yung malaki na score haha. either na-beat mo yung allocated time or not
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

    // ginamit for showTimeline Timeline para mag show yung placements ng colors na ipagpi-pair.
    private void showButtons(){
        for (int i = 0; i < 8; i++) {
            Color color = GameEasyMode.getOptionAtIndex(i);
            BackgroundFill backgroundFill = new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY);
            Background background = new Background(backgroundFill);
            buttons.get(i).setBackground(background);

            hideReveal.play();
            flipSEffects();
        }
    }

    // ginamit sa hideReveal na Timeline para magha-hide uli yung lahat ng buttons after mag show yung colors ng buttons
    private void hideAll(){
        for (int i = 0; i < 8; i++) {
            buttons.get(i).setBackground(new Background(new BackgroundFill(Color.rgb(64, 64, 79), null, null)));
            buttons.get(i).setDisable(false);
            flipSEffects();
        }
        backButton.setDisable(false);
        startTimer();
    }

    // ginamit itong method doon sa hideTimeLine na Timeline para pag hindi nagmatch yung both buttons magha-hide uli siya within 1 sec
    private void hideButtons(){
        buttons.get(firstButtonIndex).setBackground(new Background(new BackgroundFill(Color.rgb(64, 64, 79), null, null)));
        buttons.get(secondButtonIndex).setBackground(new Background(new BackgroundFill(Color.rgb(64, 64, 79), null, null)));
        buttons.get(firstButtonIndex).setDisable(false);
        buttons.get(secondButtonIndex).setDisable(false);
    }

    // kapag clinick mawawala yung styles na nakalagay sa first button and second button na clinick which is yung gray(parang nakataob)
    // para lalabas yung color niya
    public void removeStyle(){
        buttons.get(firstButtonIndex).setStyle(null);
        buttons.get(secondButtonIndex).setStyle(null);
    }

    // kapag clinick yung play sa main menu, ito yung maglo-load na fxml file which is yung pipili na ng levels yung player
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

    // this methods ay para sa mga sound effects ng buttons natin kapag kini-click
    // all of this pababa
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