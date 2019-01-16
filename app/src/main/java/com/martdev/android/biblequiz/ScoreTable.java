package com.martdev.android.biblequiz;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.martdev.android.biblequiz.database.ScoreDbHelper;

public class ScoreTable extends AppCompatActivity {
    private static final String EXTRA_PERCENT_SCORE = "com.martdev.biblequiz.percent_score";
    private static final String SCORE_DIALOG = "ScoreDialog";
    private EditText mNameText;
    private TextView mScore;
    private Button mSaveButton, mViewButton, mPlayAgain;
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
        mScore.setText(String.valueOf(getScore));
        mScore.setEnabled(false);

        mSaveButton = findViewById(R.id.save_button);

        mViewButton = findViewById(R.id.view_button);

        mPlayAgain = findViewById(R.id.play_again);
        mPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                int playerScore = Integer.parseInt(score);

                if (name.equals("")) {
                    mNameText.setError("Please enter your name");
                } else {
                    try {
                        SQLiteDatabase database = new ScoreDbHelper(ScoreTable.this).getWritableDatabase();
                        ScoreDbHelper.insertScore(database, name, playerScore);
                        Toast.makeText(ScoreTable.this, "Data saved", Toast.LENGTH_SHORT).show();
                        mNameText.setEnabled(false);
                        mSaveButton.setEnabled(false);
                    } catch (SQLiteException e) {
                        Toast.makeText(ScoreTable.this, "Database unavailable", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void viewScore() {
        mViewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                ScoreBoard dialog = ScoreBoard.newInstance();
                dialog.show(manager, SCORE_DIALOG);
            }
        });
    }
}
