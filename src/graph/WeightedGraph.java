package graph;
// This class represents a directed weighted graph


import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Queue;

/**
 * <P>This class represents a general "directed graph", which could 
 * be used for any purpose.  The graph is viewed as a collection 
 * of vertices, which are sometimes connected by weighted, directed
 * edges.</P> 
 * 
 * <P>This graph will never store duplicate vertices.</P>
 * 
 * <P>The weights will always be non-negative integers.</P>
 * 
 * <P>The WeightedGraph will be capable of performing three algorithms:
 * Depth-First-Search, Breadth-First-Search, and Djikatra's.</P>
 * 
 * <P>The Weighted Graph will maintain a collection of 
 * "GraphAlgorithmObservers", which will be notified during the
 * performance of the graph algorithms to update the observers
 * on how the algorithms are progressing.</P>
 */
public class WeightedGraph<V> {

	/* STUDENTS:  You decide what data structure(s) to use to
	 * implement this class.
	 * 
	 * You may use any data structures you like, and any Java 
	 * collections that we learned about this semester.  Remember 
	 * that you are implementing a weighted, directed graph.
	 */


	public Map<V,Map<V,Integer>> directedWeightedGraph;

	/* Collection of observers.  Be sure to initialize this list
	 * in the constructor.  The method "addObserver" will be
	 * called to populate this collection.  Your graph algorithms 
	 * (DFS, BFS, and Dijkstra) will notify these observers to let 
	 * them know how the algorithms are progressing. 
	 */
	private Collection<GraphAlgorithmObserver<V>> observerList;


	/** Initialize the data structures to "empty", including
	 * the collection of GraphAlgorithmObservers (observerList).
	 */


	/* Constructor */
	public WeightedGraph() {
		this.directedWeightedGraph = new HashMap<>();
		this.observerList = new LinkedList<>();
	}

	/** Add a GraphAlgorithmObserver to the collection maintained
	 * by this graph (observerList).
	 * 
	 * @param observer
	 */
	//This is a void method takes a GraphAlgorithmObserver observer as parameter and 
	// add it to the observerList.
	public void addObserver(GraphAlgorithmObserver<V> observer) {
		this.observerList.add(observer);
	}

	/** Add a vertex to the graph.  If the vertex is already in the
	 * graph, throw an IllegalArgumentException.
	 * 
	 * @param vertex vertex to be added to the graph
	 * @throws IllegalArgumentException if the vertex is already in
	 * the graph
	 */
	//This is a void method that takes a vertex as parameter and add this vertex to the graph
	// If this vertex already exist in the graph, then just throw IllegalArgument Exception
	public void addVertex(V vertex) {
		if(containsVertex(vertex)) {// If the weighted graph already contains the vertex
			throw new IllegalArgumentException(); //throw IllegalArgument Exception
		}
		directedWeightedGraph.put(vertex, null);
	}

	/** Searches for a given vertex.
	 * 
	 * @param vertex the vertex we are looking for
	 * @return true if the vertex is in the graph, false otherwise.
	 */
	// This is a method whose return type is boolean, 
	// to check whether a given vertex is in the weighted graph
	public boolean containsVertex(V vertex) {
		for(V v :directedWeightedGraph.keySet()) {//Iterate through the keySet of the map
			if(v.equals(vertex)) {//If the key equals vertex
				return true;
			}
		}
		return false;
	}

	/** 
	 * <P>Add an edge from one vertex of the graph to another, with
	 * the weight specified.</P>
	 * 
	 * <P>The two vertices must already be present in the graph.</P>
	 * 
	 * <P>This method throws an IllegalArgumentExeption in three
	 * cases:</P>
	 * <P>1. The "from" vertex is not already in the graph.</P>
	 * <P>2. The "to" vertex is not already in the graph.</P>
	 * <P>3. The weight is less than 0.</P>
	 * 
	 * @param from the vertex the edge leads from
	 * @param to the vertex the edge leads to
	 * @param weight the (non-negative) weight of this edge
	 * @throws IllegalArgumentException when either vertex
	 * is not in the graph, or the weight is negative.
	 */
	// This is void method which will add edge between two edges currently exist in the graph.
	// It will throw exception when either of the vertex is not in the graph or the weight is negative
	public void addEdge(V from, V to, Integer weight) {
		if(containsVertex(from) && containsVertex(to) && weight >= 0) {
			//If the graph contains both vertex we want to build the edge between and the weight is nonnegative
			Map<V, Integer> value = directedWeightedGraph.get(from); //Get the map the from vertex being mapped to
			if(value == null) {
				// If the from vertex has nothing to mapped to
				value = new HashMap<>(); // Initialize a new HashMap for the value
			}
			value.put(to, weight);// put the to vertex and weight to the value map
			directedWeightedGraph.put(from, value); // put the from vertex and the map to the weighted graph
		}else {
			throw new IllegalArgumentException();
		}

	}


	/** 
	 * <P>Returns weight of the edge connecting one vertex
	 * to another.  Returns null if the edge does not
	 * exist.</P>
	 * 
	 * <P>Throws an IllegalArgumentException if either
	 * of the vertices specified are not in the graph.</P>
	 * 
	 * @param from vertex where edge begins
	 * @param to vertex where edge terminates
	 * @return weight of the edge, or null if there is
	 * no edge connecting these vertices
	 * @throws IllegalArgumentException if either of
	 * the vertices specified are not in the graph.
	 */
	// This method will return an Integer represents the weight between the from and to vertices
	// It will throw IllegalArgument Exception if either vertex is not in the graph
	public Integer getWeight(V from, V to) {
		if(containsVertex(from) && containsVertex(to)) {// If the graph contains both from and to vertices
			if(directedWeightedGraph.get(from) == null){// If the from vertex's corresponding map is null
				return null; 
			}
			if(directedWeightedGraph.get(from).keySet().contains(to)) {// If the to vertex is in the keySet of the from vertex's corresponding map
				return directedWeightedGraph.get(from).get(to); // return the weight
			}else { // If the to vertex is not in the keySet of the from vertex's corresponding map
				return null;
			}
		}
		throw new IllegalArgumentException();
	}


	/** 
	 * <P>This method will perform a Breadth-First-Search on the graph.
	 * The search will begin at the "start" vertex and conclude once
	 * the "end" vertex has been reached.</P>
	 * 
	 * <P>Before the search begins, this method will go through the
	 * collection of Observers, calling notifyBFSHasBegun on each
	 * one.</P>
	 * 
	 * <P>Just after a particular vertex is visited, this method will
	 * go through the collection of observers calling notifyVisit
	 * on each one (passing in the vertex being visited as the
	 * argument.)</P>
	 * 
	 * <P>After the "end" vertex has been visited, this method will
	 * go through the collection of observers calling 
	 * notifySearchIsOver on each one, after which the method 
	 * should terminate immediately, without processing further 
	 * vertices.</P> 
	 * 
	 * @param start vertex where search begins
	 * @param end the algorithm terminates just after this vertex
	 * is visited
	 */
	//This is a void method that does a Breadth-First Search on the graph from a 
	// start vertex and stop once the end vertex being visited.
	public void DoBFS(V start, V end) {
		for(GraphAlgorithmObserver<V> observer : this.observerList) {
			observer.notifyBFSHasBegun();//Before the traversal starts,
			//notify each observer in the observerList that the breadth-first traversal has started
		}

		Set<V> visitedSet = new HashSet<>(); // Initialize a visitedSet using HashSet
		Queue<V> discoveredQueue = new LinkedList<>(); // Initialize a discoveredQueue using LinkedList
		discoveredQueue.add(start); // add the start vertex to the discoveredQueue
		// While the discoveredQueue is not empty
		while(!(discoveredQueue.isEmpty())) {
			V currVertex = discoveredQueue.remove(); 
			//remove a vertex that we will process from one end of the discoveredQueue

			if(!(visitedSet.contains(currVertex))){ // If we have not visited the currVertex yet

				visitedSet.add(currVertex); // Add the currVertex to the visitedSet


				for(GraphAlgorithmObserver<V> observer : this.observerList) {
					observer.notifyVisit(currVertex); // notify each observer in the observerList that we are visiting the currVertex
				}
				if(currVertex.equals(end)) { //If the currVertex is the end vertex
					for(GraphAlgorithmObserver<V> observer : this.observerList) {
						observer.notifySearchIsOver(); // notify each observer in the observerList that we have found the vertex
					}
					return; // terminate the search by returning from the method 
				}
				//If the currVertex is not the end vertex
				if(this.directedWeightedGraph.get(currVertex) != null) { // If the currVertex has some neighbors
					for(V successor: this.directedWeightedGraph.get(currVertex).keySet()) {
						if(!(visitedSet.contains(successor))) { //If we have not visited these successors before
							discoveredQueue.add(successor); // add the newly discovered successor to the discoveredQueue
						}
					}
				}
			}
		}

	}

	/** 
	 * <P>This method will perform a Depth-First-Search on the graph.
	 * The search will begin at the "start" vertex and conclude once
	 * the "end" vertex has been reached.</P>
	 * 
	 * <P>Before the search begins, this method will go through the
	 * collection of Observers, calling notifyDFSHasBegun on each
	 * one.</P>
	 * 
	 * <P>Just after a particular vertex is visited, this method will
	 * go through the collection of observers calling notifyVisit
	 * on each one (passing in the vertex being visited as the
	 * argument.)</P>
	 * 
	 * <P>After the "end" vertex has been visited, this method will
	 * go through the collection of observers calling 
	 * notifySearchIsOver on each one, after which the method 
	 * should terminate immediately, without visiting further 
	 * vertices.</P> 
	 * 
	 * @param start vertex where search begins
	 * @param end the algorithm terminates just after this vertex
	 * is visited
	 */
	//This is a void method that does a Depth-First Search on the graph from a 
	// start vertex and stop once the end vertex being visited.
	public void DoDFS(V start, V end) {
		for(GraphAlgorithmObserver<V> observer : this.observerList) {
			observer.notifyDFSHasBegun();//Before the traversal starts,
			//notify each observer in the observerList that the depth-first traversal has started
		}

		Stack<V> discoveredStack = new Stack<V>(); // Initialize a discoveredStack using Stack
		Set<V> visitedSet = new HashSet<V>();// Initialize a visitedSet using HashSet
		discoveredStack.push(start); // push the start vertex to one end of the discoveredStack

		// While the discoveredStack is not empty
		while(!discoveredStack.empty()) {
			V currVertex = discoveredStack.pop(); //pop a vertex from the same end of the discoveredStack
			if(! visitedSet.contains(currVertex)) { // If the visitedSet does not contain the currVertex 
				visitedSet.add(currVertex); // add it to the visitedSet


				for(GraphAlgorithmObserver<V> observer : this.observerList) {
					observer.notifyVisit(currVertex);// notify each observer in the observerList that we are visiting the currVertex
				}
				if(currVertex.equals(end)) {//If the currVertex is the end vertex
					for(GraphAlgorithmObserver<V> observer : this.observerList) {
						observer.notifySearchIsOver();// notify each observer in the observerList that we have found the vertex
					}
					return; // terminate the search by returning from the method 
				}

				//If the currVertex is not the end vertex
				if(this.directedWeightedGraph.get(currVertex) != null) { // If the currVertex has some neighbors
					for(V successor: this.directedWeightedGraph.get(currVertex).keySet()) {
						if(!(visitedSet.contains(successor))) {//If we have not visited these successors before
							discoveredStack.push(successor); // push the newly discovered successor to the discoveredStack
						}
					}
				}

			}
		}


	}



	/** 
	 * <P>Perform Dijkstra's algorithm, beginning at the "start"
	 * vertex.</P>
	 * 
	 * <P>The algorithm DOES NOT terminate when the "end" vertex
	 * is reached.  It will continue until EVERY vertex in the
	 * graph has been added to the finished set.</P>
	 * 
	 * <P>Before the algorithm begins, this method goes through 
	 * the collection of Observers, calling notifyDijkstraHasBegun 
	 * on each Observer.</P>
	 * 
	 * <P>Each time a vertex is added to the "finished set", this 
	 * method goes through the collection of Observers, calling 
	 * notifyDijkstraVertexFinished on each one (passing the vertex
	 * that was just added to the finished set as the first argument,
	 * and the optimal "cost" of the path leading to that vertex as
	 * the second argument.)</P>
	 * 
	 * <P>After all of the vertices have been added to the finished
	 * set, the algorithm will calculate the "least cost" path
	 * of vertices leading from the starting vertex to the ending
	 * vertex.  Next, it will go through the collection 
	 * of observers, calling notifyDijkstraIsOver on each one, 
	 * passing in as the argument the "lowest cost" sequence of 
	 * vertices that leads from start to end (I.e. the first vertex
	 * in the list will be the "start" vertex, and the last vertex
	 * in the list will be the "end" vertex.)</P>
	 * 
	 * @param start vertex where algorithm will start
	 * @param end special vertex used as the end of the path 
	 * reported to observers via the notifyDijkstraIsOver method.
	 */
	// This void method will perform a Dijsktra's algorithm on this graph 
	// and will calculate the lowest cost path from the start vertex to the end vertex
	public void DoDijsktra(V start, V end) {
		for(GraphAlgorithmObserver<V> observer : this.observerList) {
			observer.notifyDijkstraHasBegun();//Before the traversal starts,
			//notify each observer in the observerList that the Dijsktra's algorithm traversal has started
		}
		int counter = 0; //this counter will keep track of how many vertices has been finished calculated the lowest path.
		Map<V,Integer> totalCost = new HashMap<>(); // This hashMap will map from the current vertex to how many steps it take from the predecessor to current vertex
		Set<V> finishedSet = new HashSet<>(); // This hashSet will collect the vertex that is visited
		Map<V,V> predecessor = new HashMap<>(); // from end node map to predecessor


		totalCost.put(start, 0); // add the start vertex to the totalCost map
		predecessor.put(start,start); // add the start vertex and itself to the predecessor map
		
		// This for loop initializes all the lowest cost to infinity and predecessor to null
		for(V vertex : directedWeightedGraph.keySet()) {// go through the graph's vertex in the keySet
			if(!(vertex.equals(start))) {// if the current vertex is not the start vertex
				totalCost.put(vertex, Integer.MAX_VALUE); 
				predecessor.put(vertex, null);
			}
		}

		// This minCost variable will keep track of the current minimum cost in the totalCost map
	    // and will be updated to the smallest value that has not been finished
		Integer minCost = Integer.MAX_VALUE; 
		// This currSmallest will be the vertex who has not been finished processed  with the smallest cost
		V currSmallest = start;

		while(counter < directedWeightedGraph.size()) { // while we have not calculated every vertex's cost
			minCost = Integer.MAX_VALUE; // first each time at the start of the loop reassign the minCost to the maximum value
			// This for loop will find the smallest cost of the vertex that has not been added to the finished set
			for(V vertex : totalCost.keySet()) {
				if(!(finishedSet.contains(vertex)) && totalCost.get(vertex) < minCost) {
					// If we have not added the current vertex to the finished set and the cost is less than minCost
					//Do an update
					currSmallest = vertex;
					minCost = totalCost.get(vertex);
				}
			}
			finishedSet.add(currSmallest); // add the currSmallest vertex to the finished set
			counter++; // increment the counter that we have found a new vertex to process

			for(GraphAlgorithmObserver<V> observer : this.observerList) {
				observer.notifyDijkstraVertexFinished(currSmallest, minCost);
				//notify each observer in the observerList that we have added a vertex to the finished and also it's corresponding optimal cost
			}
			// process currSmallest's predecessors
			if(directedWeightedGraph.get(currSmallest) != null) {
				for(V successor : directedWeightedGraph.get(currSmallest).keySet()) { // for all the predecessors of the currSmallest
					if(!(finishedSet.contains(successor))) { // if the predecessor is not in the finished set
						//if the minimum cost to the currSmallest plus the cost from the currSmallest to this current successor is less than the total cost in the totalCost map
						if(minCost + directedWeightedGraph.get(currSmallest).get(successor) < totalCost.get(successor)) {
							//put the new cost and this successor to the totalCost map
							totalCost.put(successor, minCost + directedWeightedGraph.get(currSmallest).get(successor));
							// put the current successor and currSmalles as the predecessor to the predecessor map
							predecessor.put(successor, currSmallest);
						}
					}
				}
			}
		}
		// This linked list  will give the lowest path from start to the end
		LinkedList<V> lowestPath = new LinkedList<>();
		lowestPath.addFirst(end); // add the end vertex to the path

		V preVertex = predecessor.get(end); // find the predecessor of the end vertex


		while(!(preVertex.equals(start)) ) { // while the predecessor is not the start vertex

			lowestPath.addFirst(preVertex); // add the previous vertex to the path(head of the linked list)
			preVertex = predecessor.get(preVertex); // update the preVertex to the predecessor of the vertex we just added to the linkedList

		}

		lowestPath.addFirst(start); // add the start vertex to the front of the path
		


		for(GraphAlgorithmObserver<V> observer : this.observerList) {
			observer.notifyDijkstraIsOver(lowestPath);
			//notify each observer in the observerList that Dijkstra's algorithm is over and give them the lowest path we just found
		}


	}
}
