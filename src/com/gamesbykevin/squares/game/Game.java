package com.gamesbykevin.squares.game;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.level.Select;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.androidframework.text.TimeFormat;
import com.gamesbykevin.squares.assets.Assets;
import com.gamesbykevin.squares.board.Board;
import com.gamesbykevin.squares.board.BoardHelper;
import com.gamesbykevin.squares.game.controller.Controller;
import com.gamesbykevin.squares.panel.GamePanel;
import com.gamesbykevin.squares.scorecard.Score;
import com.gamesbykevin.squares.scorecard.ScoreCard;
import com.gamesbykevin.squares.screen.MainScreen;
import com.gamesbykevin.squares.screen.OptionsScreen;

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
    
    //the game score card
    private ScoreCard scoreCard;
    
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
        Hard("Hard"),
        Expert("Expert"),
        Twisted("Twisted");
        
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
    
    //the board where game play will take place
    private Board board;
    
    //our level select object
    private Select levelSelect;
    
    //store mode setting
    private Mode mode;
    
    //store difficulty setting
    private Difficulty difficulty;
    
    /**
     * For timed mode, the number of blocks will be a factor to determine the time remaining
     */
    private static final long TIMED_BLOCK_DURATION = 10000L;
    
    /**
     * The multiplier to the timer for timed mode if the difficulty is the hardest
     */
    private static final double TWISTED_DIFFICULTY_MULTIPLIER = 1.5;
    
    //where the timer is displayed
    public static final int LOCATION_TIMER_X = 100;
    public static final int LOCATION_TIMER_Y = 600;
    
    //where the difficulty is displayed
    private static final int LOCATION_DIFFICULTY_X = 100;
    private static final int LOCATION_DIFFICULTY_Y = LOCATION_TIMER_Y + 40;
    
    //where the level is displayed
    private static final int LOCATION_LEVEL_X = 100;
    private static final int LOCATION_LEVEL_Y = LOCATION_DIFFICULTY_Y + 40;
    
    //paint object to draw text
    private Paint paint;
    
    //track the time (milliseconds)
    private long totalTime = 0L;
    
    //track the previous time (milliseconds)
    private long previousTime = 0L;
    
    //the time if we are counting down, used for display purposes
    private long countdownTime = 0L;
    
    //do we stop the timer
    private boolean stopTimer = false;
    
    public Game(final MainScreen screen) throws Exception
    {
        //our main screen object reference
        this.screen = screen;
        
        //create score card
        this.scoreCard = new ScoreCard(this, screen.getPanel().getActivity());
        
        //create new paint object
        this.paint = new Paint();
        this.paint.setTextSize(24f);
        this.paint.setColor(Color.WHITE);
        
        //create new controller
        this.controller = new Controller(this);
        
        //create the level select screen
        this.levelSelect = new Select();
        this.levelSelect.setButtonNext(new Button(Images.getImage(Assets.ImageKey.PageNext)));
        this.levelSelect.setButtonOpen(new Button(Images.getImage(Assets.ImageKey.LevelNotSolved)));
        this.levelSelect.setButtonPrevious(new Button(Images.getImage(Assets.ImageKey.PagePrevious)));
        this.levelSelect.setButtonSolved(new Button(Images.getImage(Assets.ImageKey.LevelSolved)));
        this.levelSelect.setCols(5);
        this.levelSelect.setRows(8);
        this.levelSelect.setDimension(80);
        this.levelSelect.setDescription("", (GamePanel.WIDTH / 2) - 75, GamePanel.HEIGHT - 75);
        this.levelSelect.setPadding(5);
        this.levelSelect.setStartX(25);
        this.levelSelect.setStartY(15);
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
     * Get the score card
     * @return Our list of levels and the high score for this player
     */
    public ScoreCard getScorecard()
    {
    	return this.scoreCard;
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
     * @throws Exception 
     */
    public void reset(final Mode mode, final Difficulty difficulty) throws Exception
    {
        //assign settings
        this.mode = mode;
        this.difficulty = difficulty;
        
        //reset level select info
        getLevelSelect().reset();
        
        //determine how many levels there are
        switch (difficulty)
        {
		    case Easy:
		    	getLevelSelect().setTotal(301);
		    	break;
		    	
		    case Normal:
		    	getLevelSelect().setTotal(299);
		    	break;
		    	
		    case Hard:
		    	getLevelSelect().setTotal(225);
		    	break;
		    	
		    case Expert:
		    	getLevelSelect().setTotal(100);
		    	break;
		    	
		    case Twisted:
		    	getLevelSelect().setTotal(100);
		    	break;
		    	
			default:
				throw new Exception("Difficulty not setup here:" + difficulty.toString());
        }
        
        //load the saved data
        for (int levelIndex = 0; levelIndex < getLevelSelect().getTotal(); levelIndex++)
        {
        	//get the score for the specified level and difficulty
        	Score score = getScorecard().getScore(levelIndex, screen.getScreenOptions().getIndex(OptionsScreen.INDEX_BUTTON_DIFFICULTY));
        	
        	//mark completed if the score object exists
        	getLevelSelect().setCompleted(levelIndex, (score != null));
        }
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
            this.board = new Board();
        
        //reset the board
        switch (difficulty)
        {
		    case Easy:
		    	getBoard().reset(Assets.TextKey.Easy, getLevelSelect().getLevelIndex());
		    	break;
		    	
		    case Normal:
		    	getBoard().reset(Assets.TextKey.Normal, getLevelSelect().getLevelIndex());
		    	break;
		    	
		    case Hard:
		    	getBoard().reset(Assets.TextKey.Hard, getLevelSelect().getLevelIndex());
		    	break;
		    	
		    case Expert:
		    	getBoard().reset(Assets.TextKey.Expert, getLevelSelect().getLevelIndex());
		    	break;
		    	
		    case Twisted:
		    	getBoard().reset(Assets.TextKey.Twisted, getLevelSelect().getLevelIndex());
		    	break;
		    	
			default:
				throw new Exception("Difficulty not setup here:" + difficulty.toString());
        }
        
        //reset the timer
        this.totalTime = 0;
        
        //setup the game mode
        switch (mode)
        {
        	//no need to do anything here
            case Default:
                break;
                
            //the amount of time remaining will depend on the # of blocks
            case Timed:
            	//get the score for the specified level and difficulty
            	Score score = getScorecard().getScore(
            		getLevelSelect().getLevelIndex(), 
            		screen.getScreenOptions().getIndex(OptionsScreen.INDEX_BUTTON_DIFFICULTY));
            	
            	//if score exists, the count down will be the best time
            	if (score != null)
            	{
            		this.countdownTime = score.getTime();
            	}
            	else
            	{
            		//calculate the time limit
            		this.countdownTime = TIMED_BLOCK_DURATION * ((getBoard().getCols() - 1) * (getBoard().getRows() - 1));
            		
            		//the hardest difficulty we multiply the timer
                	if (difficulty == Difficulty.Twisted)
                		this.countdownTime *= TWISTED_DIFFICULTY_MULTIPLIER;
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
     * Get the level select
     * @return The level select object
     */
    public Select getLevelSelect()
    {
    	return this.levelSelect;
    }
    
    /**
     * Get the time
     * @return The total time elapsed milliseconds
     */
    public long getTime()
    {
    	return this.totalTime;
    }
    
    /**
     * Update the game based on the motion event
     * @param event Motion Event
     * @param x (x-coordinate)
     * @param y (y-coordinate)
     */
    public void update(final MotionEvent event, final float x, final float y)
    {
    	//if we don't have a selection
    	if (!getLevelSelect().hasSelection())
    	{
    		//if action up, check the location
    		if (event.getAction() == MotionEvent.ACTION_UP)
    			getLevelSelect().setCheck((int)x, (int)y);
    		
    		//don't continue
    		return;
    	}
    	
        //only update game if no controller buttons were clicked
        if (!getController().update(event, x, y))
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
                    //update the score card
                    getScorecard().update(
                		getLevelSelect().getLevelIndex(),
                		screen.getScreenOptions().getIndex(OptionsScreen.INDEX_BUTTON_DIFFICULTY),
                    	getTime()
                    );
                    
                    //load the saved data
                    for (int levelIndex = 0; levelIndex < getLevelSelect().getTotal(); levelIndex++)
                    {
                    	//get the score for the specified level and difficulty
                    	Score score = getScorecard().getScore(levelIndex, screen.getScreenOptions().getIndex(OptionsScreen.INDEX_BUTTON_DIFFICULTY)); 
                    	
                    	//mark completed if the score object exists
                    	getLevelSelect().setCompleted(levelIndex, (score != null));
                    }
                    
                	
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
    	if (!getLevelSelect().hasSelection())
    	{
    		//update the object
    		getLevelSelect().update();
    		
    		//if we have a selection now, reset the board
    		if (getLevelSelect().hasSelection())
    			reset();
    		
    		//no need to continue
    		return;
    	}
    	
        //if we stopped the timer, record the previous time
        if (this.stopTimer)
        {
            this.stopTimer = false;
            this.previousTime = System.currentTimeMillis();
        }
        
        //get the current time
        final long current = System.currentTimeMillis();
        
        //add the difference to the total time
        this.totalTime += (current - previousTime);
        
        //the timer will behave different depending on the mode
        switch (mode)
        {
        	//don't need to do anything here
            case Default:
                break;
                
            //check if time has run out
            case Timed:
            	
                //don't let the time go below 0
                if (this.countdownTime - this.totalTime < 0)
                {
                    //set time to 0
                    this.totalTime = this.countdownTime;
                    
                    //set the state
                    screen.setState(MainScreen.State.GameOver);
                    
                    //play sound
                    Audio.play(Assets.AudioKey.GameoverLose);
                    
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
        
        if (levelSelect != null)
        {
        	levelSelect.dispose();
        	levelSelect = null;
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
    	if (!getLevelSelect().hasSelection())
    	{
    		//render level select screen
    		getLevelSelect().render(canvas, this.paint);
    		
    		//no need to continue
    		return;
    	}
    	
        //draw the game controller
        if (getController() != null)
            getController().render(canvas);
        
        if (getBoard() != null)
            getBoard().render(canvas, paint);
        
        //draw timer accordingly
        if (mode == Mode.Timed)
        {
	        canvas.drawText(
	        	"Time: " + TimeFormat.getDescription(TimeFormat.FORMAT_1, countdownTime - totalTime), 
		        LOCATION_TIMER_X, 
		        LOCATION_TIMER_Y, 
		        paint
	        );
        }
        else
        {
	        canvas.drawText(
	        	"Time: " + TimeFormat.getDescription(TimeFormat.FORMAT_1, totalTime), 
	        	LOCATION_TIMER_X, 
	        	LOCATION_TIMER_Y, 
	        	paint
	        );
        }
        
        //draw difficulty
        canvas.drawText(
        	"Difficulty: " + this.difficulty.toString(), 
        	LOCATION_DIFFICULTY_X, 
        	LOCATION_DIFFICULTY_Y, 
        	paint
        );
        
        //draw level #
        canvas.drawText(
        	"Level: " + (this.getLevelSelect().getLevelIndex() + 1), 
        	LOCATION_LEVEL_X, 
        	LOCATION_LEVEL_Y, 
        	paint);
    }
}