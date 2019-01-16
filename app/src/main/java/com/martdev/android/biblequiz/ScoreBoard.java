package com.martdev.android.biblequiz;

import android.app.Dialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.martdev.android.biblequiz.database.ScoreDbHelper;
import com.martdev.android.biblequiz.database.ScoreSchema;
import com.martdev.android.biblequiz.database.ScoreSchema.Score;
import com.martdev.android.biblequiz.database.ScoreSchema.Score.Cols;

public class ScoreBoard extends DialogFragment {

    public static ScoreBoard newInstance() {
        Bundle args = new Bundle();

        ScoreBoard scoreBoard = new ScoreBoard();
        scoreBoard.setArguments(args);

        return scoreBoard;
    }

    private SQLiteDatabase mDatabase;
    private Cursor mCursor;
    private ListView mScoreList;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.score_list_view, null);

        mScoreList = view.findViewById(R.id.list_view);
        try {
            mDatabase = new ScoreDbHelper(getActivity()).getReadableDatabase();
            mCursor = mDatabase.query(Score.NAME,
                    new String[]{"_id", Cols.PLAYER_NAME, Cols.PLAYER_SCORE},
                    null, null, null, null, null);

            mScoreList.setAdapter(new SimpleCursorAdapter(
                    getActivity(),
                    R.layout.score_list_item,
                    mCursor,
                    new String[]{Cols.PLAYER_NAME, Cols.PLAYER_SCORE},
                    new int[] {R.id.player_name, R.id.player_score},
                    0
            ));
        } catch (SQLiteException e) {
            Toast.makeText(getActivity(), "Database unavailable", Toast.LENGTH_SHORT).show();
        }

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.score_board_dialog)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}
