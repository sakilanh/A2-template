package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONArray;
import org.json.JSONObject;



public class Decider {

    private enum Stage {
        FIND_ISLAND, END;
    }
    private Stage stage = Stage.FIND_ISLAND;

    private enum Task {
        START, FLY, LEFT, RIGHT, SCAN, RADAR_FRONT, RADAR_LEFT, RADAR_RIGHT, STOP, RADAR
    }
    private Task task = Task.START;

    private enum Needle {
        N, E, S, W
    }
    private Needle facing = Needle.E;

    public String[][] decide() {

        if (stage == Stage.FIND_ISLAND) {
            return findIsland2();
        } else {
            task = Task.STOP;
            return getCommand();
        }
        
    }

    /*
    public String[][] findIsland() {
        if ((task == Task.START)||(task == Task.FLY)) {
            task = Task.RADAR_RIGHT;
        } else if (found.equals("GROUND")) {
            if (task == Task.RADAR_RIGHT) {
                task = Task.RIGHT;
            } else {
                if (count == 0) {
                    task = Task.FLY;
                    count -= 1;
                } else {
                    task = Task.SCAN;
                    stage = Stage.END;
                }
            }
        } else {
            task = task.FLY;
        }
        return getCommand();
    }
    */

    public String[][] findIsland2() {
            if ((task == Task.START)||(task == Task.LEFT)) {
                task = Task.RIGHT;
            } else if (task == Task.RIGHT) {
                task = task.RADAR_FRONT;
            } else if (found.equals("GROUND")) {
                if (range < 0) {
                    task = Task.SCAN;
                    stage = Stage.END;
                } else {
                    task = Task.FLY;
                    range -= 1;
                }
            } else {
                task = Task.LEFT;
            }
        return getCommand();
    }

    //go to function
    //change x and y values
    //

    public String[][] getCommand() {
        switch (task) {
            case FLY:
                return new String[][] {{"fly"}};
            case LEFT:
                facing = turnLeft(facing);
                return new String[][] {{"heading"}, {"direction", facing.toString()}};
            case RIGHT:
                facing = turnRight(facing);
                return new String[][] {{"heading"}, {"direction", facing.toString()}};
            case SCAN:
                return new String[][] {{"scan"}};
            case RADAR_FRONT:
                return new String[][] {{"echo"}, {"direction", facing.toString()}};
            case RADAR_LEFT:
                return new String[][] {{"echo"}, {"direction", turnLeft(facing).toString()}};
            case RADAR_RIGHT:
                return new String[][] {{"echo"}, {"direction", turnRight(facing).toString()}};
            case STOP:
                return new String[][] {{"stop"}};
            default:
                return new String[][] {{"stop"}};
        }
    }

    public Needle turnLeft(Needle current) {
        Needle turned = current;
        switch (current) {
            case N:
                turned = Needle.W; break;
            case E:
                turned = Needle.N; break;
            case S:
                turned = Needle.E; break;
            case W:
                turned = Needle.S; break;
        }
        return turned;
    }
    public Needle turnRight(Needle current) {
        Needle turned = current;
        switch (current) {
            case N:
                turned = Needle.E; break;
            case E:
                turned = Needle.S; break;
            case S:
                turned = Needle.W; break;
            case W:
                turned = Needle.N; break;
        }
        return turned;
    }

    //analyising the response
    //analyising the response
    private int budget = 1000;
    private String status = "OK";
    private String found = null;
    //private int count = 0;
    private int range;
    public String analyse(JSONObject response) {
        budget -= response.getInt("cost");
        if (task == Task.RADAR_FRONT) {
            return analyseRadar(response.getJSONObject("extras"));
        } else if (task == Task.SCAN) {
            return analyseScan(response.getJSONObject("extras"));
        }
        return "na";
    }

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