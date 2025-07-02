package models;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.*;

public class Chess {

    public static boolean currentPlayer = true; //true when player is white
    public static boolean gameEnded = false;
    public static boolean draw = false;
    public static boolean resign = false;
    public static boolean blackInCheck = false;
    public static boolean whiteInCheck = false;
    public static boolean whiteWin = false;
    public static boolean blackWin = false;
    public static Scanner scan = new Scanner(System.in);

    ArrayList<String> movesList = new ArrayList<String>();
    long gameId = System.currentTimeMillis();


    public static void main(String[] args) {

        Piece[][] board = Board.createNewBoard();
        Board.printBoard(board);
        String rank = "";

        while (!gameEnded) {

            //gets a String array of the input ex: e2 e4
            String[] str = EnterMove();
            int l = str.length;
            if (l == 3) {
                //if input draw
                if (str[2] != null) {
                    if ("draw?".equals(str[2])) {
                        System.out.println("draw");
                        draw = true;
                        System.exit(0);
                    } else if (str[2].equals("Q") || str[2].equals("B") || str[2].equals("R") || str[2].equals("N")) {
                        rank = str[2];
                    }
                }
            }
            //if input resign
            if ("resign".equals(str[0])) {
                if (currentPlayer == true) {
                    System.out.println("Black wins");
                    System.exit(0);
                } else {
                    System.out.println("White wins");
                    System.exit(0);
                }
                resign = true;
                gameEnded = true;
                break;
            }

            //gets input array of rows and columns (first and second space)
            int[] inputRowsCols = handleInput(str[0], str[1]);

            int firstRow = inverseRow(inputRowsCols[0]);
            int firstCol = inputRowsCols[1];
            int secondRow = inverseRow(inputRowsCols[2]);
            int secondCol = inputRowsCols[3];

            Piece[][] tempBoard = new Piece[8][8];
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    tempBoard[i][j] = board[i][j];
                }
            }

            //checks if valid
            boolean valid = checkValid(firstRow, firstCol, secondRow, secondCol, board, currentPlayer);
            if (valid) {
                tempBoard = movePiece(inverseRow(inputRowsCols[0]), inputRowsCols[1], inverseRow(inputRowsCols[2]), inputRowsCols[3], tempBoard, currentPlayer);
                if (moveIsSafe(tempBoard)) {
                    if (isCheckmate(tempBoard)) {
                        System.out.println("Checkmate");
                        gameEnded = true;
                        if (currentPlayer) {
                            whiteWin = true;
                        } else {
                            blackWin = true;
                        }
                    }
                    setChecks(tempBoard);
                    if (whiteInCheck) {
                        System.out.println("White is in check!");
                    } else if (blackInCheck) {
                        System.out.println("Black is in check!");
                    }
                    if (board[firstRow][firstCol].getType().equals("Pawn")) {
                        board = ((Pawn) board[firstRow][firstCol]).promote(firstRow, firstCol, secondRow, rank, board);
                    }

                    board = movePiece(inverseRow(inputRowsCols[0]), inputRowsCols[1], inverseRow(inputRowsCols[2]), inputRowsCols[3], board, currentPlayer);

                    System.out.println();
                    Board.printBoard(board);
                    System.out.println();

                    currentPlayer = !currentPlayer;
                    rank = "";
                } else {
                    System.out.println("Illegal move (puts you into check), try again");
                    System.out.println();
                }
            } else {
                System.out.println("Illegal move, try again");
                System.out.println();
            }
        }

        if (whiteWin) {
            System.out.println("white wins");
        } else if (blackWin) {
            System.out.println("black wins");
        }

        scan.close();
    }

    public static String[] EnterMove() {
        if (currentPlayer) {
            System.out.print("White's Move: ");
        } else {
            System.out.print("Black's Move: ");
        }

        String input = scan.nextLine();
        String[] str = input.split(" ");

        return str;
    }

    public static int[] handleInput(String firstSpace, String secondSpace) {
        char firstChar = firstSpace.charAt(0);
        char secondChar = secondSpace.charAt(0);

        int firstCol = (int) firstChar - 97;
        int secondCol = (int) secondChar - 97;

        int firstRow = Integer.parseInt(firstSpace.substring(1)) - 1;
        int secondRow = Integer.parseInt(secondSpace.substring(1)) - 1;

        int[] inputRowsCols = {firstRow, firstCol, secondRow, secondCol};
        return inputRowsCols;


    }

    public static boolean checkValid(int firstRow, int firstCol, int secondRow, int secondCol, Piece[][] board, boolean whiteTurn) {
        if (whiteTurn == false) {
            currentPlayer = false;
        } else {
            currentPlayer = true;
        }

        if (firstRow == secondRow && firstCol == secondCol) {
            return false;
        }
        if ((Board.onBoard(secondRow, secondCol, board)) == false) {
            return false;
        }
        if (board[secondRow][secondCol] != null) {
            if ((board[secondRow][secondCol]).getColor().equals("black") && currentPlayer == false || (board[secondRow][secondCol]).getColor().equals("white") && currentPlayer == true) {
                return false;
            }
        }
        Piece temp = board[firstRow][firstCol];
        String type = temp.getType(); //"Rook", "Knight", etc
        if ("Rook".equals(type)) {
            return ((Rook) temp).isValid(firstRow, firstCol, secondRow, secondCol, board);
        } else if ("Knight".equals(type)) {
            return ((Knight) temp).isValid(firstRow, firstCol, secondRow, secondCol, board);
        } else if ("Bishop".equals(type)) {
            return ((Bishop) temp).isValid(firstRow, firstCol, secondRow, secondCol, board);
        } else if ("Queen".equals(type)) {
            return ((Queen) temp).isValid(firstRow, firstCol, secondRow, secondCol, board);
        } else if ("King".equals(type)) {
            return ((King) temp).isValid(firstRow, firstCol, secondRow, secondCol, board);
        } else if ("Pawn".equals(type)) {
            Piece lastMove = null;
            return ((Pawn) temp).isValid(firstRow, firstCol, secondRow, secondCol, board);
        }

        return true;
    }


    public static Piece[][] movePiece(int row, int col, int newRow, int newCol, Piece[][] board, boolean whiteTurn) {
        currentPlayer = whiteTurn;
        Piece temp = board[row][col];
        if (currentPlayer && board[newRow][newCol] != null && board[newRow][newCol].getType().equalsIgnoreCase("King") && board[newRow][newCol].getColor().equalsIgnoreCase("black")) {
            whiteWin = true;
            gameEnded = true;
        } else if (!currentPlayer && board[newRow][newCol] != null && board[newRow][newCol].getType().equalsIgnoreCase("King") && board[newRow][newCol].getColor().equalsIgnoreCase("white")) {
            blackWin = true;
            gameEnded = true;
        }

        board[row][col] = null;
        board[newRow][newCol] = temp;
        return board;
    }

    public static int inverseRow(int row) {
        return 7 - row;
    }

    public static int[] playersKing(String color, Piece board[][]) {
        int pos[] = new int[2];
        if (color.equals("white")) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board[i][j] != null) {
                        if (board[i][j].getType().equals("King") && board[i][j].getColor().equals("white")) {
                            pos[0] = board[i][j].getRow(); //row of white's king
                            pos[1] = board[i][j].getCol(); //col of white's king
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (board[i][j] != null) {
                        if (board[i][j].getType().equals("King") && board[i][j].getColor().equals("black")) {
                            pos[0] = board[i][j].getRow(); //row of black's king
                            pos[1] = board[i][j].getCol(); //col of black's king
                        }
                    }
                }
            }
        }
        return pos;
    }

    public static boolean moveIsSafe(Piece tempBoard[][]) {
        currentPlayer = !currentPlayer;
        setChecks(tempBoard);
        currentPlayer = !currentPlayer;
        if (currentPlayer && whiteInCheck == true) {
            return false;
        } else if (!currentPlayer && blackInCheck == true) {
            return false;
        }
        return true;
    }

    public static void setChecks(Piece[][] tempBoard) {
        int whiteKingPos[] = new int[2];
        int blackKingPos[] = new int[2];
        whiteKingPos = playersKing("white", tempBoard);
        blackKingPos = playersKing("black", tempBoard);

        if (currentPlayer) { //If it's white's turn see if black is in check
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (tempBoard[i][j] != null && tempBoard[i][j].getColor().equals("white") && checkValid(i, j, blackKingPos[0], blackKingPos[1], tempBoard, currentPlayer)) {
                        blackInCheck = true;
                        return;
                    }
                }
            }
            blackInCheck = false;
        } else if (!currentPlayer) { //If it's black's turn see if white is in check
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (tempBoard[i][j] != null && tempBoard[i][j].getColor().equals("black") && checkValid(i, j, whiteKingPos[0], whiteKingPos[1], tempBoard, currentPlayer)) {
                        whiteInCheck = true;
                    }
                }
            }
            whiteInCheck = false;
        }
    }

    public static boolean isCheckmate(Piece[][] tempBoard) {
        // Check if the king is in check
        setChecks(tempBoard);
        if (currentPlayer) {
            if (!blackInCheck) {
                return false; // Black king is not in check, so not in checkmate
            }
        } else {
            if (!whiteInCheck) {
                return false; // White king is not in check, so not in checkmate
            }
        }

        // Check if the king can move out of check
        int[] kingPos = currentPlayer ? playersKing("black", tempBoard) : playersKing("white", tempBoard);
        for (int i = kingPos[0] - 1; i <= kingPos[0] + 1; i++) {
            for (int j = kingPos[1] - 1; j <= kingPos[1] + 1; j++) {
                if (i >= 0 && i < 8 && j >= 0 && j < 8 && (i != kingPos[0] || j != kingPos[1])) {
                    Piece temp = tempBoard[i][j];
                    tempBoard[i][j] = tempBoard[kingPos[0]][kingPos[1]];
                    tempBoard[kingPos[0]][kingPos[1]] = null;
                    setChecks(tempBoard);
                    if (currentPlayer ? !blackInCheck : !whiteInCheck) {
                        // King can move out of check, so not in checkmate
                        tempBoard[kingPos[0]][kingPos[1]] = tempBoard[i][j];
                        tempBoard[i][j] = temp;
                        return false;
                    }
                    tempBoard[kingPos[0]][kingPos[1]] = tempBoard[i][j];
                    tempBoard[i][j] = temp;
                }
            }
        }

        // Check if any piece can block or capture the attacking piece(s)
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece temp = tempBoard[i][j];
                if (temp != null && temp.getColor().equals(currentPlayer ? "black" : "white")) {
                    for (int x = 0; x < 8; x++) {
                        for (int y = 0; y < 8; y++) {
                            if (checkValid(i, j, x, y, tempBoard, currentPlayer)) {
                                tempBoard[x][y] = tempBoard[i][j];
                                tempBoard[i][j] = null;
                                setChecks(tempBoard);
                                if (currentPlayer ? !blackInCheck : !whiteInCheck) {
                                    // Piece can block or capture, so not in checkmate
                                    tempBoard[i][j] = tempBoard[x][y];
                                    tempBoard[x][y] = temp;
                                    return false;
                                }
                                tempBoard[i][j] = tempBoard[x][y];
                                tempBoard[x][y] = temp;
                            }
                        }
                    }
                }
            }
        }

        // King cannot move out of check and no piece can block or capture, so in checkmate
        return true;
    }

    public void writeToFile(Context context, ArrayList<String> movesList) {
        String filename = "game_moves_" + gameId + ".txt";

        try {
            FileOutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            OutputStreamWriter osw = new OutputStreamWriter(outputStream);
            BufferedWriter bw = new BufferedWriter(osw);

            for (String move : movesList) {
                bw.write(move);
                bw.newLine();
            }

            bw.close();
            osw.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> readFromFile(Context context, long gameId) {
        String filename = "game_moves_" + gameId + ".txt";
        ArrayList<String> movesList = new ArrayList<>();

        try {
            FileInputStream inputStream = context.openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(isr);

            String line;
            while ((line = br.readLine()) != null) {
                movesList.add(line);
            }

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return movesList;
    }

    public static boolean castleValid(int firstRow, int firstCol, int secondRow, int secondCol, Piece[][] board, boolean whitePlayer) {
        currentPlayer = whitePlayer;
        Piece tempK = board[firstRow][firstCol];
        if (!(tempK instanceof King)) {
            return false;
        }

        String color = ((King) tempK).getColor();
        boolean moved = ((King) tempK).getMoved();

        if (color.equals("white")) {
            if (!moved) {
                if (secondCol == 6) { // kingside castling
                    for (int i = 5; i <= 6; i++) { // check empty squares between king and rook
                        if (board[7][i] != null) {
                            return false;
                        }
                    }
                    if (board[7][7] != null) {
                        if (board[7][7] instanceof Rook) {
                            if (!((Rook) board[7][7]).getMoved()) {
                                if (board[7][5] == null && board[7][6] == null) {
                                    return true;
                                }
                            }
                        }
                    }
                } else if (secondCol == 2) { // queenside castling
                    for (int i = 3; i >= 1; i--) { // check empty squares between king and rook
                        if (board[7][i] != null) {
                            return false;
                        }
                    }
                    if (board[7][0] != null) {
                        if (board[7][0] instanceof Rook) {
                            if (!((Rook) board[7][0]).getMoved()) {
                                if (board[7][1] == null && board[7][2] == null && board[7][3] == null) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        } else if (color.equals("black") && !currentPlayer) { // check color
            if (!moved) { // check moved
                if (secondCol == 6) { //col g
                    for (int i = 5; i <= 6; i++) { // check empty between knight and rook
                        if (board[firstRow][i] != null) {
                            return false;
                        }
                    }
                    if (board[firstRow][7] != null) {
                        if (board[firstRow][7].getType().equals("Rook")) { // check rook is there
                            if (((Rook) board[firstRow][7]).getMoved()) { // check rook moved
                                return false;
                            }
                        } else {
                            return false;
                        }
                    } else {
                        return false;
                    }
                    return true;
                } else if (secondCol == 2) {// col c
                    for (int i = 3; i >= 1; i--) { // check empty between knight and rook
                        if (board[firstRow][i] != null) {
                            return false;
                        }
                    }
                    if (board[firstRow][0] != null) {
                        if (board[firstRow][0].getType().equals("Rook")) { // check rook is there
                            if (((Rook) board[firstRow][0]).getMoved()) { // check rook moved
                                return false;
                            } else {
                                return true;
                            }
                        } else {
                            return false;
                        }
                    }
                }
            }
        }
        return false;
    }

}

