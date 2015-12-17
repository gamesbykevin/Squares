package com.gamesbykevin.squares.board;

import com.gamesbykevin.androidframework.base.Entity;
import com.gamesbykevin.androidframework.resources.Files;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.gamesbykevin.squares.assets.Assets;
import com.gamesbykevin.squares.block.BlockKey;
import com.gamesbykevin.squares.board.Peg.Fill;
import com.gamesbykevin.squares.game.Game;
import com.gamesbykevin.squares.panel.GamePanel;

/**
 * The game board
 * @author GOD
 */
public final class Board extends Entity implements IBoard
{
    /**
     * The default range for game play
     */
    public static final int DIFFICULTY_RANGE_DEFAULT = 2;
    
    /**
     * The range for evil game play
     */
    public static final int DIFFICULTY_RANGE_EVIL = 3;
    
    /**
     * The largest size of a board
     */
    public static final int DEFAULT_SIZE = 5;
    
    //is the specified peg flagged
    private boolean[][] flagged;
    
    //the key to the board
    private int[][] solution;
    
    //the key that the player interacts with
    private int[][] player;
    
    //the block key object
    private BlockKey blockKey;
    
    //a peg is one of the corners of a single block
    private Peg peg;
    
    //toggle between fill and flag
    private boolean fill = true;
    
    /**
     * The amount of pixels on each side
     */
    private static final int PIXEL_PADDING = 10;
    
    //store the dimensions
    private int cols, rows;
    
    /**
     * Create a new Board
     * @param cols
     * @param rows
     * @param range
     * @throws Exception 
     */
    public Board() throws Exception
    {
        //create a new block key
        this.blockKey = new BlockKey();
        
        //create a new peg
        this.peg = new Peg();
        
        //create the new key for the player and the solution
        this.solution = new int[Board.DEFAULT_SIZE + 1][Board.DEFAULT_SIZE + 1];
        this.player = new int[solution.length][solution[0].length];
        
        //create a new flagged list
        this.flagged = new boolean[solution.length][solution[0].length];
    }
    
    /**
     * Get the columns
     * @return Column dimension of the current board
     */
    public int getCols()
    {
        return this.cols;
    }
    
    /**
     * Get the rows
     * @return Row dimension of the current board
     */
    public int getRows()
    {
        return this.rows;
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
        this.flagged = null;
    }
    
    /**
     * Reset the board
     * @param key They key of the text file asset containing the levels
     * @param levelIndex The desired level
     * @param hint Do we reveal the solution to a row or column?
     * @throws Exception
     */
    @Override
    public final void reset(final Assets.TextKey key, final int levelIndex, final boolean hint) throws Exception
    {
    	//default to filling the pegs
    	this.fill = true;
    	
        //reset all to 0
        for (int row = 0; row < getSolution().length; row++)
        {
            for (int col  = 0; col < getSolution()[0].length; col++)
            {
                getPlayer()[row][col] = 0;
                getSolution()[row][col] = 0;
                getFlagged()[row][col] = false;
            }
        }
        
    	//get the line representing the solution in our text file
    	final String line = Files.getText(key).getLines().get(levelIndex);
        
    	//determine the size of the board
    	if (line.length() == 16)
    	{
    		this.cols = 4;
    		this.rows = 4;
    	}
    	else
    	{
    		this.cols = 6;
    		this.rows = 6;
    	}
    	
    	//the range of the peg we can select
    	int range = 0;
    	
        for (int row = 0; row < getRows(); row++)
        {
            for (int col  = 0; col < getCols(); col++)
            {
            	//calculate the character position
            	final int index = (row * getCols()) + col;
            	
            	//get the key
            	final int tmp = Integer.parseInt(line.substring(index, index + 1));
            	
            	//assign the key to the solution
            	getSolution()[row][col] = tmp;
            	
            	//check if we have a new range
            	if (tmp >= range)
            		range = tmp + 1;
            }
        }
        
        //if we have a hint enabled
        if (hint)
        {
        	//decide if we reveal a row or column
        	if (GamePanel.RANDOM.nextBoolean())
        	{
        		//we will reveal a row
        		final int row = GamePanel.RANDOM.nextInt(getRows());
        		
        		//reveal every column in the row
                for (int col  = 0; col < getCols(); col++)
                {
                    getPlayer()[row][col] = getSolution()[row][col];
                    
                    //if 0 lets flag this one
                    if (getPlayer()[row][col] == 0)
                    	getFlagged()[row][col] = true;
                }
        	}
        	else
        	{
        		//we will reveal a column
        		final int col = GamePanel.RANDOM.nextInt(getCols());
        		
        		//reveal every row in the column
                for (int row = 0; row < getRows(); row++)
                {
                	getPlayer()[row][col] = getSolution()[row][col];
                	
                    //if 0 lets flag this one
                    if (getPlayer()[row][col] == 0)
                    	getFlagged()[row][col] = true;
                }
        	}
        }
        
        //set dimension accordingly
        if (getCols() == 6)
        {
            //assign cell dimensions
            super.setWidth((GamePanel.WIDTH / (getCols() - 1)) - PIXEL_PADDING);
            super.setHeight(super.getWidth());
        }
        else
        {
            //assign cell dimensions
            super.setWidth((GamePanel.WIDTH / getCols()) - PIXEL_PADDING);
            super.setHeight(super.getWidth());
        }
        
        //assign the single block dimension
        blockKey.setWidth(getWidth());
        blockKey.setHeight(getHeight());
        
        //assign the range
        this.peg.setRange(range);
        
        //assign the peg dimensions, now that the board width is set
        this.peg.setWidth(getWidth() * .5);
        this.peg.setHeight(peg.getWidth());
        
        //calculate the width of the key board
        final double width = (getCols() - 1) * getWidth();
        
        //position the start point in the middle
        setX((GamePanel.WIDTH / 2) - (width / 2));
        setY(PIXEL_PADDING * 4);
    }
    
    /**
     * Get the key board
     * @return The key to solve the board
     */
    protected int[][] getSolution()
    {
        return this.solution;
    }
    
    /**
     * Get the player key
     * @return The key of player selections
     */
    protected int[][] getPlayer()
    {
        return this.player;
    }
    
    /**
     * Get the flagged key
     * @return The key containing which locations are flagged
     */
    protected boolean[][] getFlagged()
    {
    	return this.flagged;
    }
    
    private int getFillX()
    {
    	return Game.LOCATION_TIMER_X;
    }
    
    private int getFillY()
    {
    	return (int)(Game.LOCATION_TIMER_Y - peg.getHeight() - (peg.getHeight() / 2));
    }
    
    /**
     * Update the pegs on the board
     * @param x x-coordinate
     * @param y y-coordinate
     */
    public void update(final float x, final float y)
    {
        //render the pegs
        for (int row = 0; row < getRows(); row++)
        {
            for (int col  = 0; col < getCols(); col++)
            {
            	//calculate coordinates
            	peg.setX(BoardHelper.getStartX(this, col));
            	peg.setY(BoardHelper.getStartY(this, row));
            	
                //if the coordinate is inside the object
                if (peg.contains(x, y))
                {
                	//if we want to fill
                	if (fill)
                	{
                    	//if this location is flagged, we can't continue
                    	if (getFlagged()[row][col])
                    		continue;
                		
                        //increase the count
                        getPlayer()[row][col]++;
                        
                        //if out of range, reset
                        if (getPlayer()[row][col] >= peg.getRange())
                            getPlayer()[row][col] = 0;
                	}
                	else
                	{
                		//if flagged undo and vice versa
            			getFlagged()[row][col] = !getFlagged()[row][col];
            			
            			//reset back to 0
            			getPlayer()[row][col] = 0;
                	}
                	
                	//no need to continue
                	return;
                }
            }
        }
        
        //check the fill peg
        peg.setX(getFillX());
        peg.setY(getFillY());
        
        //if the coordinate is inside the object flip the setting
        if (peg.getDestination().contains((int)x, (int)y))
        	this.fill = !this.fill;
    }
    
    /**
     * Render the board
     * @param canvas Object to write pixels to
     * @param paint Object to write text
     * @throws Exception
     */
    @Override
    public void render(final Canvas canvas, final Paint paint) throws Exception
    {
        //render the block keys
        for (int row = 0; row < getRows() - 1; row++)
        {
            for (int col  = 0; col < getCols() - 1; col++)
            {
                //the actual solution count
                final int countSolution = BoardHelper.getCount(getSolution(), col, row);

                //the count of the player entry
                final int countPlayer = BoardHelper.getCount(getPlayer(), col, row);

                //assign the proper animation
                blockKey.setAnimation(countSolution, countPlayer);
                
                //assign coordinates and render the image
                blockKey.setX(BoardHelper.getStartX(this, col));
                blockKey.setY(BoardHelper.getStartY(this, row));
                blockKey.render(canvas);
            }
        }
        
        //render the pegs
        for (int row = 0; row < getRows(); row++)
        {
            for (int col  = 0; col < getCols(); col++)
            {
                //calculate coordinates
                peg.setX(BoardHelper.getStartX(this, col) - (peg.getWidth() / 2));
                peg.setY(BoardHelper.getStartY(this, row) - (peg.getHeight() / 2));
                
                //assign the animation
                peg.setAnimation(getPlayer()[row][col], getFlagged()[row][col]);
                
                //render the image
                peg.render(canvas);
            }
        }
        
        //render the peg flag option
        peg.setX(getFillX());
        peg.setY(getFillY());
    	peg.getSpritesheet().setKey((!fill) ? Fill.Flagged : Fill.Full);
    	peg.render(canvas);
    	
    	canvas.drawText(
    		(fill) ? " - Solve Game" : " - Flag Peg", 
    		(int)(getFillX() + peg.getWidth()), 
    		(int)((getFillY() + peg.getHeight()) - (peg.getHeight() * .33)), 
    		paint
    	);
    }
}