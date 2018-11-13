package Network_Analyzer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import java.util.LinkedHashSet;
// Ideas:
// Use same add_node() method to build an initial list of nodes.
// Keep using the same code for this stage as it seems to work.
// During this stage check if a node is duplicated.

// Use build_network() method to check the network for errors (except loops).
// Have separate methods for checking each error.
// Then link nodes together (fill in each node's child list).
// After this path_finder is called to find all paths in the network, returns an error if a loop is encountered.

// get_paths returns a list of all paths in network (empty if no paths or an error was encountered in the build_network stage.



public class Network {
	
	private Errors error = new Errors(); // Load errors object
	private Node start; //Start node of network
	private Node end; //End node of network
	
	private static LinkedHashSet<Path> paths; //Stores all paths in the network.
	//Each path is an object that stores a list of path names in order from start to end.
	//E.G. paths = [Path1, Path2, Path3]
	
	private ArrayList<String> nodeNames; //Maintains a list of all node names
	private LinkedHashSet<String> parentSet; //maintains a set of parent nodes that are referenced by each node
//	private HashMap<Node, ArrayList<String>> nodesMap;
	
	private HashMap<String, Node> nodeMap; //Keys are node names, values are corresponding node objects
	//private HashMap<Node, ArrayList<Path>> pathMap; //Associates a node with a list of paths
	
	private HashMap<String, Integer> durationMap; //Maps node names to duration
	private HashMap<String, ArrayList<String>> parentMap; //Maps node names to list of parents
	
	private ArrayList<Node> nodelist = new ArrayList<Node>(); //Store all nodes in the network
	
	//private boolean invalid_network = false;
	
	
    /**
     * 
     */
    public Network() {
    	clear();
//    	paths = new LinkedHashSet<Path>(); // Array of path objects
//    	start = null;
//    	end = null;
//    	nodeMap = new HashMap<String, Node>();
//    	nodeNames = new ArrayList<String>();
//    	parentSet = new LinkedHashSet<String>();
//    	durationMap = new HashMap<String, Integer>();
//    	parentMap = new HashMap<String, ArrayList<String>>();
    }
    
    /**
     * Add a new node to the network.
     * @param name
     * @param duration
     * @param parents
     * @return error code
     * Seems to work as expected
     */
    public int add_node(String name, int duration, ArrayList<String> parents) {
    	Node node = new Node(name, duration); //Create new node object
    	//Check that name is allowed
    	if(name == null || name == "") {
    		return error.no_error;
    	}
    	//Check if node already exists
    	if(durationMap.get(name) != null) {
    		return error.duplicate_node;
    	}
    	
    	//Check for start node
    	if(parents.isEmpty()) {
    		if(start == null) {
    			start = node; //Placeholder node to indicate start node exists
    			parentMap.put(name, new ArrayList<String>());
    		}
    		else {
    			return error.multiple_start_nodes;
    		}
    	}
    	//Add node info to data structures
		durationMap.put(name, duration);
		System.out.println("Name: "+ name + " Parents: "+parents.toString());
		parentMap.put(name, parents);
		nodeNames.add(name);
		parentSet.addAll(parents);
		nodeMap.put(name, node);
		nodelist.add(node);
		//size++;
		
    	return error.no_error;
    }
    
    
    /**
     * Build the network. If an error is encountered then network building will be aborted and an error code returned.
     * @return error code
     * There may be issues present here
     */
    public Errors build_network() {
    	//Process:
    	// Run initial error checking on network
    	// Build linked network
    	// Find all paths in network
    	
    	
    	System.out.println("Network size: " + nodeMap.size());
    	int errorCode = error.no_error;
    	
    	//Check for reference to nonexistent parent
    	for(String parent : parentSet) {
    		if(!nodeNames.contains(parent)) {
    			clear();
    			System.out.println("invalid_parent_reference");
    			error.setInvalid_parent_reference(true);
    			return error;
    		}
    	}
    	
    	// Find end node
    	// Check for duplicate end nodes
    	for(String nodeName : nodeNames) {
    		if(!parentSet.contains(nodeName)) {
    			if(end == null) {//make sure there is no other end node set
    				end = nodeMap.get(nodeName);
    				System.out.println("End node: " + end);
    			}
    			else {//An end node already exists!
    				clear();
    				System.out.println("multiple_end_nodes");
    				error.setMultiple_end_nodes(true);
    				return error;
    			}
    		}
    	}
    	
    	//Recursively link nodes starting from end node
    	//System.out.println("End node: "+ end.get_name());
    	//build_links(end);
	
    	//Add each node to its parents' child list
		for(Node node : nodelist) {
			//Get the parents of node
			for(String parent: parentMap.get(node.get_name())) {
				System.out.println(node.get_name() + "'s parent: " + parent);
				nodeMap.get(parent).add_child(node);
			}
		}
    	
		//printInfo();
    	
    	// Find paths in network
		System.out.println("Build_network: finding paths");
    	errorCode = find_paths();
    	
    	if(errorCode == error.node_loop) {
    		error.setNode_loop(true);
    	}
    	
    	//Original code doesn't work:
//    	ArrayList<Path> pathList = new ArrayList<Path>();
//		for(Node child : start.get_children()) {
//			Path path = new Path();
//			path.append_node(start);
//			//Append Child to new path and check for loop
//			if(path.append_node(child) == error.node_loop) {
//				System.out.println("Build Network: Node loop detected");
//				error.setNode_loop(true);
//				return error;
//			}
//			ArrayList<Path> tmpList = new ArrayList<Path>();
//			tmpList.add(path);
//			//Add new path to path list
//			try {
//				pathList.addAll(build_paths(child, tmpList));
//			} 
//			catch (NullPointerException e) {
//				error.setNode_loop(true);
//				return error;
//			}
//			
//		}
//    	ArrayList<Path> tmpPaths  = build_paths(start, null); 
//    	if(pathList == null) {
//    		error.setNode_loop(true);
//    		System.out.println("Loop detected");
//    	}
//    	paths.addAll(pathList);
//    	System.out.println("Build network error code: " + errorCode);
    	return error;
    }
    
    /**
     * Find all paths and their durations
     * This bootstraps the path_finder method
     * Stores path objects in the paths linked hash set
     * @return error object storing any errors encountered
     */
    private int find_paths() {
    	//Setup integer error catcher
    	int errorCode = error.no_error;
    	
    	//Setup local list of paths, will track paths as they are built
    	ArrayList<Path> pathList = new ArrayList<Path>();
    	
    	//Make an initial path containing the start node
    	Path path = new Path();
    	path.append_node(start);
    	
    	pathList.add(path); //Add path to pathList
    	// Run the path finder
    	//errorCode = path_finder(pathList, 0);
    	
    	int index = 0; // Active list index
    	errorCode = path_finder(pathList, index);
//    	while(true) {
//    		if(pathList.isEmpty()) {
//    			break;
//    		}
//    		Path activePath = pathList.get(index);
//    		if(activePath.get_last_node() == end) { //Check path for completeness
//    			paths.add(activePath);
//    			pathList.remove(activePath);
//    			index++; //Advance to next path in list
//    			activePath = pathList.get(index);
//    		}
//    		
//    	}
    	return errorCode;
    }
    
    /**
     * This method finds all paths in the network and stores them in Network.paths
     * @return integer error code
     */
    private int path_finder(ArrayList<Path> pathList, int index) {
    	if(index >= pathList.size()) {
    		index = 0;
    	}
    	System.out.println("_____Pathfinder:____");
    	System.out.println("pathList:" + pathList.toString());
    	int errorCode = error.no_error;
    	//Check for loops anytime a node is added to a path
    	//Start:
    	//Check if list is empty
    	// Loop through list of paths
    	// Check if path is complete (last element is end node)
    	// 		Complete: Move path to main path list
    	// Pick last element(node) in active list
    	// evaluate each child of this node
    	// 		if last child: append child to active list
    	// 		else: copy active list and append child to copy
    	// repeat for all children
    	// Increment index (or loop around to zero if end of list)
    	// Call pathfinder again
    
    	if(pathList.isEmpty()) {
    		System.out.println("pathList is empty");
			return errorCode;
		}
		Path activePath = pathList.get(index);
		System.out.println("activePath: " + activePath.toString());
		// Pick last element(node) in the active path
		Node node = activePath.get_last_node();
		
		try{ 
			System.out.println("last node in the active path: " + node.get_name());
		}
		catch(NullPointerException e){
			error.setNode_loop(true); //throws error to UI 
		}
		//System.out.println("Node: " + node.get_name());
		if(node == end) { //Check path for completeness
			System.out.println("Found end node");
			paths.add(activePath);
			pathList.remove(activePath);
			index++; //Advance to next path in list
			return path_finder(pathList, index);
//			if(path_finder(pathList, index) == error.node_loop) {
//				return error.node_loop;
//			}	
		}
		
		// evaluate each child of this node
		ArrayList<Node> children = node.get_children();
		System.out.println("Children: " + children.toString());
		System.out.println("children.size() = " + children.size());
		for(Node child : children) {
			System.out.println("children.indexOf(child) = " + children.indexOf(child));
			if(children.indexOf(child) == (children.size()-1)) {
				System.out.println("Last child");
				if(activePath.append_node(child)== error.node_loop) {
					System.out.println("Node loop detected");
					return error.node_loop;
				}
			}
			else {
				// copy active list and append child to copy
				System.out.println("Copy active list and append child to copy");
				Path newPath = new Path();
				newPath.copy(activePath);
				//newPath.set_path(activePath.get_path());
				if(newPath.append_node(child) == error.node_loop) {
					System.out.println("Node loop detected");
					return error.node_loop;
				}
				pathList.add(newPath);
			}
		}
//		for(int i = 0; i < children.size(); i++) {
//			Node child = children.get(i);
//			System.out.println("Child: " + child.get_name());
//			if(i == children.size() - 1) { //Check for last child
//				System.out.println("Last child");
//				if(activePath.append_node(child)== error.node_loop) {
//					System.out.println("Node loop detected");
//					return error.node_loop;
//				}
//			}
//			else {
//				// copy active list and append child to copy
//				Path newPath = new Path();
//				newPath.set_path(activePath.get_path());
//				if(newPath.append_node(child) == error.node_loop) {
//					System.out.println("Node loop detected");
//					return error.node_loop;
//				}
//			}
//		}
		
		//repeat process
		index++;
		
		
		
    	return path_finder(pathList, index);
    }
    
    /**
     * @param childNode
     * 
     */    private void build_links(Node childNode) {
    	System.out.println("build_links: childNode = "+childNode.get_name());
    	//Recursively build linked network backwards from the end node
    	
    	//End case
    	if(childNode.get_name() == start.get_name()) {
    		System.out.println("build_links: found start node");
    		return;
    	}
    	for(String parentName : parentMap.get(childNode.get_name())) {
    		System.out.println(parentName);
    		Node parentNode = nodeMap.get(parentName);
    		parentNode.add_child(childNode);
    		build_links(parentNode);
    	}
    	return;
    	
    	
    }
    
    /**
     * @param node
     * @param inPaths
     * @return
     * DOES NOT work as expected
     */
//    private ArrayList<Path> build_paths(Node node, ArrayList<Path> inPaths) {
//		// TODO Auto-generated method stub
//		ArrayList<Path> pathList = new ArrayList<Path>();
//		
//		System.out.println("Traversing node: "+ node.get_name() + " :" + node.get_name());
//    	if(node == end) {
//    		return inPaths;
//    	}
//    	
//    	for(Node child : node.get_children()) {
//    		ArrayList<Path> localPaths = new ArrayList<Path>();
//    		localPaths = build_paths(child, pathify(inPaths, node.get_children()));
//    		if(localPaths == null) {
//    			return null;
//    		}
//    		pathList.addAll(localPaths);
//    	}
//		
//	
//		return pathList;
//	}

    
    /**
     * Build new paths based on input paths and child node names
     * 
     * @param inPaths
     * @return ArrayList of paths or null if loop is detected
     * DOES NOT work as expected
     */
//    private ArrayList<Path> pathify(ArrayList<Path> inPaths, ArrayList<Node> children){
//    	ArrayList<Path> paths = new ArrayList<Path>();
//    	
//    	if(inPaths == null) {
//    		for(Node child : children) {
//    			Path path = new Path();
//    			//Append Child to new path and check for loop
//    			if(path.append_node(child) == error.node_loop) {
//    				System.out.println("Pathify: Node loop detected");
//    				paths = null;
//    				return paths;
//    			}
//    			//Add new path to path list
//    			paths.add(path);
//    		}
//    	}
//    	else {
//    		for(Path inPath : inPaths) {
//        		for(Node child : children) {
//        			Path path = new Path();
//        			path.set_path(inPath.get_path());//Copy inPath to new path
//        			path.set_duration(inPath.get_duration());
//        			//Append Child to new path and check for loop
//        			if(path.append_node(child) == error.node_loop) {
//        				System.out.println("Pathify: Node loop detected");
//        				paths = null;
//        				return paths;
//        			}
//        			//Add new path to path list
//        			System.out.println("Pathify: Adding path " + path.toString());
//        			paths.add(path);
//        		}
//        	}
//    	}
//    	
//    	
//    	System.out.println(paths);
//    	return paths;
//    }
    /**
     * Recursively traverse the network starting at the given node
     * @param node
     */
    private void traverse_network(Node node) {
    	System.out.println("Traversing node: "+ node.get_name() + " :" + node);
    	if(node == end) {
    		return;
    	}
    	for(Node child : node.get_children()) {
    		traverse_network(child);
    	}
    }
    
	private ArrayList<String> findChildren(String parentName) {
		// TODO Auto-generated method stub
    	
    	ArrayList<String> children = new ArrayList<String>(); //child nodes to return
    	ArrayList<String> parents = new ArrayList<String>(); //parents of current searched node
    	String currentNode;
    	
    	for(int i = 0; i < nodeNames.size(); i++) {
    		currentNode = nodeNames.get(i);
    		parents.clear();
    		parents.addAll(parentMap.get(currentNode)); //Load parent list of current node
    		
    		for(int j = 0; j < parents.size(); j++) { //Loop through parents list looking for parentName
    			if(parents.get(j).equals(parentName)) {//Match!
    				children.add(currentNode); //Add current node as child of parentName
    			}
    		}
    	}
    	return children;
		
	}
    
    /**
     * @param node
     * Remove node from network.
     * Note this does not do any sanity checking and may leave the network in a broken state.
     */
    private void remove_node(Node node) {
    	nodeMap.remove(node); //Remove new node to map
    	//pathMap.remove(node); //Remove new node to pathMap
    	//size--; //Decrement network size
    }
    
    public void clear() {
    	paths = new LinkedHashSet<Path>(); // Array of path objects
    	start = null;
    	end = null;
    	nodeMap = new HashMap<String, Node>();
    	nodeNames = new ArrayList<String>();
    	parentSet = new LinkedHashSet<String>();
    	durationMap = new HashMap<String, Integer>();
    	parentMap = new HashMap<String, ArrayList<String>>();
    	nodelist = new ArrayList<Node>();
	}

	public Node get_node(String name) {
    	return nodeMap.get(name);
    }
    
    public boolean isEmpty() {
    	return nodeMap.size() == 0;
    }
    
    /**
     * @return an ArrayList of paths in the network
     */
    public ArrayList<Path> get_paths() {
    	//Convert paths to ArrayList
    	ArrayList<Path> pathsList = new ArrayList<Path>(paths);
    	//Sort list based on path duration
    	Collections.sort(pathsList, new PathComparator());
    	return pathsList;
    }
    
    /*
     * @return an ArrayList of critical paths in the network
     */
    public ArrayList<Path> get_critical_paths() {
    	ArrayList<Path> critPaths = new ArrayList<Path>();
    	try {
    		ArrayList<Path> allPaths = get_paths();
        	
        	
        	critPaths.add(allPaths.get(0));
        	for(int i = 1; i < allPaths.size(); i++) {
        		if(allPaths.get(i).get_duration() == critPaths.get(0).get_duration()) {
        			critPaths.add(allPaths.get(i));
        		}
        		else
        			break;
        	}
    	}
    	catch(Exception e){
    		return new ArrayList<Path>();
    	}
    	
    	
    	
    	
    	return critPaths;
    }
    
    /**
     * Used to sort list of paths
     *
     */
    public class PathComparator implements Comparator<Path>{
    	@Override
    	public int compare(Path path1, Path path2) {
    		int result;
    		if(path1.get_duration() < path2.get_duration()){
    			result = 1;
    		}
    		else if (path1.get_duration() > path2.get_duration()){
    			result = -1;
    		}
    		else {
    			result = 0;
    		}
    		return result;
    	}
    }
    
     
    public String printBasicInfo() {
    	String string;
    	string = "Network Info:\n\n";
    	string += "Network size: " + nodeMap.size() + "\n";
    	try {
    		string += "Start node: " + start.get_name() + "\n";
    	}catch(Exception e) {
    		string += "Start node not set\n";
    	}
    	try{
    		string += "End node: " + end.get_name() + "\n";
    	}
    	catch (Exception e) {
			string += "End node not set";
		}
    	string += nodes_toString();
    	string += paths_toString();
    	
    	return string;
    }
    
    
    /**
     * Print information about the network to the console.
     * First each node's attributes are printed(name, duration, and children).
     * 
     * Example node formatting:
     * NodeName(Duration): Child1, Child2, Child3
     * 
     * Then each path is printed. Example path formatting:
     * Path1(duration):Node1->Node2->Node3
     */
    public String printInfo() {
    	String string;
    	string = "Network Info:\n\n";
    	string += "Network size: " + nodeMap.size() + "\n";
    	try {
    		string += "Start node: " + start.get_name() + "\n";
    	}catch(Exception e) {
    		string += "Start node not set\n";
    	}
    	try{
    		string += "End node: " + end.get_name() + "\n";
    	}
    	catch (Exception e) {
			string += "End node not set";
		}
    	
    	// Print each node
    	string += "Nodes: [NodeName(Duration): Child1, Child2, Child3]\n\n";
    	//System.out.println("Start node: " + start.get_name()+ "[" + start + "]\n");
    	for(HashMap.Entry<String, Node> pair: nodeMap.entrySet()) {
    		Node node = pair.getValue();
    		String output = ""; //Create output string
    		output += node.get_name(); //Add name
    		output += "(" + node.get_duration() + "): ";
    		//Print Child Nodes:
    		ArrayList<Node> nodeList = node.get_children();
    		for(int i=0; i < nodeList.size();i++) {
    			output += nodeList.get(i).get_name();
    			if(i != nodeList.size() - 1) {
    				output += ", ";
    			}
    		}
    		string += output  + "\n"; //Print the current node's info
    	}
    	string += "\n\n";
    	
    	
    	// Print each path
    	string += "Paths: [PathX(duration):Node1->Node2->Node3]\n" + "\n";
    	ArrayList<Path> tmpPathList = get_paths();
    	for(int i = 0; i<tmpPathList.size();i++) {
    		Path path = tmpPathList.get(i); //Get current path object
    		String output = ""; //Create output string
    		output += "Path" + i + "(" + path.get_duration() + "):";
    		for(int j = 0;j<path.path.size();j++) {// Loop through each node in path
    			output += path.path.get(j).get_name();
    			if(j != path.path.size() - 1) {//Check if -> should be added
    				output += "->";
    			}
    		}
    		string += output + "\n";
    	}
    	
    	//Print critical paths
    	string += "\nCritical paths:" + "\n";
    	ArrayList<Path> critPathList = get_critical_paths();
    	for(int i = 0; i < critPathList.size(); i++) {
    		Path path = critPathList.get(i);
    		String output = "";
    		for(int j = 0; j < path.path.size(); j++) {
    			output += path.path.get(j).get_name();
    			if(j != path.path.size() - 1) {
    				output += "->";
    			}
    		}
    		string += output + "\n";
    	}
    	
    	string += "\n\n";
    	string += "Parent Set: " + parentSet.toString() + "\n";
    	string += "Parent Map: " + parentMap.toString() + "\n";
    	
    	System.out.println(string);
    	return string;
    }
    
    public String nodes_toString() {
    	String string = "";
    	string += "\nNodes: [NodeName(Duration): Child1, Child2, Child3]\n";
    	//System.out.println("Start node: " + start.get_name()+ "[" + start + "]\n");
    	for(HashMap.Entry<String, Node> pair: nodeMap.entrySet()) {
    		Node node = pair.getValue();
    		String output = ""; //Create output string
    		output += node.get_name(); //Add name
    		output += "(" + node.get_duration() + "): ";
    		//Print Child Nodes:
    		ArrayList<Node> nodeList = node.get_children();
    		for(int i=0; i < nodeList.size();i++) {
    			output += nodeList.get(i).get_name();
    			if(i != nodeList.size() - 1) {
    				output += ", ";
    			}
    		}
    		string += output  + "\n"; //Print the current node's info
    	}
    	
    	return string;
    }
    
    public String paths_toString() {
    	String string = "\nPaths: [PathX(duration):Node1->Node2->Node3]\n";
    	ArrayList<Path> tmpPathList = get_paths();
    	for(int i = 0; i<tmpPathList.size();i++) {
    		Path path = tmpPathList.get(i); //Get current path object
    		String output = ""; //Create output string
    		output += "Path" + i + "(" + path.get_duration() + "):";
    		for(int j = 0;j<path.path.size();j++) {// Loop through each node in path
    			output += path.path.get(j).get_name();
    			if(j != path.path.size() - 1) {//Check if -> should be added
    				output += "->";
    			}
    		}
    		string += output + "\n";
    	}
    	return string;
    }
    
    public String criticalPaths_toString() {
    	String string = "\nCritical paths:" + "\n";
    	ArrayList<Path> critPathList = get_critical_paths();
    	for(int i = 0; i < critPathList.size(); i++) {
    		Path path = critPathList.get(i);
    		String output = "";
    		for(int j = 0; j < path.path.size(); j++) {
    			output += path.path.get(j).get_name();
    			if(j != path.path.size() - 1) {
    				output += "->";
    			}
    		}
    		string += output + "\n";
    	}
    	
    	
    	return string;
    }
    /**
     * Shows the relations between node names and node objects stored in map
     */
    public void printMap() {
    	System.out.println("Map:");
    	for (String name: nodeMap.keySet()){

            String key = name.toString();
            String value = nodeMap.get(name).get_name();  
            System.out.println(key + " -> " + value + "(" + nodeMap.get(name)+")");  
    	} 
    	System.out.println("\n");
	}
    /**
	 * Shows the relations between node objects and path lists stored in pathMap
	 * Not currently implemented due to changes in data structures
	 */
	public void printPathMap() {
//		System.out.println("pathMap:");
//		for (Node name: pathMap.keySet()){
//	
//	        String key = name.get_name();
//	        String value = "";
//	        if(!(pathMap.get(name) == null)) {
//	        	 value = pathMap.get(name).toString();
//	        	 System.out.println(key + " -> " + value);
//	        }
//	         
//	          
//		} 
//		System.out.println("\n");
	}
	
	public String printPaths() {
		String string;
		string = "paths\n";
		string += paths.toString() + "\n";
		System.out.println(string);
		return string;
	}
    
	public void printDataStructures() {
		printMap();
		printPathMap();
		printPaths();
		System.out.println("Parent set: " + parentSet.toString());
		System.out.println("Parent Map: " + parentMap.toString());
	}
// The following methods are lower priority as they are not necessary for phase 1.
    
//    public void insert_node(String name, int duration, ArrayList<String> parents, ArrayList<String> children) {
//    	//TODO
//    }
    
//    public void link_nodes(String parent, String child) {
//    	//TODO
//    	map.get(parent).add_child(map.get(child));
//    }
    
//    public boolean remove_node(String name) {
//    	//TODO
//    	return false; //Placeholder
//    }
    
    
}
