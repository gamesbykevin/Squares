package com.gamesbykevin.squaro.game;

import android.view.MotionEvent;
import android.graphics.Canvas;

import com.gamesbykevin.androidframework.resources.Audio;

import com.gamesbykevin.squaro.assets.Assets;
import com.gamesbykevin.squaro.game.controller.Controller;

import com.gamesbykevin.squaro.screen.MainScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * The main game logic will happen here
 * @author ABRAHAM
 */
public class Game implements IGame
{
    //our main screen object reference
    private final MainScreen screen;
    
    //the controller for our game
    private Controller controller;
    
    /**
     * The different game modes
     */
    public enum Mode
    {
        Default
    }

    /**
     * The game difficulty
     */
    public enum Difficulty
    {
        Normal,
        Hard,
        Easy
    }
    
    public Game(final MainScreen screen, final Mode mode, final Difficulty difficulty) throws Exception
    {
        //our main screen object reference
        this.screen = screen;
        
        //setup the game mode
        switch (mode)
        {
            case Default:
                break;
                
            default:
                throw new Exception("Mode not setup here - " + mode.toString());
        }
        
        //create new controller
        this.controller = new Controller(this);
    }
    
    /**
     * Get the main screen object reference
     * @return The main screen object reference
     */
    public MainScreen getMainScreen()
    {
        return this.screen;
    }
    
    @Override
    public void reset()
    {
        //make sure no existing audio
        Audio.stop();
            
        //play song
        resumeMusic();
    }
    
    /**
     * Resume playing music
     */
    public void resumeMusic()
    {
        //play song
        //Audio.play(Assets.AudioKey.MusicVs, true);
    }
    
    /**
     * Get our controller object
     * @return Object containing game controls
     */
    public Controller getController()
    {
        return this.controller;
    }
    
    /**
     * Update the game based on the motion event
     * @param event Motion Event
     * @param x (x-coordinate)
     * @param y (y-coordinate)
     */
    public void updateMotionEvent(final MotionEvent event, final float x, final float y)
    {
        getController().updateMotionEvent(event, x, y);
    }
    
    /**
     * Update game
     * @throws Exception 
     */
    public void update() throws Exception
    {
        
    }
    
    @Override
    public void dispose()
    {
        if (controller != null)
        {
            controller.dispose();
            controller = null;
        }
    }
    
    /**
     * Render game elements
     * @param canvas Where to write the pixel data
     * @throws Exception 
     */
    public void render(final Canvas canvas) throws Exception
    {
        //draw the game controller
        if (getController() != null)
            getController().render(canvas);
    }
}