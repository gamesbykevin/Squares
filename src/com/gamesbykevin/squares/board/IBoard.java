package com.gamesbykevin.squares.board;

import android.graphics.Canvas;

import com.gamesbykevin.androidframework.resources.Disposable;

/**
 * Each board will need to implement this
 * @author GOD
 */
public interface IBoard extends Disposable
{
    /**
     * Method to update the board
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void update(final float x, final float y);
    
    /**
     * Reset the board
     * @param cols Total columns on the board
     * @param rows Total rows on the board
     * @param range The difficulty range
     * @throws Exception
     */
    public void reset(final int cols, final int rows, final int range) throws Exception;
    
    /**
     * Render the board
     * @param canvas Object to write pixels to
     * @throws Exception
     */
    public void render(final Canvas canvas) throws Exception;
}