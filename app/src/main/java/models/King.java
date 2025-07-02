package models;

public class King extends Piece{

    boolean moved; //checks if Piece has moved


    public King (int col, int row, String color, String name){
        this.col = col;
        this.row = row;
        this.color = color;
        this.name = name;
        this.moved = false;
        this.type = "King";
    }

    public String getStringName() {
        return this.name;
    }
    public String getType() {
        return this.type;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public boolean getMoved() {
        return this.moved;
    }

    public boolean isValid (int firstRow, int firstCol, int secondRow, int secondCol, Piece[][] board) {
        //needs to check if King is moving to a place that causes Check

       /* if(firstRow == secondRow && firstCol == secondCol)
            return false;

        if (Math.abs(firstRow - secondRow) <= 1 && Math.abs(firstCol - secondCol) <= 1) {
            setMoved();
            return true;
        }
        else
            return false; */

        if (firstRow == secondRow && firstCol == secondCol) {
            return false;
        }

        int deltaRow = Math.abs(secondRow - firstRow);
        int deltaCol = Math.abs(secondCol - firstCol);

        if (deltaRow <= 1 && deltaCol <= 1) {
            // King is moving at most one square away in each direction
            setMoved();
            return true;
        }

        return false;
    }
    public void setMoved() {
        moved = true;
    }
    public void setMovedFalse() {
        moved = false;
    }
}