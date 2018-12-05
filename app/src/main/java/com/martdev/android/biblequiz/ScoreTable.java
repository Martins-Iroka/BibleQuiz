package com.martdev.android.biblequiz;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class ScoreTable extends AppCompatActivity {
    private static final String EXTRA_PERCENT_SCORE = "com.martdev.biblequiz.percent_score";
    private EditText mNameText;
    private TextView mScore;
    private Button mSaveButton, mViewButton, mPlayAgain;
    String fileName = "myName";
    String fileScore = "myScore";
    int getScore;

    public static Intent getScore(Context packageContext,int percent_score) {
        Intent intent = new Intent(packageContext, ScoreTable.class);
        intent.putExtra(EXTRA_PERCENT_SCORE, percent_score);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_table);

        getScore = getIntent().getIntExtra(EXTRA_PERCENT_SCORE, 0);

        mNameText = findViewById(R.id.name_text);

        mScore = findViewById(R.id.score);
        mScore.setText(getString(R.string.score_display, getScore));
        mScore.setEnabled(false);

        mSaveButton = findViewById(R.id.save_button);

        mViewButton = findViewById(R.id.view_button);

        mPlayAgain = findViewById(R.id.play_again);
        mPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreTable.this, BibleQuizActivity.class);
                startActivity(intent);
                finish();
            }
        });
        saveScore();
        viewScore();
    }

    private void saveScore() {
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = mNameText.getText().toString();
                String score = mScore.getText().toString();

                if (name.equals(String.valueOf(""))) {
                    mNameText.setError("Please enter your name!");
                } else {
                    try {
                        FileOutputStream nameFile = openFileOutput(fileName, MODE_PRIVATE);
                        FileOutputStream scoreFile = openFileOutput(fileScore, MODE_PRIVATE);
                        OutputStreamWriter outputStreamWriter1 = new OutputStreamWriter(nameFile);
                        OutputStreamWriter outputStreamWriter2 = new OutputStreamWriter(scoreFile);
                        outputStreamWriter1.write(name);
                        outputStreamWriter2.write(score);
                        outputStreamWriter1.close();
                        outputStreamWriter2.close();

                        mNameText.setText("");
                        mNameText.setEnabled(false);
                        mScore.setText("");

                        Toast.makeText(ScoreTable.this, "Score saved.", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void viewScore() {
        mViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileInputStream nameFile = openFileInput(fileName);
                    FileInputStream scoreFile = openFileInput(fileScore);

                    InputStreamReader nameRead = new InputStreamReader(nameFile);
                    InputStreamReader scoreRead = new InputStreamReader(scoreFile);

                    BufferedReader nameReader = new BufferedReader(nameRead);
                    BufferedReader scoreReader = new BufferedReader(scoreRead);

                    StringBuilder nameBuilder = new StringBuilder();
                    StringBuilder scoreBuilder = new StringBuilder();
                    String name;
                    String score;
                    while ((name = nameReader.readLine()) != null | (score = scoreReader.readLine()) != null) {
                        nameBuilder.append(name);
                        scoreBuilder.append(score);
                    }

                    nameFile.close();
                    scoreFile.close();
                    nameRead.close();
                    scoreRead.close();

                    showScore(nameBuilder.toString(), scoreBuilder.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void showScore(String title, String score) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(score);
        builder.create().show();
    }
}
