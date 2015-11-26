package com.gamesbykevin.squares.board;

/**
 * Board helper methods
 * @author GOD
 */
public class BoardHelper 
{
    /**
     * Do we have a match?
     * @param board The board containing the player and solution we want to check
     * @return true if all the values match, false otherwise
     */
    public static final boolean hasMatch(final Board board)
    {
    	return hasMatch(board.getSolution(), board.getPlayer());
    }
    
    /**
     * Do we have a match?
     * @param solution The solution key to the board
     * @param player The player key with their findings
     * @return true if the supplied player key matches the solution key
     */
    public static final boolean hasMatch(final int[][] solution, final int[][] player)
    {
        //make sure each location matches
        for (int row = 0; row < solution.length - 1; row++)
        {
            for (int col = 0; col < solution[0].length - 1; col++)
            {
                //if the count is not equal, there is no match
                if (getCount(player, col, row) != getCount(solution, col, row))
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
    protected static final int getStartX(final Board board, final double col)
    {
        return (int)(board.getX() + (col * board.getWidth()));
    }
    
    /**
     * Get the start y
     * @param board Board containing location
     * @param row The row where the coordinate is
     * @return The y-coordinate on the board
     */
    protected static final int getStartY(final Board board, final double row)
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