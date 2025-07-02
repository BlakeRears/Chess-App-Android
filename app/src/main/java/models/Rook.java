package models;

public class Rook extends Piece{
    //int col
    //int row
    //String color
    //String name
    //boolean valid
    boolean moved; //checks if Piece has moved


    public Rook (int col, int row, String color, String name){
        this.col = col;
        this.row = row;
        this.color = color;
        this.name = name;
        this.moved = false;
        this.type = "Rook";
        this.valid = true;

    }
    public String getType() {
        return this.type;
    }
    public String getStringName() {
        return this.name;
    }
    public boolean getMoved() {
        return this.moved;
    }

    public boolean isValid (int firstRow, int firstCol, int secondRow, int secondCol, Piece[][] board) {
        int xRow = 1;
        int yCol = 1;

        if (firstRow != secondRow && firstCol != secondCol) {
            return false;
        }

        if (firstRow - secondRow != 0) {
            if (firstRow - secondRow < 0) {
                xRow = 1;
            }
            else if (firstRow - secondRow > 0) {
                xRow = -1;
            }
        }
        else if (firstRow - secondRow == 0) {
            if (firstCol - secondCol < 0) {
                yCol = 1;
            }
            else if (firstCol - secondCol > 0) {
                yCol = -1;
            }
        }

        if (firstRow!=secondRow) {
            for (int i = firstRow + xRow; i != secondRow; i+=xRow) {
                if (board[i][firstCol] != null) {
                    return false;
                }
            }
        }
        if (firstCol!=secondCol) {
            for (int i = firstCol + yCol; i != secondCol; i+=yCol) {
                if (board[firstRow][i] != null) {
                    return false;
                }
            }
        }





        return true;
    }

    //correctPath
    //interferenceCheck
    public void setMovedFalse() {
        this.moved = false;
    }





}