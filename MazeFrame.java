import java.util.ArrayList;
import javax.swing.JFrame;

public class MazeFrame
{
	public static void main(String[] args) throws InterruptedException
	{
		int width, height;
		if (args.length != 2)
		{
			System.out.println("Usage: java MazeFrame <width> <height>");
			return;
		}
		width = Integer.valueOf(args[0]);
		height = Integer.valueOf(args[1]);
		
		JFrame frame = new JFrame();
		Maze maze = new Maze(width, height);
		ArrayList<Pair<Integer,Integer>> solution = new ArrayList<Pair<Integer,Integer>>();
		MazeComponent mc = new MazeComponent(maze, solution);
		frame.setSize(800,800);
		frame.setTitle("Maze");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(mc);
		frame.setVisible(true);
		
		solution.add(new Pair<Integer,Integer>(0,0)); //starting pt.
		Thread.sleep(1000);
		solveMaze(solution, mc, maze, new Integer(0), new Integer(0), 'z');
		mc.repaint();
	}
	
	/** Solve Maze: recursively solve the maze
	 * 
	 * @param solution   : The array list solution is needed so that every recursive call,
	 *                     a new (or more) next position can be added or removed.
	 * @param mc         : This is the MazeComponent. We need that only for the purpose of
	 *                     animation. We need to call mc.repaint() every time a new position
	 *                     is added or removed. For example,
	 *                       :
	 *                     solution.add(...);
	 *                     mc.repaint();
	 *                     Thread.sleep(500);
	 *                       :
	 *                     solution.remove(...);
	 *                     mc.repaint();
	 *                     Thread.sleep(500);
	 *                       :
	 * @param maze       : The maze data structure to be solved. 
	 * @return a boolean value to previous call to tell the previous call whether a solution is
	 *         found.
	 * @throws InterruptedException: We need this because of our Thread.sleep(50);
	 */
	 
	/**
	 *solveMaze
	 *@param solution an ArrayList of points which make up a path through the maze
	 *@param mc MazeComponent used to draw the maze
	 *@param maze the maze
	 *@param currentX current x position in the maze
	 *@param currentY current y position in the maze
	 *@param fromDir the direction from which we moved to get to position currentX,CurrentY
	 */
	public static boolean solveMaze(ArrayList<Pair<Integer,Integer>> solution, MazeComponent mc, Maze maze, Integer currentX, Integer currentY, char fromDir) throws InterruptedException
	{
		if (currentY.intValue() == maze.getHeight()-1 && currentX.intValue() == maze.getWidth()-1) //success!
			return true;
		
		if (fromDir == 'n' && maze.isEastWall(currentY.intValue(), currentX.intValue()) //dead end
			&& maze.isSouthWall(currentY.intValue(),currentX.intValue())
			&& maze.isWestWall(currentY.intValue(),currentX.intValue()))
			return false;
		else if (fromDir == 'e' && maze.isNorthWall(currentY.intValue(), currentX.intValue()) //dead end
			&& maze.isSouthWall(currentY.intValue(),currentX.intValue())
			&& maze.isWestWall(currentY.intValue(),currentX.intValue()))
			return false;
		else if (fromDir == 's' && maze.isEastWall(currentY.intValue(), currentX.intValue()) //dead end
			&& maze.isNorthWall(currentY.intValue(),currentX.intValue())
			&& maze.isWestWall(currentY.intValue(),currentX.intValue()))
			return false;
		else if (fromDir == 'w' && maze.isEastWall(currentY.intValue(), currentX.intValue()) //dead end
			&& maze.isSouthWall(currentY.intValue(),currentX.intValue())
			&& maze.isNorthWall(currentY.intValue(),currentX.intValue()))
			return false;
		
		if (!maze.isNorthWall(currentY.intValue(),currentX.intValue()) && fromDir!='n') //if no north wall, try all solutions from moving north
		{
			solution.add(new Pair<Integer,Integer>(new Integer(currentY.intValue()-1),currentX));
			mc.repaint();
			Thread.sleep(50);
			if (!solveMaze(solution, mc, maze, currentX, new Integer(currentY.intValue()-1), 's'))
			{	
				solution.remove(solution.size()-1);
				mc.repaint();
				Thread.sleep(50);
			}
			else return true;
		}
		if (!maze.isEastWall(currentY.intValue(), currentX.intValue()) && fromDir!='e') //if no east wall, try all solutions from moving east
		{
			solution.add(new Pair<Integer, Integer>(currentY, new Integer(currentX.intValue()+1)));
			mc.repaint();
			Thread.sleep(50);
			if (!solveMaze(solution, mc, maze, new Integer(currentX.intValue()+1), currentY, 'w'))
			{
				solution.remove(solution.size()-1);
				mc.repaint();
				Thread.sleep(50);
			}
			else return true;
		}
		if (!maze.isSouthWall(currentY.intValue(),currentX.intValue()) && fromDir!='s') //if no south wall, try all solutions from moving south
		{
			solution.add(new Pair<Integer, Integer>(new Integer(currentY.intValue()+1), currentX));
			mc.repaint();
			Thread.sleep(50);
			if (!solveMaze(solution, mc, maze, currentX, new Integer(currentY.intValue()+1), 'n'))
			{	
				solution.remove(solution.size()-1);
				mc.repaint();
				Thread.sleep(50);
			}
			else return true;
		}
		if (!maze.isWestWall(currentY.intValue(),currentX.intValue()) && fromDir!='w') //if no west wall, try all solutions from moving west
		{
			solution.add(new Pair<Integer, Integer>(currentY, new Integer(currentX.intValue()-1)));
			mc.repaint();
			Thread.sleep(50);
			if (!solveMaze(solution, mc, maze, new Integer(currentX.intValue()-1), currentY, 'e'))
			{
				solution.remove(solution.size()-1);
				mc.repaint();
				Thread.sleep(50);
			}
			else return true;
		}
		return false;
	}
}