package com.gamesbykevin.squaro.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Disposable;

import com.gamesbykevin.squaro.assets.Assets;
import com.gamesbykevin.squaro.board.Board;
import com.gamesbykevin.squaro.board.BoardHelper;
import com.gamesbykevin.squaro.game.controller.Controller;

import com.gamesbykevin.squaro.screen.MainScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * The main game logic will happen here
 * @author ABRAHAM
 */
public final class Game implements IGame
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
        Default("Casual"),
        Timed("Timed");
        
        private final String desc;
        
        private Mode(final String desc)
        {
            this.desc = desc;
        }
        
        public String getDescription()
        {
            return this.desc;
        }
    }

    /**
     * The game difficulty
     */
    public enum Difficulty
    {
        Easy("Easy"),
        Normal("Normal"),
        Hard("Hard");
        
        private final String desc;
        
        private Difficulty(final String desc)
        {
            this.desc = desc;
        }
        
        public String getDescription()
        {
            return this.desc;
        }
    }
    
    /**
     * The size of the board
     */
    public enum Size
    {
        Medium("Medium"),
        Large("Large"),
        VeryLarge("Very Large"),
        VeryLong("Very Long"),
        Small("Small");
        
        private final String desc;
        
        private Size(final String desc)
        {
            this.desc = desc;
        }
        
        public String getDescription()
        {
            return this.desc;
        }
    }
    
    //the board where gameplay will take place
    private Board board;
    
    //store the settings
    private int cols, rows, range;
    
    //store game settings
    private Mode mode;
    private Difficulty difficulty;
    private Size size;
    
    /**
     * For timed mode, the number of blocks will be a factor to determine the time remaining
     */
    private static final long TIMED_BLOCK_DURATION = 5000L;
    
    /**
     * Timed multiplier for each difficulty
     */
    private static final float TIMED_MULTIPLIER_EASY = 1.0f;
    private static final float TIMED_MULTIPLIER_NORMAL = 1.5f;
    private static final float TIMED_MULTIPLIER_HARD = 2.25f;
    
    //where the difficulty is displayed
    private static final int LOCATION_TIMER_X = 100;
    private static final int LOCATION_TIMER_Y = 580;
    
    //where the difficulty is displayed
    private static final int LOCATION_DIFFICULTY_X = 100;
    private static final int LOCATION_DIFFICULTY_Y = 615;
    
    //where the size is displayed
    private static final int LOCATION_SIZE_X = 100;
    private static final int LOCATION_SIZE_Y = 650;
    
    //paint object to draw text
    private Paint paint;
    
    //track the time (milliseconds)
    private long totalTime = 0L;
    
    //track the previous time (milliseconds)
    private long previousTime = 0L;
    
    //do we stop the timer
    private boolean stopTimer = false;
    
    public Game(final MainScreen screen) throws Exception
    {
        //our main screen object reference
        this.screen = screen;
        
        //create new paint object
        this.paint = new Paint();
        this.paint.setTextSize(24f);
        this.paint.setColor(Color.WHITE);
        
        //create new controller
        this.controller = new Controller(this);
    }
    
    /**
     * Get the board
     * @return The game board where play happens
     */
    private Board getBoard()
    {
        return this.board;
    }
    
    /**
     * Get the main screen object reference
     * @return The main screen object reference
     */
    public MainScreen getMainScreen()
    {
        return this.screen;
    }
    
    /**
     * Stop the timer
     */
    public void stopTimer()
    {
        this.stopTimer = true;
    }
    
    /**
     * Reset game with the specified settings
     * @param mode Game mode
     * @param difficulty Game difficulty
     * @param size Board size
     * @throws Exception 
     */
    public void reset(final Mode mode, final Difficulty difficulty, final Size size) throws Exception
    {
        //assign settings
        this.mode = mode;
        this.size = size;
        this.difficulty = difficulty;
        
        //assign the range
        switch (difficulty)
        {
            case Normal:
                range = Board.DIFFICULTY_RANGE_MEDIUM;
                break;
                
            case Easy:
                range = Board.DIFFICULTY_RANGE_EASY;
                break;
                
            case Hard:
                range = Board.DIFFICULTY_RANGE_HARD;
                break;
                
            default:
                throw new Exception("Difficulty not setup - " + difficulty.toString());
        }
        
        //assign the size
        switch (size)
        {
            case Small:
                cols = Board.SIZE_SMALL;
                rows = Board.SIZE_SMALL;
                break;
                
            case Medium:
                cols = Board.SIZE_MEDIUM;
                rows = Board.SIZE_MEDIUM;
                break;
                
            case Large:
                cols = Board.SIZE_LARGE;
                rows = Board.SIZE_LARGE;
                break;
                
            case VeryLarge:
                cols = Board.SIZE_VERY_LARGE;
                rows = Board.SIZE_VERY_LARGE;
                break;
                
            case VeryLong:
                cols = Board.SIZE_VERY_LARGE + Board.SIZE_SMALL;
                rows = Board.SIZE_VERY_LARGE + Board.SIZE_MEDIUM;
                break;
                
            default:
                throw new Exception("Size not setup here - " + size.toString());
        }
        
        //reset game
        reset();
    }
    
    /**
     * Restart game with assigned settings
     * @throws Exception 
     */
    @Override
    public void reset() throws Exception
    {
        //create a new board, if it doesn't exist
        if (getBoard() == null)
            this.board = new Board(cols, rows, range);
        
        //reset the board
        getBoard().reset(cols, rows, range);
        
        //setup the game mode
        switch (mode)
        {
            case Default:
                this.totalTime = 0;
                break;
                
            case Timed:
                
                //the amount of time remaining will depend on the # of blocks
                this.totalTime = TIMED_BLOCK_DURATION * ((getBoard().getCols() - 1) * (getBoard().getRows() - 1));
                
                //the multiplier will depend on difficulty
                switch (difficulty)
                {
                    case Easy:
                        this.totalTime *= TIMED_MULTIPLIER_EASY;
                        break;
                        
                    case Normal:
                        this.totalTime *= TIMED_MULTIPLIER_NORMAL;
                        break;
                        
                    case Hard:
                        this.totalTime *= TIMED_MULTIPLIER_HARD;
                        break;
                        
                    default:
                        throw new Exception("Difficulty not setup here - " + difficulty.toString());
                }
                break;
                
            default:
                throw new Exception("Mode not setup here - " + mode.toString());
        }
        
        stopTimer();
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
        //only update game if no controller buttons were clicked
        if (!getController().updateMotionEvent(event, x, y))
        {
            //if the board exists and the action is up
            if (getBoard() != null && event.getAction() == MotionEvent.ACTION_UP)
            {
                //do we have a match before updating
                final boolean match = BoardHelper.hasMatch(getBoard());
                
                //update board
                getBoard().update(x, y);
                
                //if we now have a match
                if (!match && BoardHelper.hasMatch(getBoard()))
                {
                    //set game over state
                    screen.setState(MainScreen.State.GameOver);
                    
                    //set display message
                    screen.getScreenGameover().setMessage("Game Over, You win");
                    
                    //play sound
                    Audio.play(Assets.AudioKey.GameoverWin);
                }
            }
        }
    }
    
    /**
     * Update game
     * @throws Exception 
     */
    public void update() throws Exception
    {
        //if we stopped the timer, record the previous time
        if (this.stopTimer)
        {
            this.stopTimer = false;
            this.previousTime = System.currentTimeMillis();
        }
        
        //get the current time
        final long current = System.currentTimeMillis();
        
        //the timer will behave different depending on the mode
        switch (mode)
        {
            case Default:
                //add the difference to the total time
                this.totalTime += (current - previousTime);
                break;
                
            case Timed:
                //subtract the difference to the total time
                this.totalTime = totalTime - (current - previousTime);
                
                //don't let the time go below 0
                if (this.totalTime < 0)
                {
                    //set time to 0
                    this.totalTime = 0;
                    
                    //play sound
                    Audio.play(Assets.AudioKey.GameoverLose);
                    
                    //set the state
                    screen.setState(MainScreen.State.GameOver);
                    
                    //set display message
                    screen.getScreenGameover().setMessage("Time up, You lose");
                }
                break;
                
            default:
                throw new Exception("Mode not setup here - " + mode.toString());
        }
        
        //update the previous
        this.previousTime = current;
    }
    
    @Override
    public void dispose()
    {
        if (controller != null)
        {
            controller.dispose();
            controller = null;
        }
        
        if (board != null)
        {
            board.dispose();
            board = null;
        }
        
        if (paint != null)
            paint = null;
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
        
        if (getBoard() != null)
            getBoard().render(canvas);
        
        //draw stats
        canvas.drawText("Difficulty: " + this.difficulty.toString(), LOCATION_DIFFICULTY_X, LOCATION_DIFFICULTY_Y, paint);
        canvas.drawText("Size: " + this.size.getDescription(), LOCATION_SIZE_X, LOCATION_SIZE_Y, paint);
        
        //calculate time
        int secs = (int) (totalTime / 1000);
        final int mins = secs / 60;
        secs = secs % 60;
        final int milliseconds = (int) (totalTime % 1000);
        
        //time description
        final String desc = mins + ":" + String.format("%02d", secs) + ":" + String.format("%03d", milliseconds);
        
        //draw timer
        canvas.drawText("Time: " + desc, LOCATION_TIMER_X, LOCATION_TIMER_Y, paint);
    }
}