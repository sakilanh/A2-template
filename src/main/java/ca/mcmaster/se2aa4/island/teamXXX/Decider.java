package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONArray;
import org.json.JSONObject;

public class Decider {

    //Stages of Mission
    private enum Stage {
        FIND_ISLAND, EXPLORE_ISLAND, CALCULATE_CLOSEST_CREEK;
    }
    //current stage
    private Stage stage = Stage.FIND_ISLAND;

    //Different Task(actions)
    public enum Task {
        START, FLY, LEFT, RIGHT, SCAN, RADAR_FRONT, RADAR_LEFT, RADAR_RIGHT, STOP, RADAR
    }
    //current task
    private Task task = Task.START;

    //Compass class keeps track of direction
    private Compass compass = new Compass();

    //map keeps track of current coordinate
    private Coordinate map = new Coordinate();

    //logging information
    private String pos_msg = "0-0-E-5-5-FIND_ISLAND";

    public String getMessage() {
        return pos_msg;
    }

    //class for important points
    private Points points = new Points();

    //decide function sends an action to Explorer class
    public String[][] decide() {

        //first stage find island
        if (stage == Stage.FIND_ISLAND) {
            findIsland();
            return getCommand();

        //second stage explore island
        } else if (stage == Stage.EXPLORE_ISLAND) {
            patrol();
            return getCommand();

        //last stage find closest creek and return along with site info
        } else if (stage == Stage.CALCULATE_CLOSEST_CREEK) {
            String closest_creek = points.closestCreek();
            if (closest_creek == null) {
                pos_msg = "NO CREEK FOUND";
            } else {
                int[] creek_location = points.getCreekLocation(closest_creek);
                int[] site_location = points.getSiteLocation();
                pos_msg = "Site:"+site_location[0]+"-"+site_location[1]+":"+points.getSiteId()+
                " / Closest Creek:"+creek_location[0]+"-"+creek_location[1]+":"+closest_creek;
            }
            
            task = task.STOP;
        } else {
            task = Task.STOP;
        }
        return getCommand();
        
    }

    //##################################################################################################
    //####################### Finding Island ###########################################################
    //##################################################################################################
    
    public void findIsland() {

            //if you are starting or went left, go right
            if ((task == Task.START)||(task == Task.LEFT)) {
                task = Task.RIGHT;

            //if you went right, scan infront
            } else if (task == Task.RIGHT) {
                task = task.RADAR_FRONT;

            //if you find land, go to it, and then move to next stage
            } else if (land_found()) {
                if (range < 0) {
                    task = Task.SCAN;
                    stage = Stage.EXPLORE_ISLAND;
                } else {
                    task = Task.FLY;
                    range -= 1;
                }

            //otherwise go left
            } else {
                task = Task.LEFT;
            }
    }

    //##################################################################################################
    //####################### Exploring Island #########################################################
    //##################################################################################################

    //variable to know wether you are facing north or south
    private boolean lookingDown = true;

    //variable wether to check if you are in ocean
    private boolean checkForOcean = true;

    //counter to see how many times you do not see land
    private int notFoundIn = 0;

    //patrol function controls how to explore island
    public void patrol() {
        
        //if you fail finding land 2 times, move to next stage
        if (notFoundIn == 3) { //a
           stage = Stage.CALCULATE_CLOSEST_CREEK;
           decide();

        //if you see ocean tile and are looking for ocean
        } else if (hasOcean(biomes) && checkForOcean) {
            offIsland();

        //if you scanned, move forward
        } else if ((task == Task.SCAN)) {
            task = Task.FLY;
            notFoundIn = 0; //a

        //otherwise scan and check for ocean
        } else {
            task = Task.SCAN;
            checkForOcean = true;
        }
    }

    //OffIsland function defines what to do when you leave island
    private int offIslandSteps = 0;
    private void offIsland() {
        switch (offIslandSteps) {

            //on the first step, radar infront
            case 0:
                task = Task.RADAR_FRONT; offIslandSteps += 1; break;
            
            case 1:
                //if you see ground, fly to it
                if (land_found()) {
                
                    if (range < 0) {
                        task = Task.SCAN;
                        offIslandSteps = 0;
                    } else {
                        task = Task.FLY;
                        range -= 1;
                    }

                //if you do not see ground, u turn depending on if you are looking up or down
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

    //functions to control U turns
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

    //hasOcean function that checks if you are on a ocean tile
    private boolean hasOcean(JSONArray array) {
        for (int i=0; i < array.length(); i++) {
            if (array.getString(i).equals("OCEAN")) {
                return true;
            }
        }
        return false;
    }


    //##################################################################################################
    //############ Command Information #################################################################
    //##################################################################################################


    //getCommand function return JSON information to be used in Explorer class
    public String[][] getCommand() {
        int[] position = map.getPosition();

        switch (task) {
            case FLY:
                map.update(compass.getNeedle(), Task.FLY);
                return new String[][] {{"fly"}};
            case LEFT:
                map.update(compass.getNeedle(), Task.LEFT);
                compass.turnLeft();
                return new String[][] {{"heading"}, {"direction", compass.getNeedle().toString()}};
            case RIGHT:
                map.update(compass.getNeedle(), Task.RIGHT);
                compass.turnRight();
                return new String[][] {{"heading"}, {"direction", compass.getNeedle().toString()}};
            case SCAN:
                return new String[][] {{"scan"}};
            case RADAR_FRONT:
                return new String[][] {{"echo"}, {"direction", compass.getNeedle().toString()}};
            case RADAR_LEFT:
                return new String[][] {{"echo"}, {"direction", compass.getLeft()}};
            case RADAR_RIGHT:
                return new String[][] {{"echo"}, {"direction", compass.getRight()}};
            case STOP:
                return new String[][] {{"stop"}};
            default:
                return new String[][] {{"stop"}};
        }
    }

    //##################################################################################################
    //####################### Analysing Response and Updating ##########################################
    //##################################################################################################

    //found feature is for radar command
    private enum Found {
        NOT_IN_RANGE, GROUND
    }
    private Found found = Found.NOT_IN_RANGE;
    private int range;

    //return whether land was found
    public boolean land_found() {
        if (found == Found.GROUND) {
            return true;
        } else {
            return false;
        }
    }

    //ouput of scan
    JSONArray biomes;
    JSONArray creeks;
    JSONArray sites;

    //analyse function break down response and update state
    public String analyse(JSONObject response) {
        if (task == Task.RADAR_FRONT) {
            return analyseRadar(response.getJSONObject("extras"));
        } else if (task == Task.SCAN) {
            return analyseScan(response.getJSONObject("extras"));
        }
        return "na";
    }

    //analysing radar
    private String analyseRadar(JSONObject extras) {
        range = extras.getInt("range");
        if (extras.getString("found").equals("GROUND")) {
            found = Found.GROUND;
        } else {
            found = Found.NOT_IN_RANGE;
        }

        String logString = "" + range + " " + found.toString();
        return logString;
    }

    //analysing scan
    private String analyseScan(JSONObject extras) {
        biomes = extras.getJSONArray("biomes");
        creeks = extras.getJSONArray("creeks");
        sites = extras.getJSONArray("sites");

        //if you find sites or creeks, update the points
        if (creeks.length() > 0) {
            points.addToCreeks(creeks.getString(0), map.getPosition());
        }
        if (sites.length() > 0) {
            points.setSiteLocation(map.getPosition(), sites.getString(0));
        }

        String logString = "" + biomes.toString() + creeks.toString() + sites.toString();
        return logString;
    }


}