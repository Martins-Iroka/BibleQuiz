package com.martdev.android.biblequiz.model;

public class Question {
    private int mTextResId;
    private boolean mAnswerIsTrue;

    public Question(int textResId, boolean answerIsTrue) {
        mTextResId = textResId;
        mAnswerIsTrue = answerIsTrue;
    }

    public int getTextResId() {
        return mTextResId;
    }

    public boolean isAnswerIsTrue() {
        return mAnswerIsTrue;
    }
}
