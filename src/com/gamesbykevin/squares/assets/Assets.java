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
    private static final String DIRECTORY_AUDIO = "audio";
    
    /**
     * The directory where image resources are kept
     */
    private static final String DIRECTORY_IMAGE = "image";
    
    /**
     * The directory where font resources are kept
     */
    private static final String DIRECTORY_FONT = "font";
    
    /**
     * The directory where our text files are kept
     */
    private static final String DIRECTORY_TEXT = "text";
    
    /**
     * The different fonts used in our game.<br>
     * Order these according to the file name in the "font" assets folder.
     */
    public enum FontKey
    {
        Default, Dialog
    }
    
    /**
     * The different images in our game.<br>
     * Order these according to the file name in the "image" assets folder.
     */
    public enum ImageKey
    {
        Background, 
        Button,
        CancelDisabled, CancelEnabled,
        ConfirmDisabled, ConfirmEnabled,
        Fill, 
        LevelNotSolved,
        LevelSolved,
        Logo, 
        MenuDisabled, MenuEnabled,
        NumbersBeige, NumbersGreen, NumbersRed,
        PageNext, PagePrevious,
        PauseDisabled, PauseEnabled, 
        SoundDisabled, SoundEnabled,
    }
    
    /**
     * The key of each sound in our game.<br>
     * Order these according to the file name in the "audio" assets folder.
     */
    public enum AudioKey
    {
        GameoverLose, 
        MenuSeletion, 
        Song1, 
        Song2, 
        GameoverWin
    }
    
    /**
     * The text file containing the solution of each level, keep in alphabet order
     * @author GOD
     *
     */
    public enum TextKey
    {
    	Easy, Expert, Hard, Normal, Twisted
    }
    
    /**
     * Load all assets
     * @param activity Object containing AssetManager needed to load assets
     * @throws Exception 
     */
    public static final void load(final Activity activity) throws Exception
    {
        //load all images
        Images.load(activity, ImageKey.values(), DIRECTORY_IMAGE, true);
        
        //load all fonts
        Font.load(activity, FontKey.values(), DIRECTORY_FONT, true);
        
        //load all audio
        Audio.load(activity, AudioKey.values(), DIRECTORY_AUDIO, true);
        
        //load all text files
        Files.load(activity, TextKey.values(), DIRECTORY_TEXT, true);
    }
    
    /**
     * Recycle assets
     */
    public static void recycle()
    {
        Images.dispose();
        Font.dispose();
        Audio.dispose();
        Files.dispose();
    }
    
    /**
     * Play a random song, looping enabled
     */
    public static void playSong()
    {
        //pick random audio
        Assets.AudioKey key = GamePanel.RANDOM.nextBoolean() ? Assets.AudioKey.Song1 : Assets.AudioKey.Song2;

        //make no other songs are playing
        Audio.stop(Assets.AudioKey.Song1);
        Audio.stop(Assets.AudioKey.Song2);
        
        //set volume level
        Audio.setVolume(key, 0.5f);

        //start to play the loop
        Audio.play(key, true);
    }
}