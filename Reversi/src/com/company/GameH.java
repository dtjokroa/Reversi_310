package com.company;

import java.util.ArrayList;
import java.util.List;

public class GameH {
    private Board board;
    private List<Long> mcsTime = new ArrayList<>();
    private List<Long> mcsHTime = new ArrayList<>();
    public GameH() {
        this.board = new Board();
    }

    public void runGame(){
        boolean invalidMoveX = false;
        boolean invalidMoveO = false;
        while(!board.isDone()){
            System.out.println("X is Computer O is Users");
            char turn = board.getTurn();
            System.out.println("It is " + turn + " now");
            board.displayBoard();
            Coordinates nextMove;
            if(turn == 'X'){
                if(!board.checkAvail(turn)){
                    if(invalidMoveO){
                        break;
                    }
                    invalidMoveX = true;
                    board.changeTurn();
                    continue;
                }
                invalidMoveX = false;
                MonteCarlo mcs = new MonteCarlo(board);
                long start = System.currentTimeMillis();
                nextMove = mcs.getBestMove();
                long finish = System.currentTimeMillis();
                long res = finish - start;
                mcsTime.add(res);
                board.setCell(nextMove);
            }
            else{
                if(!board.checkAvail(turn)){
                    if(invalidMoveX){
                        break;
                    }
                    invalidMoveO = true;
                    board.changeTurn();
                    continue;
                }
                invalidMoveO = false;
                MonteCarloH mcsH = new MonteCarloH(board);
                long start = System.currentTimeMillis();
                nextMove = mcsH.getBestMove();
                long finish = System.currentTimeMillis();
                long res = finish - start;
                mcsHTime.add(res);
                board.setCell(nextMove);
            }
            board.changeTurn();
        }

        System.out.println();
        System.out.println("***** FINAL RESULT *****");
        board.displayBoard();
        char winner = board.getWinner();
        System.out.println("The winner is " + winner);
        System.out.println("Score:");
        System.out.print("X: ");
        System.out.println(board.getTotalX());
        System.out.print("O: ");
        System.out.println(board.getTotalO());

    }

    public char getWinner(){
        return board.getWinner();
    }

    public int getTotalX(){
        return board.getTotalX();
    }

    public int getTotalO(){
        return board.getTotalO();
    }

    public long getMcsTime() {
        long sum = 0;
        for(int i = 0; i < mcsTime.size(); i++){
            sum += mcsTime.get(i);
        }
        return sum / mcsTime.size();
    }

    public long getMcsHTime() {
        long sum = 0;
        for(int i = 0; i < mcsHTime.size(); i++){
            sum += mcsHTime.get(i);
        }
        return sum / mcsHTime.size();
    }
}
