package com.example.jacob.pongforthesingleperson_bryantja18;

import android.app.Activity;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.MotionEvent;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * PongAnimator
 *
 * An animator for a game of Pong
 * Implements Animator
 *
 * @author Jacob Bryant
 * @version February 25th 2016
 */
public class PongAnimator implements Animator {

    //static variables for tablet screen size
    private int BOTTOM_OF_SCREEN = 1536;
    private int RIGHT_OF_SCREEN = 2048;

    //instance variables
    private ArrayList<Ball> balls = new ArrayList<Ball>();
    private Wall top;
    private Wall left;
    private Wall bottom;
    private Wall paddle;
    private Wall opponent;
    private int radius;
    private Boolean singlePlayer;
    private Boolean computer;
    private Boolean impossible;
    private int playerScore;
    private int opponentScore;
    private MainActivity activity;
    private int computerIncrement;
    private int computerTime;

    /**
     * PongAnimator()
     *
     * Creates a Pong animator given the radius of the balls
     * Initializes the walls/paddles and creates an initial ball
     *
     * @param initRadius - initial radius of the balls
     */
    public PongAnimator(int initRadius, MainActivity initActivity){

        //make walls
        top = new Wall(0, 0, RIGHT_OF_SCREEN, 50);
        bottom = new Wall(0, 1213, RIGHT_OF_SCREEN, 1263);
        left = new Wall(0,0, 50, BOTTOM_OF_SCREEN);
        paddle = new Wall(1810, 606, 1840, 607);
        opponent = new Wall(208, 606, 238, 607);
        radius = initRadius;
        singlePlayer = true;
        computer = false;

        activity = initActivity;

        //initialize scores
        playerScore = 0;
        opponentScore = 0;
    }

    /**
     * setPaddleSizes(int size)
     *
     *sets the hieght of the player's paddle and the opponent's paddle to a given height
     *
     *@param size - half of the new height of the paddles
     */
    public void setPaddleSizes(int size){
        paddle.setSize(size);
        opponent.setSize(size);
    }


    @Override
    public int interval() {
        return 15;
    }

    @Override
    public int backgroundColor() {
        return Color.rgb(180, 200, 255);
    }

    @Override
    public boolean doPause() {
        return false;
    }

    @Override
    public boolean doQuit() {
        return false;
    }

    @Override
    /**
     * tick(Canvas canvas)
     *
     *checks each ball in the array to see if it hits a wall and draws balls and walls
     *Draws Balls and Walls on a given Canvas
     *
     * @param canvas - Canvas where everything will be drawn
     */
    public void tick(Canvas canvas) {
        Ball computerBall; //ball that the computer pays attention to
        if(balls.size() > 0) {
            computerBall = balls.get(0);
            //run through each existing ball
            for (int i = 0; i <= (balls.size() - 1); i++) {

                //check for a bounce
                top.bounceCheck(balls.get(i));
                bottom.bounceCheck(balls.get(i));
                if (paddle.bounceCheck(balls.get(i))) {
                    //increase score once per bounce
                    playerScore++;
                }
                if (singlePlayer && !computer) {
                    //single player mode
                    left.bounceCheck(balls.get(i));
                } else {
                    //mode with an opponent
                    if (opponent.bounceCheck(balls.get(i))) {
                        //increase score once per bounce
                        opponentScore++;
                    }
                }


                if ((balls.get(i).getNextLeftEdge() - opponent.getXEnd()) <=
                        (computerBall.getNextLeftEdge() - opponent.getXEnd())) {
                    //computerBall is changed to closest ball moving towards opponent paddle
                    computerBall = balls.get(i);
                }

                //if the ball leaves the player's side of the screen, remove it from the ArrayList
                if (balls.get(i).getNextLeftEdge() > RIGHT_OF_SCREEN) {
                    removeBall(i);
                    playerScore = playerScore - 5;
                    continue; //skip drawing the ball
                }

                //if the ball leaves the opponent's side of the screen, remove it from the ArrayList
                if (((!singlePlayer) || (computer)) && (balls.get(i).getNextRightEdge() < 0)) {
                    removeBall(i);
                    opponentScore = opponentScore - 5;
                    continue; //skip drawing the ball
                }

                //ball has been on screen for one tick more
                balls.get(i).addTick();
                //check to see if it's time to speed up
                balls.get(i).checkTicks();

                // Draw the ball
                balls.get(i).increment();
                balls.get(i).draw(canvas);
            }

            if (computer) {
                //computer takes its turn
                computerTurn(computerBall);
            }
            updateScore();
        }

        //draw walls
        top.draw(canvas);
        bottom.draw(canvas);
        paddle.draw(canvas);
        if(singlePlayer && !computer){
            //single player
            left.draw(canvas);
        }
        else {
            //playing against an opponent
            opponent.draw(canvas);
        }
    }

    /**
     * newBall()
     *
     * adds a new ball to the game
     */
    public void newBall(){
        balls.add(new Ball(radius));
    }

    /**
     * removeBall(int index)
     *
     * removes a ball from the game given the index of the ball
     *
     * @param index - index of the ball in the balls ArrayList
     */
    public void removeBall(int index){
        balls.remove(index);
    }

    /**
     * newGame()
     *
     * Resets the game to initial conditions for the mode
     */
    public void newGame(){
        //reset balls array, scores, and paddle locations
        balls.clear();
        playerScore = 0;
        opponentScore = 0;
        updateScore();
        paddle.setMidY(606);
        opponent.setMidY(606);
    }

    /**
     * setBallRadius(int newRadius)
     *
     * changes the radius of each existing ball and new balls to a given radius
     *
     * @param newRadius - new radius of the balls
     */
    public void setBallRadius(int newRadius){
        //set radius for each existing ball
        for(int i = 0; i <= (balls.size() - 1); i++){
            balls.get(i).setRadius(newRadius);
        }
        //set radius for each new ball
        radius = newRadius;
    }

    /**
     * onTouch(MotionEvent event)
     *
     * Method to detect touches
     *
     * @param event a MotionEvent describing the touch
     */
    @Override
    public void onTouch(MotionEvent event) {
        if(singlePlayer) {
            //if only one person touching screen
            paddle.setMidY((int) event.getY());
        }
        else{//if two people touching screen
            //first touch
            if(event.getX(0) < (RIGHT_OF_SCREEN/2)) {
                //opponent's paddle (left of screen)
                opponent.setMidY((int) event.getY(0));
            }
            else{
                //player's paddle (right of screen)
                paddle.setMidY((int) event.getY(0));
            }

            //second touch
            if(event.getPointerCount() > 1){
                if(event.getX(1) < (RIGHT_OF_SCREEN/2)) {
                    //opponent's paddle (left of screen)
                    opponent.setMidY((int) event.getY(1));
                }
                else{
                    //player's paddle (right of screen)
                    paddle.setMidY((int) event.getY(1));
                }
            }
        }

    }

    /**
     * updateScore()
     *
     * Tells activity the current score
     */
    public void updateScore(){
        activity.setTexts("" + playerScore, "" + opponentScore);
    }

    /**
     * computerTurn(Ball ball)
     *
     * Moves opponent paddle
     *
     * @param ball
     */
    public void computerTurn(Ball ball){
        if(impossible){
            //never misses
            opponent.setMidY(ball.getY());
        }
        else if(((ball.getNextLeftEdge() - opponent.getXEnd()) <= computerTime) &&
                (ball.getXIncrement() < 0)){
            //computer reacts if ball is moving towards the paddle and within a certain distance
            if(Math.abs(ball.getY() - opponent.getMidY()) < computerIncrement){
                //if paddle doesn't need to move, do nothing
            }
            else if(ball.getY() > opponent.getMidY()){
                //paddle needs to go down
                opponent.setMidY(opponent.getMidY() + computerIncrement);
            }
            else{
                //paddle needs to go up
                opponent.setMidY(opponent.getMidY() - computerIncrement);
            }
        }
    }
    public void setSinglePlayer(Boolean newSinglePlayer){singlePlayer = newSinglePlayer;}

    public void setComputer(Boolean newComputer){computer = newComputer;}

    public void setImpossible(Boolean newImpossible){impossible = newImpossible;}

    /**
     * setComputerPlayer(int initIncrement, int initComputerTime)
     *
     * Sets the computer's speed and reactivity
     *
     * @param initIncrement - how much the computer can move per tick
     * @param initComputerTime - how close ball needs to be to be sensed
     */
    public void setComputerPlayer(int initIncrement, int initComputerTime) {
        computerIncrement = initIncrement;
        computerTime = initComputerTime;
    }
}
