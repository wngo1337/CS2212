/**
 * @author PengJu Chang (pchang32)
 */
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ToolTipManager;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

public class UImain {

	private JFrame frame; //The frame that will be the main applicationw window.
	private UIedit newWindow; //the window of the editing mode.
	private JLabel label = null; //A JLabel to store the map image.
	private static BufferedImage image = null; //Map image.
	private static BufferedImage markerImage = null; //Roomo marker image.
	private static BufferedImage poi = null; //POI marker image.
	private String inputImage = ""; //Map image path.
	private JScrollPane scrollPane; //Scroll panel created for scrollable map.
	private int iCopy = -1; //Initialize the index of building selection to -1.
	private int kCopy =  -1; //Initialize the index of floor selection to -1.
	private int roomSelection = -1; //Initialize the index of room selection to -1.
	private int favouriteRoom = -1; //Initialize the index of favourite room selection to -1.
	private String[] buildingNames; //Array that stores all the names of the buildings.
	private String[] floorNames; //Array that stores all the names of the floors in a specific building.
	private String[] roomNames; //Array that stores all the names of the roooms in a specific floor.
	private String[] favouriteArray; //Array that stores the names of the favourite rooms.
	private ArrayList<String> favouriteList; //Arraylist of favourite room names.
	private String roomDescription; //Rooms description/
	private int xCoor = 0; //X coordinates of the room marker.
	private int yCoor = 0; //Y coordinate of the room marker.
	private JLabel buildingTick = null; //Label to store the image of a green tick to signify the building selection.
	private JLabel floorTick = null; //Label to store the image of a green tick to signify the floor selection.
	private JLabel roomTick = null; //Label to store the image of a green tick to signify the room selection.
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UImain window = new UImain(); //Creates the UImain object
					window.frame.setVisible(true); //Sets the window to be visible.
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public UImain() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//Sets the default settings for the frame.
		frame = new JFrame("The Awesome Map Of Western - Main");
		frame.setBounds(0, 0, 1280, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setBackground(Color.WHITE);
		frame.setResizable(false);
		
		//Creates the javaManager object to get the ArrayList of buildings objects.
		javaManager newManager = new javaManager("building");
		ArrayList<Building> allBuilding = newManager.allBuildingInfo();
		//Stores all the buildings names in to an array.
		buildingNames = new String[allBuilding.size()];
		for(int i = 0; i < allBuilding.size(); i++) {
			buildingNames[i] = allBuilding.get(i).getName();
		}
		
		//Creates the javaManager object to read in all the POI information from the poi.json file in to the Poi object allPoi.
		javaManager poiManager = new javaManager("poi");
		Poi allPoi = poiManager.readPoi();
		
		//Creates the Favourites object to reading all the favourite rooms data from the json file and store all names in to an array.
		Favourites favouriteManager = new Favourites();
		favouriteList = favouriteManager.getFavourites();
		favouriteArray = new String[favouriteList.size()];
		for(int i =  0; i < favouriteList.size(); i++) {
			favouriteArray[i] = favouriteList.get(i).toString();
		}
		
		//Edit mode button.
		JButton buttonEditMode = new JButton("Edit Mode");
		buttonEditMode.setBounds(46, 608, 150, 50);
		frame.getContentPane().add(buttonEditMode);
		
		//Sets the default combobox model for both buildings and favourites.
		DefaultComboBoxModel buildingModel = new DefaultComboBoxModel(buildingNames);
		DefaultComboBoxModel favouriteModel = new DefaultComboBoxModel(favouriteArray);
		
		//Favourites dropdown menu.
		JComboBox favourites = new JComboBox(favouriteModel);
		favourites.setBounds(46, 406, 150, 40);
		frame.getContentPane().add(favourites);
		
		//Buildings dropdown menu.
		JComboBox selectBuilding = new JComboBox(buildingModel);
		selectBuilding.setBounds(46, 60, 150, 40);
		frame.getContentPane().add(selectBuilding);
		
		//Sets what the program does when the "edit mode" button has been clicked on.
		buttonEditMode.addActionListener(new ActionListener() {
		       public void actionPerformed(ActionEvent e) {
		    	   //opens the edit mode window and pass in the appropriate variables/data sturctures.
		    	   newWindow = new UIedit(allBuilding, allPoi, favouriteManager, favouriteList);
		    	   //Sets the current window to be invisible.
		    	   frame.setVisible(false);
		    	   System.out.println(newWindow.getMainFrame().isDisplayable());
		    	   
		    	   //What happens when the edit mode window is closed.
		    	   newWindow.getMainFrame().addWindowListener(new java.awt.event.WindowAdapter() {
		    		   public void windowClosed(java.awt.event.WindowEvent windowEvent) {
		    			   //Sets the current window to be visible.
		    			   frame.setVisible(true);
		    			   
		    			   //Refreshes the building dropdown menu.
		    			   buildingNames = new String[allBuilding.size()];
		    			   for(int i =  0; i < allBuilding.size(); i++) {
		    				   buildingNames[i] = allBuilding.get(i).getName();
		    			   }
		    			   selectBuilding.setModel(new DefaultComboBoxModel(buildingNames));
		    			   
		    			   //Refreshes the favourites dropdown menu.
		    			   favouriteArray = new String[favouriteList.size()];
		    			   for(int i =  0; i < favouriteList.size(); i++) {
		    				   favouriteArray[i] = favouriteList.get(i).toString();
		    			   }
		    			   favourites.setModel(new DefaultComboBoxModel(favouriteArray));
							
		    			   System.out.println(newWindow.getMainFrame().isDisplayable());
		    		   }
		    	   });
		      }
		});		
		
		//Initializes the scrollpanel for the map image
		scrollPane = new JScrollPane();
		scrollPane.setBounds(245, 11, 1010, 659);
		frame.getContentPane().add(scrollPane);
		JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
		label = new JLabel();
        scrollPane.setViewportView(panel);
        //Calls the displayImage method to display the default image: campus map.
        displayImage("src/images/Campus Map.jpg", panel);
		
        //Select floor dropdown menu
		JComboBox selectFloor = new JComboBox();
		selectFloor.setBounds(46, 151, 150, 40);
		frame.getContentPane().add(selectFloor);
		
		//Select room dorpdown menu
		JComboBox selectRoom = new JComboBox();
		selectRoom.setBounds(46, 239, 150, 40);
		frame.getContentPane().add(selectRoom);
		
		//Check box for washroom POI
		JCheckBox chckbxwashroom = new JCheckBox("Washroom");
		chckbxwashroom.setBounds(46, 509, 150, 23);
		frame.getContentPane().add(chckbxwashroom);
		
		//Check box for accessibility POI
		JCheckBox chckbxAccessibility = new JCheckBox("Accessibility");
		chckbxAccessibility.setBounds(46, 483, 150, 23);
		frame.getContentPane().add(chckbxAccessibility);
		
		//Check box for food and shopping POI
		JCheckBox chckbxFoodShopping = new JCheckBox("Food & Shopping");
		chckbxFoodShopping.setBounds(46, 561, 150, 23);
		frame.getContentPane().add(chckbxFoodShopping);
		
		//Check box for navigations POI
		JCheckBox chckbxNavigation = new JCheckBox("Navigation");
		chckbxNavigation.setBounds(46, 535, 150, 23);
		frame.getContentPane().add(chckbxNavigation);
		
		//Check box for adding and removing favourites.
		JCheckBox checkFavourites = new JCheckBox("Toggle Favourites");
		checkFavourites.setBounds(46, 352, 150, 23);
		frame.getContentPane().add(checkFavourites);
		
		//Food and shopping POI check box is defaulted to be greyed out.
		chckbxFoodShopping.setEnabled(false);
		chckbxFoodShopping.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JCheckBox cbsource = (JCheckBox) e.getSource();
				//If this check box is checked.
				if (cbsource.isSelected()) {
					chckbxNavigation.setEnabled(false); //grey out navigation check box
					//Displays POI with room marker since the room is selected.
					if(roomSelection != -1) {
						String buildingName = allBuilding.get(iCopy).getName();
						String floorNum = allBuilding.get(iCopy).getFloorNum().get(kCopy);
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
							} else {
								ArrayList<RoomCoorPoi> roomCoor = allPoi.returnAll().get("food and shopping").get(index).getAllRooms();
								displayImageWithMarkerPOI(inputImage, panel, xCoor, yCoor, roomDescription, roomCoor, "food and shopping");
								roomSelection = -2;
							}
						} else {
							new ErrorMessageWindow("nofoodshooping");
							chckbxFoodShopping.setSelected(false);
							chckbxNavigation.setEnabled(true);
						}
					//Room has not been selected, so display POI on map without room marker.
					} else {
						String buildingName = allBuilding.get(iCopy).getName();
						String floorNum = allBuilding.get(iCopy).getFloorNum().get(kCopy);
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
							} else {
								ArrayList<RoomCoorPoi> roomCoor = allPoi.returnAll().get("food and shopping").get(index).getAllRooms();
								displayImageWithPOI(inputImage, panel, roomCoor, "food and shopping");
							}
						} else {
							new ErrorMessageWindow("nofoodshopping");
							chckbxFoodShopping.setSelected(false);
							chckbxNavigation.setEnabled(true);
						}
					}
				//If the check box is not checked.
				} else {
					chckbxNavigation.setEnabled(true);
					if(roomSelection != -1) {
						displayImageWithMarker(inputImage, panel, xCoor, yCoor, roomDescription);
						roomSelection = -2;
					} else {
						displayImage(inputImage, panel);
					}
				}
			}
		});
		
		//Defaults the navigation check box to be greyed out
		chckbxNavigation.setEnabled(false);
		chckbxNavigation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JCheckBox cbsource = (JCheckBox) e.getSource();
				if (cbsource.isSelected()) {
					chckbxFoodShopping.setEnabled(false); //Grey out the food and shopping check box.
					//Displays POI with room marker since the room is selected.
					if(roomSelection != -1) {
						String buildingName = allBuilding.get(iCopy).getName();
						String floorNum = allBuilding.get(iCopy).getFloorNum().get(kCopy);
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
							} else {
								ArrayList<RoomCoorPoi> roomCoor = allPoi.returnAll().get("navigation").get(index).getAllRooms();
								displayImageWithMarkerPOI(inputImage, panel, xCoor, yCoor, roomDescription, roomCoor, "navigation");
								roomSelection = -2;
							}
						} else {
							new ErrorMessageWindow("nonavigation");
							chckbxNavigation.setSelected(false);
							chckbxFoodShopping.setEnabled(true);
						}
					//Room has not been selected, so display POI on map without room marker.
					} else {
						String buildingName = allBuilding.get(iCopy).getName();
						String floorNum = allBuilding.get(iCopy).getFloorNum().get(kCopy);
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
							} else {
								ArrayList<RoomCoorPoi> roomCoor = allPoi.returnAll().get("navigation").get(index).getAllRooms();
								displayImageWithPOI(inputImage, panel, roomCoor, "navigation");
							}
						} else {
							new ErrorMessageWindow("nonavigation");
							chckbxNavigation.setSelected(false);
							chckbxFoodShopping.setEnabled(true);
						}
					}
				//Check box is not checked.
				} else {
					chckbxFoodShopping.setEnabled(true);
					if(roomSelection != -1) {
						displayImageWithMarker(inputImage, panel, xCoor, yCoor, roomDescription);
						roomSelection = -2;
					} else {
						displayImage(inputImage, panel);
					}
				}
			}
		});
		
		//Default favourite check box to be greyed out.
		checkFavourites.setEnabled(false);
		checkFavourites.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JCheckBox cbsource = (JCheckBox) e.getSource();
				//If is checked.
				if (cbsource.isSelected()) {
					if(iCopy != -1 && kCopy != -1 && roomSelection != -1) {
						String favouriteBuildingName = allBuilding.get(iCopy).getName();
						String favouriteFloorName = allBuilding.get(iCopy).getFloorNum().get(kCopy);
						String favouriteRoomName = allBuilding.get(iCopy).getFloorToRomm(favouriteFloorName).get(favouriteRoom);
						favouriteManager.addFavourite(favouriteBuildingName, favouriteFloorName, favouriteRoomName);
						
						//Adds the current selected room to the list of favourites.
						favouriteList.add(favouriteBuildingName + "," + favouriteFloorName + "," + favouriteRoomName);
						//Refreshes the favourites dropdown menu.
						favouriteArray = new String[favouriteList.size()];
						for(int i =  0; i < favouriteList.size(); i++) {
							favouriteArray[i] = favouriteList.get(i).toString();
						}
						favourites.setModel(new DefaultComboBoxModel(favouriteArray));
					}
				//If not checked.
				} else {
					if(iCopy != -1 && kCopy != -1 && roomSelection != -1) {
						String favouriteBuildingName = allBuilding.get(iCopy).getName();
						String favouriteFloorName = allBuilding.get(iCopy).getFloorNum().get(kCopy);
						String favouriteRoomName = allBuilding.get(iCopy).getFloorToRomm(favouriteFloorName).get(favouriteRoom);	
						favouriteManager.removeFavourite(favouriteBuildingName, favouriteFloorName, favouriteRoomName);
						
						//Removes the current selected room from the list of favourites
						favouriteList.remove(favouriteBuildingName + "," + favouriteFloorName + "," + favouriteRoomName);
						//Refreshes the favourites dropdown menu.
						favouriteArray = new String[favouriteList.size()];
						for(int i =  0; i < favouriteList.size(); i++) {
							favouriteArray[i] = favouriteList.get(i).toString();
						}
						favourites.setModel(new DefaultComboBoxModel(favouriteArray));
					}
				}
			}
		});
		
		//Adds a tooptip to each item in the favourites dropdown menu
		favourites.setRenderer(new favouritesTooltipRenderer());
		favourites.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//Sets other check boxes to the appropriate states.
				buildingTick.setVisible(false);
				floorTick.setVisible(false);
				roomTick.setVisible(false);
				checkFavourites.setEnabled(true);
				checkFavourites.setSelected(true);
				chckbxNavigation.setEnabled(true);
				chckbxNavigation.setSelected(false);
				chckbxFoodShopping.setEnabled(true);
				chckbxFoodShopping.setSelected(false);
				
				//Gets the building index, floor index and room index.
				String selectedFavourite = favourites.getSelectedItem().toString();
				String[] splitted = selectedFavourite.split(",");
				for(int i = 0; i < allBuilding.size(); i++) {
					if(splitted[0].compareTo(allBuilding.get(i).getName()) == 0) {
						iCopy = i;
						break;
					}
				}
				for(int i = 0; i < allBuilding.get(iCopy).getFloorNum().size(); i++) {
					if(splitted[1].compareTo(allBuilding.get(iCopy).getFloorNum().get(i)) == 0) {
						kCopy = i;
						break;
					}
				}
				for(int i = 0; i < allBuilding.get(iCopy).getFloorToRomm(splitted[1]).size(); i++) {
					if(splitted[2].compareTo(allBuilding.get(iCopy).getFloorToRomm(splitted[1]).get(i)) == 0) {
						roomSelection = i;
						break;
					}
				}
				
				favouriteRoom = roomSelection;
				xCoor = allBuilding.get(iCopy).getRoomCoor(splitted[2]).get(0);
				yCoor = allBuilding.get(iCopy).getRoomCoor(splitted[2]).get(1);
				inputImage = allBuilding.get(iCopy).getImagePath(splitted[1]);
				roomDescription = allBuilding.get(iCopy).getRoomInfo(splitted[2]);
				
				//Calls the displayImageWithMarker function to display the room marker and map.
				displayImageWithMarker(inputImage, panel, xCoor, yCoor, roomDescription);
			}
		});
		
		//Opens the images of the green tick.
		BufferedImage tick = null;
		try {
			tick = ImageIO.read(new File("src/images/tick.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		//Adds the images to JLabels and set location.
		buildingTick = new JLabel(new ImageIcon(new ImageIcon(tick).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
		buildingTick.setBounds(200, 60, 30, 30);
		frame.getContentPane().add(buildingTick);
		floorTick = new JLabel(new ImageIcon(new ImageIcon(tick).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
		floorTick.setBounds(200, 151, 30, 30);
		frame.getContentPane().add(floorTick);
		roomTick = new JLabel(new ImageIcon(new ImageIcon(tick).getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH)));
		roomTick.setBounds(200, 239, 30, 30);
		frame.getContentPane().add(roomTick);
		buildingTick.setVisible(false);
		floorTick.setVisible(false);
		roomTick.setVisible(false);
		
		//Sets what happens when an item from the buildings drop down menu is selected.
		selectBuilding.setSelectedIndex(1);
		selectBuilding.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buildingTick.setVisible(true);
				floorTick.setVisible(false);
				roomTick.setVisible(false);
				
				chckbxFoodShopping.setSelected(false);
				chckbxNavigation.setSelected(false);
				chckbxNavigation.setEnabled(false);
				chckbxFoodShopping.setEnabled(false);
				checkFavourites.setEnabled(false);
				checkFavourites.setSelected(false);
				int i = selectBuilding.getSelectedIndex();
				iCopy = i;
				//Populates the floor dropdown menu using defaultComboBoxModel.
				floorNames = new String[allBuilding.get(i).getFloorNum().size()];
				for(int j = 0; j < allBuilding.get(i).getFloorNum().size(); j++) {
					floorNames[j] = allBuilding.get(i).getFloorNum().get(j);
				}
				selectFloor.setModel(new DefaultComboBoxModel(floorNames));
			}
		});
		
        //Sets what happens when an item from the floor dropdown menu is selected.
		selectFloor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buildingTick.setVisible(true);
				floorTick.setVisible(true);
				roomTick.setVisible(false);
				
				chckbxFoodShopping.setSelected(false);
				chckbxNavigation.setSelected(false);
				checkFavourites.setEnabled(false);
				checkFavourites.setSelected(false);
				xCoor = 0;
				yCoor = 0;
				int k = selectFloor.getSelectedIndex();
				kCopy = k;
				String floor = floorNames[k];
				//Populates the rooms dropdown menu.
				roomNames = new String[allBuilding.get(iCopy).getFloorToRomm(floor).size()];
				for(int l = 0; l < allBuilding.get(iCopy).getFloorToRomm(floor).size(); l++) {
					roomNames[l] = allBuilding.get(iCopy).getFloorToRomm(floor).get(l);
				}
				selectRoom.setModel(new DefaultComboBoxModel(roomNames));
				
				//Sets the floor map image path and display the image.
				inputImage = allBuilding.get(iCopy).getImagePath(floor);
				displayImage(inputImage, panel);
				if(kCopy != -1) {
					chckbxNavigation.setEnabled(true);
					chckbxFoodShopping.setEnabled(true);
				}
			}
		});
		
		//Sets what happens when an item from the room dropdown menu is selected.
		selectRoom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buildingTick.setVisible(true);
				floorTick.setVisible(true);
				roomTick.setVisible(true);
				
				chckbxFoodShopping.setSelected(false);
				chckbxNavigation.setSelected(false);
				int i = selectRoom.getSelectedIndex();
				String floor = floorNames[kCopy];
				String room = roomNames[i];
				roomSelection = i;
				favouriteRoom = roomSelection;
				xCoor = (int) allBuilding.get(iCopy).getRoomCoor(room).get(0);
				yCoor = (int) allBuilding.get(iCopy).getRoomCoor(room).get(1);
				roomDescription = allBuilding.get(iCopy).getRoomInfo(room);
				
				inputImage = allBuilding.get(iCopy).getImagePath(floor);
				//Displays the marker on the map by calling the displayImageWithMarker method.
				displayImageWithMarker(inputImage, panel, xCoor, yCoor, roomDescription);
				if(kCopy != -1) {
					chckbxNavigation.setEnabled(true);
					chckbxFoodShopping.setEnabled(true);
					checkFavourites.setEnabled(true);
				}
				//If this room is a favourite room, check the favourites check box.
				for(int index = 0; index < favouriteList.size(); index++) {
					String favouritebuildingFloorRoomCheck = allBuilding.get(iCopy).getName() + "," + floorNames[kCopy] + "," + roomNames[i];
					if(favouritebuildingFloorRoomCheck.compareTo(favouriteList.get(index)) == 0) {
						checkFavourites.setSelected(true);
						break;
					} else {
						checkFavourites.setSelected(false);
					}
				}
			}
		});
		
		//Labels for dropdown menus and check boxes.
		JLabel labelFavourites = new JLabel("Favourites");
		labelFavourites.setBounds(46, 380, 150, 15);
		frame.getContentPane().add(labelFavourites);
		
		JLabel labelSelectBuilding = new JLabel("Select Building");
		labelSelectBuilding.setBounds(46, 34, 150, 15);
		frame.getContentPane().add(labelSelectBuilding);
		
		JLabel labelSelectFloor= new JLabel("Select Floor");
		labelSelectFloor.setBounds(46, 125, 150, 15);
		frame.getContentPane().add(labelSelectFloor);
		
		JLabel labelSelectRoom = new JLabel("Select Room");
		labelSelectRoom.setBounds(46, 213, 150, 15);
		frame.getContentPane().add(labelSelectRoom);
		
		JLabel lblNoteReselectThe = new JLabel("<HTML>NOTE: Reselect the building<br>after editing!");
		lblNoteReselectThe.setBounds(46, 290, 199, 35);
		frame.getContentPane().add(lblNoteReselectThe);
		
		//Creates the help button.
		JButton buttonHelp = new JButton();
		try {
		    Image helpIcon = ImageIO.read(new File("src/images/help.png"));
		    buttonHelp.setIcon(new ImageIcon(new ImageIcon(helpIcon).getImage().getScaledInstance(23, 23, Image.SCALE_SMOOTH)));
		} catch (Exception ex) {
		    System.out.println(ex);
		}
		//Opens the main window help text.
		buttonHelp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new MainWindowHelp();
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
		//Opening the image file.
		try {
			image = ImageIO.read(new File(path));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	    
		//Remove previously displayed image
	    panel.removeAll();
	    //Adds and displays new image.
	    if(path.compareTo("src/images/Campus Map.jpg") == 0) {
	    	panel.add(new JLabel(new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(1080, 1400, Image.SCALE_SMOOTH))));
	    } else {
	    	panel.add(new JLabel(new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(image.getWidth() / 3, image.getHeight() / 3, Image.SCALE_SMOOTH))));
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
		//Opens the map images and the POI marker image
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
	    
		//Remove previously displayed image
	    panel.removeAll();
	    //Adds and displays new image with POI markers.
	    if(path.compareTo("src/images/Campus Map.jpg") == 0) {
	    	panel.add(new JLabel(new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(1080, 1400, Image.SCALE_SMOOTH))));
	    } else {
	    	JLabel label = new JLabel(new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(image.getWidth() / 3, image.getHeight() / 3, Image.SCALE_SMOOTH)));
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
	    	panel.add(label);
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
	 * A method to add tooltips to the dropdown menu for favourites.
	 */
	 class favouritesTooltipRenderer extends BasicComboBoxRenderer {
		 public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			 if (isSelected) {
				 setBackground(list.getSelectionBackground());
				 setForeground(list.getSelectionForeground());
				 if (-1 < index) {
					 list.setToolTipText(favouriteList.get(index).toString());
				 }
		     } else {
		    	 setBackground(list.getBackground());
		    	 setForeground(list.getForeground());
		     }
		     setFont(list.getFont());
		     setText((value == null) ? "" : value.toString());
		     return this;
		 }
	 }
}