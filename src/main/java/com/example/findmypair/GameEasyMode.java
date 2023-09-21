package com.example.findmypair;

import javafx.scene.paint.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class GameEasyMode {


    private final Random random = new Random();

    // this is the array of colors of cards kapag hinide (did not match)
    private final ArrayList<Color> memoryBoard = new ArrayList<>(Arrays.asList(Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY));

    // colors when clicked
    private final ArrayList<Color> memoryOptions = new ArrayList<>(Arrays.asList(Color.RED, Color.RED, Color.BLUE, Color.BLUE, Color.ORANGE, Color.ORANGE, Color.GREEN, Color.GREEN));

    // setting up the game board
    public void setupGame(){
        setupMemoryBoard();
    }

    // getting the index of every buttons
    public Color getOptionAtIndex(int index){
        return memoryBoard.get(index);
    }

    public void setupMemoryBoard(){
        // buttons are only 8
        int boardSize = 8;

        // Set initial color of all tiles to gray
        for (int i = 0; i < boardSize; i++) {
            memoryBoard.set(i, Color.GRAY);
        }

        // assign the colors to the buttons
        for (int i = 0; i < boardSize; i++) {
            Color memoryOption = memoryOptions.get(i);
            int position = random.nextInt(boardSize);

            while (!Objects.equals(memoryBoard.get(position), Color.GRAY)){
                position = random.nextInt(boardSize);
            }

            memoryBoard.set(position, memoryOption);
        }
    }

    // check if the colors match
    public boolean checkTwoPositions(int firstIndex, int secondIndex){
        return memoryBoard.get(firstIndex).equals(memoryBoard.get(secondIndex));
    }

}
