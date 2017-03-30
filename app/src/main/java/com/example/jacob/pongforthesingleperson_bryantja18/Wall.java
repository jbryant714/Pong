package com.example.jacob.pongforthesingleperson_bryantja18;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Wall
 *
 * A wall that a ball bounces off of in the game of Pong
 *
 * @author Jacob Bryant
 * @version February 25th 2016
 */
public class Wall {

    //instance variables
    private int xStart;
    private int xEnd;
    private int yStart;
    private int yEnd;
    private int midY;
    private int size;
    private Paint wallPaint;

    /**
     * Wall(int initXStart, int initYStart, int initXEnd, int initYEnd)
     *
     * A constructor for wall that takes in the start and end values of the
     * x and y axis for the wall
     *
     * @param initXStart
     * @param initYStart
     * @param initXEnd
     * @param initYEnd
     */
    public Wall(int initXStart, int initYStart, int initXEnd, int initYEnd){
        //set values
        xStart = initXStart;
        xEnd = initXEnd;
        yStart = initYStart;
        yEnd = initYEnd;
        //calculate vertical midpoint
        midY = (yEnd - yStart) + yStart;
        //set gray paint
        wallPaint = new Paint();
        wallPaint.setColor(Color.GRAY);
    }

    /**
     * setSize(int newSize)
     *
     * resizes the Wall in the y direction to a given size and modifies the edge values
     *
     * @param newSize
     */
    public void setSize(int newSize){
        size = newSize;
        //modify vertical values to new size
        yStart = midY - size;
        yEnd = midY + size;
    }

    /**
     * setMidY(int newMid)
     *
     * Makes the wall move to a new position
     * @param newMid
     */
    public void setMidY(int newMid){
        //makes sure the wall doesn't go up or down off the screen
        if(newMid - size <= 50){
            midY = size + 50;
        }
        else if(newMid + size >= 1213){
            midY = 1213 - size;
        }
        else{
            midY = newMid;
        }
        //set new values based on the new midpoint
        setSize(size);
    }

    /**
     * draw(Canvas canvas)
     * draws the Wall on a given canvas
     *
     * @param canvas
     */
    public void draw(Canvas canvas){
        canvas.drawRect(xStart, yStart, xEnd, yEnd, wallPaint);
    }

    /**
     * bounceCheck(Ball ball)
     *
     * check to see if the given ball bounces against the Wall
     *
     * @param ball
     */
    public Boolean bounceCheck(Ball ball){

        //check corners for a bounce, only one bounce can happen
        if(checkTopLeft(ball)){
            return true;
        }
        else if(checkBottomLeft(ball)){
            return true;
        }
        else if(checkBottomRight(ball)){
            return true;
        }
        else if(checkTopRight(ball)){
            return true;
        }
        //check sides
        else if(checkTop(ball)){
            return true;
        }
        else if(checkLeft(ball)){
            return true;
        }
        else if(checkBottom(ball)){
            return true;
        }
        else if(checkRight(ball)){
            return true;
        }
        //no bounce
        else {
            return false;
        }
    }

    /**
     * checkTopLeft(Ball ball)
     *
     * helper method to see if a given ball bounces against the Wall's top left corner
     * if a bounce happens, the appropriate helper method is called and true is returned
     * if no bounce happens, false is returned
     *
     * @param ball
     *
     * @return true if bounce happened, false if not
     */
    public Boolean checkTopLeft(Ball ball){
        //next ball position overlaps with the top left corner of the wall
        if((ball.getNextBottomEdge() > yStart) && (ball.getNextTopEdge() < yStart)
                && (ball.getNextRightEdge() > xStart) && (ball.getNextLeftEdge() < xStart)){
            //if ball is going up, ball bounces off the left edge
            if(ball.getYIncrement() < 0){
                checkLeft(ball);
                return true;
            }
            //if ball is going left, ball bounces off the top edge
            else if(ball.getXIncrement() < 0){
                checkTop(ball);
            }
            //if ball overlaps with the left edge more than the top edge, bounce off left edge
            else if((ball.getNextRightEdge() - xStart) < (ball.getNextBottomEdge() - yStart)){
                checkLeft(ball);
                return true;
            }
            //otherwise bounce off top edge
            else {
            checkTop(ball);
                return true;
            }
        }
        return false;
    }

    /**
     * checkBottomLeft(Ball ball)
     *
     * helper method to see if a given ball bounces against the Wall's bottom left corner
     * if a bounce happens, the appropriate helper method is called and true is returned
     * if no bounce happens, false is returned
     *
     * @param ball
     *
     * @return true if bounce happened, false if not
     */
    public Boolean checkBottomLeft(Ball ball){
        //next ball position overlaps with the bottom left corner of the wall
        if((ball.getNextBottomEdge() > yEnd) && (ball.getNextTopEdge() < yEnd)
                && (ball.getNextRightEdge() >= xStart) && (ball.getNextLeftEdge() <= xStart)) {
            //if ball is going down, bounce off left edge
            if(ball.getYIncrement() > 0){
                checkLeft(ball);
                return true;
            }
            //if ball is going left, bounce off bottom edge
            else if(ball.getXIncrement() < 0){
                checkBottom(ball);
            }
            //if ball overlaps with the bottom edge more than the left edge, bounce off bottom edge
            else if((yEnd - ball.getNextTopEdge()) < (ball.getNextRightEdge() - xStart)){
                checkBottom(ball);
                return true;
            }
            //otherwise bounce off left wall
            else{
                checkLeft(ball);
                return true;
            }
        }
        return false;
    }

    /**
     * checkBottomRight(Ball ball)
     *
     * helper method to see if a given ball bounces against the Wall's bottom right corner
     * if a bounce happens, the appropriate helper method is called and true is returned
     * if no bounce happens, false is returned
     *
     * @param ball
     *
     * @return true if bounce happened, false if not
     */
    public Boolean checkBottomRight(Ball ball){
        //next ball position overlaps with the bottom right corner of the wall
        if((ball.getNextBottomEdge() > yEnd) && (ball.getNextTopEdge() <= yEnd)
                && (ball.getNextRightEdge() > xEnd) && (ball.getNextLeftEdge() <= xEnd)) {
            //if ball is going down, bounce off right edge
            if(ball.getYIncrement() > 0){
                checkRight(ball);
                return true;
            }
            //if ball is going right, bounce off bottom edge
            else if(ball.getXIncrement() > 0){
                checkBottom(ball);
            }
            //if ball overlaps with the bottom edge more than the right edge, bounce off top edge
            else if((yEnd - ball.getNextTopEdge()) < (xEnd - ball.getNextLeftEdge())){
                checkBottom(ball);
                return true;
            }
            //otherwise bounce off right edge
            else{
                checkRight(ball);
                return true;
            }
        }
        return false;
    }

    /**
     * checkTopRight(Ball ball)
     *
     * helper method to see if a given ball bounces against the Wall's top right corner
     * if a bounce happens, the appropriate helper method is called and true is returned
     * if no bounce happens, false is returned
     *
     * @param ball
     *
     * @return true if bounce happened, false if not
     */
    public Boolean checkTopRight(Ball ball){
        //next ball position overlaps with the top right corner of the wall
        if((ball.getNextBottomEdge() >= yStart) && (ball.getNextTopEdge() <= yStart)
                && (ball.getNextLeftEdge() <= xEnd) && (ball.getNextRightEdge() >= xEnd)) {
            //if ball is going up, bounce off right edge
            if(ball.getYIncrement() < 0){
                checkRight(ball);
                return true;
            }
            //if ball is going right, bounce off top edge
            else if(ball.getXIncrement() > 0){
                checkTop(ball);
            }
            //if ball overlaps with the top edge more than the right edge, bounce off top edge
            else if((xEnd - ball.getNextLeftEdge()) > (ball.getNextBottomEdge() - yStart)){
                checkTop(ball);
                return true;
            }
            //otherwise bounce off right edge
            else{
                checkRight(ball);
                return true;
            }
        }
        return false;
    }

    /**
     * checkTop(Ball ball)
     *
     * helper method to see if a given ball bounces against the Wall's top edge
     * if a bounce happens, the appropriate increment of the ball is changed and true is returned
     * if no bounce happens, false is returned
     *
     * @param ball
     *
     * @return true if bounce happened, false if no bounce happened
     */
    public Boolean checkTop(Ball ball){
        //next ball position overlaps with the top edge of the wall
        if((ball.getNextBottomEdge() >= yStart) && (ball.getNextTopEdge() <= yStart)
                && (ball.getNextRightEdge() >= xStart) && (ball.getNextLeftEdge() <= xEnd)) {

            //modify increment
            ball.setYIncrement(ball.getYIncrement() * (-1));
            return true;
        }
        return false;
    }

    /**
     * checkLeft(Ball ball)
     *
     * helper method to see if a given ball bounces against the Wall's left edge
     * if a bounce happens, the appropriate increment of the ball is changed and true is returned
     * if no bounce happens, false is returned
     *
     * @param ball
     *
     * @return true if bounce happened, false if no bounce happened
     */
    public Boolean checkLeft(Ball ball){
        //next ball position overlaps with the left edge of the wall
        if((ball.getNextBottomEdge() >= yStart) && (ball.getNextTopEdge() <= yEnd)
                && (ball.getNextRightEdge() >= xStart) && (ball.getNextLeftEdge() <= xStart)) {

            //modify increment
            ball.setXIncrement(ball.getXIncrement() * (-1));
            return true;
        }
        return false;
    }

    /**
     * checkBottom(Ball ball)
     *
     * helper method to see if a given ball bounces against the Wall's Bottom edge
     * if a bounce happens, the appropriate increment of the ball is changed and true is returned
     * if no bounce happens, false is returned
     *
     * @param ball
     *
     * @return true if bounce happened, false if no bounce happened
     */
    public Boolean checkBottom(Ball ball){
        //next ball position overlaps with the bottom edge of the wall
        if((ball.getNextBottomEdge() >= yEnd) && (ball.getNextTopEdge() <= yEnd)
                && (ball.getNextRightEdge() >= xStart) && (ball.getNextLeftEdge() <= xEnd)) {

            //modify increment
            ball.setYIncrement(ball.getYIncrement() * (-1));
            return true;
        }
        return false;
    }

    /**
     * checkRight(Ball ball)
     *
     * helper method to see if a given ball bounces against the Wall's right edge
     * if a bounce happens, the appropriate increment of the ball is changed and true is returned
     * if no bounce happens, false is returned
     *
     * @param ball
     *
     * @return true if bounce happened, false if no bounce happened
     */
    public Boolean checkRight(Ball ball){
        //next ball position overlaps with the right edge of the wall
        if((ball.getNextBottomEdge() >= yStart) && (ball.getNextTopEdge() <= yEnd)
                && (ball.getNextRightEdge() >= xEnd) && (ball.getNextLeftEdge() <= xEnd)) {

            //modify increment
            ball.setXIncrement(ball.getXIncrement() * (-1));
            return true;
        }
        return false;
    }

    /*
    getXEnd()

    getter method for the maximum x value

    @return xEnd
     */
    public int getXEnd(){return xEnd;}

    /*
    getMidY()

    getter method for the y value in the middle of the wall

    @return midY
     */
    public int getMidY(){return midY;}
}
