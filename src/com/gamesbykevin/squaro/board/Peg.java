package com.gamesbykevin.squaro.board;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.base.Entity;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.squaro.assets.Assets;

/**
 * A peg is one of the selections on the board the user can interact with
 * @author GOD
 */
public final class Peg extends Entity
{
    /**
     * Each type of peg fill
     */
    public enum Fill
    {
        Empty, OneQuarter, Half, ThreeQuarters, Full
    }
    
    /**
     * The size of the image on the sprite sheet
     */
    private static final int DEFAULT_DIMENSION = 300;
    
    //the number of selections for this peg
    private int range;
    
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
                Animation animation = new Animation(Images.getImage(Assets.ImageKey.Fill), x, y, DEFAULT_DIMENSION, DEFAULT_DIMENSION);
                
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
        if (range != Board.DIFFICULTY_RANGE_EASY && range != Board.DIFFICULTY_RANGE_MEDIUM && 
            range != Board.DIFFICULTY_RANGE_HARD)
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
     * Assign the animation
     * @param count The count for the peg
     * @throws Exception If range is not setup or invalid count provided
     */
    public void setAnimation(final int count) throws Exception
    {
        switch (getRange())
        {
            case Board.DIFFICULTY_RANGE_EASY:
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
            
            case Board.DIFFICULTY_RANGE_MEDIUM:
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
                
            case Board.DIFFICULTY_RANGE_HARD:
                switch (count)
                {
                    case 0:
                        getSpritesheet().setKey(Fill.Empty);
                        break;
                        
                    case 1:
                        getSpritesheet().setKey(Fill.OneQuarter);
                        break;
                        
                    case 2:
                        getSpritesheet().setKey(Fill.Half);
                        break;
                        
                    case 3:
                        getSpritesheet().setKey(Fill.ThreeQuarters);
                        break;
                        
                    case 4:
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