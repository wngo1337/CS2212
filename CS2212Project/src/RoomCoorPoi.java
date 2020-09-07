/**
 * @author Allen Zhang
 */
import java.util.ArrayList;
import java.util.HashMap;

public class RoomCoorPoi{
    private String roomNum;
    private ArrayList<Integer> coor;
    private HashMap<String,String> roomInfo;

    public RoomCoorPoi(){
        this.roomNum = null;
        this.coor = new ArrayList<Integer>();
        this.roomInfo = new HashMap<String, String>();
    }

    // add roomNum and coor
    public void addRoomCoor(String roomNum, int x, int y){
        this.roomNum = roomNum;
        this.coor.add(x);
        this.coor.add(y);
    }
    // get roomNum
    public String getRoomNum(){
        return this.roomNum;
    }
    // get Coor
    public ArrayList<Integer> getCoor(){
        return this.coor;
    }

    // add room description
    public void addRoomInfo(String roomNum,String description){
        if (this.roomInfo.containsKey(roomNum)){
            this.roomInfo.replace(roomNum,description);
        } else {
            this.roomInfo.put(roomNum, description);
        }
    }

    // get a room description
    public String getRoomInfo(String roomNum){
        if (this.roomInfo.containsKey(roomNum)){
            return this.roomInfo.get(roomNum);
        } else {
            return null;
        }
    }

    // get all room description
    public HashMap<String,String> getAllRoomInfo(){
        return this.roomInfo;
    }
    
    public String getRoomDescription() {
    	return this.roomInfo.get(this.roomNum);
    }
}