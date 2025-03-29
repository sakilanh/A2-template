package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONArray;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.teamXXX.Decider.Task;

public class State {

    /*

    //found feature is for radar command
    private enum Found {
        NOT_IN_RANGE, GROUND
    }
    private Found found = Found.NOT_IN_RANGE;
    private int range;

    //return wether land was found
    public boolean land_found() {
        if (found == Found.GROUND) {
            return true;
        } else {
            return false;
        }
    }

    //stores output of scan command
    JSONArray biomes;
    JSONArray creeks;
    JSONArray sites;

    //analyse function breaks down response and updates state, return String for logging
    public String analyse(JSONObject response, Task task, int[] location) {
        if (task == Task.RADAR_FRONT || task == Task.RADAR_LEFT || task == Task.RADAR_RIGHT) {
            return analyseRadar(response.getJSONObject("extras"));
        } else if (task == Task.SCAN) {
            //return analyseScan(response.getJSONObject("extras"));
            return "i";
        } else {
            return "na";
        }
    }

    //analyse radar
    private String analyseRadar(JSONObject extras) {
        range = extras.getInt("range");
        if (extras.getString("found").equals("GROUND")) {
            found = Found.GROUND;
        } else {
            found = Found.NOT_IN_RANGE;
        }
        String logString = "" + range + " " + found;
        return logString;
    }

    //analyse scan
    private String analyseScan(JSONObject extras, int[] location) {
        biomes = extras.getJSONArray("biomes");
        creeks = extras.getJSONArray("creeks");
        sites = extras.getJSONArray("sites");
        if (creeks.length() > 0) {
            location_of_creeks.put(creeks.getString(0), map.getPosition());
        }
        if (sites.length() > 0) {
            site_location = map.getPosition();
        }
        String logString = "" + biomes.toString() + creeks.toString() + sites.toString();
        return logString;
    }
    */
    
    
}