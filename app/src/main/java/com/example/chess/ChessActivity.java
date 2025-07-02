package com.example.chess;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import models.*;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class ChessActivity extends AppCompatActivity {

    Button undo;
    Button resign;
    Button draw;
    Button AI;

    private static final int QUEEN_ID = 2131231305;
    private static final int ROOK_ID = 2131231306;
    private static final int KNIGHT_ID = 2131231304;
    private static final int BISHOP_ID = 2131231303;

    boolean originalSpotClicked = false;
    ArrayList<Integer> firstSpot;


    public static Piece[][] board;
    public Piece lastEatenPiece;
    public TextView[][] textViewBoard;

    public ArrayList<String> moves = new ArrayList<String>();

    boolean whiteTurn;
    boolean gameStarted;
    boolean undone;
    boolean enpassantDone = false;
    boolean promoted;
    public String moveString;
    public boolean whiteInCheck = false;
    public boolean blackInCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chess);
        textViewBoard = new TextView[8][8];
        createNewBoard();
        drawClickableBoard();
        lastEatenPiece = null;
        whiteTurn = true;
        gameStarted = false;
        firstSpot = new ArrayList<Integer>();
        promoted = false;
        moveString = "";

        Button resign = (Button) findViewById(R.id.resignButton);
        resign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resign(view);
            }
        });

        Button draw = (Button) findViewById(R.id.drawButton);
        draw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                draw(view);
            }
        });

        Button ai = (Button) findViewById(R.id.aiButton);
        ai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CALL METHOD TO AI MOVE
                aiMove();
            }
        });

        Button undo = (Button) findViewById(R.id.undoButton);
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //CALL METHOD TO UNDO MOVE
                undoMove();
            }
        });

    }

    public static void createNewBoard() {
        board = new Piece[8][8];

        // Initialize the board with the starting positions of the pieces
        board[0][0] = new Rook(0, 0, "black", "bR");
        board[0][1] = new Knight(1, 0, "black", "bN");
        board[0][2] = new Bishop(2, 0, "black", "bB");
        board[0][3] = new Queen(3, 0, "black", "bQ");
        board[0][4] = new King(4, 0, "black", "bK");
        board[0][5] = new Bishop(5, 0, "black", "bB");
        board[0][6] = new Knight(6, 0, "black", "bN");
        board[0][7] = new Rook(7, 0, "black", "bR");

        board[1][0] = new Pawn(0, 1, "black", "bp");
        board[1][1] = new Pawn(1, 1, "black", "bp");
        board[1][2] = new Pawn(2, 1, "black", "bp");
        board[1][3] = new Pawn(3, 1, "black", "bp");
        board[1][4] = new Pawn(4, 1, "black", "bp");
        board[1][5] = new Pawn(5, 1, "black", "bp");
        board[1][6] = new Pawn(6, 1, "black", "bp");
        board[1][7] = new Pawn(7, 1, "black", "bp");

        board[6][0] = new Pawn(0, 6, "white", "wp");
        board[6][1] = new Pawn(1, 6, "white", "wp");
        board[6][2] = new Pawn(2, 6, "white", "wp");
        board[6][3] = new Pawn(3, 6, "white", "wp");
        board[6][4] = new Pawn(4, 6, "white", "wp");
        board[6][5] = new Pawn(5, 6, "white", "wp");
        board[6][6] = new Pawn(6, 6, "white", "wp");
        board[6][7] = new Pawn(7, 6, "white", "wp");

        board[7][0] = new Rook(0, 7, "white", "wR");
        board[7][1] = new Knight(1, 7, "white", "wN");
        board[7][2] = new Bishop(2, 7, "white", "wB");
        board[7][3] = new Queen(3, 7, "white", "wQ");
        board[7][4] = new King(4, 7, "white", "wK");
        board[7][5] = new Bishop(5, 7, "white", "wB");
        board[7][6] = new Knight(6, 7, "white", "wN");
        board[7][7] = new Rook(7, 7, "white", "wR");

        //return board;
    }


    public void drawClickableBoard() {
        textViewBoard[0][0] = (TextView) findViewById(R.id.Clickable00);
        textViewBoard[0][1] = (TextView) findViewById(R.id.Clickable01);
        textViewBoard[0][2] = (TextView) findViewById(R.id.Clickable02);
        textViewBoard[0][3] = (TextView) findViewById(R.id.Clickable03);
        textViewBoard[0][4] = (TextView) findViewById(R.id.Clickable04);
        textViewBoard[0][5] = (TextView) findViewById(R.id.Clickable05);
        textViewBoard[0][6] = (TextView) findViewById(R.id.Clickable06);
        textViewBoard[0][7] = (TextView) findViewById(R.id.Clickable07);
        textViewBoard[1][0] = (TextView) findViewById(R.id.Clickable10);
        textViewBoard[1][1] = (TextView) findViewById(R.id.Clickable11);
        textViewBoard[1][2] = (TextView) findViewById(R.id.Clickable12);
        textViewBoard[1][3] = (TextView) findViewById(R.id.Clickable13);
        textViewBoard[1][4] = (TextView) findViewById(R.id.Clickable14);
        textViewBoard[1][5] = (TextView) findViewById(R.id.Clickable15);
        textViewBoard[1][6] = (TextView) findViewById(R.id.Clickable16);
        textViewBoard[1][7] = (TextView) findViewById(R.id.Clickable17);
        textViewBoard[2][0] = (TextView) findViewById(R.id.Clickable20);
        textViewBoard[2][1] = (TextView) findViewById(R.id.Clickable21);
        textViewBoard[2][2] = (TextView) findViewById(R.id.Clickable22);
        textViewBoard[2][3] = (TextView) findViewById(R.id.Clickable23);
        textViewBoard[2][4] = (TextView) findViewById(R.id.Clickable24);
        textViewBoard[2][5] = (TextView) findViewById(R.id.Clickable25);
        textViewBoard[2][6] = (TextView) findViewById(R.id.Clickable26);
        textViewBoard[2][7] = (TextView) findViewById(R.id.Clickable27);
        textViewBoard[3][0] = (TextView) findViewById(R.id.Clickable30);
        textViewBoard[3][1] = (TextView) findViewById(R.id.Clickable31);
        textViewBoard[3][2] = (TextView) findViewById(R.id.Clickable32);
        textViewBoard[3][3] = (TextView) findViewById(R.id.Clickable33);
        textViewBoard[3][4] = (TextView) findViewById(R.id.Clickable34);
        textViewBoard[3][5] = (TextView) findViewById(R.id.Clickable35);
        textViewBoard[3][6] = (TextView) findViewById(R.id.Clickable36);
        textViewBoard[3][7] = (TextView) findViewById(R.id.Clickable37);
        textViewBoard[4][0] = (TextView) findViewById(R.id.Clickable40);
        textViewBoard[4][1] = (TextView) findViewById(R.id.Clickable41);
        textViewBoard[4][2] = (TextView) findViewById(R.id.Clickable42);
        textViewBoard[4][3] = (TextView) findViewById(R.id.Clickable43);
        textViewBoard[4][4] = (TextView) findViewById(R.id.Clickable44);
        textViewBoard[4][5] = (TextView) findViewById(R.id.Clickable45);
        textViewBoard[4][6] = (TextView) findViewById(R.id.Clickable46);
        textViewBoard[4][7] = (TextView) findViewById(R.id.Clickable47);
        textViewBoard[5][0] = (TextView) findViewById(R.id.Clickable50);
        textViewBoard[5][1] = (TextView) findViewById(R.id.Clickable51);
        textViewBoard[5][2] = (TextView) findViewById(R.id.Clickable52);
        textViewBoard[5][3] = (TextView) findViewById(R.id.Clickable53);
        textViewBoard[5][4] = (TextView) findViewById(R.id.Clickable54);
        textViewBoard[5][5] = (TextView) findViewById(R.id.Clickable55);
        textViewBoard[5][6] = (TextView) findViewById(R.id.Clickable56);
        textViewBoard[5][7] = (TextView) findViewById(R.id.Clickable57);
        textViewBoard[6][0] = (TextView) findViewById(R.id.Clickable60);
        textViewBoard[6][1] = (TextView) findViewById(R.id.Clickable61);
        textViewBoard[6][2] = (TextView) findViewById(R.id.Clickable62);
        textViewBoard[6][3] = (TextView) findViewById(R.id.Clickable63);
        textViewBoard[6][4] = (TextView) findViewById(R.id.Clickable64);
        textViewBoard[6][5] = (TextView) findViewById(R.id.Clickable65);
        textViewBoard[6][6] = (TextView) findViewById(R.id.Clickable66);
        textViewBoard[6][7] = (TextView) findViewById(R.id.Clickable67);
        textViewBoard[7][0] = (TextView) findViewById(R.id.Clickable70);
        textViewBoard[7][1] = (TextView) findViewById(R.id.Clickable71);
        textViewBoard[7][2] = (TextView) findViewById(R.id.Clickable72);
        textViewBoard[7][3] = (TextView) findViewById(R.id.Clickable73);
        textViewBoard[7][4] = (TextView) findViewById(R.id.Clickable74);
        textViewBoard[7][5] = (TextView) findViewById(R.id.Clickable75);
        textViewBoard[7][6] = (TextView) findViewById(R.id.Clickable76);
        textViewBoard[7][7] = (TextView) findViewById(R.id.Clickable77);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                TextView temp = findViewById(getResources().getIdentifier("Clickable" + i + j, "id", getPackageName()));
                setOnClickListenerForTextView(temp, i, j);
            }
        }

        setPieces();

    }

    private void setOnClickListenerForTextView(TextView textView, int row, int col) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idString = getResources().getResourceEntryName(v.getId());
                System.out.println(idString);
                int col = Integer.parseInt(idString.substring(9, 10));
                int row = Integer.parseInt(idString.substring(10, 11));

                //CODE SO IF YOU CLICK ON A PIECE WITH NO VALID MOVE YOU CAN GET OFF OF IT
                if (board[row][col] != null && ((board[row][col].getColor() == "white" && whiteTurn == true) || (board[row][col].getColor() == "black" && whiteTurn == false))) {
                    if (originalSpotClicked) {
                        firstSpot.remove(1);
                        firstSpot.remove(0);
                        firstSpot.add(row);
                        firstSpot.add(col);
                    } else {
                        originalSpotClicked = true;
                        firstSpot.add(row);
                        firstSpot.add(col);
                    }
                } else if (originalSpotClicked) {
                    secondSpotClicked(row, col);
                }

                /*if (originalSpotClicked == false) {
                    firstSpotClicked(row, col);
                }
                else if (originalSpotClicked) {
                    secondSpotClicked(row, col);
                } */
                return;
            }
        });
        return;
    }

    public void firstSpotClicked(int row, int col) {
        if (board[row][col] == null) {
            //error message
            System.out.println("ERROR, Null spot");
            return;
        } else if (board[row][col].getColor() == "white" && whiteTurn == false) {
            //wrong color
            System.out.println("ERROR, Wrong Color");
            return;
        } else if (board[row][col].getColor() == "black" && whiteTurn == true) {
            //wrong color
            System.out.println("ERROR, Wrong Color");
            return;
        } else {
            //highlight square
            originalSpotClicked = true;
            System.out.println("First Spot Clicked");
            firstSpot.add(row);
            firstSpot.add(col);
            System.out.println(firstSpot.get(0) + " " + firstSpot.get(1));
            return;

        }


    }

    public void secondSpotClicked(int secondRow, int secondCol) {
        promoted = false;
        enpassantDone = false;
        int firstRow = firstSpot.get(0);
        int firstCol = firstSpot.get(1);
        boolean enpassant = false;
        boolean validMove = false;
        boolean castle = false;
        moveString = "";
        System.out.println(firstRow + " " + firstCol);
        //check for enpassant
        if (moves.size() != 0) {
            String lastMoveString = moves.get(moves.size()-1);
            Piece temp = board[firstRow][firstCol];
            if (temp.getType().equals("Pawn")) {
                enpassant = ((Pawn)temp).checkForEnpassant(firstRow, firstCol, secondRow, secondCol, board, lastMoveString);
            }
        }

        if (!enpassant) {
            castle = Chess.castleValid(firstRow, firstCol, secondRow, secondCol, board, whiteTurn);
            System.out.println("Castle true or false: " + castle);
        }

        if (castle == false) {
            validMove = Chess.checkValid(firstRow, firstCol, secondRow, secondCol, board, whiteTurn);
        }
        if (validMove == false && castle== false && enpassant == false) {
            System.out.println("Move is not valid");
            //send error message if not
            return;
        }
        else if (validMove && moveIsSafe(firstRow, firstCol, secondRow, secondCol)) {
            System.out.println("Move is valid");
            lastEatenPiece = board[secondRow][secondCol]; // fills null if no piece, fills with piece if piece is present, intentional
            checkForWin(firstRow, firstCol, secondRow, secondCol);
            board = Chess.movePiece(firstRow, firstCol, secondRow, secondCol, board, whiteTurn);
            String moveString = "" + firstRow + firstCol + secondRow + secondCol;

            if(board[secondRow][secondCol].getType().equalsIgnoreCase("Pawn")){
                isPromotion(secondRow, secondCol); //Check for promotion
            }


            isChecked(board, false);
            moves.add(moveString);
            whiteTurn = !whiteTurn;
            originalSpotClicked = false; //resets moves
            firstSpot.remove(1);
            firstSpot.remove(0);
            setPieces();
            undone = false;
            return;
        }
        else if (castle == true) {
            System.out.println("castle");
            Piece tempK = board[firstRow][firstCol];
            board[firstRow][firstCol] = null;
            board[secondRow][secondCol] = tempK;
            if (firstRow == 0 && firstCol == 4) {
                if (secondRow == 0 && secondCol == 6) {
                    Piece tempR = board[0][7];
                    board[0][7] = null;
                    board[0][5] = tempR;
                }
                else if (secondRow == 0 && secondCol == 2) {
                    Piece tempR = board[0][0];
                    board[0][0] = null;
                    board[0][3] = tempR;
                }
            }
            else if (firstRow == 7 && firstCol == 4) {
                if (secondRow == 7 && secondCol == 6) {
                    Piece tempR = board[7][7];
                    board[7][7] = null;
                    board[7][5] = tempR;
                }
                else if (secondRow == 7 && secondCol == 2) {
                    Piece tempR = board[7][0];
                    board[7][0] = null;
                    board[7][3] = tempR;
                }
            }
            lastEatenPiece = board[secondRow][secondCol]; // fills null if no piece, fills with piece if piece is present, intentional
            String moveString = "" + firstRow + firstCol + secondRow + secondCol;
            moves.add(moveString);
            whiteTurn = !whiteTurn;
            originalSpotClicked = false; //resets moves
            firstSpot.remove(1);
            firstSpot.remove(0);
            setPieces();
            undone = false;
            return;
        }
        else if (enpassant) {
            System.out.println("enpassant");
            lastEatenPiece = board[firstRow][secondCol]; // fills null if no piece, fills with piece if piece is present, intentional
            board[firstRow][secondCol] = null;
            checkForWin(firstRow, firstCol, secondRow, secondCol);
            board = Chess.movePiece(firstRow, firstCol, secondRow, secondCol, board, whiteTurn);
            String moveString = "" + firstRow + firstCol + secondRow + secondCol;
            moves.add(moveString);
            whiteTurn = !whiteTurn;
            originalSpotClicked = false; //resets moves
            firstSpot.remove(1);
            firstSpot.remove(0);
            setPieces();
            undone = false;
            enpassantDone = true;
            return;
        }
        //if move is valid, move the pieces
        //if opponent piece dies, get rid of piece, and replace with yours
        //record original and second moves in big move list
        //originalSpotClicked == false
        //change turn
        //clear firstSpot clicked once the turn is done
    }

    public void isPromotion(int row, int col) {
        boolean isWhite;
        if (board[row][col].getColor().equalsIgnoreCase("white")) {
            isWhite = true;
        } else {
            isWhite = false;
        }

        if ((isWhite && row == 0) || (!isWhite && row == 7)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.promotion_dialog, null);
            builder.setView(dialogView);

            RadioGroup promotionGroup = dialogView.findViewById(R.id.promotion_group);

            // Set up the buttons for the dialog box
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    int selectedId = promotionGroup.getCheckedRadioButtonId();

                    Log.d("BEFORE", selectedId + "");
                    System.out.println("" + selectedId);
                    switch (selectedId) {
                        case QUEEN_ID:
                            //Handle promotion to queen
                            if (isWhite) {
                                promoted = true;
                                board[row][col] = new Queen(col, row, "white", "wQ");
                                Log.d("AFTER", "QUEEN");
                                String promotionMove = moves.get(moves.size()-1);
                                moves.remove(moves.size()-1);
                                moves.add("" + promotionMove + "Q");
                                System.out.println(moves.get(moves.size()-1));
                            } else {
                                promoted = true;
                                board[row][col] = new Queen(col, row, "black", "bQ");
                                String promotionMove = moves.get(moves.size()-1);
                                moves.remove(moves.size()-1);
                                moves.add("" + promotionMove + "Q");
                            }
                            break;
                        case ROOK_ID:
                            //Handle promotion to rook
                            if (isWhite) {
                                promoted = true;
                                board[row][col] = new Rook(col, row, "white", "wR");
                                String promotionMove = moves.get(moves.size()-1);
                                moves.remove(moves.size()-1);
                                moves.add("" + promotionMove + "R");
                            }
                            else {
                                promoted = true;
                                board[row][col] = new Rook(col, row, "black", "bR");
                                String promotionMove = moves.get(moves.size()-1);
                                moves.remove(moves.size()-1);
                                moves.add("" + promotionMove + "R");
                            }
                            break;
                        case KNIGHT_ID:
                            //Handle promotion to knight
                            if (isWhite) {
                                promoted = true;
                                board[row][col] = new Knight(col, row, "white", "wN");
                                String promotionMove = moves.get(moves.size()-1);
                                moves.remove(moves.size()-1);
                                moves.add("" + promotionMove + "N");
                            }
                            else {
                                promoted = true;
                                board[row][col] = new Knight(col, row, "black", "bN");
                                String promotionMove = moves.get(moves.size()-1);
                                moves.remove(moves.size()-1);
                                moves.add("" + promotionMove + "N");
                            }
                            break;
                        case BISHOP_ID:
                            //Handle promotion to bishop
                            if (isWhite) {
                                promoted = true;
                                board[row][col] = new Bishop(col, row, "white", "wB");
                                String promotionMove = moves.get(moves.size()-1);
                                moves.remove(moves.size()-1);
                                moves.add("" + promotionMove + "B");
                            }
                            else {
                                promoted = true;
                                board[row][col] = new Bishop(col, row, "black", "bB");
                                String promotionMove = moves.get(moves.size()-1);
                                moves.remove(moves.size()-1);
                                moves.add("" + promotionMove + "B");
                            }
                            break;
                    }
                    dialog.dismiss();
                    setPieces();
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    //stay pawn?
                }
            });

            builder.show();
        }
    }

    public void setPieces() {
        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                Piece temp = board[j][i];
                if (temp != null) {
                    String name = temp.getStringName();
                    if (name.equals("wK")) {
                        textViewBoard[i][j].setBackgroundResource(R.drawable.whiteking);
                    }
                    if (name.equals("bK")) {
                        textViewBoard[i][j].setBackgroundResource(R.drawable.blackking);
                    }
                    if (name.equals("wQ")) {
                        textViewBoard[i][j].setBackgroundResource(R.drawable.whitequeen);
                    }
                    if (name.equals("bQ")) {
                        textViewBoard[i][j].setBackgroundResource(R.drawable.blackqueen);
                    }
                    if (name.equals("wB")) {
                        textViewBoard[i][j].setBackgroundResource(R.drawable.whitebishop);
                    }
                    if (name.equals("bB")) {
                        textViewBoard[i][j].setBackgroundResource(R.drawable.blackbishop);
                    }
                    if (name.equals("wB")) {
                        textViewBoard[i][j].setBackgroundResource(R.drawable.whitebishop);
                    }
                    if (name.equals("bR")) {
                        textViewBoard[i][j].setBackgroundResource(R.drawable.blackrook);
                    }
                    if (name.equals("wR")) {
                        textViewBoard[i][j].setBackgroundResource(R.drawable.whiterook);
                    }
                    if (name.equals("bN")) {
                        textViewBoard[i][j].setBackgroundResource(R.drawable.blackknight);
                    }
                    if (name.equals("wN")) {
                        textViewBoard[i][j].setBackgroundResource(R.drawable.whiteknight);
                    }
                    if (name.equals("bp")) {
                        textViewBoard[i][j].setBackgroundResource(R.drawable.blackpawn);
                    }
                    if (name.equals("wp")) {
                        textViewBoard[i][j].setBackgroundResource(R.drawable.whitepawn);
                    }
                } else {
                    textViewBoard[i][j].setBackgroundResource(0);
                }


            }
        }
    }

    public void aiMove() {
        boolean validMove = false;

        while (!validMove) {
            Random random = new Random();
            int randRow = random.nextInt(8);
            int randCol = random.nextInt(8);
            int randRow2 = random.nextInt(8);
            int randCol2 = random.nextInt(8);

            if (whiteTurn == true) {
                if (board[randRow][randCol] != null && board[randRow][randCol].getColor().equals("white")) {
                    boolean valid = Chess.checkValid(randRow, randCol, randRow2, randCol2, board, whiteTurn);
                    if (valid && moveIsSafe(randRow, randCol, randRow2, randCol2)) {
                        String moveString = "" + randRow + randCol + randRow2 + randCol2;
                        moves.add(moveString);
                        checkForWin(randRow, randCol, randRow2, randCol2);
                        lastEatenPiece = board[randRow2][randCol2]; // fills null if no piece, fills with piece if piece is present, intentional
                        board = Chess.movePiece(randRow, randCol, randRow2, randCol2, board, whiteTurn);
                        if (board[randRow2][randCol2].getType().equalsIgnoreCase("Pawn")) {
                            isPromotion(randRow2, randCol2); //Check for promotion
                        }
                        isChecked(board, false);
                        whiteTurn = !whiteTurn;



                        setPieces();
                        validMove = true;
                        undone = false;
                        return;
                    }
                }
            } else if (whiteTurn == false) {
                if (board[randRow][randCol] != null && board[randRow][randCol].getColor().equals("black")) {
                    boolean valid = Chess.checkValid(randRow, randCol, randRow2, randCol2, board, whiteTurn);
                    if (valid && moveIsSafe(randRow, randCol, randRow2, randCol2)) {
                        System.out.println("Move is valid");
                        String moveString = "" + randRow + randCol + randRow2 + randCol2;
                        moves.add(moveString);
                        checkForWin(randRow, randCol, randRow2, randCol2);
                        lastEatenPiece = board[randRow2][randCol2]; // fills null if no piece, fills with piece if piece is present, intentional
                        board = Chess.movePiece(randRow, randCol, randRow2, randCol2, board, whiteTurn);
                        if (board[randRow2][randCol2].getType().equalsIgnoreCase("Pawn")) {
                            isPromotion(randRow2, randCol2); //Check for promotion
                        }
                        isChecked(board, false);
                        whiteTurn = !whiteTurn;




                        setPieces();
                        validMove = true;
                        undone = false;
                        return;
                    }
                }
            }
        }
    }

    public void undoMove() {
        if (undone == true) {
            return;
        }
        whiteTurn = !whiteTurn;

        String lastMove = moves.get(moves.size() - 1);

        int firstRow = Integer.parseInt(lastMove.substring(0,1));
        int firstCol = Integer.parseInt(lastMove.substring(1,2));
        int secondRow = Integer.parseInt(lastMove.substring(2,3));
        int secondCol = Integer.parseInt(lastMove.substring(3,4));

        if(lastMove.equals("0402") || lastMove.equals("0406") || lastMove.equals("7472") || lastMove.equals("7476")) {
            Piece tempK = board[secondRow][secondCol]; //gets King piece from new spot
            if (tempK.getType().equals("King")) {
                ((King)tempK).setMovedFalse(); //sets it to not actually being moved yet
                board[secondRow][secondCol] = null; //takes King out of new spot
                board[firstRow][firstCol] = tempK; //moves King back to OG spot

                if (secondRow == 0 && secondCol == 4) {
                    if (firstRow == 0 && firstCol == 6) {
                        Piece tempR = board[0][5];
                        ((Rook)tempR).setMovedFalse();
                        board[0][5] = null;
                        board[0][7] = tempR;
                    }
                    else if (secondRow == 0 && secondCol == 2) {
                        Piece tempR = board[0][3];
                        ((Rook)tempR).setMovedFalse();
                        board[0][3] = null;
                        board[0][0] = tempR;
                    }
                }
                else if (firstRow == 7 && firstCol == 4) {
                    if (secondRow == 7 && secondCol == 6) {
                        Piece tempR = board[7][5];
                        ((Rook)tempR).setMovedFalse();
                        board[7][5] = null;
                        board[7][7] = tempR;
                    } else if (secondRow == 7 && secondCol == 2) {
                        Piece tempR = board[7][3];
                        ((Rook)tempR).setMovedFalse();
                        board[7][3] = null;
                        board[7][0] = tempR;
                    }
                }
            }
        }
        else if (enpassantDone == true) {
            board = Chess.movePiece(secondRow, secondCol, firstRow, firstCol, board, whiteTurn);
            board[firstRow][secondCol] = lastEatenPiece;
        }
        else if (promoted == true) {
            if (whiteTurn) {
                board[secondRow][secondCol] = new Pawn(secondCol, secondRow, "white", "wp");
            }
            else {
                board[secondRow][secondCol] = new Pawn(secondCol, secondRow, "black", "bp");
            }
            board = Chess.movePiece(secondRow, secondCol, firstRow, firstCol, board, whiteTurn);
            board[secondRow][secondCol] = lastEatenPiece;
        }
        else {
            if (board[secondRow][secondCol].getType().equals("Pawn")) {
                if (Math.abs(secondRow-firstRow) == 2) {
                    ((Pawn)board[secondRow][secondCol]).setMovedFalse();
                }
            }
            board = Chess.movePiece(secondRow, secondCol, firstRow, firstCol, board, whiteTurn);
            board[secondRow][secondCol] = lastEatenPiece;
        }
        setPieces();
        moves.remove(moves.size()-1);
        undone = true;
        enpassantDone = false;
        promoted = false;

        // have to reset first move for pawns and castling (king and rook), and undo castle

        // System.out.println(firstRow + "" + firstCol + "" + secondRow + "" + secondCol);

    }


    //Call this method when resign button is clicked
    public void resign(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Over");
        builder.setMessage("Sorry, you lost the game.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showSaveDialog();
            }
        });
        builder.show();
    }

    //Call this method when draw button is clicked
    public void draw(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Over");
        builder.setMessage("game ended in a draw.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showSaveDialog();
            }
        });
        builder.show();
    }

    //Call this method when player has won the game
    public void winner(String colorWhoWon) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Over");
        builder.setMessage(colorWhoWon + " won the game.");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showSaveDialog();
            }
        });
        builder.show();
    }

    public void checkForWin(int row, int col, int rowNew, int colNew) {
        if(whiteTurn && board[rowNew][colNew] != null && board[rowNew][colNew].getType().equalsIgnoreCase("King") && board[rowNew][colNew].getColor().equalsIgnoreCase("black")) {
           winner("White");
        }
        else if(!whiteTurn && board[rowNew][colNew] != null && board[rowNew][colNew].getType().equalsIgnoreCase("King") && board[rowNew][colNew].getColor().equalsIgnoreCase("white")) {
            winner("Black");
        }
    }

    //Call this method when game ends
    private void showSaveDialog() {
        //Create a new dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(ChessActivity.this);

        //Set the dialog title and message
        builder.setTitle("Save game?");
        builder.setMessage("Do you want to save the game?");

        //When 'Yes' is clicked do this
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveGame(ChessActivity.this); //Saves the game
            }
        });

        //When 'No' is clicked do this
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final Intent homeIntent = new Intent(ChessActivity.this, MainActivity.class);
                startActivity(homeIntent);
            }
        });

        builder.show(); //Shows the dialogue
    }

    public void saveGame(Context context) {
        //Create a new dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(ChessActivity.this);

        //Set the dialog title and message
        builder.setTitle("Enter a name for the save:");

        //Set up the input field
        final EditText input = new EditText(ChessActivity.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        //Set the 'Save' button action
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String saveName = input.getText().toString(); //Stores the users input in saveName

                //Get the date
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                String currentDate = year + "-" + month + "-" + day;

                try {
                    FileOutputStream fos = context.openFileOutput("saves.txt", Context.MODE_APPEND);
                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fos);
                    outputStreamWriter.write(saveName + "\n" + currentDate + "\n");
                    outputStreamWriter.close();
                    fos.close();
                } catch (IOException e) {
                    Log.e("Exception", "File write failed: " + e.toString());
                }

                String filename = saveName.replaceAll("\\s", "") + ".txt";

                try {
                    FileOutputStream outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
                    OutputStreamWriter osw = new OutputStreamWriter(outputStream);
                    BufferedWriter bw = new BufferedWriter(osw);

                    for (String move : moves) {
                        bw.write(move);
                        bw.newLine();
                    }
                    bw.close();
                    osw.close();
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final Intent homeIntent = new Intent(ChessActivity.this, MainActivity.class);
                startActivity(homeIntent);
            }

        });

        //Set the 'Cancel' button action
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                final Intent homeIntent = new Intent(ChessActivity.this, MainActivity.class);
                startActivity(homeIntent);
            }
        });

        //Show the dialog
        builder.show();
    }

    public int[] playersKing(String color, Piece[][] temp) {
        int pos[] = new int[2];
        if (color.equals("white")) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (temp[i][j] != null) {
                        if (temp[i][j].getType().equals("King") && temp[i][j].getColor().equals("white")) {
                            pos[0] = i; //row of white's king
                            pos[1] = j; //col of white's king
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (temp[i][j] != null) {
                        if (temp[i][j].getType().equals("King") && temp[i][j].getColor().equals("black")) {
                            pos[0] = i; //row of black's king
                            pos[1] = j; //col of black's king
                        }
                    }
                }
            }
        }
        return pos;
    }

    public void isChecked(Piece[][] temp, boolean isTest) {
        int whiteKingPos[] = new int[2];
        int blackKingPos[] = new int[2];
        int count = 0;
        int count1 = 0;
        whiteKingPos = playersKing("white", temp);
        blackKingPos = playersKing("black", temp);
        whiteInCheck = false;
        blackInCheck = false;
        boolean player = whiteTurn;

        System.out.println("Color: " + whiteTurn);
        System.out.println("WHITE KING: " + whiteKingPos[0] + " " + whiteKingPos[1]);
        System.out.println("BLACK KING: " + blackKingPos[0] + " " + blackKingPos[1]);
        if(player == false) {
            player = true;
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (temp[i][j] != null && temp[i][j].getColor().equals("white") && Chess.checkValid(i, j, blackKingPos[0], blackKingPos[1], temp, player)) {
                    blackInCheck = true;
                    if (!isTest) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChessActivity.this);

                    //Set the dialog title and message
                    builder.setTitle("KING IN CHECK");
                    builder.setMessage("Black's king is in check");

                    //When 'Yes' is clicked do this
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //NOTHING
                        }
                    });
                    builder.show(); //Shows the dialogue
                }}
            }
        }
        if(player == true) {
            player = false;
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (temp[i][j] != null && temp[i][j].getColor().equals("black") && Chess.checkValid(i, j, whiteKingPos[0], whiteKingPos[1], temp, player)) {
                    whiteInCheck = true;
                    if(!isTest) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ChessActivity.this);

                    //Set the dialog title and message
                    builder.setTitle("KING IN CHECK");
                    builder.setMessage("White's king is in check");

                    //When 'Yes' is clicked do this
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //NOTHING
                        }
                    });
                    builder.show(); //Shows the dialogue
                }}
            }
        }
    }

    public boolean moveIsSafe(int firstRow, int firstCol, int secondRow, int secondCol) {
        System.out.println("LETS SEE");
        boolean isTest = true;
        Piece[][] tempBoard = new Piece[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                tempBoard[i][j] = board[i][j];
            }
        }

        tempBoard = Chess.movePiece(firstRow, firstCol, secondRow, secondCol, tempBoard, whiteTurn);
        isChecked(tempBoard, isTest);

        if (whiteTurn && whiteInCheck == true) {
            System.out.println("PUTS WHITE IN CHECK");
            return false;
        } else if (!whiteTurn && blackInCheck == true) {
            System.out.println("PUTS BLACK IN CHECK");
            return false;
        }
        System.out.println("NO ONE IN CHECK");
        return true;
    }
}
