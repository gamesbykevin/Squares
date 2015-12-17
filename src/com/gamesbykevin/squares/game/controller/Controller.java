package com.gamesbykevin.squares.game.controller;

import com.gamesbykevin.androidframework.awt.Button;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.squares.screen.OptionsScreen;
import com.gamesbykevin.squares.assets.Assets;
import com.gamesbykevin.squares.game.Game;
import com.gamesbykevin.squares.screen.MainScreen;

import java.util.HashMap;

import java.util.ArrayList;
import java.util.List;

/**
 * This class will be our game controller
 * @author GOD
 */
public class Controller implements IController
{
    //all of the buttons for the player to control
    private HashMap<Assets.ImageMenuKey, Button> buttons;
    
    //our game object reference
    private final Game game;
    
    /**
     * Default Constructor
     * @param game Object game object reference
     */
    public Controller(final Game game)
    {
        //assign object reference
        this.game = game;
        
        //create temporary list
        List<Assets.ImageMenuKey> tmp = new ArrayList<Assets.ImageMenuKey>();
        tmp.add(Assets.ImageMenuKey.SoundDisabled);
        tmp.add(Assets.ImageMenuKey.SoundEnabled);
        tmp.add(Assets.ImageMenuKey.PauseDisabled);
        tmp.add(Assets.ImageMenuKey.PauseEnabled);
        tmp.add(Assets.ImageMenuKey.MenuDisabled);
        tmp.add(Assets.ImageMenuKey.MenuEnabled);
        
        //create new list of buttons
        this.buttons = new HashMap<Assets.ImageMenuKey, Button>();
        
        //add button controls
        for (Assets.ImageMenuKey key : tmp)
        {
            this.buttons.put(key, new Button(Images.getImage(key)));
        }
        
        int x = 100;
        final int y = 700;
        final int incrementX = 100;
        
        this.buttons.get(Assets.ImageMenuKey.SoundDisabled).setX(x);
        this.buttons.get(Assets.ImageMenuKey.SoundDisabled).setY(y);
        this.buttons.get(Assets.ImageMenuKey.SoundEnabled).setX(x);
        this.buttons.get(Assets.ImageMenuKey.SoundEnabled).setY(y);
        
        x += incrementX;
        this.buttons.get(Assets.ImageMenuKey.PauseDisabled).setX(x);
        this.buttons.get(Assets.ImageMenuKey.PauseDisabled).setY(y);
        this.buttons.get(Assets.ImageMenuKey.PauseEnabled).setX(x);
        this.buttons.get(Assets.ImageMenuKey.PauseEnabled).setY(y);
        
        x += incrementX;
        this.buttons.get(Assets.ImageMenuKey.MenuDisabled).setX(x);
        this.buttons.get(Assets.ImageMenuKey.MenuDisabled).setY(y);
        this.buttons.get(Assets.ImageMenuKey.MenuEnabled).setX(x);
        this.buttons.get(Assets.ImageMenuKey.MenuEnabled).setY(y);
        
        //adjust the dimensions of each image
        for (Assets.ImageMenuKey key : tmp)
        {
            this.buttons.get(key).setWidth(this.buttons.get(key).getWidth() / 3);
            this.buttons.get(key).setHeight(this.buttons.get(key).getHeight() / 3);
        }
        
        //assign boundary
        for (Assets.ImageMenuKey key : tmp)
        {
            this.buttons.get(key).updateBounds();
        }
    }
    
    /**
     * Get our game object reference
     * @return Our game object reference
     */
    private Game getGame()
    {
        return this.game;
    }
    
    /**
     * Update the controller based on the motion event
     * @param event Motion Event
     * @param x (x-coordinate)
     * @param y (y-coordinate)
     * @return true if motion event was applied, false otherwise
     */
    public boolean update(final MotionEvent event, final float x, final float y)
    {
        //check if the touch screen was released
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            //check if the player hit the pause
            if (buttons.get(Assets.ImageMenuKey.PauseEnabled).contains(x, y))
            {
                //change the state to paused
                getGame().getMainScreen().setState(MainScreen.State.Paused);
                
                //event was applied
                return true;
            }
            else if (buttons.get(Assets.ImageMenuKey.MenuEnabled).contains(x, y))
            {
                //change to the exit confirm screen
                getGame().getMainScreen().setState(MainScreen.State.Exit);
                
                //event was applied
                return true;
            }
            else if (buttons.get(Assets.ImageMenuKey.SoundEnabled).contains(x, y))
            {
                //flip the audio setting
                Audio.setAudioEnabled(!Audio.isAudioEnabled());

                //update
                
                //make sure the correct button is showing
                if (Audio.isAudioEnabled())
                {
                    //play random song
                    Assets.playSong();
                }
                else
                {
                    //if audio is not enabled, stop all sound
                    Audio.stop();
                }
                
                getGame().getMainScreen().getScreenOptions().setIndex(
                	OptionsScreen.INDEX_BUTTON_SOUND, 
                    getGame().getMainScreen().getScreenOptions().getIndex(OptionsScreen.INDEX_BUTTON_SOUND) + 1
                );
                
                //event was applied
                return true;
            }
        }
        
        //no event was applied
        return false;
    }
    
    @Override
    public void reset()
    {
    	if (buttons != null)
    	{
	        //determine which button is displayed
	        buttons.get(Assets.ImageMenuKey.SoundEnabled).setVisible(Audio.isAudioEnabled());
	        buttons.get(Assets.ImageMenuKey.SoundDisabled).setVisible(!Audio.isAudioEnabled());
    	}
    }
    
    /**
     * Recycle objects
     */
    @Override
    public void dispose()
    {
        if (buttons != null)
        {
            for (Button button : buttons.values())
            {
                if (button != null)
                {
                    button.dispose();
                    button = null;
                }
            }
            
            buttons.clear();
            buttons = null;
        }
    }
    
    /**
     * Render the controller
     * @param canvas Write pixel data to this canvas
     * @throws Exception 
     */
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //draw the buttons
        if (buttons != null)
        {
            switch (getGame().getMainScreen().getState())
            {
                case Paused:
                case Exit:
                    buttons.get(Assets.ImageMenuKey.SoundDisabled).render(canvas);
                    buttons.get(Assets.ImageMenuKey.PauseDisabled).render(canvas);
                    buttons.get(Assets.ImageMenuKey.MenuDisabled).render(canvas);
                    break;
                    
                default:
                    buttons.get(Audio.isAudioEnabled() ? Assets.ImageMenuKey.SoundEnabled : Assets.ImageMenuKey.SoundDisabled).render(canvas);
                    buttons.get(Assets.ImageMenuKey.PauseEnabled).render(canvas);
                    buttons.get(Assets.ImageMenuKey.MenuEnabled).render(canvas);
                    break;
            }
        }
    }
}