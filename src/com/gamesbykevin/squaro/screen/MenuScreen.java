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
import com.gamesbykevin.squaro.MainActivity;
import com.gamesbykevin.squaro.assets.Assets;

import java.util.HashMap;

/**
 * Our main menu
 * @author ABRAHAM
 */
public class MenuScreen implements Screen, Disposable
{
    //the logo
    private final Bitmap logo;
    
    //our main screen reference
    private final MainScreen screen;
    
    //the buttons on the menu screen
    private HashMap<Key, Button> buttons;
    
    //object used to define font metrics
    private Paint paint;
    
    /**
     * Button text to display to exit the game
     */
    public static final String BUTTON_TEXT_EXIT_GAME = "Exit Game";
    
    /**
     * Button text to display to rate the game
     */
    public static final String BUTTON_TEXT_RATE_APP = "Rate this App";
    
    private enum Key
    {
        Start, Exit, Settings, Instructions, More, Rate
    }
    
    public MenuScreen(final MainScreen screen)
    {
        //store reference to the logo
        this.logo = Images.getImage(Assets.ImageKey.Logo);
        
        //store our screen reference
        this.screen = screen;
        
        //create a new hashmap
        this.buttons = new HashMap<Key, Button>();
        
        //temp button
        Button tmp;
        
        int y = 75;
        final int incrementY = 100;
        final int x = 110;
        
        y += incrementY;
        tmp = new Button(Images.getImage(Assets.ImageKey.Button));
        tmp.setX(x);
        tmp.setY(y);
        tmp.setText("Begin Game");
        this.buttons.put(Key.Start, tmp);
        
        y += incrementY;
        tmp = new Button(Images.getImage(Assets.ImageKey.Button));
        tmp.setX(x);
        tmp.setY(y);
        tmp.setText("Options");
        this.buttons.put(Key.Settings, tmp);
        
        y += incrementY;
        tmp = new Button(Images.getImage(Assets.ImageKey.Button));
        tmp.setX(x);
        tmp.setY(y);
        tmp.setText("Instuctions");
        this.buttons.put(Key.Instructions, tmp);
        
        y += incrementY;
        tmp = new Button(Images.getImage(Assets.ImageKey.Button));
        tmp.setX(x);
        tmp.setY(y);
        tmp.setText(BUTTON_TEXT_RATE_APP);
        this.buttons.put(Key.Rate, tmp);
        
        y += incrementY;
        tmp = new Button(Images.getImage(Assets.ImageKey.Button));
        tmp.setX(x);
        tmp.setY(y);
        tmp.setText("More Games");
        this.buttons.put(Key.More, tmp);
        
        y += incrementY;
        tmp = new Button(Images.getImage(Assets.ImageKey.Button));
        tmp.setX(x);
        tmp.setY(y);
        tmp.setText(BUTTON_TEXT_EXIT_GAME);
        this.buttons.put(Key.Exit, tmp);
        
        //create new paint object
        this.paint = new Paint();
        
        //set the text size
        this.paint.setTextSize(24f);
        
        //set the color
        this.paint.setColor(Color.WHITE);
        
        for (Button button : buttons.values())
        {
            button.updateBounds();
            button.positionText(paint);
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
            if (buttons.get(Key.Start).contains(x, y))
            {
                //play sound effect
                Audio.play(Assets.AudioKey.MenuSeletion);
                
                //set running state
                screen.setState(MainScreen.State.Running);

                //create the game
                screen.getScreenGame().createGame();
            }
            else if (buttons.get(Key.Settings).contains(x, y))
            {
                //play sound effect
                Audio.play(Assets.AudioKey.MenuSeletion);
                
                //set the state
                screen.setState(MainScreen.State.Options);
            }
            else if (buttons.get(Key.Instructions).contains(x, y))
            {
                //play sound effect
                Audio.play(Assets.AudioKey.MenuSeletion);
                
                //go to instructions
                this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_GAME_INSTRUCTIONS_URL);
            }
            else if (buttons.get(Key.Rate).contains(x, y))
            {
                //play sound effect
                Audio.play(Assets.AudioKey.MenuSeletion);
                
                //go to web page
                //this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_RATE_URL);
            }
            else if (buttons.get(Key.More).contains(x, y))
            {
                //play sound effect
                Audio.play(Assets.AudioKey.MenuSeletion);
                
                //go to web page
                this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_MORE_GAMES_URL);
            }
            else if (buttons.get(Key.Exit).contains(x, y))
            {
                //play sound effect
                Audio.play(Assets.AudioKey.MenuSeletion);
                
                //exit game
                this.screen.getPanel().getActivity().finish();
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
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //draw main logo
        canvas.drawBitmap(logo, MainScreen.LOGO_X, MainScreen.LOGO_Y, null);
        
        //draw the menu buttons
        if (buttons != null)
        {
            for (Button button : buttons.values())
            {
                if (button != null)
                    button.render(canvas, paint);
            }
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
        
        paint = null;
    }
}