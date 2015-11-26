package com.gamesbykevin.squares.game;

import com.gamesbykevin.androidframework.resources.Disposable;

/**
 * Game interface methods
 * @author GOD
 */
public interface IGame extends Disposable
{
    /**
     * Reset the game with the specified settings
     * @param mode Game mode
     * @param difficulty Game difficulty
     * @throws Exception 
     */
    public void reset(final Game.Mode mode, final Game.Difficulty difficulty) throws Exception;
    
    /**
     * Logic to restart the game with the same settings
     */
    public void reset() throws Exception;
}
