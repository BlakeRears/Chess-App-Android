package com.example.chess;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import models.AlphaComparator;
import models.Recording;

public class ReplayActivity extends AppCompatActivity {
    ListView listView;

    ArrayList<Recording> recordingList = new ArrayList<Recording>(); // Create an ArrayList to store the names

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_replay);
        listView = (ListView) findViewById(R.id.listView);
        readFromFile(this);
        ArrayAdapter<Recording> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, recordingList);
        listView.setAdapter(adapter); //Set the adapter for the ListView

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recording selectedItem = (Recording) listView.getItemAtPosition(position);
                String selectedName = selectedItem.getName();
                //Add code to switch to scene to replay game
                final Intent recordingIntent = new Intent(ReplayActivity.this, RecordingActivity.class);
                recordingIntent.putExtra("selectedName", selectedName);
                startActivity(recordingIntent);
                }
            });


        final Intent homeIntent = new Intent(this, MainActivity.class);
        Button home = (Button)findViewById(R.id.homeButton);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(homeIntent);
            }
        });

        Button byTitleButton = (Button)findViewById(R.id.byTitle);
        byTitleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SORT LIST CODE
                if(recordingList != null) {
                    recordingList.sort(new AlphaComparator());
                    adapter.notifyDataSetChanged();
                }
            }
        });

        Button byDateButton = (Button)findViewById(R.id.byDate);
        byDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //SORT LIST CODE
                if(recordingList != null) {
                    Recording.sortByDate(recordingList);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void readFromFile(Context context) {
       /* try {
            InputStream is = getAssets().open("saves.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            String name;
            String date;
            while ((line = reader.readLine()) != null) {
                name = line;
                date = reader.readLine();
                Recording save = new Recording(name, date);
                recordingList.add(save); //Add each save to the ArrayList
            }
            reader.close();
        } catch (IOException e) {
            //Handle the exception
        } */

        //NEED THIS CODE FOR WHEN WE HAVE CHESS WORKING THE CODE ABOVE WAS JUST A TEST TO SEE IF IT WORKED
       try {
            FileInputStream fis = context.openFileInput("saves.txt");
            InputStreamReader inputStreamReader = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            String name;
            String date;
            while ((line = bufferedReader.readLine()) != null) {
                name = line;
                date = bufferedReader.readLine();
                Recording save = new Recording(name, date);
                recordingList.add(save); //Add each save to the ArrayList
            }
            bufferedReader.close();
            inputStreamReader.close();
            fis.close();
        } catch (IOException e) {
            Log.e("Exception", "File read failed: " + e.toString());
        }

    }


}