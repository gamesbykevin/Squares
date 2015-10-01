package com.gamesbykevin.squares.game.controller;

import com.gamesbykevin.androidframework.awt.Button;

import android.graphics.Canvas;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Images;

import com.gamesbykevin.squares.assets.Assets;
import com.gamesbykevin.squares.game.Game;
import com.gamesbykevin.squares.panel.GamePanel;
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
    private HashMap<Assets.ImageKey, Button> buttons;
    
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
        
        //create temp list
        List<Assets.ImageKey> tmp = new ArrayList<Assets.ImageKey>();
        tmp.add(Assets.ImageKey.SoundDisabled);
        tmp.add(Assets.ImageKey.SoundEnabled);
        tmp.add(Assets.ImageKey.PauseDisabled);
        tmp.add(Assets.ImageKey.PauseEnabled);
        tmp.add(Assets.ImageKey.MenuDisabled);
        tmp.add(Assets.ImageKey.MenuEnabled);
        
        //create new list of buttons
        this.buttons = new HashMap<Assets.ImageKey, Button>();
        
        //add button controls
        for (Assets.ImageKey key : tmp)
        {
            this.buttons.put(key, new Button(Images.getImage(key)));
        }
        
        int x = 100;
        final int y = 675;
        final int incrementX = 100;
        
        this.buttons.get(Assets.ImageKey.SoundDisabled).setX(x);
        this.buttons.get(Assets.ImageKey.SoundDisabled).setY(y);
        this.buttons.get(Assets.ImageKey.SoundEnabled).setX(x);
        this.buttons.get(Assets.ImageKey.SoundEnabled).setY(y);
        
        x += incrementX;
        this.buttons.get(Assets.ImageKey.PauseDisabled).setX(x);
        this.buttons.get(Assets.ImageKey.PauseDisabled).setY(y);
        this.buttons.get(Assets.ImageKey.PauseEnabled).setX(x);
        this.buttons.get(Assets.ImageKey.PauseEnabled).setY(y);
        
        x += incrementX;
        this.buttons.get(Assets.ImageKey.MenuDisabled).setX(x);
        this.buttons.get(Assets.ImageKey.MenuDisabled).setY(y);
        this.buttons.get(Assets.ImageKey.MenuEnabled).setX(x);
        this.buttons.get(Assets.ImageKey.MenuEnabled).setY(y);
        
        //adjust the dimensions of each image
        for (Assets.ImageKey key : tmp)
        {
            this.buttons.get(key).setWidth(this.buttons.get(key).getWidth() / 3);
            this.buttons.get(key).setHeight(this.buttons.get(key).getHeight() / 3);
        }
        
        //assign boundary
        for (Assets.ImageKey key : tmp)
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
    public boolean updateMotionEvent(final MotionEvent event, final float x, final float y)
    {
        //check if the touch screen was released
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            //check if the player hit the pause
            if (buttons.get(Assets.ImageKey.PauseEnabled).contains(x, y))
            {
                //change the state to paused
                getGame().getMainScreen().setState(MainScreen.State.Paused);
                
                //event was applied
                return true;
            }
            else if (buttons.get(Assets.ImageKey.MenuEnabled).contains(x, y))
            {
                //change to the exit confirm screen
                getGame().getMainScreen().setState(MainScreen.State.Exit);
                
                //event was applied
                return true;
            }
            else if (buttons.get(Assets.ImageKey.SoundEnabled).contains(x, y))
            {
                //flip the audio setting
                Audio.setAudioEnabled(!Audio.isAudioEnabled());

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
                
                //event was applied
                return true;
            }
        }
        
        //no event was applied
        return false;
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
                    buttons.get(Assets.ImageKey.SoundDisabled).render(canvas);
                    buttons.get(Assets.ImageKey.PauseDisabled).render(canvas);
                    buttons.get(Assets.ImageKey.MenuDisabled).render(canvas);
                    break;
                    
                default:
                    buttons.get(Audio.isAudioEnabled() ? Assets.ImageKey.SoundEnabled : Assets.ImageKey.SoundDisabled).render(canvas);
                    buttons.get(Assets.ImageKey.PauseEnabled).render(canvas);
                    buttons.get(Assets.ImageKey.MenuEnabled).render(canvas);
                    break;
            }
        }
    }
}