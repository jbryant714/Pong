package com.example.jacob.pongforthesingleperson_bryantja18;

import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * PongMainActivity
 *
 * This is the activity for the Pong game. It attaches a PongAnimator to
 * an AnimationSurface.
 *
 * Instructions:
 * Press new Ball to start the game/add a new ball
 * press new game to reset scores and paddle positions
 * use the spinner in the top left corner to choose which game mode you want to play
 *
 * Added functionality:
 * new ball button
 * ability to have multiple balls/ get a new ball when ready
 * change the size of the balls
 * change the size of the paddles
 * A score display
 * A button to reset the score
 * A two player mode
 * 3 different computer players with different difficulty
 * Balls speed up if they have been in the game for a while
 *
 * @author Andrew Nuxoll
 * @author Steven R. Vegdahl
 * @author Jacob Bryant
 * @version February 25th 2016
 *
 */
public class MainActivity extends Activity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    //initialize views
    private Button newBallButton;
    private Button newGameButton;
    private AnimationSurface mySurface;
    private PongAnimator animator;
    private SeekBar ballSizeBar;
    private SeekBar paddleSizeBar;
    private Spinner gameSpinner;
    private String[] gameEntries;
    private TextView playerScore;
    private TextView opponentScore;

    /**
     * creates an AnimationSurface containing a PongAnimator and sets listeners
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //set button listener
        newBallButton = (Button)findViewById(R.id.newBallButton);
        newBallButton.setOnClickListener(this);
        newGameButton = (Button)findViewById(R.id.newGameButton);
        newGameButton.setOnClickListener(this);

        //set seek bar listeners
        ballSizeBar = (SeekBar)findViewById(R.id.ballSizeBar);
        ballSizeBar.setOnSeekBarChangeListener(this);

        paddleSizeBar = (SeekBar)findViewById(R.id.paddleSizeBar);
        paddleSizeBar.setOnSeekBarChangeListener(this);

        //initialize text views
        playerScore = (TextView)findViewById(R.id.playerScore);
        opponentScore = (TextView)findViewById(R.id.opponentScore);
        opponentScore.setVisibility(View.INVISIBLE);

        //set spinner listener
        gameSpinner = (Spinner)findViewById(R.id.gameSpinner);
        gameEntries = getResources().getStringArray(R.array.game_styles);
        // create adapter with the strings
        ArrayAdapter noseAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, gameEntries);
        noseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // bind the spinner and adapter
        gameSpinner.setAdapter(noseAdapter);
        gameSpinner.setOnItemSelectedListener(new MySpinnerListener());

        // Connect the animation surface with the animator
        mySurface = (AnimationSurface) this
                .findViewById(R.id.animationSurface);
        animator = new PongAnimator(ballSizeBar.getProgress() + 15, this);
        mySurface.setAnimator(animator);
        animator.setPaddleSizes(paddleSizeBar.getProgress() + 25);
    }

    /**
     External Citation
     Date: 28 February 2016
     Problem: Kept getting thread error when the score was changed

     Resource:
     http://stackoverflow.com/questions/11140285
     /how-to-use-runonuithread

     Solution: runOnUiThread method
     */

    /**
     * setTexts(String initPlayerScoreText, String initOpponentScoreText)
     *
     * Changes the score text views to a given updated score
     *
     * @param initPlayerScoreText
     * @param initOpponentScoreText
     */
    public void setTexts(String initPlayerScoreText, String initOpponentScoreText){
        final String playerScoreText = initPlayerScoreText;
        final String opponentScoreText = initOpponentScoreText;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //change texts
                playerScore.setText(playerScoreText);
                opponentScore.setText(opponentScoreText);
            }
        });
    }


    @Override
    /**
     * onClick(View v)
     *
     * A button is clicked and either a new ball is added to the animator
     * or a new game is started
     *
     * @param View v - the button view that was clicked
     */
    public void onClick(View v) {
        if(v == newBallButton) {
            animator.newBall();
        }
        if(v == newGameButton){
            animator.newGame();
        }
    }

    @Override
    /**
     * onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
     *
     * When a seek bar is changed either the ball size or the paddle size is changed
     *
     * @param SeekBar seekBar
     * @param int progress
     * @param boolean fromUser
     */
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        // if ball size bar is changed, change ball radius
        if(seekBar == ballSizeBar) {
            animator.setBallRadius(progress + 10);
        }
        //if pddles sizebar is changes, change paddle size
        if(seekBar == paddleSizeBar){
            animator.setPaddleSizes(progress + 25);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        //not used
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //not used
    }

    /**
     * MySpinnerListener class
     *
     * class that handles our spinner's selection events
     */
    private class MySpinnerListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            /*
            each item changes a number of variables to match the game type:
            singlePlayer - only one human player
            computer - a computer player is involved
                if computer is involved its difficulty is set
            impossible - computer is on impossible mode
            each resets the game
            makes the opponent score visible when applicable
             */
            if(parent == gameSpinner){
                if(position == 0){
                    //single player
                    animator.setSinglePlayer(true);
                    animator.setComputer(false);
                    animator.setImpossible(false);
                    animator.newGame();
                    opponentScore.setVisibility(View.INVISIBLE);
                }
                else if(position == 1){
                    //two player
                    animator.setSinglePlayer(false);
                    animator.setComputer(false);
                    animator.setImpossible(false);
                    animator.newGame();
                    opponentScore.setVisibility(View.VISIBLE);
                }
                else if(position == 2){
                    //easy computer
                    animator.setSinglePlayer(true);
                    animator.setComputer(true);
                    animator.setImpossible(false);
                    animator.newGame();
                    animator.setComputerPlayer(10, 300);
                    opponentScore.setVisibility(View.VISIBLE);
                }
                else if (position == 3){
                    //hard computer
                    animator.setSinglePlayer(true);
                    animator.setComputer(true);
                    animator.setImpossible(false);
                    animator.newGame();
                    animator.setComputerPlayer(15, 500);
                    opponentScore.setVisibility(View.VISIBLE);
                }
                else{
                    //impossible computer
                    animator.setSinglePlayer(true);
                    animator.setComputer(true);
                    animator.newGame();
                    animator.setImpossible(true);
                    opponentScore.setVisibility(View.VISIBLE);
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
