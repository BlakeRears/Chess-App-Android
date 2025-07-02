package models;

import java.util.*;

public class Board {
   // public static Piece[][] board;
	
	public static Piece[][] createNewBoard() {
        Piece[][] board = new Piece[8][8];

		// Initialize the board with the starting positions of the pieces
        board[0][0] = new Rook(0,0,"black", "bR"); 
        board[0][1] = new Knight(1 , 0,"black", "bN"); 
        board[0][2] = new Bishop(2 , 0, "black", "bB"); 
        board[0][3] = new Queen(3 , 0, "black", "bQ"); 
        board[0][4] = new King(4 , 0, "black", "bK"); 
        board[0][5] = new Bishop(5 , 0, "black", "bB"); 
        board[0][6] = new Knight(6 , 0,"black", "bN"); 
        board[0][7] = new Rook(7 , 0,"black", "bR");

        board[1][0] = new Pawn(0 , 1,"black", "bp");
        board[1][1] = new Pawn(1 , 1,"black", "bp");
        board[1][2] = new Pawn(2 , 1,"black", "bp");
        board[1][3] = new Pawn(3 , 1,"black", "bp");
        board[1][4] = new Pawn(4 , 1,"black", "bp");
        board[1][5] = new Pawn(5 , 1,"black", "bp");
        board[1][6] = new Pawn(6 , 1,"black", "bp");
        board[1][7] = new Pawn(7 , 1,"black", "bp");

        board[6][0] = new Pawn(0 , 6,"white", "wp");
        board[6][1] = new Pawn(1 , 6,"white", "wp");
        board[6][2] = new Pawn(2 , 6,"white", "wp");
        board[6][3] = new Pawn(3 , 6,"white", "wp");
        board[6][4] = new Pawn(4 , 6,"white", "wp"); 
        board[6][5] = new Pawn(5 , 6,"white", "wp");
        board[6][6] = new Pawn(6 , 6,"white", "wp");
        board[6][7] = new Pawn(7 , 6,"white", "wp");

        board[7][0] = new Rook(0 , 7,"white", "wR");
        board[7][1] = new Knight(1 , 7,"white", "wN"); 
        board[7][2] = new Bishop(2 , 7,"white", "wB"); 
        board[7][3] = new Queen(3 , 7,"white", "wQ"); 
        board[7][4] = new King(4 , 7,"white", "wK"); 
        board[7][5] = new Bishop(5 , 7,"white", "wB"); 
        board[7][6] = new Knight(6 , 7,"white", "wN"); 
        board[7][7] = new Rook(7 , 7,"white", "wR");

        return board;
	}

        
    public static void printBoard(Piece[][] board) {
		for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (board[row][col] == null) {
                    if (row % 2 == 0 && col % 2 == 0) {
                        System.out.print("   ");
                    }
                    else if (row % 2 == 0 && col % 2 != 0) {
                        System.out.print("## ");
                    }
                    else if (row % 2 != 0 && col % 2 == 0) {
                        System.out.print("## ");
                    }
                    else if (row % 2 != 0 && col % 2 != 0) {
                        System.out.print("   ");
                    }
                }
                else {
                    String printPiece = (board[row][col]).getStringName();
                    System.out.print(printPiece + " ");
                }
            }
            System.out.print(8 - row + " ");
            System.out.println();
        }
        System.out.println(" a  b  c  d  e  f  g  h");
		System.out.println();
	}

    public static boolean isEmpty(int row, int col, Piece[][] board) {
        if (board[row][col] == null) {
            return true;
        }
        return false;
    }
    public static boolean onBoard(int row, int col, Piece[][] board) {
        if (row < 0 || row > 7 || col < 0 || col > 7) {
            return false;
        }
        return true;
    }


}
