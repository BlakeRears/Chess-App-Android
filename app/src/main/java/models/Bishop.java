package models;

public class Bishop extends Piece{
	
    public Bishop(int col, int row, String color, String name){
        this.col = col;
        this.row = row;
        this.color = color;
        this.name = name;
        this.type = "Bishop";
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
       
       if(firstRow == secondRow || firstCol == secondCol)
    	   return false;
       
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
    	   if(board[x][y] != null) {
    		    System.out.println(x);
    		    System.out.println(y);

    		   return false;
    	   }
    	   x += xAxis;
    	   y += yAxis;
       }
       
       return true;

       }        
}