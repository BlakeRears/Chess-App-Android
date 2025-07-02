package models;

public class Knight extends Piece{

    public Knight (int col, int row, String color, String name) {
        this.col = col;
        this.row = row;
        this.color = color;
        this.name = name;
        this.type = "Knight";
    }

    public String getStringName() {
        return this.name;
    }
    public String getType() {
        return this.type;
    }
    
    public boolean isValid (int firstRow, int firstCol, int secondRow, int secondCol, Piece[][] board) {
        int row = Math.abs(firstRow - secondRow);     
        int col = Math.abs(firstCol - secondCol);
        
        if ((row == 2 && col == 1) || (row == 1 && col == 2)) {
            return true;
        } 
        else {
            return false;
        }
    }
}