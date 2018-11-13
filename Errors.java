package Network_Analyzer;

public class Errors {
	protected int no_error = 0;
	protected int duplicate_node = 1;
	protected int node_loop = 2;
	protected int orphaned_node = 3;
	protected int multiple_end_nodes = 4;
	public int multiple_start_nodes = 5;
	public int invalid_parent_reference = 6;
	
	protected boolean no_error_set = false;
	protected boolean duplicate_node_set = false;
	protected boolean node_loop_set = false;
	protected boolean orphaned_node_set = false;
	protected boolean multiple_end_nodes_set = false;
	public boolean multiple_start_nodes_set = false;
	public boolean invalid_parent_reference_set = false;
	
	
	public boolean isNo_error_set() {
		return no_error_set;
	}
	public void setNo_error(boolean no_error_set) {
		this.no_error_set = no_error_set;
		if(no_error_set) {
			duplicate_node_set = false;
			node_loop_set = false;
			orphaned_node_set = false;
			multiple_end_nodes_set = false;
			multiple_start_nodes_set = false;
			invalid_parent_reference_set = false;
		}
	}
	public boolean isDuplicate_node_set() {
		return duplicate_node_set;
	}
	public void setDuplicate_node(boolean duplicate_node_set) {
		this.duplicate_node_set = duplicate_node_set;
	}
	public boolean isNode_loop_set() {
		return node_loop_set;
	}
	public void setNode_loop(boolean node_loop_set) {
		this.node_loop_set = node_loop_set;
	}
	public boolean isOrphaned_node_set() {
		return orphaned_node_set;
	}
	public void setOrphaned_node(boolean orphaned_node_set) {
		this.orphaned_node_set = orphaned_node_set;
	}
	public boolean isMultiple_end_nodes_set() {
		return multiple_end_nodes_set;
	}
	public void setMultiple_end_nodes(boolean multiple_end_nodes_set) {
		this.multiple_end_nodes_set = multiple_end_nodes_set;
	}
	public boolean isMultiple_start_nodes_set() {
		return multiple_start_nodes_set;
	}
	public void setMultiple_start_nodes(boolean multiple_start_nodes_set) {
		this.multiple_start_nodes_set = multiple_start_nodes_set;
	}
	public boolean isInvalid_parent_reference_set() {
		return invalid_parent_reference_set;
	}
	public void setInvalid_parent_reference(boolean invalid_parent_reference_set) {
		this.invalid_parent_reference_set = invalid_parent_reference_set;
	}
	

}
