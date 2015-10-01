package com.gamesbykevin.squares.screen;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Font;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.androidframework.screen.Screen;
import com.gamesbykevin.squares.assets.Assets;
import com.gamesbykevin.squares.panel.GamePanel;
import java.util.HashMap;

/**
 * The exit screen, when the player wants to go back to the menu
 * @author GOD
 */
public class ExitScreen implements Screen, Disposable
{
    /**
     * Custom message displayed on screen
     */
    private static final String MESSAGE = "Go back to menu?";
    
    //the dimensions of the text message above
    private final int pixelW, pixelH;
    
    //our main screen reference
    private final MainScreen screen;
    
    //object to paint background
    private Paint paint;
    
    //all of the buttons for the player to control
    private HashMap<Assets.ImageKey, Button> buttons;
    
    public ExitScreen(final MainScreen screen)
    {
        //store our parent reference
        this.screen = screen;
        
        //create paint text object
        this.paint = new Paint();
        this.paint.setColor(Color.WHITE);
        this.paint.setTextSize(36F);
        this.paint.setTypeface(Font.getFont(Assets.FontKey.Default));
        
        //create temporary rectangle
        Rect tmp = new Rect();
        
        //get the rectangle around the message
        paint.getTextBounds(MESSAGE, 0, MESSAGE.length(), tmp);
        
        //store the dimensions
        pixelW = tmp.width();
        pixelH = tmp.height();
        
        //create buttons
        this.buttons = new HashMap<Assets.ImageKey, Button>();
        this.buttons.put(Assets.ImageKey.CancelDisabled, new Button(Images.getImage(Assets.ImageKey.CancelDisabled)));
        this.buttons.put(Assets.ImageKey.CancelEnabled, new Button(Images.getImage(Assets.ImageKey.CancelEnabled)));
        this.buttons.put(Assets.ImageKey.ConfirmDisabled, new Button(Images.getImage(Assets.ImageKey.ConfirmDisabled)));
        this.buttons.put(Assets.ImageKey.ConfirmEnabled, new Button(Images.getImage(Assets.ImageKey.ConfirmEnabled)));
        
        //position
        final int x = 50;
        final int y = 450;
        
        //position buttons
        this.buttons.get(Assets.ImageKey.ConfirmDisabled).setX(x);
        this.buttons.get(Assets.ImageKey.ConfirmDisabled).setY(y);
        this.buttons.get(Assets.ImageKey.ConfirmEnabled).setX(x);
        this.buttons.get(Assets.ImageKey.ConfirmEnabled).setY(y);
        this.buttons.get(Assets.ImageKey.CancelDisabled).setX(x + 273);
        this.buttons.get(Assets.ImageKey.CancelDisabled).setY(y);
        this.buttons.get(Assets.ImageKey.CancelEnabled).setX(x + 273);
        this.buttons.get(Assets.ImageKey.CancelEnabled).setY(y);
        
        //adjust the dimensions of each image
        for (Button button : buttons.values())
        {
            button.setWidth(button.getWidth() / 2);
            button.setHeight(button.getHeight() / 2);
            button.updateBounds();
        }
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
            if (buttons.get(Assets.ImageKey.CancelEnabled).contains(x, y))
            {
                //if cancel, go back to game
                screen.setState(MainScreen.State.Running);
                
                //play sound effect
                Audio.play(Assets.AudioKey.MenuSeletion);
                
                //return true;
                return false;
            }
            else if (buttons.get(Assets.ImageKey.ConfirmEnabled).contains(x, y))
            {
                //if confirm, go back to menu
                screen.setState(MainScreen.State.Ready);
                
                //play sound effect
                Audio.play(Assets.AudioKey.MenuSeletion);
                
                //return true;
                return false;
            }
        }
        
        return true;
    }
    
    @Override
    public void update() throws Exception
    {
        //nothing needed to update here
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        if (paint != null)
        {
            //calculate middle
            final int x = (GamePanel.WIDTH / 2) - (pixelW / 2);
            final int y = (GamePanel.HEIGHT / 2) - (pixelH / 2);
             
            //draw text
            canvas.drawText(MESSAGE, x, y, paint);
        }
        
        switch (screen.getState())
        {
            case Paused:
                buttons.get(Assets.ImageKey.ConfirmDisabled).render(canvas);
                buttons.get(Assets.ImageKey.CancelDisabled).render(canvas);
                break;
                
            default:
                buttons.get(Assets.ImageKey.ConfirmEnabled).render(canvas);
                buttons.get(Assets.ImageKey.CancelEnabled).render(canvas);
                break;
        }
    }
    
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
        
        if (paint != null)
            paint = null;
    }
}