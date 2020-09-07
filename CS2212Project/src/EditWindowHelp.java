/**
 * @author PengJu Chang (pchang32)
 */
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class EditWindowHelp {

	private JFrame frame;

	/**
	 * Create the application.
	 */
	public EditWindowHelp() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Editing Help");
		frame.setVisible(true);
		frame.setBounds(150, 150, 500, 600);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setBackground(Color.WHITE);
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);
		
		//Creates the editor panel to display formatted text.
		JEditorPane helpTextPane = new JEditorPane();
		helpTextPane.setContentType("text/html");
		helpTextPane.setText("<b><u>Help with editting existing maps or adding new ones:</b></u><br><br><b>1) Deleting existing buildings, floors"
				+ " or rooms:</b><br><ul><li><u>To delete a building</u>: Select a building and click the \"Delete\" button under the dropdown menu for"
				+ "buildings.<li><u>To delete a floor</u>: Select a building first, then select the floor you wish to delete and click on the \"Delete\""
				+ "button under the dropdown menu for floors.<li><u>To delete a room</u>: Select a building, then select a floor, finally select the "
				+ "room you wish to delete and click on the \"Delete\" button under the list of rooms.</ul><br><br><b>2) Adding a new room to an"
				+ "existing map:</b><br>To add a new room to an existing map, select the building and then the floor you wish the room to be on,"
				+ "then enter the name of the room in the text box and an optional description for the room, then click on the map where the room"
				+ "would go, and click the \"Add to map\" button.<br><br><b>3) Adding a new map:</b><br><ul><li><u>To add a new building</u>: Enter"
				+ " the name of the building, enter the floor number, click the \"Browse for map\" button and select a map in .jpg format and then"
				+ "click the \"Add new map\" button.<li><u>To add a new floor</u>: Enter the name of the building that you want to add the floor to ("
				+ "The building must already exist), enter the floor number, browse for the map in .jpg format and click the \"Add new map\" button.</ul><br>"
				+ "<br><b>4) Adding/deleting points of interests:</b><br><ul><li><u>Adding a point of interest</u>: Enter the name of the point"
				+ " of interest in the text box of the correct type of POI with an optional description of the point of interest, then click on the map "
				+ "where the point of interest should go and click the \"Add\" button.<li><u>Deleting a point of interest</u>: Enter the name of the point"
				+ "of interest in the text box of the correct type of POI, then click the \"Delete\" button.</ul><br><u>NOTE: When the points of interest"
				+ "check box is check, you cannot add or delete POIs, uncheck the boxes first before addiing/deleting.</u>");
		helpTextPane.setEditable(false);
		
		//Scroll panel to scroll text
		JScrollPane scrollPane = new JScrollPane(helpTextPane, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(10, 11, 464, 484);
		frame.getContentPane().add(scrollPane);
		
		//Sets the scroll bar to be at the top
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() { 
				scrollPane.getVerticalScrollBar().setValue(0);
			}
		});
		
		//Exit button to close the help window.
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
