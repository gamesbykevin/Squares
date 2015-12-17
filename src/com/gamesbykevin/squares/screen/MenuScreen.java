package com.gamesbykevin.squares.screen;

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
import com.gamesbykevin.squares.panel.GamePanel;
import com.gamesbykevin.squares.MainActivity;
import com.gamesbykevin.squares.assets.Assets;

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
     * Button text to display to start the game
     */
    public static final String BUTTON_TEXT_START = "Start Game";

    /**
     * Button text to display to start the game
     */
    public static final String BUTTON_TEXT_MORE = "More Games";
    
    /**
     * Button text to display for the game options
     */
    public static final String BUTTON_TEXT_OPTIONS = "Options";
    
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
        Start, Exit, Settings, Instructions, More, Rate, Twitter, Facebook
    }
    
    //start new game, and did we notify user
    private boolean reset = false, notify = false;
    
    /**
     * Dimension of the standard menu button
     */
    public static final int BUTTON_WIDTH = 301;
    
    /**
     * Dimension of the standard menu button
     */
    public static final int BUTTON_HEIGHT = 64;
    
    /**
     * The size of our icon buttons
     */
    public static final int ICON_DIMENSION = 64;
    
    public MenuScreen(final MainScreen screen)
    {
        //create new paint object
        this.paint = new Paint();
        
        //set the text size
        this.paint.setTextSize(24f);
        
        //set the color
        this.paint.setColor(Color.WHITE);
        
        //store reference to the logo
        this.logo = Images.getImage(Assets.ImageMenuKey.Logo);
        
        //store our screen reference
        this.screen = screen;
        
        //create a new hash map
        this.buttons = new HashMap<Key, Button>();
        
        int y = MainScreen.BUTTON_Y;
        final int x = MainScreen.BUTTON_X;
        
        addButton(Key.Start, x, y, BUTTON_TEXT_START);
        
        y += MainScreen.BUTTON_Y_INCREMENT;
        addButton(Key.Settings, x, y, BUTTON_TEXT_OPTIONS);
        
        y += MainScreen.BUTTON_Y_INCREMENT;
        addButton(Key.Rate, x, y, BUTTON_TEXT_RATE_APP);
        
        y += MainScreen.BUTTON_Y_INCREMENT;
        addButton(Key.More, x, y, BUTTON_TEXT_MORE);
        
        y += MainScreen.BUTTON_Y_INCREMENT;
        addButton(Key.Exit, x, y, BUTTON_TEXT_EXIT_GAME);
        
        //add social media icons
        addIcons();

        //set the size and bounds of the buttons
        for (Key key : Key.values())
        {
        	//get the current button
        	Button button = buttons.get(key);
        	
        	switch (key)
        	{
	        	case Twitter:
	        	case Facebook:
	        	case Instructions:
                	button.setWidth(ICON_DIMENSION);
                	button.setHeight(ICON_DIMENSION);
                	button.updateBounds();
	        		break;
        		
        		default:
                	button.setWidth(BUTTON_WIDTH);
                	button.setHeight(BUTTON_HEIGHT);
                    button.updateBounds();
                    button.positionText(paint);
        			break;
        	}
        }
    }
    
    /**
     * Add button to list
     * @param key Unique key of button
     * @param x x-coordinate
     * @param y y-coordinate
     * @param description Text to display for button
     */
    private void addButton(final Key key, final int x, final int y, final String description)
    {
        Button tmp = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        tmp.setX(x);
        tmp.setY(y);
        tmp.addDescription(description);
        this.buttons.put(key, tmp);
    }
    
    private void addIcons()
    {
        Button tmp = new Button(Images.getImage(Assets.ImageMenuKey.Instructions));
        tmp.setX(GamePanel.WIDTH - (ICON_DIMENSION * 4.5));
        tmp.setY(GamePanel.HEIGHT - (ICON_DIMENSION * 1.25));
        this.buttons.put(Key.Instructions, tmp);
        
        tmp = new Button(Images.getImage(Assets.ImageMenuKey.Facebook));
        tmp.setX(GamePanel.WIDTH - (ICON_DIMENSION * 3));
        tmp.setY(GamePanel.HEIGHT - (ICON_DIMENSION * 1.25));
        this.buttons.put(Key.Facebook, tmp);
        
        tmp = new Button(Images.getImage(Assets.ImageMenuKey.Twitter));
        tmp.setX(GamePanel.WIDTH - (ICON_DIMENSION * 1.5));
        tmp.setY(GamePanel.HEIGHT - (ICON_DIMENSION * 1.25));
        this.buttons.put(Key.Twitter, tmp);
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
        //if the game is to reset, don't continue
        if (reset)
            return false;
    	
        //we only want action up
        if (event.getAction() != MotionEvent.ACTION_UP)
        	return true;
        
    	//check every button
    	for (Key key : Key.values())
    	{
    		//get the current button
    		Button button = buttons.get(key);
    		
    		//if the coordinates are contained in the button
    		if (button.contains(x, y))
    		{
    			//do something different depending on the key
    			switch (key)
    			{
	        		case Instructions:
	                    //play sound effect
	                    Audio.play(Assets.AudioMenuKey.Selection);
	                    
	                    //go to instructions
	                    this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_GAME_INSTRUCTIONS_URL);
	                    
	                    //we do not request any additional events
	                    return false;
	                    
	        		case Facebook:
	                    //play sound effect
	                    Audio.play(Assets.AudioMenuKey.Selection);
	                    
	                    //go to instructions
	                    this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_FACEBOOK_URL);
	                    
	                    //we do not request any additional events
	                    return false;
	                    
	        		case Twitter:
	                    //play sound effect
	                    Audio.play(Assets.AudioMenuKey.Selection);
	                    
	                    //go to instructions
	                    this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_TWITTER_URL);
	                    
	                    //we do not request any additional events
	                    return false;
	        			
	        		case Start:
	                    //flag reset
	                    reset = true;
	                    
	                    //flag notify false
	                    notify = false;
	                    
	                    //play sound effect
	                    Audio.play(Assets.AudioMenuKey.Selection);
	                    
	                    //we do not request any additional events
	                    return false;
	                    
        			case Exit:
                        //play sound effect
                        Audio.play(Assets.AudioMenuKey.Selection);
                        
                        //exit game
                        this.screen.getPanel().getActivity().finish();
                        
                        //we do not request any additional events
                        return false;
                        
        			case Settings: 
                        //set the state
                        screen.setState(MainScreen.State.Options);
                        
                        //play sound effect
                        Audio.play(Assets.AudioMenuKey.Selection);
                        
                        //we do not request any additional events
                        return false;
                        
    				case More: 
    	                //play sound effect
    	                Audio.play(Assets.AudioMenuKey.Selection);
    	                
    	                //go to web page
    	                this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_MORE_GAMES_URL);
    	                
    	                //we do not request any additional events
    	                return false;
    	                
					case Rate:
		                //play sound effect
		                Audio.play(Assets.AudioMenuKey.Selection);
		                
		                //go to web page
		                this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_RATE_URL);
		                
		                //we do not request any additional events
		                return false;
		                
	                default:
	                	throw new Exception("Key not handled here: " + key);
    			}
    		}
    	}
        
        //return true
        return true;
    }
    
    @Override
    public void update() throws Exception
    {
    	//only reset if we notified the user by displaying the splash screen
        if (reset && notify)
        {
            //load game assets
            Assets.load(screen.getPanel().getActivity());

            //create the game
            screen.getScreenGame().createGame();

            //set running state
            screen.setState(MainScreen.State.Running);
            
            //we are done resetting
            reset = false;
            
            //play random song
            Assets.playSong();
        }
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        if (reset)
        {
            //render splash screen
            canvas.drawBitmap(Images.getImage(Assets.ImageMenuKey.Splash), 0, 0, null);
            
            //we notified the user
            notify = true;
        }
        else
        {
	        //draw main logo
	        canvas.drawBitmap(logo, MainScreen.LOGO_X, MainScreen.LOGO_Y, null);
	        
	        //draw the menu buttons
	        if (buttons != null)
	        {
	        	for (Key key : Key.values())
	        	{
	        		//get the current button
	        		Button button = buttons.get(key);
	        		
	        		//render the button accordingly
	        		switch (key)
	        		{
		        		case Instructions:
		        		case Facebook:
		        		case Twitter:
		        			button.render(canvas);
		        			break;
		        			
		        		case Start:
	        			case Exit:
	        			case Settings: 
        				case More: 
    					case Rate:
	        				button.render(canvas, paint);
	        				break;
	        				
        				default:
        					throw new Exception("Key is not handled here: " + key);
	        		}
	        	}
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