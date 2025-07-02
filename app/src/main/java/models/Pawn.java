package models;

public class Pawn extends Piece{
 
    boolean moved; //checks if Piece has moved
	boolean enpassant;

    public Pawn (int col, int row, String color, String name){
        this.col = col;
        this.row = row;
        this.color = color;
        this.name = name;
        this.moved = false;
        this.type = "Pawn";

    }

    public String getStringName() {
        return this.name;
    }
    public String getType() {
        return this.type;
    }
    public boolean isValid (int firstRow, int firstCol, int secondRow, int secondCol, Piece[][] board) {
        int xRow;
        
        if(color.equals("black") && firstRow - secondRow > 0 || firstRow == secondRow) {
        	return false;
        }
        if(color.equals("white") && firstRow - secondRow < 0 || firstRow == secondRow) {
        	return false;
        }
        
        if(color.equals("white"))
        	xRow = -1;
        else
        	xRow = 1;
        
        if(firstCol - secondCol == 0) { //pawn is moving forward
        	if(Math.abs(firstRow - secondRow) == 1 || (Math.abs(firstRow - secondRow) == 2 && moved == false)) {
        		for (int i = firstRow + xRow; i != secondRow + xRow; i += xRow) {
                    if(board[i][firstCol] != null) {
                        return false;
                    }
                }
        		setMoved();
                return true;
        	}
        }
        
        if (Math.abs(firstCol - secondCol) == 1 && secondRow - firstRow == xRow) {
			if (board[secondRow][secondCol] != null) {
				setMoved();
				return true;
			}
			else if (board[secondRow][secondCol] == null){

			}

        }

        
        return false;
    }
    
    public Piece[][] promote(int r, int c, int r2, String rank, Piece[][] board) {
    	if(isPromotable(r2)) {
    		if(color.equals("white")) {
    			if(rank.equals("Q") || rank.equals("B") || rank.equals("R") || rank.equals("N")) {
    			switch (rank) {
    				case "Q":
    					board[r][c] = new Queen(r, c, "white", "wQ");
    					break;
    				case "R":
    					board[r][c] = new Rook(r, c, "white", "wR");
    					break;
    				case "B":
    					board[r][c] = new Bishop(r, c, "white", "wB");
    					break;
    				case "N":
    					board[r][c] = new Knight(r, c, "white", "wN");
    					break;
    			}
    			}
    			else {
    			board[r][c] = new Queen(r, c, "white", "wQ"); //If rank is blank
    			}
    			
    		}
    		else {
    			if(rank.equals("Q") || rank.equals("B") || rank.equals("R") || rank.equals("N")) {
    				switch (rank) {
    				case "Q":
    					board[r][c] = new Queen(r, c, "black", "bQ");
    					break;
    				case "R":
    					board[r][c] = new Rook(r, c, "black", "bR");
    					break;
    				case "B":
    					board[r][c] = new Bishop(r, c, "black", "bB");
    					break;
    				case "N":
    					board[r][c] = new Knight(r, c, "black", "bN");
    					break;
    				}
    			}
    			else {
    			board[r][c] = new Queen(r, c, "black", "bQ"); //If rank is blank
    			}
    		}
    	}
    	
    	return board;
    }
    
    public boolean isPromotable(int r) {
        if ((color.equals("white") && r != 0) || (color.equals("black") && r != 7)) {
            return false;
        }
        return true;
    }
    
    public void setMoved() {
    	moved = true;
    }
	public boolean checkForEnpassant(int firstRow, int firstCol, int secondRow, int secondCol, Piece[][] board, String lastMove) {
		int lastFirstRow = Integer.parseInt(lastMove.substring(0,1));
		int lastFirstCol = Integer.parseInt(lastMove.substring(1,2));
		int lastSecondRow = Integer.parseInt(lastMove.substring(2,3));
		int lastSecondCol = Integer.parseInt(lastMove.substring(3,4));
		if (board[lastSecondRow][lastSecondCol].getType().equals("Pawn")) {
			if (firstRow == lastSecondRow && secondCol == lastSecondCol) {
				if (Math.abs(lastSecondRow-lastFirstRow) == 2) {
					return true;
				}
			}
		}
		return false;
	}

	public void setMovedFalse() {
		this.moved = false;
	}

}