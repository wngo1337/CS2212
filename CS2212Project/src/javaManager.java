/**
 * @author Allen Zhang
 */
import java.io.File;
import java.util.Iterator;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import java.io.IOException;

public class javaManager {
	private String fileName;
	private String filePath;
	private ArrayList<Building> buildingList = new ArrayList<Building>();
	// constructor
		// input file name 
		// store file name 
	public javaManager (String fileName) {
		this.fileName = fileName;
		if (this.fileName=="building") {
			this.filePath = "src/buildings.json";
		} else if (this.fileName == "poi") {
			this.filePath = "src/poi.json";
		}
	}
	/**
	 * returnJsonObject
	 * output jsonObject
	 * return jsonObject
	 */
	public JSONObject returnJsonObject() {
		JSONObject jsonObj = null;
		try {
			if (this.filePath == null) {
				return null;
			}
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(new FileReader(this.filePath));
			jsonObj = (JSONObject) obj;
			return jsonObj;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonObj;
	}
	
	/**
	 * unpackBuilding
	 * input jsonObj
	 * output hashmap<String,hashmap<String,Arraylist<>
	 */
	public ArrayList<Building> allBuildingInfo(){
		JSONObject buildingObj = null;
		buildingObj = returnJsonObject();
		// iterate through building json file 
		for(Iterator iterator = buildingObj.keySet().iterator(); iterator.hasNext();) {
			// init a Building object
			Building building = new Building();
			// get building name
			String buildingName = (String) iterator.next();

			JSONObject buildingInfo = (JSONObject) buildingObj.get(buildingName);
			
			// iterate through each floor 
			for(Iterator iterFloor = buildingInfo.keySet().iterator(); iterFloor.hasNext();) {
				// get building name 
				// store building name
				String name = (String) iterFloor.next();
				if(name.compareTo("name") == 0) {
					String actualName = (String) buildingInfo.get(name);
					building.addName(actualName);
				}
			}

			// iterate throug floor to get each room number and its coordinates
			JSONObject floorRooms = (JSONObject) buildingInfo.get("floors");
			for(Iterator iterRooms = floorRooms.keySet().iterator(); iterRooms.hasNext();) {
				// add floor number to building 
				// get floor number
				// store floor number
				String floorNum = (String) iterRooms.next();
				building.addFloorNum(floorNum);
				
				// get all the rooms from a specific floor
				JSONObject roomNumbers = (JSONObject) floorRooms.get(floorNum);
				for(Iterator iterCoor = roomNumbers.keySet().iterator(); iterCoor.hasNext();) {
					String roomNum = (String) iterCoor.next();
					if(roomNum.compareTo("path") == 0) {
						// add path
						String path = (String) roomNumbers.get(roomNum);
						building.addPath(floorNum, path);
					} else {
						// get room number
						// add room number to building
						building.addRoom(floorNum, roomNum);
						JSONArray coor = (JSONArray) roomNumbers.get(roomNum);
						building.addRoomCoor(roomNum, (int) (long) coor.get(0), (int) (long) coor.get(1));
						building.addRoomDescription(roomNum, (String)coor.get(2));
					}
				}
			}
			this.buildingList.add(building);
		}
		return this.buildingList;
	}
	/**
	 * 
	 * @param buildingName: a building name 
	 * @return arraylist of building
	 */
	
	public ArrayList<Building> removeBuilding(ArrayList<Building> buildingList, String buildingName){ 
		int l = buildingList.size();
		System.out.println(l);
		for (int i =0; i<l; i++){
			if (buildingList.get(i).getName().equals(buildingName)){
				buildingList.remove(i);
				return buildingList;
			}
		}
		return buildingList;
	}
	/**
	 * read poi.json file 
	 * input: none
	 * output: poi
	*/
	public Poi readPoi(){
		JSONParser parser = new JSONParser();
		Object obj = null;
		Poi allPoi = new Poi();
		try {
			obj = parser.parse(new FileReader("src/poi.json"));
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject jsonObj = (JSONObject) obj;
		for(Iterator iterator = jsonObj.keySet().iterator(); iterator.hasNext();) {
			// POI type
			String poiType = (String) iterator.next();
			JSONObject poiRoomList = (JSONObject) jsonObj.get(poiType);
			
			// iterate through each floor 
			for(Iterator iter = poiRoomList.keySet().iterator(); iter.hasNext();) {
				// get building name 
				// store building name
				// key 
				String name = (String) iter.next();
				// array -> building, floor, room
				String[] arrOfStr = name.split(","); 
				JSONObject tmp = (JSONObject)poiRoomList.get(name);
				// description and coordinates
				String info = (String) tmp.get("description");
				JSONArray coor = (JSONArray) tmp.get("coor");
				allPoi.addRoom(poiType, arrOfStr[0], arrOfStr[1], arrOfStr[2], (int)(long)coor.get(0), (int)(long)coor.get(1), info);
			}
		}
		return allPoi;
	}
	
	/**
	 * write to building.json
	 * @return void
	 */
	public void writingToBuilding(ArrayList<Building> buildingList){
		// opoen file

		// iterate through building
		// create JSONObject and add building element to json object 
		// write to the file 
		try (FileWriter file = new FileWriter("src/buildings.json")){
			JSONObject buildings = new JSONObject();
			for (int i = 0; i < buildingList.size();i++){
				JSONObject building = new JSONObject();
				building.put("name", buildingList.get(i).getName());
				// create json object floors
				JSONObject floors = new JSONObject();
				ArrayList<String> allFloors = buildingList.get(i).getFloorNum();
				// iterate thorugh floor to room
				for (int j = 0; j<allFloors.size();j++){
					// new json object floor 
					// add image path
					JSONObject floor = new JSONObject();
					String floorNum = allFloors.get(j).toString();
					floor.put("path", buildingList.get(i).getImagePath(floorNum));
					// iterate through room 
					String tmp = buildingList.get(i).getFloorNum().get(j);
					for (int k=0;k< buildingList.get(i).getFloorToRomm(tmp).size();k++){
						// new json array for each room
						
						JSONArray room = new JSONArray();
						// add coordinates and description
						ArrayList<String> roomList = buildingList.get(i).getFloorToRomm(tmp);
						room.add(buildingList.get(i).getRoomCoor(roomList.get(k)).get(0));
						room.add(buildingList.get(i).getRoomCoor(roomList.get(k)).get(1));
						room.add(buildingList.get(i).getRoomInfo(roomList.get(k)));
						// add room to floor 
						floor.put(roomList.get(k),room);
					}
					// add floor number
					// add floor to floors 
					floors.put(tmp,floor);
				}
			building.put("floors",floors);
			buildings.put(buildingList.get(i).getName(),building);
			}
		//Write JSON file
		file.write(buildings.toJSONString());
		// close file 
		file.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * write to poi.json
	 * input: poi
	 * output: void
	 */
	public void writeToPoi(Poi poi){
		// try to open the file 
		try (FileWriter file = new FileWriter("src/poi.json")){
			// new json object for all pois
			JSONObject pois = new JSONObject();
			ArrayList<String> allTypes = poi.getTypes();
			// loop through all types of pois
			for (int i =0;i<allTypes.size();i++){
				// new json object for that type
				JSONObject type = new JSONObject();
				ArrayList<BuildingFloorPoi> BuildingFloorRooms = poi.getAllRooms(allTypes.get(i));
				// loop through all rooms from that type
				for (int j =0;j<BuildingFloorRooms.size();j++){
					// new json object for each room
					String key = BuildingFloorRooms.get(j).getBuilding() + "," + BuildingFloorRooms.get(j).getFloorNum(); 
					ArrayList<RoomCoorPoi> rooms = BuildingFloorRooms.get(j).getAllRooms();
					for (int k=0;k<rooms.size();k++){
						JSONObject item = new JSONObject();
						// get key 
						// get coordinates
						// get description
						String finalKey = key + "," + rooms.get(k).getRoomNum();
						JSONArray coor = new JSONArray();
						coor.add(rooms.get(k).getCoor().get(0));
						coor.add(rooms.get(k).getCoor().get(1));
						item.put("coor",coor);
						String description = rooms.get(k).getRoomInfo(rooms.get(k).getRoomNum());
						item.put("description",description);
						// add to type
						type.put(finalKey,item);
					}
				}
				pois.put(allTypes.get(i),type);	
				// add name and type to pois
			}


			//Write JSON file
			file.write(pois.toJSONString());
			// close file 
			file.flush();
	
		} catch (IOException e) {
				e.printStackTrace();
		}
	}
}