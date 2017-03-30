package com.example.jacob.pongforthesingleperson_bryantja18;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Ball
 *
 * A ball in the Pong game
 *
 * @author Jacob Bryant
 * @version February 25th 2016
 */
public class Ball {
    private int x;
    private int y;
    private int xIncrement;
    private int yIncrement;
    private int radius;
    private int tickCount;
    private int speedUp;
    Paint ballPaint = new Paint();

    /**
     * Ball(int initRadius)
     *
     * Creates a new ball with a random speed
     *
     * @param initRadius
     */
    public Ball(int initRadius){

        //ball has been through no ticks
        tickCount = 0;
        //ball won't speed up much the first speed up
        speedUp = 1;
        //set radius
        radius = initRadius;

        //set position to the middle of the screen
        x = 1024;
        y = 768;

        //create a random speed
        xIncrement = (int)(10*Math.random()) + 10;
        yIncrement = (int)(10*Math.random()) + 10;
        //make the speed's direction random
        if(Math.random() < .5){
            xIncrement = xIncrement*(-1);
        }
        if(Math.random() < .5){
            yIncrement = yIncrement*(-1);
        }

    }

    /**
     * addTick()
     *
     * Tells the ball that it has been through one more tick
     */
    public void addTick(){tickCount = tickCount +1;}

    /**
     * checkTicks()
     *
     * Checks to see if the ball needs to speed up
     * if ball has been in play for a while it speeds up
     */
    public void checkTicks(){
        //ball has been on board for a while
        if(tickCount > 1500){
            //makes the ball's X/Y speeds faster by speedUp
            if( xIncrement > 0){
                xIncrement = xIncrement + speedUp;
            }
            if(xIncrement < 0){
                xIncrement = xIncrement - speedUp;
            }
            if(yIncrement > 0){
                yIncrement = yIncrement + speedUp;
            }
            if(yIncrement < 0){
                yIncrement = yIncrement - speedUp;
            }
            //resets ticks
            tickCount = 0;
            //ball will speed up more next time
            speedUp++;
        }
    }

    /**
     * increment()
     *
     * change the x and y position of the ball according to the speed of the ball
     */
    public void increment(){
        x = x + xIncrement;
        y = y + yIncrement;
    }

    /**
     * getXIncrement()
     *
     * getter for the change in x each tick
     *
     * @return xIncrement
     */
    public int getXIncrement(){return xIncrement;}

    /**
     * setXIncrement(int newXIncrement)
     *
     * setter for the change in x each tick
     *
     * @param newXIncrement
     */
    public void setXIncrement(int newXIncrement){
        xIncrement = newXIncrement;
    }

    /**
     * getYIncrement()
     *
     * getter for the change in y each tick
     *
     * @return yIncrement
     */
    public int getYIncrement(){return yIncrement;}

    /**
     * setYIncrement(int newYIncrement)
     *
     * setter for the change in y each tick
     *
     * @param newYIncrement
     */
    public void setYIncrement(int newYIncrement){
        yIncrement = newYIncrement;
    }

    /**
     * getNextLeftEdge()
     *
     * getter for the x value of the left edge of the ball after an increment
     *
     * @return min x value
     */
    public int getNextLeftEdge(){
        return x + xIncrement - radius;
    }

    /**
     * getNextRightEdge()
     *
     * getter for the x value of the right edge of the ball after an increment
     *
     * @return max x value
     */
    public int getNextRightEdge(){
        return x + xIncrement + radius;
    }

    /**
     * getNextTopEdge()
     *
     * getter for the y value of the top edge of the ball after an increment
     *
     * @return minimum y value
     */
    public int getNextTopEdge(){
        return y + yIncrement - radius;
    }

    /**
     * getNextBottomEdge()
     *
     *getter for the y value of the bottom edge of the ball after an increment
     *
     * @return max y value
     */
    public int getNextBottomEdge(){
        return y + yIncrement + radius;
    }

    /**
     * setRadius()
     *
     * sets the radius of the ball to a given value
     *
     * @param newRadius
     */
    public void setRadius(int newRadius){
        radius = newRadius;
    }

    /**
     * getY()
     *
     * Getter for the y value of the ball
     *
     * @return y
     */
    public int getY(){return y;}

    /**
     * draw(Canvas canvas)
     *
     * draws the ball in red on a given canvas
     *
     * @param canvas
     */
    public void draw(Canvas canvas){
        ballPaint.setColor(Color.RED);
        canvas.drawCircle(x, y, radius, ballPaint);
    }



}
