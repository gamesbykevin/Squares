package com.gamesbykevin.squaro.screen;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.androidframework.screen.Screen;
import com.gamesbykevin.squaro.assets.Assets;
import com.gamesbykevin.squaro.game.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * This screen will contain the game options
 * @author GOD
 */
public class OptionsScreen implements Screen, Disposable
{
    //our logo reference
    private final Bitmap logo;
    
    //list of difficulty buttons
    private List<Button> difficulties;
    
    //list of different sizes
    private List<Button> sizes;
    
    //list of mode buttons
    private List<Button> modes;
    
    //list of buttons for the sound
    private List<Button> sounds;
    
    //store the index
    private int indexDifficulty = 0;
    private int indexMode = 0;
    private int indexSize = 0;
    
    //the go back button
    private Button back;
    
    //our main screen reference
    private final MainScreen screen;
    
    //paint object to draw text
    private Paint paint;
    
    public OptionsScreen(final MainScreen screen)
    {
        //our logo reference
        this.logo = Images.getImage(Assets.ImageKey.Logo);
        
        //store our screen reference
        this.screen = screen;
        
        //create new paint object
        this.paint = new Paint();
        
        //set the text size
        this.paint.setTextSize(24f);
        
        //set the color
        this.paint.setColor(Color.WHITE);
        
        //start coordinates
        final int x = 110;
        int y = 175;
        final int incrementY = 100;
        
        //add size option
        this.sizes = new ArrayList<Button>();
        
        //add audio option
        this.sounds = new ArrayList<Button>();
        
        Button button = new Button(Images.getImage(Assets.ImageKey.Button));
        button.setText("Sound: Disabled");
        button.setX(x);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        
        this.sounds.add(button);
        
        button = new Button(Images.getImage(Assets.ImageKey.Button));
        button.setText("Sound: Enabled");
        button.setX(x);
        button.setY(y);
        button.updateBounds();
        button.positionText(paint);
        
        this.sounds.add(button);
        
        //add buttons
        this.modes = new ArrayList<Button>();
        
        y += incrementY;
        for (Game.Mode mode : Game.Mode.values())
        {
            //create button
            button = new Button(Images.getImage(Assets.ImageKey.Button));
            
            //set the description
            button.setText("Mode: " + mode.getDescription());
            
            //assign location
            button.setX(x);
            button.setY(y);
            
            //set boundary
            button.updateBounds();
            
            //position text
            button.positionText(paint);
            
            //add to list
            modes.add(button);
        }
        
        //add buttons
        this.difficulties = new ArrayList<Button>();
        
        y += incrementY;
        for (Game.Difficulty diff : Game.Difficulty.values())
        {
            //create button
            button = new Button(Images.getImage(Assets.ImageKey.Button));
            
            //set the description
            button.setText("Difficutly: " + diff.getDescription());
            
            //assign location
            button.setX(x);
            button.setY(y);
            
            //set boundary
            button.updateBounds();
            
            //position text
            button.positionText(paint);
            
            //add to list
            difficulties.add(button);
        }
        
        //add size option
        this.sizes = new ArrayList<Button>();
        
        y += incrementY;
        for (Game.Size size : Game.Size.values())
        {
            //create button
            button = new Button(Images.getImage(Assets.ImageKey.Button));
            
            //set the description
            button.setText("Size: " + size.getDescription());
            
            //assign location
            button.setX(x);
            button.setY(y);
            
            //set boundary
            button.updateBounds();
            
            //position text
            button.positionText(paint);
            
            //add to list
            sizes.add(button);
        }
        
        y += incrementY;
        //the back button
        this.back = new Button(Images.getImage(Assets.ImageKey.Button));
        this.back.setText("Go Back");
        this.back.setX(x);
        this.back.setY(y);
        this.back.updateBounds();
        this.back.positionText(paint);
    }
    
    /**
     * Reset any necessary screen elements here
     */
    @Override
    public void reset()
    {
        //do we need anything here
    }
    
    @Override
    public boolean update(final MotionEvent event, final float x, final float y) throws Exception
    {
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            if (back.contains(x, y))
            {
                //play sound effect
                Audio.play(Assets.AudioKey.MenuSeletion);
                
                //set ready state
                screen.setState(MainScreen.State.Ready);
                
                //no need to continue
                return false;
            }
            
            for (Button button : sounds)
            {
                if (button.contains(x, y))
                {
                    //flip setting
                    Audio.setAudioEnabled(!Audio.isAudioEnabled());
                    
                    //play sound effect
                    Audio.play(Assets.AudioKey.MenuSeletion);
                    
                    //exit loop
                    return false;
                }
            }
            
            for (Button button : modes)
            {
                if (button.contains(x, y))
                {
                    //play sound effect
                    Audio.play(Assets.AudioKey.MenuSeletion);
                    
                    //increase index
                    this.indexMode++;
                    
                    if (this.indexMode >= modes.size())
                        this.indexMode = 0;
                    
                    //no need to continue
                    return false;
                }
            }
            
            for (Button button : sizes)
            {
                if (button.contains(x, y))
                {
                    //play sound effect
                    Audio.play(Assets.AudioKey.MenuSeletion);
                    
                    //increase index
                    this.indexSize++;
                    
                    if (this.indexSize >= sizes.size())
                        this.indexSize = 0;
                    
                    //no need to continue
                    return false;
                }
            }
            
            for (Button button : difficulties)
            {
                if (button.contains(x, y))
                {
                    //play sound effect
                    Audio.play(Assets.AudioKey.MenuSeletion);
                    
                    //increase index
                    this.indexDifficulty++;
                    
                    if (this.indexDifficulty >= difficulties.size())
                        this.indexDifficulty = 0;
                    
                    //no need to continue
                    return false;
                }
            }
        }
        
        //return true
        return true;
    }
    
    @Override
    public void update() throws Exception
    {
        //no updates needed here
    }
    
    /**
     * Get the mode index
     * @return The user selected mode
     */
    public int getIndexMode()
    {
        return this.indexMode;
    }
    
    /**
     * Get the difficulty index
     * @return The user selected difficulty
     */
    public int getIndexDifficulty()
    {
        return this.indexDifficulty;
    }
    
    /**
     * Get the size index
     * @return The desired user size of the board
     */
    public int getIndexSize()
    {
        return this.indexSize;
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //draw main logo
        canvas.drawBitmap(logo, MainScreen.LOGO_X, MainScreen.LOGO_Y, null);
        
        //draw the menu buttons
        difficulties.get(this.indexDifficulty).render(canvas, paint);
        modes.get(this.indexMode).render(canvas, paint);
        sizes.get(this.indexSize).render(canvas, paint);
        sounds.get(Audio.isAudioEnabled() ? 1 : 0).render(canvas, paint);
        back.render(canvas, paint);
    }
    
    @Override
    public void dispose()
    {
        if (back != null)
        {
            back.dispose();
            back = null;
        }
        
        if (difficulties != null)
        {
            for (Button button : difficulties)
            {
                if (button != null)
                {
                    button.dispose();
                    button = null;
                }
            }
            
            difficulties.clear();
            difficulties = null;
        }
        
        if (modes != null)
        {
            for (Button button : modes)
            {
                if (button != null)
                {
                    button.dispose();
                    button = null;
                }
            }
            
            modes.clear();
            modes = null;
        }
        
        if (sounds != null)
        {
            for (Button button : sounds)
            {
                if (button != null)
                {
                    button.dispose();
                    button = null;
                }
            }
            
            sounds.clear();
            sounds = null;
        }
        
        if (sizes != null)
        {
            for (Button button : sizes)
            {
                if (button != null)
                {
                    button.dispose();
                    button = null;
                }
            }
            
            sizes.clear();
            sizes = null;
        }
        
        if (paint != null)
            paint = null;
    }
}