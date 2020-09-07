/**
 * @author Allen Zhang
 */
import java.util.ArrayList;
import java.util.HashMap;

public class Poi {
    /**
     * global variables
     */

    // key poi type value -> buildingFloor
    private HashMap <String,ArrayList<BuildingFloorPoi>> poiType;

    public Poi(){
        this.poiType = new HashMap <String,ArrayList<BuildingFloorPoi>>();
    }

    /**
     * add a room
     * input: building & floor & roomNum & coor
     * output: void
     */
    public void addRoom(String poiType, String building, String floor, String roomNum, int x, int y, String info){
        /**
         * form key with building and floor
         * if key exists -> add a room
         * else add new key then add room
         */
        String key = building + floor;
        boolean exist = false;


        if (this.poiType.containsKey(poiType)){
            for (int i =0; i<this.poiType.get(poiType).size();i++){
                if (this.poiType.get(poiType).get(i).getKey().equals(key)){

                    this.poiType.get(poiType).get(i).addBuilding(building);
                    this.poiType.get(poiType).get(i).addFloor(floor);
                    this.poiType.get(poiType).get(i).addRoom(roomNum, x, y, info);
                    exist = true;
                }
            }
            if (!exist){
                BuildingFloorPoi tmp = new BuildingFloorPoi();
                tmp.addKey(key);
                tmp.addBuilding(building);
                tmp.addFloor(floor);
                tmp.addRoom(roomNum, x, y, info);
                this.poiType.get(poiType).add(tmp);
            }
        } else {
            this.poiType.put(poiType, new ArrayList<BuildingFloorPoi>());
            BuildingFloorPoi tmp = new BuildingFloorPoi();
            tmp.addKey(key);
            tmp.addBuilding(building);
            tmp.addFloor(floor);
            tmp.addRoom(roomNum, x, y, info);
            this.poiType.get(poiType).add(tmp);
        }
    }

    /**
     * remove room
     * input: key, building, floor, roomNum
     * output void 
     */
    public void removeRoom(String poiType, String building, String floor, String roomNum){
        String key = building + floor;
        if (!this.poiType.containsKey(poiType)){
            return;
        }
        for (int i =0; i<this.poiType.get(poiType).size();i++){
            if (this.poiType.get(poiType).get(i).getKey().equals(key)) {
                this.poiType.get(poiType).get(i).removeRoom(roomNum);
            }
        }
    }
    /**
     * remove a floor from poi
     * @param poiType: type of POI
     * @param building: building name
     * @param floor: floor number
     * 
     */
    public void removeFloor(String poiType, String building, String floor){
        String key = building + floor;
        if (!this.poiType.containsKey(poiType)){
            return;
        }
        for (int i =0; i<this.poiType.get(poiType).size();i++){
            if (this.poiType.get(poiType).get(i).getKey().equals(key)) {
                this.poiType.get(poiType).remove(i);
            }
        }
    }

    /**
     * remvoe building
     */

    /**
     * get all rooms with a given poi type in a building 
     * input: poi tupe & & building & floor 
     * output: hashmap of roomNum with coors
     */
    public HashMap<String,ArrayList<Integer>> getAllRoomsCoor(String poiType, String building, String floorNum){
        HashMap<String,ArrayList<Integer>> output = new HashMap<String,ArrayList<Integer>>();
        String key = building + floorNum;
        if (!this.poiType.containsKey(poiType)){
            return null;
        }
        for (int i =0; i<this.poiType.get(poiType).size();i++){
            if (this.poiType.get(poiType).get(i).getKey().equals(key)) {
                ArrayList<RoomCoorPoi> tmp = this.poiType.get(poiType).get(i).getAllRooms();
                for (int j =0; i<tmp.size();i++){
                    output.put(tmp.get(j).getRoomNum(), tmp.get(i).getCoor());
                }
            }
        }
        return output;
    }

    public HashMap <String,ArrayList<BuildingFloorPoi>> returnAll(){
        return this.poiType;
    }
    
    /**
     * get all types of pois
     * input void
     * output ArrayList<String>
     */
    public ArrayList<String> getTypes(){
        ArrayList<String> types = new ArrayList<String>();
        for (String key: this.poiType.keySet()) {
            types.add(key);
        }
        return types;
    }
    public ArrayList<BuildingFloorPoi> getAllRooms(String type){
        if (this.poiType.containsKey(type)){
            return this.poiType.get(type);
        } else {
            return null;
        }
    }
} 