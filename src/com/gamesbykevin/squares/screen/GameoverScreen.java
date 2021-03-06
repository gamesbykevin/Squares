package com.gamesbykevin.squares.screen;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.SparseArray;
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
    private Paint paintMessage, paint;
    
    //the message to display
    private String message = "";
    
    //where we draw the image
    private int messageX = 0, messageY = 0;
    
    //time we have displayed text
    private long time;
    
    /**
     * The amount of time to wait until we render the game over menu
     */
    private static final long DELAY_MENU_DISPLAY = 1250;
    
    //do we display the menu
    private boolean display = false;
    
    //list of buttons
    private SparseArray<Button> buttons;
    
    //buttons to access each button list
    public static final int INDEX_BUTTON_NEW = 0;
    public static final int INDEX_BUTTON_REPLAY = 1;
    public static final int INDEX_BUTTON_MENU = 2;
    public static final int INDEX_BUTTON_RATE = 3;
    public static final int INDEX_BUTTON_LEVEL_SELECT = 4;
    
    public GameoverScreen(final MainScreen screen)
    {
        //store our parent reference
        this.screen = screen;
        
        //create new paint object
        this.paint = new Paint();
        
        //set the text size
        this.paint.setTextSize(24f);
        
        //set the color
        this.paint.setColor(Color.WHITE);
        
        //the start location
        final int x = MainScreen.BUTTON_X;
        int y = MainScreen.BUTTON_Y;

        //create a list of buttons
        this.buttons = new SparseArray<Button>();
        
        //create our buttons
        addButton(x, y, INDEX_BUTTON_NEW, "Next");

        y += MainScreen.BUTTON_Y_INCREMENT;
        addButton(x, y, INDEX_BUTTON_REPLAY, "Replay");
        
        y += MainScreen.BUTTON_Y_INCREMENT;
        addButton(x, y, INDEX_BUTTON_LEVEL_SELECT, "Level Select");
        
        y += MainScreen.BUTTON_Y_INCREMENT;
        addButton(x, y, INDEX_BUTTON_MENU, "Menu");
        
        y += MainScreen.BUTTON_Y_INCREMENT;
        addButton(x, y, INDEX_BUTTON_RATE, MenuScreen.BUTTON_TEXT_RATE_APP);
    }
    
    /**
     * Add button to our list
     * @param x desired x-coordinate
     * @param y desired y-coordinate
     * @param index Position to place in our array list
     * @param description The text description to add
     */
    private void addButton(final int x, final int y, final int index, final String description)
    {
    	//create new button
    	Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
    	
    	//position the button
    	button.setX(x);
    	button.setY(y);
    	
    	//assign the dimensions
    	button.setWidth(MenuScreen.BUTTON_WIDTH);
    	button.setHeight(MenuScreen.BUTTON_HEIGHT);
    	button.updateBounds();
    	
    	//add the text description
    	button.addDescription(description);
    	button.positionText(paint);
    	
    	//add button to the list
    	this.buttons.put(index, button);
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
        setDisplay(false);
    }
    
    /**
     * Flag display
     * @param display true if we want to display the buttons, false otherwise
     */
    private void setDisplay(final boolean display)
    {
    	this.display = display;
    }
    
    /**
     * Do we display the buttons?
     * @return true = yes, false = no
     */
    private boolean hasDisplay()
    {
    	return this.display;
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
        if (paintMessage == null)
        {
            //assign metrics
        	paintMessage = new Paint();
        	paintMessage.setColor(Color.WHITE);
	        paintMessage.setTextSize(34f);
	        paintMessage.setTypeface(Font.getFont(Assets.FontMenuKey.Default));
        }
        
        //get the rectangle around the message
        paintMessage.getTextBounds(message, 0, message.length(), tmp);
        
        //calculate the position of the message
        this.messageX = (GamePanel.WIDTH / 2) - (tmp.width() / 2);
        this.messageY = (int)(GamePanel.HEIGHT * .12);
    }
    
    @Override
    public boolean update(final MotionEvent event, final float x, final float y) throws Exception
    {
        //if we aren't displaying the menu, return false
        if (!hasDisplay())
            return false;
        
        if (event.getAction() == MotionEvent.ACTION_UP)
        {
        	for (int index = 0; index < buttons.size(); index++)
        	{
        		//get the current button
        		Button button = buttons.get(index);
        		
        		//if we did not click this button skip to the next
        		if (!button.contains(x, y))
        			continue;
        		
                //remove message
                setMessage("");
                
        		//handle each button different
        		switch (index)
        		{
	        		case INDEX_BUTTON_LEVEL_SELECT:
	        			
	        			//mark that we have not made a selection
	        			screen.getScreenGame().getGame().getLevelSelect().setSelection(false);
	        			
	                    //move back to the game
	                    screen.setState(MainScreen.State.Running);
	                    
	                    //play sound effect
	                    Audio.play(Assets.AudioMenuKey.Selection);
	        			
	                    //we don't request additional motion events
	                    return false;
        		
	        		case INDEX_BUTTON_NEW:
	        			
	                    //move to the next level
	                    screen.getScreenGame().getGame().getLevelSelect().setLevelIndex(
	                    	screen.getScreenGame().getGame().getLevelSelect().getLevelIndex() + 1
	                    );
	                    
	                    //reset with the same settings
	                    screen.getScreenGame().getGame().reset();
	                    
	                    //move back to the game
	                    screen.setState(MainScreen.State.Running);
	                    
	                    //play sound effect
	                    Audio.play(Assets.AudioMenuKey.Selection);
	                    
	                    //we don't request additional motion events
	                    return false;

	        		case INDEX_BUTTON_REPLAY:
	                    
	                    //reset with the same settings
	                    screen.getScreenGame().getGame().reset();
	                    
	                    //move back to the game
	                    screen.setState(MainScreen.State.Running);
	                    
	                    //play sound effect
	                    Audio.play(Assets.AudioMenuKey.Selection);
	                    
	                    //we don't request additional motion events
	                    return false;
	        			
	        		case INDEX_BUTTON_MENU:
	                    
	                    //move to the main menu
	                    screen.setState(MainScreen.State.Ready);
	                    
	                    //play sound effect
	                    Audio.play(Assets.AudioMenuKey.Selection);
	                    
	                    //we don't request additional motion events
	                    return false;
	        			
	        		case INDEX_BUTTON_RATE:
	                    
	                    //play sound effect
	                    Audio.play(Assets.AudioMenuKey.Selection);
	                    
	                    //go to rate game page
	                    screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_RATE_URL);
	                    
	                    //we don't request additional motion events
	                    return false;
	        			
        			default:
        				throw new Exception("Index not setup here: " + index);
        		}
        	}
        }
        
        //no action was taken here
        return true;
    }
    
    @Override
    public void update() throws Exception
    {
        //if not displaying the menu, track timer
        if (!hasDisplay())
        {
            //if time has passed display menu
            if (System.currentTimeMillis() - time >= DELAY_MENU_DISPLAY)
            {
            	//flag true
                setDisplay(true);
            }
        }
    }
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //do we display the menu
        if (hasDisplay())
        {
            if (hasDisplay())
            {
                //only darken the background when the menu is displayed
            	MainScreen.darkenBackground(canvas);
                
                //if message exists, draw the text
                if (paintMessage != null)
                    canvas.drawText(this.message, messageX, messageY, paintMessage);
            
                //render the buttons
                for (int index = 0; index < buttons.size(); index++)
                {
                	buttons.get(index).render(canvas, paint);
                }
            }
        }
    }
    
    @Override
    public void dispose()
    {
        if (paintMessage != null)
        	paintMessage = null;
        
        if (paint != null)
        	paint = null;
        
        if (buttons != null)
        {
	        for (int index = 0; index < buttons.size(); index++)
	        {
	        	if (buttons.get(index) != null)
	        	{
	        		buttons.get(index).dispose();
	        		buttons.setValueAt(index, null);
	        	}
	        }
	        
	        buttons.clear();
	        buttons = null;
        }
    }
}