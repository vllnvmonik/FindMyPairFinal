package com.example.findmypair;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class GameMediumMode {

    private final Random random = new Random();

    private final ArrayList<Color> memoryBoard = new ArrayList<>(Arrays.asList(Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY, Color.GRAY));
    private final ArrayList<Color> memoryOptions = new ArrayList<>(Arrays.asList(Color.RED, Color.RED, Color.BLUE, Color.BLUE, Color.ORANGE, Color.ORANGE, Color.GREEN, Color.GREEN, Color.VIOLET,Color.VIOLET,Color.PINK,Color.PINK,Color.BROWN,Color.BROWN,Color.BURLYWOOD,Color.BURLYWOOD,Color.CORAL,Color.CORAL));

    public void setupGame(){
        setupMemoryBoard();
    }

    public Color getOptionAtIndex(int index){
        return memoryBoard.get(index);
    }

    public void setupMemoryBoard(){
        // Set initial color of all tiles to gray
        int boardSize = 18;
        for (int i = 0; i < boardSize; i++) {
            memoryBoard.set(i, Color.GRAY);
        }

        // Assign random memory options to the tiles
        for (int i = 0; i < boardSize; i++) {
            Color memoryOption = memoryOptions.get(i);
            int position = random.nextInt(boardSize);

            while (!Objects.equals(memoryBoard.get(position), Color.GRAY)){
                position = random.nextInt(boardSize);
            }

            memoryBoard.set(position, memoryOption);
        }
    }


    public boolean checkTwoPositions(int firstIndex, int secondIndex){
        return memoryBoard.get(firstIndex).equals(memoryBoard.get(secondIndex));
    }
}
