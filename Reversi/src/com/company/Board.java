package com.company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private char[][] cells;
    private int totalEmpty;
    private char turn;

    public void changeTurn() {
        if (turn == 'X') {
            turn = 'O';
        } else {
            turn = 'X';
        }
    }

    public int getTotalX() {
        int total = 0;
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                if (cells[i][j] == 'X') {
                    total++;
                }
            }
        }
        return total;
    }

    public int getTotalO() {
        int total = 0;
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                if (cells[i][j] == 'O') {
                    total++;
                }
            }
        }
        return total;
    }

    public char getWinner() {
        getTotalX();
        if (getTotalX() > getTotalO()) {
            return 'X';
        } else if (getTotalO() > getTotalX()) {
            return 'O';
        } else {
            return 'D';
        }
    }

    public boolean checkAvail(char piece){
        for(int j = 0; j < 8; j++){
            for(int i = 0; i < 8; i++){
                int x = i;
                int y = j;
                char curr = cells[x][y];
                if (curr != '.') {
                    continue;
                }
                boolean res = check(x, y, piece, 0, -1);
                if (res == true) {
                    return true;
                }
                res = check(x, y, piece, 0, 1);
                if (res == true) {
                    return true;
                }
                res = check(x, y, piece, -1, 0);
                if (res == true) {
                    return true;
                }
                res = check(x, y, piece, 1, 0);
                if (res == true) {
                    return true;
                }
                res = check(x, y, piece, -1, -1);
                if (res == true) {
                    return true;
                }
                res = check(x, y, piece, 1, -1);
                if (res == true) {
                    return true;
                }
                res = check(x, y, piece, -1, 1);
                if (res == true) {
                    return true;
                }
                res = check(x, y, piece, 1, 1);
                if (res == true) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isDone() {
        return totalEmpty == 0;
    }

    public Board() {
        startBoard();
    }

    public void revertCell(Coordinates nextMove){
        int x = nextMove.getX();
        int y = nextMove.getY();
        cells[x][y] = '.';
        totalEmpty++;
        return;
    }

    public boolean setCell(Coordinates nextMove) {
        int x = nextMove.getX();
        int y = nextMove.getY();
        if (x < 0 || x > 8 || y < 0 || y > 8) {
            return false;
        }
        if (!checkValidity(nextMove, turn)) {
            return false;
        }
        cells[x][y] = turn;
        totalEmpty--;
        return true;
    }


    public char getTurn() {
        return turn;
    }

    private boolean check(int x, int y, char piece, int offsetX, int offsetY) {
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

    private boolean checkValidity(Coordinates nextMove, char piece) {
        int x = nextMove.getX();
        int y = nextMove.getY();
        char curr = cells[x][y];
        if (curr != '.') {
            return false;
        }
        boolean flag = false;
        boolean res = check(x, y, piece, 0, -1);
        if (res == true) {
            flag = true;
            flipLine(x, y, piece, 0, -1);
        }
        res = check(x, y, piece, 0, 1);
        if (res == true) {
            flag = true;
            flipLine(x, y, piece, 0, 1);
        }
        res = check(x, y, piece, -1, 0);
        if (res == true) {
            flag = true;
            flipLine(x, y, piece, -1, 0);
        }
        res = check(x, y, piece, 1, 0);
        if (res == true) {
            flag = true;
            flipLine(x, y, piece, 1, 0);
        }
        res = check(x, y, piece, -1, -1);
        if (res == true) {
            flag = true;
            flipLine(x, y, piece, -1, -1);
        }
        res = check(x, y, piece, 1, -1);
        if (res == true) {
            flag = true;
            flipLine(x, y, piece, 1, -1);
        }
        res = check(x, y, piece, -1, 1);
        if (res == true) {
            flag = true;
            flipLine(x, y, piece, -1, 1);
        }
        res = check(x, y, piece, 1, 1);
        if (res == true) {
            flag = true;
            flipLine(x, y, piece, 1, 1);
        }
        return flag;
    }

    private void flipLine(int x, int y, char piece, int offsetX, int offsetY) {
        int i = x + offsetX;
        int j = y + offsetY;
        while (i >= 0 && i < 8 && j >= 0 && j < 8) {
            char tmp = cells[i][j];
            if (tmp == piece) {
                return;
            }
            cells[i][j] = piece;
            i += offsetX;
            j += offsetY;
        }
    }


    private void startBoard() {
        this.cells = new char[8][8];
        for (int j = 0; j < 8; j++) {
            for (int i = 0; i < 8; i++) {
                cells[i][j] = '.';
            }
        }
        cells[3][3] = 'X';
        cells[4][4] = 'X';
        cells[3][4] = 'O';
        cells[4][3] = 'O';
        totalEmpty = 60;
        turn = 'O';
    }

    public int getTotalEmpty() {
        return totalEmpty;
    }

    public void displayBoard() {
        System.out.println("  0 1 2 3 4 5 6 7");
        for (int j = 0; j < 8; j++) {
            System.out.print(j);
            System.out.print(" ");
            for (int i = 0; i < 8; i++) {
                System.out.print(cells[i][j]);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public char[][] getCells() {
        return cells;
    }

    public void copy(Board board) {
        char[][] cells = board.getCells();
        for (int j = 0; j < 8; j++) {
            this.cells[j] = Arrays.copyOf(cells[j], cells[j].length);
        }
        this.totalEmpty = board.getTotalEmpty();
        this.turn = board.getTurn();
    }

}
