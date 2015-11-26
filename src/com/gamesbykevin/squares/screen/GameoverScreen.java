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
import com.gamesbykevin.squares.MainActivity;
import com.gamesbykevin.squares.assets.Assets;
import com.gamesbykevin.squares.panel.GamePanel;

/**
 * The game over screen
 * @author GOD
 */
public class GameoverScreen implements Screen, Disposable
{
    //our main screen reference
    private final MainScreen screen;
    
    //object to paint background
    private Paint paint, paintButton;
    
    //the message to display
    private String message = "";
    
    //the dimensions of the text message
    private int pixelW;
    
    //buttons
    private Button next, mainmenu, rateapp;
    
    //time we have displayed text
    private long time;
    
    /**
     * The amount of time to wait until we render the game over menu
     */
    private static final long DELAY_MENU_DISPLAY = 1250;
    
    //do we display the menu
    private boolean display = false;
    
    
    public GameoverScreen(final MainScreen screen)
    {
        //store our parent reference
        this.screen = screen;
        
        //create new paint object
        this.paintButton = new Paint();
        
        //set the text size
        this.paintButton.setTextSize(24f);
        
        //set the color
        this.paintButton.setColor(Color.WHITE);
        
        final int x = 110;
        int y = 75;
        final int addY = 100;

        //create our buttons
        y += addY;
        this.next = new Button(Images.getImage(Assets.ImageKey.Button));
        this.next.setX(x);
        this.next.setY(y);
        this.next.updateBounds();
        this.next.addDescription("Next");
        this.next.positionText(paintButton);
        
        y += addY;
        this.mainmenu = new Button(Images.getImage(Assets.ImageKey.Button));
        this.mainmenu.setX(x);
        this.mainmenu.setY(y);
        this.mainmenu.updateBounds();
        this.mainmenu.addDescription("Menu");
        this.mainmenu.positionText(paintButton);
        
        y += addY;
        this.rateapp = new Button(Images.getImage(Assets.ImageKey.Button));
        this.rateapp.setX(x);
        this.rateapp.setY(y);
        this.rateapp.updateBounds();
        this.rateapp.addDescription(MenuScreen.BUTTON_TEXT_RATE_APP);
        this.rateapp.positionText(paintButton);
    }
    
    /**
     * Reset any necessary screen elements here
     */
    @Override
    public void reset()
    {
        //reset timer
        time = System.currentTimeMillis();
        
        //do we display the menu
        display = false;
    }
    
    /**
     * Assign the message
     * @param message The message we want displayed
     */
    public void setMessage(final String message)
    {
        //assign the message
        this.message = message;
        
        //create temporary rectangle
        Rect tmp = new Rect();
        
        //create paint text object for the message
        if (paint == null)
            paint = new Paint();
        
        //assign metrics
        paint.setColor(Color.WHITE);
        paint.setTextSize(34f);
        paint.setTypeface(Font.getFont(Assets.FontKey.Default));
        
        //get the rectangle around the message
        paint.getTextBounds(message, 0, message.length(), tmp);
        
        //store the dimensions
        pixelW = tmp.width();
    }
    
    @Override
    public boolean update(final MotionEvent event, final float x, final float y) throws Exception
    {
        //if we aren't displaying the menu, return false
        if (!display)
            return false;
        
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            if (next.contains(x, y))
            {
                //stop sound
                Audio.stop();
                
                //move back to the game
                screen.setState(MainScreen.State.Running);
                
                //play sound effect
                Audio.play(Assets.AudioKey.MenuSeletion);
                
                //move to the next level
                screen.getScreenGame().getGame().getLevelSelect().setLevelIndex(
                	screen.getScreenGame().getGame().getLevelSelect().getLevelIndex() + 1
                );
                
                //restart game with the same settings
                screen.getScreenGame().getGame().reset();
            }
            else if (mainmenu.contains(x, y))
            {
                //stop sound
                Audio.stop();
                
                //play sound effect
                Audio.play(Assets.AudioKey.MenuSeletion);
                
                //move to the main menu
                screen.setState(MainScreen.State.Ready);
            }
            else if (rateapp.contains(x, y))
            {
                //stop sound
                Audio.stop();
                
                //play sound effect
                Audio.play(Assets.AudioKey.MenuSeletion);
                
                //go to rate game page
                screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_RATE_URL);
            }
        }
        
        //no action was taken here
        return true;
    }
    
    @Override
    public void update() throws Exception
    {
        //if not displaying the menu, track timer
        if (!display)
        {
            //if time has passed display menu
            if (System.currentTimeMillis() - time >= DELAY_MENU_DISPLAY)
            {
            	//flag true
                display = true;
            }
        }
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //do we display the menu
        if (display)
        {
        	//darken background
        	MainScreen.darkenBackground(canvas, MainScreen.ALPHA_DARK);
        	
            //render buttons
            next.render(canvas, paintButton);
            rateapp.render(canvas, paintButton);
            mainmenu.render(canvas, paintButton);
            
            if (paint != null)
            {
                //calculate middle
                final int x = (GamePanel.WIDTH / 2) - (pixelW / 2);
                final int y = (int)(GamePanel.HEIGHT * .15);
                 
                //draw text
                canvas.drawText(this.message, x, y, paint);
            }
        }
    }
    
    @Override
    public void dispose()
    {
        if (paint != null)
            paint = null;
        
        if (paintButton != null)
            paintButton = null;
        
        if (next != null)
        {
            next.dispose();
            next = null;
        }
        
        if (mainmenu != null)
        {
            mainmenu.dispose();
            mainmenu = null;
        }
        
        if (rateapp != null)
        {
            rateapp.dispose();
            rateapp = null;
        }
    }
}