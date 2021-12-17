// this class represents an edge of the graph
public class Edge {
    
    // both nodes are connected by the edge
    Node firstEndpoint, secondEndpoint;

    /**
     * type of an edge can be:
     *      1. Corridor
     *      2. Brick Wall
     *      3. Rock Wall
     *      4. Metal Wall
     */
    private int type;

    // constructor creates an edge of the given type connecting nodes u and v
    public Edge(Node u, Node v, int edgeType) {
        this.firstEndpoint = u;
        this.secondEndpoint = v;
        this.type = edgeType;
    }

    // returns the first endpoint of the edge
    public Node firstEndpoint() {
        return this.firstEndpoint;
    }

    // returns the second endpoint of the edge
    public Node secondEndpoint() {
        return this.secondEndpoint;
    }

    // returns the type of edge
    public int getType() {
        return this.type;
    }

    // sets the type of the edge to the specified value
    public void setType(int newType) {
        this.type = newType;
    }

    // returns true if this Edge object connects the same two nodes as otherEdge
    public boolean equals(Edge otherEdge) {
        if ((this.firstEndpoint.equals(otherEdge.firstEndpoint) && secondEndpoint.equals(otherEdge.secondEndpoint))|| (this.secondEndpoint.equals(otherEdge.firstEndpoint) && firstEndpoint.equals(otherEdge.secondEndpoint))) {
            return true;
        }
        else {
            return false;
        }
    }
}
