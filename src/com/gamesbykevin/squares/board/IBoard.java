package com.gamesbykevin.squares.board;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.squares.assets.Assets;

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
     * Reset the board at the assigned level
     * @param key The asset key for the desired text file
     * @param levelIndex The selected level to reset
     * @param hint Do we reveal the solution to a row or column?
     * @throws Exception
     */
    public void reset(final Assets.TextKey key, final int levelIndex, final boolean hint) throws Exception;
    
    /**
     * Render the board
     * @param canvas Object to write pixels to
     * @param paint Object to draw text
     * @throws Exception
     */
    public void render(final Canvas canvas, final Paint paint) throws Exception;
}