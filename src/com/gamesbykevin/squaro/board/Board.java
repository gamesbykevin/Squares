package com.gamesbykevin.squaro.board;

import com.gamesbykevin.androidframework.base.Entity;

import android.graphics.Canvas;

import com.gamesbykevin.squaro.block.BlockKey;
import com.gamesbykevin.squaro.panel.GamePanel;

/**
 * The game board
 * @author GOD
 */
public final class Board extends Entity implements IBoard
{
    /**
     * The size of a default board
     */
    public static final int DEFAULT_DIMENSIONS = 3;
    
    /**
     * The size of a small board
     */
    public static final int SIZE_SMALL = 2;
    
    /**
     * The size of a medium board
     */
    public static final int SIZE_MEDIUM = 3;
    
    /**
     * The size of a large board
     */
    public static final int SIZE_LARGE = 4;
    
    /**
     * The size of a very large board
     */
    public static final int SIZE_VERY_LARGE = 5;
    
    /**
     * The range for easy game play
     */
    public static final int DIFFICULTY_RANGE_EASY = 2;
    
    /**
     * The range for medium game play
     */
    public static final int DIFFICULTY_RANGE_MEDIUM = 3;
    
    /**
     * The range for hard game play
     */
    public static final int DIFFICULTY_RANGE_HARD = 5;
    
    //the key to the board
    private int[][] solution;
    
    //the key that the player interacts with
    private int[][] player;
    
    //the block key object
    private BlockKey blockKey;
    
    //a peg is one of the corners of a single block
    private Peg peg;
    
    /**
     * The amount of pixels on each side
     */
    private static final int PIXEL_PADDING = 15;
    
    /**
     * Create a new Board
     * @throws Exception
     */
    public Board() throws Exception
    {
        //create a new block key
        this.blockKey = new BlockKey();
        
        //create a new peg
        this.peg = new Peg();
        
        //set default board
        reset(DEFAULT_DIMENSIONS, DEFAULT_DIMENSIONS, DIFFICULTY_RANGE_HARD);
    }
    
    @Override
    public void dispose()
    {
        super.dispose();
        
        if (peg != null)
        {
            peg.dispose();
            peg = null;
        }
        
        if (blockKey != null)
        {
            blockKey.dispose();
            blockKey = null;
        }
        
        this.solution = null;
        this.player = null;
    }
    
    /**
     * Reset the board
     * @param cols Total columns on the board
     * @param rows Total rows on the board
     * @param range The difficulty range
     * @throws Exception
     */
    public final void reset(final int cols, final int rows, final int range) throws Exception
    {
        //create the new key to the board
        this.solution = new int[rows + 1][cols + 1];
        
        //create new key board for the player
        this.player = new int[rows + 1][cols + 1];
        
        for (int row = 0; row < getSolution().length; row++)
        {
            for (int col  = 0; col < getSolution()[0].length; col++)
            {
                //assign random value
                getSolution()[row][col] = GamePanel.RANDOM.nextInt(range);
                
                //reset player back to 0
                getPlayer()[row][col] = 0;
            }
        }
        
        //assign cell dimensions
        super.setWidth((GamePanel.WIDTH - PIXEL_PADDING - PIXEL_PADDING) / getSolution()[0].length);
        super.setHeight(super.getWidth());
        
        //assign the range
        this.peg.setRange(range);
        
        //assign the peg dimensions, now that the board width is set
        this.peg.setWidth(getWidth() * .5);
        this.peg.setHeight(peg.getWidth());
        
        //calculate the width of the key board
        final double width = (getSolution()[0].length - 1) * getWidth();
        
        //position the start point in the middle
        setX((GamePanel.WIDTH / 2) - (width / 2));
        setY(PIXEL_PADDING * 4);
    }
    
    /**
     * Get the key board
     * @return The 
     */
    protected int[][] getSolution()
    {
        return this.solution;
    }
    
    /**
     * 
     * @return 
     */
    protected int[][] getPlayer()
    {
        return this.player;
    }
    
    /**
     * Update the pegs on the board
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void update(final float x, final float y)
    {
        //render the pegs
        for (int row = 0; row < getPlayer().length; row++)
        {
            for (int col  = 0; col < getPlayer()[0].length; col++)
            {
                //calculate coordinates
                peg.setX(BoardHelper.getStartX(this, col) - (peg.getWidth() / 2));
                peg.setY(BoardHelper.getStartY(this, row) - (peg.getHeight() / 2));
                
                //if the coordinate is inside the object
                if (peg.getDestination().contains((int)x, (int)y))
                {
                    //increase the count
                    getPlayer()[row][col]++;
                    
                    //if out of range, reset
                    if (getPlayer()[row][col] >= peg.getRange())
                        getPlayer()[row][col] = 0;
                }
            }
        }
    }
    
    /**
     * Render the board
     * @param canvas Object to write pixels to
     * @throws Exception
     */
    @Override
    public void render(final Canvas canvas) throws Exception
    {
        //render the block keys
        for (int row = 0; row < getSolution().length - 1; row++)
        {
            for (int col  = 0; col < getSolution()[0].length - 1; col++)
            {
                //the actual solution count
                final int countActual = BoardHelper.getCount(getSolution(), col, row);

                //the count of the player entry
                final int countPlayer = BoardHelper.getCount(getPlayer(), col, row);;

                //assign the proper animation
                blockKey.setAnimation(countActual == countPlayer, countActual);
                
                //calculate coordinates
                final int x = BoardHelper.getStartX(this, col);
                final int y = BoardHelper.getStartY(this, row);
                
                //assign coordinates and render the image
                blockKey.setX(x);
                blockKey.setY(y);
                blockKey.setWidth(getWidth());
                blockKey.setHeight(getHeight());
                blockKey.render(canvas);
            }
        }
        
        //render the pegs
        for (int row = 0; row < getSolution().length; row++)
        {
            for (int col  = 0; col < getSolution()[0].length; col++)
            {
                //calculate coordinates
                peg.setX(BoardHelper.getStartX(this, col) - (peg.getWidth() / 2));
                peg.setY(BoardHelper.getStartY(this, row) - (peg.getHeight() / 2));
                
                //assign the animation
                peg.setAnimation(getPlayer()[row][col]);
                
                //render the image
                peg.render(canvas);
            }
        }
    }
}