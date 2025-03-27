package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONArray;
import org.json.JSONObject;



public class Decider {

    private enum Stage {
        FIND_ISLAND, EXPLORE_ISLAND, END;
    }
    private Stage stage = Stage.FIND_ISLAND;

    public enum Task {
        START, FLY, LEFT, RIGHT, SCAN, RADAR_FRONT, RADAR_LEFT, RADAR_RIGHT, STOP, RADAR
    }
    private Task task = Task.START;

    public enum Needle {
        N, E, S, W
    }
    private Needle facing = Needle.E;

    private Coordinate map = new Coordinate();

    public String[][] decide() {

        if (stage == Stage.FIND_ISLAND) {
            return findIsland2();
        } else if (stage == Stage.EXPLORE_ISLAND) {
            return patrol();
        } else {
            task = Task.STOP;
            return getCommand();
        }
        
    }

    
    public String[][] findIsland() {
        if ((task == Task.START)) {
            task = Task.RADAR_RIGHT;
        } else if (found.equals("GROUND")) {
            if (task == Task.RADAR_RIGHT) {
                task = Task.RIGHT;
            } else {
                if (range < 0) {
                    task = Task.SCAN;
                    stage = Stage.EXPLORE_ISLAND;
                } else {
                    task = Task.FLY;
                    range -= 1;
                }
            }
        } else {
            if (task == Task.RADAR_RIGHT) {
                task = Task.FLY;
            } else {
                task = Task.RADAR_RIGHT; 
            }
        }
        return getCommand();
    }
    

    public String[][] findIsland2() {
            if ((task == Task.START)||(task == Task.LEFT)) {
                task = Task.RIGHT;
            } else if (task == Task.RIGHT) {
                task = task.RADAR_FRONT;
            } else if (found.equals("GROUND")) {
                if (range < 0) {
                    task = Task.SCAN;
                    stage = Stage.EXPLORE_ISLAND;
                } else {
                    task = Task.FLY;
                    range -= 1;
                }
            } else {
                task = Task.LEFT;
            }
        return getCommand();
    }

    //exploring island
    private boolean lookingDown = true;
    private boolean checkForOcean = true;
    private int notFoundIn = 0;


    private int debug = 0;

    public String[][] patrol() {
        debug += 1;
        if (debug == 3000) {

            //error at 1240 for map 10

            task = Task.STOP;
        } else if (notFoundIn == 3) { //a
            task = Task.STOP;         //a

        } else if (onlyOcean(biomes) && checkForOcean) {
            offIsland();
        } else if ((task == Task.SCAN)) {
            task = Task.FLY;
            notFoundIn = 0; //a
        } else {
            task = Task.SCAN;
            checkForOcean = true;
        }
        return getCommand();
    }

    private int offIslandSteps = 0;
    private void offIsland() {
        switch (offIslandSteps) {
            case 0:
                task = Task.RADAR_FRONT; offIslandSteps += 1; break;
            
            case 1:
                if (found.equals("GROUND")) {
                    if (range < 0) {
                        task = Task.SCAN;
                        offIslandSteps = 0;
                    } else {
                        task = Task.FLY;
                        range -= 1;
                    }
                } else {
                    if (lookingDown) {
                        UturnLeft();
                    } else {
                        UturnRight();
                    }
                }
                break;
            
        }
    }

    private int turnCounter = 0;
    public void UturnRight() {
        Task[] algo = {Task.FLY, Task.FLY, Task.LEFT, Task.LEFT, Task.LEFT, Task.FLY, Task.RIGHT};
        task = algo[turnCounter];
        turnCounter += 1;
        if (task == Task.RIGHT) {
            turnCounter = 0;
            checkForOcean = false;
            lookingDown = true;
            offIslandSteps = 0;
            notFoundIn += 1; //a
        }
    }
    public void UturnLeft() {
        Task[] algo = {Task.FLY, Task.FLY, Task.RIGHT, Task.RIGHT, Task.RIGHT, Task.FLY, Task.LEFT};
        task = algo[turnCounter];
        turnCounter += 1;
        if (task == Task.LEFT) {
            turnCounter = 0;
            checkForOcean = false;
            lookingDown = false;
            offIslandSteps = 0;
            notFoundIn += 1; //a
        }
    }

    private boolean onlyOcean(JSONArray array) {
        for (int i=0; i < array.length(); i++) {
            if (!array.getString(i).equals("OCEAN")) {
                return false;
            }
        }
        return true;
    }

    //#############################################################################

    public String[][] getCommand() {
        switch (task) {
            case FLY:
                map.update(facing, Task.FLY);
                return new String[][] {{"fly"}};
            case LEFT:
                facing = turnLeft(facing);
                map.update(facing, Task.LEFT);
                return new String[][] {{"heading"}, {"direction", facing.toString()}};
            case RIGHT:
                facing = turnRight(facing);
                map.update(facing, Task.RIGHT);
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

    //State

    //analyising the response
    //analyising the response
    private int budget = 1000;
    private String status = "OK";
    private String found = "NOT_IN_RANGE";
    private int count = 0;
    private int range;

    JSONArray biomes;
    JSONArray creeks;
    JSONArray sites;

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
        biomes = extras.getJSONArray("biomes");
        creeks = extras.getJSONArray("creeks");
        sites = extras.getJSONArray("sites");
        String aa = "" + biomes.toString() + creeks.toString() + sites.toString();
        return aa;
    }

}