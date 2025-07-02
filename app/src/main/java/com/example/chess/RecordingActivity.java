package com.example.chess;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import models.AlphaComparator;
import models.Bishop;
import models.Chess;
import models.King;
import models.Knight;
import models.Pawn;
import models.Piece;
import models.Queen;
import models.Recording;
import models.Rook;

public class RecordingActivity extends AppCompatActivity {

    public static Piece[][] board;
    public TextView[][] textViewBoard;

    public ArrayList<String> moves = new ArrayList<String>();

    boolean whiteTurn;
    boolean gameStarted;

    public int count;

    private static final int QUEEN_ID = 2131231305;
    private static final int ROOK_ID = 2131231306;
    private static final int KNIGHT_ID = 2131231304;
    private static final int BISHOP_ID = 2131231303;
    boolean promoted = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recording);

        count = 0;

        textViewBoard = new TextView[8][8];
        createNewBoard();
        drawClickableBoard();
        whiteTurn = true;
        gameStarted = false;

        String selectedItem = getIntent().getStringExtra("selectedName");
        String strWithoutSpaces = selectedItem.replaceAll("\\s", "");
        readFromFile(strWithoutSpaces, this);

        final Intent homeIntent = new Intent(this, MainActivity.class);
        Button home = (Button)findViewById(R.id.homeButton2);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(homeIntent);
            }
        });

        Button next = (Button)findViewById(R.id.nextButton);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Code to move piece
                nextMove();
            }
        });
    }

    public void nextMove() {
        if (!moves.isEmpty() && count < moves.size() && moves.get(count) != null) {
            int firstRow = Integer.parseInt(moves.get(count).charAt(0) + "");
            int firstCol = Integer.parseInt(moves.get(count).charAt(1) + "");
            int secondRow = Integer.parseInt(moves.get(count).charAt(2) + "");
            int secondCol = Integer.parseInt(moves.get(count).charAt(3) + "");
            Log.d("VALUES", "Contents of rows/cols: " + firstRow + firstCol + secondRow + secondCol);

            boolean enpassant = false;
            boolean castle = false;
            promoted = false;



            //checks for enpassant
            if (count > 0) {
                System.out.println("" + count);
                String lastMoveString = moves.get(count - 1);
                System.out.println(lastMoveString);
                Piece temp = board[firstRow][firstCol];
                if (temp != null) {
                    if (temp.getType().equals("Pawn")) {
                        enpassant = ((Pawn)temp).checkForEnpassant(firstRow, firstCol, secondRow, secondCol, board, lastMoveString);
                    }
                }
            }




            //checks for castle
            if (!enpassant) {
                System.out.println("checks castle");
                castle = Chess.castleValid(firstRow, firstCol, secondRow, secondCol, board, whiteTurn);

            }



            if (!castle && !enpassant) {
                board = Chess.movePiece(firstRow, firstCol, secondRow, secondCol, board, whiteTurn);
                //checks for promotion
                if (moves.get(count).length() == 5) {
                    System.out.println("length is 5");
                    char piece = moves.get(count).charAt(4);
                    promoted = true;
                    isPromotion(secondRow, secondCol, piece);
                }
                setPieces();
                whiteTurn = !whiteTurn;
                count++;
            }
            if (castle) {
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
                setPieces();
                whiteTurn = !whiteTurn;
                count++;
                return;
            }
            if (enpassant) {
                board[firstRow][secondCol] = null;
                board = Chess.movePiece(firstRow, firstCol, secondRow, secondCol, board, whiteTurn);
                setPieces();
                whiteTurn = !whiteTurn;
                count++;
                return;
            }

        }
        else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("End of Recording");
            builder.setMessage("No more moves in this recording.");
            builder.setPositiveButton("Home", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    final Intent homeIntent = new Intent(RecordingActivity.this, MainActivity.class);
                    startActivity(homeIntent);
                }
            });
            builder.show();
        }
    }

    private void setOnClickListenerForTextView(TextView textView, int row, int col) {
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    public void isPromotion(int row, int col, char piece) {
        boolean isWhite;
        if (board[row][col].getColor().equalsIgnoreCase("white")) {
            isWhite = true;
        } else {
            isWhite = false;
        }
        switch (piece) {
            case 'Q':
                //Handle promotion to queen
                if (isWhite) {
                    promoted = true;
                    System.out.println("gets promoted");
                    board[row][col] = new Queen(col, row, "white", "wQ");
                } else {
                    board[row][col] = new Queen(col, row, "black", "bQ");
                    promoted = true;
                }

                break;
            case 'R':
                //Handle promotion to rook
                if (isWhite) {
                    board[row][col] = new Rook(col, row, "white", "wR");
                    promoted = true;
                } else {
                    board[row][col] = new Rook(col, row, "black", "bR");
                    promoted = true;
                }
                break;
            case 'N':
                //Handle promotion to knight
                if (isWhite) {
                    board[row][col] = new Knight(col, row, "white", "wN");
                    promoted = true;
                } else {
                    board[row][col] = new Knight(col, row, "black", "bN");
                    promoted = true;
                }
                break;
            case 'B':
                //Handle promotion to bishop
                if (isWhite) {
                    board[row][col] = new Bishop(col, row, "white", "wB");
                    promoted = true;
                } else {
                    board[row][col] = new Bishop(col, row, "black", "bB");
                    promoted = true;
                }
                break;
        }
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

    public void setPieces() {
        for (int i = 0; i <= 7; i++) {
            for(int j = 0; j <= 7; j++) {
                Piece temp = board[j][i];
                if (temp != null) {
                    String name = temp.getStringName();
                    if (name.equals("wK")){
                        textViewBoard[i][j].setBackgroundResource(R.drawable.whiteking);
                    }
                    if (name.equals("bK")){
                        textViewBoard[i][j].setBackgroundResource(R.drawable.blackking);
                    }
                    if (name.equals("wQ")){
                        textViewBoard[i][j].setBackgroundResource(R.drawable.whitequeen);
                    }
                    if (name.equals("bQ")){
                        textViewBoard[i][j].setBackgroundResource(R.drawable.blackqueen);
                    }
                    if (name.equals("wB")){
                        textViewBoard[i][j].setBackgroundResource(R.drawable.whitebishop);
                    }
                    if (name.equals("bB")){
                        textViewBoard[i][j].setBackgroundResource(R.drawable.blackbishop);
                    }
                    if (name.equals("wB")){
                        textViewBoard[i][j].setBackgroundResource(R.drawable.whitebishop);
                    }
                    if (name.equals("bR")){
                        textViewBoard[i][j].setBackgroundResource(R.drawable.blackrook);
                    }
                    if (name.equals("wR")){
                        textViewBoard[i][j].setBackgroundResource(R.drawable.whiterook);
                    }
                    if (name.equals("bN")){
                        textViewBoard[i][j].setBackgroundResource(R.drawable.blackknight);
                    }
                    if (name.equals("wN")){
                        textViewBoard[i][j].setBackgroundResource(R.drawable.whiteknight);
                    }
                    if (name.equals("bp")){
                        textViewBoard[i][j].setBackgroundResource(R.drawable.blackpawn);
                    }
                    if (name.equals("wp")){
                        textViewBoard[i][j].setBackgroundResource(R.drawable.whitepawn);
                    }
                }
                else {
                    textViewBoard[i][j].setBackgroundResource(0);
                }


            }
        }
    }

    public void readFromFile(String saveName, Context context) {
        String filename = saveName.replaceAll("\\s", "") + ".txt";
        //NEED THIS CODE FOR WHEN WE HAVE CHESS WORKING THE CODE ABOVE WAS JUST A TEST TO SEE IF IT WORKED
        try {
            FileInputStream inputStream = context.openFileInput(filename);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                moves.add(line);
            }

            Log.d("MyApp", "Contents of movesList: " + moves.toString());
            reader.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}