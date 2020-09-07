/**
 * Allen Zhang
 */
import java.util.ArrayList;
import java.util.HashMap;

public class Building {
	/**
	 * name:
	 * path:
	 * floorNum -> hashMap -> rooms
	 * constructor -> name,path,floors-rooms
	 * 
	 */
	private String name;
	private ArrayList<String> floorNum;
	private HashMap<String,String> floorToImage;
	private HashMap<String,ArrayList<String>> floorWithRoom;
	private HashMap<String,ArrayList<Integer>> roomToCoor;
	private HashMap<String, String> roomInfo;
	
	// constructor 
		// init global values
	public Building(){
		this.name = "";
		this.floorNum = new ArrayList<String>();
		this.floorToImage = new HashMap<String,String>();
		this.floorWithRoom = new HashMap<String,ArrayList<String>>();
		this.roomToCoor = new HashMap<String,ArrayList<Integer>>();
		this.roomInfo = new HashMap<String, String>();
	}

	/**
	 * addName
	 * input string
	 * output bool
	 * 
	 */
	public boolean addName(String name){
		this.name = name;
		if (this.name == null){
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @return name of building
	 */
	public String getName(){
		return this.name;
	}
	/**
	 * addPath
	 * input: string
	 * output: boolean
	 * add image path to this.path
	 */
	public boolean addPath(String floor,String path){
		if (floor == null || path == null) {
			return false;
		}
		this.floorToImage.put(floor, path);
		return true;
	}

	/**
	 *
	 * @return image path
	 */
	public String getImagePath(String FloorNum) {
		if(this.floorToImage.containsKey(FloorNum)) {
			return this.floorToImage.get(FloorNum);
		} else {
			return null;
		}
	}

	/**
	 * addFloorNum
	 * input string
	 * output void
	 * add floor num to floorNum
	 */
	public void addFloorNum(String floorNum){
		if (floorNum != null){
			this.floorNum.add(floorNum);
			if (!this.floorWithRoom.containsKey(floorNum)){
				this.floorWithRoom.put(floorNum, new ArrayList<String>());
			}
		}
	}
	
	/**
	 * add room description
	 * input: roomNum and description
	 * output: boolean
	 * @return true -> add successfully fale -> not succeed
	 */
	public void addRoomDescription(String roomNum, String description){
		if (!this.roomInfo.containsKey(roomNum)){
			this.roomInfo.put(roomNum, description);
		} else {
			this.updateRoomInfo(roomNum, description);
		}
	}
	/**
	 * get room description
	 * input: roomNum
	 * output: string
	 * @return room description
	 */
	public String getRoomInfo(String roomNum){
		if (this.roomInfo.containsKey(roomNum)){
			return this.roomInfo.get(roomNum);
		} else {
			return null;
		}
	}
	/**
	 * remove room descrition
	 * input roomNum
	 * output: bool
	 * @return true -> success false -> not succeed
	 */
	public boolean removeRoomInfo(String roomNum){
		if (this.roomInfo.containsKey(roomNum)){
			this.roomInfo.remove(roomNum);
			return true;
		} else {
			return false;
		}
	}
	/**
	 * update room descrition
	 * input roomNum and description
	 * output: boolean 
	 * @return
	 */
	public boolean updateRoomInfo(String roomNum,String description){
		if (this.roomInfo.containsKey(roomNum)){
			this.roomInfo.replace(roomNum, description);
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 
	 * @return all the floor numbers
	 */
	public ArrayList<String> getFloorNum(){
		return this.floorNum;
	}
	
	/**
	 * removeFloor
	 * remove a floor from the building
	 * input: buildingName, floorNum
	 * output: updated floorNum
	 */
	public void removeFloor(String floorNum){
		if (this.floorNum.contains(floorNum)){
			this.floorNum.remove(floorNum);
			this.floorToImage.remove(floorNum);
			this.floorWithRoom.remove(floorNum);
		}
	}

	/**
	 * addRoom
	 * input string floorNum, roomNum
	 * output void
	 * add roomNum to floorNum
	 */
	public void addRoom(String floorNum, String roomNum){
		if (floorNum == null || roomNum == null){
			return;
		}
		if (this.floorWithRoom.containsKey(floorNum)){
			this.addRoomDescription(roomNum, null);
			this.floorWithRoom.get(floorNum).add(roomNum);
		} else {
			this.floorWithRoom.put(floorNum, new ArrayList<String>());
			this.floorWithRoom.get(floorNum).add(roomNum);
			this.addRoomDescription(roomNum, null);
		}
		if (!this.roomToCoor.containsKey(roomNum)){
			this.roomToCoor.put(roomNum, new ArrayList<Integer>());
		}
	}

	/**
	 * 
	 * @return hashmap of floor to room
	 */

	public ArrayList<String> getFloorToRomm(String floor){
		if (this.floorWithRoom.containsKey(floor)){
			return this.floorWithRoom.get(floor);
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param roomNum: room number
	 * @param x: x coordinate
	 * @param y: y coordinate
	 * add coordinate to related room 
	 */
	public void addRoomCoor(String roomNum, int x, int y){
		if (!this.roomToCoor.containsKey(roomNum)){
			this.addRoomDescription(roomNum, null);
			this.roomToCoor.put(roomNum, new ArrayList<Integer>());
		}
		this.roomToCoor.get(roomNum).add(x);
		this.roomToCoor.get(roomNum).add(y);
	}

	/**
	 * 
	 * @param roomNum room number
	 * @return coordinates of the room 
	 */
	public ArrayList<Integer> getRoomCoor(String roomNum){
		if (this.roomToCoor.containsKey(roomNum)){
			return this.roomToCoor.get(roomNum);
		} else {
			return null;
		}
	}
	
	/**
	 * removeRoom
	 * remove room from the floor
	 * input floorNum & roomNum
	 * output updated roomToCoor
	 */
	public void removeRoom(String floor, String room){
		if (this.floorWithRoom.containsKey(floor)){
			this.floorWithRoom.get(floor).remove(room);
			this.removeRoomInfo(room);
			this.roomToCoor.remove(room);
		}
	}

}