package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.HashMap;
import java.util.Map;


public class Points {

    //location of emergency site
    private int[] site_location = {0, 0};

    //site id
    private String site_id;
    
    //key is creek id, value is array of location
    private Map<String, int[]> location_of_creeks = new HashMap<>();

    //set location of site
    public void setSiteLocation(int[] loc, String id) {
        site_location = loc;
        site_id = id;
    }

    //return location of site
    public int[] getSiteLocation() {
        return site_location.clone();
    }

    //return site id
    public String getSiteId() {
        return site_id;
    }

    //add to set of creeks
    public void addToCreeks(String key, int[] value) {
        location_of_creeks.put(key, value);
    }

    //return location of a creek
    public int[] getCreekLocation(String key) {
        return location_of_creeks.get(key);
    }

    //return the key of the closest creek from all creeks or return null
    public String closestCreek() {
        String creek_index = null;
        double shortest_distance = -1;

        //go through all keys and get the location of the creek
        for (String key : location_of_creeks.keySet()) {
            int[] creek_location = location_of_creeks.get(key);

            //call function to find distance than update if it the shortest so far
            double d = distance(creek_location[0], creek_location[1], site_location[0], site_location[1]);
            if (shortest_distance == -1) {
                creek_index = key;
                shortest_distance = d;
            } else {
                if (d < shortest_distance) {
                    creek_index = key;
                    shortest_distance = d;
                }
            }
        }
        return creek_index;
    }

    //calculate distance between 2 points
    private double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt( Math.pow((x2-x1), 2) + Math.pow((y2-y1), 2) ); //sqrt( (x2-x1)^2 + (y2-y1)^2 )
    }
}