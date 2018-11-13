package Network_Analyzer;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.AbstractAction;
import javax.swing.Action;
import java.awt.Font;
import javax.swing.JCheckBox;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.UIManager;


public class Node_Inputs {

	private JFrame frame;

	private final Action action = new SwingAction();
	private final JLabel lblNetworkAnalyzer = new JLabel("Network Analyzer");
	public boolean criticalPath = false;
	Network network = new Network();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Node_Inputs window = new Node_Inputs();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Node_Inputs() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public void initialize() {

		frame = new JFrame();
		frame.setBounds(100, 100, 852, 624);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton button = new JButton("Exit");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);
			}
		});
		button.setBounds(616, 516, 97, 25);
		frame.getContentPane().add(button);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(364, 112, 384, 189);
		frame.getContentPane().add(scrollPane);
		
		JTextPane output = new JTextPane();
		output.setBackground(UIManager.getColor("FormattedTextField.inactiveBackground"));
		output.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		scrollPane.setViewportView(output);
		output.setEditable(false);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setBackground(UIManager.getColor("Button.focus"));
		btnSubmit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) { 
				network = new Network();
				//use a for loop to go through counter (which represents the # of rows)
				
				//iterate through each component in the frame by Row
				//counter should represent the # of rows
				ArrayList<String> names = new ArrayList<String>();		
				ArrayList<Integer> durations = new ArrayList<Integer>();
				ArrayList<String> parents = new ArrayList<String>();
				
				int activity_y_val = 42;
				for (int i = 0; i < counter; i++) {
					names.add(((JFormattedTextField)frame.getContentPane().getComponentAt(22, activity_y_val)).getText());
					if(((JFormattedTextField)frame.getContentPane().getComponentAt(22, activity_y_val)).getText().isEmpty()) {
						output.setText("an activityName is empty");
						output.setForeground(Color.red);
						throw new java.lang.Error("an activityName is empty");
					}
					activity_y_val+= 50;
				}	
				
				int duration_y_val = 42;
				int errorStatus = 0;
				for (int i = 0; i < counter; i++) {
					if(((JFormattedTextField)frame.getContentPane().getComponentAt(128, duration_y_val)).getText().isEmpty()) {
						output.setText("a duration field is empty");
						output.setForeground(Color.red);
						throw new java.lang.Error("a duration field is empty");
					}
					try{
						durations.add(Integer.parseInt(((JFormattedTextField)frame.getContentPane().getComponentAt(128, duration_y_val)).getText()));
					}
					catch(NumberFormatException e) {
						output.setText("Duration must be an integer!");
						output.setForeground(Color.red);
					}
					
					duration_y_val += 50;

					//throw error msg if not int or empty
					//if (((JFormattedTextField)frame.getContentPane().getComponentAt(128, duration_y_val)).getText().isEmpty() || ((JFormattedTextField)frame.getContentPane().getComponentAt(128, duration_y_val)).getText())
				}
				
				int parents_y_val = 42;
				for (int i = 0; i < counter; i++) {
					parents.add(((JFormattedTextField)frame.getContentPane().getComponentAt(248, parents_y_val)).getText());
					
					parents_y_val += 50;
				
				
			}
				for(int j = 0; j < names.size(); j++) {
					ArrayList<String> tmp = new ArrayList<String>();
					if(parents.get(j).isEmpty()) {
						errorStatus = network.add_node(names.get(j), durations.get(j), tmp);
					}
					else {
						for(String element : parents.get(j).split(",")) {
							tmp.add(element);
							
						}
						errorStatus = network.add_node(names.get(j), durations.get(j), tmp);
	
					}
				}
				System.out.println(errorStatus);
				
				Errors errors = network.build_network();
				
				if(errors.isNode_loop_set()) {
					output.setText("node_loop");
					output.setForeground(Color.red);
				}
				else if (errorStatus == 1) {
					output.setText("duplicate_node");
					output.setForeground(Color.red);
				}
				else if (errorStatus == errors.multiple_start_nodes) {
					output.setText("multiple_start_nodes");
					output.setForeground(Color.red);
				}
				else if(errors.isMultiple_end_nodes_set()) {
					output.setText("multiple_end_nodes");
					output.setForeground(Color.red);
				}
				else if(errors.isInvalid_parent_reference_set()) {
					output.setText("invalid_parent_reference");
					output.setForeground(Color.red);
				}
				else if(errors.isMultiple_start_nodes_set()) {
					output.setText("isMultiple_start_nodes_set");
					output.setForeground(Color.red);
				}
				else if(errors.isOrphaned_node_set()) {
					output.setText("isOrphaned_node_set");
					output.setForeground(Color.red);
				}
				
				else {  
					if (criticalPath == true) { //implement criticalPaths checkbox action into submit buttons return output
						//get_crit_paths
						output.setText("success...\n" + network.printBasicInfo() + "\nGet Critical Paths: \n" + network.get_critical_paths());
						output.setForeground(Color.black);
					}
					else {
						//get_paths
						output.setText("success...\n" + network.printBasicInfo() + "\nGet Paths: \n" + network.get_paths());
						output.setForeground(Color.black);
					}
					
				}
			
				
				network.printInfo();
				
				
								
				frame.setVisible(true);			
				frame.revalidate();

		}});
		btnSubmit.setBounds(474, 360, 97, 25);
		
		frame.getContentPane().add(btnSubmit);
		JButton btnHelp = new JButton("Help");
		btnHelp.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "New Node: Adds additional nodes" + "\n" + "Submit: Inserts all inputs into the program" + "\n" + "About this program: Information about the program and it's developers" + "\n" + "Restart: Resets program to its input stage" + "\n" + "Quit: Quits program" + "\n" + "Generate Report: Creates a text file that includes all the information relating to input and output." + "\n" + "Calculate critical paths: Determines critical paths that result from the nodes and durations inputted");
        	}
		});
		btnHelp.setBounds(525, 516, 79, 25);
		frame.getContentPane().add(btnHelp);
		
		JButton btnAbout = new JButton("About this program");
		btnAbout.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			JOptionPane.showMessageDialog(frame.getComponent(0), "This program analyzes all inputted paths in a network, determines their duration, calculates the critical path(s), and creates reports as text files." + "\n" + "Team members involved in program: Kristian Charboneau, Qianru Zhao, Benjamin Downes, Weinn Jiang");
	        }
		});
		btnAbout.setBounds(334, 516, 179, 25);
		frame.getContentPane().add(btnAbout);
		
		JLabel lblActivityName = new JLabel("Activity Name");
		lblActivityName.setBounds(12, 13, 112, 16);
		frame.getContentPane().add(lblActivityName);
		
		JLabel label = new JLabel("Duration");
		label.setBounds(139, 13, 97, 16);
		frame.getContentPane().add(label);
		
		JLabel label_1 = new JLabel("Dependencies");
		label_1.setBounds(248, 13, 97, 16);
		frame.getContentPane().add(label_1);
		
		JFormattedTextField activityName_1 = new JFormattedTextField();
		activityName_1.setBounds(22, 42, 79, 22);
		frame.getContentPane().add(activityName_1);
		
		JFormattedTextField duration_1 = new JFormattedTextField();
		duration_1.setBounds(128, 42, 79, 22);
		frame.getContentPane().add(duration_1);
		lblNetworkAnalyzer.setFont(new Font("Times New Roman", Font.PLAIN, 30));
		lblNetworkAnalyzer.setBounds(589, 0, 245, 76);
		frame.getContentPane().add(lblNetworkAnalyzer);
		
		JButton btnAdd = new JButton("Add Row");
		btnAdd.setBackground(UIManager.getColor("Button.focus"));
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnAdd.setBounds(392, 360, 67, 25);
		frame.getContentPane().add(btnAdd);
		btnAdd.setAction(action);
		
		JFormattedTextField dependencies_1 = new JFormattedTextField();
		dependencies_1.setBounds(248, 42, 79, 22);
		frame.getContentPane().add(dependencies_1);
		
		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(e.getSource() == btnReset) {
					int activity_y_val = 42;
					((JFormattedTextField)frame.getContentPane().getComponentAt(22, 42 )).setText("");		
					((JFormattedTextField)frame.getContentPane().getComponentAt(128, 42)).setText("");
					((JFormattedTextField)frame.getContentPane().getComponentAt(248, 42)).setText("");
					for (int i = 0; i < counter; i++) {
						activity_y_val+= 50;
						frame.remove(frame.getContentPane().getComponentAt(22, activity_y_val));
						frame.remove(frame.getContentPane().getComponentAt(128, activity_y_val));
						frame.remove(frame.getContentPane().getComponentAt(248, activity_y_val));
						frame.repaint();
						
						
						
					}
					y = 42;
					counter = 1;
					network = new Network();
					output.setText("Network has been reset");
					output.setForeground(Color.green);

				}
			}
		});
		btnReset.setBounds(725, 516, 97, 25);
		frame.getContentPane().add(btnReset);
		
		JButton btnGenerate = new JButton("Generate Report");
		btnGenerate.setBackground(UIManager.getColor("CheckBoxMenuItem.selectionBackground"));
		btnGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String filenameInput = JOptionPane.showInputDialog(frame, "Enter Filename", null);
				Date date = new Date();
				File file = new File(filenameInput);
				PrintWriter fileInfo;
				try {
					fileInfo = new PrintWriter(file);
					fileInfo.print("Title for the Report: ");
					fileInfo.println(filenameInput);
					fileInfo.print("Date and time of creation: ");
					fileInfo.println(date.toString());
					fileInfo.print("Activities in alphanumeric order with current duration: ");
				    fileInfo.print(network.nodes_toString());
				    fileInfo.print("Paths with activity names and total duration: ");
				    fileInfo.print(network.paths_toString() + network.criticalPaths_toString());
					fileInfo.close();
					
					
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Desktop desktop = Desktop.getDesktop();
				if(file.exists()) { 
					try {
						desktop.open(file);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
 			}
		});
		btnGenerate.setBounds(392, 401, 179, 56);
		frame.getContentPane().add(btnGenerate);
		
		JCheckBox chckbxCalculate = new JCheckBox("Calculate critical paths");
		
		chckbxCalculate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 criticalPath = !criticalPath; //set boolean to opposite value when checked/unchecked, initialized at false
 			}			
		});
		chckbxCalculate.setBounds(392, 319, 194, 29);
		frame.getContentPane().add(chckbxCalculate);
		//node testing function
		//0: 10-node network, in order
		//1: 10-node network, out of order
		//node_test(1);
    
	}
	int y = 42;
	int counter = 1;
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "Add");
			putValue(SHORT_DESCRIPTION, "Add a new row");
		}

		public void actionPerformed(ActionEvent e) {
			
			JFormattedTextField activityName_x = new JFormattedTextField();
			activityName_x.setBounds(22, y+50, 79, 22);
			frame.getContentPane().add(activityName_x);
			activityName_x.setName("activityName_" + counter);
		

			JFormattedTextField duration_x = new JFormattedTextField();
			duration_x.setBounds(128, y+50, 79, 22);
			frame.getContentPane().add(duration_x);
			duration_x.setName("duration_" + counter);
			
			JFormattedTextField dependencies_x = new JFormattedTextField();
			dependencies_x.setBounds(248, y+50, 79, 22);
			frame.getContentPane().add(dependencies_x);
			dependencies_x.setName("dependencies_" + counter);
			y+=50;
			counter++; //keeps track of current row index
			frame.repaint();
		}
		
	}
		/**public void node_test(int testcase) {
		if(testcase == 0) {
			Network network = new Network();
			ArrayList<String> parentA = new ArrayList<>();
			ArrayList<String> parentB = new ArrayList<>();
			parentB.add("A");
			ArrayList<String> parentC = new ArrayList<>();
			parentC.add("A");
			ArrayList<String> parentD = new ArrayList<>();
			parentD.add("B");
			parentD.add("C");
			ArrayList<String> parentE = new ArrayList<>();
			parentE.add("A");
			parentE.add("D");
			ArrayList<String> parentF = new ArrayList<>();
			parentF.add("B");
			parentF.add("D");
			ArrayList<String> parentG = new ArrayList<>();
			parentG.add("E");
			parentG.add("I");
			ArrayList<String> parentH = new ArrayList<>();
			parentH.add("G");
			ArrayList<String> parentI = new ArrayList<>();
			parentI.add("F");
			ArrayList<String> parentJ = new ArrayList<>();
			parentJ.add("I");
			parentJ.add("H");
			
			network.add_node("A", 4, parentA);
			network.add_node("B", 2, parentB);
			network.add_node("C", 1, parentC);
			network.add_node("D", 3, parentD);
			network.add_node("E", 6, parentE);
			network.add_node("F", 5, parentF);
			network.add_node("G", 1, parentG);
			network.add_node("H", 3, parentH);
			network.add_node("I", 2, parentI);
			network.add_node("J", 7, parentJ);
			
			network.build_network();
			network.printInfo();
		}
		else if(testcase == 1) {
			Network network = new Network();
			ArrayList<String> parentA = new ArrayList<>();
			ArrayList<String> parentB = new ArrayList<>();
			parentB.add("A");
			ArrayList<String> parentC = new ArrayList<>();
			parentC.add("A");
			ArrayList<String> parentD = new ArrayList<>();
			parentD.add("B");
			parentD.add("C");
			ArrayList<String> parentE = new ArrayList<>();
			parentE.add("A");
			parentE.add("D");
			ArrayList<String> parentF = new ArrayList<>();
			parentF.add("B");
			parentF.add("D");
			ArrayList<String> parentG = new ArrayList<>();
			parentG.add("E");
			parentG.add("I");
			ArrayList<String> parentH = new ArrayList<>();
			parentH.add("G");
			ArrayList<String> parentI = new ArrayList<>();
			parentI.add("F");
			ArrayList<String> parentJ = new ArrayList<>();
			parentJ.add("I");
			parentJ.add("H");
			network.add_node("J", 7, parentJ);
			network.add_node("E", 6, parentE);
			network.add_node("D", 3, parentD);
			network.add_node("F", 5, parentF);
			network.add_node("A", 4, parentA);
			network.add_node("B", 2, parentB);
			network.add_node("G", 1, parentG);
			network.add_node("C", 1, parentC);
			network.add_node("I", 2, parentI);
			network.add_node("H", 3, parentH);
			
			network.build_network();
			network.printInfo();
		}
	}
	**/
}