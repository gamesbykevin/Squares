package com.gamesbykevin.squaro.board;

/**
 * Board helper methods
 * @author GOD
 */
public class BoardHelper 
{
    /**
     * Do we have a match
     * @param board The board we are checking
     * @return true if all the values match, false otherwise
     */
    public static final boolean hasMatch(final Board board)
    {
        //make sure each location matches
        for (int row = 0; row < board.getRows() - 1; row++)
        {
            for (int col = 0; col < board.getCols() - 1; col++)
            {
                //if the count is not equal, there is no match
                if (getCount(board.getPlayer(), col, row) != getCount(board.getSolution(), col, row))
                    return false;
            }
        }
        
        //we made it here, and now found a match
        return true;
    }
    
    /**
     * Get the start x
     * @param board Board containing location
     * @param col The col where the coordinate is
     * @return The x-coordinate on the board
     */
    protected static final int getStartX(final Board board, final int col)
    {
        return (int)(board.getX() + (col * board.getWidth()));
    }
    
    /**
     * Get the start y
     * @param board Board containing location
     * @param row The row where the coordinate is
     * @return The y-coordinate on the board
     */
    protected static final int getStartY(final Board board, final int row)
    {
        return (int)(board.getY() + (row * board.getHeight()));
    }
    
    /**
     * Get the count at the specified location
     * @param key The key containing the count for each location
     * @param col Column
     * @param row Row
     * @return The total of all 4 neighboring corners
     */
    protected static final int getCount(final int[][] key, final int col, final int row)
    {
        int count = 0;
        
        //add to total count
        count += key[row][col];
        count += key[row][col + 1];
        count += key[row + 1][col];
        count += key[row + 1][col + 1];
        
        return count;
    }
}
