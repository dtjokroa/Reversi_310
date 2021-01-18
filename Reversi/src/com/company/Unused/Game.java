package com.company.Unused;

import com.company.Board;
import com.company.Coordinates;
import com.company.MonteCarlo;

import javax.sound.midi.SysexMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    private Board board;

    public Game() {
        this.board = new Board();
    }

    private Coordinates getNextMove(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please input your next move: ");
        System.out.print("x coordinate (0-7): ");
        int x = scanner.nextInt();
        System.out.print("y coordinate (0-7): ");
        int y = scanner.nextInt();
        Coordinates coords = new Coordinates(x, y);
        return coords;
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
                    continue;
                }
                invalidMoveX = false;
                MonteCarlo mcs = new MonteCarlo(board);
                nextMove = mcs.getBestMove();
                board.setCell(nextMove);
            }
            else{
                if(!board.checkAvail(turn)){
                    if(invalidMoveX){
                        break;
                    }
                    invalidMoveO = true;
                    continue;
                }
                invalidMoveO = false;
                nextMove = getNextMove();
                boolean res = board.setCell(nextMove);
                if(!res){
                    System.out.println("Invalid move please try again");
                    continue;
                }
            }
            board.changeTurn();
        }

        System.out.println();
        System.out.println("***** FINAL RESULT *****");
        board.displayBoard();
        char winner = board.getWinner();
        System.out.println("The winner is " + winner);
    }
}
