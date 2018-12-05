package com.martdev.android.biblequiz;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class CheatActivity extends AppCompatActivity {
    private final static String EXTRA_ANSWER = "com.martdev.android.biblequiz.answer_is_true";
    private final static String EXTRA_ANSWER_SHOWN = "com.martdev.android.biblequiz.answer_is_shown";
    private final static String SAVE_CHEAT = "cheat";
    private TextView mAnswerTextView;
    private Button mShowAnswerButton;

    public static Intent newIntent(Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER, answerIsTrue);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    private boolean mAnswerReceived;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if (savedInstanceState != null) {
            mAnswerReceived = savedInstanceState.getBoolean(SAVE_CHEAT, false);
        }

        mAnswerReceived = getIntent().getBooleanExtra(EXTRA_ANSWER, false);

        mAnswerTextView = findViewById(R.id.show_answer);

        mShowAnswerButton = findViewById(R.id.show_button);
        mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnswer();
                sendResult();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(SAVE_CHEAT, mAnswerReceived);
    }

    private void showAnswer() {
        if (mAnswerReceived) {
            mAnswerTextView.setText(R.string.true_button);
        } else {
            mAnswerTextView.setText(R.string.false_button);
        }
    }

    private void sendResult() {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, true);
        setResult(RESULT_OK, data);
    }
}
