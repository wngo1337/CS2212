/**
 * @author Allen Zhang
 */
import java.util.ArrayList;

public class BuildingFloorPoi{
    
    /**
     * global vars
     * arraylist of roomCoor
     * key: BuildingFloor
     * String building name
     * Strring floor num
     */
    private String key;
    private String buildingName;
    private String floorNum;
    private ArrayList<RoomCoorPoi> rooms;

    public BuildingFloorPoi(){
        this.key = null;
        this.buildingName = null;
        this.floorNum = null;
        this.rooms = new ArrayList<RoomCoorPoi>();
    }
    /**
     * add key
     * input key
     * output void
     */
    public void addKey(String key){
        this.key = key;
    }
    /**
     * get key
    */
    public String getKey(){
        return this.key;
    }
    /**
     * add building name
     */
    public void addBuilding(String building){
        this.buildingName = building;
    }
    /**
     * get building name
     */
    public String getBuilding(){
        return this.buildingName;
    }

    /**
     * get floor number
     */
    public String getFloorNum(){
        return this.floorNum;
    }
    /**
     * add floor number
    */
    public void addFloor(String floorNum){
        this.floorNum = floorNum;
    }
    /**
     * add room
     */
    public void addRoom(String roomNum, int x, int y, String description){
        RoomCoorPoi newRoom = new RoomCoorPoi();
        newRoom.addRoomCoor(roomNum, x, y);
        newRoom.addRoomInfo(roomNum, description);
        this.rooms.add(newRoom);
    }

    /**
     * get room
     */
    public ArrayList<RoomCoorPoi> getAllRooms(){
        return this.rooms;
    }
    /**
     * remove room
     */
    public void removeRoom(String room){
        for (int i =0;i<this.rooms.size();i++){
            if (this.rooms.get(i).getRoomNum().equals(room)){
                this.rooms.remove(i);
            }
        }
    }
}