package com.gamesbykevin.squares.screen;

import android.graphics.Canvas;
import android.view.MotionEvent;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.screen.Screen;
import com.gamesbykevin.squares.assets.Assets;
import com.gamesbykevin.squares.game.Game;

/**
 * The game screen that contains the game
 * @author GOD
 */
public class GameScreen implements Screen, Disposable
{
    //our object containing the main game functionality
    private Game game;
    
    //our main screen reference
    private final MainScreen screen;
    
    public GameScreen(final MainScreen screen)
    {
        this.screen = screen;
    }
    
    protected Game getGame()
    {
        return this.game;
    }
    
    /**
     * Create game object
     * @throws Exception
     */
    public void createGame() throws Exception
    {
        if (getGame() == null)
            this.game = new Game(screen);
        
        //reset the game
        getGame().reset(
            Game.Mode.values()[screen.getScreenOptions().getIndexMode()], 
            Game.Difficulty.values()[screen.getScreenOptions().getIndexDifficulty()], 
            Game.Size.values()[screen.getScreenOptions().getIndexSize()]);
        
        //play random song
        Assets.playSong();
    }
    
    /**
     * Reset any necessary screen elements here
     */
    @Override
    public void reset()
    {
        
    }
    
    @Override
    public boolean update(final MotionEvent event, final float x, final float y) throws Exception
    {
        if (getGame() != null)
            getGame().updateMotionEvent(event, x, y);
        
        return true;
    }
    
    @Override
    public void update() throws Exception
    {
        if (getGame() != null)
            getGame().update();
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //render game if exists
        if (getGame() != null)
            getGame().render(canvas);
    }
    
    @Override
    public void dispose()
    {
        if (game != null)
        {
            game.dispose();
            game = null;
        }
    }
}