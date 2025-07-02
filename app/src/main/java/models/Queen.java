package models;

public class Queen extends Piece{
    //int col
    //int row
    //String color
    //String name


    public Queen (int col, int row, String color, String name){
        this.col = col;
        this.row = row;
        this.color = color;
        this.name = name;
        this.type = "Queen";
    }
    
    public String getStringName() {
        return this.name;
    }
    public String getType() {
        return this.type;
    }
    public boolean isValid (int firstRow, int firstCol, int secondRow, int secondCol, Piece[][] board) {
        
        int xAxis;
        int yAxis;
        int x;
        int y;
       
        // if(firstRow == secondRow || firstCol == secondCol)
    	    //return false;
        
        if (firstRow != secondRow && firstCol != secondCol) {
            
            if(firstRow < secondRow)
                xAxis = 1;
            else
                xAxis = -1;
            
            if(firstCol < secondCol)
                yAxis = 1;
            else
                yAxis = -1;
            
            x = firstRow + xAxis;
            y = firstCol + yAxis;
            
            if (Math.abs(firstRow - secondRow) != Math.abs(firstCol - secondCol)) {
                return false;
            }

            while(x != secondRow && y != secondCol) {
                if (board[x][y] != null) {
                        //System.out.println(x);
                        //System.out.println(y);

                    return false;
                }
                x += xAxis;
                y += yAxis;
            }
            
            return true;
        }

        else if ((firstRow == secondRow) ^ (firstCol == secondCol)) {
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

        else {
            return false;
        }


        // return true; 
    }
}
