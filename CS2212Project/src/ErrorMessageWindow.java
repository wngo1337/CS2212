/**
 * @author PengJu Chang (pchang32)
 */
import javax.swing.JFrame;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;

public class ErrorMessageWindow {

	private JFrame frame;

	/**
	 * Create the application.
	 */
	public ErrorMessageWindow(String code) {
		initialize(code);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(String code) {
		frame = new JFrame("Error!");
		frame.setVisible(true);
		frame.setBounds(500, 500, 360, 195);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setBackground(Color.WHITE);
		frame.setResizable(false);
		
		//Close button to close the error message
		JButton closeButton = new JButton("Close");
		closeButton.setBounds(120, 104, 110, 41);
		frame.getContentPane().add(closeButton);
		
		closeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
			
		});
		
		//Checks what error code is passed to this window and displays the appropriate error message
		JLabel errorLabel = null;
		if(code.compareTo("newmap") == 0) {
			errorLabel = new JLabel("<html><div style='text-align: center;'>Please make sure that the building name and floor number"
					+ " has been entered, and that you have chosen a map file!");
			errorLabel.setBounds(25, -50, 300, 200);
			frame.getContentPane().add(errorLabel);
		} else if(code.compareTo("noroominfo") == 0) {
			errorLabel = new JLabel("<html><div style='text-align: center;'>Please make sure that the name of the room has been entered and you have click on"
					+ " the map where the room should be!");
			errorLabel.setBounds(25, -50, 300, 200);
			frame.getContentPane().add(errorLabel);
		} else if(code.compareTo("buildingandfloorexists") == 0) {
			errorLabel = new JLabel("<html><div style='text-align: center;'>The building and floor you are trying to add already exists"
					+ ", please try a different building and floor!");
			errorLabel.setBounds(25, -50, 300, 200);
			frame.getContentPane().add(errorLabel);
		} else if(code.compareTo("nolessthantwobuildings") == 0) {
			errorLabel = new JLabel("<html><div style='text-align: center;'>You cannot have less than two buildings!");
			errorLabel.setBounds(55, -50, 300, 200);
			frame.getContentPane().add(errorLabel);
		} else if(code.compareTo("defaultBuilding") == 0) {
			errorLabel = new JLabel("<html><div style='text-align: center;'>You cannot delete the default Building!");
			errorLabel.setBounds(55, -50, 300, 200);
			frame.getContentPane().add(errorLabel);
		} else if(code.compareTo("nonavigation") == 0) {
			errorLabel = new JLabel("<html><div style='text-align: center;'>This floor does not have navigation POIs!");
			errorLabel.setBounds(55, -50, 300, 200);
			frame.getContentPane().add(errorLabel);
		} else if(code.compareTo("nofoodshopping") == 0) {
			errorLabel = new JLabel("<html><div style='text-align: center;'>This floor does not have food and shopping POIs!");
			errorLabel.setBounds(35, -50, 300, 200);
			frame.getContentPane().add(errorLabel);
		} else if(code.compareTo("selectfloor") == 0) {
			errorLabel = new JLabel("<html><div style='text-align: center;'>Please select and building and floor first!");
			errorLabel.setBounds(52, -50, 300, 200);
			frame.getContentPane().add(errorLabel);
		} else if(code.compareTo("emptynavigation") == 0) {
			errorLabel = new JLabel("<html><div style='text-align: center;'>Please make sure you entered a name for the<br> navigation"
					+ " and you have clicked on the map where the POI should be!");
			errorLabel.setBounds(25, -50, 300, 200);
			frame.getContentPane().add(errorLabel);
		} else if(code.compareTo("emptyfoodshopping") == 0) {
			errorLabel = new JLabel("<html><div style='text-align: center;'>Please make sure you entered a name for the<br> food and shopping"
					+ " and you have clicked on the map where the POI should be!");
			errorLabel.setBounds(25, -50, 300, 200);
			frame.getContentPane().add(errorLabel);
		} else if(code.compareTo("emptydeletenavigation") == 0) {
			errorLabel = new JLabel("<html><div style='text-align: center;'>Please make sure you have entered a navigation name!");
			errorLabel.setBounds(25, -50, 300, 200);
			frame.getContentPane().add(errorLabel);
		} else if(code.compareTo("emptydeletefoodshopping") == 0) {
			errorLabel = new JLabel("<html><div style='text-align: center;'>Please make sure you have entered a food/shopping name!");
			errorLabel.setBounds(25, -50, 300, 200);
			frame.getContentPane().add(errorLabel);
		}
	}
}
