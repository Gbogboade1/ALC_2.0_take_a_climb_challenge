package com.ayomide.gbogboade.tic_tac_toe;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @author GBOGBOADE AYOMIDE MICHEAL
 * ay4allcrown@gmail.com
 * ALC 2.0
 * ANDROID BEGINNER: TAKE A CLIMB CHALLENGE
 */
public class MainActivity extends AppCompatActivity {

    public enum BoardMatrix {X, O, NONE}

    private RadioGroup numOfPlayersRadioButton;
    private RadioGroup boardSizeRadioGroup;
    private AlertDialog.Builder dialog;

    private TextView player1scoreTextView;
    private TextView player2scoreTextView;

    private ImageView player1NowPlayingImageView;
    private ImageView player2NowPlayingImageView;

    private TextView[] activeTextViews;
    private LinearLayout[] rows;

    private BoardMatrix[][] game = null;
    private boolean COMPUTER_CAN_PLAY;
    private int PLAYER_COUNT = 1;
    private String BLANK_TEXT = "";
    private int BOARD_TYPE = 3;

    private final String PLAYER_1 = "Player 1";
    private final String PLAYER_2 = "Player 2";
    private final String LETTER_X = "X";
    private final String LETTER_O = "O";

    private int player1Score = 0;
    private int player2Score = 0;

    private Spinner player1Spinner;
    private Spinner player2Spinner;

    private String player1GameLetter;
    private String player2GameLetter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gameStartDialog();

        game = new BoardMatrix[BOARD_TYPE][BOARD_TYPE];
        rows = new LinearLayout[5];
        rows[0] = (LinearLayout) findViewById(R.id.row1);
        rows[1] = (LinearLayout) findViewById(R.id.row2);
        rows[2] = (LinearLayout) findViewById(R.id.row3);
        rows[3] = (LinearLayout) findViewById(R.id.row4);
        rows[4] = (LinearLayout) findViewById(R.id.row5);

        player1NowPlayingImageView = (ImageView) findViewById(R.id.player1_imageView);
        player2NowPlayingImageView = (ImageView) findViewById(R.id.player2_imageView);

        player1scoreTextView = (TextView) findViewById(R.id.player1Score_textView);
        player2scoreTextView = (TextView) findViewById(R.id.player2Score_textView);

        ImageView pauseIconImageView = (ImageView) findViewById(R.id.pauseIcon);
        pauseIconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gamePausedDialog();
            }
        });

        //ATTACH TAG TO ALL THE TEXTVIEWS
        int r = 0;
        int c = 0;
        for (LinearLayout row : rows) {
            for (int i = 0; i < row.getChildCount(); i++) {
                FrameLayout layout = (FrameLayout) row.getChildAt(i);
                TextView textView = (TextView) layout.getChildAt(0);
                String tag = "r" + r + "c" + c;
                //ADD TAG TO IDENTIFY INDIVIDUAL TEXTVIEW POSITION ON THE BOARD
                textView.setTag(tag);
                //ADD ONCLICK LISTENER
                textView.setOnClickListener(textViewListerner);
                c++;
            }
            c = 0;
            r++;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        gamePausedDialog();
    }

    @Override
    public void onBackPressed() {
        gamePausedDialog();
    }

    private boolean canCOMPUTER_PLAY() {
        return COMPUTER_CAN_PLAY;
    }

    private void allowCOMPUTER_TO_PLAY(boolean COMPUTER_CAN_PLAY) {
        this.COMPUTER_CAN_PLAY = COMPUTER_CAN_PLAY;
    }

    /**
     * GET THE LETTER PLAYER 2 IS USING
     * @return
     */
    public String getPlayer2GameLetter() {
        return player2GameLetter;
    }

    /**
     * GET THE LETTER PLAYER 1 IS USING
     * @return
     */
    public String getPlayer1GameLetter() {
        return player1GameLetter;
    }

    /**
     * INCREASE PLAYER 1 SCORE AND DISPLAY IT
     */
    private void increasePlayer1Score(){
        player1Score += 1;
        player1scoreTextView.setText(String.format("%d", player1Score));
    }
    /**
     * INCREASE PLAYER 2 SCORE AND DISPLAY IT
     */
    private void increasePlayer2Score(){
        player2Score += 1;
        player2scoreTextView.setText(String.format("%d", player2Score));
    }

    /**
     * USE THE TEXTVIEW TAG TO GET THE ROW AND COLUMN AND RETURN AS INT ARRAY
     *
     * @param textView
     * @return
     */
    private int[] getTextViewCoordinates(TextView textView) {
        int[] coordinates = new int[2];
        String viewTag = (String) textView.getTag();

        int row = Integer.parseInt(viewTag.substring(1, 2));
        int col = Integer.parseInt(viewTag.substring(3, 4));

        coordinates[0] = row;
        coordinates[1] = col;

        return coordinates;
    }

    /**
     * DISPLAY THE GAME START DIALOG
     */
    private void gameStartDialog() {
        dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.game_start_dialog_layout, null);
        dialog.setView(dialogLayout);

        numOfPlayersRadioButton = (RadioGroup) dialogLayout.findViewById(R.id.num_of_players_group);
        boardSizeRadioGroup = (RadioGroup) dialogLayout.findViewById(R.id.board_size_group);
        player1Spinner = (Spinner) dialogLayout.findViewById(R.id.player1_spinner);
        player2Spinner = (Spinner) dialogLayout.findViewById(R.id.player2_spinner);

        String[] letters = {LETTER_X, LETTER_O};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_spinner_dropdown_item, letters);

        player1Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (player1Spinner.getSelectedItemPosition() == 0) {
                    player2Spinner.setSelection(1);
                } else if (player1Spinner.getSelectedItemPosition() == 1) {
                    player2Spinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        player2Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (player2Spinner.getSelectedItemPosition() == 0) {
                    player1Spinner.setSelection(1);
                } else if (player2Spinner.getSelectedItemPosition() == 1) {
                    player1Spinner.setSelection(0);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        player1Spinner.setAdapter(adapter);
        player2Spinner.setAdapter(adapter);
        player1Spinner.setSelection(0);
        player2Spinner.setSelection(1);

        dialog.setNegativeButton(getString(R.string.start), startButtonListerner);

        dialog.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    /**
     * DIALOG WHENEVER GAME IS PAUSED
     */
    private void gamePausedDialog() {
        dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.game_pause_dialog_layout, null);
        dialog.setView(dialogLayout);
        dialog.setPositiveButton("CONTINUE GAME", null);
        dialog.setNegativeButton("RESTART GAME", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                resetBoard(BOARD_TYPE);
            }
        });
        dialog.setNeutralButton("EXIT GAME", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                Intent intent = new Intent(MainActivity.this, LauncherScreenActivity.class);
                startActivity(intent);
            }
        });

        dialog.create();
        dialog.show();
    }

    /**
     * OPEN THE DIALOG SHOWING THE GAME CONCLUSION WIN OR DRAW
     *
     * @param conclusion
     */
    private void gameConclusionDialog(BoardMatrix conclusion) {
        dialog = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogLayout = inflater.inflate(R.layout.game_conclusion_dialog_layout, null);
        TextView winnerPlayerTextView = (TextView) dialogLayout.findViewById(R.id.player_textView);
        String player = "";
        if (conclusion == BoardMatrix.X) {
            player = whichPlayerIsUsingLetter("X");
            winnerPlayerTextView.setText(String.format("%s wins!!!!", player));
        } else if (conclusion == BoardMatrix.O) {
            player = whichPlayerIsUsingLetter("O");
            winnerPlayerTextView.setText(String.format("%s wins!!!!", player));
        } else if (conclusion == BoardMatrix.NONE) {
            winnerPlayerTextView.setText(R.string.game_is_a_draw);
        }

        if (player.equals(PLAYER_1)) {
            increasePlayer1Score();
        }else if (player.equals(PLAYER_2)){
            increasePlayer2Score();
        }

        dialog.setCancelable(false);
        dialog.setView(dialogLayout);

        dialog.setNegativeButton("Play Again", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearBoard();
                resetBoard(BOARD_TYPE);
            }
        });

        dialog.setPositiveButton("EXIT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                Intent intent = new Intent(MainActivity.this, LauncherScreenActivity.class);
                startActivity(intent);
            }
        });

        dialog.create();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.show();
            }
        }, 500);
    }

    private DialogInterface.OnClickListener startButtonListerner = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (numOfPlayersRadioButton.getCheckedRadioButtonId()) {
                case R.id.single:
                    allowCOMPUTER_TO_PLAY(true);
                    break;
                case R.id.multi:
                    allowCOMPUTER_TO_PLAY(false);
                    break;
                default:
                    break;
            }

            switch (boardSizeRadioGroup.getCheckedRadioButtonId()) {
                case R.id.board3:
                    BOARD_TYPE = 3;
                    break;
                case R.id.board4:
                    BOARD_TYPE = 4;
                    break;
                case R.id.board5:
                    BOARD_TYPE = 5;
                    break;
                default:
                    BOARD_TYPE = 3;
                    break;
            }

            player1GameLetter = player1Spinner.getSelectedItem().toString();
            player2GameLetter = player2Spinner.getSelectedItem().toString();

            clearBoard();
            resetBoard(BOARD_TYPE);
        }
    };


    /**
     * ONCLICK LISTERNER FOR THE TEXTVIEWS BELOW THE FRAME LAYOUT
     */
    private final View.OnClickListener textViewListerner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView textView = (TextView) v;
            textView.setText(playerLetter(PLAYER_COUNT % 2));
            textView.setClickable(false);

            nextPlayerPrompt();
            int[] viewCoordinates = getTextViewCoordinates(textView);
            addToBoardMatrix(viewCoordinates[0], viewCoordinates[1]);
            BoardMatrix response = findWinner(game, BOARD_TYPE);

            if (processResult(response)) {
                return;
            }
            PLAYER_COUNT++;

            if (canCOMPUTER_PLAY()) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        computerPlay();
                    }
                }, 150);
            }
        }
    };


    /**
     * Returns the winning player, or NONE if the game has
     * finished without a winner, or null if the game is unfinished.
     */
    private BoardMatrix findWinner(BoardMatrix[][] board, int lengthToWin) {
        // Check each lengthToWin x lengthToWin board for a winner.
        for (int top = 0; top <= board.length - lengthToWin; ++top) {
            int bottom = top + lengthToWin - 1;

            for (int left = 0; left <= board.length - lengthToWin; ++left) {
                int right = left + lengthToWin - 1;

                // Check each row.
                nextRow:
                for (int row = top; row <= bottom; ++row) {
                    if (board[row][left] == BoardMatrix.NONE) {
                        continue;
                    }

                    for (int col = left; col <= right; ++col) {
                        if (board[row][col] != board[row][left]) {
                            continue nextRow;
                        }
                    }
                    return board[row][left];
                }

                // Check each column.
                nextCol:
                for (int col = left; col <= right; ++col) {
                    if (board[top][col] == BoardMatrix.NONE) {
                        continue;
                    }

                    for (int row = top; row <= bottom; ++row) {
                        if (board[row][col] != board[top][col]) {
                            continue nextCol;
                        }
                    }
                    return board[top][col];
                }

                // Check top-left to bottom-right diagonal.
                diag1:
                if (board[top][left] != BoardMatrix.NONE) {
                    for (int i = 1; i < lengthToWin; ++i) {
                        if (board[top + i][left + i] != board[top][left]) {
                            break diag1;
                        }
                    }
                    return board[top][left];
                }

                // Check top-right to bottom-left diagonal.
                diag2:
                if (board[top][right] != BoardMatrix.NONE) {
                    for (int i = 1; i < lengthToWin; ++i) {
                        if (board[top + i][right - i] != board[top][right]) {
                            break diag2;
                        }
                    }
                    return board[top][right];
                }
            }
        }

        // Check for a completely full board.
        boolean isFull = true;

        full:
        for (int row = 0; row < board.length; ++row) {
            for (int col = 0; col < board.length; ++col) {
                if (board[row][col] == BoardMatrix.NONE) {
                    isFull = false;
                    break full;
                }
            }
        }

        // The board is full.
        if (isFull) {
            return BoardMatrix.NONE;
        }
        // The board is not full and we didn't find a solution.
        else {
            return null;
        }
    }

    /**
     * STORE AN ARRAY OF TEXTVIEWS SHOWING ON THE USER BOARD
     */
    private void storeActiveTextViews() {
        activeTextViews = new TextView[BOARD_TYPE * BOARD_TYPE];
        int pos = 0;
        for (LinearLayout row : rows) {
            for (int i = 0; i < row.getChildCount(); i++) {
                FrameLayout frameLayout = (FrameLayout) row.getChildAt(i);
                if ((frameLayout.getVisibility() == View.VISIBLE) &&
                        (row.getVisibility() == View.VISIBLE)) {
                    TextView textView = (TextView) frameLayout.getChildAt(0);
                    activeTextViews[pos] = textView;
                    pos++;
                }
            }
        }
    }

    /**
     * GET CURRENT PLAYER LETTER
     *
     * @return
     */
    public String playerLetter(int playerCode) {
        if (playerCode == 0) {
            return getPlayer2GameLetter();
        } else {
            return getPlayer1GameLetter();
        }
    }

    /**
     * LOGIC FOR COMPUTER PLAY IN SINGLE PLAYER MODE
     */
    private void computerPlay() {
        ArrayList<TextView> availableTextView = new ArrayList<>();
        for (TextView textView : activeTextViews) {
            if (textView.getText().toString().equals(BLANK_TEXT)) {
                availableTextView.add(textView);
            }
        }

        Collections.shuffle(availableTextView);
        TextView textView = availableTextView.get(0);

        textView.setText(playerLetter(PLAYER_COUNT % 2));
        textView.setClickable(false);

        nextPlayerPrompt();
        int[] viewCoordinates = getTextViewCoordinates(textView);
        addToBoardMatrix(viewCoordinates[0], viewCoordinates[1]);
        BoardMatrix response = findWinner(game, BOARD_TYPE);
        if (processResult(response)) {
            return;
        }
        PLAYER_COUNT++;
    }

    /**
     * PROCESS RESPONSE FROM FIND WINNER
     *
     * @param response
     */
    private boolean processResult(BoardMatrix response) {

        if (response == BoardMatrix.O) {
            lockUnUsedTextViews();
            gameConclusionDialog(BoardMatrix.O);
            return true;
        } else if (response == BoardMatrix.X) {
            lockUnUsedTextViews();
            gameConclusionDialog(BoardMatrix.X);
            return true;
        } else if (response == BoardMatrix.NONE) {
            lockUnUsedTextViews();
            gameConclusionDialog(BoardMatrix.NONE);
            return true;
        }
        return false;
    }

    /**
     * DETERMINE PLAYER USING LETTER IN GAME
     *
     * @param letter
     * @return
     */
    private String whichPlayerIsUsingLetter(String letter) {
        if (getPlayer1GameLetter().equalsIgnoreCase(letter)) {
            return PLAYER_1;
        } else if (getPlayer2GameLetter().equalsIgnoreCase(letter)) {
            return PLAYER_2;
        }
        return "Don't know";
    }

    /**
     * ADD THE PLAYER TO BOARD MATRIX
     *
     * @param row textview row position
     * @param col textview column position
     */
    private void addToBoardMatrix(int row, int col) {
        if (playerLetter(PLAYER_COUNT % 2).equals(LETTER_X)) {
            game[row][col] = BoardMatrix.X;
        } else if (playerLetter(PLAYER_COUNT % 2).equals(LETTER_O)) {
            game[row][col] = BoardMatrix.O;
        }
    }

    /**
     * ACTIVATE NEXT PLAYER PROMPT
     */
    private void nextPlayerPrompt() {
        if ((PLAYER_COUNT % 2) == 0) {
            player1NowPlayingImageView.setVisibility(View.VISIBLE);
            player2NowPlayingImageView.setVisibility(View.INVISIBLE);
        } else {
            player2NowPlayingImageView.setVisibility(View.VISIBLE);
            player1NowPlayingImageView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * SHOW THREE BY THREE GRID
     */
    public void threeByThree() {
        for (int i = 0; i < 3; i++) {
            LinearLayout layout = rows[i];
            layout.setVisibility(View.VISIBLE);
            layout.getChildAt(3).setVisibility(View.GONE);
            layout.getChildAt(4).setVisibility(View.GONE);
        }
    }

    /**
     * SHOW FOUR BY FOUR GRID
     */
    public void fourByFour() {
        for (int i = 0; i < 4; i++) {
            LinearLayout layout = rows[i];
            layout.setVisibility(View.VISIBLE);
            layout.getChildAt(4).setVisibility(View.GONE);
        }
    }

    /**
     * SHOW FIVE BY FIVE GRID
     */
    public void fiveByFive() {
        for (LinearLayout row : rows) {
            row.setVisibility(View.VISIBLE);
        }
    }

    /**
     * CLEAR CONTENT OF BOARD
     * SET VISIBILITY OF ROWS TO GONE
     */
    public void clearBoard() {
        for (LinearLayout row : rows) {
            for (int i = 0; i < row.getChildCount(); i++) {
                FrameLayout frameLayout = (FrameLayout) row.getChildAt(i);
                TextView textView = (TextView) frameLayout.getChildAt(0);
                textView.setText(BLANK_TEXT);
                textView.setClickable(true);
                frameLayout.setVisibility(View.VISIBLE);
            }
            row.setVisibility(View.GONE);
        }
    }

    /**
     * METHOD TO LOCK THE TEXTVIEWS ONCE THERE IS A RESULT IN THE GAME
     */
    private void lockUnUsedTextViews() {
        for (TextView textView : activeTextViews) {
            textView.setClickable(false);
        }
    }

    /**
     * GET BORD DIMENSION AND RESET BOARD
     *
     * @param dimen - 3 FOR 3 X 3
     *              4 FOR 4 X 4
     *              5 FOR 5 X 5
     */
    public void resetBoard(int dimen) {
        PLAYER_COUNT = 1;
        player1NowPlayingImageView.setVisibility(View.VISIBLE);
        player2NowPlayingImageView.setVisibility(View.INVISIBLE);
        game = new BoardMatrix[dimen][dimen];
        for (int i = 0; i < dimen; i++) {
            for (int j = 0; j < dimen; j++) {
                game[i][j] = BoardMatrix.NONE;
            }
        }
        clearBoard();
        switch (dimen) {
            case 3:
                threeByThree();
                break;
            case 4:
                fourByFour();
                break;
            case 5:
                fiveByFive();
                break;
            default:
                threeByThree();
                break;
        }
        storeActiveTextViews();
    }

}
