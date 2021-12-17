// this class represents a node of the graph
public class Node {
    
    // name of a node is an integer value between 0 and n-1
    private int name = 0;

    // initially the value is false
    private boolean marked = false;

    // creates an unmarked node with the given name
    public Node(int nodeName) {
        this.name = nodeName;
    }

    // marks the node with the specified value
    public void setMark(boolean mark) {
        this.marked = mark;
    }

    // returns the value with which the node has been marked
    public boolean getMark() {
        return this.marked;
    }

    // returns the name of the node
    public int getName() {
        return this.name;
    }

    // returns true if node ahs the same name as "otherNode", false otherwise
    public boolean equals(Node otherNode) {
        if (this.name == otherNode.getName()) {
            return true;
        }
        else {
            return false;
        }
    }
   
}