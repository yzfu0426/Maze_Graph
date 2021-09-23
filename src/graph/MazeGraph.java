package graph;
// This class represents a graph to which we convert a maze.

import graph.WeightedGraph;
import maze.Juncture;
import maze.Maze;

/** 
 * <P>The MazeGraph is an extension of WeightedGraph.  
 * The constructor converts a Maze into a graph.</P>
 */
public class MazeGraph extends WeightedGraph<Juncture> {

	/* STUDENTS:  SEE THE PROJECT DESCRIPTION FOR A MUCH
	 * MORE DETAILED EXPLANATION ABOUT HOW TO WRITE
	 * THIS CONSTRUCTOR
	 */
	

	/** 
	 * <P>Construct the MazeGraph using the "maze" contained
	 * in the parameter to specify the vertices (Junctures)
	 * and weighted edges.</P>
	 * 
	 * <P>The Maze is a rectangular grid of "junctures", each
	 * defined by its X and Y coordinates, using the usual
	 * convention of (0, 0) being the upper left corner.</P>
	 * 
	 * <P>Each juncture in the maze should be added as a
	 * vertex to this graph.</P>
	 * 
	 * <P>For every pair of adjacent junctures (A and B) which
	 * are not blocked by a wall, two edges should be added:  
	 * One from A to B, and another from B to A.  The weight
	 * to be used for these edges is provided by the Maze. 
	 * (The Maze methods getMazeWidth and getMazeHeight can
	 * be used to determine the number of Junctures in the
	 * maze. The Maze methods called "isWallAbove", "isWallToRight",
	 * etc. can be used to detect whether or not there
	 * is a wall between any two adjacent junctures.  The 
	 * Maze methods called "getWeightAbove", "getWeightToRight",
	 * etc. should be used to obtain the weights.)</P>
	 * 
	 * @param maze to be used as the source of information for
	 * adding vertices and edges to this MazeGraph.
	 */
	public MazeGraph(Maze maze) {
		for(int y = 0; y < maze.getMazeHeight(); y++) {
			for(int x = 0; x < maze.getMazeWidth(); x++) {
				Juncture curr = new Juncture(x, y); // construct a new juncture as a vertex in the weighted graph
				//since this class is an extension of weighted graph, call method of the super(WeightedGraph) of this class 
				//to check that whether the weighted graph contains the current juncture
				if(!super.containsVertex(curr)) { 
					super.addVertex(curr); // If not, add this juncture to the weighted graph
				}
				if(x != maze.getMazeWidth() - 1) { // If the x component is not the right most column of the graph
					if(!maze.isWallToRight(curr)) { // If the right of the current juncture is not a wall
						int weight = maze.getWeightToRight(curr); // get the weight to the right of the current juncture
						Juncture rightTocurr = new Juncture(x + 1, y); // construct a juncture that is right to the current juncture
						if(!super.containsVertex(rightTocurr)) { // If the graph does not contain the juncture to the right
							super.addVertex(rightTocurr); // add the right of curr juncture to the graph
						}
						super.addEdge(curr, rightTocurr, weight); // add an edge leading from the current juncture to the juncture to the right 
					}
				}
				if(x != 0) { // If the x component is not the left most column of the graph
					if(!maze.isWallToLeft(curr)) {// If the left of the current juncture is not a wall
						int weight = maze.getWeightToLeft(curr);// get the weight to the left of the current juncture
						Juncture leftTocurr = new Juncture(x - 1, y);// construct a juncture that is left to the current juncture

						if(!super.containsVertex(leftTocurr)) {// If the graph does not contain the juncture to the left
							super.addVertex(leftTocurr);// add the left of curr juncture to the graph
						}
						super.addEdge(curr, leftTocurr, weight);// add an edge leading from the current juncture to the juncture to the left
					}
				}
				if(y != 0) { // If the y component is not the up most row of the graph
					if(!maze.isWallAbove(curr)) {// If the above of the current juncture is not a wall
						int weight = maze.getWeightAbove(curr);// get the weight to the above of the current juncture
						Juncture abovecurr = new Juncture(x, y - 1);// construct a juncture that is above of the current juncture

						if(!super.containsVertex(abovecurr)) {// If the graph does not contain the juncture above the current one
							super.addVertex(abovecurr);// add the above of curr juncture to the graph
						}
						super.addEdge(curr, abovecurr, weight);// add an edge leading from the current juncture to the above juncture 
					}
				}
				if(y != maze.getMazeHeight() - 1) {// If the y component is not the down most row of the graph
					if(!maze.isWallBelow(curr)) {// If the below of the current juncture is not a wall
						int weight = maze.getWeightBelow(curr);// get the weight to the below of the current juncture
						Juncture belowcurr = new Juncture(x, y + 1);// construct a juncture that is below of the current juncture

						if(!super.containsVertex(belowcurr)) {// If the graph does not contain the juncture below the current one
							super.addVertex(belowcurr);// add the below of curr juncture to the graph
						}
						super.addEdge(curr, belowcurr, weight); // add an edge leading from the current juncture to the below juncture 
					}
				}
			}
		}
	}
}

