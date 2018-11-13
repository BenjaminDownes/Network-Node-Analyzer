package Network_Analyzer;

import java.util.ArrayList;

public class Node {
	protected int duration;
	protected String name;
	protected ArrayList<Node> child_nodes;
	
	//default constructor
	public Node() {
		name = "";
		duration = 0;
		child_nodes = new ArrayList<Node>();
	}

	//constructor
	public Node(String n, int d) {
		name = n;
		duration = d;
		child_nodes = new ArrayList<Node>();
	}

	//returns list of child nodes
	public ArrayList<Node> get_children() {
		return child_nodes;
	}

	//overwrites the current child_node list with a given list
	public void set_children(ArrayList<Node> list) {
		child_nodes = list;
	}
	
	//adds child to list of dependencies
	public void add_child(Node child) {
		child_nodes.add(child);
	}
	
	//returns false if child is not in list (couldn't be removed)
	public boolean remove_child(Node child) {
		//checks whether list is empty
		if(child_nodes.isEmpty())
			return false;
		else
			//returns true if list contained the specific element
			return child_nodes.remove(child);
	}
	
	//returns name of node
	public String get_name() {
		return name;
	}
	
	//sets the name of the node
	public void set_name(String n) {
		name = n;
	}
	
	public int get_duration() {
		return duration;
	}

	public void set_duration(int d) {
		duration = d;
	}

    public boolean equals(Node n) {
        if(n.get_name().equals(name))
            return true;
        else
            return false;
    }
    
    public String toString() {
    	String string = "";
    	string = "(" + name + ", " + duration + ")";
    	return string;
    }
}
