package com.example.chess;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import com.example.chess.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private Button startGameButton;
    private Button watchOldGameButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);

        final Intent startChessGameIntent = new Intent(this, ChessActivity.class);
        final Intent watchReplayIntent = new Intent(this, ReplayActivity.class);

        Button newGame = (Button)findViewById(R.id.newGame);
        Button watchReplay = (Button)findViewById(R.id.watchReplay);

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(startChessGameIntent);
            }
        });

        watchReplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(watchReplayIntent);
            }
        });

    }
}
