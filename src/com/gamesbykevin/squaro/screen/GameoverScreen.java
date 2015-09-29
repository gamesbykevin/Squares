package com.gamesbykevin.squaro.screen;

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
import com.gamesbykevin.squaro.MainActivity;
import com.gamesbykevin.squaro.assets.Assets;
import com.gamesbykevin.squaro.panel.GamePanel;

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
    private Button restart, mainmenu, rateapp, exitgame;
    
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
        this.restart = new Button(Images.getImage(Assets.ImageKey.Button));
        this.restart.setX(x);
        this.restart.setY(y);
        this.restart.updateBounds();
        this.restart.setText("New Game");
        this.restart.positionText(paintButton);
        
        y += addY;
        this.mainmenu = new Button(Images.getImage(Assets.ImageKey.Button));
        this.mainmenu.setX(x);
        this.mainmenu.setY(y);
        this.mainmenu.updateBounds();
        this.mainmenu.setText("Menu");
        this.mainmenu.positionText(paintButton);
        
        y += addY;
        this.rateapp = new Button(Images.getImage(Assets.ImageKey.Button));
        this.rateapp.setX(x);
        this.rateapp.setY(y);
        this.rateapp.updateBounds();
        this.rateapp.setText(MenuScreen.BUTTON_TEXT_RATE_APP);
        this.rateapp.positionText(paintButton);
        
        y += addY;
        this.exitgame = new Button(Images.getImage(Assets.ImageKey.Button));
        this.exitgame.setX(x);
        this.exitgame.setY(y);
        this.exitgame.updateBounds();
        this.exitgame.setText(MenuScreen.BUTTON_TEXT_EXIT_GAME);
        this.exitgame.positionText(paintButton);
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
        paint.setTextSize(32f);
        paint.setTypeface(Font.getFont(Assets.FontKey.Default));
        
        //get the rectangle around the message
        paint.getTextBounds(message, 0, message.length(), tmp);
        
        //store the dimensions
        pixelW = tmp.width();
    }
    
    @Override
    public boolean update(final MotionEvent event, final float x, final float y) throws Exception
    {
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
            if (restart.contains(x, y))
            {
                //play sound effect
                //Audio.play(Assets.AudioKey.SettingChange);
                
                //move back to the game
                screen.setState(MainScreen.State.Running);
                
                //restart game with the same settings
                screen.getGame().reset();
            }
            else if (mainmenu.contains(x, y))
            {
                //play sound effect
                //Audio.play(Assets.AudioKey.SettingChange);
                
                //move to the main menu
                screen.setState(MainScreen.State.Ready);
            }
            else if (rateapp.contains(x, y))
            {
                //play sound effect
                //Audio.play(Assets.AudioKey.SettingChange);
                
                //go to rate game page
                screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_RATE_URL);
            }
            else if (exitgame.contains(x, y))
            {
                //play sound effect
                //Audio.play(Assets.AudioKey.SettingChange);
                
                //exit game
                screen.getPanel().getActivity().finish();
            }
            return true;
        }
        
        //no action was taken here
        return false;
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
            final int y = (int)(GamePanel.HEIGHT * .15);
             
            //draw text
            canvas.drawText(this.message, x, y, paint);
        }
        
        //render buttons
        restart.render(canvas, paintButton);
        rateapp.render(canvas, paintButton);
        mainmenu.render(canvas, paintButton);
        exitgame.render(canvas, paintButton);
    }
    
    @Override
    public void dispose()
    {
        if (paint != null)
            paint = null;
        
        if (paintButton != null)
            paintButton = null;
        
        if (restart != null)
        {
            restart.dispose();
            restart = null;
        }
        
        if (mainmenu != null)
        {
            mainmenu.dispose();
            mainmenu = null;
        }
        
        if (exitgame != null)
        {
            exitgame.dispose();
            exitgame = null;
        }
        
        if (rateapp != null)
        {
            rateapp.dispose();
            rateapp = null;
        }
    }
}