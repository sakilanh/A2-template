package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONArray;
import org.json.JSONObject;

import ca.mcmaster.se2aa4.island.teamXXX.Decider.Task;

public class State {
    private int budget = 1000;
    private String status = "OK";
    private String found = null;
    private int distance;
    private int range;

    /*
    public String update(JSONObject response) {
        budget -= response.getInt("cost");
        status = response.getString("status");
        if (task == Task.RADAR_FRONT) {
            return analyseRadar(response.getJSONObject("extras"));
        } else if (task == Task.SCAN) {
            return analyseScan(response.getJSONObject("extras"));
        }
        return "na";
    }
    */

    private String analyseRadar(JSONObject extras) {
        range = extras.getInt("range");
        found = extras.getString("found");
        String aa = "" + range + " " + found;
        return aa;
    }
    private String analyseScan(JSONObject extras) {
        JSONArray biomes = extras.getJSONArray("biomes");
        JSONArray creeks = extras.getJSONArray("creeks");
        JSONArray sites = extras.getJSONArray("sites");
        String aa = "" + biomes.toString() + creeks.toString() + sites.toString();
        return aa;
    }
}