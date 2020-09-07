/**
 * @author PengJu Chang (pchang32)
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ToolTipManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JSeparator;

public class UIedit {

	private JFrame frame; //The frame of the editing mode window.
	private JLabel label = null; //Label to display the map
	private static BufferedImage image = null; //Map image
	private static BufferedImage markerImage = null; //Room marker image
	private static BufferedImage poi = null; //POI marker image
	private String inputImage = ""; //Input image path for map.
	private JScrollPane scrollPane; //Scroll panel to display the map with scroll bars
	private int iCopy = -1; //Selected building index initialized to -1.
	private int kCopy =  -1; //Selected floor index initialized to -1.
	private String[] buildingNames; //Array to store building names
	private String[] floorNames; //Array to store all floor names
	private String[] roomNames; //Array to store all room names.
	private int selectedRoomIndex = -1; //Selected room index initialized to -1.
	private int roomSelection = -1; //Selected room index initialized to -1.
	private String roomDescription; //Description of the room
	private int xCoor = 0; //X coordinate of the room.
	private int yCoor = 0; //Y coordinate of the room.
	private String newMapPath = null; //Path of new map image.
	private String newBuildingName = null; //new building name
	private String newFloorName = null; //New floor number
	private static int mouseXcoor = -1; //X coordinates of mouse click on image
	private static int mouseYcoor = -1; //Y coordinates of mouse click on image
	private String errorCode = null; //Error code
	private javaManager deleteBuildingManager = new javaManager("building"); //JavaManager object to delete buildings as well as to write back to json file.
	private JTextField textFieldNavigationName;
	private JTextField textFieldFoodShoppingName;
	private JTextField textFieldNavigationDescription;
	private JTextField textFieldFoodAndShoppingDescription;
	private JTextField textFieldDeleteNavigation;
	private JTextField textFieldDeleteFoodShopping;

	/**
	 * Create the application.
	 */
	public UIedit(ArrayList<Building> buildingList, Poi allPoi, Favourites favouriteManager, ArrayList<String> favouriteList) {
		initialize(buildingList, allPoi, favouriteManager, favouriteList);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize(ArrayList<Building> buildingList, Poi allPoi, Favourites favouriteManager, ArrayList<String> favouriteList) {
		frame = new JFrame("The Awesome Map Of Western - Edit Mode");
		frame.setVisible(true);
		frame.setBounds(100, 100, 1620, 800);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setBackground(Color.WHITE);
		frame.setResizable(false);
		
		//Adds the confirmation dialogue when closing the window.
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int confirmed = JOptionPane.showConfirmDialog(null, "Are you sure you want to save and exit?", "Save "
						+ "and exit?", JOptionPane.YES_NO_OPTION);
				if (confirmed == JOptionPane.YES_OPTION) {
					//Writes the data sturctures back to file.
					deleteBuildingManager.writingToBuilding(buildingList);
					deleteBuildingManager.writeToPoi(allPoi);
					frame.dispose();
				}
			}
		});
		
		//Populates the building names array
		buildingNames = new String[buildingList.size()];
		for(int i = 0; i < buildingList.size(); i++) {
			buildingNames[i] = buildingList.get(i).getName();
		}
		
		//defaultComboBoxModel for building dropdown menu
		DefaultComboBoxModel buildingModel = new DefaultComboBoxModel(buildingNames);
		JComboBox selectBuilding = new JComboBox(buildingModel);
		selectBuilding.setBounds(46, 79, 150, 50);
		frame.getContentPane().add(selectBuilding);
		
		//Button for deleting buildings
		JButton buttonBuildingDelete = new JButton("Delete");
		buttonBuildingDelete.setBounds(76, 140, 89, 23);
		frame.getContentPane().add(buttonBuildingDelete);
		
		//select floor dropdown menu
		JComboBox selectFloor = new JComboBox();
		selectFloor.setBounds(46, 203, 150, 50);
		frame.getContentPane().add(selectFloor);
		
		//Button for deleting floors
		JButton buttonDeleteFloor = new JButton("Delete");
		buttonDeleteFloor.setBounds(76, 264, 89, 23);
		frame.getContentPane().add(buttonDeleteFloor);
		
		//List of rooms, added to scrollpanel to make it scrollable.
		JList listRoom = new JList();
		JScrollPane scrollPaneJList = new JScrollPane();
		scrollPaneJList.setViewportView(listRoom);
		scrollPaneJList.setBounds(206, 79, 150, 160);
		frame.getContentPane().add(scrollPaneJList);
		
		//Initializes the scrollpanel to display map image.
		scrollPane = new JScrollPane();
		scrollPane.setBounds(380, 11, 870, 740);
		frame.getContentPane().add(scrollPane);
		JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
		label = new JLabel();
        scrollPane.setViewportView(panel);
        //Displays campus map as default map image.
        displayImage("src/images/Campus Map.jpg", panel);
        
        //Check box for food and shopping POI
        JCheckBox chckbxFoodShopping = new JCheckBox("Food & Shopping");
		chckbxFoodShopping.setBounds(1260, 448, 150, 23);
		frame.getContentPane().add(chckbxFoodShopping);
		
		//Check box for navigation POI.
		JCheckBox chckbxNavigation = new JCheckBox("Navigation");
		chckbxNavigation.setBounds(1260, 402, 150, 23);
		frame.getContentPane().add(chckbxNavigation);
		
		//Textfields for new buildings, floors, rooms etc.
		textFieldNavigationName = new JTextField();
		textFieldNavigationName.setBounds(1370, 77, 124, 20);
		frame.getContentPane().add(textFieldNavigationName);
		textFieldNavigationName.setColumns(10);	
		
		textFieldNavigationDescription = new JTextField();
		textFieldNavigationDescription.setBounds(1370, 124, 124, 20);
		frame.getContentPane().add(textFieldNavigationDescription);
		textFieldNavigationDescription.setColumns(10);
		
		textFieldFoodShoppingName = new JTextField();
		textFieldFoodShoppingName.setBounds(1370, 265, 124, 20);
		frame.getContentPane().add(textFieldFoodShoppingName);
		textFieldFoodShoppingName.setColumns(10);
		
		textFieldFoodAndShoppingDescription = new JTextField();
		textFieldFoodAndShoppingDescription.setBounds(1370, 328, 124, 20);
		frame.getContentPane().add(textFieldFoodAndShoppingDescription);
		textFieldFoodAndShoppingDescription.setColumns(10);
		
		textFieldDeleteNavigation = new JTextField();
		textFieldDeleteNavigation.setBounds(1370, 544, 124, 20);
		frame.getContentPane().add(textFieldDeleteNavigation);
		textFieldDeleteNavigation.setColumns(10);
		
		textFieldDeleteFoodShopping = new JTextField();
		textFieldDeleteFoodShopping.setBounds(1370, 603, 124, 20);
		frame.getContentPane().add(textFieldDeleteFoodShopping);
		textFieldDeleteFoodShopping.setColumns(10);
		
		//Buttons to add or delete POIs.
		JButton buttonAddNavigation = new JButton("Add");
		buttonAddNavigation.setBounds(1504, 98, 89, 23);
		frame.getContentPane().add(buttonAddNavigation);
		
		JButton buttonAddFoodShopping = new JButton("Add");
		buttonAddFoodShopping.setBounds(1504, 294, 89, 23);
		frame.getContentPane().add(buttonAddFoodShopping);
		
		JButton buttonDeleteNavigation = new JButton("Delete");
		buttonDeleteNavigation.setBounds(1504, 543, 89, 23);
		frame.getContentPane().add(buttonDeleteNavigation);
				
		JButton buttonDeleteFoodShopping = new JButton("Delete");
		buttonDeleteFoodShopping.setBounds(1504, 602, 89, 23);
		frame.getContentPane().add(buttonDeleteFoodShopping);
		
		//Same code as main UI window.
		chckbxFoodShopping.setEnabled(false);
		chckbxFoodShopping.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JCheckBox cbsource = (JCheckBox) e.getSource();
				if (cbsource.isSelected()) {
					chckbxNavigation.setEnabled(false);
					textFieldNavigationName.setEnabled(false);
					textFieldNavigationDescription.setEnabled(false);
					textFieldFoodShoppingName.setEnabled(false);
					textFieldFoodAndShoppingDescription.setEnabled(false);
					textFieldDeleteNavigation.setEnabled(false);
					textFieldDeleteFoodShopping.setEnabled(false);
					buttonAddNavigation.setEnabled(false);
					buttonAddFoodShopping.setEnabled(false);
					buttonDeleteNavigation.setEnabled(false);
					buttonDeleteFoodShopping.setEnabled(false);
					if(roomSelection != -1) {
						String buildingName = buildingList.get(iCopy).getName();
						String floorNum = buildingList.get(iCopy).getFloorNum().get(kCopy);
						String key = buildingName + floorNum;
						int index = -1;
						if(allPoi.returnAll().containsKey("food and shopping")) {
							for(int i = 0; i < allPoi.returnAll().get("food and shopping").size(); i++) {
								if(allPoi.returnAll().get("food and shopping").get(i).getKey().compareTo(key) == 0) {
									index = i;
									break;
								}
							}
							
							if(index == -1) {
								new ErrorMessageWindow("nofoodshopping");
								chckbxFoodShopping.setSelected(false);
								chckbxNavigation.setEnabled(true);
								textFieldNavigationName.setEnabled(true);
								textFieldNavigationDescription.setEnabled(true);
								textFieldFoodShoppingName.setEnabled(true);
								textFieldFoodAndShoppingDescription.setEnabled(true);
								textFieldDeleteNavigation.setEnabled(true);
								textFieldDeleteFoodShopping.setEnabled(true);
							} else {
								ArrayList<RoomCoorPoi> roomCoor = allPoi.returnAll().get("food and shopping").get(index).getAllRooms();
								displayImageWithMarkerPOI(inputImage, panel, xCoor, yCoor, roomDescription, roomCoor, "food and shopping");
								roomSelection = -2;
							}
						} else {
							new ErrorMessageWindow("nofoodshooping");
							chckbxFoodShopping.setSelected(false);
							chckbxNavigation.setEnabled(true);
							textFieldNavigationName.setEnabled(true);
							textFieldNavigationDescription.setEnabled(true);
							textFieldFoodShoppingName.setEnabled(true);
							textFieldFoodAndShoppingDescription.setEnabled(true);
							textFieldDeleteNavigation.setEnabled(true);
							textFieldDeleteFoodShopping.setEnabled(true);
							buttonAddNavigation.setEnabled(true);
							buttonAddFoodShopping.setEnabled(true);
							buttonDeleteNavigation.setEnabled(true);
							buttonDeleteFoodShopping.setEnabled(true);
						}
					} else {
						String buildingName = buildingList.get(iCopy).getName();
						String floorNum = buildingList.get(iCopy).getFloorNum().get(kCopy);
						String key = buildingName + floorNum;
						int index = -1;
						if(allPoi.returnAll().containsKey("food and shopping")) {
							for(int i = 0; i < allPoi.returnAll().get("food and shopping").size(); i++) {
								if(allPoi.returnAll().get("food and shopping").get(i).getKey().compareTo(key) == 0) {
									index = i;
									break;
								}
							}
							
							if(index == -1) {
								new ErrorMessageWindow("nofoodshopping");
								chckbxFoodShopping.setSelected(false);
								chckbxNavigation.setEnabled(true);
								textFieldNavigationName.setEnabled(true);
								textFieldNavigationDescription.setEnabled(true);
								textFieldFoodShoppingName.setEnabled(true);
								textFieldFoodAndShoppingDescription.setEnabled(true);
								textFieldDeleteNavigation.setEnabled(true);
								textFieldDeleteFoodShopping.setEnabled(true);
								buttonAddNavigation.setEnabled(true);
								buttonAddFoodShopping.setEnabled(true);
								buttonDeleteNavigation.setEnabled(true);
								buttonDeleteFoodShopping.setEnabled(true);
							} else {
								ArrayList<RoomCoorPoi> roomCoor = allPoi.returnAll().get("food and shopping").get(index).getAllRooms();
								displayImageWithPOI(inputImage, panel, roomCoor, "food and shopping");
							}
						} else {
							new ErrorMessageWindow("nofoodshopping");
							chckbxFoodShopping.setSelected(false);
							chckbxNavigation.setEnabled(true);
							textFieldNavigationName.setEnabled(true);
							textFieldNavigationDescription.setEnabled(true);
							textFieldFoodShoppingName.setEnabled(true);
							textFieldFoodAndShoppingDescription.setEnabled(true);
							textFieldDeleteNavigation.setEnabled(true);
							textFieldDeleteFoodShopping.setEnabled(true);
							buttonAddNavigation.setEnabled(true);
							buttonAddFoodShopping.setEnabled(true);
							buttonDeleteNavigation.setEnabled(true);
							buttonDeleteFoodShopping.setEnabled(true);
						}
					}
				} else {
					chckbxNavigation.setEnabled(true);
					textFieldNavigationName.setEnabled(true);
					textFieldNavigationDescription.setEnabled(true);
					textFieldFoodShoppingName.setEnabled(true);
					textFieldFoodAndShoppingDescription.setEnabled(true);
					textFieldDeleteNavigation.setEnabled(true);
					textFieldDeleteFoodShopping.setEnabled(true);
					buttonAddNavigation.setEnabled(true);
					buttonAddFoodShopping.setEnabled(true);
					buttonDeleteNavigation.setEnabled(true);
					buttonDeleteFoodShopping.setEnabled(true);
					if(roomSelection != -1) {
						displayImageWithMarker(inputImage, panel, xCoor, yCoor, roomDescription);
						roomSelection = -2;
					} else {
						displayImage(inputImage, panel);
					}
				}
			}
		});
		
		//Same code as main UI window.
		chckbxNavigation.setEnabled(false);
		chckbxNavigation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JCheckBox cbsource = (JCheckBox) e.getSource();
				if (cbsource.isSelected()) {
					chckbxFoodShopping.setEnabled(false);
					textFieldNavigationName.setEnabled(false);
					textFieldNavigationDescription.setEnabled(false);
					textFieldFoodShoppingName.setEnabled(false);
					textFieldFoodAndShoppingDescription.setEnabled(false);
					textFieldDeleteNavigation.setEnabled(false);
					textFieldDeleteFoodShopping.setEnabled(false);
					buttonAddNavigation.setEnabled(false);
					buttonAddFoodShopping.setEnabled(false);
					buttonDeleteNavigation.setEnabled(false);
					buttonDeleteFoodShopping.setEnabled(false);
					if(roomSelection != -1) {
						String buildingName = buildingList.get(iCopy).getName();
						String floorNum = buildingList.get(iCopy).getFloorNum().get(kCopy);
						String key = buildingName + floorNum;
						int index = -1;
						if(allPoi.returnAll().containsKey("navigation")) {
							for(int i = 0; i < allPoi.returnAll().get("navigation").size(); i++) {
								if(allPoi.returnAll().get("navigation").get(i).getKey().compareTo(key) == 0) {
									index = i;
									break;
								}
							}
							
							if(index == -1) {
								new ErrorMessageWindow("nonavigation");
								chckbxNavigation.setSelected(false);
								chckbxFoodShopping.setEnabled(true);
								textFieldNavigationName.setEnabled(true);
								textFieldNavigationDescription.setEnabled(true);
								textFieldFoodShoppingName.setEnabled(true);
								textFieldFoodAndShoppingDescription.setEnabled(true);
								textFieldDeleteNavigation.setEnabled(true);
								textFieldDeleteFoodShopping.setEnabled(true);
								buttonAddNavigation.setEnabled(true);
								buttonAddFoodShopping.setEnabled(true);
								buttonDeleteNavigation.setEnabled(true);
								buttonDeleteFoodShopping.setEnabled(true);
							} else {
								ArrayList<RoomCoorPoi> roomCoor = allPoi.returnAll().get("navigation").get(index).getAllRooms();
								displayImageWithMarkerPOI(inputImage, panel, xCoor, yCoor, roomDescription, roomCoor, "navigation");
								roomSelection = -2;
							}
						} else {
							new ErrorMessageWindow("nonavigation");
							chckbxNavigation.setSelected(false);
							chckbxFoodShopping.setEnabled(true);
							textFieldNavigationName.setEnabled(true);
							textFieldNavigationDescription.setEnabled(true);
							textFieldFoodShoppingName.setEnabled(true);
							textFieldFoodAndShoppingDescription.setEnabled(true);
							textFieldDeleteNavigation.setEnabled(true);
							textFieldDeleteFoodShopping.setEnabled(true);
							buttonAddNavigation.setEnabled(true);
							buttonAddFoodShopping.setEnabled(true);
							buttonDeleteNavigation.setEnabled(true);
							buttonDeleteFoodShopping.setEnabled(true);
						}
					} else {
						String buildingName = buildingList.get(iCopy).getName();
						String floorNum = buildingList.get(iCopy).getFloorNum().get(kCopy);
						String key = buildingName + floorNum;
						int index = -1;
						if(allPoi.returnAll().containsKey("navigation")) {
							for(int i = 0; i < allPoi.returnAll().get("navigation").size(); i++) {
								if(allPoi.returnAll().get("navigation").get(i).getKey().compareTo(key) == 0) {
									index = i;
									break;
								}
							}
							
							if(index == -1) {
								new ErrorMessageWindow("nonavigation");
								chckbxNavigation.setSelected(false);
								chckbxFoodShopping.setEnabled(true);
								textFieldNavigationName.setEnabled(true);
								textFieldNavigationDescription.setEnabled(true);
								textFieldFoodShoppingName.setEnabled(true);
								textFieldFoodAndShoppingDescription.setEnabled(true);
								textFieldDeleteNavigation.setEnabled(true);
								textFieldDeleteFoodShopping.setEnabled(true);
								buttonAddNavigation.setEnabled(true);
								buttonAddFoodShopping.setEnabled(true);
								buttonDeleteNavigation.setEnabled(true);
								buttonDeleteFoodShopping.setEnabled(true);
							} else {
								ArrayList<RoomCoorPoi> roomCoor = allPoi.returnAll().get("navigation").get(index).getAllRooms();
								displayImageWithPOI(inputImage, panel, roomCoor, "navigation");
							}
						} else {
							new ErrorMessageWindow("nonavigation");
							chckbxNavigation.setSelected(false);
							chckbxFoodShopping.setEnabled(true);
							textFieldNavigationName.setEnabled(true);
							textFieldNavigationDescription.setEnabled(true);
							textFieldFoodShoppingName.setEnabled(true);
							textFieldFoodAndShoppingDescription.setEnabled(true);
							textFieldDeleteNavigation.setEnabled(true);
							textFieldDeleteFoodShopping.setEnabled(true);
							buttonAddNavigation.setEnabled(true);
							buttonAddFoodShopping.setEnabled(true);
							buttonDeleteNavigation.setEnabled(true);
							buttonDeleteFoodShopping.setEnabled(true);
						}
					}
				} else {
					chckbxFoodShopping.setEnabled(true);
					textFieldNavigationName.setEnabled(true);
					textFieldNavigationDescription.setEnabled(true);
					textFieldFoodShoppingName.setEnabled(true);
					textFieldFoodAndShoppingDescription.setEnabled(true);
					textFieldDeleteNavigation.setEnabled(true);
					textFieldDeleteFoodShopping.setEnabled(true);
					buttonAddNavigation.setEnabled(true);
					buttonAddFoodShopping.setEnabled(true);
					buttonDeleteNavigation.setEnabled(true);
					buttonDeleteFoodShopping.setEnabled(true);
					if(roomSelection != -1) {
						displayImageWithMarker(inputImage, panel, xCoor, yCoor, roomDescription);
						roomSelection = -2;
					} else {
						displayImage(inputImage, panel);
					}
				}
			}
		});
		
		selectBuilding.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				kCopy = -1;
				chckbxFoodShopping.setSelected(false);
				chckbxNavigation.setSelected(false);
				chckbxNavigation.setEnabled(false);
				chckbxFoodShopping.setEnabled(false);
				textFieldNavigationName.setEnabled(true);
				textFieldNavigationDescription.setEnabled(true);
				textFieldFoodShoppingName.setEnabled(true);
				textFieldFoodAndShoppingDescription.setEnabled(true);
				textFieldDeleteNavigation.setEnabled(true);
				textFieldDeleteFoodShopping.setEnabled(true);
				buttonAddNavigation.setEnabled(true);
				buttonAddFoodShopping.setEnabled(true);
				buttonDeleteNavigation.setEnabled(true);
				buttonDeleteFoodShopping.setEnabled(true);
				int i = selectBuilding.getSelectedIndex();
				iCopy = i;
				floorNames = new String[buildingList.get(i).getFloorNum().size()];
				for(int j = 0; j < buildingList.get(i).getFloorNum().size(); j++) {
					floorNames[j] = buildingList.get(i).getFloorNum().get(j);
				}
				selectFloor.setModel(new DefaultComboBoxModel(floorNames));
			}
		});
		
		selectFloor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				chckbxFoodShopping.setSelected(false);
				chckbxNavigation.setSelected(false);
				textFieldNavigationName.setEnabled(true);
				textFieldNavigationDescription.setEnabled(true);
				textFieldFoodShoppingName.setEnabled(true);
				textFieldFoodAndShoppingDescription.setEnabled(true);
				textFieldDeleteNavigation.setEnabled(true);
				textFieldDeleteFoodShopping.setEnabled(true);
				buttonAddNavigation.setEnabled(true);
				buttonAddFoodShopping.setEnabled(true);
				buttonDeleteNavigation.setEnabled(true);
				buttonDeleteFoodShopping.setEnabled(true);
				xCoor = 0;
				yCoor = 0;
				int k = selectFloor.getSelectedIndex();
				kCopy = k;
				String floor = floorNames[k];
				roomNames = new String[buildingList.get(iCopy).getFloorToRomm(floor).size()];
				for(int l = 0; l < buildingList.get(iCopy).getFloorToRomm(floor).size(); l++) {
					roomNames[l] = buildingList.get(iCopy).getFloorToRomm(floor).get(l);
				}
				listRoom.setModel(new DefaultComboBoxModel(roomNames));
				
				inputImage = buildingList.get(iCopy).getImagePath(floor);
				displayImage(inputImage, panel);
				if(kCopy != -1) {
					chckbxNavigation.setEnabled(true);
					chckbxFoodShopping.setEnabled(true);
				}
			}
		});
		
		MouseListener mouseListenerListRoom = new MouseAdapter() {
		    public void mouseClicked(MouseEvent e) {
		        if (e.getClickCount() == 1 && (kCopy >= 0 && iCopy >= 0) && roomNames.length != 0) {
		        	chckbxFoodShopping.setSelected(false);
					chckbxNavigation.setSelected(false);
					textFieldNavigationName.setEnabled(true);
					textFieldNavigationDescription.setEnabled(true);
					textFieldFoodShoppingName.setEnabled(true);
					textFieldFoodAndShoppingDescription.setEnabled(true);
					textFieldDeleteNavigation.setEnabled(true);
					textFieldDeleteFoodShopping.setEnabled(true);
					buttonAddNavigation.setEnabled(true);
					buttonAddFoodShopping.setEnabled(true);
					buttonDeleteNavigation.setEnabled(true);
					buttonDeleteFoodShopping.setEnabled(true);
		        	String floor = floorNames[kCopy];
		        	String room = (String) listRoom.getSelectedValue();
		        	selectedRoomIndex = listRoom.getSelectedIndex();
		        	roomSelection = selectedRoomIndex;
		        	xCoor = buildingList.get(iCopy).getRoomCoor(room).get(0);
		        	yCoor = buildingList.get(iCopy).getRoomCoor(room).get(1);
		        	roomDescription = buildingList.get(iCopy).getRoomInfo(room);
					
		        	inputImage = buildingList.get(iCopy).getImagePath(floor);
		        	displayImageWithMarker(inputImage, panel, xCoor, yCoor, roomDescription);
		        	if(kCopy != -1) {
						chckbxNavigation.setEnabled(true);
						chckbxFoodShopping.setEnabled(true);
					}
		        }
		    }
		};
		listRoom.addMouseListener(mouseListenerListRoom);
		
		JButton buttonRoomDelete = new JButton("Delete");
		buttonRoomDelete.setBounds(241, 249, 80, 23);
		frame.getContentPane().add(buttonRoomDelete);
		
		//delete a building.
		buttonBuildingDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(buildingModel.getSize() >= 3) {
					if(iCopy != -1) {
						String buildingName = buildingList.get(iCopy).getName();
						
						//Removes all POIs from each floor of the building
						for(int i = 0; i < buildingList.get(iCopy).getFloorNum().size(); i++) {
							allPoi.removeFloor("navigation", buildingName, buildingList.get(iCopy).getFloorNum().get(i));
							allPoi.removeFloor("food and shopping", buildingName, buildingList.get(iCopy).getFloorNum().get(i));
						}
						
						//Removes all favourited roooms.
						for(int i = 0; i < buildingList.get(iCopy).getFloorNum().size(); i++) {
							for(int j = 0; j < buildingList.get(iCopy).getFloorToRomm(buildingList.get(iCopy).getFloorNum().get(i)).size(); j++) {
								for(int k = 0; k < favouriteList.size(); k++) {
									if(favouriteList.get(k).compareTo(buildingList.get(iCopy).getName() + "," + buildingList.get(iCopy).getFloorNum().get(i) + "," + buildingList.get(iCopy).getFloorToRomm(buildingList.get(iCopy).getFloorNum().get(i)).get(j)) == 0) {
										favouriteManager.removeFavourite(buildingList.get(iCopy).getName(), buildingList.get(iCopy).getFloorNum().get(i), buildingList.get(iCopy).getFloorToRomm(buildingList.get(iCopy).getFloorNum().get(i)).get(j));
										favouriteList.remove(buildingList.get(iCopy).getName() + "," + buildingList.get(iCopy).getFloorNum().get(i) + "," + buildingList.get(iCopy).getFloorToRomm(buildingList.get(iCopy).getFloorNum().get(i)).get(j));
									}
								}
							}
						}
						
						//Removes the building and refreshes the dropdown menu.
						buildingModel.removeElementAt(iCopy);
						deleteBuildingManager.removeBuilding(buildingList, buildingName);
						refreshModel(buildingList, buildingModel);
					} else {
						errorCode = "defaultBuilding";
						new ErrorMessageWindow(errorCode);
					}
				} else {
					errorCode = "nolessthantwobuildings";
					new ErrorMessageWindow(errorCode);
				}
			}
		});
		
		//Deleting a floor.
		buttonDeleteFloor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(iCopy != -1 && kCopy != -1) {
					String floor = floorNames[kCopy];
					
					//Removes all POIs from this floor
					allPoi.removeFloor("navigation", buildingList.get(iCopy).getName(), floor);
					allPoi.removeFloor("food and shopping", buildingList.get(iCopy).getName(), floor);
					
					//Removes all favourited rooms from this floor
					for(int i = 0; i < buildingList.get(iCopy).getFloorToRomm(floor).size(); i++) {
						for(int j = 0; j < favouriteList.size(); j++) {
							if(favouriteList.get(j).compareTo(buildingList.get(iCopy).getName() + "," + floor + "," + buildingList.get(iCopy).getFloorToRomm(floor).get(i)) == 0) {
								favouriteManager.removeFavourite(buildingList.get(iCopy).getName(), floor, buildingList.get(iCopy).getFloorToRomm(floor).get(i));
								favouriteList.remove(buildingList.get(iCopy).getName() + "," + floor + "," + buildingList.get(iCopy).getFloorToRomm(floor).get(i));
							}
						}
					}
					
					//Removes the floor
					buildingList.get(iCopy).removeFloor(floor);
					
					//Refreshes the floor dropdown menu
					floorNames = new String[buildingList.get(iCopy).getFloorNum().size()];
					for(int j = 0; j < buildingList.get(iCopy).getFloorNum().size(); j++) {
						floorNames[j] = buildingList.get(iCopy).getFloorNum().get(j);
					}
					selectFloor.setModel(new DefaultComboBoxModel(floorNames));
					
					refreshModel(buildingList, buildingModel);
				}
			}
		});
		
		//Deletes a room
		buttonRoomDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(iCopy != -1 && kCopy != -1 && selectedRoomIndex != -1) {
					String room = (String) listRoom.getSelectedValue();
					
					//Removes the room from favourites is it is favourited.
					for(int i = 0; i < favouriteList.size(); i++) {
						if(favouriteList.get(i).compareTo(buildingList.get(iCopy).getName() + "," + buildingList.get(iCopy).getFloorNum().get(kCopy) + "," + room) == 0) {
							favouriteManager.removeFavourite(buildingList.get(iCopy).getName(), buildingList.get(iCopy).getFloorNum().get(kCopy), room);
							favouriteList.remove(buildingList.get(iCopy).getName() + "," + buildingList.get(iCopy).getFloorNum().get(kCopy) + "," + room);
							break;
						}
					}
					
					//Removes the room
					buildingList.get(iCopy).removeRoom(floorNames[kCopy], room);
					
					//Refreshes the room dropdown mennu.
					roomNames = new String[buildingList.get(iCopy).getFloorToRomm(floorNames[kCopy]).size()];
					for(int l = 0; l < buildingList.get(iCopy).getFloorToRomm(floorNames[kCopy]).size(); l++) {
						roomNames[l] = buildingList.get(iCopy).getFloorToRomm(floorNames[kCopy]).get(l);
					}
					listRoom.setModel(new DefaultComboBoxModel(roomNames));
					
					refreshModel(buildingList, buildingModel);
				}
			}
		});
		
		JTextField textFieldNewRoom = new JTextField();
		textFieldNewRoom.setBounds(46, 447, 190, 25);
		frame.getContentPane().add(textFieldNewRoom);
		
		JButton buttonAddToMap = new JButton("Add To Map");
		buttonAddToMap.setBounds(246, 447, 110, 25);
		frame.getContentPane().add(buttonAddToMap);
		
		JLabel labelRoomDescription = new JLabel("<HTML>Room Description:<br>(Optional)");
		labelRoomDescription.setBounds(47, 490, 109, 35);
		frame.getContentPane().add(labelRoomDescription);
		
		JTextField textFieldRoomDescription = new JTextField();
		textFieldRoomDescription.setBounds(166, 496, 190, 25);
		frame.getContentPane().add(textFieldRoomDescription);
		textFieldRoomDescription.setColumns(10);
		
		//Adds a new room to the map.
		buttonAddToMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String newRoomName = textFieldNewRoom.getText();
				String newRoomDescription = textFieldRoomDescription.getText();
				if(!newRoomName.equals("") && (mouseXcoor != -1 && mouseYcoor != -1)) {
					boolean roomExists = false;
					for(int i = 0; i < buildingList.get(iCopy).getFloorNum().size(); i++) {
						for(int j = 0; j < buildingList.get(iCopy).getFloorToRomm(floorNames[i]).size(); j++) {
							if(buildingList.get(iCopy).getFloorToRomm(floorNames[i]).get(j).compareTo(newRoomName) == 0) {
								roomExists = true;
								break;
							}
						}
					}
					
					if(roomExists == false) {
						buildingList.get(iCopy).addRoom(floorNames[kCopy], newRoomName);
						buildingList.get(iCopy).addRoomCoor(newRoomName, mouseXcoor, mouseYcoor);
						buildingList.get(iCopy).addRoomDescription(newRoomName, newRoomDescription);
						
						roomNames = new String[buildingList.get(iCopy).getFloorToRomm(floorNames[kCopy]).size()];
						for(int l = 0; l < buildingList.get(iCopy).getFloorToRomm(floorNames[kCopy]).size(); l++) {
							roomNames[l] = buildingList.get(iCopy).getFloorToRomm(floorNames[kCopy]).get(l);
						}
						listRoom.setModel(new DefaultComboBoxModel(roomNames));
						
						refreshModel(buildingList, buildingModel);
					} else {
						int optionChosen = JOptionPane.showConfirmDialog(null, "<HTML>This room already exists, do you want to "
								+ "change the location of this room?", "Are you sure?", JOptionPane.YES_NO_OPTION);
						if(optionChosen == 0) {
							buildingList.get(iCopy).removeRoom(floorNames[kCopy], newRoomName);
							buildingList.get(iCopy).addRoom(floorNames[kCopy], newRoomName);
							buildingList.get(iCopy).addRoomCoor(newRoomName, mouseXcoor, mouseYcoor);
							buildingList.get(iCopy).addRoomDescription(newRoomName, newRoomDescription);
						
							roomNames = new String[buildingList.get(iCopy).getFloorToRomm(floorNames[kCopy]).size()];
							for(int l = 0; l < buildingList.get(iCopy).getFloorToRomm(floorNames[kCopy]).size(); l++) {
								roomNames[l] = buildingList.get(iCopy).getFloorToRomm(floorNames[kCopy]).get(l);
							}
							listRoom.setModel(new DefaultComboBoxModel(roomNames));
						
							refreshModel(buildingList, buildingModel);
						}
					}
				} else {
					errorCode = "noroominfo";
					new ErrorMessageWindow(errorCode);
				}
				
				textFieldNewRoom.setText("");
				textFieldRoomDescription.setText("");
				mouseXcoor = -1;
				mouseYcoor = -1;
			}
		});
		
		JTextField textFieldBuildingName = new JTextField();
		textFieldBuildingName.setBounds(156, 560, 200, 25);
		frame.getContentPane().add(textFieldBuildingName);
		
		JTextField textFieldFloorNumber = new JTextField();
		textFieldFloorNumber.setBounds(156, 615, 200, 25);
		frame.getContentPane().add(textFieldFloorNumber);
		
		JButton buttonBrowse = new JButton("Browse For Map");
		buttonBrowse.setBounds(46, 720, 150, 25);
		frame.getContentPane().add(buttonBrowse);
		
		//Creates the file browser to browse for new map.
		buttonBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileBrowser = new JFileChooser();
				FileNameExtensionFilter fileFilter = new FileNameExtensionFilter("JPG Images", "jpg");
				fileBrowser.setFileFilter(fileFilter);
		        int returnValue = fileBrowser.showOpenDialog(null);
		        if(returnValue == JFileChooser.APPROVE_OPTION) {
		            newMapPath = fileBrowser.getSelectedFile().getAbsolutePath();
		        }
			}
		});
		
		JButton buttonAddNewMap = new JButton("Add New Map");
		buttonAddNewMap.setBounds(206, 720, 150, 25);
		frame.getContentPane().add(buttonAddNewMap);
		
		//Adds new map.
		buttonAddNewMap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				newBuildingName = textFieldBuildingName.getText();
				newFloorName = "floor" + textFieldFloorNumber.getText();
				if(newBuildingName != null && newFloorName != null && newMapPath != null) {
					boolean buildingExists = false, floorExists = false;
					int buildingIndex = 0;
					for(int i = 0; i < buildingList.size(); i++) {
						if(buildingList.get(i).getName().compareTo(newBuildingName) == 0) {
							buildingExists = true;
							buildingIndex = i;
							break;
						}
					}
					
					if(buildingExists == false) {
						Building addNewBuilding = new Building();
						addNewBuilding.addName(newBuildingName);
						buildingList.add(addNewBuilding);
						buildingList.get(buildingList.size() - 1).addFloorNum(newFloorName);
						buildingList.get(buildingList.size() - 1).addPath(newFloorName, newMapPath);
						refreshModel(buildingList, buildingModel);
					} else {
						for(int j = 0; j < buildingList.get(buildingIndex).getFloorNum().size(); j++) {
							if(buildingList.get(buildingIndex).getFloorNum().get(j).compareTo(newFloorName) == 0) {
								floorExists = true;
								break;
							}
						}
						
						if(floorExists == false) {
							buildingList.get(buildingIndex).addFloorNum(newFloorName);
							buildingList.get(buildingIndex).addPath(newFloorName, newMapPath);
							
							floorNames = new String[buildingList.get(buildingIndex).getFloorNum().size()];
							for(int j = 0; j < buildingList.get(buildingIndex).getFloorNum().size(); j++) {
								floorNames[j] = buildingList.get(buildingIndex).getFloorNum().get(j);
							}
							selectFloor.setModel(new DefaultComboBoxModel(floorNames));
							
							refreshModel(buildingList, buildingModel);
						} else {
							errorCode = "buildingandfloorexists";
							new ErrorMessageWindow(errorCode);
						}
					}
				} else {
					errorCode = "newmap";
					new ErrorMessageWindow(errorCode);
				}
				
				newMapPath = null;
				newBuildingName = null;
				newFloorName = null;
				textFieldBuildingName.setText("");
				textFieldFloorNumber.setText("");
			}
		});
		
		JLabel labelSelectBuilding = new JLabel("Select Building");
		labelSelectBuilding.setBounds(46, 46, 150, 23);
		frame.getContentPane().add(labelSelectBuilding);
		
		JLabel labelSelectFloor = new JLabel("Select Floor");
		labelSelectFloor.setBounds(46, 169, 150, 23);
		frame.getContentPane().add(labelSelectFloor);
		
		JSeparator separatorOne = new JSeparator();
		separatorOne.setBounds(46, 303, 310, 2);
		frame.getContentPane().add(separatorOne);
		
		JSeparator separatorTwo = new JSeparator();
		separatorTwo.setBounds(46, 544, 310, 2);
		frame.getContentPane().add(separatorTwo);
		
		JLabel labelBuildingName = new JLabel("Building Name");
		labelBuildingName.setBounds(46, 558, 100, 23);
		frame.getContentPane().add(labelBuildingName);
		
		JLabel labelFloorNumber = new JLabel("<HTML>Floor Number<br>(Enter 1, 2, 3, etc.)");
		labelFloorNumber.setBounds(46, 603, 100, 50);
		frame.getContentPane().add(labelFloorNumber);
		
		JLabel labelAddNewBuildingNote = new JLabel("<HTML>NOTE: Adding an already existing building but different floor, will add a new floor to that building!");
		labelAddNewBuildingNote.setBounds(46, 664, 310, 50);
		frame.getContentPane().add(labelAddNewBuildingNote);
		
		JLabel labelAddToMap = new JLabel("<HTML>To add a new room to the current map, select the " + 
				"building, select the floor (selecting a room is optional), then click on the map at the new location, enter the " + 
				"name of the location in the text box below " + 
				"and click the \"Add To Map\" button.<br>"
				+ "NOTE: Adding an already existing room will replace the current one!");
		labelAddToMap.setBounds(46, 316, 310, 120);
		frame.getContentPane().add(labelAddToMap);
		
		JLabel labelAddNavigationPoiName = new JLabel("Navigation name");
		labelAddNavigationPoiName.setBounds(1260, 76, 100, 23);
		frame.getContentPane().add(labelAddNavigationPoiName);
		
		JLabel labelNavigationDescription = new JLabel("<HTML>Navigation description");
		labelNavigationDescription.setBounds(1260, 110, 115, 50);
		frame.getContentPane().add(labelNavigationDescription);
		
		JLabel labelAddNavigationPoi = new JLabel("Add navigation POI");
		labelAddNavigationPoi.setBounds(1377, 50, 130, 14);
		frame.getContentPane().add(labelAddNavigationPoi);
		
		JLabel labelAddFoodShoppingName = new JLabel("<HTML>Add food and shopping Name");
		labelAddFoodShoppingName.setBounds(1260, 255, 100, 41);
		frame.getContentPane().add(labelAddFoodShoppingName);
		
		JLabel labelFoodAndShoppingDescription = new JLabel("<HTML>Food and shopping description");
		labelFoodAndShoppingDescription.setBounds(1260, 310, 110, 50);
		frame.getContentPane().add(labelFoodAndShoppingDescription);
		
		JLabel labelAddFoodShopping = new JLabel("Add food and shopping POI");
		labelAddFoodShopping.setBounds(1355, 230, 170, 23);
		frame.getContentPane().add(labelAddFoodShopping);
		
		JLabel labelDeletePois = new JLabel("Delete POIs");
		labelDeletePois.setBounds(1398, 517, 66, 14);
		frame.getContentPane().add(labelDeletePois);
		
		JLabel labelDeleteNavigation = new JLabel("Navigation Name");
		labelDeleteNavigation.setBounds(1260, 537, 109, 35);
		frame.getContentPane().add(labelDeleteNavigation);
		
		JLabel labelDeleteFoodShopping = new JLabel("<HTML>Food and shoppnig Name");
		labelDeleteFoodShopping.setBounds(1260, 593, 89, 35);
		frame.getContentPane().add(labelDeleteFoodShopping);	
		
		//Adds a new navigation POI
		buttonAddNavigation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(iCopy != -1 && kCopy != -1) {
					if(!textFieldNavigationName.getText().equals("") && (mouseXcoor != -1 && mouseYcoor != -1)) {
						String poiRoomNum = textFieldNavigationName.getText();
						String poiDescription = textFieldNavigationDescription.getText();
						String buildingName = buildingList.get(iCopy).getName();
						String floorName = floorNames[kCopy];
						allPoi.addRoom("navigation", buildingName, floorName, poiRoomNum, mouseXcoor, mouseYcoor, poiDescription);
					} else {
						errorCode = "emptynavigation";
						new ErrorMessageWindow(errorCode);
					}
				} else {
					errorCode = "selectfloor";
					new ErrorMessageWindow(errorCode);
				}
				textFieldNavigationName.setText("");
				textFieldNavigationDescription.setText("");
				mouseXcoor = -1;
				mouseYcoor = -1;
			}
		});
		
		//Adds a new food and shopping POI.
		buttonAddFoodShopping.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(iCopy != -1 && kCopy != -1) {
					if(!textFieldFoodShoppingName.getText().equals("") && (mouseXcoor != -1 && mouseYcoor != -1)) {
						String poiRoomNum = textFieldFoodShoppingName.getText();
						String poiDescription = textFieldFoodAndShoppingDescription.getText();
						String buildingName = buildingList.get(iCopy).getName();
						String floorName = floorNames[kCopy];
						allPoi.addRoom("food and shopping", buildingName, floorName, poiRoomNum, mouseXcoor, mouseYcoor, poiDescription);
					} else {
						errorCode = "emptyfoodshopping";
						new ErrorMessageWindow(errorCode);
					}
				} else {
					errorCode = "selectfloor";
					new ErrorMessageWindow(errorCode);
				}
				textFieldFoodShoppingName.setText("");
				textFieldFoodAndShoppingDescription.setText("");
				mouseXcoor = -1;
				mouseYcoor = -1;
			}
		});
		
		//Delete a navigation POI
		buttonDeleteNavigation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(iCopy != -1 && kCopy != -1) {
					if(!textFieldDeleteNavigation.getText().equals("")) {
						String poiDelete = textFieldDeleteNavigation.getText();
						String buildingName = buildingList.get(iCopy).getName();
						String floorName = floorNames[kCopy];
						allPoi.removeRoom("navigation", buildingName, floorName, poiDelete);
					} else {
						errorCode = "emptydeletenavigation";
						new ErrorMessageWindow(errorCode);
					}
				} else {
					errorCode = "selectfloor";
					new ErrorMessageWindow(errorCode);
				}
				textFieldDeleteNavigation.setText("");
				mouseXcoor = -1;
				mouseYcoor = -1;
			}
		});
		
		//Deletes a food and shopping POI.
		buttonDeleteFoodShopping.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(iCopy != -1 && kCopy != -1) {
					if(!textFieldDeleteFoodShopping.getText().equals("")) {
						String poiDelete = textFieldDeleteFoodShopping.getText();
						String buildingName = buildingList.get(iCopy).getName();
						String floorName = floorNames[kCopy];
						allPoi.removeRoom("food and shopping", buildingName, floorName, poiDelete);
					} else {
						errorCode = "emptydeletefoodshopping";
						new ErrorMessageWindow(errorCode);
					}
				} else {
					errorCode = "selectfloor";
					new ErrorMessageWindow(errorCode);
				}
				textFieldDeleteFoodShopping.setText("");
				mouseXcoor = -1;
				mouseYcoor = -1;
			}
		});
		
		//Creates a save and exit button that writes data structures to file and exits edit mode.
		JButton buttonSaveAndExit = new JButton("Save and Exit");
		try {
		    Image saveImage = ImageIO.read(new File("src/images/save.png"));
		    buttonSaveAndExit.setIcon(new ImageIcon(new ImageIcon(saveImage).getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH)));
		} catch (Exception ex) {
		    System.out.println(ex);
		}
		buttonSaveAndExit.setBounds(1362, 701, 145, 50);
		frame.getContentPane().add(buttonSaveAndExit);
		
		buttonSaveAndExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Need to write to file here!
				deleteBuildingManager.writingToBuilding(buildingList);
				deleteBuildingManager.writeToPoi(allPoi);
				frame.dispose();
			}
		});
		
		//Help button for edit mode.
		JButton buttonHelp = new JButton();
		try {
		    Image helpIcon = ImageIO.read(new File("src/images/help.png"));
		    buttonHelp.setIcon(new ImageIcon(new ImageIcon(helpIcon).getImage().getScaledInstance(23, 23, Image.SCALE_SMOOTH)));
		} catch (Exception ex) {
		    System.out.println(ex);
		}
		buttonHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new EditWindowHelp();
			}
		});
		buttonHelp.setBounds(10, 11, 30, 30);
		frame.getContentPane().add(buttonHelp);
	}
	
	/**
	 * A method that displays a image in the panel.
	 * @param path
	 * 			file path of the image.
	 * @param panel
	 * 			the panel to display the image in
	 */
	public static void displayImage(String path, JPanel panel) {
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	    
	    panel.removeAll(); //Remove Current Image.
	    //Adds New Image.
	    if(path.compareTo("src/images/Campus Map.jpg") == 0) {
	    	panel.add(new JLabel(new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(1080, 1400, Image.SCALE_SMOOTH))));
	    } else {
	    	JLabel label = new JLabel(new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(image.getWidth() / 3, image.getHeight() / 3, Image.SCALE_SMOOTH)));
	    	panel.add(label);
	    	
	    	label.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
			         int mouseClickX = e.getX();
			         int mouseClickY = e.getY();
			         mouseXcoor = mouseClickX;
			         mouseYcoor = mouseClickY;
			         System.out.println(mouseXcoor + ", " + mouseYcoor);
			    }
			});
	    }
	    
	    panel.revalidate();
	    panel.repaint();
	}
	
	/**
	 * A method that displays a image with POI markers.
	 * @param path
	 * 			file path of the image
	 * @param panel
	 * 			the panel to display the image in
	 * @param list
	 * 			the list of POIs with their coordinates
	 * @param poiType
	 * 			the type of the POI to be displayed.
	 */
	public static void displayImageWithPOI(String path, JPanel panel, ArrayList<RoomCoorPoi> list, String poiType) {
		try {
			image = ImageIO.read(new File(path));
			if(poiType.compareTo("navigation") == 0) {
				poi = ImageIO.read(new File("src/images/poi_navigation.png"));
			} else {
				poi = ImageIO.read(new File("src/images/poi_food.png"));
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	    
	    panel.removeAll(); //Remove Current Image.
	    //Adds New Image.
	    if(path.compareTo("src/images/Campus Map.jpg") == 0) {
	    	panel.add(new JLabel(new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(1080, 1400, Image.SCALE_SMOOTH))));
	    } else {
	    	JLabel label = new JLabel(new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(image.getWidth() / 3, image.getHeight() / 3, Image.SCALE_SMOOTH)));
	    	for(int i = 0; i < list.size(); i++) {
				JLabel poiMarker = new JLabel(new ImageIcon(new ImageIcon(poi).getImage().getScaledInstance(46, 64, Image.SCALE_SMOOTH)));
				int poiXcoor = list.get(i).getCoor().get(0);
				int poiYcoor = list.get(i).getCoor().get(1);
				String poiDescription= list.get(i).getRoomDescription();
				String poiName = list.get(i).getRoomNum();
				poiMarker.setSize(label.getPreferredSize());
				poiMarker.setBounds(poiXcoor - 23, poiYcoor - 65, 46, 64);
				poiMarker.setToolTipText("<HTML>Name: " + poiName + "<br><br> Description: " + poiDescription);
				ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
				label.add(poiMarker);
			}
	    	panel.add(label);
	    	
	    	label.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
			         int mouseClickX = e.getX();
			         int mouseClickY = e.getY();
			         mouseXcoor = mouseClickX;
			         mouseYcoor = mouseClickY;
			         System.out.println(mouseXcoor + ", " + mouseYcoor);
			    }
			});
	    }
	    panel.revalidate();
	    panel.repaint();
	}
	
	/**
	 * A method that displays the image with the room marker.
	 * @param path
	 * 			file path of the image.
	 * @param panel
	 * 			the panel to display the image.
	 * @param x
	 * 			x coordinate of the room
	 * @param y
	 * 			y coordinate of the room
	 * @param roomDescription
	 * 			description of the room
	 */
	public static void displayImageWithMarker(String path, JPanel panel, int x, int y, String roomDescription) {
		try {
			image = ImageIO.read(new File(path));
			markerImage = ImageIO.read(new File("src/images/marker.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	    
	    panel.removeAll(); //Remove Current Image.
	    //Adds New Image.
	    ImageIcon icon = new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(image.getWidth() / 3, image.getHeight() / 3, Image.SCALE_SMOOTH));
	    JLabel label = new JLabel(icon);
	    JLabel marker = new JLabel(new ImageIcon(new ImageIcon(markerImage).getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH)));
		marker.setSize(label.getPreferredSize());
		marker.setBounds(x - 32, y - 52, 64, 64);
		marker.setToolTipText(roomDescription);
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
		label.add(marker);
	    panel.add(label);
	    
	    label.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
		         int mouseClickX = e.getX();
		         int mouseClickY = e.getY();
		         mouseXcoor = mouseClickX;
		         mouseYcoor = mouseClickY;
		         System.out.println(mouseXcoor + ", " + mouseYcoor);
		    }
		});
	    
	    panel.revalidate();
	    panel.repaint();
	}
	
	/**
	 * A method to display the image with room marker as well as the POI markers.
	 * @param path
	 * 			file path of the image.
	 * @param panel
	 * 			the panel to display the image
	 * @param x
	 * 			x coordinate of the room
	 * @param y
	 * 			y coordinate of the room
	 * @param roomDescription
	 * 			description of the room
	 * @param list
	 * 			list of POIs and their coordinates.
	 * @param poiType
	 * 			Type of POI
	 */
	public static void displayImageWithMarkerPOI(String path, JPanel panel, int x, int y, String roomDescription, ArrayList<RoomCoorPoi> list, String poiType) {
		try {
			image = ImageIO.read(new File(path));
			markerImage = ImageIO.read(new File("src/images/marker.png"));
			if(poiType.compareTo("navigation") == 0) {
				poi = ImageIO.read(new File("src/images/poi_navigation.png"));
			} else {
				poi = ImageIO.read(new File("src/images/poi_food.png"));
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	    
	    panel.removeAll(); //Remove Current Image.
	    //Adds New Image.
	    ImageIcon icon = new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(image.getWidth() / 3, image.getHeight() / 3, Image.SCALE_SMOOTH));
	    JLabel label = new JLabel(icon);
	    JLabel marker = new JLabel(new ImageIcon(new ImageIcon(markerImage).getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH)));
		marker.setSize(label.getPreferredSize());
		marker.setBounds(x - 32, y - 52, 64, 64);
		marker.setToolTipText(roomDescription);
		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
		
		for(int i = 0; i < list.size(); i++) {
			JLabel poiMarker = new JLabel(new ImageIcon(new ImageIcon(poi).getImage().getScaledInstance(46, 64, Image.SCALE_SMOOTH)));
			int poiXcoor = list.get(i).getCoor().get(0);
			int poiYcoor = list.get(i).getCoor().get(1);
			String poiDescription = list.get(i).getRoomDescription();
			String poiName = list.get(i).getRoomNum();
			poiMarker.setSize(label.getPreferredSize());
			poiMarker.setBounds(poiXcoor - 23, poiYcoor - 65, 46, 64);
			poiMarker.setToolTipText("<HTML>Name: " + poiName + "<br><br> Description: " + poiDescription);
			ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
			label.add(poiMarker);
		}
		
		label.add(marker);
	    panel.add(label);
	    
	    label.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
		         int mouseClickX = e.getX();
		         int mouseClickY = e.getY();
		         mouseXcoor = mouseClickX;
		         mouseYcoor = mouseClickY;
		         System.out.println(mouseXcoor + ", " + mouseYcoor);
		    }
		});
	    
	    panel.revalidate();
	    panel.repaint();
	}
	
	/**
	 * A method that refreshes the defaultComboBoxModel of buildings dropdown menu
	 * @param list
	 * @param buildingModel
	 */
	public void refreshModel(ArrayList<Building> list, DefaultComboBoxModel buildingModel) {
		buildingNames = new String[list.size()];
		ArrayList<String> tmp = new ArrayList<String>();
		for(int i = 0; i < list.size(); i++) {
			buildingNames[i] = list.get(i).getName();
			tmp.add(list.get(i).getName());
		}
		
		for(int i = 0; buildingModel.getElementAt(i) != null; i++) {
			if(!tmp.contains(buildingModel.getElementAt(i))) {
				buildingModel.removeElementAt(i);
			}
		}
		
		for(int i = 0; i < buildingNames.length; i++) {
			if(buildingNames[i] != buildingModel.getElementAt(i)) {
				buildingModel.addElement(buildingNames[i]);
			}
		}
	}
	
	/**
	 * A method that returns the frame of this class.
	 * @return
	 * 			frame for edit mode window.
	 */
	public final JFrame getMainFrame(){
        return frame;
    }
}