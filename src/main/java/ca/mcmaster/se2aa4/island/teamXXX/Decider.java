package ca.mcmaster.se2aa4.island.teamXXX;

import org.json.JSONArray;
import org.json.JSONObject;

public class Decider {

    //Stages of Mission
    private enum Stage {
        FIND_ISLAND, EXPLORE_ISLAND, GO_TO_SITE, CALCULATE_CLOSEST_CREEK, END;
    }
    private Stage stage = Stage.FIND_ISLAND;

    //Different Task(actions)
    public enum Task {
        START, FLY, LEFT, RIGHT, SCAN, RADAR_FRONT, RADAR_LEFT, RADAR_RIGHT, STOP, RADAR
    }
    private Task task = Task.START;

    //Compass class keeps track of direction
    private Compass compass = new Compass();

    //map keeps track of current coordinate
    private Coordinate map = new Coordinate();
    public String pos_msg = "0-0-E-5-5-FIND_ISLAND";

    //class for important points
    private Points points = new Points();

    //State keeps record of return values of 
    private State state = new State();

    /*
    //private String c = "c";
    int xdif = 5;
    int ydif = 5;
    */ //TO REMOVE

    private int debug2 = 0;
    

    //decide function sends an action to Explorer class
    public String[][] decide() {

        if (stage == Stage.FIND_ISLAND) {
            findIsland2();
            return getCommand();
        } else if (stage == Stage.EXPLORE_ISLAND) {
            patrol();
            return getCommand();
        } else if (stage == Stage.GO_TO_SITE) {
            debug2 += 1;
            //goTo();
            //if (debug2 % 2 == 0 && debug2 > 20) {
            //    task = Task.SCAN;
            //}
            if (debug2 == 50) { //35, 42
                task = Task.STOP;
            }
            return getCommand();
        } else if (stage == Stage.CALCULATE_CLOSEST_CREEK) {
            //String closest_creek = closestCreek();
            String closest_creek = points.closestCreek();
            if (closest_creek == null) {
                pos_msg = "NO CREEK FOUND";
            } else {
                //int[] creek_location = location_of_creeks.get(closest_creek);
                int[] creek_location = points.getCreekLocation(closest_creek);
                int[] site_location = points.getSiteLocation();
                pos_msg = "Site:"+site_location[0]+"-"+site_location[1]+
                " / Closest Creek:"+creek_location[0]+"-"+creek_location[1]+":"+closest_creek;
            }
            
            task = task.STOP;
            return getCommand();
        } else {
            task = Task.STOP;
            return getCommand();
        }
        
    }

    //##################################################################################################
    //####################### Finding Island ###########################################################
    //##################################################################################################

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
    

    public void findIsland2() {
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
        //return getCommand(); TO REMOVE
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

    //private int debug = 0; //DEBUG   TO REMOVE

    //patrol function controls how to explore island
    public void patrol() {

        /* TO REMOVE
        debug += 1;
        if (debug == 3000) {

            //error at 1240 for map 10

            task = Task.STOP;
        } else 
        */
        
        if (notFoundIn == 3) { //a

            /*
            //task = Task.STOP;         //a
            setAlignmentAlorithm();
            stage = Stage.GO_TO_SITE;
            goTo();
            */
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
        //return getCommand(); TO REMOVE
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
                if (found.equals("GROUND")) {
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
    //################ Going to Site ###################################################################
    //##################################################################################################

    /*

    int[] destination = {5, 5};
    
    //function sets the algorithm for turns so you face the correct direction for y travel or skips Y travel 
    private void setAlignmentAlorithm() {
        int x_difference = destination[0]-map.getPosition()[0];
        int y_difference = destination[1]-map.getPosition()[1];

        xdif = x_difference; ydif = y_difference;

            if (y_difference < 0) { //to the north
                switch (facing) {
                    case N:
                        goToStep = GoToSteps.Y_Travel; break;
                    case E:
                        properLeftAlgorithm(); break;
                    case S:
                        turnAroundAlgorithm(); break;
                    case W:
                        properRightAlgorithm(); break;
                }
            } else if (y_difference > 0) { //to the south
                switch (facing) {
                    case N:
                        c = "w1"; 
                        turnAroundAlgorithm(); break;
                    case E:
                        properRightAlgorithm(); break;
                    case S:
                        goToStep = GoToSteps.Y_Travel; break;
                    case W:
                        properLeftAlgorithm(); break;
                }
            } else { //on same y
                
            }
    }
    
    private enum GoToSteps {
        Y_Alignment, X_Travel, Y_Travel
    }
    private GoToSteps goToStep = GoToSteps.Y_Alignment;
    private Task[] Y_Alignment_Algo;
    private int alignCounter = 0;
    public void turnAroundAlgorithm() {
        Y_Alignment_Algo = new Task[] {Task.LEFT, Task.RIGHT, Task.RIGHT, Task.RIGHT, Task.FLY, Task.FLY};
    }
    public void properLeftAlgorithm() {
        Y_Alignment_Algo = new Task[] {Task.FLY, Task.RIGHT, Task.RIGHT, Task.RIGHT, Task.FLY, Task.FLY};
    }
    public void properRightAlgorithm() {
        Y_Alignment_Algo = new Task[] {Task.FLY, Task.LEFT, Task.LEFT, Task.LEFT, Task.FLY, Task.FLY};
    }

    //Go To feature travels first in y direction, then x direction
    private void goTo() {
        int x_difference = destination[0]-map.getPosition()[0];
        int y_difference = destination[1]-map.getPosition()[1];

        xdif = x_difference; ydif = y_difference;

        if (goToStep == GoToSteps.Y_Alignment) { //y alignment
            task = Y_Alignment_Algo[alignCounter];
            alignCounter += 1;
            c=c+alignCounter;
            if (alignCounter == Y_Alignment_Algo.length) {
                alignCounter = 0;
                goToStep = GoToSteps.Y_Travel;
            }
        } else if (goToStep == GoToSteps.Y_Travel) {
            if (y_difference != 1 && y_difference != -1) {
                task = Task.FLY;
            } else {
                if (x_difference < 0) { //left
                    if (facing == Needle.N) { //< ^
                        task = Task.LEFT;
                    } else {
                        task = Task.RIGHT; //< v
                    }
                } else if (x_difference > 0) { //right
                    if (facing == Needle.N) { //^ >
                        task = Task.RIGHT;
                    } else {
                        task = Task.LEFT; //v >
                    }
                } else { //infront
                    task = Task.FLY;
                }
                goToStep = GoToSteps.X_Travel;
            }
        } else if (goToStep == GoToSteps.X_Travel) {
            if (x_difference != 0) {
                task = Task.FLY;
            } else {
                task = Task.STOP;
            }
        }
    }
    */

    //##################################################################################################
    //############ Command Information #################################################################
    //##################################################################################################


    //getCommand function return JSON information to be used in Explorer class
    public String[][] getCommand() {
        int[] position = map.getPosition();

        //pos_msg = ""+map.getPosition()[0]+"-"+map.getPosition()[1]+"-"+xdif+"-"+ydif+
        //        "-"+task.toString()+"-"+facing.toString()+"-"+stage.toString()+"-"+c;

        switch (task) {
            case FLY:
                /*
                pos_msg = ""+map.getPosition()[0]+"-"+map.getPosition()[1]+"-"+xdif+"-"+ydif+
                "-"+task.toString()+"-"+facing.toString()+"-"+stage.toString()+"-"+c;
                */
                map.update(compass.getNeedle(), Task.FLY);
                return new String[][] {{"fly"}};
            case LEFT:
                /*
                pos_msg = ""+map.getPosition()[0]+"-"+map.getPosition()[1]+"-"+xdif+"-"+ydif+
                "-"+task.toString()+"-"+facing.toString()+"-"+stage.toString()+"-"+c;
                */
                map.update(compass.getNeedle(), Task.LEFT);
                compass.turnLeft();
                return new String[][] {{"heading"}, {"direction", compass.getNeedle().toString()}};
            case RIGHT:
                /*
                pos_msg = ""+map.getPosition()[0]+"-"+map.getPosition()[1]+"-"+xdif+"-"+ydif+
                "-"+task.toString()+"-"+facing.toString()+"-"+stage.toString()+"-"+c;
                */
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

    /*
    //calls state class and returns log message
    public String analyseWrapper(JSONObject response) {
        return state.analyse(response, task, map.getPosition());
    }
    */

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

    //analyse function break down response and update state
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
        if (creeks.length() > 0) {
            //location_of_creeks.put(creeks.getString(0), map.getPosition());
            points.addToCreeks(creeks.getString(0), map.getPosition());
        }
        if (sites.length() > 0) {
            //site_location = map.getPosition();
            points.setSiteLocation(map.getPosition());
        }
        String aa = "" + biomes.toString() + creeks.toString() + sites.toString();
        return aa;
    }


}