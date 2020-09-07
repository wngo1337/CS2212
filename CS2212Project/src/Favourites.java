/**
 * @author William Ngo
 */
import java.io.FileWriter;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

// NOTE: You cannot add comments in the json file, or else the parser will not parse properly

/**
 * @author William Ngo (wngo2)
 */

public class Favourites {

	public Favourites() {
	}

	public void addFavourite(String buildingName, String floorName, String roomName) {
		try {
			JSONParser favParser = new JSONParser();
			JSONArray favArray = (JSONArray) favParser.parse(new FileReader("src/favourites.json"));
			// opening the favourites.json file to add to

			JSONObject newFavourite = new JSONObject();
			// preparing the new favourite object to be put in
			JSONObject newDetails = new JSONObject();
			newDetails.put("name", buildingName);
			newDetails.put("floor", floorName);
			newDetails.put("room", roomName);
			newFavourite.put("favourite", newDetails);
			// add key/value pairs to the new favourite

			favArray.add(newFavourite);
			// adding the new favourite to the current file

			BufferedWriter favWriter = new BufferedWriter(new FileWriter("src/favourites.json"));
			favWriter.write(favArray.toJSONString());
			favWriter.newLine();
			favWriter.close();
			// saving the new favourite in the file
			System.out.println("favourite added");
		} catch (Exception e) {
			e.printStackTrace();
		}
	} // end of addFavourite

	public void removeFavourite(String buildingName, String floorName, String roomName) {
		try {
			JSONParser favParser = new JSONParser();
			JSONArray favArray = (JSONArray) favParser.parse(new FileReader("src/favourites.json"));

			for (int i = 0; i < favArray.size(); i++) {
				JSONObject favourite = (JSONObject) favArray.get(i);
				// iterate through the favourites array until we find the fav to remove
				for (Iterator iterator = favourite.keySet().iterator(); iterator.hasNext();) {
					String favouriteHeader = (String) iterator.next();
					// tells us this is the beginning of a favourite object

					JSONObject favouriteInfo = (JSONObject) favourite.get(favouriteHeader);
					// getting all the values for the object

					if (favouriteInfo.get("name").equals(buildingName) && favouriteInfo.get("floor").equals(floorName)
							&& favouriteInfo.get("room").equals(roomName)) {
						// if all keys match, remove the favourite object
						favArray.remove(i);
						BufferedWriter favWriter = new BufferedWriter(new FileWriter("src/favourites.json"));
						favWriter.write(favArray.toJSONString());
						favWriter.newLine();
						favWriter.close();
						System.out.println("favourite removed");
						break;
					}
				}
			}
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<String> getFavourites() {
		// returns an ArrayList with building/room Strings of all objects in favourites.json
		ArrayList<String> buildingRoomPairs = new ArrayList<String>();
		try {
			JSONParser favParser = new JSONParser();
			JSONArray favArray = (JSONArray) favParser.parse(new FileReader("src/favourites.json"));
			// gives a JSONArray filled with JSONObjects that we can iterate over
			for (int i = 0; i < favArray.size(); i++) {
				JSONObject favourite = (JSONObject) favArray.get(i);

				for (Iterator iterator = favourite.keySet().iterator(); iterator.hasNext();) {
					String favouriteHeader = (String) iterator.next();

					JSONObject favouriteInfo = (JSONObject) favourite.get(favouriteHeader);

					StringBuilder pairBuilder = new StringBuilder();
					pairBuilder.append(favouriteInfo.get("name"));
					pairBuilder.append(",");
					pairBuilder.append(favouriteInfo.get("floor"));
					pairBuilder.append(",");
					pairBuilder.append(favouriteInfo.get("room"));
					
					buildingRoomPairs.add(pairBuilder.toString());
					// add the building/room pair to the ArrayList
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buildingRoomPairs;
	} // end of getFavourite

	// below is a testing method for the above functions, uncomment to use
	/*
	public static void main(String[] args) throws Exception {
		Favourites newFav = new Favourites();
		ArrayList<String> pairs = newFav.getFavourites();
		System.out.println("The getFavourites method returned an ArrayList with the following values:");
		for (String pair : pairs) {
			System.out.println(pair);
		}
		// newFav.addFavourite("another building", "another floor", "another room");
		// newFav.addFavourite("building1", "floor1", "room1");
		// newFav.addFavourite("Middlesex College", "floor1", "MC110");
		// newFav.addFavourite("University College", "floor3", "UC9001");

		// JSONObject fav = newFav.getFavourite("Middlesex College", "floor1", "MC110");
		// System.out.println(fav.toJSONString() + " is the favourite we got");
		// JSONObject fav2 = newFav.getFavourite("not", "in", "building");
		// newFav.removeFavourite("building1", "floor1", "room1"); }
	}
	*/
} // end of class