import java.util.ArrayList;

// this class represents an undirected graph
public class Graph implements GraphADT {

    private int n; // number of nodes
    private Node graph[];
    private Edge edges[][];

    // creates an empty graph with n nodes and no edges
    public Graph(int n) {
        this.n = n;
        graph = new Node[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new Node(i);
        }
        edges = new Edge[n][n];
    }

    /**
     * adds to the graph an edge connecting nodes u and v
     */
    public void insertEdge(Node u, Node v, int type) throws GraphException {
        // check if the nodes exist or not
        if (u.getName() < 0 || u.getName() > this.n-1 || v.getName() < 0 || v.getName() > this.n-1) {
            throw new GraphException("Sorry, node(s) do(es) not exist, cannot insert edge.");
        }

        // check if the edge is already in between 2 nodes
        if (edges[u.getName()][v.getName()] != null) {
            throw new GraphException("Sorry, edge already exists, cannot insert edge.");
        }
        // here we insert the edge 
        else {
            // when inserting an edge, there is  
            Edge edge = new Edge(u,v,type);
            edges[u.getName()][v.getName()] = edge;
            edges[v.getName()][u.getName()] = edge;
        }

    }

    // returns the node with the specified name
    public Node getNode(int u) throws GraphException {
        Node node = this.graph[u];

        if (node == null) {
            throw new GraphException("No node with this name exists in the graph!");
        }
        return node;
    }

    // returns a list storing all the edges incident on node u
    public ArrayList<Edge> incidentEdges(Node u) throws GraphException {
        // first we check if the node exists
        if (edges.length <= u.getName()) {
            throw new GraphException("Node does not exist.");
        }

        // create a list to store the incident edges
        ArrayList<Edge> incidents = new ArrayList<Edge>();
        
        // loop through the graph to see if there is an edge that is an incident
        for (int i = 0; i < edges.length; i++) {
            if (edges[u.getName()][i] != null) {
                incidents.add(edges[u.getName()][i]);
            }
        }

        if (incidents.size() == 0) {
            return null;
        }

        return incidents;
    }

    // returns the edge connecting nodes u and v
    public Edge getEdge(Node u, Node v) throws GraphException {
        // check if the nodes have edges
        if (this.edges[u.getName()][v.getName()] == null) {
            throw new GraphException("Sorry, these nodes don't have edges.");
        }

        // checking if the nodes are on the graph
        if (u.getName() < 0 || u.getName() >= n || v.getName() < 0 || v.getName() >= n) {
            throw new GraphException("Sorry, node does not exist.");
        }

        // if passed, return the edge connecting the nodes
        return this.edges[u.getName()][v.getName()];
    }

    // returns true if and only if nodes u and v are adjacent
/*    public boolean areAdjacent(Node u, Node v) throws GraphException {
        // check if the nodes have exist
        if (edges.length <= u.getName() || edges.length <= v.getName()) {
            throw new GraphException("Node does not exist.");
        }
        // if the nodes are connected by the same edge, then they are adjacent
        if (edges[v.getName()][u.getName()] == edges[u.getName()][v.getName()]) {
            return true;
        }
        else {
            return false;
        }
    }
*/    
    
	public boolean areAdjacent(Node u, Node v) throws GraphException {
		if(u.getName() >= n || v.getName() >= n)
			throw new GraphException("Invalid, can find adjacent");	
		
		return (edges[u.getName()][v.getName()] != null && edges[u.getName()][v.getName()]!= null);

	}

}
