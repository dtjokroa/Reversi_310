package com.company;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MonteCarloH {
    private Board boardCopy;
    private List<Coordinates> possibleMovesO = new ArrayList<>();
    private final int NUMTEST = 1000;

    public MonteCarloH(Board board) {
        this.boardCopy = board;
        getPossibleMoves('O', boardCopy, possibleMovesO);
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

    private boolean edgeHeuristic(Coordinates coords){
        Coordinates topLeft = new Coordinates(0,0);
        Coordinates topRight = new Coordinates(7,0);
        Coordinates bottomLeft = new Coordinates(0,7);
        Coordinates bottomRight = new Coordinates(7,7);
        return coords.equals(topLeft) || coords.equals(topRight) || coords.equals(bottomLeft) || coords.equals(bottomRight);
    }

    private Coordinates mobilityHeuristic(List<Coordinates> possibleMoves, Board board){
        List<Coordinates> possibleMovesOpposite = new ArrayList<>();
        Board newBoard = new Board();
        newBoard.copy(board);
        newBoard.setCell(possibleMoves.get(0));
        getPossibleMoves('X', newBoard, possibleMovesOpposite);
        int min = possibleMovesOpposite.size();
        Coordinates ret = null;

        for(Coordinates coords : possibleMoves){
            newBoard.copy(board);
            possibleMovesOpposite.clear();
            newBoard.setCell(coords);
            getPossibleMoves('X', newBoard, possibleMovesOpposite);
            int tmp = possibleMovesOpposite.size();
            if(min < tmp){
                min = tmp;
                ret = coords;;
            }
        }
        return ret;
    }

    private Coordinates differenceHeuristic(List<Coordinates> possibleMoves, Board board){
        Board newBoard = new Board();
        newBoard.copy(board);
        newBoard.setCell(possibleMoves.get(0));
        int x = newBoard.getTotalX();
        int o = newBoard.getTotalO();
        int diff = o - x;
        int max = diff;
        Coordinates ret = null;

        for(Coordinates coords : possibleMoves){
            newBoard.copy(board);
            newBoard.setCell(coords);
            x = newBoard.getTotalX();
            o = newBoard.getTotalO();
            diff = o - x;
            if(max < diff){
                max = diff;
                ret = coords;
            }
        }
        return ret;
    }

    private boolean isAnEdge(Coordinates coord){
        return coord.getX() > 1 && coord.getX() < 6 || coord.getY() > 1 && coord.getY() < 6;
    }

    private boolean closeToEdge(Coordinates coord){
        return coord.equals(new Coordinates(1,0)) || coord.equals(new Coordinates(0,1)) ||
                coord.equals(new Coordinates(0,6)) || coord.equals(new Coordinates( 6,0)) ||
                coord.equals(new Coordinates(1,7)) || coord.equals(new Coordinates(7,1)) ||
                coord.equals(new Coordinates(6,7)) || coord.equals(new Coordinates(7,6));
    }

    private boolean isADiagonal(Coordinates coord){
        return coord.equals(new Coordinates(1,1)) || coord.equals(new Coordinates(6,1)) ||
                coord.equals(new Coordinates(1,6)) || coord.equals(new Coordinates(6,6));
    }

    // {Win, Loss, Draw}
    public Coordinates getBestMove() {
        HashMap<Coordinates, int[]> results = new HashMap<>();
        for (int i = 0; i < possibleMovesO.size(); i++) {
            results.put(possibleMovesO.get(i), new int[]{0, 0, 0});
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
                    results.get(coords)[1]++;
                }
                if (simResult == 'O') {
                    results.get(coords)[0]++;
                }

            }
        }

        int max = 0 - NUMTEST-500;
        Coordinates bestCoord = null;
        for (Coordinates coords : results.keySet()) {
            int tmp = 0;
            int draw = results.get(coords)[2];
            int win = results.get(coords)[0];
            int loss = results.get(coords)[1];
            tmp = tmp + draw + win - loss;
            if(tmp == NUMTEST){
                return coords;
            }
            if(edgeHeuristic(coords)){
                return coords;
            }
            if(coords == mobilityHeuristic(possibleMovesO, boardCopy)){
                tmp = tmp + 50;
            }
            if(boardCopy.getTotalEmpty() > 32 ){
                if(isAnEdge(coords)){
                    tmp = tmp - 100;
                }
                if(closeToEdge(coords)){
                    tmp = tmp - 500;
                }
            }
            if(boardCopy.getTotalEmpty() > 16){
                if(isADiagonal(coords)){
                    tmp = tmp - 500;
                }
            }
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
            if (turn == 'O') {
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
            } else {
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
