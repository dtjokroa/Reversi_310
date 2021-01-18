package com.company;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        long[] timeMCS = new long[10];
        long[] timeMCSH = new long[10];
        char[] winners = new char[10];
        int[] totalX = new int[10];
        int[] totalO = new int[10];
        for(int i = 0; i < 10; i++){
            GameH game = new GameH();
            game.runGame();
            timeMCS[i] = game.getMcsTime();
            timeMCSH[i] = game.getMcsHTime();
            winners[i] = game.getWinner();
            totalX[i] = game.getTotalX();
            totalO[i] = game.getTotalO();
        }

        System.out.println("\nDATA FOR DOCUMENT");
        System.out.println(Arrays.toString(timeMCS));
        System.out.println(Arrays.toString(timeMCSH));
        System.out.println(Arrays.toString(winners));
        System.out.println(Arrays.toString(totalO));
        System.out.println(Arrays.toString(totalX));
    }
}
