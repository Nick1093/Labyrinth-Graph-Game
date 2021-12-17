import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Solver {

    // graph of the labyrinth
    private Graph graph;

    // entrance and exit nodes of the graph
    private int ent, ex;

    // list of bombs
    private int blastBombs;
    private int meltBombs;
    private int n; // total number of nodes 
//    private int width, length;

    // create an arraylist of node called path
    private ArrayList<Node> path;

    // hashmap to store bombs used
    private HashMap<Integer, ArrayList<Integer>> bombsUsed;
    

    Solver(String inputFile) throws LabyrinthException {
        int name = 0;
        try { 
            // read in all the input lines
			BufferedReader input = new BufferedReader(new FileReader(inputFile));
			int scale = Integer.parseInt(input.readLine());

            // get the length and width
			int w = Integer.parseInt(input.readLine());
			int l = Integer.parseInt(input.readLine());

            // store the number of bombs per type allowed
            blastBombs = Integer.parseInt(input.readLine());
            meltBombs = Integer.parseInt(input.readLine());

            // create a new graph
            n = w * l;
//            width = w;
//           length = l;
			graph = new Graph(w * l); 

            this.path = new ArrayList<>();

            // line for reading
			String line;
            
			while ((line = input.readLine()) != null) {
				for (int i = 0; i < line.length(); i++) {
					char element = line.charAt(i); // element is character at line position

                    // if entrance
                    if (element == 'e') {
                        ent = name;
                        name++;
                    }
                    // if exit
                    else if (element == 'x') {
                        ex = name;
                        name++;
                    }
                    // if room
                    else if (element == 'o') {
                        name++;
                    }
                    // horizontal
                    // if corridor
                    else if (element == '-') {
                        graph.insertEdge(graph.getNode(name-1), graph.getNode(name), 1);
                    }
                    // if brick wall
                    else if (element == 'b') {
                        graph.insertEdge(graph.getNode(name-1), graph.getNode(name), 2);
                    }
                    // if rock wall
                    else if (element == 'r') {
                        graph.insertEdge(graph.getNode(name-1), graph.getNode(name), 3);
                    }
                    // if metal wall
                    else if (element == 'm') {
                        graph.insertEdge(graph.getNode(name-1), graph.getNode(name), 4);
                    }
                    // vertical
                    else if (element == '|') {
                        graph.insertEdge(graph.getNode(name-w+((i+1)/2)), graph.getNode(name+((i+1)/2)), 1);
                    }
                    // if brick wall
                    else if (element == 'B') {
                        graph.insertEdge(graph.getNode(name-w+((i+1)/2)), graph.getNode(name+((i+1)/2)), 2);
                    }
                    // if rock wall
                    else if (element == 'R') {
                        graph.insertEdge(graph.getNode(name-w+((i+1)/2)), graph.getNode(name+((i+1)/2)), 3);
                    }
                    // if metal wall
                    else if (element == 'M') {
                        graph.insertEdge(graph.getNode(name-w+((i+1)/2)), graph.getNode(name+((i+1)/2)), 4);
                    }
                }
            }
            input.close();
/*
            for (int i =0; i < w*l; i++){
                System.out.println(this.getGraph().getNode(i));
                ArrayList<Edge> list = this.getGraph().incidentEdges(this.getGraph().getNode(i));
                for (int j = 0; j < list.size();j++){
                    
                }
*/

		} 
        catch (Exception e) {
			throw new LabyrinthException("Labyrinth creation failed!");
		}
    }

    // get the graph
    public Graph getGraph() throws LabyrinthException {
        if (graph == null) {
            throw new LabyrinthException("Graph is non existant.");
        }
        else {
            return graph;
        }
    }

    // helper function to help choose which way to go
    private Node chooseFirstOrSecondEndPoint(Node currentNode, Edge edge) {
    	// get the left value 
        int leftValue = edge.firstEndpoint().getName();

        // if the left value is equal to our current node, then we go to the second endpoint of the edge
        if (leftValue == currentNode.getName())
            return edge.secondEndpoint;
        else
        	// else we go to the first endpoint
            return edge.firstEndpoint();
    }

    // helper method to add to the array list
    private void addToArrayList(ArrayList<Node> list, Node n){
        boolean isFound = false;
        
        // iterate through the list, if found then we dont need to add
        for (int i =0; i <list.size();i++){
            if (list.get(i).equals(n))
                isFound = true;
        }
        // else add
        if (isFound == false)
            list.add(n);

    }
    
    // helper method to see if the node is in the stack
    private boolean isInStack(Stack<Node> stack, Node n) {
    	Iterator solution = stack.iterator();
    	
    	// iterate through if found return true else return false
		while (solution.hasNext()) {
            Node v = (Node)solution.next();
            if (v.equals(n))
            	return true;
	    }
    	
    	return false;
    }
    
    // collect all adjacentNodes (with clear path) that are not yet traveled to
    private ArrayList<Node> adjacentNodes(Node currentNode) throws GraphException, LabyrinthException{
    	ArrayList<Node> list = new ArrayList<Node>();
    	
    	// iterate through the size 
    	for( int i = 0; i < n; i++) {
    		Node temp = this.getGraph().getNode(i);
    		//System.out.println("here " + temp);
    		
    		// if the node and the current node are adjacent but our temp node has not been traversed go next
    		if (this.getGraph().areAdjacent(temp, currentNode) && !temp.getMark()) {
    			// if the edge is a corridor add it
    			if (this.getGraph().getEdge(temp, currentNode).getType() == 1)
    				list.add(temp);
    		}
    	}
    	// return the list
    	return list;
    }
    
    // this private method to check the path from one room to another room if we can travel to without using any bomb
    // if we can get to room 10 from room 5 without using any bomb, then don't spend any bomb for it
    private boolean isReachableWithoutBombSpent(Node start, Node end) throws GraphException, LabyrinthException {
    	//copy the array of Nodes for before and after
    	//printGraphTravel();
    	Boolean [] nodes = new Boolean[n];
    	for (int i = 0; i < n; i++)
    		nodes[i] = this.getGraph().getNode(i).getMark();
    	
    	Stack<Node> startStack = new Stack<Node>();
    	startStack.push(start);
    	
    	while(!startStack.isEmpty()) {
    		
    		Node currentNode = startStack.pop();
    		currentNode.setMark(true);
    		//System.out.println("Pop " + currentNode + " stack size = " + startStack.size());
    		
    		// put all adjacent nodes into the stack
    		ArrayList<Node> list = this.adjacentNodes(currentNode);
    		for (int i = 0; i < list.size(); i++) {
    			//System.out.println("* " + list.get(i));
    			if (!isInStack(startStack, list.get(i)))
    				startStack.push(list.get(i));
    		}
    		
    		Iterator solution = startStack.iterator();
    		while (solution.hasNext()) {
                Node v = (Node)solution.next();
                //System.out.print(v + ", ");
                
                // if we reached the end then 
                if (v.equals(end)) {
                	resetTravesalGraph(nodes);
                	return true;
                }
		    }
    	}	
    	resetTravesalGraph(nodes);

    	return false;
    }
    
    // helper to reset the graph from the traversal
    private void resetTravesalGraph(Boolean[] nodes) throws GraphException, LabyrinthException {
    	//System.out.println("---- reset graph traversal");
    	for (int i = 0; i < n; i++) {
    		
    		// marking with our list of marks
    		this.getGraph().getNode(i).setMark(nodes[i]);
    	}
    }
    
//    private void printGraphTravel() throws GraphException {
//    	int counter = 0;
//    	for (int i = 0; i < length; i++) {
//    		for (int j = 0; j < width; j++) {
//    			System.out.print(graph.getNode(counter).getMark() +" ");
//    			counter++;
//    		}
//    		System.out.println();
//    	}
//    	
//    }
    
    //
    public Iterator solve() {
    	ArrayList<Node> pathList = new ArrayList<Node>();
    	
    	// we try the following 
    	try {
	        // put the entry node to the pathList solution, it always start from there 
	        Node entrance = this.getGraph().getNode(ent);
	        pathList.add(entrance);
	        entrance.setMark(true);
	        
	        //get all the edges connected to the node, put them into the Path
	        ArrayList<Edge> temp = this.getGraph().incidentEdges(entrance);
	        for (int i = 0; i < temp.size(); i++){
	        	if (temp.get(i).getType()==1) {
	        		this.addToArrayList(this.path, chooseFirstOrSecondEndPoint(entrance, temp.get(i)));
	        	}
	        }
	
	        Node previous = entrance;
	        int countStep = 0;
	        
	        while(true) {
	            	        	
	        	Node currentStep = this.path.remove(this.path.size()-1);
	            currentStep.setMark(true);
	            
	            //check if this currentStep room is actually an adjacent room to the previous room in the ArrayList
	            //only consider it as a Room on the path if it is adjacent. Otherwise, pick the one before that
	            if (!this.getGraph().areAdjacent(currentStep, previous)) {
		        	int pathSize = this.path.size();
		            for (int i = pathSize-1; i >= 0; i--) {
		        		if (this.getGraph().areAdjacent(previous, this.path.get(i))) {
		        			currentStep = this.path.get(i);
		        			
		        			// mark the current node
		        			currentStep.setMark(true);
		        			
		        			break;
		        		}
		            }
	        	}
	
	            ArrayList<Edge> tempEdges = this.graph.incidentEdges(currentStep);
	            for (int i = 0; i < tempEdges.size(); i++ ){
	                Edge e = tempEdges.get(i);
	            
	                Node n = chooseFirstOrSecondEndPoint(currentStep, e);
	                if (isPassable(e) && !n.getMark()){
	                	// before adding, invoke function isReachableWithoutBombSpent - this is to pass test 2
	                	// if the person can get there without having to use up a bomb, then don't use the bomb
	                	// thus, don't add this room to path (save bomb)
	                	if (e.getType() == 1) {
	                		addToArrayList(this.path, n);
	                	}
	                	else if (e.getType() != 1) {
	                		if (!isReachableWithoutBombSpent(currentStep, n)) {
	                			addToArrayList(this.path, n);
	                		}
	                	}
	                }
	            }
	            
	            //check for exit at every current Room, if found, just return and exit
	            pathList.add(currentStep);
	            if (currentStep.equals(this.getGraph().getNode(ex))) {
	                return pathList.iterator();
	            }
	            
	            if (this.graph.areAdjacent(currentStep, this.path.get(path.size() - 1))){
		            if (this.graph.getEdge(currentStep, this.path.get(path.size() - 1)).getType() == 2) {
		            	//this.blastBombs--; // use 1 bomb, this next step (last element of this.path) is going through wall
		            }
		            else if (this.graph.getEdge(currentStep, this.path.get(path.size() - 1)).getType() == 3) {
		            	//this.blastBombs = this.blastBombs - 2; // use 2 bomb, this next step (last element of this.path) is going through wall
		            }
		            else if (this.graph.getEdge(currentStep, this.path.get(path.size() - 1)).getType() == 4) {
		            	//this.meltBombs--; // use melt bomb, this next step (last element of this.path) is going through wall
		            }
	            }
	            
	            
	            previous = currentStep;
	            //countStep++;
	            // if (countStep == 10)
	            //	break;
	        }
    	} catch (LabyrinthException e) {
    		return null;
    	} catch (GraphException e) {
    		return null;
    	} catch (Exception e) {
    		return null;
    	}       
    }

    private boolean isPassable(Edge edge) {
        // if corridor just go through
        if (edge.getType() == 1) {
            return true;
        }
        // a brick wall requires a blastBomb
        else if (edge.getType() == 2) {
            //blastBombs--;
            if (blastBombs < 1) {
                return false;
            }
            else {
                return true;
            }
        }
        // a rock wall requires 2 blastBombs
        else if (edge.getType() == 3) {
           // blastBombs -= 2;
            if (blastBombs < 3) {
                return false;
            }
            else {
                return true;
            }
        }
        // a metal wall requires a meltBomb
        else if (edge.getType() == 4) {
           // meltBombs--;
            if (meltBombs < 1) {
                return false;
            }
            else {
                return true;
            }
        }

        return false;
    }

//
//    public static void main (String[] args) throws LabyrinthException, GraphException, InterruptedException{
//        Node u, v;
//        Solver program = new Solver("lab2");
//        DrawLab display = new DrawLab("lab2");
//
//        Iterator solution = program.solve();
//
//        if (solution != null) {
//		    if (solution.hasNext()) u = (Node)solution.next();
//		    else return;
//		    while (solution.hasNext()) {
//                v = (Node)solution.next();
//                Thread.sleep(500);
//                display.drawEdge(u,v);
//                u = v;
//		    }
//		}
//		else {
//		    System.out.println("No solution was found");
//		    System.out.println("");
//		}
//
//
//    }
}
