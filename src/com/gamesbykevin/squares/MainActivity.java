package com.gamesbykevin.squares;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.gamesbykevin.squares.panel.GamePanel;

public class MainActivity extends Activity
{
    //our game panel
    private GamePanel panel;
    
    /**
     * Our web site address where more games can be found
     */
    public static final String WEBPAGE_MORE_GAMES_URL = "http://gamesbykevin.com";

    /**
     * The web address where this game can be rated
     */
    public static final String WEBPAGE_RATE_URL = "https://play.google.com/store/apps/details?id=com.gamesbykevin.squares";

    /**
     * The url that contains the instructions for the game
     */
    public static final String WEBPAGE_GAME_INSTRUCTIONS_URL = "http://gamesbykevin.com/2015/10/01/squares/";
    
    /**
     * The face book url
     */
    public static final String WEBPAGE_FACEBOOK_URL = "http://facebook.com/gamesbykevin";
    
    /**
     * The twitter url
     */
    public static final String WEBPAGE_TWITTER_URL = "http://twitter.com/gamesbykevin";
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        //call parent create
        super.onCreate(savedInstanceState);
        
        //turn the title off
        super.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //set the screen to full screen
        super.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //if the panel has not been created
        if (getGamePanel() == null)
        {
            //create the game panel
            setGamePanel(new GamePanel(this));

            //add callback to the game panel to intercept events
            getGamePanel().getHolder().addCallback(getGamePanel());
        }

        //set the content view to our game
        setContentView(getGamePanel());
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onStart()
    {
        super.onStart();
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onStop()
    {
        super.onStop();
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onDestroy()
    {
        //cleanup game panel
        if (getGamePanel() != null)
        {
            getGamePanel().dispose();
            setGamePanel(null);
        }
        
        //finish the current activity
        super.finish();
        
        //perform final cleanup
        super.onDestroy();
    }
    
    /**
     * Part of the activity life cycle
     */
    @Override
    public void onPause()
    {
        super.onPause();
    }
    
    /**
     * Navigate to the desired web page
     * @param url The desired url
     */
    public void openWebpage(final String url)
    {
        //create action view intent
        Intent intent = new Intent(Intent.ACTION_VIEW);
        
        //the content will be the web page
        intent.setData(Uri.parse(url));
        
        //start this new activity
        startActivity(intent);        
    }
    
    /**
     * Get the game panel.
     * @return The object containing our game logic, assets, threads, etc...
     */
    private GamePanel getGamePanel()
    {
        return this.panel;
    }
    
    /**
     * Assign the game panel
     * @param panel The game panel
     */
    private void setGamePanel(final GamePanel panel)
    {
        this.panel = panel;
    }
}