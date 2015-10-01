package com.gamesbykevin.squaro.assets;

import android.app.Activity;

import com.gamesbykevin.androidframework.resources.*;
import com.gamesbykevin.squaro.panel.GamePanel;

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
        Logo, 
        MenuDisabled, MenuEnabled,
        NumbersBeige, NumbersGreen, NumbersRed,
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
     * Load all assets
     * @param activity Object containing AssetManager needed to load assets
     * @throws Exception 
     */
    public static final void load(final Activity activity) throws Exception
    {
        //load all images
        Images.load(activity, ImageKey.values(), DIRECTORY_IMAGE);
        
        //load all fonts
        Font.load(activity, FontKey.values(), DIRECTORY_FONT);
        
        //load all audio
        Audio.load(activity, AudioKey.values(), DIRECTORY_AUDIO);
    }
    
    /**
     * Recycle assets
     */
    public static void recycle()
    {
        Images.dispose();
        Font.dispose();
        Audio.dispose();
    }
    
    /**
     * Play a random song, looping enabled
     */
    public static void playSong()
    {
        //pick random audio
        Assets.AudioKey key = GamePanel.RANDOM.nextBoolean() ? Assets.AudioKey.Song1 : Assets.AudioKey.Song2;

        //set volume level
        Audio.setVolume(key, 0.25f);

        //start to play the loop
        Audio.play(key, true);
    }
}