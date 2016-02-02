import java.util.Random;

public class Maze
{
	int width;
	int height;
	chamber[][] maze;
	Random random;
	
	/**
	 * Constructor
	 * @param aWidth the number of chambers in each row
	 * @param aHeight the number of chamber in each column
	 */
	public Maze(int aWidth, int aHeight)
	{
		random = new Random();
		width = aWidth;
		height = aHeight;
		maze = new chamber[height][width];
		for (int y = 0; y<height; y++)
			for (int x = 0; x<width; x++)
			{	
				maze[y][x] = new chamber();
				if (y == 0) maze[y][x].nWall = true;
				else if (y == height - 1) maze[y][x].sWall = true;
				if (x == 0) maze[y][x].wWall = true;
				else if (x == width-1) maze[y][x].eWall = true;
			}	
		makeMaze(0, height-1, 0, width-1);
	}
	/**
	 * makeMaze
	 * @param loVertBound the low vertical boundary (top)
	 * @param hiVertBound the high vertical boundary (bottom)
	 * @param loSideBound the low horizontal boundary (left side)
	 * @param hiSideBound the high horizontal boundary (right side)
	 */
	private void makeMaze(int loVertBound, int hiVertBound, int loSideBound, int hiSideBound)
	{
		if ((hiVertBound-loVertBound)>=1 && (hiSideBound-loSideBound)>=1)
		{
			boolean up; //if true horizontal wall will be drawn along top of chamber
			boolean left; //if true vertical wall will be drawn along left side of chamber
			int xPos = random.nextInt((hiSideBound+1)-loSideBound)+loSideBound; //xPos = rand b/w low and high horizontal bounds
			int yPos = random.nextInt((hiVertBound+1)-loVertBound)+loVertBound; //yPos = rand b/w low and high vertical bounds
			if (xPos == loSideBound) left = false; //if x coordinate of point is along left bound, must draw wall along right side of chamber 
			else if (xPos == hiSideBound) left = true; //opposite for right bound
			else left = random.nextBoolean();
			if (yPos == loVertBound) up = false;//if y coordinate of point is along top bound, must draw wall along bottom of chamber
			else if (yPos == hiVertBound) up = true;//opposite for bottom bound
			else up = random.nextBoolean();
			for (int x = loSideBound; x<=hiSideBound; x++) //draw horizontal wall through point
			{	
				if (up)
				{
					maze[yPos][x].nWall = true;
					maze[yPos-1][x].sWall = true;
				}
				else 
				{
					maze[yPos][x].sWall = true;
					maze[yPos+1][x].nWall = true;
				}
			}
			for (int y = loVertBound; y<=hiVertBound; y++) //draw vertical wall through point
			{
				if (left) 
				{
					maze[y][xPos].wWall = true;
					maze[y][xPos-1].eWall = true;
				}
				else 
				{
					maze[y][xPos].eWall = true;
					maze[y][xPos+1].wWall = true;
				}
			}
			//make openings in random three of the four walls
			int[] wallsToOpen = new int[]{0,1,2,3}; //0 = north, 1 = east, 2 = south, 3 = west
			int notThisWall = random.nextInt(4);
			wallsToOpen[notThisWall] = -1;
			int loHoleRange; //inclusive
			int hiHoleRange; //inclusive
			int holeIndex = 0;
			for (int i = 0; i<4; i++)
			{
				if (wallsToOpen[i]>=0)
				{
					if (wallsToOpen[i] == 0 || wallsToOpen[i] == 2) //north or south walls
					{	
						//set valid range (y value) in which to create opening
						if (wallsToOpen[i] == 0) //north wall
						{
							loHoleRange = loVertBound; 
							if (up) hiHoleRange = yPos-1;
							else hiHoleRange = yPos;
						}
						else  //south wall
						{
							hiHoleRange = hiVertBound;
							if (up) loHoleRange = yPos;
							else loHoleRange = yPos+1;
						}
						holeIndex = random.nextInt((hiHoleRange+1) - loHoleRange)+loHoleRange;
						//create opening
						if (left)
						{
							maze[holeIndex][xPos].wWall = false;
							maze[holeIndex][xPos-1].eWall = false;
						}
						else 
						{
							maze[holeIndex][xPos].eWall = false;
							maze[holeIndex][xPos+1].wWall = false;
						}
					}
					else //east or west walls
					{
						//set valid range for opening
						if (wallsToOpen[i] == 1) //east wall
						{
							hiHoleRange = hiSideBound;
							if (left) loHoleRange = xPos;
							else loHoleRange = xPos+1;
						}
						else //west wall
						{	
							loHoleRange = loSideBound;
							if (left) hiHoleRange = xPos-1;
							else hiHoleRange = xPos;	
						}	
						holeIndex = random.nextInt((hiHoleRange+1)-loHoleRange)+loHoleRange;
						//make opening
						if (up) 
						{
							maze[yPos][holeIndex].nWall = false;
							maze[yPos-1][holeIndex].sWall = false;
						}
						else 
						{	
							maze[yPos][holeIndex].sWall = false;
							maze[yPos+1][holeIndex].nWall = false;
						}
					}
				}
			}
			if (left)
				xPos--;
			if (up)
				yPos--;
			//make smaller maze in each section created by new walls 
			makeMaze(loVertBound,yPos,loSideBound,xPos);
			makeMaze(loVertBound,yPos,xPos+1,hiSideBound);
			makeMaze(yPos+1,hiVertBound,loSideBound,xPos);
			makeMaze(yPos+1,hiVertBound,xPos+1,hiSideBound);
		}
	}
	/**
	 * getWidth
	 * @return the width of this maze
	 */
	public int getWidth()
	{
		return width;
	}
	
	/**
	 * getHeight
	 * @return the height of this maze
	 */
	public int getHeight()
	{
		return height;
	}
	
	/**
	 * isNorthWall
	 * @param row the row identifier of a chamber
	 * @param column the column identifier of a chamber
	 * @return true if the chamber at row row and column column
	 * contain a north wall. Otherwise, return false
	 */
	public boolean isNorthWall(int row, int column)
	{
		return maze[row][column].nWall;
	}
	
	/**
	 * isEastWall
	 * @param row the row identifier of a chamber
	 * @param column the column identifier of a chamber
	 * @return true if the chamber at row row and column column
	 * contain an east wall. Otherwise, return false
	 */
	public boolean isEastWall(int row, int column)
	{
		return maze[row][column].eWall;
	}
	
	/**
	 * isSouthWall
	 * @param row the row identifier of a chamber
	 * @param column the column identifier of a chamber
	 * @return true if the chamber at row row and column column
	 * contain a south wall. Otherwise, return false
	 */
	public boolean isSouthWall(int row, int column)
	{
		return maze[row][column].sWall;
	}
	
	/**
	 * isWestWall
	 * @param row the row identifier of a chamber
	 * @param column the column identifier of a chamber
	 * @return true if the chamber at row row and column column
	 * contain a west wall. Otherwise, return false
	 */
	public boolean isWestWall(int row, int column)
	{
		return maze[row][column].wWall;
	}

	private class chamber
	{
		boolean nWall, sWall, eWall, wWall;
		
		private chamber()
		{
			nWall = false;
			sWall = false;
			eWall = false;
			wWall = false;
		}
	}
}













































