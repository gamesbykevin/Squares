package com.gamesbykevin.squares.assets;

import android.app.Activity;

import com.gamesbykevin.androidframework.resources.*;
import com.gamesbykevin.squares.panel.GamePanel;

/**
 * This class will contain our game assets
 * @author GOD
 */
public class Assets 
{
    /**
     * The directory where audio sound effect resources are kept
     */
    private static final String DIRECTORY_MENU_AUDIO = "audio/menu";
    
    /**
     * The directory where audio sound effect resources are kept
     */
    private static final String DIRECTORY_GAME_AUDIO = "audio/game";
    
    /**
     * The directory where image resources are kept for the menu
     */
    private static final String DIRECTORY_MENU_IMAGE = "image/menu";
    
    /**
     * The directory where image resources are kept for the game
     */
    private static final String DIRECTORY_GAME_IMAGE = "image/game";
    
    /**
     * The directory where font resources are kept
     */
    private static final String DIRECTORY_MENU_FONT = "font/menu";
    
    /**
     * The directory where font resources are kept
     */
    private static final String DIRECTORY_GAME_FONT = "font/game";
    
    /**
     * The directory where our text files are kept
     */
    private static final String DIRECTORY_TEXT = "text";
    
    /**
     * The different fonts used in our game.<br>
     * Order these according to the file name in the "font" assets folder.
     */
    public enum FontMenuKey
    {
    	Default
    }
    
    /**
     * The different fonts used in our game.<br>
     * Order these according to the file name in the "font" assets folder.
     */
    public enum FontGameKey
    {
    	
    }
    
    /**
     * The different images for our menu items
     */
    public enum ImageMenuKey
    {
        Background, 
        Button,
        CancelDisabled, CancelEnabled,
        ConfirmDisabled, ConfirmEnabled,
        Facebook,
        Instructions,
        Twitter,
        Logo, 
        MenuDisabled, MenuEnabled,
        PauseDisabled, PauseEnabled, 
        SoundDisabled, SoundEnabled,
        Splash
    }
    
    /**
     * The different images in our game.<br>
     * Order these according to the file name in the "image" assets folder.
     */
    public enum ImageGameKey
    {
        Fill, 
        LevelNotSolved,
        LevelSolved,
        NumbersBeige, NumbersGreen, NumbersRed,
        PageNext, PagePrevious
    }
    
    /**
     * The key of each text file.<br>
     * Order these according to the file name in the "text" assets folder.
     */
    public enum TextKey
    {
    	Easy, Expert, Hard, Normal, Twisted
    }
    
    /**
     * The key of each sound in our game.<br>
     * Order these according to the file name in the "audio" assets folder.
     */
    public enum AudioMenuKey
    {
        Selection
    }
    
    /**
     * The key of each sound in our game.<br>
     * Order these according to the file name in the "audio" assets folder.
     */
    public enum AudioGameKey
    {
        GameoverLose, 
        Song1, 
        Song2, 
        GameoverWin
    }
	
    /**
     * Load all assets
     * @param activity Object containing AssetManager needed to load assets
     * @throws Exception 
     */
    public static final void load(final Activity activity) throws Exception
    {
        //load all images for the menu
        Images.load(activity, ImageMenuKey.values(), DIRECTORY_MENU_IMAGE, true);
        
        //load all fonts for the menu
        Font.load(activity, FontMenuKey.values(), DIRECTORY_MENU_FONT, true);
        
        //load all audio for the menu
        Audio.load(activity, AudioMenuKey.values(), DIRECTORY_MENU_AUDIO, true);
        
        //load images for the game
        Images.load(activity, ImageGameKey.values(), DIRECTORY_GAME_IMAGE, true);
        
        //load all audio for the game
        Audio.load(activity, AudioGameKey.values(), DIRECTORY_GAME_AUDIO, true);
        
        //load all fonts for the game
        Font.load(activity, FontGameKey.values(), DIRECTORY_GAME_FONT, true);
        
        //load all text files
        Files.load(activity, TextKey.values(), DIRECTORY_TEXT, true);
    }
    
    /**
     * Recycle assets
     */
    public static void recycle()
    {
        try
        {
            Images.dispose();
            Font.dispose();
            Audio.dispose();
            Files.dispose();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    /**
     * Play a random song, looping enabled
     */
    public static void playSong()
    {
        //pick random audio
        Assets.AudioGameKey key = GamePanel.RANDOM.nextBoolean() ? Assets.AudioGameKey.Song1 : Assets.AudioGameKey.Song2;

        //make no other songs are playing
        Audio.stop(Assets.AudioGameKey.Song1);
        Audio.stop(Assets.AudioGameKey.Song2);

        //start to play the loop
        Audio.play(key, true);
    }
}