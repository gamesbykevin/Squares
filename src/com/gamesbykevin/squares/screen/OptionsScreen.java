package com.gamesbykevin.squares.screen;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.SparseArray;
import android.view.MotionEvent;

import com.gamesbykevin.androidframework.awt.Button;
import com.gamesbykevin.androidframework.resources.Audio;
import com.gamesbykevin.androidframework.resources.Disposable;
import com.gamesbykevin.androidframework.resources.Images;
import com.gamesbykevin.androidframework.screen.Screen;
import com.gamesbykevin.squares.MainActivity;
import com.gamesbykevin.squares.panel.GamePanel;
import com.gamesbykevin.squares.assets.Assets;
import com.gamesbykevin.squares.game.Game;
import com.gamesbykevin.squares.storage.settings.Settings;

/**
 * This screen will contain the game options
 * @author GOD
 */
public class OptionsScreen implements Screen, Disposable
{
    //our logo reference
    private final Bitmap logo;
    
    //list of buttons on the options screen
    private SparseArray<Button> buttons;
    
    //our storage settings object
    private Settings settings;
    
    //buttons to access each button list
    public static final int INDEX_BUTTON_BACK = 0;
    public static final int INDEX_BUTTON_SOUND = 1;
    public static final int INDEX_BUTTON_DIFFICULTY = 2;
    public static final int INDEX_BUTTON_MODE = 3;
    public static final int INDEX_BUTTON_INSTRUCTIONS = 4;
    public static final int INDEX_BUTTON_FACEBOOK = 5;
    public static final int INDEX_BUTTON_TWITTER = 6;
    public static final int INDEX_BUTTON_HINT = 7;
    public static final int INDEX_BUTTON_VIBRATE = 8;
    
    //our main screen reference
    private final MainScreen screen;
    
    //paint object to draw text
    private Paint paint;
    
    public OptionsScreen(final MainScreen screen)
    {
        //our logo reference
        this.logo = Images.getImage(Assets.ImageMenuKey.Logo);

        //create buttons hash map
        this.buttons = new SparseArray<Button>();
        
        //store our screen reference
        this.screen = screen;
        
        //create new paint object
        this.paint = new Paint();
        
        //set the text size
        this.paint.setTextSize(24f);
        
        //set the color
        this.paint.setColor(Color.WHITE);
        
        //start coordinates
        final int x = MainScreen.BUTTON_X;
        int y = MainScreen.BUTTON_Y;
        
        addButtonSound(x, y);
        
        y += MainScreen.BUTTON_Y_INCREMENT;
        addButtonVibrate(x, y);
        
        y += MainScreen.BUTTON_Y_INCREMENT;
        addButtonHint(x, y);
        
        y += MainScreen.BUTTON_Y_INCREMENT;
        addButtonMode(x, y);
        
        y += MainScreen.BUTTON_Y_INCREMENT;
        addButtonDifficulty(x, y);
        
        y += MainScreen.BUTTON_Y_INCREMENT;
        addButtonBack(x, y);
        
        //add social media icons after the above, because the dimensions are different
        addIcons();
        
        //setup each button
        for (int index = 0; index < buttons.size(); index++)
        {
        	switch (index)
        	{
	        	case INDEX_BUTTON_INSTRUCTIONS:
	        	case INDEX_BUTTON_FACEBOOK:
	        	case INDEX_BUTTON_TWITTER:
	        		buttons.get(index).setWidth(MenuScreen.ICON_DIMENSION);
	        		buttons.get(index).setHeight(MenuScreen.ICON_DIMENSION);
	        		buttons.get(index).updateBounds();
	        		break;
        		
        		default:
                	buttons.get(index).setWidth(MenuScreen.BUTTON_WIDTH);
                	buttons.get(index).setHeight(MenuScreen.BUTTON_HEIGHT);
                	buttons.get(index).updateBounds();
                	buttons.get(index).positionText(paint);
        			break;
        	}
        }
        
        //create our settings object last, which will load the previous settings
        this.settings = new Settings(this, screen.getPanel().getActivity());
    }
    
    /**
     * Add icons, including links to social media
     */
    private void addIcons()
    {
        Button tmp = new Button(Images.getImage(Assets.ImageMenuKey.Instructions));
        tmp.setX(GamePanel.WIDTH - (MenuScreen.ICON_DIMENSION * 4.5));
        tmp.setY(GamePanel.HEIGHT - (MenuScreen.ICON_DIMENSION * 1.25));
        this.buttons.put(INDEX_BUTTON_INSTRUCTIONS, tmp);
        
        tmp = new Button(Images.getImage(Assets.ImageMenuKey.Facebook));
        tmp.setX(GamePanel.WIDTH - (MenuScreen.ICON_DIMENSION * 3));
        tmp.setY(GamePanel.HEIGHT - (MenuScreen.ICON_DIMENSION * 1.25));
        this.buttons.put(INDEX_BUTTON_FACEBOOK, tmp);
        
        tmp = new Button(Images.getImage(Assets.ImageMenuKey.Twitter));
        tmp.setX(GamePanel.WIDTH - (MenuScreen.ICON_DIMENSION * 1.5));
        tmp.setY(GamePanel.HEIGHT - (MenuScreen.ICON_DIMENSION * 1.25));
        this.buttons.put(INDEX_BUTTON_TWITTER, tmp);
    }
    
    /**
     * Get the list of buttons.<br>
     * We typically use this list to help load/set the settings based on the index of each button.
     * @return The list of buttons on the options screen
     */
    public SparseArray<Button> getButtons()
    {
    	return this.buttons;
    }
    
    private void addButtonBack(final int x, final int y)
    {
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.addDescription("Back");
        button.setX(x);
        button.setY(y);
        this.buttons.put(INDEX_BUTTON_BACK, button);
    }
    
    private void addButtonDifficulty(final int x, final int y)
    {
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        
        for (Game.Difficulty diff : Game.Difficulty.values())
        {
            //set the description
            button.addDescription("Difficutly: " + diff.getDescription());
        }
        
        button.setX(x);
        button.setY(y);
        this.buttons.put(INDEX_BUTTON_DIFFICULTY, button);
    }
    
    private void addButtonMode(final int x, final int y)
    {
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        
        for (Game.Mode mode : Game.Mode.values())
        {
            //set the description
            button.addDescription("Mode: " + mode.getDescription());
        }
        
        button.setX(x);
        button.setY(y);
        this.buttons.put(INDEX_BUTTON_MODE, button);
    }
    
    private void addButtonSound(final int x, final int y)
    {
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.addDescription("Sound: On");
        button.addDescription("Sound: Off");
        button.setX(x);
        button.setY(y);
        this.buttons.put(INDEX_BUTTON_SOUND, button);
    }
    
    private void addButtonHint(final int x, final int y)
    {
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.addDescription("Hint: Off");
        button.addDescription("Hint: On");
        button.setX(x);
        button.setY(y);
        this.buttons.put(INDEX_BUTTON_HINT, button);
    }
    
    private void addButtonVibrate(final int x, final int y)
    {
        Button button = new Button(Images.getImage(Assets.ImageMenuKey.Button));
        button.addDescription("Vibrate: On");
        button.addDescription("Vibrate: Off");
        button.setX(x);
        button.setY(y);
        this.buttons.put(INDEX_BUTTON_VIBRATE, button);
    }
    
    /**
     * Assign the index.
     * @param key The key of the button we want to change
     * @param index The desired index
     */
    public void setIndex(final int key, final int index)
    {
    	buttons.get(key).setIndex(index);
    }
    
    /**
     * Get the index selection of the specified button
     * @param key The key of the button we want to check
     * @return The current selection for the specified button key
     */
    public int getIndex(final int key)
    {
    	return buttons.get(key).getIndex();
    }
    
    /**
     * Reset any necessary screen elements here
     */
    @Override
    public void reset()
    {
        if (buttons != null)
        {
        	for (int key = 0; key < buttons.size(); key++)
        	{
        		//get the current button
        		Button button = buttons.get(key);
        		
        		try
        		{
	        		switch (key)
	        		{
						case INDEX_BUTTON_BACK:
						case INDEX_BUTTON_SOUND:
						case INDEX_BUTTON_MODE:
						case INDEX_BUTTON_DIFFICULTY:
						case INDEX_BUTTON_HINT:
						case INDEX_BUTTON_VIBRATE:
							button.positionText(paint);
							break;
							
						//do nothing for these
						case INDEX_BUTTON_INSTRUCTIONS:
						case INDEX_BUTTON_FACEBOOK:
						case INDEX_BUTTON_TWITTER:
							break;
							
						default:
							throw new Exception("Key not handled here: " + key);
	        		}
        		}
        		catch (Exception e)
        		{
        			e.printStackTrace();
        		}
        	}
        }
    }
    
    @Override
    public boolean update(final MotionEvent event, final float x, final float y) throws Exception
    {
    	//we only want motion event up
    	if (event.getAction() != MotionEvent.ACTION_UP)
    		return true;
    	
        if (buttons != null)
        {
        	for (int key = 0; key < buttons.size(); key++)
        	{
        		//get the current button
        		Button button = buttons.get(key);
        		
        		//if the button does not exist skip to the next
        		if (button == null)
        			continue;
        		
    			//if we did not select this button, skip to the next
    			if (!button.contains(x, y))
    				continue;
				
				//determine which button
				switch (key)
				{
    				case INDEX_BUTTON_BACK:
    					
    					//change index
    					button.setIndex(button.getIndex() + 1);
    					
    	                //store our settings
    	                settings.save();
    	                
    	                //set ready state
    	                screen.setState(MainScreen.State.Ready);
    	                
    	                //play sound effect
    	                Audio.play(Assets.AudioMenuKey.Selection);
    	                
    	                //no need to continue
    	                return false;
    	                
    				case INDEX_BUTTON_MODE:
    				case INDEX_BUTTON_DIFFICULTY:
    				case INDEX_BUTTON_HINT:
    				case INDEX_BUTTON_VIBRATE:
    					//change index
    					button.setIndex(button.getIndex() + 1);
    					
    					//position the text
    			        button.positionText(paint);
    					
    	                //play sound effect
    	                Audio.play(Assets.AudioMenuKey.Selection);
    	                
                        //no need to continue
                        return false;
    	                
    				case INDEX_BUTTON_SOUND:
    	    			
    					//change index
    					button.setIndex(button.getIndex() + 1);
    					
    					//position the text
    			        button.positionText(paint);
    			        
                        //flip setting
                        Audio.setAudioEnabled(!Audio.isAudioEnabled());
                        
                        //we also want to update the audio button in the controller so the correct is displayed
                        if (screen.getScreenGame() != null && screen.getScreenGame().getGame() != null)
                        {
                        	//make sure the controller exists
                    		if (screen.getScreenGame().getGame().getController() != null)
                    			screen.getScreenGame().getGame().getController().reset();
                        }
                        
                        //play sound effect
                        Audio.play(Assets.AudioMenuKey.Selection);
                        
                        //exit loop
                        return false;
                        
    				case INDEX_BUTTON_INSTRUCTIONS:
    					
    	                //play sound effect
    	                Audio.play(Assets.AudioMenuKey.Selection);
    	                
    	                //go to instructions
    	                this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_GAME_INSTRUCTIONS_URL);
    	                
    	                //we do not request any additional events
    	                return false;
    					
    				case INDEX_BUTTON_FACEBOOK:
    					
    	                //play sound effect
    	                Audio.play(Assets.AudioMenuKey.Selection);
    	                
    	                //go to instructions
    	                this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_FACEBOOK_URL);
    	                
    	                //we do not request any additional events
    	                return false;
    					
    				case INDEX_BUTTON_TWITTER:
    					
    	                //play sound effect
    	                Audio.play(Assets.AudioMenuKey.Selection);
    	                
    	                //go to instructions
    	                this.screen.getPanel().getActivity().openWebpage(MainActivity.WEBPAGE_TWITTER_URL);
    	                
    	                //we do not request any additional events
    	                return false;
    				
    				default:
                    	throw new Exception("Key not setup here: " + key);
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
    
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //draw our main logo
        canvas.drawBitmap(logo, MainScreen.LOGO_X, MainScreen.LOGO_Y, null);
        
        //draw the menu buttons
    	for (int index = 0; index < buttons.size(); index++)
    	{
    		if (buttons.get(index) != null)
    		{
    			switch (index)
    			{
	    			case INDEX_BUTTON_BACK:
	    			case INDEX_BUTTON_SOUND:
	    			case INDEX_BUTTON_MODE:
	    			case INDEX_BUTTON_DIFFICULTY:
	    			case INDEX_BUTTON_HINT:
	    			case INDEX_BUTTON_VIBRATE:
	    				buttons.get(index).render(canvas, paint);
	    				break;
	    				
	    			case INDEX_BUTTON_INSTRUCTIONS:
	    			case INDEX_BUTTON_FACEBOOK:
	    			case INDEX_BUTTON_TWITTER:
	    				buttons.get(index).render(canvas);
	    				break;
	    				
	    			default:
	    				throw new Exception("Button with index not setup here: " + index);
    			}
    		}
    	}
    }
    
    @Override
    public void dispose()
    {
        if (paint != null)
        	paint = null;
    	
        if (settings != null)
        {
            settings.dispose();
            settings = null;
        }
        
        if (buttons != null)
        {
        	for (int i = 0; i < buttons.size(); i++)
        	{
        		if (buttons.get(i) != null)
        		{
        			buttons.get(i).dispose();
        			buttons.put(i, null);
        		}
        	}
        	
        	buttons.clear();
        	buttons = null;
        }
    }
}