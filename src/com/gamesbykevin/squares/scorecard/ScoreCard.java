package com.gamesbykevin.squares.scorecard;

import android.app.Activity;

import com.gamesbykevin.androidframework.io.storage.Internal;
import com.gamesbykevin.squares.game.Game;
import com.gamesbykevin.squares.screen.OptionsScreen;

import java.util.ArrayList;
import java.util.List;

/**
 * Here we will track the best time and save it to the internal storage
 * @author GOD
 */
public final class ScoreCard extends Internal
{
    //list of scores
    private List<Score> scores;
    
    /**
     * New score separator string
     */
    private static final String NEW_SCORE = ";";
    
    /**
     * This string will separate the data for a score
     */
    private static final String SEPARATOR = "-";
    
    //our game reference object
    private final Game game;
    
    public ScoreCard(final Game game, final Activity activity)
    {
        super("ScoreCard", activity);
        
        //store our game reference object
        this.game = game;
        
        //create new score
        this.scores = new ArrayList<Score>();
        
        //make sure content exists before we try to load it
        if (super.getContent().toString().trim().length() > 0)
        {
            //load file with each level on a new line
            final String[] scores = super.getContent().toString().split(NEW_SCORE);

            //load each level into our array
            for (int index = 0; index < scores.length; index++)
            {
                //split level data
                String[] data = scores[index].split(SEPARATOR);

                //get the information
                final int level = Integer.parseInt(data[0]);
                final int size = Integer.parseInt(data[1]);
                final long time = Long.parseLong(data[2]);

                //load the score to our list
                update(level, size, time);
            }
        }
    }
    
    /**
     * Do we have the score?<br>
     * We want the score of the specified level and size
     * @param level The level index
     * @param size The size index
     * @return true if the specified score exists, false otherwise
     */
    public boolean hasScore(final int level, final int size)
    {
    	return (getScore(level, size) != null);
    }
    
    /**
     * Do we have the score?<br>
     * We will get the score of the current level and difficulty and see if exists
     * @return true if the specified score exists, false otherwise
     */
    public boolean hasScore()
    {
    	//use the current level index, and maze difficulty
    	return hasScore(game.getLevelSelect().getLevelIndex(), game.getMainScreen().getScreenOptions().getIndex(OptionsScreen.INDEX_BUTTON_DIFFICULTY));
    }
    
    /**
     * Get the score object.<br>
     * We will get the score of the current level and difficulty
     * @return The score object of the current level and difficulty, if not found null is returned
     */
    public Score getScore()
    {
    	return getScore(game.getLevelSelect().getLevelIndex(), game.getMainScreen().getScreenOptions().getIndex(OptionsScreen.INDEX_BUTTON_DIFFICULTY));
    }
    
    /**
     * Get the score object of the specified level and difficulty
     * @param level The level index
     * @param difficulty The level difficulty
     * @return The score object of the specified level and difficulty, if not found null is returned
     */
    public Score getScore(final int level, final int difficulty)
    {
    	//check our list to see if the specified score is better
    	for (Score score : scores)
    	{
    		//if the level and difficulty match return the score
    		if (score.getLevel() == level && score.getDifficulty() == difficulty)
    			return score;
    	}
    	
    	//score was not found
    	return null;
    }
    
    /**
     * Update the score with the specified time if the time is less than the existing.<br>
     * If the score does not exist, it will be added.<br>
     * @param level The specified level
     * @param difficulty The difficulty of the level
     * @param time The time duration
     * @return true if updating the score was successful, false otherwise
     */
    public boolean update(final int level, final int difficulty, final long time)
    {
    	//our score object
    	Score score = getScore(level, difficulty);
    	
    	if (score == null)
    	{
    		//score does not exist, so add it
    		scores.add(new Score(level, difficulty, time));
    	}
    	else
    	{
    		//if the time is less update the time
    		if (time < score.getTime())
    		{
    			score.setTime(time);
    		}
    		else
    		{
    			//score was not updated
    			return false;
    		}
    	}
    	
    	//save the score
    	save();
    	
    	//score was updated
    	return true;
    }
    
    /**
     * Save the scores to the internal storage
     */
    @Override
    public void save()
    {
        //remove all existing content
        super.getContent().delete(0, super.getContent().length());
        
        for (Score score : scores)
        {
            //if content exists, add delimiter to separate each score
            if (super.getContent().length() > 0)
                super.getContent().append(NEW_SCORE);
            
            //write level, size, and time
            super.getContent().append(score.getLevel());
            super.getContent().append(SEPARATOR);
            super.getContent().append(score.getDifficulty());
            super.getContent().append(SEPARATOR);
            super.getContent().append(score.getTime());
        }
        
        //save the content to physical internal storage location
        super.save();
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        if (scores != null)
        {
            scores.clear();
            scores = null;
        }
    }
}