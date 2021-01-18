package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MonteCarlo {
    private Board boardCopy;
    private List<Coordinates> possibleMovesX = new ArrayList<>();
    private final int NUMTEST = 1000;

    public MonteCarlo(Board board) {
        this.boardCopy = board;
        getPossibleMoves('X', boardCopy, possibleMovesX);
    }

    private void getPossibleMoves(char piece, Board inputBoard, List<Coordinates> possibleMoves) {
        char[][] cells = inputBoard.getCells();
        possibleMoves.clear();
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                int x = i;
                int y = j;
                char curr = cells[x][y];
                if (curr != '.') {
                    continue;
                }
                boolean res = check(x, y, piece, 0, -1, inputBoard);
                if (res == true) {
                    Coordinates coord = new Coordinates(x, y);
                    possibleMoves.add(coord);
                    continue;
                }
                res = check(x, y, piece, 0, 1, inputBoard);
                if (res == true) {
                    Coordinates coord = new Coordinates(x, y);
                    possibleMoves.add(coord);
                }
                res = check(x, y, piece, -1, 0, inputBoard);
                if (res == true) {
                    Coordinates coord = new Coordinates(x, y);
                    possibleMoves.add(coord);
                    continue;
                }
                res = check(x, y, piece, 1, 0, inputBoard);
                if (res == true) {
                    Coordinates coord = new Coordinates(x, y);
                    possibleMoves.add(coord);
                    continue;
                }
                res = check(x, y, piece, -1, -1, inputBoard);
                if (res == true) {
                    Coordinates coord = new Coordinates(x, y);
                    possibleMoves.add(coord);
                    continue;
                }
                res = check(x, y, piece, 1, -1, inputBoard);
                if (res == true) {
                    Coordinates coord = new Coordinates(x, y);
                    possibleMoves.add(coord);
                    continue;
                }
                res = check(x, y, piece, -1, 1, inputBoard);
                if (res == true) {
                    Coordinates coord = new Coordinates(x, y);
                    possibleMoves.add(coord);
                    continue;
                }
                res = check(x, y, piece, 1, 1, inputBoard);
                if (res == true) {
                    Coordinates coord = new Coordinates(x, y);
                    possibleMoves.add(coord);
                    continue;
                }
            }
        }
    }

    private boolean check(int x, int y, char piece, int offsetX, int offsetY, Board inputBoard) {
        char[][] cells = inputBoard.getCells();
        int i = x + offsetX;
        int j = y + offsetY;
        boolean flag = false;
        while (i >= 0 && i < 8 && j >= 0 && j < 8) {
            if (!flag) {
                flag = true;
                char tmp = cells[i][j];
                if (tmp == '.' || tmp == piece) {
                    return false;
                }
            }
            char tmp = cells[i][j];
            if (tmp == '.') {
                return false;
            } else if (tmp == piece) {
                return true;
            } else {
                i += offsetX;
                j += offsetY;
                continue;
            }
        }
        return false;
    }


    // {Win, Loss, Draw}
    public Coordinates getBestMove() {
        HashMap<Coordinates, int[]> results = new HashMap<>();
        for (int i = 0; i < possibleMovesX.size(); i++) {
            results.put(possibleMovesX.get(i), new int[]{0, 0, 0});
        }

        for (Coordinates coords : results.keySet()) {
            for (int i = 1; i <= NUMTEST; i++) {
                Board newBoard = new Board();
                newBoard.copy(boardCopy);
                newBoard.setCell(coords);
                char simResult = simulate(newBoard);
                if (simResult == 'D') {
                    results.get(coords)[2]++;
                }
                if (simResult == 'X') {
                    results.get(coords)[0]++;
                }
                if (simResult == 'O') {
                    results.get(coords)[1]++;
                }

            }
        }

        int max = 0 - NUMTEST;
        Coordinates bestCoord = null;
        for (Coordinates coords : results.keySet()) {
            int draw = results.get(coords)[2];
            int win = results.get(coords)[0];
            int loss = results.get(coords)[1];
            int tmp = draw + win - loss;
            if (tmp >= max) {
                max = tmp;
                bestCoord = coords;
            }
        }
        return bestCoord;
    }

    private char simulate(Board board) {
        List<Coordinates> simMovesX = new ArrayList<>();
        List<Coordinates> simMovesO = new ArrayList<>();
        boolean oneZero = false;
        while (!board.isDone()) {
            char turn = board.getTurn();
            if (turn == 'X') {
                getPossibleMoves(turn, board, simMovesX);
                Random random = new Random();
                if (simMovesX.size() == 0) {
                    if (oneZero) {
                        break;
                    }
                    board.changeTurn();
                    oneZero = true;
                    continue;
                }
                oneZero = false;
                int randnum = random.nextInt(simMovesX.size());
                board.setCell(simMovesX.get(randnum));
            } else {
                getPossibleMoves(turn, board, simMovesO);
                Random random = new Random();
                if (simMovesO.size() == 0) {
                    if (oneZero) {
                        break;
                    }
                    board.changeTurn();
                    oneZero = true;
                    continue;
                }
                oneZero = false;
                int randnum = random.nextInt(simMovesO.size());
                board.setCell(simMovesO.get(randnum));
            }
            board.changeTurn();
        }
        int totalO = board.getTotalO();
        int totalX = board.getTotalX();
        if (totalO > totalX) {
            return 'O';
        } else if (totalX > totalO) {
            return 'X';
        } else {
            return 'D';
        }
    }


}
