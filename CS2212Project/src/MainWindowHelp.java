/**
 * @author PengJu Chang (pchang32)
 */
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JEditorPane;

public class MainWindowHelp {

	private JFrame frame;


	/**
	 * Create the application.
	 */
	public MainWindowHelp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Browsing Help");
		frame.setVisible(true);
		frame.setBounds(150, 150, 500, 600);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setBackground(Color.WHITE);
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);
		
		//Creates a editor panel for displaying formatted text
		JEditorPane helpTextPane = new JEditorPane();
		helpTextPane.setContentType("text/html");
		helpTextPane.setText("<b><u>Help with main browsing window:</u></b><br><br>" + "<b>1) Browsing:</b><br>" + "To find a "
				+ "room that you want to find, first select the name of the building from the drop down menu, then select a"
				+ "floor number and finally select the room to see where it is located on the map!<br>" + "<u>NOTE: If the "
						+ "building, floor and/or room is not in the drop down menus, you will need to add them using the"
						+ "editing mode by clicking on the \"Edit Mode\" button.</u><br>" + "<u>NOTE: Reselect the building name"
								+ "from the buildings drop down menu after exiting edit mode!</u><br><br>" + "<b>2) adding"
										+ "/removing favourites:</b><br>" + "<ul><li>To add a room to the list of favourites, first"
												+ "browse the room you want to add, then check the favourites check box!<li>If you want to "
												+ "remove a room from the favourites, either browse to a room that you have added to the"
												+ "favourites or select a room from the favourites list, then uncheck the favourites check"
												+ "box.</ul><br><br>" + "<b>3) Points of interests:</b><br>" + "To see a type of points"
														+ "of interest displayed on the map, just check the box for the respective point"
														+ "of interest type.<br><u>NOTE: Only one type of points of interest can be displayed"
														+ "on the map at once! And reselecting a building, floor or room will reset the points"
														+ "of interest checkbox!</u>");
		helpTextPane.setEditable(false);
		
		//Scroll panel to scroll text
		JScrollPane scrollPane = new JScrollPane(helpTextPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(10, 11, 464, 484);
		frame.getContentPane().add(scrollPane);
		
		//Sets the scroll bar to the top
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() { 
				scrollPane.getVerticalScrollBar().setValue(0);
			}
		});
		
		//Exit button that closes the help window
		JButton buttonExitHelp = new JButton("Exit");
		buttonExitHelp.setBounds(179, 506, 125, 44);
		frame.getContentPane().add(buttonExitHelp);
		
		buttonExitHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
	}
}
