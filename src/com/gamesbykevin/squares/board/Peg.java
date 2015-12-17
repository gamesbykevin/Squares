package com.gamesbykevin.squares.board;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.base.Entity;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.squares.assets.Assets;

/**
 * A peg is one of the selections on the board the user can interact with
 * @author GOD
 */
public final class Peg extends Entity
{
    /**
     * Each type of peg fill, keep in this order so the animation will be mapped correctly
     */
    public enum Fill
    {
        Empty, OneQuarter, Half, ThreeQuarters, Full, Flagged
    }
    
    /**
     * The size of the image on the sprite sheet
     */
    private static final int DEFAULT_DIMENSION = 300;
    
    //the number of selections for this peg
    private int range;
    
    /**
     * The ratio to check for our contains() method
     */
    private static final float CONTAINS_RATIO = .75f;
    
    /**
     * Create new peg
     */
    protected Peg()
    {
        super();
        
        int index = 0;
        
        for (int row = 0; row < 2; row++)
        {
            for (int col = 0; col < 3; col++)
            {
                //don't continue once we mapped each key
                if (index >= Fill.values().length)
                    continue;
                
                //locate x, y
                final int x = col * DEFAULT_DIMENSION;
                final int y = row * DEFAULT_DIMENSION;
                
                //create animation
                Animation animation = new Animation(Images.getImage(Assets.ImageGameKey.Fill), x, y, DEFAULT_DIMENSION, DEFAULT_DIMENSION);
                
                //assign animation
                getSpritesheet().add(Fill.values()[index], animation);
                
                index++;
            }
        }
    }
    
    /**
     * Assign the range of this peg
     * @param range 
     * @throws Exception is thrown if invalid range is assigned
     */
    public void setRange(final int range) throws Exception
    {
        if (range != Board.DIFFICULTY_RANGE_DEFAULT && range != Board.DIFFICULTY_RANGE_EVIL)
            throw new Exception("Invalid range assigned - " + range);
        
        this.range = range;
    }
    
    /**
     * Get the range
     * @return The range of selections for this peg
     */
    public int getRange()
    {
        return this.range;
    }
    
    /**
     * Is the location inside the peg boundary?<br>
     * Here we add additional padding to check for collision.<br>
     * We will take the current (x, y) and check within 66% of the dimension to see
     * @param x x-coordinate
     * @param y y-coordinate
     * @return true if the specified (x,y) is close enough to the assigned (x,y) of this peg, false otherwise
     */
    public boolean contains(final float x, final float y)
    {
    	//make sure the x coordinate is inside our invisible boundary
    	if (x >= getX() - (getWidth() * CONTAINS_RATIO) && x <= getX() + (getWidth() * CONTAINS_RATIO))
    	{
    		//make sure the y coordinate is inside our invisible boundary
    		if (y >= getY() - (getHeight() * CONTAINS_RATIO) && y <= getY() + (getHeight() * CONTAINS_RATIO))
    			return true;
    	}
    	
    	//coordinates are not within the area, return false
    	return false;
    }
    
    /**
     * Assign the animation
     * @param count The count for the peg
     * @param flagged Is the peg flagged where we can't select
     * @throws Exception If range is not setup or invalid count provided
     */
    public void setAnimation(final int count, final boolean flagged) throws Exception
    {
    	//if flagged the animation will be different
    	if (flagged)
    	{
    		//set animation
    		getSpritesheet().setKey(Fill.Flagged);
    		
    		//no need to continue
    		return;
    	}
    	
        switch (getRange())
        {
            case Board.DIFFICULTY_RANGE_DEFAULT:
                switch (count)
                {
                    case 0:
                        getSpritesheet().setKey(Fill.Empty);
                        break;
                        
                    case 1:
                        getSpritesheet().setKey(Fill.Full);
                        break;
                        
                    default:
                        throw new Exception("Invalid count provided - " + count);
                }
                break;
                
            case Board.DIFFICULTY_RANGE_EVIL:
                switch (count)
                {
                    case 0:
                        getSpritesheet().setKey(Fill.Empty);
                        break;
                        
                    case 1:
                        getSpritesheet().setKey(Fill.Half);
                        break;
                        
                    case 2:
                        getSpritesheet().setKey(Fill.Full);
                        break;
                        
                    default:
                        throw new Exception("Invalid count provided - " + count);
                }
                break;
                
            default:
                throw new Exception("Range is not setup here - " + getRange());
        }
    }
}