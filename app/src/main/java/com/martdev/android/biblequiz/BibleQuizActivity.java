package com.martdev.android.biblequiz;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.martdev.android.biblequiz.model.Question;

public class BibleQuizActivity extends AppCompatActivity {
    private final static String KEY_INDEX = "index";
    private final static String SAVE_CHEAT = "cheat";
    private final static int REQUEST_CODE = 0;
    private TextView mQuestionTextView;
    private Button mTrueButton;
    private Button mFalseButton;
    private Button mCheatButton;
    private Button mNextButton;

    Question mQuestionBank[] = {
            new Question(R.string.first_question, true),
            new Question(R.string.second_question, true),
            new Question(R.string.third_question, false),
            new Question(R.string.fourth_question, false),
            new Question(R.string.fifth_question, true),
            new Question(R.string.sixth_question, false)
    };

    private int mCurrentIndex = 0;
    public int mCorrect = 0;
    private int mIncorrect = 0;
    private int mCheatCount = 0;
    private int mCheatAttempt = 3;
    private boolean mCheater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bible_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
            mCheater = savedInstanceState.getBoolean(SAVE_CHEAT, false);
        }

        mQuestionTextView = findViewById(R.id.question_text_view);

        mTrueButton = findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
                disableButton(false);
            }
        });

        mFalseButton = findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
                disableButton(false);
            }
        });

        mCheatButton = findViewById(R.id.cheat_button);
        mCheatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerIsTrue();
                Intent intent = CheatActivity.newIntent(BibleQuizActivity.this, answerIsTrue);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        mNextButton = findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                mCheater = false;
                updateQuestion();
                disableButton(true);
                checkScore();
            }
        });
        updateQuestion();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
        savedInstanceState.putBoolean(SAVE_CHEAT, mCheater);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE) {
            if (data == null) {
                return;
            }
            mCheater = CheatActivity.wasAnswerShown(data);
        }
    }

    @Override
    public void onBackPressed() {
        quitAlertDialog();
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerIsTrue();
        int messageResId;

        if (mCheater) {
            messageResId = R.string.cheat_message;
            mCheatCount++;
            mCheatAttempt--;
            if (mCheatCount == 3) {
                messageResId = R.string.cheat_limit;
                cheatButtonState();
            } else {
                Toast.makeText(this, getString(R.string.cheats_left, mCheatAttempt), Toast.LENGTH_SHORT).show();
            }
        } else {
            if (userPressedTrue == answerIsTrue) {
                messageResId = R.string.correct_toast;
                mCorrect++;
            } else {
                messageResId = R.string.incorrect_toast;
                mIncorrect++;
            }
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
    }

    private void disableButton(boolean enable) {
        mTrueButton.setEnabled(enable);
        mFalseButton.setEnabled(enable);
    }

    private void checkScore() {
        if (mQuestionBank.length == mCorrect + mIncorrect + mCheatCount) {
            mTrueButton.setEnabled(false);
            mFalseButton.setEnabled(false);
            mNextButton.setEnabled(false);
            saveScoreDialog();
        }
    }

    private void quitAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(R.string.quit_title);
        builder.setMessage(R.string.quit_message);
        builder.setPositiveButton(R.string.yes_dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton(R.string.no_dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void saveScoreDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle(R.string.save_score);
        int percent_score = (mCorrect * 100) / mQuestionBank.length;
        builder.setMessage(getString(R.string.score, percent_score));
        builder.setPositiveButton(R.string.yes_dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int percent_score = (mCorrect * 100) / mQuestionBank.length;
                Intent intent = ScoreTable.getScore(BibleQuizActivity.this, percent_score);
                startActivity(intent);
            }
        });

        builder.setNegativeButton(R.string.restart_dialog, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resetGame();
            }
        });
        builder.create().show();
    }

    private void resetGame() {
        mCurrentIndex = 0;
        mCorrect = 0;
        mIncorrect = 0;
        mCheatAttempt = 3;
        mCheatCount = 0;
        mTrueButton.setEnabled(true);
        mFalseButton.setEnabled(true);
        mCheatButton.setEnabled(true);
        mNextButton.setEnabled(true);
    }

    private void cheatButtonState() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int cx = mCheatButton.getWidth() / 2;
            int cy = mCheatButton.getHeight() / 2;
            float radius = mCheatButton.getWidth();
            Animator anim = ViewAnimationUtils.createCircularReveal(mCheatButton, cx, cy, radius, 0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mCheatButton.setVisibility(View.INVISIBLE);
                }
            });
            anim.start();
        } else {
            mCheatButton.setVisibility(View.INVISIBLE);
        }
    }
}
