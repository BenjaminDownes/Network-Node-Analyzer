# class Network()

## append_node(name, duration, parents)
Adds a child node to the specifed parent node(s). This new node has no chidren specifed.

## insert_node(name, duration, parents, children)?
Inserts a node between other nodes. Parent and child nodes would need to be specified.

## remove_node(name)
return true/false

## isEmpty()
return true/false

## get_paths()
return list of path objects

Solver() method


# class Node(name, duration, dependencies)

private int duration

private list children

private string name

## get_paths()
return list of path objects

## get_children()
return list of node objects

## set_children(list of node objects)

## add_child(node object)

## remove_child(node object)

## get_name()
return node name

## set_name(string)

---
# class Path()

## add_node(string)

## remove_node(string)

## get_duration()
return path's total duration

## get_path()
private int duration

private list path

return list of strings containing node names

e.g. ["a", "b", "c"]
