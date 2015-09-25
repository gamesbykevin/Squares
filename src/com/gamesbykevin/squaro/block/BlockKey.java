package com.gamesbykevin.squaro.block;

import com.gamesbykevin.androidframework.anim.Animation;
import com.gamesbykevin.androidframework.base.Entity;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.squaro.assets.Assets;

/**
 * A block key will help the user solve the board
 * @author GOD
 */
public class BlockKey extends Entity
{
    public enum KeyValid
    {
        Valid_0, Valid_1, Valid_2, Valid_3, Valid_4, Valid_5, Valid_6, Valid_7, Valid_8,
        Valid_9, Valid_10, Valid_11, Valid_12, Valid_13, Valid_14, Valid_15, Valid_16
    }
    
    public enum KeyInValid
    {
        InValid_0, InValid_1, InValid_2, InValid_3, InValid_4, InValid_5, InValid_6, InValid_7, InValid_8, 
        InValid_9, InValid_10, InValid_11, InValid_12, InValid_13, InValid_14, InValid_15, InValid_16
    }
    
    /**
     * The size of the image on the sprite sheet
     */
    private static final int DEFAULT_DIMENSION = 256;
    
    /**
     * Create new block
     */
    public BlockKey()
    {
        super();
        
        int index = 0;
        
        for (int row = 0; row < 4; row++)
        {
            for (int col = 0; col < 5; col++)
            {
                //don't continue once we mapped each key
                if (index >= KeyValid.values().length)
                    continue;
                
                //locate x, y
                final int x = col * DEFAULT_DIMENSION;
                final int y = row * DEFAULT_DIMENSION;
                
                //create animation
                Animation animation = new Animation(Images.getImage(Assets.ImageKey.NumbersGreen), x, y, DEFAULT_DIMENSION, DEFAULT_DIMENSION);
                
                //assign animation
                super.getSpritesheet().add(KeyValid.values()[index], animation);
                
                index++;
            }
        }
        
        index = 0;
        
        for (int row = 0; row < 4; row++)
        {
            for (int col = 0; col < 5; col++)
            {
                //don't continue once we mapped each key
                if (index >= KeyInValid.values().length)
                    continue;
                
                //locate x, y
                final int x = col * DEFAULT_DIMENSION;
                final int y = row * DEFAULT_DIMENSION;
                
                //create animation
                Animation animation = new Animation(Images.getImage(Assets.ImageKey.NumbersRed), x, y, DEFAULT_DIMENSION, DEFAULT_DIMENSION);
                
                //assign animation
                super.getSpritesheet().add(KeyInValid.values()[index], animation);
                
                index++;
            }
        }
    }
    
    /**
     * Assign the animation
     * @param valid Is the count valid
     * @param count What is the count
     */
    public void setAnimation(final boolean valid, final int count)
    {
        switch (count)
        {
            case 0:
                setAnimation((!valid) ? KeyInValid.InValid_0 : KeyValid.Valid_0);
                break;
                
            case 1:
                setAnimation((!valid) ? KeyInValid.InValid_1 : KeyValid.Valid_1);
                break;
                
            case 2:
                setAnimation((!valid) ? KeyInValid.InValid_2 : KeyValid.Valid_2);
                break;
                
            case 3:
                setAnimation((!valid) ? KeyInValid.InValid_3 : KeyValid.Valid_3);
                break;
                
            case 4:
                setAnimation((!valid) ? KeyInValid.InValid_4 : KeyValid.Valid_4);
                break;
                
            case 5:
                setAnimation((!valid) ? KeyInValid.InValid_5 : KeyValid.Valid_5);
                break;
                
            case 6:
                setAnimation((!valid) ? KeyInValid.InValid_6 : KeyValid.Valid_6);
                break;
                
            case 7:
                setAnimation((!valid) ? KeyInValid.InValid_7 : KeyValid.Valid_7);
                break;
                
            case 8:
                setAnimation((!valid) ? KeyInValid.InValid_8 : KeyValid.Valid_8);
                break;
                
            case 9:
                setAnimation((!valid) ? KeyInValid.InValid_9 : KeyValid.Valid_9);
                break;
                
            case 10:
                setAnimation((!valid) ? KeyInValid.InValid_10 : KeyValid.Valid_10);
                break;
                
            case 11:
                setAnimation((!valid) ? KeyInValid.InValid_11 : KeyValid.Valid_11);
                break;
                
            case 12:
                setAnimation((!valid) ? KeyInValid.InValid_12 : KeyValid.Valid_12);
                break;
                
            case 13:
                setAnimation((!valid) ? KeyInValid.InValid_13 : KeyValid.Valid_13);
                break;
                
            case 14:
                setAnimation((!valid) ? KeyInValid.InValid_14 : KeyValid.Valid_14);
                break;
                
            case 15:
                setAnimation((!valid) ? KeyInValid.InValid_15 : KeyValid.Valid_15);
                break;
                
            case 16:
                setAnimation((!valid) ? KeyInValid.InValid_16 : KeyValid.Valid_16);
                break;
        }
    }
    
    /**
     * Assign the animation
     * @param key The animation in the sprite sheet
     */
    private void setAnimation(final Object key)
    {
        super.getSpritesheet().setKey(key);
    }
    
}